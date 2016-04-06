package de.datenhahn.vaadin.componentrenderer.testbench;

import org.junit.Test;

public class MemoryIT extends AbstractTestBase {

    private static final int BATCH_SIZE = 20;
    private static final int MAX_SCROLL = 500;

    @Test
    public void profileFirefox() throws InterruptedException {
        System.out.println("\n\n===Test Firefox");
        System.out.println("  BATCH_SIZE: " + BATCH_SIZE);
        setupFirefoxDriver();
        getDriver().get("http://localhost:8080/testbench");

        runMemoryTest();
    }

    @Test
    public void profileChromium() throws InterruptedException {
        System.out.println("\n\n===Test Chromium");
        System.out.println("  BATCH_SIZE: " + BATCH_SIZE);
        setupChromiumDriver();
        getDriver().get("http://localhost:8080/testbench");

        runMemoryTest();

    }

    @Test
    public void profileOpera() throws InterruptedException {
        System.out.println("\n\n=== Test Opera");
        System.out.println("  BATCH_SIZE: " + BATCH_SIZE);

        setupOperaDriver();
        getDriver().get("http://localhost:8080/testbench");

        runMemoryTest();
    }

    @Test
    public void profileInternetExplorer() throws InterruptedException {
        System.out.println("\n\n=== Test Internet Explorer");
        System.out.println("  BATCH_SIZE: " + BATCH_SIZE);

        setupInternetExplorerDriverDriver();
        getDriver().get("http://localhost:8080/testbench");

        for (int i = 1; i <= 3; i++) {
            runMemoryTest();
        }
    }


    private void runMemoryTest() {

        throw new RuntimeException("Test is broken, checks wrong JVM, reimplementation with remote JMX access to "
                                   + "jetty is needed");
        /*

        $(GridElement.class).first().scrollToRow(1);

        StopWatch stopWatchFullScroll = new StopWatch();

        MemoryUtil.forceGc();
        System.out.println("Before scroll: " + MemoryUtil.memString());

        MemoryUtil.forceGc();
        long memoryBeforeScroll = MemoryUtil.currentMem();
        stopWatchFullScroll.start();
        for (int i = 1; i <= MAX_SCROLL; i = i + BATCH_SIZE) {
            $(GridElement.class).first().scrollToRow(i);
            System.out.println(MemoryUtil.memString());

        }
        stopWatchFullScroll.stop();
        long memoryAfterScroll = MemoryUtil.currentMem();

        long memoryUsedByGrid = memoryAfterScroll - memoryBeforeScroll;

        System.out.println("scrolling took: " + stopWatchFullScroll.getTime());
        System.out.println("current memory used  by grid: " + memoryUsedByGrid);
        System.out.println(MemoryUtil.memString());

        MemoryUtil.forceGc();

        long memoryAfterGc = MemoryUtil.currentMem();
        long memoryUsedByGridAfterGc = memoryAfterGc - memoryBeforeScroll;

        assertThat(memoryUsedByGridAfterGc, is(lessThan(200L)));

        System.out.println(MemoryUtil.memString());*/
    }


}
