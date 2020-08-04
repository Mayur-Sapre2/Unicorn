package com.gslab.unicorn.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 */

public class ReportsTestManager {

    static Map extentTestMap = new HashMap();
    static ExtentReports extent = ReportsManager.getReporter();

    /**
     * Thiw will give the current executing test case instance to the reporter
     *
     * @return
     */
    public static synchronized ExtentTest getTest() {
        return (ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId()));
    }

    /**
     * This will end the logging to the report
     */
    public static synchronized void endTest() {
        extent.flush();
    }

    /**
     * This will start the logging to the report for currently executing test case
     *
     * @param testName test case name
     * @param desc     description of test case
     * @return ExtentTest
     */
    public static synchronized ExtentTest startTest(String testName, String desc) {
        ExtentTest test = extent.createTest(testName, desc);
        extentTestMap.put((int) (long) (Thread.currentThread().getId()), test);
        return test;
    }
}
