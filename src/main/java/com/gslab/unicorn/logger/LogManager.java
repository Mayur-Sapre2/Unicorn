package com.gslab.unicorn.logger;

import com.gslab.unicorn.utils.Config;
import com.gslab.unicorn.utils.ScreenRecordingManager;
import org.apache.log4j.*;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import java.io.File;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 * <p>
 * <p>
 * The LogManager provides the logging facility
 */

public class LogManager extends ScreenRecordingManager {


    public static Logger logger;
    public static Logger suitLogger;
    private static boolean isLoggingDone = false;
    private static boolean isSuiteLoggingDone = false;
    private FileAppender fAppender;

    public LogManager() {
        super();
    }

    /**
     * This tale the customized properties file and pass to the ScreenRecordingManager
     *
     * @param configFilePath properties file
     */
    public LogManager(String configFilePath) {
        super(configFilePath);
    }

    /**
     * log info message to info file
     *
     * @param infoMessage info message
     */
    public static void info(String infoMessage) {
        logger.info(infoMessage);
        System.out.println(infoMessage);
    }

    /**
     * log info message and exception stack trace to log file
     *
     * @param infoMessage info message
     * @param ex          exception object
     */
    public static void info(String infoMessage, Exception ex) {
        logger.info(infoMessage, ex);
        System.out.println(infoMessage);
    }

    /**
     * Log error message to log file
     *
     * @param errorMessage error message
     */
    public static void error(String errorMessage) {
        logger.error(errorMessage);
        System.out.println(errorMessage);
    }

    /**
     * Log error message and exception stack trace to log file
     *
     * @param errorMessage    error message
     * @param exceptionObject exception object
     */
    public static void error(String errorMessage, Exception exceptionObject) {
        logger.error(errorMessage, exceptionObject);
        System.out.println(errorMessage);
    }

    /**
     * Log debug message to log file
     *
     * @param debugMessage debug message
     */
    public static void debug(String debugMessage) {
        logger.debug(debugMessage);
        System.out.println(debugMessage);
    }

    /**
     * Log debug message and exception stack trace to log file
     *
     * @param debugMessage    debug message
     * @param exceptionObject exception object
     */
    public static void debug(String debugMessage, Exception exceptionObject) {
        logger.debug(debugMessage, exceptionObject);
        System.out.println(debugMessage);
    }

    /**
     * Log debug message to log file
     *
     * @param warnMessage warning message
     */
    public static void warning(String warnMessage) {
        logger.warn(warnMessage);
        System.out.println(warnMessage);
    }

    /**
     * Log fatal message and exception stack trace to log file
     *
     * @param fatalMessage    fatal message
     * @param exceptionObject exception object
     */
    public static void fatal(String fatalMessage, Exception exceptionObject) {
        logger.fatal(fatalMessage, exceptionObject);
        System.out.println(fatalMessage);
    }

    /**
     * Log fatal message to log file
     *
     * @param fatalMessage fatal message
     */
    public static void fatal(String fatalMessage) {
        logger.fatal(fatalMessage);
        System.out.println(fatalMessage);
    }

    /**
     * Log warning message and exception stack trace to log file
     *
     * @param warnMessage     warning message
     * @param exceptionObject exception object
     */
    public static void warning(String warnMessage, Exception exceptionObject) {
        logger.warn(warnMessage, exceptionObject);
        System.out.println(warnMessage);
    }


    /**
     * This method will log error message and will fail the execution
     *
     * @param logMessage message to be logged
     */
    public static void assertFail(String logMessage) {
        logger.error(logMessage);
        Assert.fail(logMessage);
    }

    /**
     * This method will log error message and stack trace of exception and will fail the execution
     *
     * @param logMessage      message to be logged
     * @param exceptionObject Exception object
     */
    public static void assertFail(String logMessage, Exception exceptionObject) {
        logger.error(logMessage, exceptionObject);
        exceptionObject.printStackTrace();
        Assert.fail(logMessage);
    }

    /**
     * This will setup the class level logger and create the log file by class name of test case.
     */
    @BeforeClass(alwaysRun = true)
    public void setupLogger() {
        if (!isLoggingDone) {
            logger = Logger.getLogger(this.getClass().getSimpleName());
            logger.setLevel(Level.INFO);
            logger.setLevel(Level.ERROR);
            logger.setLevel(Level.DEBUG);
            PatternLayout layout = new PatternLayout("%d{dd MMM yyyy HH:mm:ss} %-5p %x - %m%n");
            logger.addAppender(new ConsoleAppender(layout));
            File logFile = null;
            try {
                try {
                    logFile = new File(Config.getReportsLocation() + File.separator + "logs" + File.separator + this.getClass().getSimpleName() + ".log");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                fAppender = new RollingFileAppender(layout, logFile.getAbsolutePath());
                System.out.println("Path for log file: " + logFile.getAbsolutePath());

                logger.addAppender(fAppender);


            } catch (Exception e) {
                System.out.println("Failed to created logger ..  !!!");
                e.printStackTrace();
            } finally {
                isLoggingDone = true;
            }
        }
    }

    /**
     * This will reset the logger
     */
    @AfterClass(alwaysRun = true)
    public void resetLogger() {
        isLoggingDone = false;
    }

    /**
     * This will setup the suite level logger and create the log file by current executing suite name of test case.
     */
    @BeforeSuite(alwaysRun = true)
    public void setSuitLogger() {
        if (!isSuiteLoggingDone) {
            suitLogger = Logger.getLogger("unicornSuite");
            suitLogger.setLevel(Level.INFO);
            suitLogger.setLevel(Level.ERROR);
            suitLogger.setLevel(Level.DEBUG);
            PatternLayout layout = new PatternLayout("%d{dd MMM yyyy HH:mm:ss} %-5p %x - %m%n");
            suitLogger.addAppender(new ConsoleAppender(layout));
            File logFile = null;
            try {
                try {
                    logFile = new File(/*Config.getReportsLocation()*/ "/home/gslab/Desktop/Avocado" + File.separator + "logs" + File.separator + "suites.log");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                fAppender = new RollingFileAppender(layout, logFile.getAbsolutePath());
                System.out.println("Path for log file: " + logFile.getAbsolutePath());

                suitLogger.addAppender(fAppender);


            } catch (Exception e) {
                System.out.println("Failed to created logger ..  !!!");
                e.printStackTrace();
            } finally {
                isSuiteLoggingDone = true;
            }
        }
    }

    /**
     * This will reset the suite level logger.
     */
    @AfterSuite(alwaysRun = true)
    public void resetSuiteLogger() {
        isSuiteLoggingDone = false;
    }
}


