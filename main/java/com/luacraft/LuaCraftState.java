package com.luacraft;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;

import com.luacraft.classes.FileMount;
import com.luacraft.classes.Vector;
import com.luacraft.console.ConsoleManager;
import com.naef.jnlua.LuaRuntimeException;
import com.naef.jnlua.LuaStackTraceElement;
import com.naef.jnlua.LuaState;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.server.FMLServerHandler;
import org.lwjgl.Sys;

public class LuaCraftState extends LuaState implements ILuaReloader {
	private boolean scriptEnforcer = false;
	private Side sideOverride = null;

	protected LuaReloader reloader;

	public void setupReloader()
	{
		reloader = new LuaReloader(this);
	}

	public void close()
	{
		if(reloader != null) reloader.shutdown();
		super.close();
	}

	public void setSideOverride(Side side) {
		sideOverride = side;
	}

	public Side getSideOverride() {
		return sideOverride;
	}

	public Side getSide() {
		if (sideOverride != null)
			return sideOverride;

		return getActualSide();
	}

	public Side getActualSide() {
		return FMLCommonHandler.instance().getEffectiveSide();
	}

	public FMLClientHandler getForgeClient() {
		return FMLClientHandler.instance();
	}

	public FMLServerHandler getForgeServer() {
		return FMLServerHandler.instance();
	}

	public Minecraft getMinecraft() {
		return getForgeClient().getClient();
	}

	public MinecraftServer getServer() {
		if (getSide().isClient())
			return getForgeClient().getServer();
		else
			return getForgeServer().getServer();
	}

	public void downloadLuaFile(String filename, byte[] data) {

	}

	public void getLoadedAddons() {
		// TODO: Addon list
	}

	public void print(String str) {
		LuaCraft.getLogger().info(str);
		ConsoleManager.get(getSide()).onPrint(str);
	}

	public void error(String str) {
		LuaCraft.getLogger().error(str);
		ConsoleManager.get(getSide()).onError(str);
	}

	public void info(String str) {
		LuaCraft.getLogger().info(str);
		ConsoleManager.get(getSide()).onInfo(str);
	}

	public void warning(String str) {
		LuaCraft.getLogger().warn(str);
		ConsoleManager.get(getSide()).onWarning(str);
	}

	/**
	 * @author Jake
	 * @function lua.error
	 * @info Calls whenever a Lua error occurs
	 * @arguments [[String]]:error, [[Table]]:trace
	 * @return [[Boolean]]:print
	 */

	public void handleLuaError(LuaRuntimeException e) {
		StringBuilder msg = new StringBuilder();

		msg.append(e.getMessage());
		msg.append(System.lineSeparator());

		LuaStackTraceElement[] trace = e.getLuaStackTrace();

		for (int i = 0; i < trace.length; i++) {
			msg.append("\tat ");
			msg.append(trace[i]);
			msg.append(System.lineSeparator());
		}

		boolean printError = true;

		try {
			pushHookCall();
			pushString("lua.error");
			pushString(e.getMessage());
			newTable();
			for (int i = 0; i < trace.length; i++) {
				pushNumber(i + 1);
				pushString(trace[i].toString());
				setTable(-3);
			}
			call(3, 1);

			if (!isNil(1))
				printError = toBoolean(1);
		} catch (LuaRuntimeException e2) {
		} // Ignore all errors within the error hook
		finally {
			setTop(0);
		}

		if (printError)
			error(msg.toString());

		e.printStackTrace();
	}

	public void pushHookCall() {
		getGlobal("hook");
		getField(-1, "Call");
		remove(-2);
	}

	public void pushIncomingNet() {
		getGlobal("net");
		getField(-1, "Incoming");
		remove(-2);
	}

	public void pushFace(EnumFacing face) {
		switch (face) {
		case DOWN:
			new Vector(0, 0, -1).push(this);
			break;
		case UP:
			new Vector(0, 0, 1).push(this);
			break;
		case SOUTH:
			new Vector(0, 1, 0).push(this);
			break;
		case NORTH:
			new Vector(0, -1, 0).push(this);
			break;
		case WEST:
			new Vector(-1, 0, 0).push(this);
			break;
		case EAST:
			new Vector(1, 0, 0).push(this);
			break;
		default:
			new Vector(0, 0, 0).push(this);
			break;
		}
	}

	public void autorun() {
		autorun("");
	}

	public void autorun(String side) {
		ArrayList<File> files = FileMount.GetFilesIn("lua/autorun/" + side);
		if(reloader != null) {
			reloader.register("lua/autorun/" + side);
			print("Watching directory 'lua/autorun/" + side + "' for change");
		}
		for (File file : files)
			includeFile(file);
	}

	public void includeDirectory(String base) {
		ArrayList<File> files = FileMount.GetFilesIn("lua/" + base);

		for (File file : files)
			includeFiles(file);
	}

	public void includeFile(String f) {
		File file = FileMount.GetFile("lua/" + f);
		includeFile(file);
	}

	public void includePackedFile(String file) {
		InputStream in = null;
		try {
			in = LuaCraft.getPackedFileInputStream(file);
			includeFileStream(in, file);
		} catch (FileNotFoundException e) {
			throw new LuaRuntimeException("Cannot open " + file + ": No such file or directory");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void includeFiles(File base) {
		for (File file : base.listFiles()) {
			includeFile(file);
		}
	}

	public void includeFile(File file) {
		if (!file.isFile())
			return;

		if (!file.getName().endsWith(".lua"))
			throw new LuaRuntimeException("Cannot open " + FileMount.CleanPath(file) + ": File is not a lua file");

		InputStream in = null;
		try {
			in = new FileInputStream(file);
			includeFileStream(in, FileMount.CleanPath(file));
		} catch (IOException e) {
			throw new LuaRuntimeException("Cannot open " + FileMount.CleanPath(file) + ": No such file or directory");
		} catch (LuaRuntimeException e) {
			handleLuaError(e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void includeFileStream(InputStream in, String file) throws IOException {
		print("Loading: " + file);
		load(in, file);
		call(0, 0);
	}

	@Override
	public synchronized void onFileChange(File file) {
		try {
			if (file.isFile()) {
				print(String.format("Reloading file %s", file.getName()));
				includeFile(file);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
