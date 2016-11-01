package de.datenhahn.vaadin.componentrenderer.testbench;

import com.vaadin.testbench.elements.GridElement;
import de.datenhahn.vaadin.componentrenderer.testbench.util.JmxMemoryUtil;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.fail;

public class MemoryIT extends AbstractTestBase {

    private static final int BATCH_SIZE = 20;
    private static final int MAX_SCROLL = 500;

    public void profileFirefox() throws InterruptedException, IOException {
        System.out.println("\n\n===Test Firefox");
        System.out.println("  BATCH_SIZE: " + BATCH_SIZE);
        setupFirefoxDriver();
        getDriver().get("http://localhost:8080/testbench");

        runMemoryTest();
    }

    @Test
    public void profileChromium() throws InterruptedException, IOException {
        System.out.println("\n\n===Test Chromium");
        System.out.println("  BATCH_SIZE: " + BATCH_SIZE);
        setupChromiumDriver();
        getDriver().get("http://localhost:8080/testbench");

        runMemoryTest();

    }

    public void profileOpera() throws InterruptedException, IOException {
        System.out.println("\n\n=== Test Opera");
        System.out.println("  BATCH_SIZE: " + BATCH_SIZE);

        setupOperaDriver();
        getDriver().get("http://localhost:8080/testbench");

        runMemoryTest();
    }

    public void profileInternetExplorer() throws InterruptedException, IOException {
        System.out.println("\n\n=== Test Internet Explorer");
        System.out.println("  BATCH_SIZE: " + BATCH_SIZE);

        setupInternetExplorerDriverDriver();
        getDriver().get("http://localhost:8080/testbench");

        for (int i = 1; i <= 3; i++) {
            runMemoryTest();
        }
    }

    /**
     * This test scrolls MAX_SCROLL lines in BATCH_SIZE lines per scroll.
     * And outputs the memory usage after every scroll. The heap size
     * before and after the scroll is recorded.
     * After the scroll a garbage collection is triggered and it is
     * checked that there is no memory leak (memory which can't be collected).
     */
    private void runMemoryTest() throws IOException {

        try {
            JmxMemoryUtil jmxMemoryUtil = new JmxMemoryUtil();


            $(GridElement.class).first().scrollToRow(1);

            StopWatch stopWatchFullScroll = new StopWatch();

            jmxMemoryUtil.forceGc();
            System.out.println("Before scroll: " + jmxMemoryUtil.memString());

            jmxMemoryUtil.forceGc();
            long memoryBeforeScroll = jmxMemoryUtil.currentMem();
            stopWatchFullScroll.start();
            for (int i = 1; i <= MAX_SCROLL; i = i + BATCH_SIZE) {
                $(GridElement.class).first().scrollToRow(i);
                System.out.println(jmxMemoryUtil.memString());

            }
            stopWatchFullScroll.stop();
            long memoryAfterScroll = jmxMemoryUtil.currentMem();

            long memoryUsedByGrid = memoryAfterScroll - memoryBeforeScroll;

            System.out.println("scrolling took: " + stopWatchFullScroll.getTime());
            System.out.println("current memory allocated by grid scroll: " + memoryUsedByGrid + "kb");
            System.out.println(jmxMemoryUtil.memString());

            System.out.println("Run garbage collection");
            jmxMemoryUtil.forceGc();

            long memoryAfterGc = jmxMemoryUtil.currentMem();
            long memoryUsedByGridAfterGc = memoryAfterGc - memoryBeforeScroll;

            System.out.println("Grid memory allocation increase after GC: " + memoryUsedByGridAfterGc + "kb");

            assertThat(memoryUsedByGridAfterGc, is(lessThan(200L)));

            System.out.println(jmxMemoryUtil.memString());
        } catch (Exception e) {
            fail("Exception during JMX connect: " + e.getMessage());
        }
    }


}
