/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bcalc.core;

import com.bcalc.objects.Art;
import com.bcalc.objects.Mirror;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;
import org.json.*;

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
        readJsonData();
    }

    private void readJsonData() {

        JSONTokener tokener = new JSONTokener(readLineByLineJava8(Core.ppCollector.getBaseFile()));
        constructData(new JSONObject(tokener));

    }

    private static String readLineByLineJava8(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    private void constructData(JSONObject incJson) {
        JSONArray jArts = jsonObject.getJSONArray("arts");
        for (int i = 0; i < jArts.length(); i++) {
            JSONObject rec = jArts.getJSONObject(i);
            String id = rec.getString("id");
            String name = rec.getString("name");
            int width = rec.getInt("width");
            int basePrice = rec.getInt("basePrice");
            int groupid = rec.getInt("groupid");
            Art artEx = new Art(name, "articul", id, width, basePrice);
        }

        JSONArray jMirrors = jsonObject.getJSONArray("mirrors");
        for (int i = 0; i < jMirrors.length(); i++) {
            JSONObject rec = jMirrors.getJSONObject(i);
            String id = rec.getString("id");
            String name = rec.getString("name");
            int basePrice = rec.getInt("priceRect");
            int groupid = rec.getInt("groupid");
            Mirror mirrEx = new Mirror(name, "mirror", id, basePrice);
        }
//JSONArray data = incomingJson.getJSONObject("soapenv:Body").getJSONObject("jsonObject").getJSONArray("data");
    }

    private void readGoogleData() {

    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
