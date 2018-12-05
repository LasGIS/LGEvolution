/**
 * @(#)RatesColorPoint.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.draw;

import java.awt.*;
import java.util.Map;

/**
 * The Class RatesColorPoint.
 *
 * @author Laskin
 * @version 1.0
 * @since 29.09.13 0:29
 */
public class RatesColorPoint implements PrimitiveColor {

    private Color color;
    private String key;

    public RatesColorPoint(Color color, String key) {
        this.color = color;
        this.key = key;
    }

    @Override
    public Color calcColor(Map<String, Double> prop) {
        return color;
    }

    @Override
    public double getValue(Map<String, Double> prop) {
        Double val = prop.get(key);
        return val != null ? val : 0.0;
    }
}
