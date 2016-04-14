package com.luacraft;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LuaAddon
{
    public static final String ADDON_INFO_NAME = "addon.info";

    private File root;

    private String name;
    private String author;
    private String version;
    private String mcversion;
    private String resourcename;
    private String info;

    private boolean hasSuccessfullyLoaded = false;

    /**
     * Contains all the mod info about the addon
     * @param addonDir File to the addons root directory
     */
    public LuaAddon(File addonDir) {
        root = addonDir; // No copy constructor? okay
        if(root != null && root.exists() && root.isDirectory()) {
            File infoFile = new File(root, ADDON_INFO_NAME);
            if(infoFile.exists() && infoFile.isFile()) {
                FileReader reader = null;
                try {
                    reader = new FileReader(infoFile);
                    try {
                        JSONTokener tokener = new JSONTokener(reader);
                        JSONObject json = new JSONObject(tokener);
                        name = json.getString("name");
                        author = json.getString("author");
                        version = json.getString("version");
                        mcversion = json.getString("mc-version");
                        resourcename = json.getString("resource-name");
                        info = json.getString("info");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (reader != null) reader.close();
                        hasSuccessfullyLoaded = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Gets a file object of a folder in the addon if it exists
     * @param dir name of the folder
     * @return null if it doesn't exist
     */
    private File getDir(String dir) {
        return new File(root, dir);
    }

    /**
     * Checks to make sure the mod actually had a mod.info file in it
     * @return true if it has a mod.info file in it
     */
    public boolean isValid() {
        return hasSuccessfullyLoaded;
    }

    /**
     * Checks to make sure the mod info file had specified the resource domain
     * @return true if it did
     */
    public boolean isResourceValid() {
        return resourcename != null && getAssetsDir().exists();
    }

    /**
     * Gets the root addon file
     * @return file
     */
    public File getRoot() {
        return root;
    }

    /**
     * Gets the mod info file
     * @return file
     */
    public File getModInfo() {
        return new File(root, ADDON_INFO_NAME);
    }

    /**
     * Gets the lua folder
     * @return a file or null if it didnt exist
     */
    public File getLuaDir() {
        return getDir("lua");
    }

    /**
     * Gets the assets folder
     * @return a file or null if it didnt exist
     */
    public File getAssetsDir() {
        return getDir("assets");
    }

    /**
     * Gets the name of the mod
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the author of the mod
     * @return
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Gets information about the mod
     * @return
     */
    public String getInfo() {
        return info;
    }

    /**
     * Gets the version of Minecraft the mod was made for
     * @return
     */
    public String getMcversion() {
        return mcversion;
    }

    /**
     * Gets the resource domain of the mod
     * @return
     */
    public String getResourcename() {
        return resourcename;
    }

    /**
     * Gets the mods version
     * @return
     */
    public String getVersion() {
        return version;
    }

    public boolean equals(Object object) {
        if(object instanceof LuaAddon) {
            return root.getPath().equals(((LuaAddon) object).getRoot().getPath());
        } else {
            return super.equals(object);
        }
    }
}
