package com.gslab.unicorn.utils;

import com.gslab.unicorn.driver.web.WebDriverManager;
import com.gslab.unicorn.javascript.JavaScriptExecutor;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 * <p>
 * <p>
 * The AngularUtils class provides the functioning to handle angular syncing with test scripts
 */

public class AngularUtils {
    private static WebDriverWait webDriverWait;
    private static JavaScriptExecutor javaScriptExecutor;

    static {
        webDriverWait = new WebDriverWait(WebDriverManager.getDriver(), Long.parseLong(Config.getSeleniumTimeOut()));
        javaScriptExecutor = new JavaScriptExecutor();
    }

    /**
     * Waits for the Angular calls to finish with browser
     * This method is helpful for auto synchronization
     *
     * @return Pass/Fail
     * @author Abhishek Swain
     */
   /* public static String waitForAngular() {
        try {
            WebDriverManager.getDriver().manage().timeouts().setScriptTimeout(Long.parseLong(Config.getSeleniumTimeOut()), TimeUnit.SECONDS);
            new JavaScriptExecutor().executeAsyncScript("var callback=arguments[arguments.length-1];" + " if (!window.angular) { throw new Error('angular could not be found on the window');}if (angular.getTestability) {angular.getTestability(angular.element(document.body)).whenStable(callback); } else { if (!angular.element(angular.element(document.body)).injector()) { throw new Error('root element (' + 'body' + ') has no injector.' +' this may mean it is not inside ng-app.'); }}" + "angular.element(document.body).injector().get('$browser').notifyWhenNoOutstandingRequests(callback);");
            return "Pass:";
        } catch (Throwable e) {
            System.err.println(e.getMessage());
            return "Fail:" + e.getMessage();
        }
    }*/

    /**
     * Wait for JQuery Load
     */
    public static void waitForJQueryLoad() {
        //Wait for jQuery to load
        ExpectedCondition<Boolean> jQueryLoad = driver -> ((Long) ((JavascriptExecutor) WebDriverManager.getDriver())
                .executeScript("return jQuery.active") == 0);

        //Get JQuery is Ready
        boolean jqueryReady = (Boolean) javaScriptExecutor.executeScript("return jQuery.active==0");

        //Wait JQuery until it is Ready!
        if (!jqueryReady) {
            System.out.println("JQuery is NOT Ready!");
            //Wait for jQuery to load
            webDriverWait.until(jQueryLoad);
        } else {
            System.out.println("JQuery is Ready!");
        }
    }

    /**
     * Wait for Angular Load
     */
    public static void waitForAngularLoad() {

        String angularReadyScript = "return angular.element(document).injector().get('$http').pendingRequests.length === 0";

        //Wait for ANGULAR to load
        ExpectedCondition<Boolean> angularLoad = driver -> Boolean.valueOf(((JavascriptExecutor) driver)
                .executeScript(angularReadyScript).toString());

        //Get Angular is Ready
        boolean angularReady = Boolean.valueOf(javaScriptExecutor.executeScript(angularReadyScript).toString());

        //Wait ANGULAR until it is Ready!
        if (!angularReady) {
            System.out.println("ANGULAR is NOT Ready!");
            //Wait for Angular to load
            webDriverWait.until(angularLoad);
        } else {
            System.out.println("ANGULAR is Ready!");
        }
    }

    /**
     * Wait Until JS Ready
     */
    public static void waitUntilJSReady() {

        //Wait for Javascript to load
        ExpectedCondition<Boolean> jsLoad = driver -> ((JavascriptExecutor) WebDriverManager.getDriver())
                .executeScript("return document.readyState").toString().equals("complete");

        //Get JS is Ready
        boolean jsReady = (Boolean) javaScriptExecutor.executeScript("return document.readyState").toString().equals("complete");

        //Wait Javascript until it is Ready!
        if (!jsReady) {
            System.out.println("JS in NOT Ready!");
            //Wait for Javascript to load
            webDriverWait.until(jsLoad);
        } else {
            System.out.println("JS is Ready!");
        }
    }

    /**
     * Wait Until JQuery and JS Ready
     */
    public static void waitUntilJQueryReady() {

        //First check that JQuery is defined on the page. If it is, then wait AJAX
        Boolean jQueryDefined = (Boolean) javaScriptExecutor.executeScript("return typeof jQuery != 'undefined'");
        if (jQueryDefined == true) {
            //Pre Wait for stability (Optional)
            sleep(20);

            //Wait JQuery Load
            waitForJQueryLoad();

            //Wait JS Load
            waitUntilJSReady();

            //Post Wait for stability (Optional)
            sleep(20);
        } else {
            System.out.println("jQuery is not defined on this site!");
        }
    }


    /**
     * Wait Until Angular and JS Ready
     */
    public static void waitUntilAngularReady() {
        //First check that ANGULAR is defined on the page. If it is, then wait ANGULAR
        Boolean angularUnDefined = (Boolean) javaScriptExecutor.executeScript("return window.angular === undefined");
        if (!angularUnDefined) {
            Boolean angularInjectorUnDefined = (Boolean) javaScriptExecutor.executeScript("return angular.element(document).injector() === undefined");
            if (!angularInjectorUnDefined) {
                //Pre Wait for stability (Optional)
                sleep(20);

                //Wait Angular Load
                waitForAngularLoad();

                //Wait JS Load
                waitUntilJSReady();

                //Post Wait for stability (Optional)
                sleep(20);
            } else {
                System.out.println("Angular injector is not defined on this site!");
            }
        } else {
            System.out.println("Angular is not defined on this site!");
        }
    }

    /**
     * Wait Until JQuery Angular and JS is ready
     */
    public static void waitJQueryAngular() {
        waitUntilJQueryReady();
        waitUntilAngularReady();
    }

    private static void sleep(Integer seconds) {
        long secondsLong = (long) seconds;
        try {
            Thread.sleep(secondsLong);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
