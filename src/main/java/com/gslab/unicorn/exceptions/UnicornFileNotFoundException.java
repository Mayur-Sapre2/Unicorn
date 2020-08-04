package com.gslab.unicorn.exceptions;

import java.io.FileNotFoundException;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 * <p>
 */

public class UnicornFileNotFoundException extends FileNotFoundException {

    public UnicornFileNotFoundException() {
        super();
    }

    public UnicornFileNotFoundException(String message) {
        super(message);
    }
}
