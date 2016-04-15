package com.luacraft;

import java.io.File;

/**
 * Made this class so there wasn't a giant cluster in the LuaCraftState file
 */
public class LuaScriptLoader {
    public static void loadItemScript(LuaCraftState l, File luaFile) {
        LuaAddon addon = LuaAddonManager.getAddonFromFile(luaFile);

        // Thanks k0bra
        // table = {}
        l.newTable();
        {
            // ITEM = {}
            l.pushString("ITEM");
                l.newTable();
            l.setTable(-3);

            // meta = {}
            l.newTable();
            {
                // __index = _G
                l.pushString("__index");
                    l.getGlobal("_G");
                l.setTable(-3);
                // __newindex = _G
                l.pushString("__newindex");
                    l.getGlobal("_G");
                l.setTable(-3);
            }
            // setmetatable(table, meta)
            l.setMetatable(-2);

            // load file
            l.includeFile(luaFile, false);
            // push a copy of the table so that I have a copy when its popped by setfenv
            l.pushValue(-2);
            // file env = table
            l.setFEnv(-2);
            // run the script
            l.call(0, 0);

            // push item.Register function onto stack
            l.getGlobal("item");
            l.getField(-1, "Register");

            // arg 1 = ITEM
            l.getField(-3, "ITEM");
            // arg 2 = resource name
            if(addon != null)
                l.pushString(addon.getResourcename());
            else
                l.pushString(LuaCraft.DEFAULT_RESOURCEPACK);
            // call item.Register
            l.call(2, 0);
        }
        // clear stack
        l.pop(2);
    }
}
