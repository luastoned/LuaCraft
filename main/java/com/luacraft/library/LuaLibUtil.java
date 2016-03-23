package com.luacraft.library;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Hex;

import com.luacraft.LuaCraftState;
import com.luacraft.LuaUserdata;
import com.luacraft.classes.LuaJavaBlock;
import com.luacraft.classes.Vector;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaRuntimeException;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.LuaType;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class LuaLibUtil {

	public static String base64encode(String str) {
		return DatatypeConverter.printBase64Binary(str.getBytes());
	}

	public static byte[] base64decode(String str) {
		return DatatypeConverter.parseBase64Binary(str);
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

	public static String toChat(LuaState l, int stackPos) {
		StringBuilder message = new StringBuilder();

		for (int i = stackPos; i <= l.getTop(); i++) {
			if (l.isNoneOrNil(i))
				continue;

			if (l.type(i) == LuaType.NUMBER) {
				EnumChatFormatting format = EnumChatFormatting.values()[l.toInteger(i)];
				message.append(format);
			} else {
				l.getGlobal("tostring");
				l.pushValue(i);
				l.call(1, 1);
				message.append(l.toString(-1));
				l.pop(1);
			}
		}

		return message.toString();
	}

	private static MovingObjectPosition traceEntity(World world, Vec3 start, Vec3 end, List<Entity> filter) {
		MovingObjectPosition result = null;
		double hitDistance = -1;

		for (Object obj : world.loadedEntityList) {
			Entity entity = (Entity) obj;

			if (filter.contains(entity))
				continue;
			
			Vec3 entityPos = Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ);

			double distance = start.distanceTo(entityPos);
			
			MovingObjectPosition trace = entity.boundingBox.calculateIntercept(start, end);

			if (trace != null && (hitDistance == -1 || distance < hitDistance)) {
				hitDistance = distance;
				result = trace;
				result.entityHit = entity;
			}
		}

		return result;
	}

	public static void pushTrace(LuaCraftState l, World worldObj, Vector start, Vector endpos, boolean b) {
		pushTrace(l, worldObj, start, endpos, b, new ArrayList<Entity>());
	}

	public static void pushTrace(LuaCraftState l, World world, Vector start, Vector endpos, boolean hitWater,
			List<Entity> filter) {
		MovingObjectPosition trace = world.rayTraceBlocks(start.toVec3(), endpos.toVec3(), hitWater);

		// Use the block trace end position so we can't find entities through blocks
		MovingObjectPosition entTrace = traceEntity(world, start.toVec3(),
				trace != null ? trace.hitVec : endpos.toVec3(), filter);

		l.newTable();

		start.push(l);
		l.setField(-2, "StartPos");

		if (entTrace != null) {
			l.pushBoolean(true);
			l.setField(-2, "Hit");

			LuaUserdata.PushUserdata(l, entTrace.entityHit);
			l.setField(-2, "HitEntity");

			Vector hitpos = new Vector(entTrace.hitVec);
			hitpos.push(l);
			l.setField(-2, "HitPos");

			l.pushFace(entTrace.sideHit);
			l.setField(-2, "HitNormal");
		} else if (trace != null) {
			l.pushBoolean(true);
			l.setField(-2, "Hit");

			LuaJavaBlock thisBlock = new LuaJavaBlock(world, trace.blockX, trace.blockY, trace.blockZ);
			LuaUserdata.PushUserdata(l, thisBlock);
			l.setField(-2, "HitBlock");

			Vector hitpos = new Vector(trace.hitVec);
			hitpos.push(l);
			l.setField(-2, "HitPos");

			l.pushFace(trace.sideHit);
			l.setField(-2, "HitNormal");
		} else {
			l.pushBoolean(false);
			l.setField(-2, "Hit");

			Vector hitpos = new Vector(endpos.toVec3());
			hitpos.push(l);
			l.setField(-2, "HitPos");
		}
	}

	/**
	 * @author Jake
	 * @library util
	 * @function CRC32
	 * @info Get a CRC32-bit number of a string
	 * @arguments [[String]]:string
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
			l.pushString(Hex.encodeHexString(ecrypt.digest()));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @library util
	 * @function Base64Encode
	 * @info Encode a string to Base64
	 * @arguments [[String]]:string
	 * @return [[String]]:encoded
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
	 * @arguments [[String]]:base64
	 * @return [[String]]:decoded
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
	 * @arguments [[String]]:string
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
	 * @arguments [[String]]:data
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
