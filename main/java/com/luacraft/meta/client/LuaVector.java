package com.luacraft.meta.client;

import net.minecraft.util.math.Vec3d;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.luacraft.LuaCraft;
import com.luacraft.LuaCraftState;
import com.luacraft.classes.Vector;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.Entity;

public class LuaVector {
	private static Minecraft client = LuaCraft.getClient();

	static Matrix4f viewMatrix = new Matrix4f();
	static Matrix4f projectionMatrix = new Matrix4f();

	public static Vector3f Vec3TransformCoordinate(Vector3f vec, Matrix4f matrix) {
		Vector3f vOutput = new Vector3f(0, 0, 0);

		vOutput.x = (vec.x * matrix.m00) + (vec.y * matrix.m10) + (vec.z * matrix.m20) + matrix.m30;
		vOutput.y = (vec.x * matrix.m01) + (vec.y * matrix.m11) + (vec.z * matrix.m21) + matrix.m31;
		vOutput.z = (vec.x * matrix.m02) + (vec.y * matrix.m12) + (vec.z * matrix.m22) + matrix.m32;
		float w = 1 / ((vec.x * matrix.m03) + (vec.y * matrix.m13) + (vec.z * matrix.m23) + matrix.m33);

		vOutput.x *= w;
		vOutput.y *= w;
		vOutput.z *= w;

		return vOutput;
	}

	/**
	 * @author Gregor
	 * @function ToScreen Get x, y for a 3D Vector
	 * @arguments [[Vector]]:vec
	 * @return [[Boolean]]:visible, [[Number]]:x, [[Number]]:y
	 */

	public static JavaFunction ToScreen = new JavaFunction() {
		public int invoke(LuaState l) {
			Vector self = (Vector) l.checkUserdata(1, Vector.class, "Vector");

			Entity view = client.getRenderViewEntity();

			Vec3d viewNormal = view.getLook(client.timer.renderPartialTicks).normalize();

			Vec3d camPos = ActiveRenderInfo.getPosition();
			Vec3d eyePos = ActiveRenderInfo.projectViewFromEntity(view, client.timer.renderPartialTicks);

			float vecX = (float) ((camPos.xCoord + eyePos.xCoord) - self.x);
			float vecY = (float) ((camPos.yCoord + eyePos.yCoord) - self.z);
			float vecZ = (float) ((camPos.zCoord + eyePos.zCoord) - self.y);

			Vector3f pos = new Vector3f(vecX, vecY, vecZ);

			viewMatrix.load(ActiveRenderInfo.MODELVIEW.asReadOnlyBuffer());
			projectionMatrix.load(ActiveRenderInfo.PROJECTION.asReadOnlyBuffer());

			pos = Vec3TransformCoordinate(pos, viewMatrix);
			pos = Vec3TransformCoordinate(pos, projectionMatrix);

			ScaledResolution scaledRes = new ScaledResolution(client);
			pos.x = (float) ((scaledRes.getScaledWidth() * (pos.x + 1.0)) / 2.0);
			pos.y = (float) (scaledRes.getScaledHeight() * (1.0 - ((pos.y + 1.0) / 2.0)));

			boolean bVisible = false;

			double dot = viewNormal.xCoord * vecX + viewNormal.yCoord * vecY + viewNormal.zCoord * vecZ;

			if (dot < 0) // We only want vectors that are in front of the player
				bVisible = true;

			l.pushBoolean(bVisible);
			l.pushNumber(pos.x);
			l.pushNumber(pos.y);
			return 3;
		}
	};

	public static void Init(final LuaCraftState l) {
		l.newMetatable("Vector");
		{
			l.pushJavaFunction(ToScreen);
			l.setField(-2, "ToScreen");
		}
		l.pop(1);
	}
}
