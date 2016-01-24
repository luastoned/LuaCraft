package com.luacraft.classes;

import com.naef.jnlua.LuaUserdata;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class LuaJavaBlock implements LuaUserdata {
	public int x = 0;
	public int y = 0;
	public int z = 0;

	public World blockWorld;
	public Block block;

	public LuaJavaBlock(World world, int x, int y, int z) {
		this(world, new BlockPos(x, y, z));
	}

	public LuaJavaBlock(World world, BlockPos pos) {
		blockWorld = world;
		x = pos.getX();
		y = pos.getY();
		z = pos.getZ();
		block = world.getBlockState(pos).getBlock();
	}

	public BlockState getState() {
		return block.getBlockState();
	}

	public BlockPos getPos() {
		return new BlockPos(x, y, z);
	}

	public String getTypeName() {
		return "Block";
	}
}