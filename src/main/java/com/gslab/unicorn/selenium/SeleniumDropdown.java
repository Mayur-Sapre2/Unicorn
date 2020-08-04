package com.gslab.unicorn.selenium;

import com.gslab.unicorn.exceptions.UnicornSeleniumException;
import com.gslab.unicorn.logger.LogManager;
import com.gslab.unicorn.utils.Config;
import com.gslab.unicorn.driver.web.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 */

public class SeleniumDropdown implements SeleniumDropdownInterface {

    /**
     * @param selectElement: A By for a select elements
     */
    public void waitForElement(WebElement selectElement) {

        try {

            WebDriverWait wait = new WebDriverWait(WebDriverManager.getDriver(), Long.parseLong(Config.getSeleniumTimeOut()));
            wait.until(ExpectedConditions.visibilityOf(selectElement));

        } catch (TimeoutException exception) {
            exception.printStackTrace();
            LogManager.error("Timeout waiting for Dropdown: " + selectElement);
            throw new UnicornSeleniumException("Timeout waiting for Dropdown: " + selectElement);
        } catch (NullPointerException exception) {
            exception.printStackTrace();
            LogManager.error("Null pointer Exception:  " + selectElement);
            throw new UnicornSeleniumException("Null pointer Exception:  " + selectElement);
        } catch (WebDriverException exception) {
            exception.printStackTrace();
            LogManager.error("Webdriver Exceptiion:  " + selectElement);
            throw new UnicornSeleniumException("Webdriver Exceptiion:  " + selectElement);
        } catch (Exception exception) {
            LogManager.error("Exception while waiting for Dropdown: " + selectElement, exception);
            throw new UnicornSeleniumException("Exception while waiting for Dropdown: " + selectElement);
        }
    }

    /**
     * Selects an option from a drop down control based on option text
     *
     * @param dropDownId: the id for the combo box
     * @throws Exception
     */
    public void chooseSelectOption(final String dropDownId, final String text) throws Exception {
        new Select(WebDriverManager.getDriver().findElement(By.id(dropDownId))).selectByVisibleText(text);
    }


    /**
     * Returns the text of the first selected option in the specified select elements.
     *
     * @param selectElement A By for a select elements.
     * @return Text of the first selected option.
     */
    public String getFirstSelectedOptionText(WebElement selectElement) {

        String text = null;
        try {
            Select seleniumSelect = new Select(selectElement);
            text = seleniumSelect.getFirstSelectedOption().getText();
        } catch (NoSuchElementException exception) {
            LogManager.error("No selected elements in Dropdown: " + selectElement, exception);
            throw new UnicornSeleniumException("No selected elements in Dropdown: " + selectElement);
        }
        return text;
    }


    /**
     * Returns the list of WebElements for the options in the specified select elements.
     *
     * @param selectElement A By for a select elements.
     * @return A List of the options.
     */
    public List<WebElement> getOptions(WebElement selectElement) {

        List<WebElement> elements = null;
        try {
            Select seleniumSelect = new Select(selectElement);
            elements = seleniumSelect.getOptions();
        } catch (NoSuchElementException exception) {
            LogManager.error("No such Dropdown: " + selectElement, exception);
            throw new UnicornSeleniumException("No such Dropdown: " + selectElement);
        }
        return elements;
    }


    /**
     * Selects an option by index number. Indexes start at 0.
     *
     * @param selectElement A By for a select elements.
     * @param index         The index of an option.
     */
    public void selectByIndex(WebElement selectElement, int index) {

        try {
            Select seleniumSelect = new Select(selectElement);
            seleniumSelect.selectByIndex(index);
        } catch (NoSuchElementException exception) {
            LogManager.error("Index Option: " + index + " not found in Dropdown: " + selectElement, exception);
            throw new UnicornSeleniumException("Index Option: " + index + " not found in Dropdown: " + selectElement);
        }
    }


