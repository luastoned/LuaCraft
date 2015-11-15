package com.luacraft.library;

import java.util.HashMap;

import com.luacraft.LuaCraftState;
import com.luacraft.classes.LuaJavaChannel;
import com.luacraft.classes.LuaJavaThread;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

public class LuaLibThread
{
	private static HashMap<String, LuaJavaChannel> channels = new HashMap<String, LuaJavaChannel>();	

	/**
	 * @author Jake
	 * @library thread
	 * @function NewThread
	 * Create a new thread
	 * @arguments [[String]]:filename
	 * @return [[Thread]]:thread
	 */

	public static JavaFunction NewThread = new JavaFunction()
	{
		public int invoke(LuaState l)
		{
			String file = l.checkString(1);
			LuaJavaThread thread = new LuaJavaThread((LuaCraftState) l, file);
			thread.setName(file);
			l.pushUserdataWithMeta(thread, "Thread");
			return 0;
		}
	};
	
	/**
	 * @author Jake
	 * @library thread
	 * @function CreateChannel
	 * Create a new channel
	 * @arguments nil
	 * @return [Thread]:thread
	 */

	public static JavaFunction GetChannel = new JavaFunction()
	{
		public int invoke(LuaState l)
		{
			String name = l.checkString(1);
			LuaJavaChannel channel = channels.get(name);
			
			if (channel == null) {
				channel = new LuaJavaChannel();
				channels.put(name, channel);
			}
			
			l.pushUserdataWithMeta(channel, "Channel");
			return 1;
		}
	};

	public static void Init(LuaState l)
	{
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