package com.gslab.unicorn.selenium;

import org.openqa.selenium.WebElement;

import java.util.Date;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 */

public interface SeleniumTextboxInterface {

    public void sendKeys(WebElement element, String value);

    public void sendKeys(WebElement element, boolean clearBeforeSet, String value);

    public void sendKeysDate(WebElement element, boolean clearBeforeSet, Date date);

    public void setTextByAttrValue(WebElement element, String text);
}
