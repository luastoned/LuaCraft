package com.luacraft.library;

import net.minecraft.util.StatCollector;

import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

public class LuaLibLanguage {
	public static JavaFunction Translate = new JavaFunction() {
		public int invoke(LuaState l) {
			int top = l.getTop();

			Object[] strings = new Object[top - 1];

			for (int i = 2; i <= top; i++)
				strings[i - 1] = l.checkString(i);

			String translated = StatCollector.translateToLocalFormatted(
					l.checkString(1), strings);

			l.pushString(translated);
			return 1;
		}
	};
}
