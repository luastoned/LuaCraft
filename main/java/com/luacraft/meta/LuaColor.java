package com.luacraft.meta;

import com.luacraft.LuaCraftState;
import com.luacraft.LuaUserdata;
import com.luacraft.classes.Color;
import com.luacraft.classes.Vector;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

public class LuaColor {

	public static JavaFunction __tostring = new JavaFunction() {
		public int invoke(LuaState l) {
			Color self = (Color) l.checkUserdata(1, Color.class, "Color");
			l.pushString(self.toString());
			return 1;
		}
	};

	public static JavaFunction __index = new JavaFunction() {
		public int invoke(LuaState l) {
			Color self = (Color) l.checkUserdata(1, Color.class, "Color");
			String key = l.checkString(2);

			if (key.equals("r"))
				l.pushNumber(self.r);
			else if (key.equals("g"))
				l.pushNumber(self.g);
			else if (key.equals("b"))
				l.pushNumber(self.b);
			else if (key.equals("a"))
				l.pushNumber(self.a);
			else
				LuaUserdata.PushBaseMeta(l);

			return 1;
		}
	};

	public static JavaFunction __newindex = new JavaFunction() {
		public int invoke(LuaState l) {
			Color self = (Color) l.checkUserdata(1, Color.class, "Color");
			String key = l.checkString(2);
			int val = l.toInteger(3);

			if (key.equals("r"))
				self.r = val;
			else if (key.equals("g"))
				self.g = val;
			else if (key.equals("b"))
				self.b = val;
			else if (key.equals("b"))
				self.a = val;

			return 0;
		}
	};

	public static JavaFunction __eq = new JavaFunction() {
		public int invoke(LuaState l) {
			Color self = (Color) l.checkUserdata(1, Color.class, "Color");
			Color other = (Color) l.checkUserdata(2, Color.class, "Color");
			l.pushBoolean(self.equals(other));
			return 1;
		}
	};

	public static JavaFunction __add = new JavaFunction() {
		public int invoke(LuaState l) {
			Color self = (Color) l.checkUserdata(1, Color.class, "Color");
			Color other = (Color) l.checkUserdata(2, Color.class, "Color");
			self.add(other).push(l);
			return 1;
		}
	};

	public static JavaFunction __sub = new JavaFunction() {
		public int invoke(LuaState l) {
			Color self = (Color) l.checkUserdata(1, Vector.class, "Color");
			Color other = (Color) l.checkUserdata(2, Vector.class, "Color");
			self.sub(other).push(l);
			return 1;
		}
	};

	public static JavaFunction __mul = new JavaFunction() {
		public int invoke(LuaState l) {
			Color self = (Color) l.checkUserdata(1, Color.class, "Color");
			if (l.isUserdata(2, Vector.class)) {
				Color other = (Color) l.checkUserdata(2, Color.class, "Color");
				self.mul(other).push(l);
				return 1;
			} else if (l.isNumber(2)) {
				double other = l.toNumber(2);
				self.mul(other).push(l);
				return 1;
			}
			return 0;
		}
	};

	public static JavaFunction __div = new JavaFunction() {
		public int invoke(LuaState l) {
			Color self = (Color) l.checkUserdata(1, Color.class, "Color");
			if (l.isUserdata(2, Color.class)) {
				Color other = (Color) l.checkUserdata(2, Color.class, "Color");
				self.div(other).push(l);
				return 1;
			} else if (l.isNumber(2)) {
				double other = l.toNumber(2);
				self.div(other).push(l);
				return 1;
			}
			return 0;
		}
	};

	public static void Init(final LuaCraftState l) {
		l.newMetatable("Color");
		{
			l.pushJavaFunction(__tostring);
			l.setField(-2, "__tostring");
			
			l.pushJavaFunction(__index);
			l.setField(-2, "__index");
			l.pushJavaFunction(__newindex);
			l.setField(-2, "__newindex");
			
			LuaUserdata.SetupBasicMeta(l);

			l.newMetatable("Object");
			l.setField(-2, "__basemeta");

			l.pushJavaFunction(__eq);
			l.setField(-2, "__eq");
			l.pushJavaFunction(__add);
			l.setField(-2, "__add");
			l.pushJavaFunction(__sub);
			l.setField(-2, "__sub");
			l.pushJavaFunction(__mul);
			l.setField(-2, "__mul");
			l.pushJavaFunction(__div);
			l.setField(-2, "__div");
		}
		l.pop(1);
	}
}
