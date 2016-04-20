package com.luacraft.meta;

import com.luacraft.LuaCraftState;
import com.luacraft.LuaUserdata;
import com.luacraft.classes.Angle;
import com.luacraft.classes.Color;
import com.luacraft.classes.LuaJavaBlock;
import com.luacraft.classes.LuaJavaThread;
import com.luacraft.classes.Vector;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

public class LuaObject {

	/**
	 * @author Gregor
	 * @function IsAngle
	 * @info Check if the object is an angle
	 * @arguments [[Object]]:object
	 * @return [[Boolean]]:angle
	 */

	public static JavaFunction IsAngle = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushBoolean(l.isUserdata(1, Angle.class));
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function IsAnimal
	 * @info Returns if the object is an animal entity
	 * @arguments [[Object]]:object
	 * @return [[Boolean]]:animal
	 */

	public static JavaFunction IsAnimal = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushBoolean(l.isUserdata(1, EntityAnimal.class));
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function IsBlock
	 * @info Returns if the object is a block
	 * @arguments [[Object]]:object
	 * @return [[Boolean]]:block
	 */

	public static JavaFunction IsBlock = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushBoolean(l.isUserdata(1, LuaJavaBlock.class));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function IsColor
	 * @info Check if the object is an angle
	 * @arguments [[Object]]:object
	 * @return [[Boolean]]:color
	 */

	public static JavaFunction IsColor = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushBoolean(l.isUserdata(1, Color.class));
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function IsEntity
	 * @info Returns if the object is an entity
	 * @arguments [[Object]]:object
	 * @return [[Boolean]]:entity
	 */

	public static JavaFunction IsEntity = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushBoolean(l.isUserdata(1, Entity.class));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function IsInstanceOf
	 * @info Returns if the object is an instance of the given
	 * @arguments [[Object]]:object
	 * @return [[Boolean]]:instance
	 */

	public static JavaFunction IsInstanceOf = new JavaFunction() {
		public int invoke(LuaState l) {
			Object self = l.checkUserdata(1);
			Object other = l.checkUserdata(2);
			l.pushBoolean(self.getClass().isAssignableFrom(other.getClass()));
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function IsItem
	 * @info Returns if the object is an item entity
	 * @arguments [[Object]]:object
	 * @return [[Boolean]]:item
	 */

	public static JavaFunction IsItem = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushBoolean(l.isUserdata(1, EntityItem.class));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function IsItemStack
	 * @info Returns if the object is an item stack
	 * @arguments [[Object]]:object
	 * @return [[Boolean]]:itemstack
	 */

	public static JavaFunction IsItemStack = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushBoolean(l.isUserdata(1, ItemStack.class));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function IsLiving
	 * @info Returns if the object is a living entity
	 * @arguments [[Object]]:object
	 * @return [[Boolean]]:living
	 */

	public static JavaFunction IsLiving = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushBoolean(l.isUserdata(1, EntityLiving.class));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function IsMob
	 * @info Returns if the object is a mob entity
	 * @arguments [[Object]]:object
	 * @return [[Boolean]]:mob
	 */

	public static JavaFunction IsMob = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushBoolean(l.isUserdata(1, EntityMob.class));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function IsNBTTag
	 * @info Returns if the object is an nbt tag
	 * @arguments [[Object]]:object
	 * @return [[Boolean]]:nbttag
	 */

	public static JavaFunction IsNBTTag = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushBoolean(l.isUserdata(1, NBTTagCompound.class));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function IsPlayer
	 * @info Returns if the object is a player entity
	 * @arguments [[Object]]:object
	 * @return [[Boolean]]:player
	 */

	public static JavaFunction IsPlayer = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushBoolean(l.isUserdata(1, EntityPlayer.class));
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function IsTameable
	 * @info Returns if the object is a tameable entity
	 * @arguments [[Object]]:object
	 * @return [[Boolean]]:tameable
	 */

	public static JavaFunction IsTameable = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushBoolean(l.isUserdata(1, EntityTameable.class));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function IsThread
	 * @info Returns if the object is a thread
	 * @arguments [[Object]]:object
	 * @return [[Boolean]]:thread
	 */

	public static JavaFunction IsThread = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushBoolean(l.isUserdata(1, LuaJavaThread.class));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function IsValid
	 * @info Returns if the object is not null
	 * @arguments [[Object]]:object
	 * @return [[Boolean]]:valid
	 */

	public static JavaFunction IsValid = new JavaFunction() {
		public int invoke(LuaState l) {
			Object self = l.checkUserdata(1);
			l.pushBoolean(self != null);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function IsWorld
	 * @info Returns if the object is a world
	 * @arguments [[Object]]:object
	 * @return [[Boolean]]:world
	 */

	public static JavaFunction IsWorld = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushBoolean(l.isUserdata(1, World.class));
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function IsVector
	 * @info Check if the object is an vector
	 * @arguments [[Object]]:object
	 * @return [[Boolean]]:vec
	 */

	public static JavaFunction IsVector = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushBoolean(l.isUserdata(1, Vector.class));
			return 1;
		}
	};

	/**
	 * @author fr1kin
	 * @function IsExplosion
	 * @info Check if the object is an explosion
	 * @arguments [[Object]]:object
	 * @return [[Boolean]]:explosion
	 */
	public static JavaFunction IsExplosion = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushBoolean(l.isUserdata(1, Explosion.class));
			return 1;
		}
	};

