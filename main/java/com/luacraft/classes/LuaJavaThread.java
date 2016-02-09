package com.luacraft.classes;

import com.naef.jnlua.LuaUserdata;

public class LuaJavaThread extends Thread implements LuaUserdata {

	@Override
	public String getTypeName() {
		return "Thread";
	}

}
