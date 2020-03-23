package com.progmasters.mordor.selenium.test;

import com.progmasters.mordor.selenium.environment.EnvironmentManager;
import com.progmasters.mordor.selenium.environment.RunEnvironment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class OrcListTest {

    @BeforeEach
    public void startBrowser() {
        EnvironmentManager.initWebDriver();
    }

    @Test
    public void demo() {
        WebDriver driver = RunEnvironment.getWebDriver();


        driver.get("http://localhost:4200/orc-list");

        driver.findElement(By.cssSelector("a[href='/orc-form']")).click();
        String title = driver.findElement(By.cssSelector("body > app-root > div > app-orc-form > div > h3")).getAttribute("innerHTML");
        assertEquals(title, "Orc form");

        //driver.findElement(By.cssSelector("button.navbar-toggler")).click();
        driver.findElement(By.cssSelector("input[name='name']")).sendKeys("Dwarf slayer " + System.currentTimeMillis());
        driver.findElement(By.cssSelector("input[name='killCount']")).sendKeys("10");
        new Select(driver.findElement(By.cssSelector("select[name='raceType']"))).selectByVisibleText("Uruk");
        driver.findElements(By.cssSelector("input[name='weapons']")).get(0).click();
        driver.findElements(By.cssSelector("input[name='weapons']")).get(1).click();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String listPageTitle = driver.findElement(By.cssSelector("body > app-root > div > app-orc-list > h2")).getAttribute("innerHTML");
        assertEquals(listPageTitle, "Orc list");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @AfterEach
    public void tearDown() {
        EnvironmentManager.shutDownDriver();
    }
}
