package com.luacraft.library;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.luacraft.LuaCraftState;
import com.luacraft.LuaUserdataManager;
import com.luacraft.classes.LuaJavaBlock;
import com.luacraft.classes.Vector;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaRuntimeException;
import com.naef.jnlua.LuaState;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

public class LuaLibUtil {

	public static String base64encode(String str) {
		return Base64.encode(str.getBytes());
	}

	public static byte[] base64decode(String str) {
		return Base64.decode(str);
	}

	public static byte[] compress(String str) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(str.getBytes());
		gzip.close();

		byte[] ret = out.toByteArray();
		out.close();

		return ret;
	}

	public static String decompress(byte[] bytes) throws IOException {
		GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(bytes));
		BufferedReader reader = new BufferedReader(new InputStreamReader(gzip, "UTF-8"));

		StringBuilder out = new StringBuilder();
		String line;

		while ((line = reader.readLine()) != null)
			out.append(line);

		gzip.close();
		reader.close();

		return out.toString();
	}

	private static Entity traceEntity(World world, Vec3 start, Vec3 end) {
		double d0 = start.distanceTo(end);
		double d1 = d0;
		Vec3 vec3 = start;
		Vec3 vec31 = start.subtract(end);
		Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
		Entity pointedEntity = null;
		Vec3 vec33 = null;
		float f1 = 1.0F;

		AxisAlignedBB bb = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);

		List list = world.getEntitiesWithinAABB(Entity.class,
				bb.addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand((double) f1, (double) f1,
						(double) f1));
		double d2 = d1;

		for (int i = 0; i < list.size(); ++i) {
			Entity entity1 = (Entity) list.get(i);

			if (entity1.canBeCollidedWith()) {
				float f2 = entity1.getCollisionBorderSize();
				AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double) f2, (double) f2,
						(double) f2);
				MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

				if (axisalignedbb.isVecInside(vec3)) {
					if (0.0D < d2 || d2 == 0.0D) {
						pointedEntity = entity1;
						vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
						d2 = 0.0D;
					}
				} else if (movingobjectposition != null) {
					double d3 = vec3.distanceTo(movingobjectposition.hitVec);

					if (d3 < d2 || d2 == 0.0D) {
						pointedEntity = entity1;
						vec33 = movingobjectposition.hitVec;
						d2 = d3;
					}
				}
			}
		}

		if (pointedEntity != null && d2 < d1)
			return pointedEntity;

		return null;
	}

	public static void pushTrace(LuaState l, World world, Vector start, Vector endpos, boolean hitWater) {
		MovingObjectPosition trace = world.rayTraceBlocks(start.toVec3(), endpos.toVec3(), hitWater);

		AxisAlignedBB bb = new AxisAlignedBB(-0.5, -0.5, -0.5, 0.5, 0.5, 0.5);

		Vec3 size = new Vec3(0.5, 0.5, 0.5);

		Entity hitEnt = traceEntity(world, start.toVec3(), endpos.toVec3());

		l.newTable();

		start.push(l);
		l.setField(-2, "StartPos");

		if (trace == null) {
			l.pushBoolean(false);
			l.setField(-2, "Hit");

			endpos.push(l);
			l.setField(-2, "HitPos");
		} else {
			l.pushBoolean(true);
			l.setField(-2, "Hit");

			Vector hitpos = new Vector(trace.hitVec);

			hitpos.push(l);
			l.setField(-2, "HitPos");

			if (hitEnt != null) {
				LuaUserdataManager.PushUserdata(l, hitEnt);
				l.setField(-2, "HitEntity");
			} else {
				LuaJavaBlock thisBlock = new LuaJavaBlock(world, trace.getBlockPos());
				LuaUserdataManager.PushUserdata(l, thisBlock);
				l.setField(-2, "HitBlock");

				((LuaCraftState) l).pushFace(trace.sideHit);
				l.setField(-2, "HitNormal");
			}
		}
	}

	/**
	 * @author Jake
	 * @library util
	 * @function CRC32
	 * @info Get a CRC32-bit number of a string
	 * @arguments nil
	 * @return [[Number]]:crc32
	 */

	public static JavaFunction CRC32 = new JavaFunction() {
		public int invoke(LuaState l) {
			CRC32 crc = new CRC32();
			crc.update(l.checkByteArray(1));
			l.pushNumber(crc.getValue());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @library util
	 * @function Encrypt
	 * @info Encrypt a string using the specified algorithm Defaults to MD5
	 * @arguments [[String]]:input, [ [[String]]:algorithm ]
	 * @return [[String]]:encrypted
	 */

	public static JavaFunction Encrypt = new JavaFunction() {
		public int invoke(LuaState l) {
			String mode = l.checkString(2, "MD5");
			MessageDigest ecrypt;
			try {
				ecrypt = MessageDigest.getInstance(mode);
			} catch (NoSuchAlgorithmException e) {
				throw new LuaRuntimeException("Invalid crypto type: " + mode);
			}
			ecrypt.update(l.checkByteArray(1));
			l.pushString(HexBin.encode(ecrypt.digest()));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @library util
	 * @function Base64Encode
	 * @info Encode a string to Base64
	 * @arguments nil
	 * @return [[String]]:data
	 */

	public static JavaFunction Base64Encode = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushString(base64encode(l.checkString(1)));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @library util
	 * @function Base64Decode
	 * @info Decode Base64 encoded data back to a string
	 * @arguments nil
	 * @return [[String]]:string
	 */

	public static JavaFunction Base64Decode = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushByteArray(base64decode(l.checkString(1)));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @library util
	 * @function Compress
	 * @info Compress a string using GZIP
	 * @arguments nil
	 * @return [[String]]:data
	 */

	public static JavaFunction Compress = new JavaFunction() {
		public int invoke(LuaState l) {
			try {
				l.pushByteArray(compress(l.checkString(1)));
			} catch (IOException e) {
				throw new LuaRuntimeException(e);
			}
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @library util
	 * @function Decompress
	 * @info Decompress a string using GZIP
	 * @arguments nil
	 * @return [[String]]:string
	 */

	public static JavaFunction Decompress = new JavaFunction() {
		public int invoke(LuaState l) {
			try {
				l.pushString(decompress(l.checkByteArray(1)));
			} catch (IOException e) {
				throw new LuaRuntimeException(e);
			}
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @library util
	 * @function GetOS
	 * @info Get the name of the OS
	 * @arguments nil
	 * @return [[String]]:os
	 */

	public static JavaFunction GetOS = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushString(System.getProperty("os.name"));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @library util
	 * @function GetArchitecture
	 * @info Get the architecture of the computer
	 * @arguments nil
	 * @return [[String]]:arch
	 */

	public static JavaFunction GetArchitecture = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushString(System.getProperty("os.arch"));
			return 1;
		}
	};

	public static void Init(final LuaCraftState l) {
		l.newTable();
		{
			l.pushJavaFunction(CRC32);
			l.setField(-2, "CRC32");
			l.pushJavaFunction(Encrypt);
			l.setField(-2, "Encrypt");
			l.pushJavaFunction(Base64Encode);
			l.setField(-2, "Base64Encode");
			l.pushJavaFunction(Base64Decode);
			l.setField(-2, "Base64Decode");
			l.pushJavaFunction(Compress);
			l.setField(-2, "Compress");
			l.pushJavaFunction(Decompress);
			l.setField(-2, "Decompress");
			l.pushJavaFunction(GetOS);
			l.setField(-2, "GetOS");
			l.pushJavaFunction(GetArchitecture);
			l.setField(-2, "GetArchitecture");
		}
		l.setGlobal("util");
	}
}
