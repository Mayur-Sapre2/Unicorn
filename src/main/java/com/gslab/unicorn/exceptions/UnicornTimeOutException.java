package com.gslab.unicorn.exceptions;

import java.util.concurrent.TimeoutException;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 */

public class UnicornTimeOutException extends TimeoutException {

    public UnicornTimeOutException() {
        super();
    }

    public UnicornTimeOutException(String message) {
        super(message);
    }

}
