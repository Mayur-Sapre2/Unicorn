/**
 * Copyright (c) 2015, Symantec Corporation. ALL RIGHTS RESERVED.
 * Description:
 * Watermark id: CB70-2684-7135-66-15-8
 */
package com.gslab.unicorn.driver.web;

import org.apache.commons.exec.CommandLine;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 * <p>
 * The Class GridConfig contains method to configure hub and nodes.
 */
public class GridConfig {

    private CommandLine command;
    private ExecutorService executorService;

    /**
     * Configure grid. 1) Get device list 2) Find free ports 3) Create client
     * objects. 4) Start appium server.
     *
     * @param nodeConfigFilePath
     * @throws IOException
     * @throws InterruptedException
     */
    public void configureGrid(String nodeConfigFilePath, String hubJarPath) throws IOException, InterruptedException {
    }

    /**
     * Starts selenium hub to which other mobile devices will get connected.
     *
     * @param hubJarPath
     * @throws InterruptedException
     */
    private void startSeleniumHub(String hubJarPath) throws InterruptedException {
    }

    /**
     * Gets the free ports.
     *
     * @param portNumber the port number
     * @return the free ports
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private int[] getFreePorts(int portNumber) throws IOException {
        int[] result = new int[portNumber];
        List<ServerSocket> servers = new ArrayList<ServerSocket>(portNumber);
        ServerSocket tempServer = null;

        for (int i = 0; i < portNumber; i++) {
            try {
                tempServer = new ServerSocket(0);
                servers.add(tempServer);
                result[i] = tempServer.getLocalPort();
            } finally {
                for (ServerSocket server : servers) {
                    try {
                        server.close();
                    } catch (IOException e) {
                        // Continue closing servers.
                    }
                }
            }
        }
        return result;
    }

}
