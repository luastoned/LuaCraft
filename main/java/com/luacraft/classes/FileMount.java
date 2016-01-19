package com.luacraft.classes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.luacraft.LuaCraft;

public class FileMount {
	static ArrayList<File> mounted = new ArrayList<File>();

	public static String CleanPath(File file) {
		String clean = file.getPath();

		if (clean.startsWith(LuaCraft.rootDir))
			return clean.replace(LuaCraft.rootDir, "");

		return file.getName();
	}

	public static File GetFileInRoot(String file) {
		return new File(LuaCraft.rootDir, file);
	}

	public static void MountDirectory(String path) {
		MountDirectory(path, false);
	}

	public static void MountDirectory(String path, boolean head) {
		File dir = new File(LuaCraft.rootDir, path);

		if (!dir.isDirectory())
			return;

		if (head)
			mounted.add(0, dir);
		else
			mounted.add(dir);
	}

	public static File GetFile(String file) {
		File rootFile = GetFileInRoot(file);

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

		File root = GetFileInRoot(folder);

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
