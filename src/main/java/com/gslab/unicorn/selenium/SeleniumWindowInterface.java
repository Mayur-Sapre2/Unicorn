package com.gslab.unicorn.selenium;

import org.openqa.selenium.WebElement;

import java.util.Set;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 */

public interface SeleniumWindowInterface {

    public Set<String> getAllWindowsHandle();

    public String getCurrentWindowHandle();

    public String switchWindowHandleToAnotherWindow() throws Exception;

    public String getHandleToWindowContainingElement(WebElement byElement, String text);

    public String getHandleToWindowWithTitle(String text);

    public void waitForAlert(final boolean waitForNotDisplayed, final boolean dismissAlert) throws Exception;

    public void waitForPopupWindowIsDisplayed(final WebElement elementBy, final String text) throws Exception;

    public void waitForPopupWindowWithTitle(final String text) throws Exception;

    //public SeleniumWindowState getCurrentWindowState() throws Exception;

    // public void returnToWindowState(SeleniumWindowState state) throws Exception;

    public void acceptOrDismissAlert(boolean isAccept) throws Exception;


}
