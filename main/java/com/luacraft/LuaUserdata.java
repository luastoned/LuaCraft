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

public class LuaUserdata {

	public static JavaFunction __eq = new JavaFunction() {
		public int invoke(LuaState l) {
			Object self = l.checkUserdata(1);
			Object other = l.checkUserdata(2);
			l.pushBoolean(self == other);
			return 1;
		}
	};

	public static void PushBaseMeta(LuaState l) {
		l.getMetatable(1); // Push our metatable
		l.pushValue(2); // Push the key for lookup in the meta
		if (RecursiveBaseMetaCheck(l, -2))
			return;
	}

	private static boolean RecursiveBaseMetaCheck(LuaState l, int index) {
		// 1 - metatable
		// 2 - key
		l.pushValue(-1); // Push a copy of our key
		l.getTable(index - 1); // Pop off copy and push value

		if (!l.isNil(-1)) // What is being requested exists, so use it
			return true;

		l.pop(1); // Pop off the nil

		l.getField(index, "__basemeta");
		l.replace(index - 1); // replace original table with base

		if (!l.isNil(index)) // Base meta exists, go again
			return RecursiveBaseMetaCheck(l, index);

		return false; // Done
	}

	private static JavaFunction __index = new JavaFunction() {
		public int invoke(LuaState l) {
			PushBaseMeta(l);
			return 1;
		}
	};

	private static JavaFunction __index_persistant = new JavaFunction() {
		public int invoke(LuaState l) {
			Object self = l.checkUserdata(1);

			l.getMetatable(1); // Push our metatable
			l.pushValue(2); // Push the key for lookup in the meta
			if (RecursiveBaseMetaCheck(l, -2))
				return 1;

			// Check registry
			l.newMetatable("PersistantData");
			l.pushNumber(self.hashCode());
			l.getTable(-2);

			if (l.isNil(-1) || !l.isTable(-1))
				return 0; // Nothing

			l.pushValue(2); // Push the key for table lookup
			l.getTable(-2); // Get the value in the registry and push
			return 1;
		}
	};

	private static JavaFunction __newindex = new JavaFunction() {
		public int invoke(LuaState l) {
			Object self = l.checkUserdata(1);

			int index = self.hashCode();

			l.newMetatable("PersistantData");
			l.pushNumber(index);
			l.getTable(-2);

			if (l.isNil(-1)) {
				l.pop(1);
				l.pushNumber(index);
				l.newTable();
				l.setTable(-3);
			}

			l.pushValue(2); // Push the given key
			l.pushValue(3); // Push the given value
			l.setTable(-3);
			return 0;
		}
	};

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
		else if (ent instanceof Entity)
			return "Entity";

		return "Object";
	}

	public static void SetupBasicMeta(LuaState l) {
		l.newMetatable("jnlua.Object");
		l.getField(-1, "__gc");
		l.remove(-2);
		l.setField(-2, "__gc");

		l.pushJavaFunction(__eq);
		l.setField(-2, "__eq");
	}

	public static void SetupMeta(LuaState l, boolean persitantData) {
		if (persitantData) {
			l.pushJavaFunction(__index_persistant);
			l.setField(-2, "__index");
			l.pushJavaFunction(__newindex);
			l.setField(-2, "__newindex");
		} else {
			l.pushJavaFunction(__index);
			l.setField(-2, "__index");
		}
	}
}
