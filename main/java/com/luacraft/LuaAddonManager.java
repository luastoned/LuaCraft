package com.luacraft;

import com.luacraft.classes.FileMount;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LuaAddonManager
{
    private static final String ADDONS_DIR = "addons" + File.separator;
    private static final Map<File, LuaAddon> addons = new HashMap<>();

    /**
     * Get all addons under the addons folder
     */
    public static void initialize() {
        File root = FileMount.GetFileInRoot(ADDONS_DIR);
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
        if(!addons.containsKey(addon.getModInfo()) && addon.isValid()) {
            addons.put(addon.getModInfo(), addon);
            FileMount.MountDirectory(ADDONS_DIR + addon.getRoot().getName());
        }
    }

    /**
     * Gets addon from map
     * @param file A folder
     * @return LuaAddon object
     */
    public static LuaAddon getAddonFromFile(File file) {
        File modInfoFile = locateModInfoFile(file.getParentFile());
        if(modInfoFile == null) return null;
        return addons.get(modInfoFile);
    }

    /**
     * Returns list of addons
     * @return Array of addons
     */
    public static List<LuaAddon> getAddons() {
        List<LuaAddon> list = new ArrayList<>();
        for(Map.Entry<File, LuaAddon> entry : addons.entrySet())
            list.add(entry.getValue());
        return list;
    }

    /**
     * Traverses a file backwards until it finds the mod info file
     * @param file File to search
     * @return null if none is found
     */
    private static File locateModInfoFile(File file) {
        if(file.isDirectory() && file.exists() && !file.equals(FileMount.GetRoot())) {
            File modInfo = new File(file, LuaAddon.ADDON_INFO_NAME);
            if(modInfo.exists())
                return modInfo;
            return locateModInfoFile(file.getParentFile());
        }
        return null;
    }
}
