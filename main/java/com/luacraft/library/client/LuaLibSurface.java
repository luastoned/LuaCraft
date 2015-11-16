package com.luacraft.library.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.luacraft.LuaCraftState;
import com.luacraft.classes.Color;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaRuntimeException;
import com.naef.jnlua.LuaState;

public class LuaLibSurface {
	private static Minecraft client;

	public static ResourceLocation currentTexture = null;
	public static Color drawColor = new Color(255, 255, 255, 255);

	/**
	 * @author Jake
	 * @library surface
	 * @function GetDefaultFont Gets the default font object that minecraft uses
	 *           to draw text
	 * @arguments nil
	 * @return [[Font]]:font
	 */

	public static JavaFunction GetDefaultFont = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushUserdataWithMeta(client.fontRendererObj, "Font");
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @library surface
	 * @function CreateFont Creates a font object that can be used for drawing
	 *           text to the screen
	 * @arguments [[String]]:fileResource, [ [[Number]]:size,
	 *            [[Boolean]]:unicode ]
	 * @return [[Font]]:font
	 */

	public static JavaFunction CreateFont = new JavaFunction() {
		public int invoke(LuaState l) {
			ResourceLocation resource = new ResourceLocation(l.checkString(1));

			FontRenderer newFont = new FontRenderer(client.gameSettings,
					resource, client.getTextureManager(), l.checkBoolean(3,
							false));
			newFont.FONT_HEIGHT = l.checkInteger(2, newFont.FONT_HEIGHT);

			l.pushUserdataWithMeta(newFont, "Font");
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @library surface
	 * @function GetDrawColor Gets the current drawing color
	 * @arguments nil
	 * @return [[Color]]:color
	 */

	public static JavaFunction GetDrawColor = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushUserdataWithMeta(drawColor, "Color");
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @library surface
	 * @function SetDrawColor Sets the current drawing color
	 * @arguments ( [[Number]]:r, [[Number]]:g, [[Number]]:b, [[Number]]:a ) OR
	 *            [[Color]]:color
	 * @return nil
	 */

	public static JavaFunction SetDrawColor = new JavaFunction() {
		public int invoke(LuaState l) {
			if (l.isUserdata(1, Color.class))
				drawColor = (Color) l.checkUserdata(1, Color.class, "Color");
			else if (l.isNumber(1)) {
				int r = l.checkInteger(1);
				int g = l.checkInteger(2);
				int b = l.checkInteger(3);
				int a = l.checkInteger(4, 255);
				drawColor = new Color(r, g, b, a);
			} else
				throw new LuaRuntimeException(
						"surface.SetDrawColor: No valid color passed");
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @library surface
	 * @function DrawRect Draws a rectangle
	 * @arguments [[Number]]:xPos, [[Number]]:yPos, [[Number]]:width,
	 *            [[Number]]:height
	 * @return nil
	 */

	public static JavaFunction DrawRect = new JavaFunction() {
		public int invoke(LuaState l) {
			int x = l.checkInteger(1);
			int y = l.checkInteger(2);
			int w = l.checkInteger(3);
			int h = l.checkInteger(4);

			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_TEXTURE_2D);

			OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA,
					GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
			GL11.glColor4f(drawColor.r / 255.f, drawColor.g / 255.f,
					drawColor.b / 255.f, drawColor.a / 255.f);

			Tessellator tInstance = Tessellator.getInstance();
			WorldRenderer renderer = tInstance.getWorldRenderer();
			renderer.startDrawingQuads();
			renderer.addVertex(x, y + h, 0);
			renderer.addVertex(x + w, y + h, 0);
			renderer.addVertex(x + w, y, 0);
			renderer.addVertex(x, y, 0);
			tInstance.draw();

			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);

			return 0;
		}
	};

	/**
	 * @author Jake
	 * @library surface
	 * @function DrawGradientRect Draws a rectangle with a gradient
	 * @arguments [[Number]]:xPos, [[Number]]:yPos, [[Number]]:width,
	 *            [[Number]]:height, [[Color]]:fadeto
	 * @return nil
	 */

	public static JavaFunction DrawGradientRect = new JavaFunction() {
		public int invoke(LuaState l) {
			int x = l.checkInteger(1);
			int y = l.checkInteger(2);
			int w = l.checkInteger(3);
			int h = l.checkInteger(4);
			Color fadeColor = (Color) l.checkUserdata(5, Color.class, "Color");

			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_ALPHA_TEST);

			OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA,
					GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
			GL11.glShadeModel(GL11.GL_SMOOTH);

			Tessellator tInstance = Tessellator.getInstance();
			WorldRenderer renderer = tInstance.getWorldRenderer();
			renderer.startDrawingQuads();
			renderer.setColorRGBA(drawColor.r, drawColor.g, drawColor.b,
					drawColor.a);
			renderer.addVertex(x + w, y, 0);
			renderer.addVertex(x, y, 0);
			renderer.setColorRGBA(fadeColor.r, fadeColor.g, fadeColor.b,
					fadeColor.a);
			renderer.addVertex(x, y + h, 0);
			renderer.addVertex(x + w, y + h, 0);
			tInstance.draw();

			GL11.glShadeModel(GL11.GL_FLAT);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_TEXTURE_2D);

			return 0;
		}
	};

