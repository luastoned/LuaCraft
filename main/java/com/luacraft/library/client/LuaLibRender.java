package com.luacraft.library.client;

import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Disk;
import org.lwjgl.util.glu.Sphere;

import com.luacraft.LuaCraft;
import com.luacraft.LuaCraftState;
import com.luacraft.LuaUserdata;
import com.luacraft.classes.Angle;
import com.luacraft.classes.Color;
import com.luacraft.classes.Vector;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaRuntimeException;
import com.naef.jnlua.LuaState;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;

public class LuaLibRender {
	private static Minecraft client = LuaCraft.getClient();

	static Tessellator tessellator = Tessellator.getInstance();
	static VertexBuffer renderer = tessellator.getBuffer();

	public static Disk renderDisk = new Disk();
	public static Cylinder renderCylinder = new Cylinder();
	public static Sphere renderSphere = new Sphere();
	public static boolean ignoreZ = false;
	public static int currentTexture = -1;
	public static FontRenderer currentFont = client.fontRendererObj;
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
	 * @arguments [[String]]:text, [[Vector]]:pos, [[Angle]]:ang, [ [[Number]]:scale, [[Number]]:textXpos, [[Number]]:textYpos, [[Boolean]]:shadow ]
	 * @return nil
	 */

	public static JavaFunction DrawText = new JavaFunction() {
		public int invoke(LuaState l) {
			String text = l.checkString(1);
			Vector vec1 = (Vector) l.checkUserdata(2, Vector.class, "Vector");
			Angle ang1 = (Angle) l.checkUserdata(3, Angle.class, "Angle");
			float flScale = (float) l.checkNumber(4, 1) * 0.125F;

			double posX = client.thePlayer.lastTickPosX
					+ (client.thePlayer.posX - client.thePlayer.lastTickPosX) * client.timer.renderPartialTicks;
			double posY = client.thePlayer.lastTickPosY
					+ (client.thePlayer.posY - client.thePlayer.lastTickPosY) * client.timer.renderPartialTicks;
			double posZ = client.thePlayer.lastTickPosZ
					+ (client.thePlayer.posZ - client.thePlayer.lastTickPosZ) * client.timer.renderPartialTicks;

			double vecX = vec1.x - posX, vecY = vec1.z - posY, vecZ = vec1.y - posZ;

			if (ignoreZ) {
				GlStateManager.disableDepth();
				GlStateManager.disableCull();
			}

			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.depthMask(false);

			GlStateManager.pushMatrix();
			GlStateManager.translate(vecX, vecY, vecZ);

			GlStateManager.rotate((float) (90 - ang1.p), 1, 0, 0);
			GlStateManager.rotate((float) (180 - ang1.y), 0, 1, 0);
			GlStateManager.rotate((float) (ang1.r), 0, 0, 1);

			GlStateManager.scale(flScale, flScale, flScale);

			currentFont.drawString(text, l.checkInteger(5, 0), l.checkInteger(6, 0), drawColor.getRGBA(),
					l.checkBoolean(7, false));

			GlStateManager.popMatrix();
			GlStateManager.depthMask(true);
			GlStateManager.disableBlend();

			if (ignoreZ) {
				GlStateManager.enableDepth();
				GlStateManager.enableCull();
			}

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

			if (ignoreZ) {
				GlStateManager.disableDepth();
				GlStateManager.disableCull();
			}

			GL11.glLineWidth(lineWidth);

			GlStateManager.disableTexture2D();
			GlStateManager.enableBlend();
			GlStateManager.disableAlpha();
			GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
			GlStateManager.shadeModel(GL11.GL_SMOOTH);

			renderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
			renderer.pos(minX, minY, minZ).color(drawColor.r, drawColor.g, drawColor.b, drawColor.a).endVertex();
			renderer.pos(maxX, maxY, maxZ).color(drawColor.r, drawColor.g, drawColor.b, drawColor.a).endVertex();
			tessellator.draw();

			GlStateManager.shadeModel(GL11.GL_FLAT);
			GlStateManager.disableBlend();
			GlStateManager.enableAlpha();
			GlStateManager.enableTexture2D();

			if (ignoreZ) {
				GlStateManager.enableDepth();
				GlStateManager.enableCull();
			}

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

			if (ignoreZ) {
				GlStateManager.disableDepth();
				GlStateManager.disableCull();
			}

			GlStateManager.disableTexture2D();
			GlStateManager.enableBlend();
			GlStateManager.disableAlpha();
			GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
			GlStateManager.shadeModel(GL11.GL_SMOOTH);

			GlStateManager.color(drawColor.r / 255.f, drawColor.g / 255.f, drawColor.b / 255.f, drawColor.a / 255.f);

			renderer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
			renderer.pos(minX, minY, minZ).endVertex();
			renderer.pos(maxX, minY, minZ).endVertex();
			renderer.pos(maxX, minY, maxZ).endVertex();
			renderer.pos(minX, minY, maxZ).endVertex();
			renderer.pos(minX, minY, minZ).endVertex();
			tessellator.draw();
			renderer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
			renderer.pos(minX, maxY, minZ).endVertex();
			renderer.pos(maxX, maxY, minZ).endVertex();
			renderer.pos(maxX, maxY, maxZ).endVertex();
			renderer.pos(minX, maxY, maxZ).endVertex();
			renderer.pos(minX, maxY, minZ).endVertex();
			tessellator.draw();
			renderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
			renderer.pos(minX, minY, minZ).endVertex();
			renderer.pos(minX, maxY, minZ).endVertex();
			renderer.pos(maxX, minY, minZ).endVertex();
			renderer.pos(maxX, maxY, minZ).endVertex();
			renderer.pos(maxX, minY, maxZ).endVertex();
			renderer.pos(maxX, maxY, maxZ).endVertex();
			renderer.pos(minX, minY, maxZ).endVertex();
			renderer.pos(minX, maxY, maxZ).endVertex();
			tessellator.draw();

			GlStateManager.shadeModel(GL11.GL_FLAT);
			GlStateManager.disableBlend();
			GlStateManager.enableAlpha();
			GlStateManager.enableTexture2D();

			if (ignoreZ) {
				GlStateManager.enableDepth();
				GlStateManager.enableCull();
			}
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

			if (ignoreZ) {
				GlStateManager.disableDepth();
				GlStateManager.disableCull();
			}

			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.disableTexture2D();
			GlStateManager.depthMask(false);

			GlStateManager.color(drawColor.r / 255.f, drawColor.g / 255.f, drawColor.b / 255.f, drawColor.a / 255.f);

			GlStateManager.pushMatrix();
			GlStateManager.translate(vecX, vecY, vecZ);

			GL11.glRotated(90 - ang1.p, 1, 0, 0);
			GL11.glRotated(180 - ang1.y, 0, 1, 0);
			GL11.glRotated(ang1.r, 0, 0, 1);

			renderDisk.draw(flRadius2, flRadius1, iSlices, 1);

			GlStateManager.popMatrix();

			GlStateManager.depthMask(true);
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();

			if (ignoreZ) {
				GlStateManager.enableDepth();
				GlStateManager.enableCull();
			}

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

			if (ignoreZ) {
				GlStateManager.disableDepth();
				GlStateManager.disableCull();
			}

			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.disableTexture2D();
			GlStateManager.depthMask(false);

			GlStateManager.color(drawColor.r / 255.f, drawColor.g / 255.f, drawColor.b / 255.f, drawColor.a / 255.f);

			GlStateManager.pushMatrix();
			GlStateManager.translate(vecX, vecY, vecZ);

			GL11.glRotated(90 - ang1.p, 1, 0, 0);
			GL11.glRotated(180 - ang1.y, 0, 1, 0);
			GL11.glRotated(ang1.r, 0, 0, 1);

			renderCylinder.draw(flRadius1, flRadius2, flLength, iSlices, iSlices);

			GlStateManager.popMatrix();

			GlStateManager.depthMask(true);
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();

			if (ignoreZ) {
				GlStateManager.enableDepth();
				GlStateManager.enableCull();
			}

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

			if (ignoreZ) {
				GlStateManager.disableDepth();
				GlStateManager.disableCull();
			}

			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.disableTexture2D();
			GlStateManager.depthMask(false);

			GlStateManager.color(drawColor.r / 255.f, drawColor.g / 255.f, drawColor.b / 255.f, drawColor.a / 255.f);

			GlStateManager.pushMatrix();
			GlStateManager.translate(vecX, vecY, vecZ);

			renderSphere.draw(flRadius1, iSlices, iSlices);

			GlStateManager.popMatrix();

			GlStateManager.depthMask(true);
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();

			if (ignoreZ) {
				GlStateManager.enableDepth();
				GlStateManager.enableCull();
			}

			return 0;
		}
	};

