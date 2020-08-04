package com.gslab.unicorn.driver.web;

import com.gslab.unicorn.enums.BROWSER;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 * <p>
 * <p>
 */

public interface WebDriverManagerInterface {

    /**
     * Initialize webDriver session
     *
     * @param browser browser type
     * @throws Exception
     */
    WebDriver initWebDriver(BROWSER browser) throws Exception;

    /**
     * Maximize Window
     */
    void maximizeWindow();

    /**
     * Open url.
     *
     * @param url the url
     */
    void openUrl(String url);

    /**
     * Quit the driver.
     */
    void quit() throws Exception;

    /**
     * Refresh page.
     */
    void refreshPage();

    /**
     * close driver
     */
    void close() throws Exception;

    /**
     * This will accept alert
     *
     * @return string alter message
     */
    String acceptAlert();

    /**
     * Delete all cookies
     */
    public void deleteAllCookies();

    /**
     * Type of browser for which webdriver initiated
     *
     * @return either IE,CHROME,FIREFOX or SAFARI
     */
    public String getBrowserType();


    /**
     * Switch to alert
     *
     * @return alter instance
     */
    public Alert switchToAlert();

}

