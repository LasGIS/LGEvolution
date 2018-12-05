/**
 * @(#)FoodIndex.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.organs;

/**
 * Пара ключ - индекс.
 * @author vladimir.laskin
 * @version 1.0
 */
public class FoodIndex {

    private String bestKey = null;
    private double bestIndex = -10000.0;
    private double value = 0.0;

    public String getBestKey() {
        return bestKey;
    }

    public void setBestKey(String bestKey) {
        this.bestKey = bestKey;
    }

    public double getBestIndex() {
        return bestIndex;
    }

    public void setBestIndex(double bestIndex) {
        this.bestIndex = bestIndex;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}