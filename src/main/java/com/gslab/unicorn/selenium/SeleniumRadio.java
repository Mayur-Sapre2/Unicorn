package com.gslab.unicorn.selenium;

import com.gslab.unicorn.exceptions.UnicornSeleniumException;
import com.gslab.unicorn.logger.LogManager;
import org.openqa.selenium.WebElement;

import java.util.NoSuchElementException;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 */

public class SeleniumRadio implements SeleniumRadioInterface {

    /**
     * Selects Radio button if not already selected
     *
     * @param element : elements to be selected
     */
    public void selectRadioButton(WebElement element) {
        try {
            if (isRadioDisplayed(element) && isRadioEnabled(element)) {
                if (!isRadioSelected(element)) {
                    element.click();
                    LogManager.info("Radio Button : " + element + " clicked.");
                }
            }
        } catch (NoSuchElementException exception) {
            LogManager.error("Radio Button: " + element + " not found.", exception);
            throw new UnicornSeleniumException("Radio Button: " + element + " not found.", exception);
        }
    }


    /**
     * returns true if elements is displayed, false otherwise
     *
     * @param element : elements to be verified
     * @return boolean result
     */
    public boolean isRadioDisplayed(WebElement element) {
        boolean status = false;
        try {
            status = element.isDisplayed();
            LogManager.info("Radio Button : " + element + " Display Status: " + status);
        } catch (NoSuchElementException exception) {
            LogManager.error("Radio Button: " + element + " not found.", exception);
            throw new UnicornSeleniumException("Radio Button: " + element + " not found.", exception);
        }
        return status;
    }

    /**
     * returns true if radio bustton is enabled, false otherwise
     *
     * @param element : elements to be verified
     * @return boolean result
     */
    public boolean isRadioEnabled(WebElement element) {
        boolean status = false;
        try {
            if (isRadioDisplayed(element)) {
                status = element.isEnabled();
            }
            LogManager.info("Radio Button : " + element + " Enable Status: " + status);
        } catch (NoSuchElementException exception) {
            LogManager.error("Radio Button: " + element + " not found.", exception);
            throw new UnicornSeleniumException("Radio Button: " + element + " not found.", exception);
        }
        return status;
    }


    /**
     * returns true if radio button is selected, false otherwise
     *
     * @param element
     * @return boolean result
     */
    public boolean isRadioSelected(WebElement element) {

        boolean status = false;
        try {
            if (isRadioDisplayed(element)) {
                status = element.isSelected();
            }
            LogManager.info("Radio Button : " + element + " Select Status: " + status);
        } catch (NoSuchElementException exception) {
            LogManager.error("Radio Button: " + element + " not found.", exception);
            throw new UnicornSeleniumException("Radio Button: " + element + " not found.", exception);
        }
        return status;
    }
}
