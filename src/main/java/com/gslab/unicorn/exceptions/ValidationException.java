package com.gslab.unicorn.exceptions;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 */

public class ValidationException extends UnicornException {

    public ValidationException() {
        super();
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }
}
