package com.luacraft;

import com.luacraft.classes.LuaJavaBlock;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class LuaUserdataManager {
	private static boolean RecursiveBaseMetaCheck(LuaState l) {
		// Check self
		l.pushValue(2); // Push the key for lookup in the meta
		l.getTable(-2);

		if (!l.isNil(-1)) // What is being requested exists, so use it
			return true;

		l.pop(1);

		l.getField(-1, "__basemeta");
		if (!l.isNil(-1))
			return RecursiveBaseMetaCheck(l); // <3 recursion

		return false;
	}

	public static JavaFunction __index = new JavaFunction() {
		public int invoke(LuaState l) {
			Object self = l.checkUserdata(1);

			l.getMetatable(1); // Push our metatable
			if (RecursiveBaseMetaCheck(l))
				return 1;

			String index = getUniqueID(self);

			// Check registry
			l.newMetatable("PersistantData");
			l.getField(-1, index);

			if (l.isNil(-1) || !l.isTable(-1))
				return 0; // Nothing

			l.pushValue(2); // Push the key for table lookup
			l.getTable(-2); // Get the value in the registry and push
			return 1;
		}
	};

	public static JavaFunction __newindex = new JavaFunction() {
		public int invoke(LuaState l) {
			Object self = l.checkUserdata(1);
			String index = getUniqueID(self);

			newUserdataCache(l, index);
			l.pushValue(2); // Push the given key
			l.pushValue(3); // Push the given value
			l.setTable(-3);
			return 0;
		}
	};

	private static void newUserdataCache(LuaState l, String index) {
		l.newMetatable("PersistantData");
		l.getField(-1, index);

		if (l.isNil(-1)) {
			l.pop(1);
			l.newTable();
			l.setField(-2, index);
			l.getField(-1, index);
		}
		l.remove(-2);
	}

	private static String getUniqueID(Object obj) {
		if (obj instanceof EntityPlayer)
			return ((EntityPlayer) obj).getGameProfile().getId().toString();
		if (obj instanceof Entity)
			return ((Entity) obj).getPersistentID().toString();

		return Integer.toString(obj.hashCode());
	}

	public static void PushUserdata(LuaState l, Object obj) {
		if (obj == null) {
			l.pushNil();
			return;
		}

		String meta = GetMetatableForObject(obj);
		l.pushUserdataWithMeta(obj, meta);
	}

	private static String GetMetatableForObject(Object ent) {
		if (ent instanceof LuaJavaBlock)
			return "Block";
		else if (ent instanceof EntityPlayer)
			return "Player";
		else if (ent instanceof World)
			return "World";
		else if (ent instanceof EntityLiving)
			return "Living";
		else if (ent instanceof EntityLivingBase)
			return "LivingBase";
		else if (ent instanceof EntityItem)
			return "EntityItem";

		/*
		 * else if (ent instanceof EntityWolf) return "Wolf"; else if (ent instanceof LuaHuman) return "NPC";
		 */

		return "Entity"; // Default
	}

	public static void SetupMetaMethods(LuaState l) {
		l.pushJavaFunction(__index);
		l.setField(-2, "__index");
		l.pushJavaFunction(__newindex);
		l.setField(-2, "__newindex");
	}
}
