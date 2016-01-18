package com.luacraft.meta;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

import com.luacraft.LuaCraftState;
import com.luacraft.LuaUserdataManager;
import com.luacraft.classes.Vector;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

public class LuaLiving {
	/**
	 * @author Gregor
	 * @function LookAt
	 * @info Forces the entity to look at the vector
	 * @arguments [[Entity]]:ent, [ [[Number]]:yaw speed, [[Number]]:pitch speed
	 *            ]
	 * @return nil
	 */

	public static JavaFunction LookAt = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityLiving self = (EntityLiving) l.checkUserdata(1, EntityLiving.class, "Living");
			Entity other = (Entity) l.checkUserdata(1, Entity.class, "Entity");
			self.faceEntity(other, (float) l.checkNumber(2, 10), (float) l.checkNumber(3, 40));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function GetExperienceValue
	 * @info Returns how much the entity is worth, experience wise
	 * @arguments nil
	 * @return [[Number]]:exp
	 */

	public static JavaFunction GetExperienceValue = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityLiving self = (EntityLiving) l.checkUserdata(1, EntityLiving.class, "Living");
			l.pushNumber(self.experienceValue);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function SetExperienceValue
	 * @info Set how much the entity is worth, experience wise
	 * @arguments [[Number]]:exp
	 * @return nil
	 */

