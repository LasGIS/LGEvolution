/**
 * @(#)IInterval2D.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.map.element;

/**
 * Интервал на карте. В данном случае это
 * прямоугольник с координатами:
 * широты верхней, нижней и долготы правой, левой.
 *
 * @author VLaskin
 * @version 1.0
 * @since 09.02.2008 21:10:36
 */
public interface IInterval2D {

    /**
     * Вернуть северную широту интервала.
     * @return северная широта
     */
    double getNorth();

    /**
     * Вернуть южную широту интервала.
     * @return южная широта
     */
    double getSouth();

    /**
     * Вернуть западную долготу интервала.
     * @return западная долгота
     */
    double getWest();

    /**
     * Вернуть восточную долготу интервала.
     * @return восточная долгота
     */
    double getEast();

}