/**
 * @(#)NearPoint.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.map;

/**
 * Координаты ячейки с расстоянием до нулевой точки.
 * Используется для анализа и перемещения.
 *
 * @author Laskin
 * @version 1.0
 * @since 07.03.13 1:04
 */
public class NearPoint {
    int x;
    int y;
    double distance;

    /**
     * Конструктор.
     * @param x координата x на карте
     * @param y координата y на карте
     * @param distance дистанция в помидорах
     */
    public NearPoint(final int x, final int y, final double distance) {
        this.x = x;
        this.y = y;
        this.distance = distance;
    }

    public int getX() {
        return x;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(final int y) {
        this.y = y;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(final double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "NearPoint(x=" + x + ", y=" + y + ", distance=" + distance + ')';
    }
}
