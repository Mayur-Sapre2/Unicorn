package com.gslab.unicorn.selenium;

import org.openqa.selenium.WebElement;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 */

public interface SeleniumRadioInterface {

    public void selectRadioButton(WebElement element);

    public boolean isRadioDisplayed(WebElement element);

    public boolean isRadioEnabled(WebElement element);

    public boolean isRadioSelected(WebElement element);
}
