package com.luacraft.classes;

import com.naef.jnlua.LuaType;

public class LuaChannelObject
{
	public LuaType type;
	public Object object;
	public String meta;
	
	public LuaChannelObject(LuaType t, Object o)
	{
		type = t;
		object = o;
	}

	public LuaChannelObject(LuaType t, Object o, String m)
	{
		this(t, o);
		meta = m;
	}
}
