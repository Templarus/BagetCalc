/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bcalc.objects;

/**
 *
 * @author RusTe
 */
public class Mirror extends BaseObject {

    private int basePrice;

    public Mirror(String objectID, String name, String objectType, int basePrice) {
        this.name = name;
        this.objectID = objectID;
        this.objectType = objectType;
        this.basePrice = basePrice;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }
}
