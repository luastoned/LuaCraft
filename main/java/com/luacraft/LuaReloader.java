package com.luacraft;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.TimeUnit;

/**
 * Seems like the best method is to have multiple watchers and multiple threads...
 */

public class LuaReloader {
	private WatchService watcher;
	private ILuaReloader luaCallbackState;

	private boolean requestDestroy = false;

	public LuaReloader() {
		try {
			watcher = FileSystems.getDefault().newWatchService();
		} catch(IOException e) {
			e.printStackTrace();
		}
		new Thread(createThread()).start();
	}
	public LuaReloader(ILuaReloader reloader) {
		this(); setCallback(reloader);
	}

	/**
	 * Gets the watcher object
	 * @return watcher
	 */
	public WatchService getWatcher() {
		return watcher;
	}

	/**
	 * Sets the function that is called when there is file change detected
	 * @param reloader class that implements the ILuaReloader
	 */
	public void setCallback(ILuaReloader reloader) {
		luaCallbackState = reloader;
	}

	/**
	 * Calls the callback safely
	 * @param file File that changed
	 */
	public void call(File file) {
		if(luaCallbackState != null) luaCallbackState.onFileChange(file);
	}

	/**
	 * Adds a new directory to the watcher
	 * @param file Object containing the path
	 */
	public void register(Path file) {
		try {
			file.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds a new directory to the watcher
	 * @param file String that will be converted to a path
	 */
	public void register(String file) {
		register(FileSystems.getDefault().getPath(file));
	}

	/**
	 * Call this when the lua state shuts down
	 */
	public void shutdown() {
		requestDestroy = true;
	}

	/**
	 * A thread that waits for changes
	 * @return new runnable instance
	 */
	private Runnable createThread() {
		return new Runnable() {
			@Override
			public void run() {
				try {
					while (!requestDestroy) {
						WatchKey watch = getWatcher().poll(250, TimeUnit.MILLISECONDS);
						//WatchKey watch = getWatcher().take();
						Thread.sleep(1000);

						if (watch == null) {
							Thread.yield();
							continue;
						}

						for (WatchEvent<?> event : watch.pollEvents()) {
							Path changed = (Path) event.context();
							switch (event.kind().name()) {
								case "ENTRY_MODIFY":
									Path p = (Path)watch.watchable();
									call(p.resolve(changed).toFile());
									break;
								case "ENTRY_CREATE":
								case "ENTRY_DELETE":
								default: // Maybe in the future
							}
						}
						boolean valid = watch.reset();
						if (!valid)
							break;
					}
				} catch(InterruptedException e) {
					e.printStackTrace();
				} finally {
					try {
						watcher.close();
					} catch(IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
	}
}
