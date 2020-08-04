package com.gslab.unicorn.selenium;

import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 */

public interface SeleniumDropdownInterface {

    void waitForElement(WebElement selectElement);

    void chooseSelectOption(final String dropDownId, final String text) throws Exception;

    String getFirstSelectedOptionText(WebElement selectElement);

    List<WebElement> getOptions(WebElement selectElement);

    void selectByIndex(WebElement selectElement, int index);

    void selectByValue(WebElement selectElement, String value);

    void selectByVisibleText(WebElement selectElement, String text) throws Exception;

    void deselectByIndex(WebElement deselectElement, int index);

    void deselectByValue(WebElement deselectElement, String value);

    void deselectByVisibleText(WebElement deselectElement, String text);

    void openDropDown(WebElement element);
}
