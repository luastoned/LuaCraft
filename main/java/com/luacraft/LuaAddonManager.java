package com.luacraft;

import com.luacraft.classes.FileMount;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// TODO: Add file watcher in addons folder
// TODO: Add addon list to config GUI somehow
// TODO: Find a way to hax assets into the main luacraft jar to trick the resource locator

public class LuaAddonManager
{
    private static final String addonDir = "addons" + File.separator;
    private static final List<LuaAddon> addons = new ArrayList<>();

    /**
     * Get all addons under the addons folder
     */
    public static void initialize() {
        File root = FileMount.GetFileInMountedRoot(addonDir);
        for (File file : root.listFiles()) {
            LuaAddon addon = new LuaAddon(file);
            if(addon.isValid()) register(addon);
        }
    }

    /**
     * Register an addon
     * @param addon addon object
     */
    public static void register(LuaAddon addon) {
        if(!addons.contains(addon) && addon.isValid()) {
            addons.add(addon);
            FileMount.MountDirectory(addonDir + addon.getRoot().getName());
        }
    }
}
