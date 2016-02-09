package com.luacraft.meta;

import com.luacraft.LuaUserdata;
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

	/**
	 * @author Jake
	 * @function Empty
	 * @info Tests if this stack is empty.
	 * @arguments nil
	 * @return [[Boolean]]:empty
	 */

	public static JavaFunction Empty = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaJavaChannel self = (LuaJavaChannel) l.checkUserdata(1, LuaJavaChannel.class, "Channel");
			l.pushBoolean(self.empty());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function Empty
	 * @info Looks at the object at the top of this stack without removing it from the stack.
	 * @arguments nil
	 * @return [[Object]]:obj
	 */

	public static JavaFunction Peek = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaJavaChannel self = (LuaJavaChannel) l.checkUserdata(1, LuaJavaChannel.class, "Channel");
			self.peek(l);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function Pop
	 * @info Removes the object at the top of this stack and returns that object as the value of this function.
	 * @arguments nil
	 * @return [[Object]]:obj
	 */

	public static JavaFunction Pop = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaJavaChannel self = (LuaJavaChannel) l.checkUserdata(1, LuaJavaChannel.class, "Channel");
			self.pop(l);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function Push
	 * @info Pushes an item onto the top of this stack.
	 * @arguments [[Object]]:obj
	 * @return nil
	 */

	public static JavaFunction Push = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaJavaChannel self = (LuaJavaChannel) l.checkUserdata(1, LuaJavaChannel.class, "Channel");
			self.push(l, 2);
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function Search
	 * @info Returns the 1-based position from the top of the stack where the object is located; the return value -1 indicates that the object is not on the stack.
	 * @arguments [[Object]]:obj
	 * @return [[Number]]:position
	 */

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
			l.pushJavaFunction(__tostring);
			l.setField(-2, "__tostring");

			LuaUserdata.SetupBasicMeta(l);
			LuaUserdata.SetupMeta(l, false);

			l.newMetatable("Object");
			l.setField(-2, "__basemeta");

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
