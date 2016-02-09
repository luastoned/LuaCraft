package com.luacraft;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

public class LuaReloader extends Thread {
	static WatchService watcher;
	static Stack filesChanges;

	public static void WatchDirectory(String dir) throws IOException {
		final Path path = FileSystems.getDefault().getPath(dir);
		watcher = FileSystems.getDefault().newWatchService();
		path.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
	}

	public void run() {
		while (true) {
			try {
				WatchKey watch = watcher.poll(250, TimeUnit.MILLISECONDS);

				if (watch == null) {
					Thread.yield();
					continue;
				}

				for (WatchEvent<?> event : watch.pollEvents()) {
					final Path changed = (Path) event.context();
					System.out.println(changed);
				}
				boolean valid = watch.reset();
				if (!valid)
					break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
