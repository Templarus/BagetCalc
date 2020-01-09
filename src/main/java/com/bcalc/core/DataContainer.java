/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bcalc.core;

import static com.bcalc.core.Core.loggerConsole;
import com.bcalc.objects.Art;
import com.bcalc.objects.BaseObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author RusTe
 */
public class DataContainer implements Runnable {

    private HashMap<String, Art> arts;
    private HashMap<String, Art> mirrors;
    private JSONObject jsonObject;

    public DataContainer() {
        arts = new HashMap<>();
        mirrors = new HashMap<>();
    }

    private void readJsonData() {
        File baseConf = new File(Core.ppCollector.getBaseFile());
        JSONParser jsonParser = new JSONParser();

        try {

            InputStream inputStream = new FileInputStream(Core.ppCollector.getBaseFile());
            jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(inputStream, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            loggerConsole.error(Level.SEVERE, ex);
        } catch (IOException | ParseException ex) {
            loggerConsole.error(Level.SEVERE, ex);
        }
    }

    private void constructData() {
        JSONArray jArts = jsonObject.getJSONArray("arts");
        JSONArray jmirrors = jsonObject.getJSONArray("mirrors");
        
    }

    private void readGoogleData() {

    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
