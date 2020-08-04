package com.gslab.unicorn.exceptions;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 * <p>
 * <p>
 */

public class UnicornException extends Exception {

    public UnicornException() {
        super();
    }

    public UnicornException(String message) {
        super(message);
    }

    public UnicornException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnicornException(Throwable cause) {
        super(cause);
    }
}
