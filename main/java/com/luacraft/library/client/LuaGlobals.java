package com.luacraft.library.client;

import com.luacraft.LuaCraft;
import com.luacraft.LuaCraftState;
import com.luacraft.LuaUserdata;
import com.luacraft.classes.LuaJavaBlock;
import com.luacraft.classes.Vector;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

import net.minecraft.client.Minecraft;

public class LuaGlobals {
	private static Minecraft client = LuaCraft.getClient();

	/**
	 * @author Gregor
	 * @function Block
	 * @info Get the Block at specified coordinates
	 * @arguments nil
	 * @return [[Block]]:block
	 */

	public static JavaFunction Block = new JavaFunction() {
		public int invoke(LuaState l) {
			int x, y, z;

			if (l.isUserdata(1, Vector.class)) {
				Vector thisVec = (Vector) l.checkUserdata(1, Vector.class, "Vector");
				x = (int) Math.floor(thisVec.x);
				y = (int) Math.floor(thisVec.y);
				z = (int) Math.floor(thisVec.z);
			} else {
				x = (int) Math.floor(l.checkNumber(1, 0));
				y = (int) Math.floor(l.checkNumber(2, 0));
				z = (int) Math.floor(l.checkNumber(3, 0));
			}

			LuaJavaBlock thisBlock = new LuaJavaBlock(client.world, x, z, y);
			LuaUserdata.PushUserdata(l, thisBlock);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function World
	 * @info Get the world that the local player is currently in
	 * @arguments nil
	 * @return [[World]]:world
	 */

	public static JavaFunction World = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaUserdata.PushUserdata(l, client.world);
			return 1;
		}
	};

	/**
	 * @author Matt
	 * @function LocalPlayer
	 * @info Get your local player object
	 * @arguments nil
	 * @return [[Player]]:localplayer
	 */

	public static JavaFunction LocalPlayer = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaUserdata.PushUserdata(l, client.player);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function ModelResource
	 * @info Creates a new [[ModelResource]] object
	 * @arguments [[String]]:path, [ [[String]]:modid ]
	 * @return [[ModelResource]]:resource
	 */

	@Deprecated
	public static JavaFunction ModelResource = new JavaFunction() {
		public int invoke(LuaState l) {
			if (l.getTop() > 1)
				l.pushUserdataWithMeta(new Object(), "ModelResource");
			else
				l.pushUserdataWithMeta(new Object(), "ModelResource");
			return 1;
		}
	};

	public static void Init(final LuaCraftState l) {
		l.pushJavaFunction(Block);
		l.setGlobal("Block");
		l.pushJavaFunction(World);
		l.setGlobal("World");
		l.pushJavaFunction(LocalPlayer);
		l.setGlobal("LocalPlayer");
		l.pushJavaFunction(ModelResource);
		l.setGlobal("ModelResource");

		/**
		 * @author Jake
		 * @function EyeAngles
		 * @info Get your local player's camera position
		 * @arguments nil
		 * @return [[Vector]]:camera
		 */

		l.pushJavaFunction(LuaLibRender.EyeAngles);
		l.setGlobal("EyeAngles");

		/**
		 * @author Jake
		 * @function EyePos
		 * @info Get your local player's camera position
		 * @arguments nil
		 * @return [[Vector]]:camera
		 */

		l.pushJavaFunction(LuaLibRender.EyePos);
		l.setGlobal("EyePos");

		/**
		 * @author Jake
		 * @function GetViewEntity
		 * @info Returns the entity the camera is using to see with
		 * @arguments nil
		 * @return [[Entity]]:view
		 */

		l.pushJavaFunction(LuaLibRender.GetViewEntity);
		l.setGlobal("GetViewEntity");

		/**
		 * @author Matt
		 * @function ScrW
		 * @info Get the screen width
		 * @arguments nil
		 * @return [[Number]]:width
		 */

		l.pushJavaFunction(LuaLibRender.ScrW);
		l.setGlobal("ScrW");

		/**
		 * @author Matt
		 * @function ScrH
		 * @info Get the screen height
		 * @arguments nil
		 * @return [[Number]]:height
		 */

		l.pushJavaFunction(LuaLibRender.ScrH);
		l.setGlobal("ScrH");
	}
}