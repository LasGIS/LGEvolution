/**
 * @(#)ForceVector.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.organs;

/**
 * Вектор силы. Почему то животное тянет в эту сторону...
 *
 * @author Laskin
 * @version 1.0
 * @since 23.03.13 18:38
 */
public class ForceVector {
    double fx;
    double fy;
    double force;

    /**
     * Конструктор Вектора силы по расстоянию до цели.
     * @param dx проекция на X
     * @param dy проекция на Y
     * @param mass произведение гравитирующих масс
     */
    public ForceVector(final double dx, final double dy, double mass) {
        force = mass / (dx * dx + dy * dy);
        double angle = Math.atan2(dx, dy);
        fx = force * Math.sin(angle);
        fy = force * Math.cos(angle);
    }

    /**
     * Создание силы в произвольном направлении.
     * @param forceMax максимальная абсолютная величина силы
     */
    public ForceVector(final double forceMax) {
        if (forceMax > 0) {
            double angle = Math.random() * Math.PI * 2;
            force = forceMax * Math.random();
            fx = force * Math.sin(angle);
            fy = force * Math.cos(angle);
        } else {
            force = 0;
            fx = 0;
            fy = 0;
        }
    }

    /**
     * добавляем вектор силы.
     * @param vector вектор силы для добавления
     * @return этот вектор силы
     */
    public ForceVector add(ForceVector vector) {
        fx += vector.fx;
        fy += vector.fy;
        calc();
        return this;
    }

    /**
     * вычитаем вектор силы.
     * @param vector вектор силы для вычитания
     * @return этот вектор силы
     */
    public ForceVector sub(ForceVector vector) {
        fx -= vector.fx;
        fy -= vector.fy;
        calc();
        return this;
    }

    /**
     * перевычисляем абсолютную величину по проекциям.
     */
    public void calc() {
        force = Math.sqrt(fx * fx + fy * fy);
    }

    public double getFx() {
        return fx;
    }

    public double getFy() {
        return fy;
    }

    public double getForce() {
        return force;
    }

    /**
     * новое значение силы с соблюдением направления.
     * @param newForce новое значение силы
     */
    public void setForce(double newForce) {
        fx = fx * newForce / force;
        fy = fy * newForce / force;
        force = newForce;
    }

    /**
     * Вернуть направление.
     * @return направление
     */
    public double getCourse() {
        return Math.atan2(fx, fy);
    }

}
