package com.gslab.unicorn.utils;

import com.gslab.unicorn.exceptions.UnicornException;
import com.gslab.unicorn.exceptions.UnicornFileNotFoundException;
import com.gslab.unicorn.exceptions.UnicornIOException;
import com.gslab.unicorn.exceptions.ValidationException;
import org.apache.commons.exec.OS;

import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 * <p>
 * <p>
 * The CommonUtils provides the utilities functions required to test script in day to day life
 */

public class CommonUtils {

    private static final int RANDOM_STRING_LENGTH = 10;

    /**
     * Gets the resource as path from src/test/resources.
     *
     * @param fileName the file name
     * @return the resource as path
     */
    public static String getResourceAsPath(String fileName) throws Exception {
        URL fileUrl = null;
        String path = "";
        try {
            fileUrl = ClassLoader.getSystemClassLoader().getResource(fileName);
            if (fileUrl == null)
                throw new UnicornFileNotFoundException("file: " + fileName + " not found");
        } catch (Exception e) {
            throw new UnicornException(e);
        }
        try {
            path = URLDecoder.decode(fileUrl.getPath(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (OS.isFamilyWindows()) {
            return path.substring(1);
        } else if (OS.isFamilyUnix() || OS.isFamilyMac()) {
            return path;
        }
        return null;
    }

    /**
     * Get current time stamp in the format 'yyyyMMddHHmmss'
     *
     * @return time stamp
     */
    public static String getCurrentTimeStamp() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * Generate 4 bit random number
     *
     * @return random number
     */
    public static String get4BitRandonNumber() {
        return String.format("%04d", new BigInteger(4, new Random()));
    }

    /**
     * Generate 8 bit random number
     *
     * @return random number
     */
    public static String get8BitRandonNumber() {
        return String.format("%08d", new BigInteger(8, new Random()));
    }

    /**
     * Generate 16 bit random number
     *
     * @return random number
     */
    public static String get16BitRandonNumber() {
        return String.format("%016d", new BigInteger(16, new Random()));
    }

    /**
     * Generate 32 bit random number
     *
     * @return random number
     */
    public static String get32BitRandonNumber() {
        return String.format("%032d", new BigInteger(32, new Random()));
    }

    /**
     * Generate 64 bit random number
     *
     * @return random number
     */
    public static String get64BitRandonNumber() {
        return String.format("%064d", new BigInteger(64, new Random()));
    }

    /**
     * Generate n bit random number
     *
     * @param number of bit
     * @return random number
     */
    public static String getNBitRandonNumber(int number) {
        String format = "%0" + number + "d";
        return String.format(format, new BigInteger(number, new Random()));
    }

    /**
     * Generates alpha-numeric random string
     *
     * @return random String
     * @author landev2
     */
    public static String getRandomString(int length) {
        String CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuffer randStr = new StringBuffer();
        Random rnd = new Random();
        while (randStr.length() < length) { // length of the random string.
            int index = (int) (rnd.nextFloat() * CHAR_LIST.length());
            randStr.append(CHAR_LIST.charAt(index));
        }
        return randStr.toString();
    }

    /**
     * This method generates random numeric string
     *
     * @return random String
     */
    public static String getRandomNumber(int length) {
        String CHAR_LIST = "1234567890";
        StringBuffer randStr = new StringBuffer();
        Random rnd = new Random();
        while (randStr.length() < length) { // length of the random string.
            int index = (int) (rnd.nextFloat() * CHAR_LIST.length());
            randStr.append(CHAR_LIST.charAt(index));
        }
        return randStr.toString();
    }

    /**
     * Read properties file
     *
     * @param filePath filepath
     * @return properties
     * @throws Exception
     */
    public static Properties readPropertiesFile(String filePath) throws Exception {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream(filePath);
            // load a properties file
            prop.load(input);
        } catch (IOException io) {
            throw new UnicornIOException(filePath + " is not found!", io);
        }
        return prop;
    }

    /**
     * Delete empty and non-empty directory
     *
     * @param dir directory path
     * @return boolean result
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * Verify if directory is empty
     *
     * @param directory directory path
     * @return boolean result
     */
    public static boolean isDirEmpty(final Path directory) {
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory)) {
            return !dirStream.iterator().hasNext();
        } catch (IOException ioe) {

        }
        return false;
    }

    /**
     * This method detects the os type and return it
     *
     * @return os type
     */
    public static /*OSInfo.OSType*/ String getOSType() throws Exception {
       /* //DO NOT REMOVE: Un-comment below line to get all system details
        // System.getProperties().list(System.out);
        if (OS.isFamilyUnix())
            return OSInfo.OSType.LINUX;
        else if (OS.isFamilyMac())
            return OSInfo.OSType.MACOSX;
        else if (OS.isFamilyWindows())
            return OSInfo.OSType.WINDOWS;
        else
            throw new ValidationException("Unsupported operation system!");*/
        String osType = System.getProperty("os.name").toLowerCase();
        if (osType.contains("windows")) {
            return "windows";
        } else if (osType.contains("linux")) {
            return "linux";
        } else if (osType.contains("mac")) {
            return "mac";
        } else {
            throw new ValidationException("Unsupported operation system!");
        }
    }

    /**
     * This find the available port on the local machine
     *
     * @return available port number
     * @throws IOException
     */
    public static Integer findOpenPortOnAllLocalInterfaces() {
        int port;

        try (ServerSocket socket = new ServerSocket(0)) {
            port = socket.getLocalPort();
            socket.close();
            return port;
        } catch (Exception e) {
            System.out.println("Failed to find local open port !");
            e.printStackTrace();
        }
        return -1;
    }
}

