package com.luacraft.classes;

import java.io.File;
import java.util.ArrayList;

import com.luacraft.LuaCraft;

public class FileMount {
	private static File mountedRoot;
	private static ArrayList<File> mounted = new ArrayList<File>();

	public static String CleanPath(File file) {
		String clean = file.getPath();

		if (clean.startsWith(LuaCraft.rootDir))
			return clean.replace(LuaCraft.rootDir, "");

		return file.getName();
	}

	public static void CreateDirectories(String path) {
		File file = GetFileInMountedRoot(path);
		if(!file.exists())
			file.mkdirs();
	}

	public static void SetMountedRoot(String path) {
		mountedRoot = GetFileInRoot(path);
	}

	public static File GetFileInRoot(String file) {
		return new File(LuaCraft.rootDir, file);
	}

	public static File GetFileInMountedRoot(String file) {
		if(mountedRoot == null) {
			return GetFileInRoot(file);
		} else {
			return new File(GetMountedRoot(), file);
		}
	}

	public static void MountDirectory(String path) {
		File dir = GetFileInMountedRoot(path);

		if (!dir.isDirectory())
			return;

		mounted.add(dir);
	}

	public static ArrayList<File> GetMountedDirectories() {
		return new ArrayList<>(mounted);
	}

	public static File GetMountedRoot() {
		return mountedRoot;
	}

	public static File GetFile(String file) {
		File rootFile = GetFileInMountedRoot(file);

		if (rootFile.exists())
			return rootFile;

		for (File root : mounted) {
			File newFile = new File(root, file);
			if (newFile.exists())
				return newFile;
		}

		return null;
	}

	public static ArrayList<File> GetFilesIn(String folder) {
		ArrayList<File> files = new ArrayList<File>();

		File root = GetFileInMountedRoot(folder);

		if (root.isDirectory())
			for (File file : root.listFiles())
				files.add(file);

		for (File mount : mounted) {
			File mountRoot = new File(mount, folder);

			if (mountRoot.exists())
				for (File file : mountRoot.listFiles())
					files.add(file);
		}

		return files;
	}
}
