package com.luacraft.library;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.luacraft.LuaCraftState;
import com.luacraft.classes.FileMount;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaRuntimeException;
import com.naef.jnlua.LuaState;

import net.minecraft.util.text.translation.I18n;
import net.minecraft.util.text.translation.LanguageMap;

public class LuaLibLanguage {

	/**
	 * @author Jake
	 * @library language
	 * @function Translate
	 * @info Translate a translation key into its human readable language
	 * @arguments [[String]]:key, [ [[String]]:... ]
	 * @return [[String]]:translation
	 */

	public static JavaFunction Translate = new JavaFunction() {
		public int invoke(LuaState l) {
			int top = l.getTop();

			String translated = null;

			if (top > 1) {
				Object[] strings = new Object[top - 1];

				for (int i = 2; i <= top; i++)
					strings[i - 1] = l.checkString(i);

				translated = I18n.translateToLocalFormatted(l.checkString(1), strings);
			} else
				translated = I18n.translateToLocal(l.checkString(1));

			l.pushString(translated);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @library language
	 * @function ParseFile
	 * @info Load a language file into the game
	 * @arguments [[String]]:file
	 * @return nil
	 */

	public static JavaFunction ParseFile = new JavaFunction() {
		public int invoke(LuaState l) {
			File file = FileMount.GetFile(l.checkString(1));
			try {
				InputStream stream = new FileInputStream(file);
				LanguageMap.inject(stream);
			} catch (FileNotFoundException e) {
				throw new LuaRuntimeException("Cannot open " + file.getName() + ": No such file or directory");
			}
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @library language
	 * @function CanTranslate
	 * @info Returns true if the given key is translatable
	 * @arguments [[String]]:key
	 * @return [[Boolean]]:cantranslate
	 */

	public static JavaFunction CanTranslate = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushBoolean(I18n.canTranslate(l.checkString(1)));
			return 1;
		}
	};

	public static void Init(final LuaCraftState l) {
		l.newTable();
		{
			l.pushJavaFunction(Translate);
			l.setField(-2, "Translate");
			l.pushJavaFunction(ParseFile);
			l.setField(-2, "ParseFile");
			l.pushJavaFunction(CanTranslate);
			l.setField(-2, "CanTranslate");
		}
		l.setGlobal("language");
	}
}
