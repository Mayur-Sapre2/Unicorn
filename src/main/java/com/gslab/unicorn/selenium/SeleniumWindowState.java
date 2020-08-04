package com.gslab.unicorn.selenium;

import java.util.List;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 * <p>
 * <p>
 * Stores the state of a browser window including the handle, and the path to the currently selected iframe
 */

public class SeleniumWindowState {

    private String windowHandle;
    private List<String> iframePath;

    public String getWindowHandle() {
        return windowHandle;
    }

    public void setWindowHandle(String windowHandle) {
        this.windowHandle = windowHandle;
    }

    public List<String> getIframePath() {
        return iframePath;
    }

    public void setIframePath(List<String> iframePath) {
        this.iframePath = iframePath;
    }
}
