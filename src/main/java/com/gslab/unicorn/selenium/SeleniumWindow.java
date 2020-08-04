package com.gslab.unicorn.selenium;

import com.gslab.unicorn.driver.web.WebDriverManager;
import com.gslab.unicorn.elements.SeleniumElementLocatorInterface;
import com.gslab.unicorn.elements.SeleniumElementLocator;
import com.gslab.unicorn.exceptions.UnicornException;
import com.gslab.unicorn.exceptions.UnicornSeleniumException;
import com.gslab.unicorn.logger.LogManager;
import com.gslab.unicorn.utils.Config;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 */

public class SeleniumWindow implements SeleniumWindowInterface {
    // private Log log = new Log();
    private SeleniumElementsInterface seleniumElements;
    private SeleniumDialogInterface seleniumDialog;

    public SeleniumWindow() {
        seleniumElements = new SeleniumElements();
        seleniumDialog = new SeleniumDialog();
    }

    /**
     * Get current window handle
     *
     * @return current window state
     */
    public String getCurrentWindowHandle() {
        return WebDriverManager.getDriver().getWindowHandle();
    }

    /**
     * Get all opened windows state
     *
     * @return Set of all opened windows state
     */
    public Set<String> getAllWindowsHandle() {
        return WebDriverManager.getDriver().getWindowHandles();
    }

    /**
     * Switch to another window state(to the 2nd opened window if more than 2 windows has opened) other than current window
     *
     * @return switched window handle
     * @throws Exception if no another instance of window found.
     */
    public String switchWindowHandleToAnotherWindow() throws Exception {
        String currentBrowserSession = getCurrentWindowHandle();

        Set openedBrowserSessions = getAllWindowsHandle();
        Iterator iterator = openedBrowserSessions.iterator();

        if (openedBrowserSessions.size() < 2)
            throw new UnicornException("only single instance of window's has opened!");

        while (iterator.hasNext()) {
            String currentWindowReference = iterator.next().toString();
            if (!currentWindowReference.equals(currentBrowserSession)) {
                WebDriverManager.getDriver().switchTo().window(currentWindowReference);
                return currentBrowserSession;
            }
        }
        return null;
    }

    /**
     * Returns a handle to a new window. Either byElement existing, or by the text contained in the elements
     *
     * @param byElement the elements to check
     * @param text      if null, just tests for existance, if not will check that it contains this text
     * @return handle to window if it is found, or null if not.
     */
    public String getHandleToWindowContainingElement(WebElement byElement, String text) {
        String parentWindowHandle = WebDriverManager.getDriver().getWindowHandle();
        String windowHandle = null;
        try {
            Set<String> winHandleList = WebDriverManager.getDriver().getWindowHandles();
            for (String handle : winHandleList) {
                if (handle.equals(parentWindowHandle))
                    continue;

                WebDriver testdriver = WebDriverManager.getDriver().switchTo().window(handle);

                if (text == null) {
                    if (seleniumElements.isElementDisplayed(byElement))
                        return handle;
                } else {
                    if (byElement.getText().equals(text))
                        return handle;
                }

            }
        } catch (Exception e) {
            LogManager.error("Exception while getting handle to window", e);
            throw new UnicornSeleniumException("Exception while getting handle to window", e);
        } finally {
            WebDriverManager.getDriver().switchTo().window(parentWindowHandle);
        }

        return windowHandle;
    }

    /**
     * Returns a handle to a new window with the given text in it's title. Will never return the current window.
     *
     * @param text if null, just tests for existance, if not will check that it contains this text
     * @return handle to window if it is found, or null if not.
     */
    public String getHandleToWindowWithTitle(String text) {
        String parentWindowHandle = WebDriverManager.getDriver().getWindowHandle();
        String windowHandle = null;
        try {
            Set<String> winHandleList = WebDriverManager.getDriver().getWindowHandles();
            for (String handle : winHandleList) {
                if (handle.equals(parentWindowHandle))
                    continue;

                WebDriver testdriver = WebDriverManager.getDriver().switchTo().window(handle);

                if (testdriver.getTitle().contains(text))
                    return handle;
            }
        } catch (Throwable e) {
            // do nothing...
        } finally {
            WebDriverManager.getDriver().switchTo().window(parentWindowHandle);
        }

        return windowHandle;
    }


