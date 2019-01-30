package com.luacraft.meta;

import com.luacraft.LuaCraftState;
import com.luacraft.LuaUserdata;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;

public class LuaDamageSource {

	public static JavaFunction __tostring = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			l.pushString(String.format("DamageSource: 0x%08x", l.toPointer(1)));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetDamageType
	 * @info Return the name of the damage type
	 * @arguments nil
	 * @return [[String]]:type
	 */

	public static JavaFunction GetDamageType = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			l.pushString(self.getDamageType());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetImmediateSource
	 * @info Retrieves the immediate causer of the damage, e.g. the arrow entity, not its shooter
	 * @arguments nil
	 * @return [[Entity]]:target
	 */

	public static JavaFunction GetImmediateSource = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityDamageSource self = (EntityDamageSource) l.checkUserdata(1, EntityDamageSource.class,
					"EntityDamageSource");
			LuaUserdata.PushUserdata(l, self.getImmediateSource());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetTrueSource
	 * @info Retrieves the true causer of the damage, e.g. the player who fired an arrow, the shulker who fired the bullet, etc.
	 * @arguments nil
	 * @return [[Entity]]:source
	 */

	public static JavaFunction GetTrueSource = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityDamageSource self = (EntityDamageSource) l.checkUserdata(1, EntityDamageSource.class,
					"EntityDamageSource");
			LuaUserdata.PushUserdata(l, self.getTrueSource());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetHungerDamage
	 * @info How much food is consumed by this DamageSource
	 * @arguments nil
	 * @return [[Number]]:damage
	 */

	public static JavaFunction GetHungerDamage = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			l.pushNumber(self.getHungerDamage());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetSource
	 * @info Return the [[Entity]] that caused the damage
	 * @arguments nil
	 * @return [[Entity]]:source
	 */

	public static JavaFunction GetSource = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			LuaUserdata.PushUserdata(l, self.getTrueSource());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function IsAbsolute
	 * @info Whether or not the damage ignores modification by potion effects or enchantments.
	 * @arguments nil
	 * @return [[Boolean]]:absolute
	 */

	public static JavaFunction IsAbsolute = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			l.pushBoolean(self.isDamageAbsolute());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function IsDifficultyScaled
	 * @info Return whether this damage source will have its damage amount scaled based on the current difficulty.
	 * @arguments nil
	 * @return [[Boolean]]:scaled
	 */

	public static JavaFunction IsDifficultyScaled = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			l.pushBoolean(self.isDifficultyScaled());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function IsExplosion
	 * @info Return whether this damage source was caused by an explosion.
	 * @arguments nil
	 * @return [[Boolean]]:explosion
	 */

	public static JavaFunction IsExplosion = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			l.pushBoolean(self.isExplosion());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function IsFireDamage
	 * @info Returns true if the damage is fire based.
	 * @arguments nil
	 * @return [[Boolean]]:fire
	 */

	public static JavaFunction IsFireDamage = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			l.pushBoolean(self.isFireDamage());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function IsMagicDamage
	 * @info Returns true if the damage is magic based.
	 * @arguments nil
	 * @return [[Boolean]]:magic
	 */

	public static JavaFunction IsMagicDamage = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			l.pushBoolean(self.isMagicDamage());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function IsMagicDamage
	 * @info Returns true if the damage is projectile based.
	 * @arguments nil
	 * @return [[Boolean]]:projectile
	 */

	public static JavaFunction IsProjectile = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			l.pushBoolean(self.isProjectile());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function IsUnblockable
	 * @info Returns true if the damage is unblockable.
	 * @arguments nil
	 * @return [[Boolean]]:unblockable
	 */

	public static JavaFunction IsUnblockable = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			l.pushBoolean(self.isUnblockable());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function SetAllowedInCreative
	 * @info Sets this damage source to allow damage in creative mode
	 * @arguments nil
	 * @return [[DamageSource]]:this
	 */

	public static JavaFunction SetAllowedInCreative = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			LuaUserdata.PushUserdata(l, self.setDamageAllowedInCreativeMode());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function SetBypassesArmor
	 * @info Sets this damage source to bypass protective armor
	 * @arguments nil
	 * @return [[DamageSource]]:this
	 */

	public static JavaFunction SetBypassesArmor = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			LuaUserdata.PushUserdata(l, self.setDamageBypassesArmor());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function SetIsAbsolute
	 * @info Sets a value indicating whether the damage is absolute (ignores modification by potion effects or enchantments), and also clears out hunger damage.
	 * @arguments nil
	 * @return [[DamageSource]]:this
	 */

	public static JavaFunction SetIsAbsolute = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			LuaUserdata.PushUserdata(l, self.setDamageIsAbsolute());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function SetDifficultyScaled
	 * @info Set whether this damage source will have its damage amount scaled based on the current difficulty.
	 * @arguments nil
	 * @return [[DamageSource]]:this
	 */

	public static JavaFunction SetDifficultyScaled = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			LuaUserdata.PushUserdata(l, self.setDifficultyScaled());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function SetExplosion
	 * @info Set this damage source to deal explosion damage.
	 * @arguments nil
	 * @return [[DamageSource]]:this
	 */

	public static JavaFunction SetExplosion = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			LuaUserdata.PushUserdata(l, self.setExplosion());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function SetFireDamage
	 * @info Set this damage source to deal fire damage.
	 * @arguments nil
	 * @return [[DamageSource]]:this
	 */

	public static JavaFunction SetFireDamage = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			LuaUserdata.PushUserdata(l, self.setFireDamage());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function SetMagicDamage
	 * @info Set this damage source to deal magic damage.
	 * @arguments nil
	 * @return [[DamageSource]]:this
	 */

	public static JavaFunction SetMagicDamage = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			LuaUserdata.PushUserdata(l, self.setMagicDamage());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function SetProjectile
	 * @info Set this damage source to deal projectile damage.
	 * @arguments nil
	 * @return [[DamageSource]]:this
	 */

	public static JavaFunction SetProjectile = new JavaFunction() {
		public int invoke(LuaState l) {
			DamageSource self = (DamageSource) l.checkUserdata(1, DamageSource.class, "DamageSource");
			LuaUserdata.PushUserdata(l, self.setProjectile());
			return 1;
		}
	};

	public static void Init(final LuaCraftState l) {
		l.newMetatable("DamageSource");
		{
			l.pushJavaFunction(__tostring);
			l.setField(-2, "__tostring");

			LuaUserdata.SetupBasicMeta(l);
			LuaUserdata.SetupMeta(l, false);

			l.newMetatable("Object");
			l.setField(-2, "__basemeta");

			l.pushJavaFunction(GetDamageType);
			l.setField(-2, "GetDamageType");
			l.pushJavaFunction(GetImmediateSource);
			l.setField(-2, "GetImmediateSource");
			l.pushJavaFunction(GetTrueSource);
			l.setField(-2, "GetTrueSource");
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
