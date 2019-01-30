package com.luacraft.meta.client;

import java.util.ArrayList;
import java.util.List;

import com.luacraft.LuaCraft;
import com.luacraft.LuaCraftState;
import com.luacraft.classes.Vector;
import com.luacraft.library.LuaLibUtil;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class LuaLivingBase {
	private static Minecraft client = LuaCraft.getClient();

	public static JavaFunction GetAimVector = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityLivingBase self = (EntityLivingBase) l.checkUserdata(1, EntityLivingBase.class, "Living");
			Vector dir = new Vector(self.getLook(client.timer.renderPartialTicks));
			dir.push(l);
			return 1;
		}
	};

	public static JavaFunction GetEyeTrace = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityLivingBase self = (EntityLivingBase) l.checkUserdata(1, EntityLivingBase.class, "Living");

			double posX = self.lastTickPosX + (self.posX - self.lastTickPosX) * client.timer.renderPartialTicks;
			double posY = self.lastTickPosY + (self.posY - self.lastTickPosY) * client.timer.renderPartialTicks;
			double posZ = self.lastTickPosZ + (self.posZ - self.lastTickPosZ) * client.timer.renderPartialTicks;

			Vector start = new Vector(posX, posZ, posY + self.getEyeHeight());
			Vector dir = new Vector(self.getLook(client.timer.renderPartialTicks));

			Vector endpos = start.copy().add(dir.mul(160));

			List<Entity> filter = new ArrayList<Entity>();
			filter.add(self);

			LuaLibUtil.pushTrace((LuaCraftState) l, self.world, start, endpos, true, filter);
			return 1;
		}
	};

	public static void Init(LuaCraftState l) {
		l.newMetatable("LivingBase");
		{
			l.pushJavaFunction(GetAimVector);
			l.setField(-2, "GetAimVector");
			l.pushJavaFunction(GetEyeTrace);
			l.setField(-2, "GetEyeTrace");
		}
		l.pop(1);
	}
}
