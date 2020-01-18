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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author RusTe
 */
public class DataContainer implements Runnable {

    private HashMap<String, Art> arts;
    private HashMap<String, Mirror> mirrors;

    public DataContainer() {
        arts = new HashMap<>();
        mirrors = new HashMap<>();

    }

    private void readJsonData() {

        JSONTokener tokener = new JSONTokener(readLineByLineJava8(Core.ppCollector.getBaseFile()));
        JSONObject res = new JSONObject(tokener);
        constructData(res);

    }

    private static String readLineByLineJava8(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    private void constructData(JSONObject incJson) {
        JSONArray jArts = incJson.getJSONArray("arts");
        for (int i = 0; i < jArts.length(); i++) {
            JSONObject rec = jArts.getJSONObject(i);
            int id = rec.getInt("id");
            String name = rec.getString("name");
            int width = rec.getInt("width");
            int basePrice = rec.getInt("basePrice");
            int groupid = rec.getInt("groupid");
            arts.put(name, new Art(name, "articul", id, width, basePrice, groupid));
        }

        JSONArray jMirrors = incJson.getJSONArray("mirrors");
        for (int i = 0; i < jMirrors.length(); i++) {
            JSONObject rec = jMirrors.getJSONObject(i);
            int id = rec.getInt("id");
            String name = rec.getString("name");
            int basePrice = rec.getInt("priceRect");
            int groupid = rec.getInt("groupid");
            mirrors.put(name, new Mirror(id, name, "mirror", basePrice, groupid));
        }
//JSONArray data = incomingJson.getJSONObject("soapenv:Body").getJSONObject("jsonObject").getJSONArray("data");
    }

    private void readGoogleData() {

    }

    @Override
    public void run() {
        readJsonData();
    }

}
