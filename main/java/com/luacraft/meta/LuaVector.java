package com.luacraft.meta;

import com.luacraft.classes.Angle;
import com.luacraft.classes.Vector;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

public class LuaVector {
	public static JavaFunction __index = new JavaFunction() {
		public int invoke(LuaState l) {
			Vector self = (Vector) l.checkUserdata(1, Vector.class, "Vector");
			String key = l.checkString(2);

			if (key.equals("x"))
				l.pushNumber(self.x);
			else if (key.equals("y"))
				l.pushNumber(self.y);
			else if (key.equals("z"))
				l.pushNumber(self.z);
			else {
				l.getMetatable(1);
				l.pushString(key);
				l.rawGet(-2);
			}

			return 1;
		}
	};

	public static JavaFunction __newindex = new JavaFunction() {
		public int invoke(LuaState l) {
			Vector self = (Vector) l.checkUserdata(1, Vector.class, "Vector");
			String key = l.checkString(2);
			double val = l.toNumber(3);

			if (key.equals("x"))
				self.x = val;
			else if (key.equals("y"))
				self.y = val;
			else if (key.equals("z"))
				self.z = val;

			return 0;
		}
	};

	public static JavaFunction __tostring = new JavaFunction() {
		public int invoke(LuaState l) {
			Vector self = (Vector) l.checkUserdata(1, Vector.class, "Vector");
			l.pushString(self.toString());
			return 1;
		}
	};

	public static JavaFunction __eq = new JavaFunction() {
		public int invoke(LuaState l) {
			Vector self = (Vector) l.checkUserdata(1, Vector.class, "Vector");
			Vector other = (Vector) l.checkUserdata(2, Vector.class, "Vector");
			l.pushBoolean(self.equals(other));
			return 1;
		}
	};

	public static JavaFunction __add = new JavaFunction() {
		public int invoke(LuaState l) {
			Vector self = (Vector) l.checkUserdata(1, Vector.class, "Vector");
			Vector other = (Vector) l.checkUserdata(2, Vector.class, "Vector");
			self.copy().add(other).push(l);
			return 1;
		}
	};

	public static JavaFunction __sub = new JavaFunction() {
		public int invoke(LuaState l) {
			Vector self = (Vector) l.checkUserdata(1, Vector.class, "Vector");
			Vector other = (Vector) l.checkUserdata(2, Vector.class, "Vector");
			self.copy().sub(other).push(l);
			return 1;
		}
	};

	public static JavaFunction __mul = new JavaFunction() {
		public int invoke(LuaState l) {
			Vector self = (Vector) l.checkUserdata(1, Vector.class, "Vector");
			Vector ret = self.copy();
			if (l.isUserdata(2, Vector.class)) {
				Vector other = (Vector) l.checkUserdata(2, Vector.class, "Vector");
				ret.mul(other);
			} else if (l.isNumber(2)) {
				double other = l.toNumber(2);
				ret.mul(other);
			}
			ret.push(l);
			return 1;
		}
	};

