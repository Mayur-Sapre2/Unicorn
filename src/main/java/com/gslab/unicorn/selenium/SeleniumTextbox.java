package com.gslab.unicorn.selenium;

import com.gslab.unicorn.driver.web.WebDriverManager;
import com.gslab.unicorn.exceptions.UnicornSeleniumException;
import com.gslab.unicorn.logger.LogManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 */

public class SeleniumTextbox implements SeleniumTextboxInterface {

    /**
     * Clears text box value and sends text in text box
     *
     * @param element Web elements
     * @param value   text to enter
     */
    public void sendKeys(WebElement element, String value) {
        try {
            element.clear();
            // elements.sendKeys(Keys.TAB);
            element.sendKeys(value);
        } catch (NoSuchElementException exception) {
            LogManager.error("Failed on enter " + value + " in a checkbox.." + exception.getMessage(), exception);
            throw new UnicornSeleniumException("Text Box: " + element + "not found.");
        } catch (Exception e) {
            LogManager.error("Failed to enter " + value + " in a checkbox.." + e.getMessage(), e);
            throw new UnicornSeleniumException("Failed to enter '" + value + "' in Text Box: '" + element + "'.", e);
        }
    }


    /**
     * Sends text in text box
     *
     * @param element        WebElement
     * @param clearBeforeSet is set false if not to clear
     * @param value          to enter
     */
    public void sendKeys(WebElement element, boolean clearBeforeSet, String value) {
        try {
            if (clearBeforeSet) {
                element.clear();
            }
            element.sendKeys(value);
        } catch (NoSuchElementException exception) {
            LogManager.error("Failed to enter " + value + " in a text.." + exception.getMessage(), exception);
            throw new UnicornSeleniumException("Text Box: " + element + "not found.");
        } catch (Exception e) {
            LogManager.error("Failed to enter " + value + " in a text.." + e.getMessage(), e);
            throw new UnicornSeleniumException("Failed to enter '" + value + "' in Text Box: '" + element + "'.", e);
        }
    }

    /**
     * Sends date in text box
     *
     * @param element        WebElement
     * @param clearBeforeSet is set false if not to clear
     * @param date           value in date format
     */
    public void sendKeysDate(WebElement element, boolean clearBeforeSet, Date date) {
        try {
            if (clearBeforeSet) {
                element.clear();
            }
            SimpleDateFormat format = new SimpleDateFormat();
            element.sendKeys(format.format(date));
        } catch (NoSuchElementException exception) {
            LogManager.error("Failed to enter " + date + " in a checkbox.." + exception.getMessage(), exception);
            throw new UnicornSeleniumException("Text Box: " + element + "not found.");
        } catch (Exception e) {
            LogManager.error("Failed to enter " + date + " in a checkbox.." + e.getMessage(), e);
            throw new UnicornSeleniumException("Failed to enter '" + date + "' in Text Box: '" + element + "'.", e);
        }
    }


    /**
     * This method is useful when you want to set value in Attribute value,
     * without loosing focus from text field after cleaning it.
     *
     * @param element WebElement
     * @param text    Text to be enter in text box.
     */
    public void setTextByAttrValue(WebElement element, String text) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) WebDriverManager.getDriver();
            js.executeScript("arguments[0].focus();arguments[0].setAttribute('value',arguments[1])", element, text);
        } catch (Exception e) {
            LogManager.error("Failed to enter " + text + " in a checkbox.." + e.getMessage(), e);
            throw new UnicornSeleniumException("Failed to enter '" + text + "' in Text Box: '" + element + "'.", e);
        }
    }

    /**
     * Scrolls the specified text area to the bottom.
     *
     * @param textAreaId The id of the text area to scroll
     */
    public void scrollTextAreaToBottom(String textAreaId) {
        WebElement textArea = WebDriverManager.getDriver().findElement(By.id(textAreaId));
        JavascriptExecutor js = (JavascriptExecutor) WebDriverManager.getDriver();
        try {
            js.executeScript("arguments[0].scrollTop = arguments[0].scrollHeight;", textArea);
        } catch (Exception e) {
            throw new UnicornSeleniumException("Exception while scrolling text area using javascript", e);
        }
    }
}
