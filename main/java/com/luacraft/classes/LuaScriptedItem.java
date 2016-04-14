package com.luacraft.classes;

import com.luacraft.LuaCraft;
import com.luacraft.LuaCraftState;
import com.luacraft.LuaUserdata;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.LuaType;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.IResource;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LuaScriptedItem extends Item implements com.naef.jnlua.LuaUserdata {
	private LuaState l;

	public LuaScriptedItem(LuaState l) {
		super();
		this.l = l;
	}

	public static JavaFunction __tostring = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaScriptedItem self = (LuaScriptedItem) l.checkUserdata(1, LuaScriptedItem.class, "ScriptedItem");
			l.pushString(String.format("ScriptedItem: 0x%08x", l.toPointer(1)));
			return 1;
		}
	};

	public static JavaFunction Register = new JavaFunction() {
		public int invoke(LuaState l) {
			l.checkType(1, LuaType.TABLE);
			String resourceDomain = l.checkString(2, LuaCraft.DEFAULT_RESOURCEPACK);

			String className, resourcePath;

			l.getField(1, "Name");
			className = l.checkString(-1, "null");
			l.pop(1);

			l.getField(1, "ResourcePath");
			resourcePath = l.checkString(-1, "null");
			l.pop(1);

			LuaScriptedItem item = new LuaScriptedItem(l);
			item.setUnlocalizedName(className);

			l.getField(1, "CreativeTab");
			int index = l.checkInteger(-1, 4);
			l.pop(1);
			item.setCreativeTab(CreativeTabs.creativeTabArray[index]);

			l.newMetatable("ScriptedItemsRegistry");
			l.pushString(item.getUnlocalizedName());
			l.pushValue(1);
			l.setTable(-3);

			GameRegistry.register(item, new ResourceLocation(resourceDomain, resourcePath));
			return 0;
		}
	};

	public static JavaFunction GetTable = new JavaFunction() {
		public int invoke(LuaState l) {
			l.newMetatable("ScriptedItemsRegistry");
			return 1;
		}
	};

	public static JavaFunction Get = new JavaFunction() {
		public int invoke(LuaState l) {
			l.newMetatable("ScriptedItemsRegistry");
			l.getField(-1, l.checkString(1));
			return 1;
		}
	};

	public static JavaFunction dummyFunc = new JavaFunction() {
		public int invoke(LuaState l) {
			return 0;
		}
	};

	public static void Init(LuaState l) {

		l.pushInteger(EnumAction.NONE.ordinal());
		l.setGlobal("ITEM_ACTION_NONE");
		l.pushInteger(EnumAction.EAT.ordinal());
		l.setGlobal("ITEM_ACTION_EAT");
		l.pushInteger(EnumAction.DRINK.ordinal());
		l.setGlobal("ITEM_ACTION_DRINK");
		l.pushInteger(EnumAction.BLOCK.ordinal());
		l.setGlobal("ITEM_ACTION_BLOCK");
		l.pushInteger(EnumAction.BOW.ordinal());
		l.setGlobal("ITEM_ACTION_BOW");

		l.pushInteger(EnumRarity.COMMON.ordinal());
		l.setGlobal("ITEM_RARITY_COMMON");
		l.pushInteger(EnumRarity.UNCOMMON.ordinal());
		l.setGlobal("ITEM_RARITY_UNCOMMON");
		l.pushInteger(EnumRarity.RARE.ordinal());
		l.setGlobal("ITEM_RARITY_RARE");
		l.pushInteger(EnumRarity.EPIC.ordinal());
		l.setGlobal("ITEM_RARITY_EPIC");

		l.pushInteger(EnumActionResult.SUCCESS.ordinal());
		l.setGlobal("ACTION_RESULT_SUCCESS");
		l.pushInteger(EnumActionResult.PASS.ordinal());
		l.setGlobal("ACTION_RESULT_PASS");
		l.pushInteger(EnumActionResult.FAIL.ordinal());
		l.setGlobal("ACTION_RESULT_FAIL");

		l.pushInteger(0);
		l.setGlobal("TAB_BUILDING");
		l.pushInteger(1);
		l.setGlobal("TAB_DECORATIONS");
		l.pushInteger(2);
		l.setGlobal("TAB_REDSTONE");
		l.pushInteger(3);
		l.setGlobal("TAB_TRANSPORTATION");
		l.pushInteger(4);
		l.setGlobal("TAB_MISC");
		l.pushInteger(5);
		l.setGlobal("TAB_ALL");
		l.pushInteger(6);
		l.setGlobal("TAB_FOOD");
		l.pushInteger(7);
		l.setGlobal("TAB_TOOLS");
		l.pushInteger(8);
		l.setGlobal("TAB_COMBAT");
		l.pushInteger(9);
		l.setGlobal("TAB_BREWING");
		l.pushInteger(10);
		l.setGlobal("TAB_MATERIALS");
		l.pushInteger(11);
		l.setGlobal("TAB_INVENTORY");

		l.newMetatable("ScriptedItem");
		{
			l.pushJavaFunction(__tostring);
			l.setField(-2, "__tostring");

			l.pushInteger(64);
			l.setField(-2, "MaxStackSize");
			l.pushBoolean(false);
			l.setField(-2, "Full3D");
			l.pushInteger(EnumAction.NONE.ordinal());
			l.setField(-2, "UseAction");
			l.pushInteger(0);
			l.setField(-2, "UseDuration");
			l.pushInteger(EnumRarity.COMMON.ordinal());
			l.setField(-2, "Rarity");
			l.pushInteger(6000);
			l.setField(-2, "Lifespan");
			l.pushInteger(4);
			l.setField(-2, "CreativeTab");

			l.pushJavaFunction(dummyFunc);
			l.setField(-2, "GetMaxDamage");
			l.pushJavaFunction(dummyFunc);
			l.setField(-2, "GetModel");
			l.pushJavaFunction(dummyFunc);
			l.setField(-2, "OnItemUse");
			l.pushJavaFunction(dummyFunc);
			l.setField(-2, "OnItemUseFinish");
			l.pushJavaFunction(dummyFunc);
			l.setField(-2, "OnItemRightClick");
			l.pushJavaFunction(dummyFunc);
			l.setField(-2, "OnLeftClickEntity");
			l.pushJavaFunction(dummyFunc);
			l.setField(-2, "OnHitEntity");
			l.pushJavaFunction(dummyFunc);
			l.setField(-2, "OnBlockDestroyed");
			l.pushJavaFunction(dummyFunc);
			l.setField(-2, "CanInteractWithEntity");
			l.pushJavaFunction(dummyFunc);
			l.setField(-2, "OnUpdate");
			l.pushJavaFunction(dummyFunc);
			l.setField(-2, "OnCreated");
			l.pushJavaFunction(dummyFunc);
			l.setField(-2, "OnDroppedByPlayer");
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

	private void pushSelf() {
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
	public int getMaxDamage(ItemStack stack) {
		synchronized (l) {
			if (!l.isOpen())
				return super.getMaxDamage();

			pushValue("GetMaxDamage");
			{
				pushSelf();
				l.pushUserdataWithMeta(stack, "ItemStack");
			}
			l.call(2, 1);
			int ret = l.toInteger(1);
			l.setTop(0);
			return ret;
		}
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		synchronized (l) {
			if (!l.isOpen())
				return super.getItemStackLimit(stack);

			pushValue("MaxStackSize");
			int ret = l.toInteger(-1);
			l.pop(1);
			return ret;
		}
	}

	/*
	// REMOVED in 1.9
	// TODO: Find replacement
	@Override
	@SideOnly(Side.CLIENT)
	public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining) {
		synchronized (l) {
			if (!l.isOpen())
				return super.getModel(stack, player, useRemaining);

			pushValue("GetModel");
			{
				pushSelf();
				l.pushUserdataWithMeta(stack, "ItemStack");
				LuaUserdata.PushUserdata(l, player);
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
	*/

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		synchronized (l) {
			if (!l.isOpen())
				return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);

			pushValue("OnItemUse");
			{
				pushSelf();
				l.pushUserdataWithMeta(stack, "ItemStack");
				LuaUserdata.PushUserdata(l, playerIn);
				LuaUserdata.PushUserdata(l, worldIn);
				LuaUserdata.PushUserdata(l, new LuaJavaBlock(worldIn, pos));
				l.pushInteger(facing.ordinal());
				l.pushUserdataWithMeta(new Vector(hitX, hitZ, hitY), "Vector");
			}
			l.call(7, 1);
			int action = l.toInteger(1);
			l.setTop(0);
			return EnumActionResult.values()[action];
		}
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		synchronized (l) {
			if (!l.isOpen())
				return super.onItemUseFinish(stack, worldIn, entityLiving);

			pushValue("OnItemUseFinish");
			{
				pushSelf();
				l.pushUserdataWithMeta(stack, "ItemStack");
				LuaUserdata.PushUserdata(l, worldIn);
				LuaUserdata.PushUserdata(l, entityLiving); // TODO: Check this
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
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		synchronized (l) {
			if (!l.isOpen())
				return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);

			pushValue("OnItemRightClick");
			{
				pushSelf();
				l.pushUserdataWithMeta(itemStackIn, "ItemStack");
				LuaUserdata.PushUserdata(l, worldIn);
				LuaUserdata.PushUserdata(l, playerIn);
			}
			l.call(4, 1);

			ItemStack ret = itemStackIn;

			if (l.isUserdata(1, ItemStack.class))
				ret = (ItemStack) l.toUserdata(1);

			l.setTop(0);
			return new ActionResult<>(EnumActionResult.SUCCESS, ret);
		}
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		synchronized (l) {
			if (!l.isOpen())
				return super.onLeftClickEntity(stack, player, entity);

			pushValue("OnLeftClickEntity");
			{
				pushSelf();
				l.pushUserdataWithMeta(stack, "ItemStack");
				LuaUserdata.PushUserdata(l, player);
				LuaUserdata.PushUserdata(l, entity);
			}
			l.call(4, 1);
			boolean ret = l.toBoolean(1);
			l.setTop(0);
			return ret;
		}
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		synchronized (l) {
			if (!l.isOpen())
				return super.hitEntity(stack, target, attacker);

			pushValue("OnHitEntity");
			{
				pushSelf();
				l.pushUserdataWithMeta(stack, "ItemStack");
				LuaUserdata.PushUserdata(l, target);
				LuaUserdata.PushUserdata(l, attacker);
			}
			l.call(4, 1);
			boolean ret = l.toBoolean(1);
			l.setTop(0);
			return ret;
		}
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockIn, BlockPos pos, EntityLivingBase entityLiving) {
		synchronized (l) {
			if (!l.isOpen())
				return super.onBlockDestroyed(stack, worldIn, blockIn, pos, entityLiving);

			pushValue("OnBlockDestroyed");
			{
				pushSelf();
				l.pushUserdataWithMeta(stack, "ItemStack");
				LuaUserdata.PushUserdata(l, worldIn);
				LuaUserdata.PushUserdata(l, blockIn);
				LuaUserdata.PushUserdata(l, entityLiving); // TODO: Check this
			}
			l.call(5, 1);
			boolean ret = l.toBoolean(1);
			l.setTop(0);
			return ret;
		}
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
		synchronized (l) {
			if (!l.isOpen())
				return super.itemInteractionForEntity(stack, playerIn, target, hand);

			pushValue("CanInteractWithEntity");
			{
				pushSelf();
				l.pushUserdataWithMeta(stack, "ItemStack");
				LuaUserdata.PushUserdata(l, playerIn);
				LuaUserdata.PushUserdata(l, target);
			}
			l.call(4, 1);
			boolean ret = l.toBoolean(1);
			l.setTop(0);
			return ret;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D() {
		synchronized (l) {
			if (!l.isOpen())
				return super.isFull3D();

			pushValue("Full3D");
			boolean ret = l.toBoolean(-1);
			l.pop(1);
			return ret;
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			pushValue("OnUpdate");
			{
				pushSelf();
				l.pushUserdataWithMeta(stack, "ItemStack");
				LuaUserdata.PushUserdata(l, worldIn);
				LuaUserdata.PushUserdata(l, entityIn);
				l.pushInteger(itemSlot);
				l.pushBoolean(isSelected);
			}
			l.call(6, 0);
		}
	}

	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			pushValue("OnCreated");
			{
				pushSelf();
				l.pushUserdataWithMeta(stack, "ItemStack");
				LuaUserdata.PushUserdata(l, worldIn);
				LuaUserdata.PushUserdata(l, playerIn);
			}
			l.call(4, 0);
		}
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		synchronized (l) {
			if (!l.isOpen())
				return super.getItemUseAction(stack);

			pushValue("UseAction");
			int ret = l.toInteger(-1);
			l.pop(1);

			switch (ret) {
			case 0:
				return EnumAction.NONE;
			case 1:
				return EnumAction.EAT;
			case 2:
				return EnumAction.DRINK;
			case 3:
				return EnumAction.BLOCK;
			case 4:
				return EnumAction.BOW;
			}

			return EnumAction.values()[ret];
		}
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		synchronized (l) {
			if (!l.isOpen())
				return super.getMaxItemUseDuration(stack);

			pushValue("UseDuration");
			int ret = l.toInteger(-1);
			l.pop(1);
			return ret;
		}
	}

	public EnumRarity getRarity(ItemStack stack) {
		synchronized (l) {
			if (!l.isOpen())
				return super.getRarity(stack);

			pushValue("Rarity");
			int ret = l.toInteger(-1);
			l.pop(1);

			switch (ret) {
			case 0:
				return EnumRarity.COMMON;
			case 1:
				return EnumRarity.UNCOMMON;
			case 2:
				return EnumRarity.RARE;
			case 3:
				return EnumRarity.EPIC;
			}

			return EnumRarity.values()[ret];
		}
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
		synchronized (l) {
			if (!l.isOpen())
				return super.onDroppedByPlayer(item, player);

			pushValue("OnDroppedByPlayer");
			{
				pushSelf();
				l.pushUserdataWithMeta(item, "ItemStack");
				LuaUserdata.PushUserdata(l, player);
			}
			l.call(3, 1);
			boolean ret = l.toBoolean(1);
			l.pop(1);
			return ret;
		}
	}

	@Override
	public int getEntityLifespan(ItemStack itemStack, World world) {
		synchronized (l) {
			if (!l.isOpen())
				return super.getEntityLifespan(itemStack, world);

			pushValue("Lifespan");
			int ret = l.toInteger(-1);
			l.pop(1);
			return ret;
		}
	}
}
