package com.progmasters.mordor.selenium.test;

import com.progmasters.mordor.selenium.environment.EnvironmentManager;
import com.progmasters.mordor.selenium.environment.RunEnvironment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import static org.junit.Assert.assertEquals;

public class OrcListTest {

    @Before
    public void startBrowser() {
        EnvironmentManager.initWebDriver();
    }

    @Test
    public void demo() {
        WebDriver driver = RunEnvironment.getWebDriver();
        driver.get("http://localhost:3000/orcList");

        driver.findElement(By.cssSelector("button.navbar-toggler")).click();
        driver.findElement(By.cssSelector("a[href='/orcForm']")).click();
        String title = driver.findElement(By.cssSelector("div.jumbotron h3")).getAttribute("innerHTML");
        assertEquals(title, "Orc form");

        driver.findElement(By.cssSelector("input[name='name']")).sendKeys("Dwarf slayer");
        driver.findElement(By.cssSelector("input[name='killCount']")).sendKeys("10");
        new Select(driver.findElement(By.cssSelector("select[name='raceType']"))).selectByVisibleText("Uruk");
        driver.findElement(By.cssSelector("input[name='weapons'][value='BOW'")).click();
        driver.findElement(By.cssSelector("input[name='weapons'][value='SHIELD'")).click();

        //WebDriverWait wait = new WebDriverWait(driver, 30);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String listPageTitle = driver.findElement(By.cssSelector("div.jumbotron h3")).getAttribute("innerHTML");
        assertEquals(listPageTitle, "Orc list");
    }

    @After
    public void tearDown() {
        EnvironmentManager.shutDownDriver();
    }
}
