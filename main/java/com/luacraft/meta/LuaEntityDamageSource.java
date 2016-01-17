package com.luacraft.meta;

import net.minecraft.util.EntityDamageSource;

import com.luacraft.LuaCraftState;
import com.luacraft.LuaUserdataManager;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

public class LuaEntityDamageSource {

	public static JavaFunction __tostring = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityDamageSource self = (EntityDamageSource) l.checkUserdata(1, EntityDamageSource.class,
					"EntityDamageSource");
			l.pushString(String.format("EntityDamageSource: 0x%08x", l.toPointer(1)));
			return 1;
		}
	};

	public static JavaFunction GetEntity = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityDamageSource self = (EntityDamageSource) l.checkUserdata(1, EntityDamageSource.class,
					"EntityDamageSource");
			LuaUserdataManager.PushUserdata(l, self.getEntity());
			return 1;
		}
	};

	public static JavaFunction IsDifficultyScaled = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityDamageSource self = (EntityDamageSource) l.checkUserdata(1, EntityDamageSource.class,
					"EntityDamageSource");
			LuaUserdataManager.PushUserdata(l, self.isDifficultyScaled());
			return 1;
		}
	};

	public static void Init(final LuaCraftState l) {
		l.newMetatable("EntityDamageSource");
		{
			l.pushJavaFunction(__tostring);
			l.setField(-2, "__tostring");

			LuaUserdataManager.SetupMetaMethods(l);

			l.newMetatable("DamageSource");
			l.setField(-2, "__basemeta");

			l.pushJavaFunction(GetEntity);
			l.setField(-2, "GetEntity");
			l.pushJavaFunction(IsDifficultyScaled);
			l.setField(-2, "IsDifficultyScaled");
		}
		l.pop(1);
	}
}
