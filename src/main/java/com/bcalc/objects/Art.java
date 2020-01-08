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
public class Art extends BaseObject {

    private int width;
    private int basePrice;

    public Art(String objectID, String objectType, int width, int basePrice) {
        this.objectID = objectID;
        this.objectType = objectType;
        this.width = width;
        this.basePrice = basePrice;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

}
