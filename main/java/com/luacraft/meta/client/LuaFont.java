package com.luacraft.meta.client;

import org.lwjgl.opengl.GL11;

import com.luacraft.LuaCraftState;
import com.luacraft.library.client.LuaLibSurface;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

public class LuaFont {
	public static JavaFunction __tostring = new JavaFunction() {
		public int invoke(LuaState l) {
			FontRenderer self = (FontRenderer) l.checkUserdata(1, FontRenderer.class, "Font");
			l.pushString(String.format("Font: 0x%08x", l.toPointer(1)));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetSize
	 * @info Gets the width and height of a string of text with the given font
	 * @arguments [[String]]:text
	 * @return [[Number]]:width, [[Number]]:height
	 */

	public static JavaFunction GetSize = new JavaFunction() {
		public int invoke(LuaState l) {
			FontRenderer self = (FontRenderer) l.checkUserdata(1, FontRenderer.class, "Font");
			l.pushNumber(self.getStringWidth(l.checkString(2)));
			l.pushNumber(self.FONT_HEIGHT);
			return 2;
		}
	};

	/**
	 * @author Jake
	 * @function GetWidth
	 * @info Gets the width of a string of text with the given font
	 * @arguments [[String]]:text
	 * @return [[Number]]:width
	 */

	public static JavaFunction GetWidth = new JavaFunction() {
		public int invoke(LuaState l) {
			FontRenderer self = (FontRenderer) l.checkUserdata(1, FontRenderer.class, "Font");
			l.pushNumber(self.getStringWidth(l.checkString(2)));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetHeight
	 * @info Gets the height of a string of text with the given font
	 * @arguments [[String]]:text
	 * @return [[Number]]:height
	 */

	public static JavaFunction GetHeight = new JavaFunction() {
		public int invoke(LuaState l) {
			FontRenderer self = (FontRenderer) l.checkUserdata(1, FontRenderer.class, "Font");
			l.pushNumber(self.FONT_HEIGHT);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function DrawText
	 * @info Draws text on the screen
	 * @arguments [[String]]:text, [[Number]]:x, [[Number]]:y, [[Boolean]]:shadow
	 * @return nil
	 */

	public static JavaFunction DrawText = new JavaFunction() {
		public int invoke(LuaState l) {
			FontRenderer self = (FontRenderer) l.checkUserdata(1, FontRenderer.class, "Font");
			String text = l.checkString(2);
			int x = l.checkInteger(3);
			int y = l.checkInteger(4);

			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			if (l.checkBoolean(5, false))
				self.drawStringWithShadow(text, x, y, LuaLibSurface.drawColor.getRGBA());
			else
				self.drawString(text, x, y, LuaLibSurface.drawColor.getRGBA());

			GlStateManager.disableBlend();
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function DrawTextWrapped
	 * @info Draws wrapped text on the screen
	 * @arguments [[String]]:text, [[Number]]:x, [[Number]]:y, [[Number]]:width
	 * @return nil
	 */

	public static JavaFunction DrawTextWrapped = new JavaFunction() {
		public int invoke(LuaState l) {
			FontRenderer self = (FontRenderer) l.checkUserdata(1, FontRenderer.class, "Font");
			String text = l.checkString(2);
			int x = l.checkInteger(3);
			int y = l.checkInteger(4);
			int width = l.checkInteger(5);

			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			self.drawSplitString(text, x, y, width, LuaLibSurface.drawColor.getRGBA());

			GlStateManager.disableBlend();
			return 0;
		}
	};

	public static void Init(final LuaCraftState l) {
		l.newMetatable("Font");
		{
			l.pushValue(-1);
			l.setField(-2, "__index");

			l.pushJavaFunction(__tostring);
			l.setField(-2, "__tostring");

			l.pushJavaFunction(GetSize);
			l.setField(-2, "GetSize");
			l.pushJavaFunction(GetWidth);
			l.setField(-2, "GetWidth");
			l.pushJavaFunction(GetHeight);
			l.setField(-2, "GetHeight");
			l.pushJavaFunction(DrawText);
			l.setField(-2, "DrawText");
			l.pushJavaFunction(DrawTextWrapped);
			l.setField(-2, "DrawTextWrapped");
		}
		l.pop(1);
	}
}
