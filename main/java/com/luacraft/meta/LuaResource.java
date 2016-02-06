package com.luacraft.meta;

import com.luacraft.LuaCraftState;
import com.luacraft.LuaUserdata;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

import net.minecraft.util.ResourceLocation;

public class LuaResource {
	public static JavaFunction __tostring = new JavaFunction() {
		public int invoke(LuaState l) {
			ResourceLocation self = (ResourceLocation) l.checkUserdata(1, ResourceLocation.class, "Resource");
			l.pushString(String.format("Resource: 0x%08x", l.toPointer(1)));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetDomain
	 * @info Gets the domain of the resource
	 * @arguments nil
	 * @return [[String]]:domain
	 */

	public static JavaFunction GetDomain = new JavaFunction() {
		public int invoke(LuaState l) {
			ResourceLocation self = (ResourceLocation) l.checkUserdata(1, ResourceLocation.class, "Resource");
			l.pushString(self.getResourceDomain());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetPath
	 * @info Gets the path of the resource
	 * @arguments nil
	 * @return [[String]]:path
	 */

	public static JavaFunction GetPath = new JavaFunction() {
		public int invoke(LuaState l) {
			ResourceLocation self = (ResourceLocation) l.checkUserdata(1, ResourceLocation.class, "Resource");
			l.pushString(self.getResourcePath());
			return 1;
		}
	};

	public static void Init(final LuaCraftState l) {
		l.newMetatable("Resource");
		{
			l.pushJavaFunction(__tostring);
			l.setField(-2, "__tostring");
			
			LuaUserdata.SetupBasicMeta(l);

			l.newMetatable("Object");
			l.setField(-2, "__basemeta");

			l.pushJavaFunction(GetDomain);
			l.setField(-2, "GetDomain");
			l.pushJavaFunction(GetPath);
			l.setField(-2, "GetPath");
		}
		l.pop(1);
	}
}
