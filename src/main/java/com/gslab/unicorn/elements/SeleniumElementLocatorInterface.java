package com.gslab.unicorn.elements;

import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 * <p>
 */

public interface SeleniumElementLocatorInterface {
    public WebElement findElementByClassName(String className) throws Exception;

    public WebElement findElementByCssSelector(String cssSelector) throws Exception;

    public WebElement findElementById(String id) throws Exception;

    public WebElement findElementByLinkText(String linkText) throws Exception;

    public WebElement findElementByPartialLinkText(String partialLinkText) throws Exception;

    public WebElement findElementByName(String name) throws Exception;

    public WebElement findElementByTagName(String tag) throws Exception;

    public WebElement findElementByXpath(String xpath) throws Exception;

    public List<WebElement> findElementsByXpath(String xpath) throws Exception;

    public List<WebElement> findElementsByClassName(String className) throws Exception;

    public List<WebElement> findElementsByCssSelector(String cssSelector) throws Exception;

    public List<WebElement> findElementsById(String id) throws Exception;

    public List<WebElement> findElementsByTagName(String tag) throws Exception;
}
