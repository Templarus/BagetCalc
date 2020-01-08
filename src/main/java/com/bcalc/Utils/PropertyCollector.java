/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bcalc.Utils;

import static com.bcalc.core.Core.loggerConsole;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.*;
import java.util.Properties;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimplePBEConfig;
import org.jasypt.properties.PropertyValueEncryptionUtils;

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
    private String OBJDEL;
    private boolean loaded;
    private Properties property;
    private String DummyData;

    public PropertyCollector() {
        collectData();
    }

    private void collectData() {
        Properties initconfig = new Properties();

        try {
            String configFile;
            initconfig.load(this.getClass().getResourceAsStream("/start.properties"));
            loaded = readConfig(new FileInputStream(initconfig.getProperty("config.location")));
            if (!loaded) {
                loggerConsole.warn("Гружу дефолт");
                loaded = readConfig(this.getClass().getResourceAsStream(initconfig.getProperty("config.deflocation")));
            }
        } catch (IOException e) {
            loaded = false;
            loggerConsole.error("ОШИБКА: проблемы при чтении файлов конфигурации!" + e.getMessage());
        }

    }

    private boolean readConfig(InputStream fileloc) {
        try {
            property = new Properties();
            if (fileloc != null) {
                property.load(fileloc);
            } else {
                return false;
            }
            // LAUNCH_TYPE = property.getProperty("art.baseFile", "UI");
            // LDAPUSER = encodeCrypt(property.getProperty("base.spreadsheetId", ""), "sec.LDAPlogin");;
            APPLICATION_NAME = property.getProperty("base.app_Name", "empty");
            TOKENS_DIRECTORY_PATH = property.getProperty("base.TOKENS_DIRECTORY_PATH", "empty");
            CREDENTIALS_FILE_PATH = property.getProperty("base.CREDENTIALS_FILE_PATH", "empty");
            OBJDEL = property.getProperty("util.OBJDEL", "empty");
            baseFile = property.getProperty("art.baseFile", "empty");
            spreadsheetId = property.getProperty("base.spreadsheetId", "empty");
            //  SID = property.getProperty("conn.SID", "HPRDB");

        } catch (IOException e) {
            loggerConsole.warn("ОШИБКА: Файл свойств " + fileloc + " отсуствует! Используется дефолтный конфиг", e);
            return false;
        }
        return true;
    }

    private String encodeCrypt(String rawvalue, String key) {
        if (rawvalue.length() > 0) {
            String value = "ENC(" + rawvalue + ")";
            SimplePBEConfig config = new SimplePBEConfig();
            config.setAlgorithm("PBEWithMD5AndTripleDES");
            config.setKeyObtentionIterations(1000);
            config.setPassword(key);

            StandardPBEStringEncryptor encryptor = new org.jasypt.encryption.pbe.StandardPBEStringEncryptor();
            encryptor.setConfig(config);
            encryptor.initialize();
            return PropertyValueEncryptionUtils.decrypt(value, encryptor);
        } else {
            loggerConsole.fatal("ОШИБКА: Файл свойств кривой!");
            return "";
        }
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

    public String getBaseFile() {
        return baseFile;
    }

    public String getSpreadsheetId() {
        return spreadsheetId;
    }

    public String getOBJDEL() {
        return OBJDEL;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public Properties getProperty() {
        return property;
    }

    public String getDummyData() {
        return DummyData;
    }

}
