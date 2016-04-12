package com.luacraft;

import com.luacraft.classes.FileMount;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * This will find all custom assets and mount them to a dummy LuaCraft
 */
public class LuaResourcePackLoader {
    private static final Map<String,LuaResourcePack> resources = new HashMap<>();
    private static final File jarFolder = FileMount.GetFileInRoot("jars");

    public static void initialize() {
        LuaResourcePack custom = register("luacraft-resources");
        // Clean up the previous jar
        custom.delete();

        // Create meta info pack
        custom.addFile("pack.mcmeta", custom.createMcmetapackData());

        TreeMap root = custom.addFolder("assets");

        // Load assets under the root (probably should not use the regular root)
        custom.addFolder(
                custom.addFolder(root, LuaCraft.DEFAULT_RESOURCEPACK),
                FileMount.GetFileInRoot("assets"), true);

        // Load addon assets
        for(LuaAddon addon : LuaAddonManager.getAddons()) {
            if(addon.isResourceValid())
                custom.addFolder(
                        custom.addFolder(root, addon.getName()),
                        addon.getAssetsDir(), true);
        }

        // Create the jar
        custom.buildJar();

        // fuck u mojang make accessors for your class members
        LuaCraft.getClient().defaultResourcePacks.add(custom);
    }

    public static LuaResourcePack register(String name) {
        if(!resources.containsKey(name)) {
            LuaResourcePack resource;
            resources.put(name, resource = new LuaResourcePack(new File(jarFolder, name + ".jar")));
            return resource;
        }
        return resources.get(name);
    }
}
