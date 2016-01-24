package com.luacraft.library.client;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Disk;
import org.lwjgl.util.glu.Sphere;

import com.luacraft.LuaCraftState;
import com.luacraft.classes.Angle;
import com.luacraft.classes.Color;
import com.luacraft.classes.Vector;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaRuntimeException;
import com.naef.jnlua.LuaState;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;

public class LuaLibRender {
	private static Minecraft client;

	public static Disk renderDisk = new Disk();
	public static Cylinder renderCylinder = new Cylinder();
	public static Sphere renderSphere = new Sphere();
	public static boolean ignoreZ = false;
	public static int currentTexture = -1;
	public static FontRenderer currentFont;
	public static Color drawColor = new Color(255, 255, 255, 255);

	/**
	 * @author Jake
	 * @library render
	 * @function IgnoreZ
	 * @info Set ignoreZ to true or false
	 * @arguments [[Boolean]]:ignore
	 * @return nil
	 */

	public static JavaFunction IgnoreZ = new JavaFunction() {
		public int invoke(LuaState l) {
			ignoreZ = l.checkBoolean(1, false);
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @library render
	 * @function GetDrawColor
	 * @info Gets the current drawing color
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
	 * @library render
	 * @function SetDrawColor
	 * @info Sets the current drawing color
	 * @arguments ( [[Number]]:r, [[Number]]:g, [[Number]]:b, [[Number]]:a ) OR [[Color]]:color
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
				throw new LuaRuntimeException("surface.SetDrawColor: No valid color passed");
			return 0;
		}
	};

	/**
	 * @author Gregor
	 * @library render
	 * @function DrawText
	 * @info Draws text to the current screen
	 * @arguments [[String]]:text, [[Vector]]:pos, [[Angle]]:ang, [[Number]]:scale, [[Boolean]]:shadow
	 * @return nil
	 */

	public static JavaFunction DrawText = new JavaFunction() {
		public int invoke(LuaState l) {
			String text = l.checkString(1);
			Vector vec1 = (Vector) l.checkUserdata(2, Vector.class, "Vector");
			Angle ang1 = (Angle) l.checkUserdata(3, Angle.class, "Angle");
			float flScale = (float) l.checkNumber(4, 1);

			double posX = client.thePlayer.lastTickPosX
					+ (client.thePlayer.posX - client.thePlayer.lastTickPosX) * client.timer.renderPartialTicks;
			double posY = client.thePlayer.lastTickPosY
					+ (client.thePlayer.posY - client.thePlayer.lastTickPosY) * client.timer.renderPartialTicks;
			double posZ = client.thePlayer.lastTickPosZ
					+ (client.thePlayer.posZ - client.thePlayer.lastTickPosZ) * client.timer.renderPartialTicks;

			double vecX = vec1.x - posX, vecY = vec1.z - posY, vecZ = vec1.y - posZ;

			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glDepthMask(false);

			if (ignoreZ) {
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glDisable(GL11.GL_CULL_FACE);
			}

			GL11.glPushMatrix();
			GL11.glTranslated(vecX, vecY, vecZ);

			// Fix default rotation
			GL11.glRotatef(180F, 0.F, 0.F, 1.F);
			GL11.glRotatef(-90F, 0.F, 1.F, 0.F);

			GL11.glRotatef((float) ang1.p, 0.F, 0.F, 1.F);
			GL11.glRotatef((float) ang1.y, 0.F, 1.F, 0.F);
			GL11.glRotatef((float) ang1.r, 1.F, 0.F, 0.F);

			GL11.glPushMatrix();

			GL11.glScalef(0.125F * flScale, 0.125F * flScale, 0.125F * flScale);
			if (l.checkBoolean(5, false))
				currentFont.drawStringWithShadow(text, 0, 0, drawColor.getRGB());
			else
				currentFont.drawString(text, 0, 0, drawColor.getRGB());

			GL11.glPopMatrix();
			GL11.glPopMatrix();

			if (ignoreZ) {
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				GL11.glEnable(GL11.GL_CULL_FACE);
			}

			GL11.glDepthMask(true);
			GL11.glDisable(GL11.GL_BLEND);

			return 0;
		}
	};

	/**
	 * @author Gregor
	 * @library render
	 * @function DrawLine
	 * @info Draws a line in 3D space
	 * @arguments [[Vector]]:pos1, [[Vector]]:pos2, [ [[Number]]:width ]
	 * @return nil
	 */

