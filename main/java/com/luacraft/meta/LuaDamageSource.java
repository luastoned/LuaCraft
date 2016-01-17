package com.luacraft.meta;

import net.minecraft.util.DamageSource;

import com.luacraft.LuaCraftState;
import com.luacraft.LuaUserdataManager;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

public class LuaDamageSource {

	public static JavaFunction __tostring = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			l.pushString(String.format("DamageSource: 0x%08x", l.toPointer(1)));
			return 1;
		}
	};

	public static JavaFunction GetDamageType = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			l.pushString(self.getDamageType());
			return 1;
		}
	};

	public static JavaFunction GetEntity = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			LuaUserdataManager.PushUserdata(l, self.getEntity());
			return 1;
		}
	};

	public static JavaFunction GetHungerDamage = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			l.pushNumber(self.getHungerDamage());
			return 1;
		}
	};

	public static JavaFunction GetSource = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			LuaUserdataManager.PushUserdata(l, self.getSourceOfDamage());
			return 1;
		}
	};

	public static JavaFunction IsAbsolute = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			l.pushBoolean(self.isDamageAbsolute());
			return 1;
		}
	};

	public static JavaFunction IsDifficultyScaled = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			l.pushBoolean(self.isDifficultyScaled());
			return 1;
		}
	};

	public static JavaFunction IsExplosion = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			l.pushBoolean(self.isExplosion());
			return 1;
		}
	};

	public static JavaFunction IsFireDamage = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			l.pushBoolean(self.isFireDamage());
			return 1;
		}
	};

	public static JavaFunction IsMagicDamage = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			l.pushBoolean(self.isMagicDamage());
			return 1;
		}
	};

	public static JavaFunction IsProjectile = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			l.pushBoolean(self.isProjectile());
			return 1;
		}
	};

	public static JavaFunction IsUnblockable = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			l.pushBoolean(self.isUnblockable());
			return 1;
		}
	};

	public static JavaFunction SetAllowedInCreative = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			LuaUserdataManager.PushUserdata(l, self.setDamageAllowedInCreativeMode());
			return 1;
		}
	};

	public static JavaFunction SetBypassesArmor = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			LuaUserdataManager.PushUserdata(l, self.setDamageBypassesArmor());
			return 1;
		}
	};

	public static JavaFunction SetIsAbsolute = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			LuaUserdataManager.PushUserdata(l, self.setDamageIsAbsolute());
			return 1;
		}
	};

	public static JavaFunction SetDifficultyScaled = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			LuaUserdataManager.PushUserdata(l, self.setDifficultyScaled());
			return 1;
		}
	};

	public static JavaFunction SetExplosion = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			LuaUserdataManager.PushUserdata(l, self.setExplosion());
			return 1;
		}
	};

	public static JavaFunction SetFireDamage = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			LuaUserdataManager.PushUserdata(l, self.setFireDamage());
			return 1;
		}
	};

	public static JavaFunction SetMagicDamage = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			LuaUserdataManager.PushUserdata(l, self.setMagicDamage());
			return 1;
		}
	};

	public static JavaFunction SetProjectile = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			LuaUserdataManager.PushUserdata(l, self.setProjectile());
			return 1;
		}
	};

	public static void Init(final LuaCraftState l) {
		l.newMetatable("DamageSource");
		{
			l.pushValue(-1);
			l.setField(-2, "__index");

			l.pushJavaFunction(__tostring);
			l.setField(-2, "__tostring");

			l.pushJavaFunction(GetDamageType);
			l.setField(-2, "GetDamageType");
			l.pushJavaFunction(GetEntity);
			l.setField(-2, "GetEntity");
			l.pushJavaFunction(GetHungerDamage);
			l.setField(-2, "GetHungerDamage");
			l.pushJavaFunction(GetSource);
			l.setField(-2, "GetSource");
			l.pushJavaFunction(IsAbsolute);
			l.setField(-2, "IsAbsolute");
			l.pushJavaFunction(IsDifficultyScaled);
			l.setField(-2, "IsDifficultyScaled");
			l.pushJavaFunction(IsExplosion);
			l.setField(-2, "IsExplosion");
			l.pushJavaFunction(IsFireDamage);
			l.setField(-2, "IsFireDamage");
			l.pushJavaFunction(IsMagicDamage);
			l.setField(-2, "IsMagicDamage");
			l.pushJavaFunction(IsProjectile);
			l.setField(-2, "IsProjectile");
			l.pushJavaFunction(IsUnblockable);
			l.setField(-2, "IsUnblockable");
			l.pushJavaFunction(SetAllowedInCreative);
			l.setField(-2, "SetAllowedInCreative");
			l.pushJavaFunction(SetBypassesArmor);
			l.setField(-2, "SetBypassesArmor");
			l.pushJavaFunction(SetIsAbsolute);
			l.setField(-2, "SetIsAbsolute");
			l.pushJavaFunction(SetDifficultyScaled);
			l.setField(-2, "SetDifficultyScaled");
			l.pushJavaFunction(SetExplosion);
			l.setField(-2, "SetExplosion");
			l.pushJavaFunction(SetFireDamage);
			l.setField(-2, "SetFireDamage");
			l.pushJavaFunction(SetMagicDamage);
			l.setField(-2, "SetMagicDamage");
			l.pushJavaFunction(SetProjectile);
			l.setField(-2, "SetProjectile");
		}
		l.pop(1);
	}
}
