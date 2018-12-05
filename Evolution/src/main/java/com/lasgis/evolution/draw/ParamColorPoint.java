/**
 * @(#)ParamColorPoint.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.draw;

import java.awt.*;
import java.util.Map;

/**
 * точка света в ParamColor объекте.
 * @author vladimir.laskin
 * @version 1.0
 */
public class ParamColorPoint implements PrimitiveColor {

    private Color color;
    private double value;

    /**
     * Конструктор.
     * @param color
     * @param value
     */
    public ParamColorPoint(Color color, double value) {
        this.color = color;
        this.value = value;
    }

    @Override
    public Color calcColor(Map<String, Double> prop) {
        return color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getValue(Map<String, Double> prop) {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
