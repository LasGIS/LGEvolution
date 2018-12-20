/*
 * com.lasgis.evolution.utils.ColorHelper (20/28/2018)
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2018 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.utils;

public final class ColorHelper {

    /**
     * Нормализация цвета (от 0 до 255).
     * @param value исходный цвет
     * @return нормализованный цвет
     */
    public static int colorNormal(final double value) {
        final int col = (int) value;
        if (col < 0) {
            return 0;
        } else if (col > 255) {
            return 255;
        } else {
            return col;
        }
    }


}
