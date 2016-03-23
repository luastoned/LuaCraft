package com.luacraft.classes;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class LuaJavaBlock {
	public Block block;
	public World blockWorld;
	
	public int x,y,z;
	
	public LuaJavaBlock(World world, int x, int y, int z) {
		block = world.getBlock(x, y, z);
		blockWorld = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int getID() {
		return Block.getIdFromBlock(block);
	}
	
	public void setID(int id) {
		Block newBlock = Block.getBlockById(id);
		blockWorld.setBlock(x, y, z, newBlock);
		block = newBlock;
	}
	
	public int getMeta() {
		return blockWorld.getBlockMetadata(x,y,z);
	}
	
	public void setMeta(int meta) {
		blockWorld.setBlockMetadataWithNotify(x, y, z, meta, 3);
	}
	
	public void setAir() {
		blockWorld.setBlockToAir(x,y,z);
	}
	
	public void dropWithChance(double chance) {
		block.dropBlockAsItemWithChance(blockWorld, x, y, z, getMeta(), (float) chance, 0);
	}
	
	public void dropItem(ItemStack item) {
		float f = 0.7F;
        double d0 = (double)(blockWorld.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
        double d1 = (double)(blockWorld.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
        double d2 = (double)(blockWorld.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
        EntityItem entityitem = new EntityItem(blockWorld, (double)x + d0, (double)y + d1, (double)z + d2, item);
        entityitem.delayBeforeCanPickup = 10;
        setAir();
        blockWorld.spawnEntityInWorld(entityitem);
	}
	
	public TileEntity getTileEntity() {
		return blockWorld.getTileEntity(x, y, z);
	}
	
	public Vector getVec() {
		return new Vector(x,z,y);
	}
}
