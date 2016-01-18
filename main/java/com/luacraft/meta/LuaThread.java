package com.luacraft.meta;

import com.luacraft.classes.LuaJavaThread;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

public class LuaThread {
	public static JavaFunction __tostring = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaJavaThread self = (LuaJavaThread) l.checkUserdata(1, LuaJavaThread.class, "Thread");
			l.pushString(String.format("Thread: 0x%08x", l.toPointer(1)));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function SetPriority
	 * @info Sets the [[Thread]]s priority
	 * @arguments [[ENUM:THREAD_PRIORITY_*]]:priority
	 * @return nil
	 */

	public static JavaFunction SetPriority = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaJavaThread self = (LuaJavaThread) l.checkUserdata(1, LuaJavaThread.class, "Thread");
			self.setPriority(l.checkInteger(2));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function GetPriority
	 * @info Gets the [[Thread]]s priority
	 * @arguments nil
	 * @return [[ENUM:THREAD_PRIORITY_*]]:priority
	 */

	public static JavaFunction GetPriority = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaJavaThread self = (LuaJavaThread) l.checkUserdata(1, LuaJavaThread.class, "Thread");
			l.pushNumber(self.getPriority());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function IsAlive
	 * @info Tests if this [[Thread]] is alive. A [[Thread]] is alive if it has been started and has not yet died.
	 * @arguments nil
	 * @return [[Boolean]]:alive
	 */

	public static JavaFunction IsAlive = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaJavaThread self = (LuaJavaThread) l.checkUserdata(1, LuaJavaThread.class, "Thread");
			l.pushBoolean(self.isAlive());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function Interrupt
	 * @info Interrupts this [[Thread]].
	 * @arguments nil
	 * @return nil
	 */

	public static JavaFunction Interrupt = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaJavaThread self = (LuaJavaThread) l.checkUserdata(1, LuaJavaThread.class, "Thread");
			self.interrupt();
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function IsInterrupted
	 * @info Tests whether this [[Thread]] has been interrupted.
	 * @arguments nil
	 * @return [[Boolean]]:interrupted
	 */

	public static JavaFunction IsInterrupted = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaJavaThread self = (LuaJavaThread) l.checkUserdata(1, LuaJavaThread.class, "Thread");
			l.pushBoolean(self.isInterrupted());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function SetName
	 * @info Sets the name of this [[Thread]].
	 * @arguments [[String]]:name
	 * @return nil
	 */

	public static JavaFunction SetName = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaJavaThread self = (LuaJavaThread) l.checkUserdata(1, LuaJavaThread.class, "Thread");
			self.setName(l.checkString(2));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function GetName
	 * @info Gets the name of this [[Thread]].
	 * @arguments nil
	 * @return [[String]]:name
	 */

	public static JavaFunction GetName = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaJavaThread self = (LuaJavaThread) l.checkUserdata(1, LuaJavaThread.class, "Thread");
			l.pushString(self.getName());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetID
	 * @info Changes the ID of this [[Thread]].
	 * @arguments nil
	 * @return [[Number]]:id
	 */

	public static JavaFunction GetID = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaJavaThread self = (LuaJavaThread) l.checkUserdata(1, LuaJavaThread.class, "Thread");
			l.pushNumber(self.getId());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function Sleep
	 * @info Pauses the thread for the given amount of time
	 * @arguments [[Number]]:milliseconds, [ [[Number]]:nanoseconds ]
	 * @return nil
	 */

	public static JavaFunction Sleep = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaJavaThread self = (LuaJavaThread) l.checkUserdata(1, LuaJavaThread.class, "Thread");
			try {
				Thread.sleep((long) l.checkNumber(2), l.checkInteger(3, 0));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function Start
	 * @info Starts the thread
	 * @arguments nil
	 * @return nil
	 */

	public static JavaFunction Start = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaJavaThread self = (LuaJavaThread) l.checkUserdata(1, LuaJavaThread.class, "Thread");
			self.start();
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function GetState
	 * @info Returns the state of the [[Thread]]
	 * @arguments nil
	 * @return [[ENUM:THREAD_STATE_*]]:priority
	 */

	public static JavaFunction GetState = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaJavaThread self = (LuaJavaThread) l.checkUserdata(1, LuaJavaThread.class, "Thread");
			l.pushNumber(self.getState().ordinal());
			return 1;
		}
	};

	public static void Init(LuaState l) {
		l.newMetatable("Thread");
		{
			l.pushValue(-1);
			l.setField(-2, "__index");

			l.pushJavaFunction(__tostring);
			l.setField(-2, "__tostring");

			l.pushJavaFunction(SetPriority);
			l.setField(-2, "SetPriority");
			l.pushJavaFunction(GetPriority);
			l.setField(-2, "GetPriority");
			l.pushJavaFunction(IsAlive);
			l.setField(-2, "IsAlive");
			l.pushJavaFunction(Interrupt);
			l.setField(-2, "Interrupt");
			l.pushJavaFunction(IsInterrupted);
			l.setField(-2, "IsInterrupted");
			l.pushJavaFunction(SetName);
			l.setField(-2, "SetName");
			l.pushJavaFunction(GetName);
			l.setField(-2, "GetName");
			l.pushJavaFunction(GetID);
			l.setField(-2, "GetID");
			l.pushJavaFunction(Sleep);
			l.setField(-2, "Sleep");
			l.pushJavaFunction(Start);
			l.setField(-2, "Start");
			l.pushJavaFunction(GetState);
			l.setField(-2, "GetState");

		}
		l.pop(1);

		l.pushNumber(Thread.MAX_PRIORITY);
		l.setGlobal("THREAD_PRIORITY_MAX");
		l.pushNumber(Thread.MIN_PRIORITY);
		l.setGlobal("THREAD_PRIORITY_MIN");
		l.pushNumber(Thread.NORM_PRIORITY);
		l.setGlobal("THREAD_PRIORITY_NORM");

		l.pushNumber(Thread.State.BLOCKED.ordinal());
		l.setGlobal("THREAD_STATE_BLOCKED");
		l.pushNumber(Thread.State.NEW.ordinal());
		l.setGlobal("THREAD_STATE_NEW");
		l.pushNumber(Thread.State.RUNNABLE.ordinal());
		l.setGlobal("THREAD_STATE_RUNNABLE");
		l.pushNumber(Thread.State.TERMINATED.ordinal());
		l.setGlobal("THREAD_STATE_TERMINATED");
		l.pushNumber(Thread.State.TIMED_WAITING.ordinal());
		l.setGlobal("THREAD_STATE_TIMED_WAITING");
		l.pushNumber(Thread.State.WAITING.ordinal());
		l.setGlobal("THREAD_STATE_WAITING");

	}
}
