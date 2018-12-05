/**
 * @(#)Parameters.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.map.element;

import com.lasgis.evolution.map.Matrix;

/**
 * Сюда собираем все коэффициенты пересчёта.
 *
 * @author VLaskin
 * @version 1.0
 * @since 10.02.2008 11:37:29
 */
public final class Parameters {

    /** Коэффициент пересчёта из градусов в метры. */
    public static final double GRADE_2_METER = 100000.0000000;

    /** максимальное значение широты. */
    public static final double MAX_LATITUDE = Matrix.MATRIX_SIZE_X * Matrix.CELL_SIZE;

    /** максимальное значение долготы. */
    public static final double MAX_LONGITUDE = Matrix.MATRIX_SIZE_Y * Matrix.CELL_SIZE;

    /** минимальное значение широты. */
    public static final double MIN_LATITUDE = 0.0;

    /** минимальное значение долготы. */
    public static final double MIN_LONGITUDE = 0.0;

    /**
     * Avoid to open class indirectly.
     */
    private Parameters() {
    }
}