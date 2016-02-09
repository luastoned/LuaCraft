package com.luacraft.meta;

import com.luacraft.LuaUserdata;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

import net.minecraft.nbt.NBTTagCompound;

public class LuaNBTTag {
	public static JavaFunction __tostring = new JavaFunction() {
		public int invoke(LuaState l) {
			NBTTagCompound self = (NBTTagCompound) l.checkUserdata(1, NBTTagCompound.class, "NBTTag");
			l.pushString(String.format("NBTTag: 0x%08x", l.toPointer(1)));
			return 1;
		}
	};

	/**
	 * @author Matt
	 * @function HasKey
	 * @info Returns if the NBTTag has the key.
	 * @arguments [[String]]:key
	 * @return [[Boolean]]:bool
	 */

	public static JavaFunction HasKey = new JavaFunction() {
		public int invoke(LuaState l) {
			NBTTagCompound self = (NBTTagCompound) l.checkUserdata(1, NBTTagCompound.class, "NBTTag");
			l.pushBoolean(self.hasKey(l.checkString(2)));
			return 1;
		}
	};

	/**
	 * @author Matt
	 * @function SetBool
	 * @info Sets the keys bool.
	 * @arguments [[String]]:key, [[Boolean]]:value
	 * @return nil
	 */

	public static JavaFunction SetBool = new JavaFunction() {
		public int invoke(LuaState l) {
			NBTTagCompound self = (NBTTagCompound) l.checkUserdata(1, NBTTagCompound.class, "NBTTag");
			self.setBoolean(l.checkString(2), l.checkBoolean(3));
			return 0;
		}
	};

	/**
	 * @author Matt
	 * @function GetBool
	 * @info Return the property bool
	 * @arguments [[String]]:key, [[Number]]:default
	 * @return [[Number]]:value
	 */

	public static JavaFunction GetBool = new JavaFunction() {
		public int invoke(LuaState l) {
			NBTTagCompound self = (NBTTagCompound) l.checkUserdata(1, NBTTagCompound.class, "NBTTag");
			l.pushBoolean(self.getBoolean(l.checkString(2)));
			return 1;
		}
	};

	/**
	 * @author Matt
	 * @function SetByte
	 * @info Sets the keys byte.
	 * @arguments [[String]]:key, [[Number]]:value
	 * @return nil
	 */

	public static JavaFunction SetByte = new JavaFunction() {
		public int invoke(LuaState l) {
			NBTTagCompound self = (NBTTagCompound) l.checkUserdata(1, NBTTagCompound.class, "NBTTag");
			self.setByte(l.checkString(2), (byte) l.checkInteger(3));
			return 0;
		}
	};

	/**
	 * @author Matt
	 * @function GetByte
	 * @info Return the property byte
	 * @arguments [[String]]:key, [[Number]]:default
	 * @return [[Number]]:value
	 */

	public static JavaFunction GetByte = new JavaFunction() {
		public int invoke(LuaState l) {
			NBTTagCompound self = (NBTTagCompound) l.checkUserdata(1, NBTTagCompound.class, "NBTTag");
			l.pushInteger(self.getByte(l.checkString(2)));
			return 1;
		}
	};

	/**
	 * @author Matt
	 * @function SetShort
	 * @info Sets the keys short.
	 * @arguments [[String]]:key, [[Number]]:value
	 * @return nil
	 */

	public static JavaFunction SetShort = new JavaFunction() {
		public int invoke(LuaState l) {
			NBTTagCompound self = (NBTTagCompound) l.checkUserdata(1, NBTTagCompound.class, "NBTTag");
			self.setShort(l.checkString(2), (short) l.checkInteger(3));
			return 0;
		}
	};

	/**
	 * @author Matt
	 * @function GetShort
	 * @info Return the property short
	 * @arguments [[String]]:key, [[Number]]:default
	 * @return [[Number]]:value
	 */

	public static JavaFunction GetShort = new JavaFunction() {
		public int invoke(LuaState l) {
			NBTTagCompound self = (NBTTagCompound) l.checkUserdata(1, NBTTagCompound.class, "NBTTag");
			l.pushInteger(self.getShort(l.checkString(2)));
			return 1;
		}
	};

