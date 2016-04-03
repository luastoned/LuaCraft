package com.luacraft.console;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * When modifying a TextPane, Swing requires that you use a thread
 * that waits until the text area is safe to write to.
 * This class allows me to write large amounts of text quickly and
 * without risk of creating too many threads.
 */
public class ConsoleTextPane extends JTextPane
{
    private ConcurrentLinkedQueue<Object> queue;

    /**
     * Current color the new text will be
     */
    private Color currentTextColor;
    private Style style;

    /**
     * Maximum size of the document
     */
    private int maxDocSize = 1024;

    /**
     * Prevent multiple swing threads from being created
     */
    private boolean hasRequestedInvoke = false;

    public ConsoleTextPane()
    {
        super();

        setBackground(ConsoleFrame.BACKGROUND_COLOR);
        setForeground(Color.WHITE);
        setEditable(false);

        style = addStyle("default", null);
        queue = new ConcurrentLinkedQueue<>();
    }

    /**
     * Set maximum size of document (-1 = inf)
     * @param size new size
     */
    public void setMaxDocSize(int size)
    {
        maxDocSize = size;
    }

    /**
     * Gets the size of the document
     * @return size of the doc
     */
    public int getMaxDocSize()
    {
        return maxDocSize;
    }

    /**
     * Set texts color
     * @param color color
     */
    public void setTextColor(Color color)
    {
        if(currentTextColor == null || !currentTextColor.equals(color)) {
            currentTextColor = color;
            queue.add(new Color(currentTextColor.getRed(), currentTextColor.getGreen(), currentTextColor.getBlue()));
        }
    }

    /**
     * Set texts color
     * @param r red
     * @param g green
     * @param b blue
     */
    public void setTextColor(int r, int g, int b)
    {
        setTextColor(new Color(r, g, b));
    }

    /**
     * Append text to the panel
     * @param text text to append
     * @param color color of the text
     */
    public void appendText(String text, Color color)
    {
        setTextColor(color);
        queue.add(text);
    }

    /**
     * Append text to the panel
     * @param text text to append
     * @param r red
     * @param g green
     * @param b blue
     */
    public void appendText(String text, int r, int g, int b)
    {
        setTextColor(r, g, b);
        queue.add(text);
    }

    /**
     * too obvious
     */
    public void setHasRequestedInvoke(boolean b)
    {
        hasRequestedInvoke = b;
    }

    /**
     * Places text in the TextPane
     */
    public void processQueue()
    {
        if(queue.isEmpty() || !hasRequestedInvoke) return;
        setEditable(true);
        Document document = getDocument();
        while(!queue.isEmpty()) {
            Object next = queue.poll();
            if(next instanceof String) {
                try {
                    document.insertString(document.getLength(), (String) (next), style);
                    // Document writer will start having issues if it gets too large
                    if(getMaxDocSize() != -1 &&
                            (document.getLength() + ((String) next).length()) > getMaxDocSize()) {
                        document.remove(0, ((document.getLength() + ((String) next).length()) - getMaxDocSize()));
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            } else if(next instanceof Color) {
                StyleConstants.setForeground(style, (Color)next);
            }
        }
        setEditable(false);
    }
}
