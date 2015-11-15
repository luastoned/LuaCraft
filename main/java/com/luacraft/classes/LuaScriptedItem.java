package com.luacraft.classes;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.luacraft.LuaUserdataManager;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.LuaType;
import com.naef.jnlua.LuaUserdata;

public class LuaScriptedItem extends Item implements LuaUserdata {
	public LuaState l;
	int tableRef;
	
	public LuaScriptedItem(LuaState l, int tableRef){
		this.l = l;
		this.tableRef = tableRef;
	}
	
	public static JavaFunction __tostring = new JavaFunction()
	{
		public int invoke(LuaState l)
		{
			LuaScriptedItem self = (LuaScriptedItem) l.checkUserdata(1, LuaScriptedItem.class, "ScriptedItem");
			l.pushString(String.format("ScriptedItem: 0x%08x", l.toPointer(1)));
			return 1;
		}
	};
	
	public static JavaFunction Register = new JavaFunction()
	{
		public int invoke(LuaState l)
		{
			l.checkType(1, LuaType.TABLE);
			String classname = l.checkString(2);
			
			l.newMetatable("ScriptedItemReferences");
			l.pushValue(1);
			int tableRef = l.ref(-2);
			
			LuaScriptedItem item = new LuaScriptedItem(l, tableRef);
			GameRegistry.registerItem(item.setUnlocalizedName(classname), classname);
			return 0;
		}
	};
	
	public static JavaFunction dummyFunc = new JavaFunction()
	{
		public int invoke(LuaState l)
		{
			return 0;
		}
	};
	
	public static void Init(LuaState l ) {
		l.newMetatable("ScriptedItem");
		{
			l.pushJavaFunction(__tostring);
			l.setField(-2, "__tostring");
			
			l.pushInteger(64);
			l.setField(-2, "MaxStackSize");

			l.pushJavaFunction(dummyFunc);
			l.setField(-2, "OnItemUse");
			l.pushJavaFunction(dummyFunc);
			l.setField(-2, "OnItemUseFinish");
			l.pushJavaFunction(dummyFunc);
			l.setField(-2, "OnItemRightClick");
			l.pushJavaFunction(dummyFunc);
			l.setField(-2, "OnHitEntity");
		}
		l.pop(1);
		
		l.newTable();
		{
			l.pushJavaFunction(Register);
			l.setField(-2, "Register");
		}
		l.setGlobal("item");
		
		l.newMetatable("ScriptedItemReferences");
	}
	
	@Override
	public String getTypeName() {
		return "ScriptedItem";
	}
	
	private void pushSelf()
	{
		l.pushUserdataWithMeta(this, "ScriptedItem");
	}
	
	private void pushItemTable()
	{
		l.newMetatable("ScriptedItemReferences");
		l.rawGet(-1, tableRef);
		l.remove(-2);
	}
	
	private void pushValue(String name) {
		pushItemTable();
		l.getField(-1, name);
		l.remove(-2);
		
		if (l.isNil(-1)) // Fallback to metatable defaults
		{
			l.newMetatable("ScriptedItem");
			l.getField(-1, name);
			l.remove(-2);
		}
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack) {
		pushValue("MaxStackSize");
		int ret = l.toInteger(-1);
		l.setTop(0);
		return ret;
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		pushValue("OnItemUse");
		{
			pushSelf();
			l.pushUserdataWithMeta(stack, "ItemStack");
			LuaUserdataManager.PushUserdata(l, playerIn);
			LuaUserdataManager.PushUserdata(l, worldIn);
			LuaUserdataManager.PushUserdata(l, new LuaJavaBlock(worldIn, pos));
			l.pushInteger(side.ordinal());
			l.pushUserdataWithMeta(new Vector(hitX, hitZ, hitY), "Vector");
		}
		l.call(7, 1);
			boolean ret = l.toBoolean(1);
		l.pop(1);
		return ret;
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		pushValue("OnItemUseFinish");
		{
			pushSelf();
			l.pushUserdataWithMeta(stack, "ItemStack");
			LuaUserdataManager.PushUserdata(l, worldIn);
			LuaUserdataManager.PushUserdata(l, playerIn);
		}
		l.call(4, 1);
		ItemStack ret = stack;
		if (l.isUserdata(1, ItemStack.class))
			ret = (ItemStack) l.toUserdata(1);
		l.pop(1);
		return ret;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
		pushValue("OnItemRightClick");
		{
			pushSelf();
			l.pushUserdataWithMeta(itemStackIn, "ItemStack");
			LuaUserdataManager.PushUserdata(l, worldIn);
			LuaUserdataManager.PushUserdata(l, playerIn);
		}
		l.call(4, 1);
		ItemStack ret = itemStackIn;
		if (l.isUserdata(1, ItemStack.class))
			ret = (ItemStack) l.toUserdata(1);
		l.pop(1);
        return ret;
    }

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
		pushValue("OnHitEntity");
		{
			pushSelf();
			l.pushUserdataWithMeta(stack, "ItemStack");
			LuaUserdataManager.PushUserdata(l, target);
			LuaUserdataManager.PushUserdata(l, attacker);
		}
		l.call(4, 1);
		boolean ret = false;
		if (l.isBoolean(1))
			ret = l.toBoolean(1);
		l.pop(1);
        return ret;
    }
}
