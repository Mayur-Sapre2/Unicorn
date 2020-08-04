package com.gslab.unicorn.utils;

import com.gslab.unicorn.driver.web.WebDriverManager;
import com.gslab.unicorn.exceptions.UnicornIOException;
import com.gslab.unicorn.logger.LogManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 */

public class SeleniumUtils {

    /**
     * Capture screenshot
     *
     * @param locationPath   directory path, where screenshot have to store
     * @param screenshotName screenshot name
     * @return String screenshot path
     */
    public String captureScreenshot(String locationPath, String screenshotName) {
        String directory_name = null;
        String screenshotPath = null;
        try {
            if (Config.getReportsLocation().equalsIgnoreCase("") || Config.getReportsLocation() == null) {
                throw new UnicornIOException("'work.location' property is not found in '.config' file.");
            }
            directory_name = locationPath + File.separator;
            File scrFile = ((TakesScreenshot) WebDriverManager.getDriver()).getScreenshotAs(OutputType.FILE);
            try {
                if (!new File(directory_name).exists()) {
                    new File(directory_name).mkdir();
                }
            } catch (Exception e) {
                LogManager.error("Failed to create directory -" + directory_name + ". " + e);
                throw new UnicornIOException("Failed to create directory -\" + folder_name + \". \"", e);
            }
            try {
                screenshotPath = directory_name + screenshotName + "_" + CommonUtils.getCurrentTimeStamp() + ".png";
                FileUtils.copyFile(scrFile, new File(screenshotPath));
            } catch (Exception e) {
                LogManager.error("Failed to copy file !" + e);
                throw new UnicornIOException("Failed to copy file !", e);
            }
        } catch (Exception e) {
            LogManager.error("Debug: screenshot: failed: End " + e);
        }
        return new File(screenshotPath).getAbsolutePath();
    }
}
