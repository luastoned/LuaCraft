package com.luacraft.classes;

import net.minecraft.client.resources.model.ModelResourceLocation;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.luacraft.LuaUserdataManager;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.LuaType;
import com.naef.jnlua.LuaUserdata;

public class LuaScriptedItem extends Item implements LuaUserdata {
	public LuaState l;
	
	public LuaScriptedItem(LuaState l){
		this.l = l;
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
			
			LuaScriptedItem item = new LuaScriptedItem(l);
			item.setUnlocalizedName(classname);
			
			l.newMetatable("ScriptedItemsRegistry");
			l.pushString(item.getUnlocalizedName());
			l.pushValue(1);
			l.setTable(-3);
			
			GameRegistry.registerItem(item, classname);
			return 0;
		}
	};
	
	public static JavaFunction GetTable = new JavaFunction()
	{
		public int invoke(LuaState l)
		{
			l.newMetatable("ScriptedItemsRegistry");
			return 1;
		}
	};
	
	public static JavaFunction Get = new JavaFunction()
	{
		public int invoke(LuaState l)
		{
			l.newMetatable("ScriptedItemsRegistry");
			l.getField(-1, l.checkString(1));
			return 1;
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
			l.setField(-2, "GetModel");
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
			l.pushJavaFunction(GetTable);
			l.setField(-2, "GetTable");
			l.pushJavaFunction(Get);
			l.setField(-2, "Get");
		}
		l.setGlobal("item");
		
		l.newMetatable("ScriptedItemsRegistry");
		l.pop(1);
	}
	
	@Override
	public String getTypeName() {
		return "ScriptedItem";
	}
	
	private void pushSelf()
	{
		l.newMetatable("ScriptedItemsRegistry");
		l.getField(-1, getUnlocalizedName());
		l.remove(-2);
	}
	
	private void pushValue(String name) {
		pushSelf();
		l.getField(-1, name);
		l.remove(-2);
		
		if (l.isNil(-1)) // Fallback to metatable defaults
		{
			l.pop(1);
			l.newMetatable("ScriptedItem");
			l.getField(-1, name);
			l.remove(-2);
		}
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack) {
		pushValue("MaxStackSize");
		int ret = l.toInteger(-1);
		l.pop(1);
		return ret;
	}
	
    @SideOnly(Side.CLIENT)
	@Override
    public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining)
    {
    	synchronized (l)
		{
	    	pushValue("GetModel");
			{
				pushSelf();
				l.pushUserdataWithMeta(stack, "ItemStack");
				LuaUserdataManager.PushUserdata(l, player);
				l.pushInteger(useRemaining);
			}
			l.call(4, 1);
			
			ModelResourceLocation ret = null;
			if (l.isUserdata(1, ModelResourceLocation.class))
				ret = (ModelResourceLocation) l.toUserdata(1);
			
			l.setTop(0);
			return ret;
		}
    }
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
    	synchronized (l)
		{
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
				
			l.setTop(0);
			return ret;
		}
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
	{
    	synchronized (l)
		{
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
			
			l.setTop(0);
			return ret;
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
    	synchronized (l)
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
			
			l.setTop(0);
			return ret;
		}
    }

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
    	synchronized (l)
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
			
			l.setTop(0);
			return ret;
		}
    }
}
