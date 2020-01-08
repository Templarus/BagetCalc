/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bcalc.core;

import com.bcalc.objects.Art;
import com.bcalc.objects.BaseObject;
import com.google.api.client.json.Json;
import java.util.HashMap;

/**
 *
 * @author RusTe
 */
public class DataContainer implements Runnable {

    private HashMap<String, Art> arts;
    private HashMap<String, Art> mirrors;

    public DataContainer() {
        arts = new HashMap<>();
        mirrors = new HashMap<>();
    }

    private void readJsonData() {
        Json baseData = new Json();
        
    }

    private void readGoogleData() {

    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
