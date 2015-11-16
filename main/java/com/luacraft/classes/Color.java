package com.luacraft.classes;

import com.naef.jnlua.LuaState;
import com.naef.jnlua.LuaUserdata;

public class Color implements LuaUserdata {
	public int r;
	public int g;
	public int b;
	public int a;

	public Color(int red, int green, int blue, int alpha) {
		r = red;
		g = green;
		b = blue;
		a = alpha;
	}

	public void push(LuaState l) {
		l.pushUserdataWithMeta(this, "Color");
	}

	public int getRGB() {
		return (r << 16) + (g << 8) + (b << 0) + (a << 24);
	}

	public Color add(Color other) {
		r += other.r;
		g += other.g;
		b += other.b;
		a += other.a;
		return this;
	}

	public Color sub(Color other) {
		r -= other.r;
		g -= other.g;
		b -= other.b;
		a -= other.a;
		return this;
	}

	public Color mul(Color other) {
		r *= other.r;
		g *= other.g;
		b *= other.b;
		a *= other.a;
		return this;
	}

	public Color mul(double other) {
		r *= other;
		g *= other;
		b *= other;
		a *= other;
		return this;
	}

	public Color div(Color other) {
		r /= other.r;
		g /= other.g;
		b /= other.b;
		a /= other.a;
		return this;
	}

	public Color div(double other) {
		r /= other;
		g /= other;
		b /= other;
		a /= other;
		return this;
	}

	public boolean equals(Color other) {
		if (r != other.r)
			return false;
		if (g != other.g)
			return false;
		if (b != other.b)
			return false;
		if (a != other.a)
			return false;
		return true;
	}

	public Color fadeTo(Color other, float frac) {
		int red = (int) (r * (1 - frac) + other.r * frac);
		int green = (int) (g * (1 - frac) + other.g * frac);
		int blue = (int) (b * (1 - frac) + other.b * frac);
		int alpha = (int) (a * (1 - frac) + other.a * frac);
		return new Color(red, green, blue, alpha);
	}

	public String toString() {
		return String.format("Color [%d, %d, %d %d][#%08X]", r, g, b, a,
				getRGB());
	}

	public String getTypeName() {
		return "Color";
	}
}
