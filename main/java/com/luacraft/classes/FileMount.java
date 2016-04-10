package com.luacraft.classes;

import java.io.File;
import java.util.ArrayList;

import com.luacraft.LuaCraft;

public class FileMount {
	private static File mountedRoot;
	private static ArrayList<File> mounted = new ArrayList<File>();

	/**
	 * Removes the root from a files path
	 * @param file File to get path of
	 * @return Path without root dir
	 */
	public static String CleanPath(File file) {
		String clean = file.getPath();

		if (clean.startsWith(LuaCraft.rootDir))
			return clean.replace(LuaCraft.rootDir, "");

		return file.getName();
	}

	/**
	 * Creates directories if they don't exist
	 * @param path Directories to create
	 */
	public static void CreateDirectories(String path) {
		File file = GetFileInMountedRoot(path);
		if(!file.exists())
			file.mkdirs();
	}

	/**
	 * Set the root folder that will contain the lua folders
	 * Leave empty if you just want to use the default root dir
	 * @param path Name of the root dir
	 */
	public static void SetMountedRoot(String path) {
		mountedRoot = GetFileInRoot(path);
	}

	/**
	 * Gets the mounted root
	 * @return null if it hasn't been set
	 */
	public static File GetMountedRoot() {
		return mountedRoot;
	}

	/**
	 * Get file relative to the root dir
	 * @param file File name
	 * @return File object
	 */
	public static File GetFileInRoot(String file) {
		return new File(LuaCraft.rootDir, file);
	}

	/**
	 * Get file relative to the mounted root
	 * If the mounted root was never set, it will just use the normal root
	 * @param file File name
	 * @return File object
	 */
	public static File GetFileInMountedRoot(String file) {
		if(mountedRoot == null) {
			return GetFileInRoot(file);
		} else {
			return new File(GetMountedRoot(), file);
		}
	}

	/**
	 * Add directory to the mount
	 * @param path Directory name
	 */
	public static void MountDirectory(String path) {
		File dir = GetFileInMountedRoot(path);

		if (!dir.isDirectory())
			return;

		mounted.add(dir);
	}

	/**
	 * Gets list of current mounted directories
	 * @return copied list
	 */
	public static ArrayList<File> GetMountedDirectories() {
		return new ArrayList<>(mounted);
	}

	/**
	 * Gets a file in first the root dir, then a mounted dir if one didn't exist in the root
	 * @param file File name
	 * @return File object
	 */
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

	/**
	 * Gets a list of the files in both the root and then the mounted directories
	 * @param folder Folder containing the files
	 * @return List of files
	 */
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
