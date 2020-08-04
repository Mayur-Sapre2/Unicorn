package com.gslab.unicorn.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 */

public interface SeleniumElementsInterface {

    boolean verifyText(String actualText, String expText) throws Exception;

    boolean verifyTextCaseSensitive(String actualText, String expText) throws Exception;

    boolean isElementDisplayedAndEnabled(WebElement element, boolean displayed, boolean enabled) throws Exception;

    boolean isElementDisplayed(WebElement element);

    boolean isElementEnabled(WebElement element);

    void waitForWebElementIsDisplayed(final WebElement element, final boolean waitForNotDisplayed) throws Exception;

    void waitForWebElementIsDisplayed(final WebElement element) throws Exception;

    void waitForWebElementIsDisplayed(final WebElement element, int timeOutInSec, final boolean waitForNotDisplayed) throws Exception;

    void waitForWebElementIsDisplayed(final WebElement element, int timeOutInSec) throws Exception;

    void clickElementAfterFocus(WebElement element);

    void clickElementAfterFocus(WebElement element, boolean scrollToBottom) throws Exception;

    void chooseSelectValue(final WebElement dropDownId, final String value) throws Exception;

    void clickButtonByText(String buttonText) throws Exception;

    boolean isInputChecked(WebElement element);

    void waitForWebElementIsEnabled(WebElement webElement, final boolean waitForNotEnabled) throws Exception;

    void waitForWebElementIsEnabled(WebElement webElement) throws Exception;

    void waitForWebElementIsEnabled(WebElement webElement, int timeOutInSec) throws Exception;

    void waitAndClickInputButtonById(final String buttonId) throws Exception;

    void waitForItemInOptionSelect(final WebElement webElement, final String entry, final boolean isDisplayName) throws Exception;

    void doubleClickElementAfterFocus(WebElement element, boolean scrollToBottom);

    void doubleClickElementAfterFocus(WebElement element);

    boolean isElementLoading() throws Exception;

    void waitForWebElementIsDisplayed(final By elementBy, final boolean waitForNotDisplayed) throws Exception;

    void waitForWebElementIsDisplayed(final By elementBy) throws Exception;

    void waitForWebElementIsDisplayed(final By elementBy, int timeOutInSec) throws Exception;

    boolean isElementDisplayed(By byElement);

    /**
     * Hit enter key on the elements
     *
     * @param element WebElement
     */
    void hitEnter(WebElement element);

    /**
     * Get selenium elements's attribute
     *
     * @param byElement By elements byLocator
     * @param attribute attribute name
     * @return attribute value
     */
    public String getAttribute(By byElement, String attribute);

    /**
     * Get selenium elements's attribute
     *
     * @param byElement WebElement
     * @param attribute attribute name
     * @return attribute value
     */
    public String getAttribute(WebElement byElement, String attribute);
}

