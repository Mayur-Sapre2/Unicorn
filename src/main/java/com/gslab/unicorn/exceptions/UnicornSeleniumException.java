package com.gslab.unicorn.exceptions;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 */

public class UnicornSeleniumException extends RuntimeException {

    public UnicornSeleniumException() {
        super();
    }

    public UnicornSeleniumException(String message) {
        super(message);
    }

    public UnicornSeleniumException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnicornSeleniumException(Throwable cause) {
        super(cause);
    }
}
