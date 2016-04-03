package com.luacraft.console;

import com.luacraft.LuaCraft;
import net.minecraftforge.fml.relauncher.Side;

import java.awt.*;

/**
 * Save your eyes and don't look below
 */
public class ConsoleManager
{
    public final static MessageCallbacks CLIENT_CB = new MessageCallbacks.ClientMessageCallBacks();
    public final static MessageCallbacks SERVER_CB = new MessageCallbacks.ServerMessageCallBacks();

    private final static Color PRINT = new Color(255, 255, 255);
    private final static Color ERROR = new Color(188, 63, 60);
    private final static Color INFO = new Color(163, 73, 164);
    private final static Color WARNING = new Color(200, 100, 0);

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
            console.setVisible(LuaCraft.enableDeveloperConsole);
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
        public abstract void addText(String text, Color color);

        public void onPrint(String text)
        {
            addText(text, PRINT);
        }
        public void onError(String text)
        {
            addText(text, ERROR);
        }
        public void onInfo(String text)
        {
            addText(text, INFO);
        }
        public void onWarning(String text)
        {
            addText(text, WARNING);
        }

        public static class ClientMessageCallBacks extends MessageCallbacks
        {
            @Override
            public void addText(String text, Color color) {
                console.addClientText(text, color);
            }
        }

        public static class ServerMessageCallBacks extends MessageCallbacks
        {
            @Override
            public void addText(String text, Color color) {
                console.addServerText(text, color);
            }
        }
    }
}