	/**
	 * @author fr1kin
	 * @function IsExplosion
	 * @info Check if the object is an explosion
	 * @arguments [[Object]]:object
	 * @return [[Boolean]]:chunk
	 */
	public static JavaFunction IsChunk = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushBoolean(l.isUserdata(1, Chunk.class));
			return 1;
		}
	};

	/**
	 * @author fr1kin
	 * @function IsExplosion
	 * @info Check if the object is an explosion
	 * @arguments [[Object]]:object
	 * @return [[Boolean]]:explosion
	 */
	public static JavaFunction IsBiomeGenBase = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushBoolean(l.isUserdata(1, BiomeGenBase.class));
			return 1;
		}
	};

	public static void Init(final LuaCraftState l) {
		l.newMetatable("Object");
		{
			LuaUserdata.SetupBasicMeta(l);
			LuaUserdata.SetupMeta(l, false);

			l.pushJavaFunction(IsAngle);
			l.setField(-2, "IsAngle");
			l.pushJavaFunction(IsAnimal);
			l.setField(-2, "IsAnimal");
			l.pushJavaFunction(IsBlock);
			l.setField(-2, "IsBlock");
			l.pushJavaFunction(IsColor);
			l.setField(-2, "IsColor");
			l.pushJavaFunction(IsEntity);
			l.setField(-2, "IsEntity");
			l.pushJavaFunction(IsInstanceOf);
			l.setField(-2, "IsInstanceOf");
			l.pushJavaFunction(IsItem);
			l.setField(-2, "IsItem");
			l.pushJavaFunction(IsItemStack);
			l.setField(-2, "IsItemStack");
			l.pushJavaFunction(IsLiving);
			l.setField(-2, "IsLiving");
			l.pushJavaFunction(IsMob);
			l.setField(-2, "IsMob");
			l.pushJavaFunction(IsNBTTag);
			l.setField(-2, "IsNBTTag");
			l.pushJavaFunction(IsPlayer);
			l.setField(-2, "IsPlayer");
			l.pushJavaFunction(IsTameable);
			l.setField(-2, "IsTameable");
			l.pushJavaFunction(IsThread);
			l.setField(-2, "IsThread");
			l.pushJavaFunction(IsValid);
			l.setField(-2, "IsValid");
			l.pushJavaFunction(IsWorld);
			l.setField(-2, "IsWorld");
			l.pushJavaFunction(IsVector);
			l.setField(-2, "IsVector");
			l.pushJavaFunction(IsExplosion);
			l.setField(-2, "IsExplosion");
			l.pushJavaFunction(IsChunk);
			l.setField(-2, "IsChunk");
			l.pushJavaFunction(IsBiomeGenBase);
			l.setField(-2, "IsBiomeGenBase");
		}
		l.pop(1);
	}
}
