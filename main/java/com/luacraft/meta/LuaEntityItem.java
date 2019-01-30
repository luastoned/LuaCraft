package com.luacraft.meta;

import com.luacraft.LuaCraftState;
import com.luacraft.LuaUserdata;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

public class LuaEntityItem {
	/**
	 * @author Jake
	 * @function SetItemStack
	 * @info Set what item this entity will represent
	 * @arguments [[ItemStack]]:item
	 * @return nil
	 */

	public static JavaFunction SetItemStack = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityItem self = (EntityItem) l.checkUserdata(1, EntityItem.class, "EntityItem");
			ItemStack item = (ItemStack) l.checkUserdata(2, ItemStack.class, "ItemStack");
			self.setItem(item);
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function GetItemStack
	 * @info Get what item this entity is representing
	 * @arguments nil
	 * @return [[ItemStack]]:item
	 */

	public static JavaFunction GetItemStack = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityItem self = (EntityItem) l.checkUserdata(1, EntityItem.class, "EntityItem");
			l.pushUserdataWithMeta(self.getItem(), "ItemStack");
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function SetAge
	 * @info Set how old this item is
	 * @arguments [[Number]]:age
	 * @return nil
	 */

	public static JavaFunction SetAge = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityItem self = (EntityItem) l.checkUserdata(1, EntityItem.class, "EntityItem");
			self.age = l.checkInteger(2);
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function GetAge
	 * @info Get how old this item is
	 * @arguments nil
	 * @return [[Number]]:age
	 */

	public static JavaFunction GetAge = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityItem self = (EntityItem) l.checkUserdata(1, EntityItem.class, "EntityItem");
			l.pushInteger(self.getAge());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function SetPickupDelay
	 * @info Set the delay before this item can be picked up
	 * @arguments [[Number]]:delay
	 * @return nil
	 */

	public static JavaFunction SetPickupDelay = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityItem self = (EntityItem) l.checkUserdata(1, EntityItem.class, "EntityItem");
			self.pickupDelay = l.checkInteger(2);
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function GetPickupDelay
	 * @info Get the delay before this item can be picked up
	 * @arguments nil
	 * @return [[Number]]:age
	 */

	public static JavaFunction GetPickupDelay = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityItem self = (EntityItem) l.checkUserdata(1, EntityItem.class, "EntityItem");
			l.pushInteger(self.pickupDelay);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function SetLifespan
	 * @info Set the lifespan of this item
	 * @arguments [[Number]]:lifespan
	 * @return nil
	 */

	public static JavaFunction SetLifespan = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityItem self = (EntityItem) l.checkUserdata(1, EntityItem.class, "EntityItem");
			self.lifespan = l.checkInteger(2);
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function GetLifespan
	 * @info Get the lifespan of this item
	 * @arguments nil
	 * @return [[Number]]:lifespan
	 */

	public static JavaFunction GetLifespan = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityItem self = (EntityItem) l.checkUserdata(1, EntityItem.class, "EntityItem");
			l.pushInteger(self.lifespan);
			return 1;
		}
	};

	public static void Init(final LuaCraftState l) {
		l.newMetatable("EntityItem");
		{
			l.pushJavaFunction(LuaEntity.__tostring);
			l.setField(-2, "__tostring");

			LuaUserdata.SetupBasicMeta(l);
			LuaUserdata.SetupMeta(l, true);

			l.newMetatable("Entity");
			l.setField(-2, "__basemeta");

			l.pushJavaFunction(SetItemStack);
			l.setField(-2, "SetItemStack");
			l.pushJavaFunction(GetItemStack);
			l.setField(-2, "GetItemStack");
			l.pushJavaFunction(SetAge);
			l.setField(-2, "SetAge");
			l.pushJavaFunction(GetAge);
			l.setField(-2, "GetAge");
			l.pushJavaFunction(SetPickupDelay);
			l.setField(-2, "SetPickupDelay");
			l.pushJavaFunction(GetPickupDelay);
			l.setField(-2, "GetPickupDelay");
			l.pushJavaFunction(SetLifespan);
			l.setField(-2, "SetLifespan");
			l.pushJavaFunction(GetLifespan);
			l.setField(-2, "GetLifespan");
		}
		l.pop(1);
	}
}
