package com.luacraft.console;

import com.luacraft.LuaCraft;
import com.luacraft.LuaCraftState;
import com.naef.jnlua.LuaException;
import com.naef.jnlua.LuaRuntimeException;
import com.naef.jnlua.LuaState;
import net.minecraftforge.fml.relauncher.Side;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConsoleFrame extends JFrame
{
    public static final Color BACKGROUND_COLOR = new Color(55, 55, 55);

    private boolean isFocused;

    private JTextField inputField;

    private JTabbedPane tabs;

    private ConsoleTextPane clientOutputPane;
    private ConsoleTextPane serverOutputPane;

    // This should not be copied
    public static HashMap<Side, LuaCraftState> luaStates;

    /**
     * For debugging purposes
     */
    public static void main(String[] args)
    {
        new ConsoleFrame("LuaCraft Console").setVisible(true);
    }

    /**
     * Console frame for LuaCraft
     * @param name name of the console
     */
    public ConsoleFrame(String name)
    {
        super(name);

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setBackground(BACKGROUND_COLOR);
        getContentPane().setBackground(BACKGROUND_COLOR);

        setSize(750, 400);
        setMinimumSize(new Dimension(750, 400));

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

        // Auto-focus when opened
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                super.windowActivated(e);
                inputField.requestFocusInWindow();
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                super.windowDeactivated(e);
                isFocused = false;
            }

            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                LuaCraft.config.developerConsole.set(false);
                LuaCraft.config.save();
            }
        });

        // Tabs
        tabs = new JTabbedPane();

        JComponent clientTab, serverTab;
        tabs.addTab("Client", clientTab = createTab());
        tabs.addTab("Server", serverTab = createTab());

        //tabs.setBackground(BACKGROUND_COLOR);
        //tabs.setForeground(Color.WHITE);

        // Output pane
        clientOutputPane = new ConsoleTextPane();
        serverOutputPane = new ConsoleTextPane();

        FocusAdapter adapter = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                // This will allow the user to click the panel once without giving it focus, but if they want to copy they can click again
                if (!isFocused && isVisible()) {
                    inputField.requestFocusInWindow();
                    isFocused = true;
                }
            }
        };

        clientOutputPane.addFocusListener(adapter);
        serverOutputPane.addFocusListener(adapter);
        ConsoleQueueThreadManager.add(clientOutputPane);
        ConsoleQueueThreadManager.add(serverOutputPane);

        // Text input area
        inputField = new JTextField();
        inputField.requestFocusInWindow();
        inputField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

        inputField.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = inputField.getText();
                // Processing the code entered
                if (text != null && !text.equals("")) {
                    // Clear the input field
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            inputField.setText("");
                        }
                    });
                    addTextToCurrentSelection(">" + text + "\n", Color.WHITE);
                    processInput(text);
                }
            }
        });

        // Append everything to content pane
        clientTab.add(new JScrollPane(clientOutputPane));
        serverTab.add(new JScrollPane(serverOutputPane));
        getContentPane().add(tabs);
        getContentPane().add(inputField);

        ConsoleQueueThreadManager.startThread();
    }

    /**
     * Gets selected panel index
     * @return side enum
     */
    private Side getSelectedTabSide()
    {
        switch(tabs.getSelectedIndex()) {
            case 0:
                return Side.CLIENT;
            case 1:
                return Side.SERVER;
            default:
                return null;
        }
    }

    /**
     * Searches lua state for correlating side
     * @param side side enum
     * @return a lua state
     */
    private LuaCraftState getStateBySide(Side side)
    {
        if(luaStates != null && side != null) {
            return luaStates.get(side);
        }
        return null;
    }

    /**
     * Get lua state from Side enum
     * @return a lua state object
     */
    private LuaCraftState getSelectedLuaState()
    {
        return getStateBySide(getSelectedTabSide());
    }

    /**
     * Creates new panel object
     * @return new JPanel instance
     */
    private JComponent createTab()
    {
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(1, 1));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setForeground(Color.WHITE);
        return panel;
    }

    /**
     * Processes input from text field
     * @param input luacode/commands
     */
    private void processInput(String input) {
        if(input.startsWith("=")) {
            // Maybe I can do something later
            String command = input.substring(1);
            if(command.equals("hide")) {
                setVisible(false);
            }
        } else {
            LuaCraftState state = getSelectedLuaState();
            if (state == null) {
                addTextToCurrentSelection("Lua state is inactive", Color.ORANGE);
                return;
            }
            synchronized (state) {
                try {
                    state.load(input, "console");
                    state.call(0, LuaState.MULTRET);
                } catch (LuaRuntimeException e) {
                    state.handleLuaError(e);
                    e.printLuaStackTrace();
                    e.printStackTrace();
                } catch (LuaException e) {
                    state.error(e.toString());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Sets the lua state map (NOTE: It shouldn't copy, just reference)
     * @param map Map with lua states
     */
    public void setLuaStatesMap(HashMap<Side, LuaCraftState> map)
    {
        luaStates = map;
    }

    public void addTextToCurrentSelection(String text, Color color)
    {
        switch (getSelectedTabSide()) {
            case CLIENT:
                addClientText(text, color);
                break;
            case SERVER:
                addServerText(text, color);
        }
    }

    public void addClientText(String text, Color color)
    {
        if(!text.endsWith("\n")) text += "\n";
        clientOutputPane.appendText(text, color);
    }

    public void addServerText(String text, Color color)
    {
        if(!text.endsWith("\n")) text += "\n";
        serverOutputPane.appendText(text, color);
    }
}
