package com.progmasters.mordor.selenium.environment;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class EnvironmentManager {
    public static void initWebDriver() {
        ClassLoader classLoader = EnvironmentManager.class.getClassLoader();
        System.setProperty("webdriver.chrome.driver", classLoader.getResource("chromedriver.exe").getFile());

        ChromeOptions options = new ChromeOptions();
        WebDriver driver = new ChromeDriver(options);
        RunEnvironment.setWebDriver(driver);
    }

    public static void shutDownDriver() {
        RunEnvironment.getWebDriver().quit();
    }
}
