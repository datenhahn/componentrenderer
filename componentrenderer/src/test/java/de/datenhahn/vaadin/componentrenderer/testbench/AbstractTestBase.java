package de.datenhahn.vaadin.componentrenderer.testbench;

import com.vaadin.testbench.TestBenchTestCase;
import org.junit.After;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;

public class AbstractTestBase extends TestBenchTestCase {

    protected void setupOperaDriver() {
        DesiredCapabilities capabilities = DesiredCapabilities.operaBlink();

        OperaOptions options = new OperaOptions();
        options.setBinary(new File("/usr/bin/opera"));
        capabilities.setCapability(OperaOptions.CAPABILITY, options);
        setDriver(new OperaDriver(capabilities));
        getDriver().manage().window().setSize(
                new Dimension(1366, 768));
    }

    protected void setupChromiumDriver() {
        setDriver(new ChromeDriver());
        getDriver().manage().window().setSize(
                new Dimension(1366, 768));
    }

    protected void setupFirefoxDriver() {
        setDriver(new FirefoxDriver());
        getDriver().manage().window().setSize(
                new Dimension(1366, 768));
    }

    protected void setupInternetExplorerDriverDriver() {
        setDriver(new InternetExplorerDriver());
        getDriver().manage().window().setSize(
                new Dimension(1366, 768));
    }

    @After
    public void tearDown() throws Exception {
        getDriver().quit();
    }

}