	/**
	 * @author Jake
	 * @library surface
	 * @function SetTexture Sets a texture ID for to be used when drawing
	 * @arguments [[Resource]]:file
	 * @return nil
	 */

	public static JavaFunction SetTexture = new JavaFunction() {
		public int invoke(LuaState l) {
			currentTexture = (ResourceLocation) l.checkUserdata(1,
					ResourceLocation.class, "Resource");
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @library surface
	 * @function DrawTexturedRect Draws a textured rectangle
	 * @arguments [[Number]]:xPos, [[Number]]:yPos, [[Number]]:width,
	 *            [[Number]]:height
	 * @return nil
	 */

	public static JavaFunction DrawTexturedRect = new JavaFunction() {
		public int invoke(LuaState l) {
			if (currentTexture == null)
				return 0;

			int x = l.checkInteger(1);
			int y = l.checkInteger(2);
			int w = l.checkInteger(3);
			int h = l.checkInteger(4);

			GL11.glColor4f(drawColor.r / 255.f, drawColor.g / 255.f,
					drawColor.b / 255.f, drawColor.a / 255.f);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			client.renderEngine.bindTexture(currentTexture);

			Tessellator tInstance = Tessellator.getInstance();
			WorldRenderer renderer = tInstance.getWorldRenderer();
			renderer.startDrawingQuads();
			renderer.setColorOpaque_I(0xffffff);
			renderer.addVertexWithUV(x, y + h, 0.D, 0.D, 1.D);
			renderer.addVertexWithUV(x + w, y + h, 0.D, 1.D, 1.D);
			renderer.addVertexWithUV(x + w, y, 0.D, 1.D, 0.D);
			renderer.addVertexWithUV(x, y, 0.D, 0.D, 0.D);
			tInstance.draw();
			return 0;
		}
	};

	public static void Init(final LuaCraftState l) {
		client = l.getMinecraft();

		l.newTable();
		{
			l.pushJavaFunction(GetDefaultFont);
			l.setField(-2, "GetDefaultFont");
			l.pushJavaFunction(CreateFont);
			l.setField(-2, "CreateFont");
			l.pushJavaFunction(GetDrawColor);
			l.setField(-2, "GetDrawColor");
			l.pushJavaFunction(SetDrawColor);
			l.setField(-2, "SetDrawColor");
			l.pushJavaFunction(DrawRect);
			l.setField(-2, "DrawRect");
			l.pushJavaFunction(DrawGradientRect);
			l.setField(-2, "DrawGradientRect");
			l.pushJavaFunction(SetTexture);
			l.setField(-2, "SetTexture");
			l.pushJavaFunction(DrawTexturedRect);
			l.setField(-2, "DrawTexturedRect");
		}
		l.setGlobal("surface");
	}
}