    /**
     * Selects an option by value.
     *
     * @param selectElement A By for a select elements.
     * @param value         The value of an option.
     */
    public void selectByValue(WebElement selectElement, String value) {
        try {
            Select seleniumSelect = new Select(selectElement);
            seleniumSelect.selectByValue(value);
        } catch (NoSuchElementException exception) {
            LogManager.error("Value Option:  " + value + " not found in Dropdown: " + selectElement, exception);
            throw new UnicornSeleniumException("Value Option:  " + value + " not found in Dropdown: " + selectElement);
        }
    }


    /**
     * Selects an option by text.
     *
     * @param selectElement A By for a select elements.
     * @param text          The text of an option.
     */
    public void selectByVisibleText(WebElement selectElement, String text) throws Exception {
        try {
            SeleniumElementsInterface seleniumElements = new SeleniumElements();
            //seleniumElements.waitForWebElementIsDisplayed(selectElement, false);
            Select seleniumSelect = new Select(selectElement);
            seleniumSelect.selectByVisibleText(text);
        } catch (NoSuchElementException exception) {
            LogManager.error("Text Option: " + text + " not found in Dropdown: " + selectElement, exception);
            throw new UnicornSeleniumException("Text Option: " + text + " not found in Dropdown: " + selectElement);
        }
    }


    /**
     * Peforms a selection via JavaScript.
     *
     * @param selectElement A By for a select elements.
     * @param idx           The index of the option to select.
     */
    private void javaScriptSelectByIndex(WebElement selectElement, int idx) {

        JavascriptExecutor js = (JavascriptExecutor) WebDriverManager.getDriver();
        waitForElement(selectElement);
        // Manually call onchange(), as the event is not otherwise fired when the
        // index is directly changed.
        js.executeScript("arguments[0].selectedIndex = " + idx + "; " +
                "if(arguments[0].onchange !== null && arguments[0].onchange !== undefined) { arguments[0].onchange(); }", selectElement);
    }


    /**
     * de-Selects an option by index number. Indexes start at 0.
     *
     * @param deselectElement A By for a deselect elements.
     * @param index           The index of an option.
     */
    public void deselectByIndex(WebElement deselectElement, int index) {

        try {
            Select seleniumDeselect = new Select(deselectElement);
            seleniumDeselect.deselectByIndex(index);
        } catch (NoSuchElementException exception) {
            LogManager.error("Index Option: " + index + " not found in Dropdown: " + deselectElement, exception);
            throw new UnicornSeleniumException("Index Option: " + index + " not found in Dropdown: " + deselectElement);
        }
    }


    /**
     * de-Selects an option by value.
     *
     * @param deselectElement A By for a deselect elements.
     * @param value           The value of an option.
     */
    public void deselectByValue(WebElement deselectElement, String value) {
        try {
            Select seleniumDeselect = new Select(deselectElement);
            seleniumDeselect.deselectByValue(value);
        } catch (NoSuchElementException exception) {
            LogManager.error("Value Option:  " + value + " not found in Dropdown: " + deselectElement, exception);
            throw new UnicornSeleniumException("Value Option:  " + value + " not found in Dropdown: " + deselectElement);
        }
    }


    /**
     * de-selects an option by text.
     *
     * @param deselectElement A By for a deselect elements.
     * @param text            The text of an option.
     */
    public void deselectByVisibleText(WebElement deselectElement, String text) {
        try {
            Select seleniumDeselect = new Select(deselectElement);
            seleniumDeselect.deselectByVisibleText(text);
        } catch (NoSuchElementException exception) {
            LogManager.error("Text Option: " + text + " not found in Dropdown: " + deselectElement, exception);
            throw new UnicornSeleniumException("Text Option: " + text + " not found in Dropdown: " + deselectElement);
        }
    }

    /**
     * opens dropdown
     *
     * @param element
     */
    public void openDropDown(WebElement element) {

        try {
            if (element.isDisplayed()) {
                element.click();
            }

        } catch (ElementNotVisibleException exception) {
            LogManager.error("Dropdown: " + element + " is not Displayed.", exception);
            throw new UnicornSeleniumException("Dropdown: " + element + " is not Displayed.");
        }

    }
}
