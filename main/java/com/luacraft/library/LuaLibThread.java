package com.luacraft.library;

import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraftforge.fml.relauncher.Side;

import com.luacraft.LuaCraft;
import com.luacraft.LuaCraftState;
import com.luacraft.classes.LuaJavaChannel;
import com.luacraft.classes.LuaJavaThread;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

public class LuaLibThread {

	/**
	 * @author Jake
	 * @library thread
	 * @function NewThread
	 * @info Create a new thread
	 * @arguments [[String]]:filename
	 * @return [[Thread]]:thread
	 */

	public static JavaFunction NewThread = new JavaFunction() {
		public int invoke(LuaState l) {
			String file = l.checkString(1);
			LuaJavaThread thread = new LuaJavaThread((LuaCraftState) l, file);
			thread.setName(file);
			l.pushUserdataWithMeta(thread, "Thread");
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @library thread
	 * @function GetChannel
	 * @info Create a new channel
	 * @arguments [[String]]:name
	 * @return [[Channel]]:channel
	 */

	public static JavaFunction GetChannel = new JavaFunction() {
		public int invoke(LuaState l) {
			synchronized (LuaCraft.threadChannels) {
				String name = l.checkString(1);
				LuaJavaChannel channel = LuaCraft.threadChannels.get(name);

				if (channel == null) {
					channel = new LuaJavaChannel();
					LuaCraft.threadChannels.put(name, channel);
				}

				l.pushUserdataWithMeta(channel, "Channel");
				return 1;
			}
		}
	};

	public static void Init(LuaState l) {
		l.newTable();
		{
			l.pushJavaFunction(NewThread);
			l.setField(-2, "NewThread");
			l.pushJavaFunction(GetChannel);
			l.setField(-2, "GetChannel");
		}
		l.setGlobal("thread");
	}
}