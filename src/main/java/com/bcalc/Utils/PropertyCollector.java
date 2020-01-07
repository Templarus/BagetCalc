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
        Properties initconfig = new Properties();

        try {
            String configFile;
            initconfig.load(this.getClass().getResourceAsStream("/start.properties"));
            loaded = readConfig(new FileInputStream(initconfig.getProperty("config.location")));
            if (!loaded) {
                loggerDB.warn(MONMARKER, "Гружу дефолт");
                loaded = readConfig(this.getClass().getResourceAsStream(initconfig.getProperty("config.deflocation")));
            }
        } catch (IOException e) {
            loaded = false;
            loggerDB.error(MONMARKER, "ОШИБКА: проблемы при чтении файлов конфигурации!");
        }
        APPLICATION_NAME = "Google Sheets API Java Quickstart";
        TOKENS_DIRECTORY_PATH = "tokens";
        CREDENTIALS_FILE_PATH = "/credentials.json";
    }

    private boolean readConfig(InputStream fileloc) {
        try {
            property = new Properties();
            if (fileloc != null) {
                property.load(fileloc);
            } else {
                return false;
            }
            LAUNCH_TYPE = property.getProperty("launch.type", "UI");;
            LDAPUSER = encodeCrypt(property.getProperty("sec.LDAPlogin", ""), "sec.LDAPlogin");;
            LDAPPASS = encodeCrypt(property.getProperty("sec.LDAPpassword", ""), "sec.LDAPpassword");;
            HOST_TEST = encodeCrypt(property.getProperty("conn.HOST_TEST", ""), "conn.HOST_TEST");
            HOST_PROD = encodeCrypt(property.getProperty("conn.HOST_PROD", ""), "conn.HOST_PROD");
            PORT = Integer.valueOf(property.getProperty("conn.PORT", "1521"));
            SID = property.getProperty("conn.SID", "HPRDB");
            URL_TEST = property.getProperty("db.URL", "jdbc:oracle:thin:@") + HOST_TEST + ":" + PORT + ":" + SID;
            URL_PROD = property.getProperty("db.URL", "jdbc:oracle:thin:@") + HOST_PROD + ":" + PORT + ":" + SID;
            POST_URL = encodeCrypt(property.getProperty("post.URL", ""), "post.URL");
            POST_USER = encodeCrypt(property.getProperty("post.USER", ""), "post.USER");
            POST_USERPR = encodeCrypt(property.getProperty("post.USERPR", ""), "post.USERPR");
            TESTLOGIN = encodeCrypt(property.getProperty("sec.TESTLOGIN", ""), "sec.TESTLOGIN");
            TESTPS = encodeCrypt(property.getProperty("sec.TESTPS", ""), "sec.TESTPS");
            PRODLOGIN = encodeCrypt(property.getProperty("sec.PRODLOGIN", ""), "sec.PRODLOGIN");
            PRODPS = encodeCrypt(property.getProperty("sec.PRODPS", ""), "sec.PRODPS");
            LDAP = encodeCrypt(property.getProperty("sec.LDAP", ""), "sec.LDAP");
            INTERNALTEST = Boolean.valueOf(property.getProperty("test.INTERNALTEST", "false"));
            INTPRODCOMPVAL = new String(property.getProperty("test.INTPRODCOMPVAL", "C:\\HypMon\\MonitoringHystory\\Prod_testBlock").getBytes(encoding));
            INTTESTCOMPVAL = new String(property.getProperty("test.INTTESTCOMPVAL", "C:\\HypMon\\MonitoringHystory\\Test_testBlock").getBytes(encoding));
            TEST = Boolean.valueOf(property.getProperty("test.TEST", "1521"));
            PARAMDEL = property.getProperty("util.Params", "|");
            MULTYTHREADING = Boolean.valueOf(property.getProperty("util.MULTYTHREADING", "false"));
            VALDEL = property.getProperty("util.VALDEL", ";");
            OBJDEL = property.getProperty("util.OBJDEL", "#");
            COLLDEL = property.getProperty("util.COLLDEL", "@");
            COBJDEL = property.getProperty("util.COBJDEL", "!");
            SYSNAME_TEST = property.getProperty("util.SYSNAME_TEST", "OHP_TEST");
            SYSNAME_DEV = property.getProperty("util.SYSNAME_DEV", "OHP_DEV");
            SYSNAME_PROD = property.getProperty("util.SYSNAME_PROD", "OHP_PROD");
            BASEDIRECTORY = new String(property.getProperty("dir.BASEDIRECTORY", "C:\\HypMon\\").getBytes(encoding));
            PATHFORPATTERN = BASEDIRECTORY + new String(property.getProperty("dir.PATHFORPATTERN", "jrxml\\").getBytes(encoding));
            HISTORIANPATCH = BASEDIRECTORY + new String(property.getProperty("dir.HISTORIANPATCH", "MonitoringHystory\\").getBytes(encoding));
            COMPARERESULT = "Compare_result_" + new SimpleDateFormat("YYYYMMddHHmmss").format(new Date()) + ".csv";
            KSUITFTP = BASEDIRECTORY + new String(property.getProperty("dir.KSUITFTP", "KSUIT\\").getBytes(encoding));
            //old value
            //SKEIBFTP = BASEDIRECTORY + new String(property.getProperty("dir.SKEIBFTP", "SKEIB\\").getBytes(encoding));
            SKEIBFTP = new String(property.getProperty("dir.SKEIBFTP", BASEDIRECTORY + new String(property.getProperty("dir.SKEIBFTP"))).getBytes(encoding));
            FILENAMES = property.getProperty("voc.FILENAMES", "").trim().split(PARAMDEL);
            COMPARE_FIELDS_FILTER = new ArrayList(Arrays.asList(property.getProperty("voc.COMPARE_FIELDS_FILTER", "").trim().split(PARAMDEL)));
            authAllowedChange = new ArrayList(Arrays.asList(property.getProperty("voc.authAllowedChange", "").trim().split(PARAMDEL)));
            tecAllowedChange = new ArrayList(Arrays.asList(property.getProperty("voc.tecAllowedChange", "").trim().split(PARAMDEL)));
            authAllowedDelete = new ArrayList(Arrays.asList(property.getProperty("voc.authAllowedDelete", "").trim().split(PARAMDEL)));
            tecAllowedDelete = new ArrayList(Arrays.asList(property.getProperty("voc.tecAllowedDelete", "").trim().split(PARAMDEL)));
            accAllowedDelete = new ArrayList(Arrays.asList(property.getProperty("voc.accAllowedDelete", "").trim().split(PARAMDEL)));
            authAllowedAdd = new ArrayList(Arrays.asList(property.getProperty("voc.authAllowedAdd", "").trim().split(PARAMDEL)));
            tecAllowedAdd = new ArrayList(Arrays.asList(property.getProperty("voc.tecAllowedAdd", "").trim().split(PARAMDEL)));
            accAllowedAdd = new ArrayList(Arrays.asList(property.getProperty("voc.accAllowedAdd", "").trim().split(PARAMDEL)));
            ADMIN_ROLES = new ArrayList(Arrays.asList(new String(property.getProperty("filter.ADMIN_ROLES", "").getBytes(encoding)).trim().split(PARAMDEL)));
            CRIT_ROLES = new ArrayList(Arrays.asList(new String(property.getProperty("filter.CRIT_ROLES", "").getBytes(encoding)).trim().split(PARAMDEL)));
            CONS = new ArrayList(Arrays.asList(new String(property.getProperty("filter.CONS", "").getBytes(encoding)).trim().split(PARAMDEL)));
            DEVS = new ArrayList(Arrays.asList(new String(property.getProperty("filter.DEVS", "").getBytes(encoding)).trim().split(PARAMDEL)));
            APP_PARAMS_FILTER10 = new ArrayList(Arrays.asList(new String(property.getProperty("filter.APP_PARAMS_FILTER10", "").getBytes(encoding)).trim().split(PARAMDEL)));
            APP_PARAMS_FILTER11 = new ArrayList(Arrays.asList(new String(property.getProperty("filter.APP_PARAMS_FILTER11", "").getBytes(encoding)).trim().split(PARAMDEL)));
            APP_PARAMS_FILTER12 = new ArrayList(Arrays.asList(new String(property.getProperty("filter.APP_PARAMS_FILTER12", "").getBytes(encoding)).trim().split(PARAMDEL)));
            APP_PARAMS_FILTER13 = new ArrayList(Arrays.asList(new String(property.getProperty("filter.APP_PARAMS_FILTER13", "").getBytes(encoding)).trim().split(PARAMDEL)));
            APP_PARAMS_FILTER14 = new ArrayList(Arrays.asList(new String(property.getProperty("filter.APP_PARAMS_FILTER14", "").getBytes(encoding)).trim().split(PARAMDEL)));
            APP_PARAMS_FILTER20 = new ArrayList(Arrays.asList(new String(property.getProperty("filter.APP_PARAMS_FILTER20", "").getBytes(encoding)).trim().split(PARAMDEL)));
            APP_PARAMS_FILTER30 = new ArrayList(Arrays.asList(new String(property.getProperty("filter.APP_PARAMS_FILTER30", "").getBytes(encoding)).trim().split(PARAMDEL)));
            APP_PARAMS_FILTER31 = new ArrayList(Arrays.asList(new String(property.getProperty("filter.APP_PARAMS_FILTER31", "").getBytes(encoding)).trim().split(PARAMDEL)));
            APP_PARAMS_FILTER32_TEST = new ArrayList(Arrays.asList(new String(property.getProperty("filter.APP_PARAMS_FILTER32_TEST", "").getBytes(encoding)).trim().split(PARAMDEL)));
            APP_PARAMS_FILTER32_PROD = new ArrayList(Arrays.asList(new String(property.getProperty("filter.APP_PARAMS_FILTER32_PROD", "").getBytes(encoding)).trim().split(PARAMDEL)));
            APP_PARAMS_FILTER33 = new ArrayList(Arrays.asList(new String(property.getProperty("filter.APP_PARAMS_FILTER33", "").getBytes(encoding)).trim().split(PARAMDEL)));
            APP_PARAMS_FILTER34 = new ArrayList(Arrays.asList(new String(property.getProperty("filter.APP_PARAMS_FILTER34", "").getBytes(encoding)).trim().split(PARAMDEL)));
            APP_PARAMS_FILTER35 = new ArrayList(Arrays.asList(new String(property.getProperty("filter.APP_PARAMS_FILTER35_1", "").getBytes(encoding)).trim().split(PARAMDEL)));
            APP_PARAMS_FILTER35.addAll(Arrays.asList(new String(property.getProperty("filter.APP_PARAMS_FILTER35_2", "").getBytes(encoding)).trim().split(PARAMDEL)));
            APP_PARAMS_FILTER36 = new ArrayList(Arrays.asList(new String(property.getProperty("filter.APP_PARAMS_FILTER36", "").getBytes(encoding)).trim().split(PARAMDEL)));
            APP_PARAMS_FILTER40 = new ArrayList(Arrays.asList(new String(property.getProperty("filter.APP_PARAMS_FILTER40", "").getBytes(encoding)).trim().split(PARAMDEL)));
            APP_PARAMS_FILTER41 = new ArrayList(Arrays.asList(new String(property.getProperty("filter.APP_PARAMS_FILTER41", "").getBytes(encoding)).trim().split(PARAMDEL)));
            APP_PARAMS_FILTER42 = new ArrayList(Arrays.asList(new String(property.getProperty("filter.APP_PARAMS_FILTER42", "").getBytes(encoding)).trim().split(PARAMDEL)));
            APP_PARAMS_FILTER51 = new ArrayList(Arrays.asList(new String(property.getProperty("filter.APP_PARAMS_FILTER51", "").getBytes(encoding)).trim().split(PARAMDEL)));
            APP_PARAMS_FILTER52 = new ArrayList(Arrays.asList(new String(property.getProperty("filter.APP_PARAMS_FILTER52", "").getBytes(encoding)).trim().split(PARAMDEL)));
            APP_PARAMS_FILTER60 = new ArrayList(Arrays.asList(new String(property.getProperty("filter.APP_PARAMS_FILTER60", "").getBytes(encoding)).trim().split(PARAMDEL)));
            OBJACCMODE_READ = property.getProperty("types.OBJACCMODE_READ", "1");
            OBJACCMODE_WRITE = property.getProperty("types.OBJACCMODE_WRITE", "3");
            OBJACCMODE_DENY = property.getProperty("types.OBJACCMODE_DENY", "-1");
            OBJACCMODE_STR = property.getProperty("types.OBJACCMODE_STR", "4");
            OBJFLAG_MEMBER = property.getProperty("types.OBJFLAG_MEMBER", "0");
            OBJFLAG_CHILDREN = property.getProperty("types.OBJFLAG_CHILDREN", "5");
            OBJFLAG_ICHILD = property.getProperty("types.OBJFLAG_ICHILD", "6");
            OBJFLAG_DESCEND = property.getProperty("types.OBJFLAG_DESCEND", "8");
            OBJFLAG_IDESC = property.getProperty("types.OBJFLAG_IDESC", "9");
            APP_ROOT_ID = property.getProperty("types.APP_ROOT_ID", "1");
            APP_DIMENSIONS_ID = property.getProperty("types.APP_DIMENSIONS_ID", "2");
            APP_CALENDARS_ID = property.getProperty("types.APP_CALENDARS_ID", "5");
            APP_CURRENCIES_ID = property.getProperty("types.APP_CURRENCIES_ID", "6");
            APP_FX_TABLES_ID = property.getProperty("types.APP_FX_TABLES_ID", "8");
            APP_FORMS_ID = property.getProperty("types.APP_FORMS_ID", "9");
            APP_ALIASES_ID = property.getProperty("types.APP_ALIASES_ID", "10");
            APP_CUBES_ID = property.getProperty("types.APP_CUBES_ID", "11");
            APP_TASKLISTS_ID = property.getProperty("types.APP_TASKLISTS_ID", "16");
            APP_CALCMGRRULES_ID = property.getProperty("types.APP_CALCMGRRULES_ID", "22");
            APP_CALCMGRRULESETS_ID = property.getProperty("types.APP_CALCMGRRULESETS_ID", "23");
            APP_CALCMGRVARIABLES_ID = property.getProperty("types.APP_CALCMGRVARIABLES_ID", "24");
            Any1_SPO = new ArrayList(Arrays.asList(new String(property.getProperty("any.1SPO", "").getBytes(encoding)).trim().split(PARAMDEL)));
            Any2_FA = new ArrayList(Arrays.asList(new String(property.getProperty("any.2_FA", "").getBytes(encoding)).trim().split(PARAMDEL)));
            AnyUps = new ArrayList(Arrays.asList(new String(property.getProperty("any.Ups", "").getBytes(encoding)).trim().split(PARAMDEL)));

        } catch (IOException e) {
            loggerDB.warn(MONMARKER, "ОШИБКА: Файл свойств " + fileloc + " отсуствует! Используется дефолтный конфиг", e);
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
            loggerDB.fatal(MONMARKER, "ОШИБКА: Файл свойств кривой!");
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

}
