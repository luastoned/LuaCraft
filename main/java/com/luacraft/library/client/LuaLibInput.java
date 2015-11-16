package com.luacraft.library.client;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

public class LuaLibInput {
	/**
	 * @author Jake
	 * @library input
	 * @function IsKeyDown Returns if the specified key is being pressed
	 * @arguments nil
	 * @return [[Boolean]]:isdown
	 */

	public static JavaFunction IsKeyDown = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushBoolean(Keyboard.isKeyDown(l.checkInteger(1)));
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @library input
	 * @function IsMouseDown Returns if the specified mouse button is being
	 *           pressed
	 * @arguments nil
	 * @return [[Boolean]]:isdown
	 */

	public static JavaFunction IsMouseDown = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushBoolean(Mouse.isButtonDown(l.checkInteger(1)));
			return 1;
		}
	};

	public static void Init(final LuaState l) {
		l.newTable();
		{
			l.pushJavaFunction(IsKeyDown);
			l.setField(-2, "IsKeyDown");
			l.pushJavaFunction(IsMouseDown);
			l.setField(-2, "IsMouseDown");
		}
		l.setGlobal("input");

		l.pushInteger(Keyboard.KEY_0);
		l.setGlobal("KEY_0");
		l.pushInteger(Keyboard.KEY_1);
		l.setGlobal("KEY_1");
		l.pushInteger(Keyboard.KEY_2);
		l.setGlobal("KEY_2");
		l.pushInteger(Keyboard.KEY_3);
		l.setGlobal("KEY_3");
		l.pushInteger(Keyboard.KEY_4);
		l.setGlobal("KEY_4");
		l.pushInteger(Keyboard.KEY_5);
		l.setGlobal("KEY_5");
		l.pushInteger(Keyboard.KEY_6);
		l.setGlobal("KEY_6");
		l.pushInteger(Keyboard.KEY_7);
		l.setGlobal("KEY_7");
		l.pushInteger(Keyboard.KEY_8);
		l.setGlobal("KEY_8");
		l.pushInteger(Keyboard.KEY_9);
		l.setGlobal("KEY_9");

		l.pushInteger(Keyboard.KEY_A);
		l.setGlobal("KEY_A");
		l.pushInteger(Keyboard.KEY_B);
		l.setGlobal("KEY_B");
		l.pushInteger(Keyboard.KEY_C);
		l.setGlobal("KEY_C");
		l.pushInteger(Keyboard.KEY_D);
		l.setGlobal("KEY_D");
		l.pushInteger(Keyboard.KEY_E);
		l.setGlobal("KEY_E");
		l.pushInteger(Keyboard.KEY_F);
		l.setGlobal("KEY_F");
		l.pushInteger(Keyboard.KEY_G);
		l.setGlobal("KEY_G");
		l.pushInteger(Keyboard.KEY_H);
		l.setGlobal("KEY_H");
		l.pushInteger(Keyboard.KEY_I);
		l.setGlobal("KEY_I");
		l.pushInteger(Keyboard.KEY_J);
		l.setGlobal("KEY_J");
		l.pushInteger(Keyboard.KEY_K);
		l.setGlobal("KEY_K");
		l.pushInteger(Keyboard.KEY_L);
		l.setGlobal("KEY_L");
		l.pushInteger(Keyboard.KEY_M);
		l.setGlobal("KEY_M");
		l.pushInteger(Keyboard.KEY_N);
		l.setGlobal("KEY_N");
		l.pushInteger(Keyboard.KEY_O);
		l.setGlobal("KEY_O");
		l.pushInteger(Keyboard.KEY_P);
		l.setGlobal("KEY_P");
		l.pushInteger(Keyboard.KEY_Q);
		l.setGlobal("KEY_Q");
		l.pushInteger(Keyboard.KEY_R);
		l.setGlobal("KEY_R");
		l.pushInteger(Keyboard.KEY_S);
		l.setGlobal("KEY_S");
		l.pushInteger(Keyboard.KEY_T);
		l.setGlobal("KEY_T");
		l.pushInteger(Keyboard.KEY_U);
		l.setGlobal("KEY_U");
		l.pushInteger(Keyboard.KEY_V);
		l.setGlobal("KEY_V");
		l.pushInteger(Keyboard.KEY_W);
		l.setGlobal("KEY_W");
		l.pushInteger(Keyboard.KEY_X);
		l.setGlobal("KEY_X");
		l.pushInteger(Keyboard.KEY_Y);
		l.setGlobal("KEY_Y");
		l.pushInteger(Keyboard.KEY_Z);
		l.setGlobal("KEY_Z");

		l.pushInteger(Keyboard.KEY_F1);
		l.setGlobal("KEY_F1");
		l.pushInteger(Keyboard.KEY_F2);
		l.setGlobal("KEY_F2");
		l.pushInteger(Keyboard.KEY_F3);
		l.setGlobal("KEY_F3");
		l.pushInteger(Keyboard.KEY_F4);
		l.setGlobal("KEY_F4");
		l.pushInteger(Keyboard.KEY_F5);
		l.setGlobal("KEY_F5");
		l.pushInteger(Keyboard.KEY_F6);
		l.setGlobal("KEY_F6");
		l.pushInteger(Keyboard.KEY_F7);
		l.setGlobal("KEY_F7");
		l.pushInteger(Keyboard.KEY_F8);
		l.setGlobal("KEY_F8");
		l.pushInteger(Keyboard.KEY_F9);
		l.setGlobal("KEY_F9");
		l.pushInteger(Keyboard.KEY_F10);
		l.setGlobal("KEY_F10");
		l.pushInteger(Keyboard.KEY_F11);
		l.setGlobal("KEY_F11");
		l.pushInteger(Keyboard.KEY_F12);
		l.setGlobal("KEY_F12");

		l.pushInteger(Keyboard.KEY_ADD);
		l.setGlobal("KEY_ADD");
		l.pushInteger(Keyboard.KEY_SUBTRACT);
		l.setGlobal("KEY_SUBTRACT");
		l.pushInteger(Keyboard.KEY_MINUS);
		l.setGlobal("KEY_MINUS");
		l.pushInteger(Keyboard.KEY_DIVIDE);
		l.setGlobal("KEY_DIVIDE");
		l.pushInteger(Keyboard.KEY_EQUALS);
		l.setGlobal("KEY_EQUALS");
		l.pushInteger(Keyboard.KEY_RETURN);
		l.setGlobal("KEY_RETURN");

		l.pushInteger(Keyboard.KEY_PERIOD);
		l.setGlobal("KEY_PERIOD");
		l.pushInteger(Keyboard.KEY_APOSTROPHE);
		l.setGlobal("KEY_APOSTROPHE");
		l.pushInteger(Keyboard.KEY_COLON);
		l.setGlobal("KEY_COLON");
		l.pushInteger(Keyboard.KEY_SEMICOLON);
		l.setGlobal("KEY_SEMICOLON");
		l.pushInteger(Keyboard.KEY_COMMA);
		l.setGlobal("KEY_COMMA");

		l.pushInteger(Keyboard.KEY_ESCAPE);
		l.setGlobal("KEY_ESCAPE");
		l.pushInteger(Keyboard.KEY_TAB);
		l.setGlobal("KEY_TAB");
		l.pushInteger(Keyboard.KEY_CAPITAL);
		l.setGlobal("KEY_CAPITAL");

		l.pushInteger(Keyboard.KEY_UP);
		l.setGlobal("KEY_UP");
		l.pushInteger(Keyboard.KEY_DOWN);
		l.setGlobal("KEY_DOWN");
		l.pushInteger(Keyboard.KEY_LEFT);
		l.setGlobal("KEY_LEFT");
		l.pushInteger(Keyboard.KEY_RIGHT);
		l.setGlobal("KEY_RIGHT");
	}
}