	public static JavaFunction __div = new JavaFunction() {
		public int invoke(LuaState l) {
			Vector self = (Vector) l.checkUserdata(1, Vector.class, "Vector");
			Vector ret = self.copy();
			if (l.isUserdata(2, Vector.class)) {
				Vector other = (Vector) l.checkUserdata(2, Vector.class, "Vector");
				ret.div(other);
			} else if (l.isNumber(2)) {
				double other = l.toNumber(2);
				ret.div(other);
			}
			ret.push(l);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function Length
	 * @info Get the length of a vector
	 * @arguments nil
	 * @return [[Number]]:len
	 */

	public static JavaFunction Length = new JavaFunction() {
		public int invoke(LuaState l) {
			Vector self = (Vector) l.checkUserdata(1, Vector.class, "Vector");
			l.pushNumber(self.length());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function Distance
	 * @info Get the distance between two vectors
	 * @arguments [[Vector]]:vector
	 * @return [[Number]]:distance
	 */

	public static JavaFunction Distance = new JavaFunction() {
		public int invoke(LuaState l) {
			Vector self = (Vector) l.checkUserdata(1, Vector.class, "Vector");
			Vector other = (Vector) l.checkUserdata(2, Vector.class, "Vector");
			l.pushNumber(self.distance(other));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function DistanceSqr
	 * @info Get the distance between two vectors, squared
	 * @arguments [[Vector]]:vector
	 * @return [[Number]]:distance
	 */

	public static JavaFunction DistanceSqr = new JavaFunction() {
		public int invoke(LuaState l) {
			Vector self = (Vector) l.checkUserdata(1, Vector.class, "Vector");
			Vector other = (Vector) l.checkUserdata(2, Vector.class, "Vector");
			l.pushNumber(self.distanceSqr(other));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetNormal
	 * @info Get the normalized vector
	 * @arguments nil
	 * @return [[Vector]]:norm
	 */

	public static JavaFunction GetNormal = new JavaFunction() {
		public int invoke(LuaState l) {
			Vector self = (Vector) l.checkUserdata(1, Vector.class, "Vector");
			self.getNormal().push(l);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function Normalize
	 * @info Normalize the vector. Doesn't return anything, but it directly modifies the vector.
	 * @arguments nil
	 * @return nil
	 */

	public static JavaFunction Normalize = new JavaFunction() {
		public int invoke(LuaState l) {
			Vector self = (Vector) l.checkUserdata(1, Vector.class, "Vector");
			self.normalize();
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function Dot
	 * @info Get the dot product of a vector
	 * @arguments [[Vector]]:vector
	 * @return [[Number]]:dot
	 */

	public static JavaFunction DotProduct = new JavaFunction() {
		public int invoke(LuaState l) {
			Vector self = (Vector) l.checkUserdata(1, Vector.class, "Vector");
			Vector other = (Vector) l.checkUserdata(2, Vector.class, "Vector");
			l.pushNumber(self.dotProduct(other));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function Cross
	 * @info Get the dot product of a vector
	 * @arguments [[Vector]]:vector
	 * @return [[Vector]]:cross
	 */

	public static JavaFunction Cross = new JavaFunction() {
		public int invoke(LuaState l) {
			Vector self = (Vector) l.checkUserdata(1, Vector.class, "Vector");
			Vector other = (Vector) l.checkUserdata(2, Vector.class, "Vector");
			self.cross(other).push(l);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function RayQuadIntersect
	 * @info Return a UV point on a plane
	 * @arguments [[Vector]]:direction, [[Vector]]:plane, [[Vector]]:xcoord, [[Vector]]:ycoord
	 * @return [[Number]]:u, [[Number]]:v
	 */

	public static JavaFunction RayQuadIntersect = new JavaFunction() {
		public int invoke(LuaState l) {
			Vector self = (Vector) l.checkUserdata(1, Vector.class, "Vector");
			Vector dir = (Vector) l.checkUserdata(2, Vector.class, "Vector");
			Vector plane = (Vector) l.checkUserdata(2, Vector.class, "Vector");
			Vector x = (Vector) l.checkUserdata(2, Vector.class, "Vector");
			Vector y = (Vector) l.checkUserdata(2, Vector.class, "Vector");

			Vector result = self.rayQuadIntersect(dir, plane, x, y);

			if (result != null) {
				l.pushNumber(result.x);
				l.pushNumber(result.y);
				return 2;
			}

			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function Angle
	 * @info Converts a vector into an angle (pitch and yaw)
	 * @arguments [[Vector]]:vector
	 * @return [[Angle]]:angle
	 */

	public static JavaFunction Angle = new JavaFunction() {
		public int invoke(LuaState l) {
			Vector self = (Vector) l.checkUserdata(1, Vector.class, "Vector");

			double pitch = Math.toDegrees(Math.asin(-self.y));
			double yaw = Math.toDegrees(Math.atan2(self.x, self.z));

			Angle ang = new Angle(pitch, yaw);
			ang.push(l);
			return 1;
		}
	};

	public static void Init(final LuaState l) {
		l.newMetatable("Vector");
		{
			l.pushJavaFunction(__index);
			l.setField(-2, "__index");

			l.pushJavaFunction(__newindex);
			l.setField(-2, "__newindex");

			l.pushJavaFunction(__tostring);
			l.setField(-2, "__tostring");

			l.pushJavaFunction(__eq);
			l.setField(-2, "__eq");
			l.pushJavaFunction(__add);
			l.setField(-2, "__add");
			l.pushJavaFunction(__sub);
			l.setField(-2, "__sub");
			l.pushJavaFunction(__mul);
			l.setField(-2, "__mul");
			l.pushJavaFunction(__div);
			l.setField(-2, "__div");

			l.pushJavaFunction(Length);
			l.setField(-2, "Length");
			l.pushJavaFunction(Distance);
			l.setField(-2, "Distance");
			l.pushJavaFunction(GetNormal);
			l.setField(-2, "GetNormal");
			l.pushJavaFunction(Normalize);
			l.setField(-2, "Normalize");
			l.pushJavaFunction(DotProduct);
			l.setField(-2, "DotProduct");
			l.pushJavaFunction(DotProduct);
			l.setField(-2, "Dot");
			l.pushJavaFunction(Cross);
			l.setField(-2, "Cross");
			l.pushJavaFunction(RayQuadIntersect);
			l.setField(-2, "RayQuadIntersect");
			l.pushJavaFunction(Angle);
			l.setField(-2, "Angle");
		}
		l.pop(1);
	}
}