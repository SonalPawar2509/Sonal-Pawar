package com.monefy.utils;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

public class DriverManager {
    private static AndroidDriver driver;
    private static Properties prop;

    public static AndroidDriver getDriver() {
        if (driver == null) {
            initializeDriver();
        }
        return driver;
    }

    public static void initializeDriver() {
        try {
            prop = new Properties();
            FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
            prop.load(fis);

            // Using UiAutomator2Options for Appium 2.x
            UiAutomator2Options options = new UiAutomator2Options();
            options.setDeviceName(prop.getProperty("device.name"));
            options.setUdid(prop.getProperty("device.udid"));
            options.setPlatformName(prop.getProperty("device.platformName", "Android"));
            options.setPlatformVersion(prop.getProperty("device.platformVersion"));
            options.setAppPackage(prop.getProperty("app.packageName"));
            options.setAppActivity(prop.getProperty("app.activityName"));
            options.setAutomationName(prop.getProperty("device.automationName", "UiAutomator2"));
            options.setNoReset(false);
            options.setCapability("ignoreHiddenApiPolicyError", true);
            options.setCapability("autoGrantPermissions", true);
            
            // Add longer timeouts for device connection
            options.setNewCommandTimeout(Duration.ofSeconds(60));
            options.setCapability("adbExecTimeout", 60000);

            URL appiumServer = new URL(prop.getProperty("appium.server.url"));
            driver = new AndroidDriver(appiumServer, options);
            
            // Set implicit wait
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}