	/**
	 * @author Matt
	 * @function SetInteger
	 * @info Sets the keys integer.
	 * @arguments [[String]]:key, [[Number]]:value
	 * @return nil
	 */

	public static JavaFunction SetInteger = new JavaFunction() {
		public int invoke(LuaState l) {
			NBTTagCompound self = (NBTTagCompound) l.checkUserdata(1, NBTTagCompound.class, "NBTTag");
			self.setInteger(l.checkString(2), l.checkInteger(3));
			return 0;
		}
	};

	/**
	 * @author Matt
	 * @function GetInteger
	 * @info Return the property integer
	 * @arguments [[String]]:key, [[Number]]:default
	 * @return [[Number]]:value
	 */

	public static JavaFunction GetInteger = new JavaFunction() {
		public int invoke(LuaState l) {
			NBTTagCompound self = (NBTTagCompound) l.checkUserdata(1, NBTTagCompound.class, "NBTTag");
			l.pushInteger(self.getInteger(l.checkString(2)));
			return 1;
		}
	};

	/**
	 * @author Matt
	 * @function SetLong
	 * @info Sets the keys long.
	 * @arguments [[String]]:key, [[Number]]:value
	 * @return nil
	 */

	public static JavaFunction SetLong = new JavaFunction() {
		public int invoke(LuaState l) {
			NBTTagCompound self = (NBTTagCompound) l.checkUserdata(1, NBTTagCompound.class, "NBTTag");
			self.setLong(l.checkString(2), (long) l.checkNumber(3));
			return 0;
		}
	};

	/**
	 * @author Matt
	 * @function GetLong
	 * @info Return the property long
	 * @arguments [[String]]:key, [[Number]]:default
	 * @return [[Number]]:value
	 */

	public static JavaFunction GetLong = new JavaFunction() {
		public int invoke(LuaState l) {
			NBTTagCompound self = (NBTTagCompound) l.checkUserdata(1, NBTTagCompound.class, "NBTTag");
			l.pushNumber(self.getLong(l.checkString(2)));
			return 1;
		}
	};

	/**
	 * @author Matt
	 * @function SetFloat
	 * @info Sets the keys float.
	 * @arguments [[String]]:key, [[Number]]:value
	 * @return nil
	 */

	public static JavaFunction SetFloat = new JavaFunction() {
		public int invoke(LuaState l) {
			NBTTagCompound self = (NBTTagCompound) l.checkUserdata(1, NBTTagCompound.class, "NBTTag");
			self.setFloat(l.checkString(2), (float) l.checkNumber(3));
			return 0;
		}
	};

	/**
	 * @author Matt
	 * @function GetFloat
	 * @info Return the property float
	 * @arguments [[String]]:key, [[Number]]:default
	 * @return [[Number]]:value
	 */

	public static JavaFunction GetFloat = new JavaFunction() {
		public int invoke(LuaState l) {
			NBTTagCompound self = (NBTTagCompound) l.checkUserdata(1, NBTTagCompound.class, "NBTTag");
			l.pushNumber(self.getFloat(l.checkString(2)));
			return 1;
		}
	};

	/**
	 * @author Matt
	 * @function SetDouble
	 * @info Sets the keys double.
	 * @arguments [[String]]:key, [[Number]]:value
	 * @return nil
	 */

	public static JavaFunction SetDouble = new JavaFunction() {
		public int invoke(LuaState l) {
			NBTTagCompound self = (NBTTagCompound) l.checkUserdata(1, NBTTagCompound.class, "NBTTag");
			self.setDouble(l.checkString(2), l.checkNumber(3));
			return 0;
		}
	};

	/**
	 * @author Matt
	 * @function GetDouble
	 * @info Return the property double
	 * @arguments [[String]]:key, [[Number]]:default
	 * @return [[Number]]:value
	 */

	public static JavaFunction GetDouble = new JavaFunction() {
		public int invoke(LuaState l) {
			NBTTagCompound self = (NBTTagCompound) l.checkUserdata(1, NBTTagCompound.class, "NBTTag");
			l.pushNumber(self.getDouble(l.checkString(2)));
			return 1;
		}
	};

