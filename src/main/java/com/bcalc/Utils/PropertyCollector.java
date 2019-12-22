/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bcalc.Utils;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

/**
 *
 * @author RusTe
 */
public class PropertyCollector {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private String TOKENS_DIRECTORY_PATH;
    private String APPLICATION_NAME;
    private String CREDENTIALS_FILE_PATH;
    private String baseFile;
    private String spreadsheetId;

    public PropertyCollector() {
        collectData();
    }

    private void collectData() {

        APPLICATION_NAME = "Google Sheets API Java Quickstart";
        TOKENS_DIRECTORY_PATH = "tokens";
        CREDENTIALS_FILE_PATH = "/credentials.json";
    }

    public static JsonFactory getJSON_FACTORY() {
        return JSON_FACTORY;
    }

    public String getTOKENS_DIRECTORY_PATH() {
        return TOKENS_DIRECTORY_PATH;
    }

    public String getAPPLICATION_NAME() {
        return APPLICATION_NAME;
    }

    public String getCREDENTIALS_FILE_PATH() {
        return CREDENTIALS_FILE_PATH;
    }

}
