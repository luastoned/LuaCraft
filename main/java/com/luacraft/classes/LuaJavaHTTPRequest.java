package com.luacraft.classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.naef.jnlua.LuaState;

public class LuaJavaHTTPRequest extends Thread {

	private LuaState l;
	private int callbackRef;
	public HttpURLConnection connection;

	private int newCallbackRef(int index)
	{
		int ref;
		l.newMetatable("PendingHTTPRequests");
		{
			l.pushValue(index);
			ref = l.ref(-2);
		}
		l.pop(1);
		return ref;
	}

	public LuaJavaHTTPRequest(LuaState luaState, URL requestURL, int index) throws IOException
	{
		l = luaState;
		callbackRef = newCallbackRef(index);

		connection = (HttpURLConnection) requestURL.openConnection();
	}

	public void pushCallbackFunc()
	{
		l.newMetatable("PendingHTTPRequests");
		l.rawGet(-1, callbackRef);
		l.remove(-2);
	}

	public void run()
	{
		synchronized (l) {
			StringBuilder contents = new StringBuilder();

			try {
				InputStreamReader reader = new InputStreamReader(connection.getInputStream());
				BufferedReader in = new BufferedReader(reader);

				String line;
				while ((line = in.readLine()) != null) {
					contents.append(line);
					contents.append(System.lineSeparator());
				}
				in.close();

				pushCallbackFunc();
				if (l.isFunction(-1)) {
					l.pushInteger(connection.getResponseCode());
					l.pushString(contents.toString());
					l.call(2, 0);
				}
			} catch (IOException e) {
				pushCallbackFunc();
				if (l.isFunction(-1)) {
					l.pushNil();
					l.pushString("Failed to resolve: " + e.getMessage());
					l.call(2, 0);
				}
			} finally {
				l.newMetatable("PendingHTTPRequests");
				l.unref(-1, callbackRef);
				l.setTop(0);
			}
		}
	}
}
