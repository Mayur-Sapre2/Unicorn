package com.gslab.unicorn.utils;

import com.gslab.unicorn.logger.LogManager;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.monte.media.FormatKeys.EncodingKey;
import static org.monte.media.FormatKeys.FrameRateKey;
import static org.monte.media.FormatKeys.KeyFrameIntervalKey;
import static org.monte.media.FormatKeys.MIME_AVI;
import static org.monte.media.FormatKeys.MediaType;
import static org.monte.media.FormatKeys.MediaTypeKey;
import static org.monte.media.FormatKeys.MimeTypeKey;
import static org.monte.media.VideoFormatKeys.*;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 * <p>
 * <p>
 * ScreenRecordingManager provides the functioning to start, save and stop the screen recording per test case.
 */

public class ScreenRecordingManager {
    public static String testName;
    private static boolean isInitDone = false;
    private static String recordingName = null;
    private static boolean isTestFailed;
    private static File file;
    private ScreenRecorder screenRecorder;

    public ScreenRecordingManager() {
        if (!isInitDone) {
            try {
                System.out.println("Setting-up default configuration");
                Config.initConfig(null, true);
            } catch (Exception e) {
                e.printStackTrace();
                Assert.fail("Exception while setting up default configuration", e);
            }
            isInitDone = true;
        }
    }

    /**
     * User to set customized unicorn properties
     *
     * @param configFilePath config file
     */
    public ScreenRecordingManager(String configFilePath) {
        try {
            if (configFilePath != null && !configFilePath.trim().equals("")) {
                System.out.println("Setting config file");
                Config.initConfig(configFilePath, false);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to read properties file: " + configFilePath, e);
        }
        if (!isInitDone) {
            isInitDone = true;
        }
    }

    /**
     * This will start screen recording before test case execution start
     *
     * @throws IOException
     * @throws AWTException
     * @throws Exception
     */
    @BeforeMethod(alwaysRun = true, dependsOnMethods = "getTestName")
    public void startRecording() throws IOException, AWTException, Exception {
        if (Config.getTestingType() != null && !Config.getTestingType().equalsIgnoreCase("api")) {

            String screenRecorderPath = Config.getReportsLocation() + File.separator + "screenrecordings";

            file = new File(screenRecorderPath + File.separator + this.getClass().getSimpleName());
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = screenSize.width;
            int height = screenSize.height;
            Rectangle captureSize = new Rectangle(0, 0, width, height);
            GraphicsConfiguration gc = GraphicsEnvironment
                    .getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .getDefaultConfiguration();


            this.screenRecorder = new UnicornSpecializedScreenRecorder(gc, captureSize,
                    new org.monte.media.Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                    new org.monte.media.Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            DepthKey, 24, FrameRateKey, Rational.valueOf(15),
                            QualityKey, 1.0f,
                            KeyFrameIntervalKey, 15 * 60),
                    new org.monte.media.Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black",
                            FrameRateKey, Rational.valueOf(30)),
                    null, file, recordingName);
            this.screenRecorder.start();
        }
    }

    /**
     * Stop screen recorder once test case execution done
     */
    @AfterMethod(alwaysRun = true, dependsOnMethods = "getTestResult")
    public void stopRecording() {
        try {
            if (Config.getTestingType() != null && !Config.getTestingType().equalsIgnoreCase("api")) {
                if (this.screenRecorder != null)
                    this.screenRecorder.stop();
                else
                    System.out.println("ScreenRecorder instance is null !!. Not stopping it");

                if (!isTestFailed) {
                    System.out.println(file + File.separator + recordingName);
                    File createdRecordingFile = new File(file + File.separator + recordingName + ".avi");
                    File recordedFileClassDirectory = new File(file.getParentFile().toString());
                    if (createdRecordingFile.exists()) {
                        if (!createdRecordingFile.delete())
                            LogManager.error("Failed to delete recording file: " + file + File.separator + recordingName);
                    }

                    if (recordedFileClassDirectory.exists() && CommonUtils.isDirEmpty(recordedFileClassDirectory.toPath())) {
                        if (!recordedFileClassDirectory.delete())
                            LogManager.error("Failed to delete directory: " + recordedFileClassDirectory.getPath());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return test execution result
     *
     * @param iTestResult
     */
    @AfterMethod(alwaysRun = true)
    public void getTestResult(ITestResult iTestResult) {
        switch (iTestResult.getStatus()) {
            case ITestResult.FAILURE:
                isTestFailed = true;
                break;
            case ITestResult.SUCCESS:
                isTestFailed = false;
                break;
        }
    }


    /**
     * This will use to create name for screen recorder file with test case name.
     *
     * @param method Method
     */
    @BeforeMethod(alwaysRun = true)
    public void getTestName(Method method) {
        testName = method.getName();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");
        recordingName = method.getName() + "_" + CommonUtils.getCurrentTimeStamp() + "-" + dateFormat.format(new Date());
    }

}
