/**
 * @(#)ParamColor.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.draw;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.lasgis.evolution.utils.ColorHelper.colorNormal;

/**
 * управление цветом по параметру.
 * @author vladimir.laskin
 * @version 1.0
 */
public class ParamColor implements PrimitiveColor {
    private String key;
    private double value;
    private List<PrimitiveColor> colors;

    /**
     * Конструктор.
     * @param key ключевое слово
     * @param value значение, соответствующее цвету.
     * @param aColors массив цветов
     */
    public ParamColor(final String key, final double value, final PrimitiveColor... aColors) {
        this.key = key;
        this.value = value;
        this.colors = new ArrayList<>(aColors.length);
        Collections.addAll(colors, aColors);
    }

    @Override
    public double getValue(Map<String, Double> prop) {
        return value;
    }

    /**
     * вычисляем цвет по параметрам.
     * @param prop массив свойств
     * @return полученный цвет
     */
    @Override
    public Color calcColor(Map<String, Double> prop) {
        double val = prop.get(key);
        PrimitiveColor cp1 = null;
        for (PrimitiveColor cp : colors) {
            if ((cp1 != null) && (cp1.getValue(prop) < val && val < cp.getValue(prop))) {
                double ratio = (val - cp1.getValue(prop)) / (cp.getValue(prop) - cp1.getValue(prop));
                return calcColor(cp1.calcColor(prop), cp.calcColor(prop), ratio);
            }
            cp1 = cp;
        }
        if (cp1 != null) {
            return cp1.calcColor(prop);
        }
        return Color.WHITE;
    }

    /**
     * Вычисляем цвет по рейтингу.
     * @param firstColor начальный цвет
     * @param lastColor конечный цвет
     * @param ratio рейтинг
     * @return вычисленный цвет
     */
    private Color calcColor(Color firstColor, Color lastColor, double ratio) {
        int red = colorNormal((firstColor.getRed() + (lastColor.getRed() - firstColor.getRed()) * ratio));
        int green = colorNormal((int) (firstColor.getGreen() + (lastColor.getGreen() - firstColor.getGreen()) * ratio));
        int blue = colorNormal((int) (firstColor.getBlue() + (lastColor.getBlue() - firstColor.getBlue()) * ratio));
        return new Color(red, green, blue);
    }
}
