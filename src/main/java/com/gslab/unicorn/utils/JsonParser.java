package com.gslab.unicorn.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 * <p>
 * <p>
 * JsonParser class provides the functioning to work with JSON file
 */

public class JsonParser {

    /**
     * This will collect specified objects data into array.
     *
     * @param jsonFilePath Json file path
     * @param objectName   Object name which data has to collect
     * @return JSONArray
     */
    public static JSONArray getJsonObjectsArray(String jsonFilePath, String objectName) {
        try (FileReader reader = new FileReader(jsonFilePath)) {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;
            return (JSONArray) jsonObject.get(objectName);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Assert.fail("Failed to get json file at: " + jsonFilePath, e);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This will convert json file data into Map of key-value for specified object.
     *
     * @param jsonArray json objects array provided by getJsonObjectsArray().
     * @return Map of 'String as key' and 'Object as value'
     */
    public static Map<String, Object> getJsonToMap(JSONArray jsonArray) {
        Map<String, Object> map = new HashMap<>();

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject json = (JSONObject) jsonArray.get(i);
            Iterator<String> keys = json.keySet().iterator();

            while (keys.hasNext()) {
                String key = keys.next();
                map.put(key, json.get(key));
            }
        }
        return map;
    }

    /**
     * This will convert json file data into Map of key-value for specified object.
     *
     * @param jsonFile   json file
     * @param objectName json file object name which data have to convert
     * @return Map of 'String as key' and 'Object as value'
     */
    public static Map<String, Object> getJsonToMap(String jsonFile, String objectName) {
        return getJsonToMap(getJsonObjectsArray(jsonFile, objectName));
    }


    /**
     * This will collect specified objects data into array.
     *
     * @param objectName Object name which data has to collect
     * @return JSONArray
     */
/*
    public JSONArray getJsonObjectsArray(String objectName) throws Exception {
        return getJsonObjectsArray(testDataJsonFile, objectName);
    }
*/

    /**
     * Retrieve value of data field from json file
     *
     * @param jsonArray json objects array provided by getJsonObjectsArray()
     * @param dataKey   key
     * @return String value
     */
    public String getDataValue(JSONArray jsonArray, String dataKey) {
        String value = null;
        for (Object object : jsonArray) {
            JSONObject jsonNumber = (JSONObject) object;
            value = (String) jsonNumber.get(dataKey);
        }
        return value;
    }
}