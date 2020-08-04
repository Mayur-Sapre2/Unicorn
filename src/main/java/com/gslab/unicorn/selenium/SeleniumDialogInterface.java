package com.gslab.unicorn.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 */

public interface SeleniumDialogInterface {
    public void switchToDefaultContent();

    public void switchToIframe(By iframe);

    public boolean switchToDialogContentArea(By byIframe);

    public boolean switchToPopupDialogWithTitle(By byIframeElement, String expectedTitle, boolean isTitleVisible);

    public boolean switchToMainDialogWithTitle(By byIframeElement, By mainTitleElement, String title);

    public boolean validateDialogTitle(By byElement, String expectedTitle, boolean isTitleVisible);

    public boolean switchToPopupDialogWithTitle(By byIframeElement, String expectedTitle);

    public void switchToMainDialogWithTitleThrows(By byIframeElement, By mainTitleElement, String title);

    public boolean isDialogOnPage(By byDialogElement);
}
