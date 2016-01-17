package com.luacraft.meta;

import com.luacraft.classes.LuaJavaChannel;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

public class LuaChannel {
	public static JavaFunction __tostring = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaJavaChannel self = (LuaJavaChannel) l.checkUserdata(1, LuaJavaChannel.class, "Channel");
			l.pushString(String.format("Channel: 0x%08x", l.toPointer(1)));
			return 1;
		}
	};

	public static JavaFunction Empty = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaJavaChannel self = (LuaJavaChannel) l.checkUserdata(1, LuaJavaChannel.class, "Channel");
			l.pushBoolean(self.empty());
			return 1;
		}
	};

	public static JavaFunction Peek = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaJavaChannel self = (LuaJavaChannel) l.checkUserdata(1, LuaJavaChannel.class, "Channel");
			self.peek(l);
			return 1;
		}
	};

	public static JavaFunction Pop = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaJavaChannel self = (LuaJavaChannel) l.checkUserdata(1, LuaJavaChannel.class, "Channel");
			self.pop(l);
			return 1;
		}
	};

	public static JavaFunction Push = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaJavaChannel self = (LuaJavaChannel) l.checkUserdata(1, LuaJavaChannel.class, "Channel");
			self.push(l, 2);
			return 0;
		}
	};

	public static JavaFunction Search = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaJavaChannel self = (LuaJavaChannel) l.checkUserdata(1, LuaJavaChannel.class, "Channel");
			l.pushInteger(self.Search(l, 2));
			return 1;
		}
	};

	public static void Init(LuaState l) {
		l.newMetatable("Channel");
		{
			l.pushValue(-1);
			l.setField(-2, "__index");

			l.pushJavaFunction(__tostring);
			l.setField(-2, "__tostring");

			l.pushJavaFunction(Empty);
			l.setField(-2, "Empty");
			l.pushJavaFunction(Peek);
			l.setField(-2, "Peek");
			l.pushJavaFunction(Pop);
			l.setField(-2, "Pop");
			l.pushJavaFunction(Push);
			l.setField(-2, "Push");
			l.pushJavaFunction(Search);
			l.setField(-2, "Search");
		}
		l.pop(1);
	}
}
