package com.gslab.unicorn.elements;

import com.gslab.unicorn.exceptions.UnicornSeleniumException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 * <p>
 *
 */

public class ElementsObjectMap {
    Properties prop;
    public static By byLocator;
    public static WebElement webElementLocator;
    HashMap<String, By> byMap = new HashMap<>();
    HashMap<String, WebElement> elementMap = new HashMap<>();

    /**
     * Initialize object repository from properties file and store in the map
     *
     * @param filePath @see the sample file at \\unicorn\src\main\resources\sampleElementsObjectMap.properties
     * @return Map of String as object name and By as locator
     */
    public HashMap<String, By> getLocatorByMap(String filePath) {
        prop = new Properties();
        try {
            FileInputStream fis = new FileInputStream(filePath);
            prop.load(fis);
            fis.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        byMap.clear();
        Enumeration keys = prop.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            String locate = prop.getProperty(key);
            String[] objLocator = locate.split(":", 2);
            String locateBy = objLocator[0].toLowerCase();
            String locatorValue = objLocator[1];

            switch (locateBy) {
                case "id":
                    byLocator = By.id(locatorValue);
                    break;

                case "name":
                    byLocator = By.name(locatorValue);
                    break;

                case "classname":
                    byLocator = By.className(locatorValue);
                    break;

                case "partiallinktext":
                    byLocator = By.partialLinkText(locatorValue);
                    break;

                case "linktext":
                    byLocator = By.linkText(locatorValue);
                    break;

                case "cssselector":
                    byLocator = By.cssSelector(locatorValue);
                    break;

                case "xpath":
                    byLocator = By.xpath(locatorValue);
                    break;
            }
            byMap.put(key, byLocator);
        }
        return byMap;
    }

    /**
     * Initialise page elements from property file and store in the map
     *
     * @param filePath @see the sample file at \\unicorn\src\main\resources\sampleElementsObjectMap.properties
     * @return Map of String as object name and initialized WebElement
     */
    public HashMap<String, WebElement> getLocatorElementMap(String filePath) {
        SeleniumElementLocatorInterface seleniumElementLocator = new SeleniumElementLocator();
        prop = new Properties();
        try {
            FileInputStream fis = new FileInputStream(filePath);
            prop.load(fis);
            fis.close();
            Enumeration keys = prop.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                String locate = prop.getProperty(key);
                String[] objLocator = locate.split(":", 2);
                String locateBy = objLocator[0].toLowerCase();
                String locatorValue = objLocator[1];

                switch (locateBy) {
                    case "id":
                        webElementLocator = seleniumElementLocator.findElementById(locatorValue);
                        break;

                    case "name":
                        webElementLocator = seleniumElementLocator.findElementByName(locatorValue);
                        break;

                    case "classname":
                        webElementLocator = seleniumElementLocator.findElementByClassName(locatorValue);
                        break;

                    case "partiallinktext":
                        webElementLocator = seleniumElementLocator.findElementByPartialLinkText(locatorValue);
                        break;

                    case "linktext":
                        webElementLocator = seleniumElementLocator.findElementByLinkText(locatorValue);
                        break;

                    case "cssselector":
                        webElementLocator = seleniumElementLocator.findElementByCssSelector(locatorValue);
                        break;

                    case "xpath":
                        webElementLocator = seleniumElementLocator.findElementByXpath(locatorValue);
                        break;
                }
                elementMap.put(key, webElementLocator);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            throw new UnicornSeleniumException("Exception while initializing web elements ", e);
        }
        return elementMap;
    }
}
