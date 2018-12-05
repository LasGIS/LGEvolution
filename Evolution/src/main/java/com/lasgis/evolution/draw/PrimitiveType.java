/**
 * @(#)PrimitiveType.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.draw;

/**
 * Primitive Type enumerations.
 * @author vladimir.laskin
 * @version 1.0
 */
public enum PrimitiveType {
    /** только линия. */
    line,
    /** только плоскость. */
    area,
    /** Сначала рисуем плоскость, а затем линию по тем же точкам. */
    areaLine
}