    /**
     * Waits for an alert to appear, or if waitforNotDisplayed is true wait for it not to display.
     *
     * @param waitForNotDisplayed If true will wait for an alert to not be displayed.
     * @param dismissAlert        Dismiss any alert that appears
     */
    public void waitForAlert(final boolean waitForNotDisplayed, final boolean dismissAlert) throws Exception {

        (new WebDriverWait(WebDriverManager.getDriver(), Long.parseLong(Config.getSeleniumTimeOut())))
                .until(new ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver d) {
                        Boolean isAlertPresent = false;
                        try {
                            Alert alt = d.switchTo().alert();
                            if (dismissAlert) {
                                alt.dismiss();
                            }
                            isAlertPresent = true;
                        } catch (NoAlertPresentException e) {
                            isAlertPresent = false;
                        }

                        if (waitForNotDisplayed) {
                            return !isAlertPresent;
                        } else {
                            return isAlertPresent;
                        }
                    }
                });

    }

    /**
     * Will wait until a window containing the given object (or that object and a text value) is displayed
     *
     * @param elementBy An elements to test, this could be By.id, or even By.xpath... Any of the By methods
     * @param text,     if not null will contain elements's getText to text
     * @throws Exception If there is an error.
     */
    public void waitForPopupWindowIsDisplayed(final WebElement elementBy, final String text) throws Exception {

        (new WebDriverWait(WebDriverManager.getDriver(), Long.parseLong(Config.getSeleniumTimeOut())))
                .until(new ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver d) {
                        if (getHandleToWindowContainingElement(elementBy, text) != null)
                            return true;
                        else
                            return false;
                    }
                });
    }

    /**
     * Will wait until a window with the given title is displayed
     *
     * @param text contained in title
     * @throws Exception If there is an error.
     */
    public void waitForPopupWindowWithTitle(final String text) throws Exception {

        (new WebDriverWait(WebDriverManager.getDriver(), Long.parseLong(Config.getSeleniumTimeOut())))
                .until(new ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver d) {
                        if (getHandleToWindowWithTitle(text) != null)
                            return true;
                        else
                            return false;
                    }
                });
    }


    /**
     * Gets the root elements in the current context and returns an GUID that identifies that web elements.
     */
    private String getRootId() {
        return ((RemoteWebElement) WebDriverManager.getDriver().findElement(By.xpath("/*"))).getId();
    }

    /**
     * Recursively look thru iframes to find the path to the iframe with the given source
     *
     * @param rootId
     * @param currentPath
     * @return
     * @throws Exception
     */
    private List<String> findIframeWithMatchingRootId(String rootId, List<String> currentPath) {
        SeleniumDialogInterface seleniumDialog = new SeleniumDialog();
        if (getRootId().equals(rootId)) {
            // If we have found our iframe then just return
            return currentPath;
        } else {

            try {
                // If not loop thru each iframe that is a child of the current iframe and test it.
                for (String iframe : findEnabledIframes()) {
                    // Enter each iframe
                    seleniumDialog.switchToIframe(By.id(iframe));
                    ArrayList<String> testPath = new ArrayList<String>(currentPath);
                    testPath.add(iframe);

                    // test
                    List<String> newPath = findIframeWithMatchingRootId(rootId, testPath);

                    // switch back to the root iframe
                    switchToIframe(currentPath);

                    // If we got anything we have found the right page, otherwise try again.
                    if (!newPath.isEmpty()) {
                        return newPath;
                    } else {
                        continue;
                    }
                }
            } catch (Exception e) {
            }

            return new ArrayList<String>();
        }
    }

    /**
     * Finds the enabled iframes on a given page at the current iframe level.
     *
     * @return
     */
    private List<String> findEnabledIframes() {
        List<WebElement> iframes = WebDriverManager.getDriver().findElements(By.xpath("//iframe"));
        List<String> iframeList = new ArrayList<String>();
        for (WebElement frame : iframes) {
            if (frame.isDisplayed()) {
                iframeList.add(frame.getAttribute("id"));
            }
        }
        return iframeList;
    }

    /**
     * Switches to an iframe with a given path.
     *
     * @param iframePath
     * @throws Exception
     */
    private void switchToIframe(List<String> iframePath) throws Exception {
        seleniumDialog.switchToDefaultContent();

        if (iframePath.isEmpty()) {
            return;
        }

        for (String frameId : iframePath) {
            seleniumDialog.switchToIframe(By.id(frameId));
        }
    }

    /**
     * Accept a alert if true else dismiss.
     *
     * @param isAccept
     */
    public void acceptOrDismissAlert(boolean isAccept) throws Exception {
        if (isAccept) {
            waitForAlert(false, false);
            WebDriverManager.getDriver().switchTo().alert().accept();
        } else {
            waitForAlert(false, true);
        }
    }
}
