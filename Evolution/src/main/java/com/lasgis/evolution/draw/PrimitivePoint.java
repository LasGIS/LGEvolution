/**
 * @(#)PrimitivePoint.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.draw;

/**
 * The Class PrimitivePoint.
 * @author vladimir.laskin
 * @version 1.0
 */
public class PrimitivePoint {
    private double x;
    private double y;

    /**
     * Конструктор.
     * @param x
     * @param y
     */
    public PrimitivePoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void incX(double v) {
        x += v;
    }

    public void incY(double v) {
        y += v;
    }
}
