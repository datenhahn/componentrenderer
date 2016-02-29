package de.datenhahn.vaadin.componentrenderer.testbench.util;

import java.lang.management.ManagementFactory;

public class MemoryUtil {

    private static final int DIVISOR = 1024;

    public static long currentMem() {
        return ManagementFactory.getMemoryMXBean().
                getHeapMemoryUsage().getUsed() / DIVISOR;
    }

    public static long maxMem() {
        return ManagementFactory.getMemoryMXBean().
                getHeapMemoryUsage().getMax() / DIVISOR;
    }

    public static String memString() {
        return currentMem() + " / " + maxMem() + " used";
    }

    public static void forceGc() {
        System.gc();
        System.runFinalization();
        try {
            Thread.sleep(100);
            System.gc();
            System.runFinalization();
            Thread.sleep(100);
            System.gc();
            System.runFinalization();
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
