package com.gslab.unicorn.reports;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.gslab.unicorn.logger.LogManager;
import com.gslab.unicorn.utils.Config;

import java.io.File;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 */

public class ReportsManager {
    private static ExtentHtmlReporter htmlReporter;
    private static ExtentReports extent;
    private static ExtentTest logger;

    /**
     * This will initialize the extent report
     *
     * @return ExtentReports
     */
    public synchronized static ExtentReports getReporter() {
        if (extent != null)
            return extent; //avoid creating new instance of html file
        extent = new ExtentReports();
        extent.attachReporter(getHtmlReporter());
        extent.setAnalysisStrategy(AnalysisStrategy.CLASS);
        return extent;
    }

    private static ExtentHtmlReporter getHtmlReporter() {

        try {
            htmlReporter = new ExtentHtmlReporter(Config.getReportsLocation() + File.separator + "UnicornReport.html");

            // htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + File.separator + "Execution-Reports" + File.separator + "UnicornReport.html");

            extent.setSystemInfo("Host Name", "UnicornAutomation");
            extent.setSystemInfo("Environment", "Automation Testing");
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Browser", Config.getBrowserType());
            extent.setSystemInfo("User Name", "Vivek Lande");


            htmlReporter.config().setDocumentTitle("Unicorn Automation Reporting");
            htmlReporter.config().setReportName("Unicorn Automation Reporting");
            htmlReporter.config().setTheme(Theme.DARK);
            htmlReporter.config().setTestViewChartLocation(ChartLocation.BOTTOM);
            // make the charts visible on report open
            htmlReporter.config().setChartVisibilityOnOpen(false);
            //htmlReporter.setAppendExisting(true);
            //htmlReporter.loadXMLConfig(System.getProperty(System.getProperty("user.dir") + File.separator+"extent-config.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return htmlReporter;
    }

    /**
     * Log test step to the report
     *
     * @param infoMsg info message
     */
    public static void testStepInfo(String infoMsg) {
        ReportsTestManager.getTest().log(Status.INFO, infoMsg);
        LogManager.info(infoMsg);
    }

    /**
     * Log test step debug to the report
     *
     * @param debugMsg debug message
     */
    public static void testStepDebug(String debugMsg) {
        ReportsTestManager.getTest().log(Status.DEBUG, debugMsg);
        LogManager.debug(debugMsg);
    }

    /**
     * Log test step error to the report
     *
     * @param errorMsg error message
     */
    public static void testStepError(String errorMsg) {
        ReportsTestManager.getTest().log(Status.ERROR, errorMsg);
        LogManager.error(errorMsg);
    }

    /**
     * Log test step fatal info to the report
     *
     * @param fatalMsg fatal message
     */
    public static void testStepFatal(String fatalMsg) {
        ReportsTestManager.getTest().log(Status.FATAL, fatalMsg);
        LogManager.fatal(fatalMsg);
    }

    /**
     * Log test step warning message to the report
     *
     * @param warningMsg warning message
     */
    public static void testStepWarning(String warningMsg) {
        ReportsTestManager.getTest().log(Status.WARNING, warningMsg);
        LogManager.warning(warningMsg);
    }

    /**
     * Assign author author for test case to the report
     *
     * @param authorName author name
     */
    public static void testAuthor(String authorName) {
        ReportsTestManager.getTest().assignAuthor(authorName);
    }

    /**
     * Assign category for test case to the report
     *
     * @param category category
     */
    public static void testCategory(String category) {
        ReportsTestManager.getTest().assignCategory(category);
    }

}
