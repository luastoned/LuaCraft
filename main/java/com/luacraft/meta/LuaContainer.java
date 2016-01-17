package com.luacraft.meta;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import com.luacraft.LuaCraftState;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

public class LuaContainer {

	public static JavaFunction __tostring = new JavaFunction() {
		public int invoke(LuaState l) {
			IInventory self = (IInventory) l.checkUserdata(1, IInventory.class, "Container");
			l.pushString(String.format("Container: 0x%08x", l.toPointer(1)));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetMaxSlots
	 * @info Gets the maximum number of slots the container can hold
	 * @arguments nil
	 * @return [[Number]]:slots
	 */

	public static JavaFunction GetMaxSlots = new JavaFunction() {
		public int invoke(LuaState l) {
			IInventory self = (IInventory) l.checkUserdata(1, IInventory.class, "Container");
			l.pushInteger(self.getSizeInventory());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetStackLimit
	 * @info Gets the maximum stack size that the container slot can hold
	 * @arguments nil
	 * @return [[Number]]:slots
	 */

	public static JavaFunction GetStackLimit = new JavaFunction() {
		public int invoke(LuaState l) {
			IInventory self = (IInventory) l.checkUserdata(1, IInventory.class, "Container");
			l.pushInteger(self.getInventoryStackLimit());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetName
	 * @info Gets the customized name of the container
	 * @arguments nil
	 * @return [[String]]:name
	 */

	public static JavaFunction GetName = new JavaFunction() {
		public int invoke(LuaState l) {
			IInventory self = (IInventory) l.checkUserdata(1, IInventory.class, "Container");
			l.pushString(self.getName());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetSlot
	 * @info Gets the item stack within the slot
	 * @arguments nil
	 * @return [[ItemStack]]:item
	 */

	public static JavaFunction GetSlot = new JavaFunction() {
		public int invoke(LuaState l) {
			IInventory self = (IInventory) l.checkUserdata(1, IInventory.class, "Container");
			ItemStack item = self.getStackInSlot(l.checkInteger(2));
			l.pushUserdataWithMeta(item, "ItemStack");
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function SetSlot
	 * @info Sets the item stack within the slot
	 * @arguments [[Number]]:slot, [[ItemStack]]:item
	 * @return nil
	 */

	public static JavaFunction SetSlot = new JavaFunction() {
		public int invoke(LuaState l) {
			IInventory self = (IInventory) l.checkUserdata(1, IInventory.class, "Container");
			ItemStack item = (ItemStack) l.checkUserdata(3, ItemStack.class, "ItemStack");
			self.setInventorySlotContents(l.checkInteger(2), item);
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function DecreaseSlot
	 * @info Decrease the stack size of the item within the slot
	 * @arguments [[Number]]:slot, [ [[Number]]:amount ]
	 * @return nil
	 */

	public static JavaFunction DecreaseSlot = new JavaFunction() {
		public int invoke(LuaState l) {
			IInventory self = (IInventory) l.checkUserdata(1, IInventory.class, "Container");
			self.decrStackSize(l.checkInteger(2), l.checkInteger(3, 1));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function IsItemValidForSlot
	 * @info Returns whether or not this item is allowed to be placed in the
	 *       slot
	 * @arguments [[Number]]:slot, [[ItemStack]]:item
	 * @return [[Boolean]]:isvalid
	 */

	public static JavaFunction IsItemValidForSlot = new JavaFunction() {
		public int invoke(LuaState l) {
			IInventory self = (IInventory) l.checkUserdata(1, IInventory.class, "Container");
			ItemStack item = (ItemStack) l.checkUserdata(3, ItemStack.class, "ItemStack");
			l.pushBoolean(self.isItemValidForSlot(l.checkInteger(2), item));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetAll
	 * @info Returns a table of all the items within the container Format: [
	 *       SlotNumber ] = ItemStack
	 * @arguments nil
	 * @return [[Table]]:items
	 */

	public static JavaFunction GetAll = new JavaFunction() {
		public int invoke(LuaState l) {
			IInventory self = (IInventory) l.checkUserdata(1, IInventory.class, "Container");

			l.newTable();

			for (int i = 0; i < self.getSizeInventory(); i++) {
				ItemStack item = self.getStackInSlot(i);
				if (item != null) {
					l.pushInteger(i + 1);
					l.pushUserdataWithMeta(item, "ItemStack");
					l.setTable(-3);
				}
			}

			return 1;
		}
	};

	public static void Init(final LuaCraftState l) {
		l.newMetatable("Container");
		{
			l.pushValue(-1);
			l.setField(-2, "__index");

			l.pushJavaFunction(__tostring);
			l.setField(-2, "__tostring");

			l.pushJavaFunction(GetMaxSlots);
			l.setField(-2, "GetMaxSlots");
			l.pushJavaFunction(GetStackLimit);
			l.setField(-2, "GetStackLimit");
			l.pushJavaFunction(GetName);
			l.setField(-2, "GetName");
			l.pushJavaFunction(GetSlot);
			l.setField(-2, "GetSlot");
			l.pushJavaFunction(SetSlot);
			l.setField(-2, "SetSlot");
			l.pushJavaFunction(DecreaseSlot);
			l.setField(-2, "DecreaseSlot");
			l.pushJavaFunction(IsItemValidForSlot);
			l.setField(-2, "IsItemValidForSlot");
			l.pushJavaFunction(GetAll);
			l.setField(-2, "GetAll");
		}
		l.pop(1);
	}
}
