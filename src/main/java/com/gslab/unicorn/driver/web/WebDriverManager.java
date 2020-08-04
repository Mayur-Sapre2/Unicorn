package com.gslab.unicorn.driver.web;

import com.gslab.unicorn.enums.BROWSER;
import com.gslab.unicorn.exceptions.UnicornIOException;
import com.gslab.unicorn.selenium.*;
import com.gslab.unicorn.utils.CommonUtils;
import com.gslab.unicorn.utils.Config;
import io.github.bonigarcia.wdm.DriverManagerType;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;


/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 * <p>
 * <p>
 * This class intent to initialize WebDriver as OS type and browser type.
 * Created by Vivek_Lande
 */

public class WebDriverManager implements WebDriverManagerInterface {
    private static org.openqa.selenium.WebDriver driver;
    private String browserType = null;

    public SeleniumCheckboxInterface seleniumCheckbox;
    public SeleniumDialogInterface seleniumDialog;
    public SeleniumDropdownInterface seleniumDropdown;
    public SeleniumElementsInterface seleniumElements;
    public SeleniumPage seleniumPage;
    public SeleniumRadioInterface seleniumRadio;
    public SeleniumTextboxInterface seleniumTextbox;
    public SeleniumWindowInterface seleniumWindow;


    public static org.openqa.selenium.WebDriver getDriver() {
        return driver;
    }

    private static Proxy createZapProxyConfigurationForWebDriver() {
        Proxy proxy = new Proxy();
        proxy.setHttpProxy(Config.getZapProxy());
        proxy.setSslProxy(Config.getZapProxy());
        return proxy;
    }

