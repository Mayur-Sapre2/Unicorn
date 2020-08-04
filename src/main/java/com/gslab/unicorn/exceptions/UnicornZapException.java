package com.gslab.unicorn.exceptions;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 */

public class UnicornZapException extends Exception {


    public UnicornZapException() {
        super();
    }

    public UnicornZapException(String message) {
        super(message);
    }

    public UnicornZapException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnicornZapException(Throwable cause) {
        super(cause);
    }
}
