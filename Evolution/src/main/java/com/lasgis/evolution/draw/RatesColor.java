/**
 * @(#)RatesColor.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.draw;

import com.lasgis.evolution.object.animal.AbstractAnimal;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * The Class RatesColor.
 *
 * @author Laskin
 * @version 1.0
 * @since 28.09.13 23:44
 */
public class RatesColor implements PrimitiveColor {

    private double value;
    private java.util.List<RatesColorPoint> colors;

    /**
     * Конструктор.
     * @param value значение, соответствующее цвету.
     * @param aColors массив цветов
     */
    public RatesColor(final double value, final RatesColorPoint... aColors) {
        this.value = value;
        this.colors = new ArrayList<>(aColors.length);
        Collections.addAll(colors, aColors);
    }

    @Override
    public double getValue(Map<String, Double> prop) {
        return value;
    }

    @Override
    public Color calcColor(Map<String, Double> prop) {
        double r = 0;
        double g = 0;
        double b = 0;
        double sumFactor = 0.0;
        for (PrimitiveColor color : colors) {
            Color col = color.calcColor(prop);
            double factor = color.getValue(prop);
            sumFactor += factor;
            r += col.getRed() * factor;
            g += col.getGreen() * factor;
            b += col.getBlue() * factor;
        }
        return new Color(
            AbstractAnimal.colorNormal(r / sumFactor),
            AbstractAnimal.colorNormal(g / sumFactor),
            AbstractAnimal.colorNormal(b / sumFactor)
        );
    }
}
