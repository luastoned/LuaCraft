package com.luacraft.classes;

import com.naef.jnlua.LuaState;
import com.naef.jnlua.LuaUserdata;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Vector implements LuaUserdata {
	public double x = 0;
	public double y = 0;
	public double z = 0;

	public Vector() {
		x = 0;
		y = 0;
		z = 0;
	}

	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector(Vec3d vec) {
		x = vec.xCoord;
		y = vec.zCoord;
		z = vec.yCoord;
	}

	public Vector(BlockPos vec) {
		x = vec.getX();
		y = vec.getZ();
		z = vec.getY();
	}

	public Vector copy() {
		return new Vector(x, y, z);
	}

	public void push(LuaState l) {
		l.pushUserdataWithMeta(this, "Vector");
	}

	public boolean equals(Vector other) {
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		return true;
	}

	public Vector add(Vector other) {
		x += other.x;
		y += other.y;
		z += other.z;
		return this;
	}

	public Vector sub(Vector other) {
		x -= other.x;
		y -= other.y;
		z -= other.z;
		return this;
	}

	public Vector mul(Vector other) {
		x *= other.x;
		y *= other.y;
		z *= other.z;
		return this;
	}

	public Vector mul(double other) {
		x *= other;
		y *= other;
		z *= other;
		return this;
	}

	public Vector div(Vector other) {
		x /= other.x;
		y /= other.y;
		z /= other.z;
		return this;
	}

	public Vector div(double other) {
		x /= other;
		y /= other;
		z /= other;
		return this;
	}

	public double length() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	public double length2D() {
		return Math.sqrt(x * x + y * y);
	}

	public double distance(Vector other) {
		Vector result = copy();
		return result.sub(other).length();
	}

	public double distanceSqr(Vector other) {
		return Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2) + Math.pow(z - other.z, 2);
	}

	public Vector getNormal() {
		double len = length();
		return new Vector(x / len, y / len, z / len);
	}

	public Vector normalize() {
		double len = length();
		x /= len;
		y /= len;
		z /= len;
		return this;
	}

	public double dotProduct(Vector other) {
		return other.x * x + other.y * y + other.z * z;
	}

	public Vector cross(Vector other) {
		return new Vector(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x);
	}

	public Vector rayQuadIntersect(Vector dir, Vector plane, Vector x, Vector y) {
		Vector cross = dir.cross(plane);
		double d = x.dotProduct(cross);

		if (d <= 0.0)
			return null;

		Vector vt = sub(plane);
		double u = vt.dotProduct(cross);

		if (u < 0.0 || u > d)
			return null;

		double v = dir.dotProduct(vt.cross(x));
		if (v < 0.0 || v > d)
			return null;

		return new Vector(u / d, v / d, 0);
	}

	public Vec3d toVec3d() {
		return new Vec3d(x, z, y);
	}

	public String toString() {
		return String.format("Vector [%.3f, %.3f, %.3f]", x, y, z);
	}

	public String getTypeName() {
		return "Vector";
	}
}
