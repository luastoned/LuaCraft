package com.luacraft.classes;

import java.io.File;

import com.luacraft.LuaCraft;
import com.luacraft.LuaCraftState;
import com.naef.jnlua.LuaUserdata;

import net.minecraftforge.fml.relauncher.Side;

public class LuaJavaThread extends Thread implements LuaUserdata
{
	private LuaCraftState l = null;
	private File file = null;

	public LuaJavaThread(LuaCraftState state, String f)
	{
		l = new LuaCraftState();
		file = new File(f);

		if (state.getSide() == Side.CLIENT)
			LuaCraft.clientLibs.Initialize(l);
		else
			LuaCraft.serverLibs.Initialize(l);
		
		l.setSideOverride(state.getSide());
	}

	public void run()
	{
		if (l.isOpen())
		{
			try {
				l.includeFile(file);
			} catch (Exception e) {
				l.handleException(e);
			} finally {
				l.close();
			}
		}
	}

	public String getTypeName() {
		return "Thread";
	}
}