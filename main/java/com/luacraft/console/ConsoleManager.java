package com.luacraft.console;

import java.awt.Color;

import com.luacraft.LuaCraft;

import net.minecraftforge.fml.relauncher.Side;

/**
 * Save your eyes and don't look below
 */
public class ConsoleManager
{
    public final static MessageCallbacks CLIENT_CB = new MessageCallbacks.ClientMessageCallBacks();
    public final static MessageCallbacks SERVER_CB = new MessageCallbacks.ServerMessageCallBacks();

    public final static Color PRINT = new Color(255, 255, 255);
    public final static Color ERROR = new Color(205, 44, 44);
    public final static Color INFO = new Color(255, 210, 225);
    public final static Color WARNING = new Color(200, 100, 0);

    private final static Color CLIENT = new Color(255, 201, 14);
    private final static Color SERVER = new Color(153, 217, 234);

    private static ConsoleFrame console;

    public static void create()
    {
        if(console == null) {
            console = new ConsoleFrame("LuaCraft Console");
            console.setLuaStatesMap(LuaCraft.luaStates);
            onConfigChange();
        }
    }

    /**
     * Call this when the configuration file changes
     */
    public static void onConfigChange()
    {
        if(console != null) {
            console.setVisible(LuaCraft.config.developerConsole.getBoolean());
        }
    }

    public static MessageCallbacks get(Side side)
    {
        switch(side)
        {
            case CLIENT:
                return CLIENT_CB;
            default:
            case SERVER:
                return SERVER_CB;
        }
    }

    public static abstract class MessageCallbacks
    {
        public abstract void print(String text);
        public abstract void print(Color color, String text);
        
        public abstract void msg(String text);
        public abstract void msg(Color color, String text);

		public void onPrint(String text)
        {
			print(PRINT, text);
        }
        public void onError(String text)
        {
        	print(ERROR, text);
        }
        public void onInfo(String text)
        {
        	print(INFO, text);
        }
        public void onWarning(String text)
        {
        	print(WARNING, text);
        }

        public static class ClientMessageCallBacks extends MessageCallbacks
        {
            @Override
        	public void print(String text) {
        		console.clientPrint(CLIENT, text);
        	}
        	
            @Override
            public void print(Color color, String text) {
                console.clientPrint(color, text);
            }

			@Override
			public void msg(String text) {
				console.clientMsg(text);
			}

			@Override
			public void msg(Color color, String text) {
				console.clientMsg(color, text);
			}
        }

        public static class ServerMessageCallBacks extends MessageCallbacks
        {
			@Override
			public void print(String text) {
				console.serverPrint(SERVER, text);
			}
			
            @Override
            public void print(Color color, String text) {
                console.serverPrint(color, text);
            }

			@Override
			public void msg(String text) {
				console.serverMsg(text);
			}

			@Override
			public void msg(Color color, String text) {
				console.serverMsg(color, text);
			}
        }
    }
}