	/**
	 * @author Jake
	 * @library render
	 * @function EyeAngles
	 * @info Get your local player's camera position
	 * @arguments nil
	 * @return [[Vector]]:camera
	 */

	public static JavaFunction EyeAngles = new JavaFunction() {
		public int invoke(LuaState l) {
			Entity view = client.getRenderViewEntity();
			float p = view.prevRotationPitch
					+ (view.rotationPitch - view.prevRotationPitch) * client.timer.renderPartialTicks;
			float y = view.prevRotationYaw
					+ (view.rotationYaw - view.prevRotationYaw) * client.timer.renderPartialTicks;
			Angle ang = new Angle(p, y);
			ang.push(l);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @library render
	 * @function EyePos
	 * @info Get your local player's camera position
	 * @arguments nil
	 * @return [[Vector]]:camera
	 */

	public static JavaFunction EyePos = new JavaFunction() {
		public int invoke(LuaState l) {
			Entity view = client.getRenderViewEntity();
			Vec3d camPos = ActiveRenderInfo.getPosition();
			Vec3d eyePos = ActiveRenderInfo.projectViewFromEntity(view, client.timer.renderPartialTicks);

			double x = camPos.xCoord + eyePos.xCoord;
			double y = camPos.yCoord + eyePos.yCoord;
			double z = camPos.zCoord + eyePos.zCoord;

			Vector pos = new Vector(x, z, y);
			pos.push(l);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @library render
	 * @function GetViewEntity
	 * @info Returns the entity the camera is using to see with
	 * @arguments nil
	 * @return [[Entity]]:view
	 */

	public static JavaFunction GetViewEntity = new JavaFunction() {
		public int invoke(LuaState l) {
			Entity view = client.getRenderViewEntity();
			LuaUserdata.PushUserdata(l, view);
			return 1;
		}
	};

	/**
	 * @author Matt
	 * @library render
	 * @function ScrW
	 * @info Get the screen width
	 * @arguments nil
	 * @return [[Number]]:width
	 */

	public static JavaFunction ScrW = new JavaFunction() {
		public int invoke(LuaState l) {
			ScaledResolution scale = new ScaledResolution(client);
			l.pushNumber(scale.getScaledWidth());
			return 1;
		}
	};

	/**
	 * @author Matt
	 * @library render
	 * @function ScrH
	 * @info Get the screen height
	 * @arguments nil
	 * @return [[Number]]:height
	 */

	public static JavaFunction ScrH = new JavaFunction() {
		public int invoke(LuaState l) {
			ScaledResolution scale = new ScaledResolution(client);
			l.pushNumber(scale.getScaledHeight());
			return 1;
		}
	};

	public static void Init(final LuaCraftState l) {
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

			l.pushJavaFunction(EyeAngles);
			l.setField(-2, "EyeAngles");
			l.pushJavaFunction(EyePos);
			l.setField(-2, "EyePos");
			l.pushJavaFunction(GetViewEntity);
			l.setField(-2, "GetViewEntity");
			l.pushJavaFunction(ScrW);
			l.setField(-2, "ScrW");
			l.pushJavaFunction(ScrH);
			l.setField(-2, "ScrH");
		}
		l.setGlobal("render");
	}
}