    /**
     * This will return the WebDriver instance.
     *
     * @param browser browser type type of browser
     * @return WebDriver
     * @throws Exception
     */
    @Override
    public WebDriver initWebDriver(BROWSER browser) throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        String driverPath = classLoader.getResource("drivers").getPath() + File.separator + getDriverPath(browser);
        switch (browser) {
            case FIREFOX:
                setBrowserType("FIREFOX");
                // System.setProperty("webdriver.gecko.driver", driverPath);

                FirefoxProfile profile = new FirefoxProfile();
                profile.setPreference("browser.download.folderList", 2);
                profile.setPreference("browser.download.useDownloadDir", true);
                profile.setPreference("browser.download.dir", "/home/unicorn"); // Download location
                profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/zip,application/exp");
                profile.setPreference("browser.helperApps.alwaysAsk.force", false);
                profile.setPreference("browser.download.manager.showWhenStarting", false);

                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setAcceptInsecureCerts(true);
                firefoxOptions.setProfile(profile);
                firefoxOptions.setCapability("marionette", true);
                firefoxOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                firefoxOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
                firefoxOptions.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
                firefoxOptions.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
                firefoxOptions.setLogLevel(FirefoxDriverLogLevel.TRACE);
                if (Config.isToPerformPenetrationTesting()) {
                    firefoxOptions.setProxy(createZapProxyConfigurationForWebDriver());
                }
                io.github.bonigarcia.wdm.WebDriverManager.getInstance(DriverManagerType.FIREFOX).setup();
                driver = new FirefoxDriver(firefoxOptions);
                break;
            case CHROME:
                setBrowserType("CHROME");
                // System.setProperty("webdriver.chrome.driver", driverPath);

                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setAcceptInsecureCerts(true);
                chromeOptions.setCapability("marionette", true);
                chromeOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                chromeOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
                chromeOptions.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
                chromeOptions.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
                if (Config.isToPerformPenetrationTesting()) {
                    chromeOptions.setProxy(createZapProxyConfigurationForWebDriver());
                }
                io.github.bonigarcia.wdm.WebDriverManager.getInstance(DriverManagerType.CHROME).setup();
                driver = new ChromeDriver(chromeOptions);
                break;
            case IE:
                setBrowserType("IE");
                //System.setProperty("webdriver.ie.driver", driverPath);

                InternetExplorerOptions internetExplorerOptions = new InternetExplorerOptions();
                internetExplorerOptions.setCapability("marionette", true);
                internetExplorerOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                internetExplorerOptions.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
                internetExplorerOptions.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
                internetExplorerOptions.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
                if (Config.isToPerformPenetrationTesting()) {
                    internetExplorerOptions.setProxy(createZapProxyConfigurationForWebDriver());
                }
                io.github.bonigarcia.wdm.WebDriverManager.getInstance(DriverManagerType.IEXPLORER).setup();
                driver = new InternetExplorerDriver(internetExplorerOptions);
                break;
            case SAFARI:
                setBrowserType("SAFARI");

                SafariOptions safariOptions = new SafariOptions();
                safariOptions.setCapability("marionette", true);
                safariOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                safariOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
                safariOptions.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
                safariOptions.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
                if (Config.isToPerformPenetrationTesting()) {
                    safariOptions.setProxy(createZapProxyConfigurationForWebDriver());
                }
                //WebDriverManager.getInstance(DriverManagerType);
                driver = new SafariDriver(safariOptions);
                break;
        }
        initializeAllUtilityClasses();
        return driver;
    }

    /**
     * Maximize the opened browser window
     */
    @Override
    public void maximizeWindow() {
        getDriver().manage().window().maximize();
    }

    /**
     * Open url in the browser window
     *
     * @param url the url
     */
    @Override
    public void openUrl(String url) {
        getDriver().get(url);
    }

    @Override
    public void quit() throws Exception {
        try {
            if (getBrowserType().equalsIgnoreCase("ie")) {
                Runtime.getRuntime().exec("taskkill /f /fi \"pid gt 0\" /im IEDriverServer.exe");
                Runtime.getRuntime().exec("taskkill /f /fi \"pid gt 0\" /im iexplore.exe");
            }
        } catch (IOException e) {
            throw new UnicornIOException("failed to quite IE instance!", e);
        }
        getDriver().quit();
    }

    /**
     * Refresh the current browser page
     */
    public void refreshPage() {
        getDriver().navigate().refresh();
    }

    /**
     * Close the current browser window
     *
     * @throws Exception
     */
    public void close() throws Exception {
        try {
            if (getBrowserType().equalsIgnoreCase("ie")) {
                Runtime.getRuntime().exec("taskkill /f /fi \"pid gt 0\" /im IEDriverServer.exe");
                Runtime.getRuntime().exec("taskkill /f /fi \"pid gt 0\" /im iexplore.exe");
            }
        } catch (IOException e) {
            throw new UnicornIOException("failed to close IE instance!", e);
        }
        getDriver().close();
    }

    /**
     * Return the type of browser has current webdriver instance
     *
     * @return browser type
     */
    public String getBrowserType() {
        return browserType;
    }

    private void setBrowserType(String browserType) {
        this.browserType = browserType;
    }

    /**
     * Accept the alert popup
     *
     * @return message having on the alert
     */
    public String acceptAlert() {
        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        alert.accept();
        return alertText;
    }

    /**
     * delete all cookies of the session
     */
    public void deleteAllCookies() {
        driver.manage().deleteAllCookies();
    }

    /**
     * This will switch to the alert window
     *
     * @return Alert instance
     */
    public Alert switchToAlert() {
        return driver.switchTo().alert();
    }

    /**
     * This method returns driver location as per os type detected
     *
     * @return os type
     */
    private String getDriverPath(BROWSER browserType) throws Exception {
       /* OSInfo.OSType*/
        String osType = CommonUtils.getOSType();
        String directoryName = null;
        switch (osType) {
            case /*WINDOWS*/ "windows":
                switch (browserType) {
                    case FIREFOX:
                        directoryName = "geckodriver-v0.20.0-win32" + File.separator + "geckodriver.exe";
                        break;
                    case CHROME:
                        directoryName = "chromedriver_win32" + File.separator + "chromedriver.exe";
                        break;
                    case IE:
                        directoryName = "IEDriverServer_Win32_3.11.0" + File.separator + "IEDriverServer.exe";
                        break;
                    case SAFARI:
                        directoryName = "" + File.separator + "";
                }
                break;
            case /*MACOSX*/ "mac":
                switch (browserType) {
                    case FIREFOX:  // not required as of now. by default supported
                        break;
                    case CHROME:
                        directoryName = "chromedriver_mac64" + File.separator + "chromedriver";
                        break;
                    case IE:
                        // not applicable
                        break;
                }
                break;
            case /*LINUX*/ "linux":
                switch (browserType) {
                    case FIREFOX:
                        directoryName = "geckodriver-v0.20.0-linux32" + File.separator + "geckodriver";
                        break;
                    case CHROME:
                        directoryName = "chromedriver_linux32" + File.separator + "chromedriver";
                        break;
                    case IE:
                        // not applicable
                        break;
                }
                break;
            default:
                break;
        }
        return directoryName;
    }

    private void initializeAllUtilityClasses() {
        seleniumCheckbox = new SeleniumCheckbox();
        seleniumDialog = new SeleniumDialog();
        seleniumDropdown = new SeleniumDropdown();
        seleniumElements = new SeleniumElements();
        seleniumPage = new SeleniumPage();
        seleniumRadio = new SeleniumRadio();
        seleniumTextbox = new SeleniumTextbox();
        seleniumWindow = new SeleniumWindow();
    }

}

