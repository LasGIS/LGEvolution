/**
 * @(#)PrimitiveAddon.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.draw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Class PrimitiveAddon.
 * @author vladimir.laskin
 * @version 1.0
 */
public class PrimitiveAddon {

    String keyX;
    String keyY;
    double centerX;
    double centerY;
    double multiplierX;
    double multiplierY;
    List<PrimitivePoint> points;

    /**
     *
     * @param key обобщённый ключ параметра изменения
     * @param centerX координата центра X
     * @param centerY координата центра Y
     * @param multiplier множитель для изменения по X координате
     * @param aPoints набор точек
     */
    public PrimitiveAddon(
        final String key, final double centerX,
        final double centerY, final double multiplier,
        final PrimitivePoint... aPoints
    ) {
        this(key, key, centerX, centerY, multiplier, multiplier, aPoints);
    }

    /**
     *
     * @param keyX ключ параметра изменения по X координате
     * @param keyY ключ параметра изменения по Y координате
     * @param centerX координата центра X
     * @param centerY координата центра Y
     * @param multiplierX множитель для изменения по X координате
     * @param multiplierY множитель для изменения по Y координате
     * @param aPoints набор точек
     */
    public PrimitiveAddon(
        final String keyX, final String keyY,
        final double centerX, final double centerY,
        final double multiplierX, final double multiplierY,
        final PrimitivePoint ... aPoints
    ) {
        this.keyX = keyX;
        this.keyY = keyY;
        this.centerX = centerX;
        this.centerY = centerY;
        this.multiplierX = multiplierX;
        this.multiplierY = multiplierY;
        this.points = new ArrayList<>(aPoints.length);
        Collections.addAll(points, aPoints);
    }

    public String getKeyX() {
        return keyX;
    }

    public String getKeyY() {
        return keyY;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public double getMultiplierX() {
        return multiplierX;
    }

    public double getMultiplierY() {
        return multiplierY;
    }

    public List<PrimitivePoint> getPoints() {
        return points;
    }
}