	/**
	 * @author Matt
	 * @function SetString
	 * @info Sets the keys string.
	 * @arguments [[String]]:key, [[String]]:value
	 * @return nil
	 */

	public static JavaFunction SetString = new JavaFunction() {
		public int invoke(LuaState l) {
			NBTTagCompound self = (NBTTagCompound) l.checkUserdata(1, NBTTagCompound.class, "NBTTag");
			self.setString(l.checkString(2), l.checkString(3));
			return 0;
		}
	};

	/**
	 * @author Matt
	 * @function GetString
	 * @info Return the property string
	 * @arguments [[String]]:key, [[String]]:default
	 * @return [[String]]:value
	 */

	public static JavaFunction GetString = new JavaFunction() {
		public int invoke(LuaState l) {
			NBTTagCompound self = (NBTTagCompound) l.checkUserdata(1, NBTTagCompound.class, "NBTTag");
			l.pushString(self.getString(l.checkString(2)));
			return 1;
		}
	};

	/**
	 * @author Matt
	 * @function SetTag
	 * @info Sets the keys NBTTag
	 * @arguments [[String]]:key, [[NBTTag]]:value
	 * @return nil
	 */

	public static JavaFunction SetTag = new JavaFunction() {
		public int invoke(LuaState l) {
			NBTTagCompound self = (NBTTagCompound) l.checkUserdata(1, NBTTagCompound.class, "NBTTag");
			NBTTagCompound other = (NBTTagCompound) l.checkUserdata(3, NBTTagCompound.class, "NBTTag");
			self.setTag(l.checkString(2), other);
			return 0;
		}
	};

	/**
	 * @author Matt
	 * @function GetTag
	 * @info Return the property CompoundTag
	 * @arguments [[String]]:key, [[NBTTag]]:default
	 * @return [[NBTTag]]:value
	 */

	public static JavaFunction GetTag = new JavaFunction() {
		public int invoke(LuaState l) {
			NBTTagCompound self = (NBTTagCompound) l.checkUserdata(1, NBTTagCompound.class, "NBTTag");
			l.pushUserdataWithMeta(self.getTag(l.checkString(2)), "NBTTag");
			return 1;
		}
	};

	public static void Init(LuaState l) {
		l.newMetatable("NBTTag");
		{
			l.pushJavaFunction(__tostring);
			l.setField(-2, "__tostring");

			LuaUserdata.SetupBasicMeta(l);
			LuaUserdata.SetupMeta(l, false);

			l.newMetatable("Object");
			l.setField(-2, "__basemeta");

			l.pushJavaFunction(HasKey);
			l.setField(-2, "HasKey");
			l.pushJavaFunction(SetBool);
			l.setField(-2, "SetBool");
			l.pushJavaFunction(GetBool);
			l.setField(-2, "GetBool");
			l.pushJavaFunction(SetByte);
			l.setField(-2, "SetByte");
			l.pushJavaFunction(GetByte);
			l.setField(-2, "GetByte");
			l.pushJavaFunction(SetShort);
			l.setField(-2, "SetShort");
			l.pushJavaFunction(GetShort);
			l.setField(-2, "GetShort");
			l.pushJavaFunction(SetInteger);
			l.setField(-2, "SetInteger");
			l.pushJavaFunction(GetInteger);
			l.setField(-2, "GetInteger");
			l.pushJavaFunction(SetLong);
			l.setField(-2, "SetLong");
			l.pushJavaFunction(GetLong);
			l.setField(-2, "GetLong");
			l.pushJavaFunction(SetFloat);
			l.setField(-2, "SetFloat");
			l.pushJavaFunction(GetFloat);
			l.setField(-2, "GetFloat");
			l.pushJavaFunction(SetDouble);
			l.setField(-2, "SetDouble");
			l.pushJavaFunction(GetDouble);
			l.setField(-2, "GetDouble");
			l.pushJavaFunction(SetString);
			l.setField(-2, "SetString");
			l.pushJavaFunction(GetString);
			l.setField(-2, "GetString");
			l.pushJavaFunction(SetTag);
			l.setField(-2, "SetTag");
			l.pushJavaFunction(GetTag);
			l.setField(-2, "GetTag");
		}
		l.pop(1);
	}
}
