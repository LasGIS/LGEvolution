/**
 * @(#)PrimitiveColor.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.draw;

import java.awt.*;
import java.util.Map;

/**
 * The Class PrimitiveColor.
 * @author vladimir.laskin
 * @version 1.0
 */
public interface PrimitiveColor {

    /**
     * Получить текущий цвет.
     * @param prop свойства
     * @return цвет
     */
    Color calcColor(Map<String, Double> prop);

    /**
     * Значение, соответствующее этому цвету.
     * @return значение
     */
    double getValue(final Map<String, Double> prop);
}
