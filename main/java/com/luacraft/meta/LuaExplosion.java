package com.luacraft.meta;

import com.luacraft.LuaCraftState;
import com.luacraft.LuaUserdata;
import com.luacraft.classes.Vector;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;

import java.util.Map;

public class LuaExplosion {
    public static JavaFunction __tostring = new JavaFunction() {
        public int invoke(LuaState l) {
            Explosion self = (Explosion) l.checkUserdata(1, Explosion.class, "Explosion");
            l.pushString(String.format("Explosion [%.2f, %.2f, %.2f]",
                    self.getPosition().x,
                    self.getPosition().z,
                    self.getPosition().y));
            return 1;
        }
    };

    public static JavaFunction Explode = new JavaFunction() {
        public int invoke(LuaState l) {
            Explosion self = (Explosion) l.checkUserdata(1, Explosion.class, "Explosion");
            boolean spawnParticles = l.checkBoolean(2, true);
            self.doExplosionA();
            self.doExplosionB(spawnParticles);
            return 0;
        }
    };

    public static JavaFunction GetEntityPlacedBy = new JavaFunction() {
        public int invoke(LuaState l) {
            Explosion self = (Explosion) l.checkUserdata(1, Explosion.class, "Explosion");
            LuaUserdata.PushUserdata(l, self.getExplosivePlacedBy());
            return 1;
        }
    };

    public static JavaFunction GetKnockedBackedPlayers = new JavaFunction() {
        public int invoke(LuaState l) {
            Explosion self = (Explosion) l.checkUserdata(1, Explosion.class, "Explosion");
            l.newTable();
            {
                for(Map.Entry<EntityPlayer, Vec3d> entry : self.getPlayerKnockbackMap().entrySet()) {
                    LuaUserdata.PushUserdata(l, entry.getKey());
                    Vector vec = new Vector(entry.getValue().x, entry.getValue().z, entry.getValue().y);
                    vec.push(l);
                    l.setTable(-3);
                }
            }
            return 1;
        }
    };

    public static JavaFunction GetAffectedBlocks = new JavaFunction() {
        public int invoke(LuaState l) {
            Explosion self = (Explosion) l.checkUserdata(1, Explosion.class, "Explosion");
            l.newTable();
            {
                for(int i = 0; i < self.getAffectedBlockPositions().size(); i++) {
                    l.pushInteger(i + 1);
                    BlockPos pos = self.getAffectedBlockPositions().get(i);
                    Vector vec = new Vector(pos.getX(), pos.getZ(), pos.getY());
                    vec.push(l);
                    l.setTable(-3);
                }
            }
            return 1;
        }
    };

    public static JavaFunction GetPos = new JavaFunction() {
        public int invoke(LuaState l) {
            Explosion self = (Explosion) l.checkUserdata(1, Explosion.class, "Explosion");
            Vector vec = new Vector(self.getPosition().x, self.getPosition().z, self.getPosition().y);
            vec.push(l);
            return 1;
        }
    };

    public static void Init(final LuaCraftState l) {
        l.newMetatable("Explosion");
        {
            l.pushJavaFunction(__tostring);
            l.setField(-2, "__tostring");

            LuaUserdata.SetupBasicMeta(l);
            LuaUserdata.SetupMeta(l, true);

            l.newMetatable("Object");
            l.setField(-2, "__basemeta");

            l.pushJavaFunction(Explode);
            l.setField(-2, "Explode");
            l.pushJavaFunction(GetEntityPlacedBy);
            l.setField(-2, "GetEntityPlacedBy");
            l.pushJavaFunction(GetKnockedBackedPlayers);
            l.setField(-2, "GetKnockedBackedPlayers");
            l.pushJavaFunction(GetAffectedBlocks);
            l.setField(-2, "GetAffectedBlocks");
            l.pushJavaFunction(GetPos);
            l.setField(-2, "GetPos");
        }
        l.pop(1);
    }
}
