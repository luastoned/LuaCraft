package com.luacraft.meta;

import com.luacraft.LuaCraftState;
import com.luacraft.LuaUserdata;
import com.luacraft.classes.Vector;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;
import net.minecraft.world.chunk.Chunk;

public class LuaChunk {
    public static JavaFunction __tostring = new JavaFunction() {
        public int invoke(LuaState l) {
            Chunk self = (Chunk) l.checkUserdata(1, Chunk.class, "Chunk");
            l.pushString("Chunk: " + self.toString());
            return 1;
        }
    };

    public static JavaFunction IsLoaded = new JavaFunction() {
        public int invoke(LuaState l) {
            Chunk self = (Chunk) l.checkUserdata(1, Chunk.class, "Chunk");
            l.pushBoolean(self.isLoaded());
            return 1;
        }
    };

    public static JavaFunction GetWorld = new JavaFunction() {
        public int invoke(LuaState l) {
            Chunk self = (Chunk) l.checkUserdata(1, Chunk.class, "Chunk");
            LuaUserdata.PushUserdata(l, self.getWorld());
            return 1;
        }
    };

    public static JavaFunction GetPos = new JavaFunction() {
        public int invoke(LuaState l) {
            Chunk self = (Chunk) l.checkUserdata(1, Chunk.class, "Chunk");
            Vector vec = new Vector(self.x, self.z, 0);
            vec.push(l);
            return 1;
        }
    };

    public static void Init(final LuaCraftState l) {
        l.newMetatable("Chunk");
        {
            l.pushJavaFunction(__tostring);
            l.setField(-2, "__tostring");

            LuaUserdata.SetupBasicMeta(l);
            LuaUserdata.SetupMeta(l, true);

            l.newMetatable("Object");
            l.setField(-2, "__basemeta");

            l.pushJavaFunction(IsLoaded);
            l.setField(-2, "IsLoaded");
            l.pushJavaFunction(GetWorld);
            l.setField(-2, "GetWorld");
            l.pushJavaFunction(GetPos);
            l.setField(-2, "GetPos");
        }
        l.pop(1);
    }
}
