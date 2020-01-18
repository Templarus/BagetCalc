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

    /**
     *
     * @param objectID
     * @param name
     * @param objectType
     * @param basePrice
     * @param groupId
     */
    public Mirror(int objectID, String name, String objectType, int basePrice, int groupId) {
        this.name = name;
        this.objectID = objectID;
        this.objectType = objectType;
        this.basePrice = basePrice;
        this.groupId = groupId;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }
}
