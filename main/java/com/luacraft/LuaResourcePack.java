package com.luacraft;

import com.google.common.base.Charsets;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.jar.*;

/**
 * Used for mounting assets to a jar file so that Minecraft can read it
 */
public class LuaResourcePack implements IResourcePack {
    private File jarFile;
    private JarOutputStream stream;
    private TreeMap<String, Object> contents;

    /**
     * For testing
     */
    public static void main(String[] args) {
        File root = new File("C:\\Projects\\LuaCraft\\run");
        LuaResourcePack manager = new LuaResourcePack(new File("C:\\Projects\\LuaCraft\\run\\test.jar"));
        manager.addFolder(new File("C:\\Projects\\LuaCraft\\run\\lua"));
        manager.buildJar();
    }

    /**
     * Used to trick Forge.
     * @param jar Full path to the jar file
     */
    public LuaResourcePack(File jar) {
        jarFile = jar;
        contents = new TreeMap<>();
    }

    /**
     * Opens stream to the jar file
     */
    public void open() {
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        try {
            stream = new JarOutputStream(new FileOutputStream(jarFile), manifest);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the stream (has to be done manually)
     */
    public void close() {
        try {
            if(stream != null) {
                stream.close();
                stream = null;
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks to see if the stream has a non-null handle
     * @return true if not null
     */
    public boolean isOpened() {
        return stream != null;
    }

    /**
     * Add a folder and all the files inside it
     * @param folder File object of the folder
     */
    public TreeMap addFolder(File folder) {
        if(folder.isDirectory()) return addFolder(contents, folder);
        else return null;
    }
    public TreeMap addFolder(TreeMap root, File folder) {
        return addFolder(root, folder, false);
    }
    public TreeMap addFolder(TreeMap root, File folder, boolean ignore) {
        TreeMap node = null;
        if(!ignore) {
            root.put(folder.getName(), node = new TreeMap());
        } else node = root;

        if(folder.exists()) {
            for (File file : folder.listFiles()) {
                if (file.isFile())
                    addFile(node, file);
                else if(file.isDirectory())
                    addFolder(node, file);
            }
        }
        return node;
    }

    /**
     * Add a folder that does not exist to the jar
     * @param folder Name of the folder
     */
    public TreeMap addFolder(String folder) {
        return addFolder(contents, folder);
    }
    public TreeMap addFolder(TreeMap root, String folder) {
        TreeMap ret = null;
        root.put(folder, ret = new TreeMap());
        return ret;
    }

    /**
     * Add a file to the jar
     * @param file the file
     */
    public void addFile(File file) {
        addFile(contents, file);
    }
    public void addFile(TreeMap root, File file) {
        root.put(file.getName(), file);
    }

    /**
     * Add a non-existent file with data
     * @param file Name of the file
     * @param data File data
     */
    public void addFile(String file, StringBuffer data) {
        addFile(contents, file, data);
    }
    public void addFile(TreeMap root, String file, StringBuffer data) {
        root.put(file, data);
    }

    /**
     * Get root tree
     * @return contents
     */
    public TreeMap getRootDirectory() {
        return contents;
    }

    /**
     * Gets a folder in the tree
     * @param name name of the folder
     * @return Tree of the folder
     */
    public TreeMap getFolder(String name) {
        return getFolder(contents, name);
    }
    public TreeMap getFolder(TreeMap<String, Object> root, String name) {
        for(Map.Entry<String, Object> entry : root.entrySet()) {
            String key = (String)entry.getKey();
            Object value = entry.getValue();
            if(key.equals(name) &&
                    value instanceof TreeMap)
                return (TreeMap)value;
        }
        return null;
    }

    /**
     * Gets a file in the tree (but only if its value is a File object)
     * @param name Name of the file
     * @return File object to file
     */
    public File getFile(String name) {
        return getFile(contents, name);
    }
    public File getFile(TreeMap<String, Object> root, String name) {
        for(Map.Entry<String, Object> entry : root.entrySet()) {
            String key = (String)entry.getKey();
            Object value = entry.getValue();
            if(key.equals(name) &&
                    value instanceof File)
                return (File)value;
        }
        return null;
    }

    /**
     * Builds the jar from the TreeMap
     * (no need to open and close the stream, it does it for you)
     */
    public void buildJar() {
        open();
        buildJar(contents, "");
        close();
    }
    public void buildJar(TreeMap<String, Object> root, String rootPath) {
        for(Map.Entry<String, Object> ent : root.entrySet()) {
            String name = (String)ent.getKey();
            Object value = ent.getValue();
            if(value instanceof File) {
                File file = (File)value;
                JarEntry entry = new JarEntry(rootPath + name);
                entry.setTime(file.lastModified());
                try {
                    stream.putNextEntry(entry);
                    stream.write(Files.readAllBytes(file.toPath()));
                    stream.closeEntry();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            } else if(value instanceof StringBuffer) {
                StringBuffer buffer = (StringBuffer)value;
                JarEntry entry = new JarEntry(rootPath + name);
                try {
                    stream.putNextEntry(entry);
                    stream.write(String.valueOf(buffer).getBytes());
                    stream.closeEntry();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            } else if(value instanceof TreeMap) {
                name = name.endsWith("/") ? name : name + "/";
                JarEntry entry = new JarEntry(rootPath + name);
                try {
                    stream.putNextEntry(entry);
                    stream.closeEntry();
                } catch(IOException e) {
                    e.printStackTrace();
                }
                buildJar((TreeMap)value, rootPath + name);
            }
        }
    }

    /**
     * Removes the jar file
     * Call this when the mod is shutting down
     */
    public void delete() {
        if(!jarFile.exists()) return;
        try {
            Files.delete(jarFile.toPath());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the jar file
     * @return the jar
     */
    public File getJarFile() {
        return jarFile;
    }

    /**
     * Creates a pack.mcmeta json
     * @return buffer of the json
     */
    public StringBuffer createMcmetapackData() {
        StringBuffer buffer = null;
        try {
            JSONObject pack = new JSONObject();

            pack.put("pack_format", 2);
            pack.put("description", "Custom lua content for LuaCraft addons");

            JSONObject root = new JSONObject();
            root.put("pack", pack);

            buffer = new StringBuffer(root.toString());
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * Opens an input stream to a file inside the jar
     * @param path path to the file relative to the jar
     * @return input stream to the file
     */
    private InputStream getResourceFromJar(String path) {
        InputStream input = null;
        String url = "jar:file:/" + getJarFile().getPath() + "!/" + path;
        url = url.replace("\\", "/");
        try {
            URL jar = new URL(url);
            try {
                JarURLConnection connection = (JarURLConnection)jar.openConnection();
                if(connection != null) input = connection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch(MalformedURLException e) {
            e.printStackTrace();
        }
        return input;
    }

    private String resourceLocationToPathString(ResourceLocation location) {
        return String.format("assets/%s/%s", location.getResourceDomain(), location.getResourcePath());
    }

    /**
     *  Implemented methods for the IResourcePack below
     */

    @Override
    public InputStream getInputStream(ResourceLocation location) throws IOException {
        return getResourceFromJar(resourceLocationToPathString(location));
    }

    @Override
    public boolean resourceExists(ResourceLocation location) {
        boolean exists = false;
        try {
            JarFile jar = new JarFile(jarFile);
            JarEntry entry = jar.getJarEntry(resourceLocationToPathString(location));
            exists = (entry != null);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return exists;
    }

    @Override
    public Set<String> getResourceDomains() {
        Set<String> domains = new LinkedHashSet<>();
        TreeMap<String, Object> map = getFolder("assets");
        if(map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String name = (String) entry.getKey();
                Object value = entry.getValue();
                if(value instanceof TreeMap)
                    domains.add(name);
            }
        }
        return domains;
    }

    @Override
    public <T extends IMetadataSection> T getPackMetadata(IMetadataSerializer metadataSerializer, String metadataSectionName) throws IOException {
        InputStream input = getResourceFromJar("pack.mcmeta");
        JsonObject json = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(input, Charsets.UTF_8));
            json = (new JsonParser()).parse(reader).getAsJsonObject();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            if(reader != null) reader.close();
        }
        return metadataSerializer.parseMetadataSection(metadataSectionName, json);
    }

    @Override
    public BufferedImage getPackImage() throws IOException {
        return null; // no
    }

    @Override
    public String getPackName() {
        return "LuaCraftResources";
    }
}
