package com.gslab.unicorn.selenium;

import com.gslab.unicorn.driver.web.WebDriverManager;
import com.gslab.unicorn.elements.SeleniumElementLocatorInterface;
import com.gslab.unicorn.elements.SeleniumElementLocator;
import com.gslab.unicorn.exceptions.UnicornSeleniumException;
import com.gslab.unicorn.logger.LogManager;
import com.gslab.unicorn.utils.Config;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 */

public class SeleniumDialog implements SeleniumDialogInterface {
    private SeleniumElementsInterface seleniumElements;

    public SeleniumDialog() {
        seleniumElements = new SeleniumElements();
    }

    public void switchToDefaultContent() {
        try {
            WebDriverManager.getDriver().switchTo().defaultContent();
        } catch (Exception ex) {
            ex.printStackTrace();
            //LoggingHelper.logError("Unable to switch to default content");
            throw new UnicornSeleniumException("Unable to switch to default content", ex);
        }
    }

    /**
     * Switches to the content area of the current dialog.
     */
    public boolean switchToDialogContentArea(By byIframe) {
        try {
            List<WebElement> iframes = WebDriverManager.getDriver().findElements(byIframe);
            if (iframes.size() == 1) {
                WebDriverManager.getDriver().switchTo().frame(iframes.get(0));
                return true;
            }
        } catch (Exception ex) {
            // always always switch back to the root frame.
            switchToDefaultContent();
        }
        return false;
    }

    public void switchToIframe(By iframe) {
        WebDriverManager.getDriver().switchTo().frame(WebDriverManager.getDriver().findElement(iframe));

    }

    /**
     * Switches to the popup dialog with the specified. Note, if this fails to find the dialog it will switch back to
     * default content, which can be bad if you are already in a dialog.
     *
     * @return A boolean, true if the dialog was found, false otherwise.
     */
    public boolean switchToPopupDialogWithTitle(By byIframeElement, String expectedTitle, boolean isTitleVisible) {
        if (validateDialogTitle(byIframeElement, expectedTitle, isTitleVisible)) {
            return true;
        }

        try {
            List<WebElement> iframes = WebDriverManager.getDriver().findElements(byIframeElement);
            for (WebElement iframe : iframes) {
                //LoggingHelper.logger("switching to iFrame from switchToPopupDialogWithTitle...");
                WebDriverManager.getDriver().switchTo().frame(iframe);
                if (validateDialogTitle(byIframeElement, expectedTitle, isTitleVisible)) {
                    return true;
                }
            }
        } catch (Exception ex) {
            LogManager.error("Unable to switch to dialog with title " + expectedTitle);
            // always always switch back to the root frame.
            switchToDefaultContent();
        }

        return false;
    }

    /**
     * Switches to the main dialog with the specified
     *
     * @return A boolean, true if the dialog was found, false otherwise.
     */
    public boolean switchToMainDialogWithTitle(By byIframeElement, By mainTitleElement, String title) {
        try {
            WebElement iframe = WebDriverManager.getDriver().findElement(byIframeElement);
            if (iframe != null) {
                WebElement dialogTitle = WebDriverManager.getDriver().findElement(mainTitleElement);
                if (title.equals(dialogTitle.getText().trim())) {
                    // LoggingHelper.logger("switching to iFrame from switchToMainDialogWithTitle ...");
                    WebDriverManager.getDriver().switchTo().frame(iframe);
                    return true;
                }
            }
        } catch (Exception ex) {
            LogManager.error("Unable to switch to main dialog with title " + title);
            // always switch back to the root frame.
            switchToDefaultContent();
        }
        return false;
    }

    /**
     * Returns false if the currently active elements is not a dialog or its title does not match the specified string.
     *
     * @return True if the title of the dialog in the currently selected frame matches the expected title.
     */
    public boolean validateDialogTitle(By byElement, String expectedTitle, boolean isTitleVisible) {
        try {
            WebElement dialog = WebDriverManager.getDriver().findElement(byElement);
            String text;
            if (dialog.isDisplayed()) {
                text = dialog.getText();
            } else {
                // if the dialog title exists but is not visible, use javascript to get the title text
                text = (isTitleVisible) ? "" : (String) ((JavascriptExecutor) WebDriverManager.getDriver()).executeScript("return arguments[0].textContent", dialog);
            }
            return expectedTitle.equals(text.trim());
        } catch (NoSuchElementException ex) {
            // not a dialog or doesn't have a title field
            return false;
        }
    }


    public boolean switchToPopupDialogWithTitle(By byIframeElement, String expectedTitle) {
        return switchToPopupDialogWithTitle(byIframeElement, expectedTitle, true);
    }


    /**
     * Switch to the main dialog with the specified title.
     * Throws exception if the title is not found.
     *
     * @param title
     */
    public void switchToMainDialogWithTitleThrows(By byIframeElement, By mainTitleElement, String title) {
        if (!switchToMainDialogWithTitle(byIframeElement, mainTitleElement, title)) {
            throw new UnicornSeleniumException("could not switch to dialog with title \"" + title + "\"");
        }
    }

    /**
     * Returns true if there is an iFrame dialog on the page
     *
     * @return A boolean, true if there is a dialog iframe on the page, false otherwise.
     */
    public boolean isDialogOnPage(By byDialogElement) {
        switchToDefaultContent();
        WebElement frame = WebDriverManager.getDriver().findElement(byDialogElement);
        if (!frame.isDisplayed()) {
            return false;
        } else {
            return true;
        }
    }
}
