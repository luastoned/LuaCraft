package com.luacraft.meta.client;

import net.minecraft.client.resources.model.ModelResourceLocation;

import com.luacraft.LuaCraftState;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

public class LuaModelResource {
	public static JavaFunction __tostring = new JavaFunction()
	{
		public int invoke(LuaState l)
		{
			ModelResourceLocation self = (ModelResourceLocation) l.checkUserdata(1, ModelResourceLocation.class, "ModelResource");
			l.pushString(String.format("ModelResource: 0x%08x", l.toPointer(1)));
			return 1;
		}
	};

	public static void Init(final LuaCraftState l)
	{		
		l.newMetatable("ModelResource");
		{
			l.pushValue(-1);
			l.setField(-2, "__index");
			
			l.pushJavaFunction(__tostring);
			l.setField(-2, "__tostring");
			
			l.newMetatable("Resource");
			l.setField(-2, "__basemeta");
		}
		l.pop(1);
	}
}
