package com.luacraft.classes;

import net.minecraft.util.MathHelper;

import com.naef.jnlua.LuaState;
import com.naef.jnlua.LuaUserdata;

public class Angle implements LuaUserdata
{
	public double p = 0;
	public double y = 0;
	public double r = 0;

	public Angle()
	{
		p = 0;
		y = 0;
		r = 0;
	}

	public Angle(double p, double y)
	{
		this.p = p;
		this.y = y;
		r = 0;
	}

	public Angle(double p, double y, double r)
	{
		this.p = p;
		this.y = y;
		this.r = r;
	}
	
	public Angle copy()
	{
		return new Angle(p, y, r);
	}

	public void push(LuaState l)
	{
		l.pushUserdataWithMeta(this, "Angle");
	}

	public boolean equals(Angle other)
	{
		if (p != other.p) return false;
		if (y != other.y) return false;
		if (r != other.r) return false;
		return false;
	}

	public Angle add(Angle other)
	{
		p += other.p;
		y += other.y;
		r += other.r;
		return this;
	}

	public Angle sub(Angle other)
	{
		p -= other.p;
		y -= other.y;
		r -= other.r;
		return this;
	}

	public Angle mul(Angle other)
	{
		p *= other.p;
		y *= other.y;
		r *= other.r;
		return this;
	}

	public Angle mul(double other)
	{
		p *= other;
		y *= other;
		r *= other;
		return this;
	}

	public Angle div(Angle other)
	{
		p /= other.p;
		y /= other.y;
		r /= other.r;
		return this;
	}

	public Angle div(double other)
	{
		p /= other;
		y /= other;
		r /= other;
		return this;
	}

	public Vector forward()
	{
		float pitch = (float) Math.toRadians(p);
		float yaw = (float) Math.toRadians(y);
		float roll = (float) Math.toRadians(r);

		double x = (MathHelper.sin(roll) * MathHelper.sin(pitch)) + (MathHelper.cos(roll) * MathHelper.sin(yaw) * MathHelper.cos(pitch));
		double y = (-MathHelper.cos(roll) * MathHelper.sin(pitch)) + (MathHelper.sin(roll) * MathHelper.sin(yaw) * MathHelper.cos(pitch));
		double z = MathHelper.cos(yaw) * MathHelper.cos(pitch);

		return new Vector(x,y,z);
	}

	public Vector right()
	{
		float yaw = (float) Math.toRadians(y);
		float roll = (float) Math.toRadians(r);

		double x = MathHelper.cos(roll) * MathHelper.cos(yaw);
		double y = MathHelper.sin(roll) * MathHelper.cos(yaw);
		double z = -MathHelper.sin(yaw);

		return new Vector(x,y,z);
	}

	public Vector up()
	{
		float pitch = (float) Math.toRadians(p);
		float yaw = (float) Math.toRadians(y);
		float roll = (float) Math.toRadians(r);

		double x = (-MathHelper.sin(roll) * MathHelper.cos(pitch)) + (MathHelper.cos(roll) * MathHelper.sin(yaw) * MathHelper.sin(pitch));
		double y = (MathHelper.cos(roll) * MathHelper.cos(pitch)) + (MathHelper.sin(roll) * MathHelper.sin(yaw) * MathHelper.sin(pitch));
		double z = MathHelper.cos(yaw) * MathHelper.sin(pitch);

		return new Vector(x,y,z);
	}

	public  String toString() {
		return String.format("Angle [%.3f, %.3f, %.3f]", p, y, r);
	}

	public String getTypeName() {
		return "Angle";
	}
}