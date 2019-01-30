package com.luacraft.meta.client;

import com.luacraft.LuaCraft;
import com.luacraft.LuaCraftState;
import com.luacraft.classes.Vector;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;

public class LuaRenderLivingBase {
    public static JavaFunction __tostring = new JavaFunction() {
        public int invoke(LuaState l) {
            RenderLivingBase self = (RenderLivingBase) l.checkUserdata(1, RenderLivingBase.class, "RenderLivingBase");
            l.pushString(self.toString());
            return 1;
        }
    };

    public static JavaFunction DrawModel = new JavaFunction() {
        public int invoke(LuaState l) {
            RenderLivingBase self = (RenderLivingBase) l.checkUserdata(1, RenderLivingBase.class, "RenderLivingBase");
            EntityLivingBase entity = (EntityLivingBase)l.checkUserdata(2, EntityLivingBase.class, "EntityLivingBase");
            Vector pos = (Vector)l.checkUserdata(3, Vector.class, "Vector");
            float yaw = (float)l.checkNumber(4);
            self.doRender(entity, pos.x, pos.z, pos.y, yaw, LuaCraft.getClient().getRenderPartialTicks());
            return 1;
        }
    };

    // TODO: GetModel

    public static void Init(LuaCraftState l) {
        l.newMetatable("RenderLivingBase");
        {
            l.pushJavaFunction(__tostring);
            l.setField(-2, "__tostring");

            l.pushJavaFunction(DrawModel);
            l.setField(-2, "DrawModel");
        }
        l.pop(1);
    }
}
