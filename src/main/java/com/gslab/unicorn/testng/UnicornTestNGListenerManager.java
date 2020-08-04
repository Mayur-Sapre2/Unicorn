package com.gslab.unicorn.testng;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.gslab.unicorn.driver.web.WebDriverManager;
import com.gslab.unicorn.logger.LogManager;
import com.gslab.unicorn.reports.ReportsManager;
import com.gslab.unicorn.reports.ReportsTestManager;
import com.gslab.unicorn.reports.UnicornCustomReportListener;
import com.gslab.unicorn.utils.CommonUtils;
import com.gslab.unicorn.utils.Config;
import com.gslab.unicorn.utils.SeleniumUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.util.List;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 * <p>
 * <p>
 * UnicornTestNGListenerManager has implemented testNG listeners
 */

public class UnicornTestNGListenerManager extends LogManager implements IReporter, ITestListener, ISuiteListener, IInvokedMethodListener {

    /**
     * Generate emailable html report
     *
     * @param xmlSuites      List of XmlSuite
     * @param suites         List of ISuite
     * @param reportLocation location where to generate reports
     */
    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
                               String reportLocation) {
        UnicornCustomReportListener unicornCustomReportListener = new UnicornCustomReportListener();
        unicornCustomReportListener.generateEmailableReport(xmlSuites, suites, reportLocation);
    }

    /**
     * get currenly running test case name
     *
     * @param iTestResult
     * @return
     */
    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    /**
     * This belongs to ISuiteListener and will execute before the Suite start
     */
    public void onStart(ISuite suite) {
        try {
            System.out.println("Reports location: " + Config.getReportsLocation());
            CommonUtils.deleteDir(new File(Config.getReportsLocation()));
        } catch (Exception e) {
            System.out.println("Failed to delete existing work location");
            e.printStackTrace();
        }
    }

    /**
     * This belongs to ISuiteListener and will execute, once the Suite is finished
     *
     * @param suite ISuite
     */
    public void onFinish(ISuite suite) {
        ReportsManager.getReporter().flush();
    }

    /**
     * This belongs to ITestListener and will execute before starting of Test set/batch
     *
     * @param testContext ITestContext
     */
    public void onStart(ITestContext testContext) {
    }

    /**
     * This belongs to ITestListener and will execute, once the Test set/batch is finished
     *
     * @param testContext ITestContext
     */
    public void onFinish(ITestContext testContext) {
    }

    /**
     * This belongs to ITestListener and will execute only when the test is pass
     *
     * @param testResult ITestResult
     */
    public void onTestSuccess(ITestResult testResult) {
        ReportsTestManager.getTest().log(Status.PASS, MarkupHelper.createLabel("-- Test " + testResult.getName() + " is passed --", ExtentColor.GREEN));
        printTestResults(testResult);
    }

    /**
     * This belongs to ITestListener and will execute only on the event of fail test
     *
     * @param testResult ITestResult
     */
    public void onTestFailure(ITestResult testResult) {
        ReportsTestManager.getTest().fail(testResult.getThrowable());
        String name[] = testResult.getTestClass().toString().split("=")[1].replace("class", "").split("\\.");
        String testClassName = name[name.length - 1].replace("]", "");
        // if (Config.getTestingType() != null && !Config.getTestingType().equalsIgnoreCase("api")) {
        if (WebDriverManager.getDriver() != null) {
            //Take base64Screenshot screenshot.
            String base64Screenshot = "data:image/png;base64," + ((TakesScreenshot) WebDriverManager.getDriver()).
                    getScreenshotAs(OutputType.BASE64);
            try {
                new SeleniumUtils().captureScreenshot(Config.getReportsLocation() + File.separator + "screenshots" + File.separator + testClassName, testResult.getMethod().getMethodName());
                ReportsTestManager.getTest().addScreenCaptureFromPath(base64Screenshot);
            } catch (Exception e) {
                LogManager.error("Exception while capturing screenshots", e);
            }
        }
        printTestResults(testResult);
    }

    /**
     * This belongs to ITestListener and will execute only if any of the main test(@Test) get skipped
     *
     * @param testResult ITestResult
     */
    public void onTestSkipped(ITestResult testResult) {
        ReportsTestManager.getTest().log(Status.SKIP, "-------- Test Skipped -------".toUpperCase());
        printTestResults(testResult);
    }

    /**
     * This belongs to ITestListener and will execute before the main test start (@Test)
     *
     * @param testResult ITestResult
     */
    public void onTestStart(ITestResult testResult) {
        String classPkg[] = testResult.getTestClass().toString().split("=");
        String testCaseName = classPkg[classPkg.length - 1].replace("]", "").replace("class ", "") + "." + testResult.getMethod().getMethodName();
        info("----------- The execution of test: " + testResult.getMethod().getMethodName() + " started -------------");
        ReportsTestManager.startTest(testCaseName, testResult.getMethod().getDescription());
        ReportsTestManager.getTest().assignCategory(testResult.getMethod().getGroups());
    }


    //ignore this
    public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
    }

    /**
     * This is the method which will be executed in case of test pass, fail or skip
     * This will provide the information on the test
     *
     * @param result ITestResult
     * @return test execution status
     */
    private String printTestResults(ITestResult result) {
        String status = null;
        switch (result.getStatus()) {

            case ITestResult.SUCCESS:
                status = "Pass";
                break;

            case ITestResult.FAILURE:
                status = "Failed";
                break;

            case ITestResult.SKIP:
                status = "Skipped";
        }

        info("Test case status :  '" + status + "' !");
        info("----------- The execution of test: " + result.getMethod().getMethodName() + " completed -------------");
        info("");

        return status;
    }

    /**
     * This belongs to IInvokedMethodListener and will execute before every method including @Before @After @Test
     *
     * @param invokedMethod IInvokedMethod
     * @param iTestResult   ITestResult
     */
    public void beforeInvocation(IInvokedMethod invokedMethod, ITestResult iTestResult) {
    }

    /**
     * This belongs to IInvokedMethodListener and will execute after every method including @Before @After @Test
     *
     * @param invokedMethod IInvokedMethod
     * @param iTestResult   ITestResult
     */
    public void afterInvocation(IInvokedMethod invokedMethod, ITestResult iTestResult) {
    }

    /**
     * This will return method names to the calling function
     *
     * @param method ITestNGMethod
     * @return get currently exeuting method name with its class name.
     */
    private String returnMethodName(ITestNGMethod method) {
        return method.getRealClass().getSimpleName() + "." + method.getMethodName();
    }
}
