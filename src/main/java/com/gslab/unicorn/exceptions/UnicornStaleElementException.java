package com.gslab.unicorn.exceptions;

import org.openqa.selenium.StaleElementReferenceException;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 */

public class UnicornStaleElementException extends StaleElementReferenceException {

    public UnicornStaleElementException(String message) {
        super(message);
    }

    public UnicornStaleElementException(String message, Throwable cause) {
        super(message, cause);
    }
}
