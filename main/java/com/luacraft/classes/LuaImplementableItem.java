package com.luacraft.classes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.LuaUserdata;

public class LuaImplementableItem extends Item implements LuaUserdata {
	public int tblRef;
	public LuaState l;
	
	public LuaImplementableItem(LuaState l){
		l.newTable();
		tblRef = l.ref(LuaState.REGISTRYINDEX);
		this.l = l;
	}
	
	public static JavaFunction CreateItem = new JavaFunction()
	{
		public int invoke(LuaState l)
		{
			l.pushUserdataWithMeta(new LuaImplementableItem(l), "LuaItem");
			return 1;
		}
	};
	
	public static JavaFunction __index = new JavaFunction()
	{
		public int invoke(LuaState l)
		{
			LuaImplementableItem item = (LuaImplementableItem)l.checkUserdata(1, LuaImplementableItem.class, "LuaItem");
			l.rawGet(LuaState.REGISTRYINDEX, item.tblRef);
			l.pushValue(2);
			l.rawGet(-2);
			if(!l.isNoneOrNil(-1))
				return 1;
			l.pop(1);
			
			l.getMetatable(1);
			l.pushValue(2);
			l.rawGet(-2);
			return 1;
		}
	};
	
	public static JavaFunction __newindex = new JavaFunction()
	{
		public int invoke(LuaState l)
		{
			LuaImplementableItem item = (LuaImplementableItem)l.checkUserdata(1, LuaImplementableItem.class, "LuaItem");
			l.rawSet(LuaState.REGISTRYINDEX, item.tblRef);
			return 1;
		}
	};
	
	@Override
	public String getTypeName() {
		return "LuaItem";
	}
	
	public static void OpenImplementableItemLib(LuaState l ) {
		l.newMetatable("LuaItem");
		{
			l.pushJavaFunction(__index);
			l.setField(-2, "__index");
			
			l.pushInteger(64);
			l.setField(-2, "MaxStackSize");
			
			l.pushString("luaItem");
			l.setField(-2, "Name");
			
			l.pushNil();// TODO: Defaults maybe?
			l.setField(-2, "PlayerUse");
		
		}
		l.pop(1);
	}
	
	private void pushValue() {
		l.rawGet(LuaState.REGISTRYINDEX, tblRef);
		l.pushValue(-2);
		l.rawGet(-2);
		if(!l.isNoneOrNil(-1))
			return;
		l.pop(1);
		
		l.getMetatable(1);
		l.pushValue(-2);
		l.rawGet(-2);
	}
	
	@Override
	public String getUnlocalizedName() {
		l.pushString("Name");
		pushValue();
		String ret = l.toString(-1);
		l.pop(1);
		return ret;
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack) {
		l.pushString("MaxStackSize");
		pushValue();
		int ret = l.toInteger(-1);
		l.pop(1);
		return ret;
	}
	
	/*@Override
	public boolean onItemUse(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, World par3World, int bx, int by,
			int bz, int br, float x, float y, float z) {
		
		return super.onItemUse(par1ItemStack, par2EntityPlayer, par3World, bx, by,
				bz, br, x, y, z);
	}*/
}
