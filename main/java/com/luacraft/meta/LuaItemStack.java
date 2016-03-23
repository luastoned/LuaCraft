package com.luacraft.meta;

import com.luacraft.LuaCraftState;
import com.luacraft.LuaUserdata;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class LuaItemStack {
	public static JavaFunction __tostring = new JavaFunction() {
		public int invoke(LuaState l) {
			ItemStack self = (ItemStack) l.checkUserdata(1, ItemStack.class, "ItemStack");
			l.pushString(String.format("ItemStack [%s][%d]", self.getDisplayName(), self.stackSize));
			return 1;
		}
	};

	public static JavaFunction __eq = new JavaFunction() {
		public int invoke(LuaState l) {
			ItemStack self = (ItemStack) l.checkUserdata(1, ItemStack.class, "ItemStack");
			ItemStack other = (ItemStack) l.checkUserdata(2, ItemStack.class, "ItemStack");
			l.pushBoolean(self.isItemEqual(other));
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function Copy
	 * @info Gets a copy of the [[ItemStack]]
	 * @arguments nil
	 * @return [[ItemStack]]:stack
	 */

	public static JavaFunction Copy = new JavaFunction() {
		public int invoke(LuaState l) {
			ItemStack self = (ItemStack) l.checkUserdata(1, ItemStack.class, "ItemStack");
			l.pushUserdataWithMeta(self.copy(), "ItemStack");
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function GetID
	 * @info Gets the [[ItemStack]]s id
	 * @arguments nil
	 * @return [[Number]]:id
	 */

	public static JavaFunction GetID = new JavaFunction() {
		public int invoke(LuaState l) {
			ItemStack self = (ItemStack) l.checkUserdata(1, ItemStack.class, "ItemStack");
			l.pushInteger(Item.getIdFromItem(self.getItem()));
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function SetID
	 * @info Sets the [[ItemStack]]s id
	 * @arguments [[Number]]:id
	 * @return nil
	 */

	public static JavaFunction SetID = new JavaFunction() {
		public int invoke(LuaState l) {
			ItemStack self = (ItemStack) l.checkUserdata(1, ItemStack.class, "ItemStack");
			self.field_151002_e = Item.getItemById(l.checkInteger(2, 1));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function GetName
	 * @info Gets the [[ItemStack]]s human readable name
	 * @arguments nil
	 * @return [[String]]:name
	 */

	public static JavaFunction GetName = new JavaFunction() {
		public int invoke(LuaState l) {
			ItemStack self = (ItemStack) l.checkUserdata(1, ItemStack.class, "ItemStack");
			l.pushString(self.getDisplayName());
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function SetName
	 * @info Sets the [[ItemStack]]s human readable name
	 * @arguments [[String]]:name
	 * @return nil
	 */

	public static JavaFunction SetName = new JavaFunction() {
		public int invoke(LuaState l) {
			ItemStack self = (ItemStack) l.checkUserdata(1, ItemStack.class, "ItemStack");
			self.setStackDisplayName(l.checkString(2, "error"));
			return 0;
		}
	};

	/**
	 * @author Matt
	 * @function GetNBT
	 * @info Gets the [[ItemStack]]s [[NBTTag]]
	 * @arguments nil
	 * @return [[NBTTag]]:tag
	 */

	public static JavaFunction GetNBT = new JavaFunction() {
		public int invoke(LuaState l) {
			ItemStack self = (ItemStack) l.checkUserdata(1, ItemStack.class, "ItemStack");
			if (!self.hasTagCompound())
				self.setTagCompound(new NBTTagCompound());

			l.pushUserdataWithMeta(self.getTagCompound(), "NBTTag");
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function SetNBT
	 * @info Sets the [[ItemStack]]s [[NBTTag]]
	 * @arguments [[NBTTag]]:tag
	 * @return nil
	 */

	public static JavaFunction SetNBT = new JavaFunction() {
		public int invoke(LuaState l) {
			ItemStack self = (ItemStack) l.checkUserdata(1, ItemStack.class, "ItemStack");
			NBTTagCompound nbtTag = (NBTTagCompound) l.checkUserdata(2, NBTTagCompound.class, "NBTTag");
			self.setTagCompound(nbtTag);
			return 0;
		}
	};

	/**
	 * @author Gregor
	 * @function GetSize
	 * @info Gets the [[ItemStack]]s size
	 * @arguments nil
	 * @return [[Number]]:size
	 */

	public static JavaFunction GetSize = new JavaFunction() {
		public int invoke(LuaState l) {
			ItemStack self = (ItemStack) l.checkUserdata(1, ItemStack.class, "ItemStack");
			l.pushInteger(self.stackSize);
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function SetSize
	 * @info Sets the [[ItemStack]]s size
	 * @arguments [[Number]]:size
	 * @return nil
	 */

	public static JavaFunction SetSize = new JavaFunction() {
		public int invoke(LuaState l) {
			ItemStack self = (ItemStack) l.checkUserdata(1, ItemStack.class, "ItemStack");
			self.stackSize = l.checkInteger(2, 1);
			return 0;
		}
	};

	/**
	 * @author Gregor
	 * @function GetMaxSize
	 * @info Gets the [[ItemStack]]s maximal size
	 * @arguments nil
	 * @return [[Number]]:size
	 */

	public static JavaFunction GetMaxSize = new JavaFunction() {
		public int invoke(LuaState l) {
			ItemStack self = (ItemStack) l.checkUserdata(1, ItemStack.class, "ItemStack");
			l.pushInteger(self.getMaxStackSize());
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function SetMaxSize
	 * @info Sets the [[ItemStack]]s maximal size
	 * @arguments [[Number]]:size
	 * @return nil
	 */

	public static JavaFunction SetMaxSize = new JavaFunction() {
		public int invoke(LuaState l) {
			ItemStack self = (ItemStack) l.checkUserdata(1, ItemStack.class, "ItemStack");
			self.getItem().setMaxStackSize(l.checkInteger(2, 1));
			return 0;
		}
	};

	/**
	 * @author Gregor
	 * @function GetDamage
	 * @info Gets the [[ItemStack]]s damage value
	 * @arguments nil
	 * @return [[Number]]:damage
	 */

	public static JavaFunction GetDamage = new JavaFunction() {
		public int invoke(LuaState l) {
			ItemStack self = (ItemStack) l.checkUserdata(1, ItemStack.class, "ItemStack");
			l.pushInteger(self.getItemDamage());
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function SetDamage
	 * @info Sets the [[ItemStack]]s damage value
	 * @arguments [[Number]]:damage
	 * @return nil
	 */

	public static JavaFunction SetDamage = new JavaFunction() {
		public int invoke(LuaState l) {
			ItemStack self = (ItemStack) l.checkUserdata(1, ItemStack.class, "ItemStack");
			self.setItemDamage(l.checkInteger(2, 0));
			return 0;
		}
	};

	/**
	 * @author Gregor
	 * @function GetMaxDamage
	 * @info Gets the [[ItemStack]]s maximal damage
	 * @arguments nil
	 * @return [[Number]]:damage
	 */

	public static JavaFunction GetMaxDamage = new JavaFunction() {
		public int invoke(LuaState l) {
			ItemStack self = (ItemStack) l.checkUserdata(1, ItemStack.class, "ItemStack");
			l.pushInteger(self.getMaxDamage());
			return 1;
		}
	};

	/**
	 * @author Matt
	 * @function SetMaxDamage
	 * @info Sets the [[ItemStack]]s maximal damage
	 * @arguments [[Number]]:damage
	 * @return nil
	 */

	public static JavaFunction SetMaxDamage = new JavaFunction() {
		public int invoke(LuaState l) {
			ItemStack self = (ItemStack) l.checkUserdata(1, ItemStack.class, "ItemStack");
			self.getItem().setMaxDamage(l.checkInteger(2, 0));
			return 0;
		}
	};

	/**
	 * @author Matt
	 * @function IsEnchanted
	 * @info Gets the [[ItemStack]]s enchantment status
	 * @arguments nil
	 * @return [[Boolean]]:status
	 */

	public static JavaFunction IsEnchanted = new JavaFunction() {
		public int invoke(LuaState l) {
			ItemStack self = (ItemStack) l.checkUserdata(1, ItemStack.class, "ItemStack");
			l.pushBoolean(self.isItemEnchanted());
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function AddEnchantment
	 * @info Adds a specific entchantment
	 * @arguments [[Number]]:id, [[Number]]:level
	 * @return nil
	 */

	public static JavaFunction AddEnchantment = new JavaFunction() {
		public int invoke(LuaState l) {
			ItemStack self = (ItemStack) l.checkUserdata(1, ItemStack.class, "ItemStack");
			self.addEnchantment(Enchantment.enchantmentsList[l.checkInteger(2, 0)], l.checkInteger(3, 1));
			return 0;
		}
	};

	public static void Init(final LuaCraftState l) {
		l.newMetatable("ItemStack");
		{
			l.pushJavaFunction(__tostring);
			l.setField(-2, "__tostring");

			LuaUserdata.SetupBasicMeta(l);
			LuaUserdata.SetupMeta(l, false);

			l.newMetatable("Object");
			l.setField(-2, "__basemeta");

			l.pushJavaFunction(__eq);
			l.setField(-2, "__eq");

			l.pushJavaFunction(Copy);
			l.setField(-2, "Copy");
			l.pushJavaFunction(GetID);
			l.setField(-2, "GetID");
			l.pushJavaFunction(SetID);
			l.setField(-2, "SetID");
			l.pushJavaFunction(GetName);
			l.setField(-2, "GetName");
			l.pushJavaFunction(SetName);
			l.setField(-2, "SetName");
			l.pushJavaFunction(GetNBT);
			l.setField(-2, "GetNBT");
			l.pushJavaFunction(SetNBT);
			l.setField(-2, "SetNBT");
			l.pushJavaFunction(GetSize);
			l.setField(-2, "GetSize");
			l.pushJavaFunction(SetSize);
			l.setField(-2, "SetSize");
			l.pushJavaFunction(GetMaxSize);
			l.setField(-2, "GetMaxSize");
			l.pushJavaFunction(SetMaxSize);
			l.setField(-2, "SetMaxSize");
			l.pushJavaFunction(GetDamage);
			l.setField(-2, "GetDamage");
			l.pushJavaFunction(SetDamage);
			l.setField(-2, "SetDamage");
			l.pushJavaFunction(GetMaxDamage);
			l.setField(-2, "GetMaxDamage");
			l.pushJavaFunction(SetMaxDamage);
			l.setField(-2, "SetMaxDamage");
			l.pushJavaFunction(IsEnchanted);
			l.setField(-2, "IsEnchanted");
			l.pushJavaFunction(AddEnchantment);
			l.setField(-2, "AddEnchantment");
		}
		l.pop(1);
	}
}
