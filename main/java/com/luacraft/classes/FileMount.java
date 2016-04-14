package com.luacraft.classes;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import com.luacraft.LuaCraft;
import org.apache.commons.io.filefilter.WildcardFileFilter;

public class FileMount {
	private static File rootFile;
	private static ArrayList<File> mounted = new ArrayList<File>();

	/**
	 * Creates directories if they don't exist
	 * @param path Directories to create
	 */
	public static void CreateDirectories(String path) {
		File file = GetFileInRoot(path);
		if(!file.exists())
			file.mkdirs();
	}

	/**
	 * Add directory to the mount
	 * @param path Directory name
	 */
	public static void MountDirectory(String path) {
		File dir = GetFileInRoot(path);
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
	 * Sets the root directory
	 * @param path
	 */
	public static void SetRoot(String path) {
		SetRoot(new File(path));
	}
	public static void SetRoot(File file) {
		rootFile = file;
	}

	/**
	 * Gets the current storage root
	 * @return File to storage root
	 */
	public static File GetRoot() {
		return rootFile;
	}

	/**
	 * Gets file to the dir from the given root
	 * @param root Start file
	 * @param dirs Dir to follow
	 * @return File object
	 */
	public static File GetFileIn(File root, String[] dirs) {
		File file = root;
		int index = 0;
		do {
			file = new File(file, dirs[index]);
			index++;
		} while(index < dirs.length);
		return file;
	}
	public static File GetFileIn(File root, String path) {
		return GetFileIn(root, SplitPath(path));
	}

	/**
	 * Get file relative to the mounted root
	 * If the mounted root was never set, it will just use the normal root
	 * @param path File name
	 * @return File object
	 */
	public static File GetFileInRoot(String path) {
		return GetFileIn(GetRoot(), path);
	}

	/**
	 * Gets a file in either a mounted directory or in root directory
	 * @param file File name
	 * @return File object
	 */
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

	/**
	 * Gets a list of the files in both the root and then the mounted directories
	 * UPDATE: Can now use wildcards when searching for files.
	 * @param path Folder containing the files
	 * @return List of files found
	 */
	public static ArrayList<File> GetFilesIn(String path) {
		ArrayList<File> files = new ArrayList<>();
		String[] paths = SplitPath(path);
		GetFilesIn(GetRoot(), 0, paths, files);
		for (File mount : mounted)
			GetFilesIn(mount, 0, paths, files);
		return files;
	}
	public static void GetFilesIn(File nextFile, int index, String[] splitPaths, List fileList) {
		if(splitPaths == null ||
				splitPaths.length == 0 ||
				index >= splitPaths.length ||
				index < 0)
			return;
		String next = splitPaths[index];
		for(File file : nextFile.listFiles((FileFilter)(new WildcardFileFilter(next)))) {
			if(file.isDirectory() && (index + 1) < splitPaths.length)
				GetFilesIn(file, index + 1, splitPaths, fileList);
			else if(index >= (splitPaths.length - 1))
				fileList.add(file);
		}
	}

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
	 * Merges a list of directories together
	 * (I still need to test this)
	 * @param dirs list of dirs to merge
	 * @return a conjoined path
	 */
	public static String MergePaths(String[] dirs) {
		StringBuilder path = new StringBuilder();
		for(int i = 0; i < dirs.length; i++) {
			String dir = dirs[i].replace("\\", "/");
			if(!dir.endsWith("/") && i != (dirs.length - 1)) dir += "/";
			if(dir.startsWith("/") && i != 0) dir = dir.substring(1);
			path.append(dir);
		}
		return path.toString();
	}

	/**
	 * Splits a path up into an array
	 * @param path path to split
	 * @return a split path array
	 */
	public static String[] SplitPath(String path) {
		return path.replace("\\", "/").split("/");
	}
}
