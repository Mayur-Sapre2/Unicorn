package com.gslab.unicorn.selenium;

import org.openqa.selenium.WebElement;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 */

public interface SeleniumCheckboxInterface {
    void selectCheckboxButton(WebElement element) throws Exception;

    void deSelectCheckboxButton(WebElement element) throws Exception;

    boolean isCheckboxDisplayed(WebElement element) throws Exception;

    boolean isCheckboxEnabled(WebElement element) throws Exception;

    boolean isCheckboxSelected(WebElement element) throws Exception;

    void toggleCheckbox(WebElement element, boolean check) throws Exception;
}
