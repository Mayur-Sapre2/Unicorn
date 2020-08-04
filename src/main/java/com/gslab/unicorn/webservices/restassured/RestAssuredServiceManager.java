package com.gslab.unicorn.webservices.restassured;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

/**
 * @author vivek_Lande
 * ©Copyright 2018 Great Software Laboratory. All rights reserved
 * <p>
 * <p>
 * RestAssuredServiceManager provides the functioning to work with RestAssured
 */

public class RestAssuredServiceManager {
    private String baseUrl = null;

    public RestAssuredServiceManager(String baseUrl) {
        if (!String.valueOf(baseUrl.charAt(baseUrl.length() - 1)).equals("/"))
            baseUrl = baseUrl + "/";
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Return api request response
     *
     * @param parameter parameter to append to base url
     * @return Response
     */
    public Response getResponse(String parameter) {
        RequestSpecification httpRequest = RestAssured.given();
        return httpRequest.get(parameter);
    }

    /**
     * This to get api request response header
     *
     * @param response Response object return by RestAssuredService.getResponse()
     * @return Headers
     */
    public Headers getResponseHeader(Response response) {
        return response.headers();
    }

    /**
     * This will return response header data in the key-value map pairs
     *
     * @param response Response object return by RestAssuredService.getResponse()
     * @return Map of key-value both as strings
     */
    public Map<String, String> getResponseHeaderData(Response response) {
        Map<String, String> responseHeaderData = null;
        Headers allHeaders = response.headers();
        // Iterate over all the Headers
        for (Header header : allHeaders) {
            responseHeaderData.put(header.getName(), header.getValue());
            //System.out.println("Key: " + header.getName() + " Value: " + header.getValue());
        }
        return responseHeaderData;
    }

    /**
     * It provide api execution response
     *
     * @param response Response
     * @return ResponseBody
     */
    public ResponseBody getResponseBody(Response response) {
        return response.getBody();
    }

    /**
     * It provide api execution response
     *
     * @param response Response
     * @return String
     */
    public String getResponseBodyAsString(Response response) {
        return response.getBody().toString();
    }

    //TODO need to implement other generic methods
}
