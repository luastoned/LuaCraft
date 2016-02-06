package com.luacraft.meta;

import java.util.ArrayList;
import java.util.List;

import com.luacraft.LuaCraftState;
import com.luacraft.LuaUserdata;
import com.luacraft.classes.Vector;
import com.luacraft.library.LuaLibUtil;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

public class LuaLivingBase {
	/**
	 * @author Jake
	 * @function GetMaxHealth
	 * @info Return the entity max health
	 * @arguments nil
	 * @return [[Number]]:health
	 */

	public static JavaFunction GetMaxHealth = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityLivingBase self = (EntityLivingBase) l.checkUserdata(1, EntityLivingBase.class, "Living");
			l.pushNumber(self.getMaxHealth());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetHealth
	 * @info Return the entity health
	 * @arguments nil
	 * @return [[Number]]:health
	 */

	public static JavaFunction GetHealth = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityLivingBase self = (EntityLivingBase) l.checkUserdata(1, EntityLivingBase.class, "Living");
			l.pushNumber(self.getHealth());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function SetHealth
	 * @info Set the entity health (reaches from 0 to 20)
	 * @arguments [[Number]]:health
	 * @return nil
	 */

	public static JavaFunction SetHealth = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityLivingBase self = (EntityLivingBase) l.checkUserdata(1, EntityLivingBase.class, "Living");
			float health = (float) l.checkNumber(2);

			if (health <= 0)
				self.attackEntityFrom(DamageSource.generic, self.getHealth());

			self.setHealth(health);
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function Kill
	 * @info Kill the entity
	 * @arguments nil
	 * @return nil
	 */

	public static JavaFunction Kill = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityLivingBase self = (EntityLivingBase) l.checkUserdata(1, EntityLivingBase.class, "Living");
			self.attackEntityFrom(DamageSource.generic, self.getHealth());
			return 0;
		}
	};

	/**
	 * @author Gregor
	 * @function IsOnLadder
	 * @info Check if the entity is on a ladder
	 * @arguments nil
	 * @return [[Boolean]]:onladder
	 */

	public static JavaFunction IsOnLadder = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityLivingBase self = (EntityLivingBase) l.checkUserdata(1, EntityLivingBase.class, "Living");
			l.pushBoolean(self.isOnLadder());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function CanBreatheWater
	 * @info Get if the entity can breathe in water
	 * @arguments nil
	 * @return [[Boolean]]:waterbreath
	 */

	public static JavaFunction CanBreatheWater = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityLivingBase self = (EntityLivingBase) l.checkUserdata(1, EntityLivingBase.class, "Living");
			l.pushBoolean(self.canBreatheUnderwater());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetAimVector
	 * @info Returns the aim vector of the direction the entity is looking
	 * @arguments nil
	 * @return [[Vector]]:vec
	 */

	public static JavaFunction GetAimVector = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityLivingBase self = (EntityLivingBase) l.checkUserdata(1, EntityLivingBase.class, "Living");
			Vector dir = new Vector(self.getLook(1F));
			dir.push(l);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetAge
	 * @info Return the entitys age
	 * @arguments nil
	 * @return [[Number]]:age
	 */

	public static JavaFunction GetAge = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityLivingBase self = (EntityLivingBase) l.checkUserdata(1, EntityLivingBase.class, "Living");
			l.pushInteger(self.getAge());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetEyeTrace
	 * @info Returns a trace table using the players eye position and angles
	 * @arguments nil
	 * @return [[Table]]:trace
	 */

	public static JavaFunction GetEyeTrace = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityLivingBase self = (EntityLivingBase) l.checkUserdata(1, EntityLivingBase.class, "Living");

			Vector start = new Vector(self.posX, self.posZ, self.posY + self.getEyeHeight());
			Vector dir = new Vector(self.getLook(1F));
			Vector endpos = start.copy().add(dir.mul(160));

			List<Entity> filter = new ArrayList<Entity>();
			filter.add(self);

			LuaLibUtil.pushTrace((LuaCraftState) l, self.worldObj, start, endpos, true, filter);
			return 1;
		}
	};

	public static void Init(LuaState l) {
		l.newMetatable("LivingBase");
		{
			l.pushJavaFunction(LuaEntity.__tostring);
			l.setField(-2, "__tostring");

			LuaUserdata.SetupBasicMeta(l);
			LuaUserdata.SetupMeta(l, true);

			l.newMetatable("Entity");
			l.setField(-2, "__basemeta");

			l.pushJavaFunction(GetMaxHealth);
			l.setField(-2, "GetMaxHealth");
			l.pushJavaFunction(GetHealth);
			l.setField(-2, "GetHealth");
			l.pushJavaFunction(SetHealth);
			l.setField(-2, "SetHealth");
			l.pushJavaFunction(Kill);
			l.setField(-2, "Kill");
			l.pushJavaFunction(IsOnLadder);
			l.setField(-2, "IsOnLadder");
			l.pushJavaFunction(CanBreatheWater);
			l.setField(-2, "CanBreatheWater");
			l.pushJavaFunction(GetAimVector);
			l.setField(-2, "GetAimVector");
			l.pushJavaFunction(GetAge);
			l.setField(-2, "GetAge");
			l.pushJavaFunction(GetEyeTrace);
			l.setField(-2, "GetEyeTrace");
		}
		l.pop(1);
	}

}
