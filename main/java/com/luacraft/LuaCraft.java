package com.luacraft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import com.luacraft.console.ConsoleFrame;
import com.luacraft.console.ConsoleManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.luacraft.classes.LuaJavaChannel;
import com.luacraft.classes.LuaJavaRunCommand;
import com.naef.jnlua.NativeSupport;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = LuaCraft.MODID,
		name = LuaCraft.MODNAME,
		version = LuaCraft.VERSION,
		guiFactory = "com.luacraft.LuaCraftGuiFactory")
public class LuaCraft {
	public static final String MODNAME = "LuaCraft";
	public static final String MODID = "luacraft";
	public static final String VERSION = "1.2";

	public static HashMap<String, LuaJavaChannel> threadChannels = new HashMap<String, LuaJavaChannel>();

	public static String luaDir = "lua" + File.separator;
	public static String rootDir = System.getProperty("user.dir") + File.separator;

	private static Logger luaLogger;
	private static LuaLoader luaLoader = new LuaLoader(rootDir);
	public static HashMap<Side, LuaCraftState> luaStates = new HashMap<Side, LuaCraftState>();

	public static FMLEventChannel channel = null;

	public static Configuration config;
	public static File configFile;

	public static boolean scriptEnforcer = true;
	public static boolean enableDeveloperConsole = true;

	public static FMLClientHandler getForgeClient() {
		return FMLClientHandler.instance();
	}

	public static Minecraft getClient() {
		return getForgeClient().getClient();
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ModContainer modContainer = FMLCommonHandler.instance().findContainerFor(this);
		luaLogger = LogManager.getLogger(modContainer.getName());

		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		loadConfig();
		config.save();

		configFile = config.getConfigFile();

		ConsoleManager.create();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("LuaCraft");

		NativeSupport.getInstance().setLoader(luaLoader);

		if (event.getSide() == Side.CLIENT) {
			LuaClient luaState = new LuaClient();
			synchronized (luaState) {
				luaState.initialize(true);
				luaState.runScripts();
			}
			synchronized (luaStates) {
				luaStates.put(Side.CLIENT, luaState);
			}
		} else {
			LuaServer luaState = new LuaServer();
			synchronized (luaState) {
				luaState.initialize(true);
				luaState.runScripts();
			}
			synchronized (luaStates) {
				luaStates.put(Side.SERVER, luaState);
			}
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		luaLogger.info(rootDir);
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new LuaJavaRunCommand());

		if (event.getSide().isClient() && luaStates.get(Side.SERVER) == null) {
			LuaServer luaState = new LuaServer();
			synchronized (luaState) {
				luaState.setSideOverride(Side.CLIENT); // Singleplayer fix..
				luaState.initialize(true);
				luaState.runScripts();
			}
			synchronized (luaStates) {
				luaStates.put(Side.SERVER, luaState);
			}
		}
	}

	@EventHandler
	public void serverStarted(FMLServerStartedEvent event) {
	}

	@EventHandler
	public void serverStopping(FMLServerStoppingEvent event) {
		LuaCraftState luaState = luaStates.get(Side.SERVER);
		synchronized (luaState) {
			if (event.getSide().isClient() && luaState != null) {
				luaState.close();
				luaStates.remove(Side.SERVER);
			}
		}
	}

	public static void loadConfig()
	{
		scriptEnforcer = config.get(Configuration.CATEGORY_GENERAL, "script-enforcer", true, "Prevent clients from running their own Lua scripts").getBoolean();

		// TODO: Allow toggling
		enableDeveloperConsole = config.get(Configuration.CATEGORY_GENERAL, "developer-console", true, "Console for lua input and output").getBoolean();
	}

	public static Logger getLogger() {
		return luaLogger;
	}

	public static LuaCraftState getLuaState(Side side) {
		return luaStates.get(side);
	}

	public static String getMinecraftDirectory() {
		return rootDir;
	}

	public static String getRootLuaDirectory() {
		return getMinecraftDirectory() + luaDir;
	}

	public static InputStream getPackedFileInputStream(String file) throws FileNotFoundException {
		InputStream in = null;
		if (luaLoader.isEclipse)
			in = new FileInputStream(new File(rootDir, "../src/main/resources/" + file));
		else
			in = LuaCraft.class.getResourceAsStream('/' + file);

		return in;
	}

	public static File extractFile(String strFrom, String strTo) {
		File extractedFile = new File(rootDir, strTo);
		int lastSlash = extractedFile.toString().lastIndexOf(File.separator);
		String filePath = extractedFile.toString().substring(0, lastSlash);

		File fileDirectory = new File(filePath);
		if (!fileDirectory.exists())
			fileDirectory.mkdirs();

		int readBytes;
		byte[] buffer = new byte[1024];

		InputStream fileInStream = null;
		OutputStream fileOutStream = null;

		try {
			fileInStream = getPackedFileInputStream(strFrom);
			fileOutStream = new FileOutputStream(extractedFile);

			while ((readBytes = fileInStream.read(buffer)) != -1)
				fileOutStream.write(buffer, 0, readBytes);
		} catch (Exception e) {
			throw new Error(e.getMessage());
		} finally {
			try {
				fileOutStream.close();
				fileInStream.close();
			} catch (Exception e) {
				// throw new Error(e.getMessage());
				e.printStackTrace();
			}

		}
		return extractedFile;
	}

	public static void reloadClientState() {
		LuaClient state = (LuaClient) getLuaState(Side.CLIENT);

		synchronized (state) {
			state.runScripts();
		}
	}

	public static void reloadServerState() {
		LuaServer state = (LuaServer) getLuaState(Side.SERVER);

		synchronized (state) {
			state.runScripts();
		}
	}

	public static void checkVersion() {
		try {
			URL requestURL = new URL("http://luacraft.com/update/" + VERSION);
			HttpURLConnection httpCon = (HttpURLConnection) requestURL.openConnection();

			httpCon.setRequestMethod("GET");
			httpCon.setRequestProperty("User-Agent", "Minecraft, LuaCraft");

			String inputLine;
			StringBuffer resBuffer = new StringBuffer();
			int responseCode = httpCon.getResponseCode();
			BufferedReader in = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));

			while ((inputLine = in.readLine()) != null)
				resBuffer.append(inputLine);

			in.close();
			String webVersion = resBuffer.toString();
			if (!webVersion.equals(VERSION))
				LuaCraft.getLogger().info("A newer version of LuaCraft (" + webVersion + ") is available!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
