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

public class LuaReloader {
	private Thread thread;
	private WatchService watcher;
	private ILuaReloader luaCallbackState;

	private boolean requestDestroy = false;

	public LuaReloader()
	{
		try {
			watcher = FileSystems.getDefault().newWatchService();
		} catch(IOException e) {
			e.printStackTrace();
		}
		thread = new Thread(createThread());
		thread.start();
	}
	public LuaReloader(ILuaReloader reloader)
	{
		this(); setLuaCallbackState(reloader);
	}

	public WatchService getWatcher()
	{
		return watcher;
	}

	public void setLuaCallbackState(ILuaReloader reloader)
	{
		luaCallbackState = reloader;
	}
	public void call(File file)
	{
		if(luaCallbackState != null) luaCallbackState.onFileChange(file);
	}

	public void register(Path file)
	{
		try {
			file.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	public void register(String file)
	{
		register(FileSystems.getDefault().getPath(file));
	}

	public void shutdown()
	{
		requestDestroy = true;
		try {
			watcher.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private Runnable createThread()
	{
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
				}
			}
		};
	}
}
