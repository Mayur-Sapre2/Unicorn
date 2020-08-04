package com.gslab.unicorn.utils;

import com.gslab.unicorn.enums.TESTING;
import com.gslab.unicorn.exceptions.UnicornFileNotFoundException;
import com.gslab.unicorn.exceptions.UnicornIOException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 * <p>
 * <p>
 * The Config class use to set the customized as will as default configuration across the unicorn framework
 */

public class Config {

    //Default properties
    private static final String DEFAULT_REPORT_LOCATION = System.getProperty("user.dir") + File.separator + "test-output";
    private static final String ZAP_HOSTNAME = "localhost";
    private static final String DEFAULT_BROWSER = "firefox";
    //customize properties
    private static final String AUT_URL_PROPERTY = "aut.url";
    private static boolean IS_TO_PERFORM_PENETRATION_TESTING = false;
    private static final String REPORTS_LOCATION_PROPERTY = "reports.location";
    private static final String GLOBAL_TIME_OUT_PROPERTY = "selenium.wait.timeout";
    private static final String ALLOW_PENETRATION_TESTING_PROPERTY = "run.penetration.testing";
    private static final String BROWSER_TYPE_PROPERTY = "browser.type";
    private static int ZAP_SESSION_PORT;


    private static String TESTING_SPECIFIED_TYPE = "ui";
    private static String AUT_URL = null;
    private static String GLOBAL_TIME_OUT = null;
    private static String REPORTS_LOCATION;
    private static String BROWSER_TYPE = null;

    /**
     * Initialize all the neccessary constant used withing the framework
     * This must have called before any testNG notation being called, so must be called in static block of test class or its parent class.
     *
     * @param mFileName this would be configuration file and must have all properties mentioned(either with empty) in unicorn.config file
     * @throws Exception
     */
    public static void initConfig(String mFileName, boolean isDefaultInitialization) throws Exception {
        if ((mFileName == null || mFileName.trim().equals("")) && !isDefaultInitialization)
            throw new UnicornFileNotFoundException("Config file path can not be 'null' or empty(\"\"), please provide valid path!");
        try {
            if (isDefaultInitialization) {
                //set selenium wait timeout
                setSeleniumTimeOut("90");

                //set report location
                setReportsLocation(DEFAULT_REPORT_LOCATION);

                //set browser type
                setBrowserType(DEFAULT_BROWSER);

                //set aut url
                setAutUrl("");
            } else {
                try {
                    FileReader reader = new FileReader(mFileName);

                    Properties properties = new Properties();
                    properties.load(reader);

                    //set selenium wait timeout
                    setSeleniumTimeOut(properties.getProperty(GLOBAL_TIME_OUT_PROPERTY, "90"));

                    //set report location
                    setReportsLocation(properties.getProperty(REPORTS_LOCATION_PROPERTY, DEFAULT_REPORT_LOCATION));

                    //set browser type
                    setBrowserType(properties.getProperty(BROWSER_TYPE_PROPERTY, DEFAULT_BROWSER));

                    //set aut url
                    setAutUrl(properties.getProperty(AUT_URL_PROPERTY));

                    //set penetration test to be performed
                    if ((!properties.getProperty(ALLOW_PENETRATION_TESTING_PROPERTY).equals("")) && ((properties.getProperty(ALLOW_PENETRATION_TESTING_PROPERTY).equalsIgnoreCase("true")) || (properties.getProperty(ALLOW_PENETRATION_TESTING_PROPERTY).equalsIgnoreCase("yes")))) {
                        IS_TO_PERFORM_PENETRATION_TESTING = true;
                        setZapSessionPort();
                    }
                } catch (IOException ex) {
                    throw new UnicornIOException("failed to read config file", ex);
                }
            }
            File outputDir = new File(getReportsLocation());
            if (!outputDir.exists()) {
                outputDir.mkdir();
            } else {
                CommonUtils.deleteDir(outputDir);
                outputDir.mkdir();
            }
        } catch (IOException ex) {
            throw new UnicornIOException("Exception while setting configuration properties", ex);
        }
    }

    /**
     * Get the report generation location
     *
     * @return report location
     */
    public static String getReportsLocation() {
        return REPORTS_LOCATION;
    }

    private static void setReportsLocation(String directory) {
        if (directory != null && !directory.trim().equals(""))
            REPORTS_LOCATION = directory;
        else REPORTS_LOCATION = DEFAULT_REPORT_LOCATION;
    }

    /**
     * Get if security test has to run or not
     *
     * @return
     */
    public static boolean isToPerformPenetrationTesting() {
        return IS_TO_PERFORM_PENETRATION_TESTING;
    }

    /**
     * Return the zap proxy url
     *
     * @return
     */
    public static String getZapProxy() {
        return ZAP_HOSTNAME + ":" + getZapSessionPort();
    }

    public static void setZapSessionPort() {
        ZAP_SESSION_PORT = CommonUtils.findOpenPortOnAllLocalInterfaces();
    }

    /**
     * Retrieve global time out value for selenium operation
     *
     * @return timeout
     */
    public static String getSeleniumTimeOut() {
        return GLOBAL_TIME_OUT;
    }

    private static void setSeleniumTimeOut(String timeout) {
        GLOBAL_TIME_OUT = timeout;
    }

    /**
     * Get Zap proxy session port
     *
     * @return
     */
    public static int getZapSessionPort() {
        return ZAP_SESSION_PORT;
    }

    /**
     * get testing type either either api or ui
     *
     * @return api or ui
     */
    public static String getTestingType() {
        return TESTING_SPECIFIED_TYPE.equalsIgnoreCase("api") ? "api" : "ui";
    }

    public static void setTestingType(TESTING testingType) {
        TESTING_SPECIFIED_TYPE = testingType.toString();
    }

    /**
     * Get current AUT url.
     *
     * @return url if it has set by properties file else null.
     */
    public static String getAutUrl() {
        return AUT_URL;
    }

    /**
     * Get browser type specified in config file
     *
     * @return browser name
     */
    public static String getBrowserType() {
        if (BROWSER_TYPE.equalsIgnoreCase("f") || BROWSER_TYPE.equalsIgnoreCase("firefox"))
            return "Firefox";
        else if (BROWSER_TYPE.equalsIgnoreCase("c") || BROWSER_TYPE.equalsIgnoreCase("chrome"))
            return "Chrome";
        else if (BROWSER_TYPE.equalsIgnoreCase("s") || BROWSER_TYPE.equalsIgnoreCase("safari"))
            return "Safari";
        else if (BROWSER_TYPE.equalsIgnoreCase("i") || BROWSER_TYPE.equalsIgnoreCase("ie"))
            return "IE";
        else
            return "Unknown browser";
    }

    private static void setBrowserType(String browser) {
        BROWSER_TYPE = browser;
    }

    private static void setAutUrl(String url) {
        AUT_URL = url;
    }


}