	public static JavaFunction SetExperienceValue = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityLiving self = (EntityLiving) l.checkUserdata(1, EntityLiving.class, "Living");
			self.experienceValue = l.checkInteger(2);
			return 0;
		}
	};

	/**
	 * @author Gregor
	 * @function GetAttackTarget
	 * @info Check if the entity is on a ladder
	 * @arguments nil
	 * @return [[Entity]]:target
	 */

	public static JavaFunction GetAttackTarget = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityLiving self = (EntityLiving) l.checkUserdata(1, EntityLiving.class, "Living");
			LuaUserdataManager.PushUserdata(l, self.getAttackTarget());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function SetAttackTarget
	 * @info Set the target entity to attack
	 * @arguments [[Living]]:target
	 * @return nil
	 */

	public static JavaFunction SetAttackTarget = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityLiving self = (EntityLiving) l.checkUserdata(1, EntityLiving.class, "Living");
			EntityLiving other = (EntityLiving) l.checkUserdata(2, Entity.class, "Living");
			self.setAttackTarget(other);
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function GetAvoidSun
	 * @broken
	 * @info Return if this NPC keeps away from sunlight
	 * @arguments nil
	 * @return [[Boolean]]:avoid sun
	 */

	public static JavaFunction GetAvoidSun = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityLiving self = (EntityLiving) l.checkUserdata(1, EntityLiving.class, "Living");
			// l.pushBoolean(self.shouldAvoidSun);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function SetAge
	 * @broken
	 * @info Set the entitys age
	 * @arguments [[Number]]:age
	 * @return nil
	 */

	public static JavaFunction SetAvoidSun = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityLiving self = (EntityLiving) l.checkUserdata(1, EntityLiving.class, "Living");
			// self.shouldAvoidSun = l.checkBoolean(2);
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function GetAvoidWater
	 * @broken
	 * @info Return if this NPC keeps away from water
	 * @arguments nil
	 * @return [[Boolean]]:avoid water
	 */

	public static JavaFunction GetAvoidWater = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityLiving self = (EntityLiving) l.checkUserdata(1, EntityLiving.class, "Living");
			// l.pushBoolean(self.getNavigator().getAvoidsWater());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function SetAvoidWater
	 * @broken keep away from water
	 * @arguments [[Boolean]]: state
	 * @return nil
	 */

	public static JavaFunction SetAvoidWater = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityLiving self = (EntityLiving) l.checkUserdata(1, EntityLiving.class, "Living");
			// self.getNavigator().setAvoidsWater(l.checkBoolean(2));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function GetBreakDoors
	 * @broken
	 * @info Return if this NPC can break down doors
	 * @arguments nil
	 * @return [[Boolean]]:door breaking
	 */

	public static JavaFunction GetBreakDoors = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityLiving self = (EntityLiving) l.checkUserdata(1, EntityLiving.class, "Living");
			// l.pushBoolean(self.getNavigator().getCanBreakDoors());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function SetBreakDoors
	 * @broken
	 * @info Set if this NPC should break down doors
	 * @arguments [[Boolean]]: state
	 * @return nil
	 */

	public static JavaFunction SetBreakDoors = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityLiving self = (EntityLiving) l.checkUserdata(1, EntityLiving.class, "Living");
			// self.getNavigator().setBreakDoors(l.checkBoolean(2));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function SetCanSwim
	 * @broken
	 * @info Set if this NPC can swim
	 * @arguments [[Boolean]]: state
	 * @return nil
	 */

	public static JavaFunction SetCanSwim = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityLiving self = (EntityLiving) l.checkUserdata(1, EntityLiving.class, "Living");
			// self.getNavigator().setCanSwim(l.checkBoolean(2));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function GetCanSwim
	 * @broken
	 * @info Return if this NPC can swim
	 * @arguments nil
	 * @return [[Boolean]]:swimming
	 */

	public static JavaFunction GetCanSwim = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityLiving self = (EntityLiving) l.checkUserdata(1, EntityLiving.class, "Living");
			// l.pushBoolean(self.getNavigator().canSwim);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function SetEnterDoors
	 * @broken
	 * @info Set if this NPC can enter doors
	 * @arguments [[Boolean]]: state
	 * @return nil
	 */

	public static JavaFunction SetEnterDoors = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityLiving self = (EntityLiving) l.checkUserdata(1, EntityLiving.class, "Living");
			// self.getNavigator().setEnterDoors(l.checkBoolean(2));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function GetMovementSpeed
	 * @broken
	 * @info Return how fast this NPC can move
	 * @arguments nil
	 * @return [[Number]]: speed
	 */

	public static JavaFunction GetEnterDoors = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityLiving self = (EntityLiving) l.checkUserdata(1, EntityLiving.class, "Living");
			// l.pushBoolean(self.getNavigator().canPassOpenWoodenDoors);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetMovementSpeed
	 * @info Return how fast this NPC can move
	 * @arguments nil
	 * @return [[Number]]: speed
	 */

	public static JavaFunction GetMovementSpeed = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityLiving self = (EntityLiving) l.checkUserdata(1, EntityLiving.class, "Living");
			l.pushNumber(self.getNavigator().speed);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function SetMovementSpeed
	 * @info Set how fast this NPC will move
	 * @arguments [[Number]]: speed
	 * @return nil
	 */

	public static JavaFunction SetMovementSpeed = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityLiving self = (EntityLiving) l.checkUserdata(1, EntityLiving.class, "Living");
			self.getNavigator().setSpeed(l.checkNumber(2));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function MoveTo
	 * @info Set a location this NPC should try to move to
	 * @arguments [[Boolean]]: state
	 * @return nil
	 */

	public static JavaFunction MoveTo = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityLiving self = (EntityLiving) l.checkUserdata(1, EntityLiving.class, "Living");
			Vector pos = (Vector) l.checkUserdata(1, Vector.class, "Vector");
			self.getNavigator().tryMoveToXYZ(pos.x, pos.z, pos.y, self.getNavigator().speed);
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function MoveToEntity
	 * @info Set an entity this NPC should try to move to
	 * @arguments [[Boolean]]: state
	 * @return nil
	 */

	public static JavaFunction MoveToEntity = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityLiving self = (EntityLiving) l.checkUserdata(1, EntityLiving.class, "Living");
			EntityLiving other = (EntityLiving) l.checkUserdata(2, Entity.class, "Living");
			self.getNavigator().tryMoveToEntityLiving(other, self.getNavigator().speed);
			return 0;
		}
	};

	public static void Init(final LuaCraftState l) {
		l.newMetatable("Living");
		{
			l.pushJavaFunction(LuaEntity.__tostring);
			l.setField(-2, "__tostring");

			LuaUserdataManager.SetupMetaMethods(l);

			l.newMetatable("Entity");
			l.setField(-2, "__basemeta");

			l.pushJavaFunction(LookAt);
			l.setField(-2, "LookAt");
			l.pushJavaFunction(GetExperienceValue);
			l.setField(-2, "GetExperienceValue");
			l.pushJavaFunction(SetExperienceValue);
			l.setField(-2, "SetExperienceValue");
			l.pushJavaFunction(GetAttackTarget);
			l.setField(-2, "GetAttackTarget");
			l.pushJavaFunction(SetAttackTarget);
			l.setField(-2, "SetAttackTarget");
			l.pushJavaFunction(GetAvoidSun);
			l.setField(-2, "GetAvoidSun");
			l.pushJavaFunction(SetAvoidSun);
			l.setField(-2, "SetAvoidSun");
			l.pushJavaFunction(GetAvoidWater);
			l.setField(-2, "GetAvoidWater");
			l.pushJavaFunction(SetAvoidWater);
			l.setField(-2, "SetAvoidWater");
			l.pushJavaFunction(GetBreakDoors);
			l.setField(-2, "GetBreakDoors");
			l.pushJavaFunction(SetBreakDoors);
			l.setField(-2, "SetBreakDoors");
			l.pushJavaFunction(GetCanSwim);
			l.setField(-2, "GetCanSwim");
			l.pushJavaFunction(SetCanSwim);
			l.setField(-2, "SetCanSwim");
			l.pushJavaFunction(GetEnterDoors);
			l.setField(-2, "GetEnterDoors");
			l.pushJavaFunction(SetEnterDoors);
			l.setField(-2, "SetEnterDoors");
			l.pushJavaFunction(MoveTo);
			l.setField(-2, "MoveTo");
			l.pushJavaFunction(MoveToEntity);
			l.setField(-2, "MoveToEntity");
		}
		l.pop(1);
	}
}
