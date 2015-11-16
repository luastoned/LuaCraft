package com.luacraft.library.server;

import java.io.File;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.PropertyManager;
import net.minecraft.world.World;

import com.luacraft.LuaCraft;
import com.luacraft.LuaCraftState;
import com.luacraft.LuaUserdataManager;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

public class LuaGlobals {
	private static MinecraftServer server = null;

	/**
	 * @author Jake
	 * @function PropertyManager Returns a new [[PropertyManager]] for the given
	 *           file
	 * @arguments nil
	 * @return [[PropertyManager]]:manager
	 */

	public static JavaFunction PropertyManager = new JavaFunction() {
		public int invoke(LuaState l) {
			File file = new File(LuaCraft.rootDir, l.checkString(1));
			PropertyManager property = new PropertyManager(file);
			l.pushUserdataWithMeta(property, "PropertyManager");
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function World Get the world for the given dimensionID
	 * @arguments [[Number]]:dimension
	 * @return [[World]]:world
	 */

	public static JavaFunction World = new JavaFunction() {
		public int invoke(LuaState l) {
			World world = server.worldServers[l.checkInteger(1, 0)];
			LuaUserdataManager.PushUserdata(l, world);
			return 1;
		}
	};

	public static void Init(final LuaCraftState l) {
		server = l.getMinecraftServer();

		l.pushJavaFunction(PropertyManager);
		l.setGlobal("PropertyManager");
		l.pushJavaFunction(World);
		l.setGlobal("World");
	}
}
