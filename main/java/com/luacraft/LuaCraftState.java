package com.luacraft;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;

import com.luacraft.classes.FileMount;
import com.luacraft.classes.Vector;
import com.naef.jnlua.LuaRuntimeException;
import com.naef.jnlua.LuaState;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.server.FMLServerHandler;

public class LuaCraftState extends LuaState {
	private boolean scriptEnforcer = false;
	private Side sideOverride = null;

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

	public MinecraftServer getMinecraftServer() {
		if (getSide().isClient())
			return getForgeClient().getServer();
		else
			return getForgeServer().getServer();
	}

	public void getLoadedAddons() {
		// TODO: Addon list
	}

	public void print(String str) {
		LuaCraft.getLogger().info(str);
	}

	public void error(String str) {
		LuaCraft.getLogger().error(str);
	}

	public void info(String str) {
		LuaCraft.getLogger().info(str);
	}

	public void warning(String str) {
		LuaCraft.getLogger().warn(str);
	}

	public String traceback(String message) {
		getGlobal("debug");
		getField(-1, "traceback");
		remove(-2);

		pushString(message);
		pushInteger(1);
		call(2, 1);

		String trace = toString(1);
		pop(1);
		return trace;
	}

	public void handleException(Exception e) {
		String error = e.getMessage();
		error(traceback(error));
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
			new Vector(0, -1, 0).push(this);
			break;
		case NORTH:
			new Vector(0, 1, 0).push(this);
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

	public void autorun(String side) {
		ArrayList<File> files = FileMount.GetFilesIn("lua/autorun/" + side);

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
			throw new LuaRuntimeException("Cannot open " + file
					+ ": No such file or directory");
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
		if (!file.getName().endsWith(".lua"))
			throw new LuaRuntimeException("Cannot open " + file.getName()
					+ ": File is not a lua file");

		InputStream in = null;
		try {
			in = new FileInputStream(file);
			includeFileStream(in, file.getName());
		} catch (FileNotFoundException e) {
			throw new LuaRuntimeException("Cannot open " + file.getName()
					+ ": No such file or directory");
		} catch (Exception e) {
			handleException(e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void includeFileStream(InputStream in, String file)
			throws IOException {
		print("Loading: " + file);
		load(in, file);
		call(0, 0);
	}
}
