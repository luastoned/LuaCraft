package com.luacraft.meta.client;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.luacraft.LuaCraftState;
import com.luacraft.classes.Vector;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;

public class LuaVector {
	private static Minecraft client = null;

	static Matrix4f viewMatrix = new Matrix4f();
	static Matrix4f projectionMatrix = new Matrix4f();

	public static Vector3f Vec3Transform(Vector3f vec, Matrix4f matrix) {
		Vector3f vOutput = new Vector3f(0, 0, 0);

		vOutput.x = (vec.x * matrix.m00) + (vec.y * matrix.m10) + (vec.z * matrix.m20) + matrix.m30;
		vOutput.y = (vec.x * matrix.m01) + (vec.y * matrix.m11) + (vec.z * matrix.m21) + matrix.m31;
		vOutput.z = (vec.x * matrix.m02) + (vec.y * matrix.m12) + (vec.z * matrix.m22) + matrix.m32;

		return vOutput;
	}

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
	 * @return [[Number]]:x, [[Number]]:y, [[Boolean]]:visible
	 */

	public static JavaFunction ToScreen = new JavaFunction() {
		public int invoke(LuaState l) {
			Vector self = (Vector) l.checkUserdata(1, Vector.class, "Vector");

			Entity view = client.getRenderViewEntity();

			Vec3 viewNormal = view.getLook(client.timer.renderPartialTicks).normalize();
			Vec3 eyePos = ActiveRenderInfo.projectViewFromEntity(view, client.timer.renderPartialTicks);

			float vecX = (float) (eyePos.xCoord - self.x);
			float vecY = (float) (eyePos.zCoord - self.y);
			float vecZ = (float) (eyePos.yCoord - self.z);

			Vector3f viewVec = new Vector3f(vecX, vecZ, vecY);

			viewMatrix.load(ActiveRenderInfo.field_178812_b.asReadOnlyBuffer());
			projectionMatrix.load(ActiveRenderInfo.field_178813_c.asReadOnlyBuffer());

			viewVec = Vec3TransformCoordinate(viewVec, viewMatrix);
			viewVec = Vec3TransformCoordinate(viewVec, projectionMatrix);

			ScaledResolution scaledRes = new ScaledResolution(client, client.displayWidth, client.displayHeight);
			viewVec.x = (float) ((scaledRes.getScaledWidth() * (viewVec.x + 1.0)) / 2.0);
			viewVec.y = (float) (scaledRes.getScaledHeight() * (1.0 - ((viewVec.y + 1.0) / 2.0)));

			boolean bVisible = false;
			double w = viewNormal.dotProduct(new Vec3(vecX, vecZ, vecY));

			if (w < 0) // We only want vectors that are in front of the player
				bVisible = true;

			l.pushNumber(viewVec.x);
			l.pushNumber(viewVec.y);
			l.pushBoolean(bVisible);
			return 3;
		}
	};

	public static void Init(final LuaCraftState l) {
		client = l.getMinecraft();

		l.newMetatable("Vector");
		{
			l.pushJavaFunction(ToScreen);
			l.setField(-2, "ToScreen");
		}
		l.pop(1);
	}
}
