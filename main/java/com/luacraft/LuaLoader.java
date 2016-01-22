package com.luacraft;

import java.util.Map;

import org.apache.commons.lang3.SystemUtils;

import com.naef.jnlua.NativeSupport.Loader;

public class LuaLoader implements Loader {
	public boolean isEclipse = false;

	private String rootDir = null;
	private String fileExt = null;
	private String archType = null;

	private String liblua = null;
	private String libjnlua = null;

	private String libraryDir = "libraries/com/luacraft/";

	public LuaLoader(String dir) {
		rootDir = dir;

		if (rootDir.contains("eclipse")) {
			libraryDir = "../src/main/resources/bin/";
			isEclipse = true;
		}

		if (SystemUtils.IS_OS_WINDOWS) {
			fileExt = ".dll";
			liblua = "lua51";
			libjnlua = "jnlua51";
		} else if (SystemUtils.IS_OS_LINUX) {
			fileExt = ".so";
			liblua = "libluajit";
			libjnlua = "libjnluajit";
		} else {
			LuaCraft.getLogger()
					.error(String.format("Your OS (%s) is currently unsupported", System.getProperty("os.name")));
		}

		if (SystemUtils.OS_ARCH.contains("64"))
			archType = "64/";
		else
			archType = "32/";
	}

	private void loadLib(String lib) {
		if (isEclipse) {
			System.load(rootDir + libraryDir + archType + lib + fileExt);
		} else {
			LuaCraft.extractFile("/bin/" + archType + lib + fileExt, libraryDir + lib + fileExt);
			System.load(rootDir + libraryDir + lib + fileExt);
		}
	}

	public void load() {
		loadLib(liblua);
		loadLib(libjnlua);
	}
}