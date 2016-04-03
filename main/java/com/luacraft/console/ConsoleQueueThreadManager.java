package com.luacraft.console;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Makes it so I only need to run 1 thread
 */
public class ConsoleQueueThreadManager
{
    private static List<ConsoleTextPane> tasks = new ArrayList<>();
    private static Thread thread;

    public static void startThread()
    {
        if(thread == null) {
            thread = new Thread(mainTask());
            thread.start();
        }
    }

    public static void add(ConsoleTextPane pane)
    {
        tasks.add(pane);
    }

    private static Runnable mainTask()
    {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        for(ConsoleTextPane panel : tasks) {
                            panel.setHasRequestedInvoke(true);
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    panel.processQueue();
                                    panel.setHasRequestedInvoke(false);
                                }
                            });
                        }
                        Thread.sleep(100);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
