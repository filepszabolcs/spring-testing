package com.progmasters.mordor.selenium.environment;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

public class EnvironmentManager {
    public static void initWebDriver() {
        ClassLoader classLoader = EnvironmentManager.class.getClassLoader();
//        System.setProperty("webdriver.chrome.driver", classLoader.getResource("win/chromedriver.exe").getFile());
//        System.setProperty("webdriver.chrome.driver", classLoader.getResource("linux/chromedriver").getFile());
        System.setProperty("webdriver.chrome.driver", classLoader.getResource("mac/chromedriver").getFile());
//        System.setProperty("webdriver.chrome.driver", "mac/chromedriver");

        ChromeOptions options = new ChromeOptions();
        WebDriver driver = new ChromeDriver(options);
        RunEnvironment.setWebDriver(driver);
    }

    public static void shutDownDriver() {
        RunEnvironment.getWebDriver().quit();
    }
}
