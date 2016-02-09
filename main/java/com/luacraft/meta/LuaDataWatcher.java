package com.luacraft.meta;

import com.luacraft.LuaCraftState;
import com.luacraft.LuaUserdata;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

import net.minecraft.entity.DataWatcher;
import net.minecraft.item.ItemStack;

public class LuaDataWatcher {

	public static JavaFunction __tostring = new JavaFunction() {
		public int invoke(LuaState l) {
			DataWatcher self = (DataWatcher) l.checkUserdata(1, DataWatcher.class, "DataWatcher");
			l.pushString(String.format("DataWatcher: 0x%08x", l.toPointer(1)));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function AddByte
	 * @info Adds a byte to the datawatcher that will be networked to all players
	 * @arguments [[Number]]:index, [ [[Number]]:default ]
	 * @return nil
	 */

	public static JavaFunction AddByte = new JavaFunction() {
		public int invoke(LuaState l) {
			DataWatcher self = (DataWatcher) l.checkUserdata(1, DataWatcher.class, "DataWatcher");
			self.addObject(l.checkInteger(2), (byte) l.checkInteger(3, 0));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function SetByte
	 * @info Updates a byte that will be networked to all players
	 * @arguments [[Number]]:index, [[Number]]:value
	 * @return nil
	 */

	public static JavaFunction SetByte = new JavaFunction() {
		public int invoke(LuaState l) {
			DataWatcher self = (DataWatcher) l.checkUserdata(1, DataWatcher.class, "DataWatcher");
			self.updateObject(l.checkInteger(2), (byte) l.checkInteger(3));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function GetByte
	 * @info Returns a byte that is networked to all players
	 * @arguments nil
	 * @return [[Number]]
	 */

	public static JavaFunction GetByte = new JavaFunction() {
		public int invoke(LuaState l) {
			DataWatcher self = (DataWatcher) l.checkUserdata(1, DataWatcher.class, "DataWatcher");
			l.pushNumber(self.getWatchableObjectByte(l.checkInteger(2)));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function AddShort
	 * @info Adds a short to the datawatcher that will be networked to all players
	 * @arguments [[Number]]:index, [ [[Number]]:default ]
	 * @return nil
	 */

	public static JavaFunction AddShort = new JavaFunction() {
		public int invoke(LuaState l) {
			DataWatcher self = (DataWatcher) l.checkUserdata(1, DataWatcher.class, "DataWatcher");
			self.addObject(l.checkInteger(2), (short) l.checkInteger(3, 0));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function SetShort
	 * @info Updates a short that will be networked to all players
	 * @arguments [[Number]]:index, [[Number]]:value
	 * @return nil
	 */

	public static JavaFunction SetShort = new JavaFunction() {
		public int invoke(LuaState l) {
			DataWatcher self = (DataWatcher) l.checkUserdata(1, DataWatcher.class, "DataWatcher");
			self.updateObject(l.checkInteger(2), (short) l.checkInteger(3));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function GetShort
	 * @info Returns a short that is networked to all players
	 * @arguments nil
	 * @return [[Number]]
	 */

	public static JavaFunction GetShort = new JavaFunction() {
		public int invoke(LuaState l) {
			DataWatcher self = (DataWatcher) l.checkUserdata(1, DataWatcher.class, "DataWatcher");
			l.pushNumber(self.getWatchableObjectShort(l.checkInteger(2)));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function AddInt
	 * @info Adds an integer to the datawatcher that will be networked to all players
	 * @arguments [[Number]]:index, [ [[Number]]:default ]
	 * @return nil
	 */

	public static JavaFunction AddInt = new JavaFunction() {
		public int invoke(LuaState l) {
			DataWatcher self = (DataWatcher) l.checkUserdata(1, DataWatcher.class, "DataWatcher");
			self.addObject(l.checkInteger(2), l.checkInteger(3, 0));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function SetInt
	 * @info Updates an integer that will be networked to all players
	 * @arguments [[Number]]:index, [[Number]]:value
	 * @return nil
	 */

	public static JavaFunction SetInt = new JavaFunction() {
		public int invoke(LuaState l) {
			DataWatcher self = (DataWatcher) l.checkUserdata(1, DataWatcher.class, "DataWatcher");
			self.updateObject(l.checkInteger(2), l.checkInteger(3));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function GetInt
	 * @info Returns an integer that is networked to all players
	 * @arguments nil
	 * @return [[Number]]
	 */

	public static JavaFunction GetInt = new JavaFunction() {
		public int invoke(LuaState l) {
			DataWatcher self = (DataWatcher) l.checkUserdata(1, DataWatcher.class, "DataWatcher");
			l.pushInteger(self.getWatchableObjectInt(l.checkInteger(2)));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function AddFloat
	 * @info Adds a float to the datawatcher that will be networked to all players
	 * @arguments [[Number]]:index, [ [[Number]]:default ]
	 * @return nil
	 */

	public static JavaFunction AddFloat = new JavaFunction() {
		public int invoke(LuaState l) {
			DataWatcher self = (DataWatcher) l.checkUserdata(1, DataWatcher.class, "DataWatcher");
			self.addObject(l.checkInteger(2), (float) l.checkNumber(3, 0));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function SetFloat
	 * @info Updates an float that will be networked to all players
	 * @arguments [[Number]]:index, [[Number]]:value
	 * @return nil
	 */

	public static JavaFunction SetFloat = new JavaFunction() {
		public int invoke(LuaState l) {
			DataWatcher self = (DataWatcher) l.checkUserdata(1, DataWatcher.class, "DataWatcher");
			self.updateObject(l.checkInteger(2), (float) l.checkNumber(3));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function GetFloat
	 * @info Returns a float that is networked to all players
	 * @arguments nil
	 * @return [[Number]]
	 */

	public static JavaFunction GetFloat = new JavaFunction() {
		public int invoke(LuaState l) {
			DataWatcher self = (DataWatcher) l.checkUserdata(1, DataWatcher.class, "DataWatcher");
			l.pushNumber(self.getWatchableObjectFloat(l.checkInteger(2)));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function AddString
	 * @info Adds a string to the datawatcher that will be networked to all players
	 * @arguments [[Number]]:index, [ [[String]]:default ]
	 * @return nil
	 */

	public static JavaFunction AddString = new JavaFunction() {
		public int invoke(LuaState l) {
			DataWatcher self = (DataWatcher) l.checkUserdata(1, DataWatcher.class, "DataWatcher");
			self.addObject(l.checkInteger(2), l.checkString(3, ""));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function SetString
	 * @info Updates a string that will be networked to all players
	 * @arguments [[Number]]:index, [[String]]:value
	 * @return nil
	 */

	public static JavaFunction SetString = new JavaFunction() {
		public int invoke(LuaState l) {
			DataWatcher self = (DataWatcher) l.checkUserdata(1, DataWatcher.class, "DataWatcher");
			self.updateObject(l.checkInteger(2), l.checkString(3));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function GetString
	 * @info Returns a string that is networked to all players
	 * @arguments nil
	 * @return [[String]]
	 */

	public static JavaFunction GetString = new JavaFunction() {
		public int invoke(LuaState l) {
			DataWatcher self = (DataWatcher) l.checkUserdata(1, DataWatcher.class, "DataWatcher");
			l.pushString(self.getWatchableObjectString(l.checkInteger(2)));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function AddItemStack
	 * @info Adds an ItemStack to the datawatcher that will be networked to all players
	 * @arguments [[Number]]:index, [[ItemStack]]:default
	 * @return nil
	 */

	public static JavaFunction AddItemStack = new JavaFunction() {
		public int invoke(LuaState l) {
			DataWatcher self = (DataWatcher) l.checkUserdata(1, DataWatcher.class, "DataWatcher");
			self.addObject(l.checkInteger(2), l.checkUserdata(3, ItemStack.class, "ItemStack"));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function SetItemStack
	 * @info Updates an ItemStack that will be networked to all players
	 * @arguments [[Number]]:index, [[ItemStack]]:value
	 * @return nil
	 */

	public static JavaFunction SetItemStack = new JavaFunction() {
		public int invoke(LuaState l) {
			DataWatcher self = (DataWatcher) l.checkUserdata(1, DataWatcher.class, "DataWatcher");
			self.updateObject(l.checkInteger(2), l.checkUserdata(3, ItemStack.class, "ItemStack"));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function GetItemStack
	 * @info Returns an ItemStack that is networked to all players
	 * @arguments nil
	 * @return [[ItemStack]]
	 */

	public static JavaFunction GetItemStack = new JavaFunction() {
		public int invoke(LuaState l) {
			DataWatcher self = (DataWatcher) l.checkUserdata(1, DataWatcher.class, "DataWatcher");
			ItemStack item = self.getWatchableObjectItemStack(l.checkInteger(2));
			l.pushUserdataWithMeta(item, "ItemStack");
			return 1;
		}
	};

	public static void Init(final LuaCraftState l) {
		l.newMetatable("DataWatcher");
		{
			l.pushJavaFunction(__tostring);
			l.setField(-2, "__tostring");

			LuaUserdata.SetupBasicMeta(l);
			LuaUserdata.SetupMeta(l, false);

			l.newMetatable("Object");
			l.setField(-2, "__basemeta");

			l.pushJavaFunction(AddByte);
			l.setField(-2, "AddByte");
			l.pushJavaFunction(SetByte);
			l.setField(-2, "SetByte");
			l.pushJavaFunction(GetByte);
			l.setField(-2, "GetByte");

			l.pushJavaFunction(AddShort);
			l.setField(-2, "AddShort");
			l.pushJavaFunction(SetShort);
			l.setField(-2, "SetShort");
			l.pushJavaFunction(GetShort);
			l.setField(-2, "GetShort");

			l.pushJavaFunction(AddInt);
			l.setField(-2, "AddInt");
			l.pushJavaFunction(SetInt);
			l.setField(-2, "SetInt");
			l.pushJavaFunction(GetInt);
			l.setField(-2, "GetInt");

			l.pushJavaFunction(AddFloat);
			l.setField(-2, "AddFloat");
			l.pushJavaFunction(SetFloat);
			l.setField(-2, "SetFloat");
			l.pushJavaFunction(GetFloat);
			l.setField(-2, "GetFloat");

			l.pushJavaFunction(AddString);
			l.setField(-2, "AddString");
			l.pushJavaFunction(SetString);
			l.setField(-2, "SetString");
			l.pushJavaFunction(GetString);
			l.setField(-2, "GetString");

			l.pushJavaFunction(AddItemStack);
			l.setField(-2, "AddItemStack");
			l.pushJavaFunction(SetItemStack);
			l.setField(-2, "SetItemStack");
			l.pushJavaFunction(GetItemStack);
			l.setField(-2, "GetItemStack");
		}
		l.pop(1);

	}
}
