package com.gslab.unicorn.exceptions;

import java.io.IOException;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 * <p>
 */

public class UnicornIOException extends IOException {
    public UnicornIOException() {
        super();
    }

    public UnicornIOException(String message) {
        super(message);
    }

    public UnicornIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnicornIOException(Throwable cause) {
        super(cause);
    }
}