	public static JavaFunction DrawLine = new JavaFunction() {
		public int invoke(LuaState l) {
			Vector vec1 = (Vector) l.checkUserdata(1, Vector.class, "Vector");
			Vector vec2 = (Vector) l.checkUserdata(2, Vector.class, "Vector");
			float lineWidth = (float) l.checkNumber(3, 2);

			double posX = client.thePlayer.lastTickPosX
					+ (client.thePlayer.posX - client.thePlayer.lastTickPosX) * client.timer.renderPartialTicks;
			double posY = client.thePlayer.lastTickPosY
					+ (client.thePlayer.posY - client.thePlayer.lastTickPosY) * client.timer.renderPartialTicks;
			double posZ = client.thePlayer.lastTickPosZ
					+ (client.thePlayer.posZ - client.thePlayer.lastTickPosZ) * client.timer.renderPartialTicks;

			double minX = vec1.x - posX, minY = vec1.z - posY, minZ = vec1.y - posZ;
			double maxX = vec2.x - posX, maxY = vec2.z - posY, maxZ = vec2.y - posZ;

			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
			GL11.glColor4f(drawColor.r / 255.f, drawColor.g / 255.f, drawColor.b / 255.f, drawColor.a / 255.f);
			GL11.glLineWidth(lineWidth);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDepthMask(false);

			if (ignoreZ) {
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glDisable(GL11.GL_CULL_FACE);
			}

			Tessellator tInstance = Tessellator.getInstance();
			WorldRenderer renderer = tInstance.getWorldRenderer();
			renderer.startDrawing(GL11.GL_LINES);
			renderer.addVertex(minX, minY, minZ);
			renderer.addVertex(maxX, maxY, maxZ);
			tInstance.draw();

			if (ignoreZ) {
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				GL11.glEnable(GL11.GL_CULL_FACE);
			}

			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);

			return 0;
		}
	};

	/**
	 * @author Gregor
	 * @library render
	 * @function DrawBoundingBox
	 * @info Draws a bounding box in 3D space with the given a mins and a maxs
	 * @arguments [[Vector]]:min, [[Vector]]:max, [ [[Number]]:width ]
	 * @return nil
	 */

	public static JavaFunction DrawBoundingBox = new JavaFunction() {
		public int invoke(LuaState l) {
			Vector vec1 = (Vector) l.checkUserdata(1, Vector.class, "Vector");
			Vector vec2 = (Vector) l.checkUserdata(2, Vector.class, "Vector");
			float lineWidth = (float) l.checkNumber(3, 2);

			double posX = client.thePlayer.lastTickPosX
					+ (client.thePlayer.posX - client.thePlayer.lastTickPosX) * client.timer.renderPartialTicks;
			double posY = client.thePlayer.lastTickPosY
					+ (client.thePlayer.posY - client.thePlayer.lastTickPosY) * client.timer.renderPartialTicks;
			double posZ = client.thePlayer.lastTickPosZ
					+ (client.thePlayer.posZ - client.thePlayer.lastTickPosZ) * client.timer.renderPartialTicks;

			double minX = vec1.x - posX, minY = vec1.z - posY, minZ = vec1.y - posZ;
			double maxX = vec2.x - posX, maxY = vec2.z - posY, maxZ = vec2.y - posZ;

			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(drawColor.r / 255.f, drawColor.g / 255.f, drawColor.b / 255.f, drawColor.a / 255.f);
			GL11.glLineWidth(lineWidth);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDepthMask(false);

			if (ignoreZ) {
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glDisable(GL11.GL_CULL_FACE);
			}

			Tessellator tInstance = Tessellator.getInstance();
			WorldRenderer renderer = tInstance.getWorldRenderer();
			renderer.startDrawing(GL11.GL_LINE_STRIP);
			renderer.addVertex(minX, minY, minZ);
			renderer.addVertex(maxX, minY, minZ);
			renderer.addVertex(maxX, minY, maxZ);
			renderer.addVertex(minX, minY, maxZ);
			renderer.addVertex(minX, minY, minZ);
			tInstance.draw();
			renderer.startDrawing(GL11.GL_LINE_STRIP);
			renderer.addVertex(minX, maxY, minZ);
			renderer.addVertex(maxX, maxY, minZ);
			renderer.addVertex(maxX, maxY, maxZ);
			renderer.addVertex(minX, maxY, maxZ);
			renderer.addVertex(minX, maxY, minZ);
			tInstance.draw();
			renderer.startDrawing(GL11.GL_LINES);
			renderer.addVertex(minX, minY, minZ);
			renderer.addVertex(minX, maxY, minZ);
			renderer.addVertex(maxX, minY, minZ);
			renderer.addVertex(maxX, maxY, minZ);
			renderer.addVertex(maxX, minY, maxZ);
			renderer.addVertex(maxX, maxY, maxZ);
			renderer.addVertex(minX, minY, maxZ);
			renderer.addVertex(minX, maxY, maxZ);
			tInstance.draw();

			if (ignoreZ) {
				GL11.glEnable(GL11.GL_CULL_FACE);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}

			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);

			return 0;
		}
	};

	/**
	 * @author Gregor
	 * @library render
	 * @function DrawCircle
	 * @info Draws a circle in 3D space
	 * @arguments [[Vector]]:pos, [[Number]]:radius1, [ [[Number]]:radius2, [[Angle]]:ang ]
	 * @return nil
	 */

	public static JavaFunction DrawCircle = new JavaFunction() {
		public int invoke(LuaState l) {
			Vector vec1 = (Vector) l.checkUserdata(1, Vector.class, "Vector");
			float flRadius1 = (float) l.checkNumber(2);
			float flRadius2 = (float) l.checkNumber(3, 0);
			Angle ang1 = (Angle) l.checkUserdata(4, Angle.class, "Angle");
			int iSlices = l.checkInteger(5, 24);

			double posX = client.thePlayer.lastTickPosX
					+ (client.thePlayer.posX - client.thePlayer.lastTickPosX) * client.timer.renderPartialTicks;
			double posY = client.thePlayer.lastTickPosY
					+ (client.thePlayer.posY - client.thePlayer.lastTickPosY) * client.timer.renderPartialTicks;
			double posZ = client.thePlayer.lastTickPosZ
					+ (client.thePlayer.posZ - client.thePlayer.lastTickPosZ) * client.timer.renderPartialTicks;

			double vecX = vec1.x - posX, vecY = vec1.z - posY, vecZ = vec1.y - posZ;

			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(drawColor.r / 255.f, drawColor.g / 255.f, drawColor.b / 255.f, drawColor.a / 255.f);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDepthMask(false);

			if (ignoreZ) {
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glDisable(GL11.GL_CULL_FACE);
			}

			GL11.glPushMatrix();
			GL11.glTranslated(vecX, vecY, vecZ);

			// Fix default rotation
			GL11.glRotatef(-90F, 1.F, 0.F, 0.F);

			GL11.glRotatef((float) ang1.p, 0.F, 0.F, 1.F);
			GL11.glRotatef((float) ang1.y, 0.F, 1.F, 0.F);
			GL11.glRotatef((float) ang1.r, 1.F, 0.F, 0.F);

			renderDisk.draw(flRadius2, flRadius1, iSlices, 1);

			GL11.glPopMatrix();

			if (ignoreZ) {
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				GL11.glEnable(GL11.GL_CULL_FACE);
			}

			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);

			return 0;
		}
	};

	/**
	 * @author Gregor
	 * @library render
	 * @function DrawCylinder
	 * @info Draws a cylinder in 3D space
	 * @arguments [[Vector]]:pos, [[Number]]:length, [[Number]]:radius1, [ [[Number]]:radius2, [[Angle]]:ang, [[Number]]:numSlices ]
	 * @return nil
	 */

	public static JavaFunction DrawCylinder = new JavaFunction() {
		public int invoke(LuaState l) {
			Vector vec1 = (Vector) l.checkUserdata(1, Vector.class, "Vector");
			float flLength = (float) l.checkNumber(2);
			float flRadius1 = (float) l.checkNumber(3);
			float flRadius2 = (float) l.checkNumber(4, flRadius1);
			Angle ang1 = (Angle) l.checkUserdata(5, Angle.class, "Angle");
			int iSlices = l.checkInteger(6, 24);

			double posX = client.thePlayer.lastTickPosX
					+ (client.thePlayer.posX - client.thePlayer.lastTickPosX) * client.timer.renderPartialTicks;
			double posY = client.thePlayer.lastTickPosY
					+ (client.thePlayer.posY - client.thePlayer.lastTickPosY) * client.timer.renderPartialTicks;
			double posZ = client.thePlayer.lastTickPosZ
					+ (client.thePlayer.posZ - client.thePlayer.lastTickPosZ) * client.timer.renderPartialTicks;

			double vecX = vec1.x - posX, vecY = vec1.z - posY, vecZ = vec1.y - posZ;

			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(drawColor.r / 255.f, drawColor.g / 255.f, drawColor.b / 255.f, drawColor.a / 255.f);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDepthMask(false);

			if (ignoreZ) {
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glDisable(GL11.GL_CULL_FACE);
			}

			GL11.glPushMatrix();
			GL11.glTranslated(vecX, vecY, vecZ);

			// Fix default rotation
			GL11.glRotatef(-90F, 1.F, 0.F, 0.F);

			GL11.glRotatef((float) ang1.p, 0.F, 0.F, 1.F);
			GL11.glRotatef((float) ang1.y, 0.F, 1.F, 0.F);
			GL11.glRotatef((float) ang1.r, 1.F, 0.F, 0.F);

			renderCylinder.draw(flRadius1, flRadius2, flLength, iSlices, iSlices);

			GL11.glPopMatrix();

			if (ignoreZ) {
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				GL11.glEnable(GL11.GL_CULL_FACE);
			}

			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);

			return 0;
		}
	};

	/**
	 * @author Gregor
	 * @library render
	 * @function DrawSphere
	 * @info Draws a sphere in 3D space
	 * @arguments [[Vector]]:pos, [[Number]]:radius, [ [[Number]]:numSlices ]
	 * @return nil
	 */

	public static JavaFunction DrawSphere = new JavaFunction() {
		public int invoke(LuaState l) {
			Vector vec1 = (Vector) l.checkUserdata(1, Vector.class, "Vector");
			float flRadius1 = (float) l.checkNumber(2);
			int iSlices = l.checkInteger(3, 24);

			double posX = client.thePlayer.lastTickPosX
					+ (client.thePlayer.posX - client.thePlayer.lastTickPosX) * client.timer.renderPartialTicks;
			double posY = client.thePlayer.lastTickPosY
					+ (client.thePlayer.posY - client.thePlayer.lastTickPosY) * client.timer.renderPartialTicks;
			double posZ = client.thePlayer.lastTickPosZ
					+ (client.thePlayer.posZ - client.thePlayer.lastTickPosZ) * client.timer.renderPartialTicks;

			double vecX = vec1.x - posX, vecY = vec1.z - posY, vecZ = vec1.y - posZ;

			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(drawColor.r / 255.f, drawColor.g / 255.f, drawColor.b / 255.f, drawColor.a / 255.f);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDepthMask(false);

			if (ignoreZ) {
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glDisable(GL11.GL_CULL_FACE);
			}

			GL11.glPushMatrix();
			GL11.glTranslated(vecX, vecY, vecZ);

			renderSphere.draw(flRadius1, iSlices, iSlices);

			GL11.glPopMatrix();

			if (ignoreZ) {
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				GL11.glEnable(GL11.GL_CULL_FACE);
			}

			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);

			return 0;
		}
	};

	public static void Init(final LuaCraftState l) {
		client = l.getMinecraft();
		currentFont = client.fontRendererObj;

		l.newTable();
		{
			l.pushJavaFunction(IgnoreZ);
			l.setField(-2, "IgnoreZ");
			l.pushJavaFunction(GetDrawColor);
			l.setField(-2, "GetDrawColor");
			l.pushJavaFunction(SetDrawColor);
			l.setField(-2, "SetDrawColor");
			l.pushJavaFunction(DrawText);
			l.setField(-2, "DrawText");
			l.pushJavaFunction(DrawLine);
			l.setField(-2, "DrawLine");
			l.pushJavaFunction(DrawBoundingBox);
			l.setField(-2, "DrawBoundingBox");
			l.pushJavaFunction(DrawCircle);
			l.setField(-2, "DrawCircle");
			l.pushJavaFunction(DrawCylinder);
			l.setField(-2, "DrawCylinder");
			l.pushJavaFunction(DrawSphere);
			l.setField(-2, "DrawSphere");
		}
		l.setGlobal("render");
	}
}
