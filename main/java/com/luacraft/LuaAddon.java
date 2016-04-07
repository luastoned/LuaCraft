package com.luacraft;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LuaAddon
{
    private static final String ADDON_INFO_NAME = "addon.info";
    private static final String NULL_STRING = "<null>";

    private File root;

    private String name;
    private String author;
    private String version;
    private String mcversion;
    private String info;

    public LuaAddon(File addonDir) {
        root = addonDir; // No copy constructor? okay
        name = author = version = mcversion = info = NULL_STRING;
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
                        info = json.getString("info");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (reader != null) reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private File getDir(String dir) {
        File assets = new File(root, dir);
        if(assets.exists())
            return assets;
        else
            return null;
    }

    public boolean isValid() {
        return !info.equals(NULL_STRING);
    }

    public File getRoot() {
        return root;
    }

    public File getLuaDir() {
        return getDir("lua");
    }

    public File getAssetsDir() {
        return getDir("assets");
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getInfo() {
        return info;
    }

    public String getMcversion() {
        return mcversion;
    }

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
