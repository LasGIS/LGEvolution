/*
 * @(#)AniPoint.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.animation.map.object;

/**
 * Точка во времени.
 * @author Vladimir Laskin
 * @version 1.0
 * @since 27.01.2011 : 15:03:50
 */
public class AniPoint {

    /** координата X. */
    private double x;

    /** координата Y. */
    private double y;

    /** время в миллисекундах. */
    private double time;

    /**
     * Простой конструктор.
     */
    public AniPoint() {
    }

    /**
     * Конструктор.
     * @param crdX координата X
     * @param crdY координата Y
     * @param aTime время в миллисекундах
     */
    public AniPoint(
        double crdX,
        double crdY,
        double aTime
    ) {
        this.x = crdX;
        this.y = crdY;
        this.time = aTime;
    }

    /**
     * @return координата X
     */
    public double getX() {
        return x;
    }

    /**
     * Set координата X.
     * @param x координата X
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return координата Y.
     */
    public double getY() {
        return y;
    }

    /**
     * Set координата Y.
     * @param y координата Y
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * @return время в миллисекундах
     */
    public double getTime() {
        return time;
    }

    /**
     * Set время в миллисекундах.
     * @param time время в миллисекундах
     */
    public void setTime(double time) {
        this.time = time;
    }
}
