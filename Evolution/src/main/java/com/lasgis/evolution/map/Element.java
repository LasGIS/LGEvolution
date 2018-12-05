/**
 * @(#)Element.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.map;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Элемент карты. Этот элемент содержит
 * числовое значение различных сущностей.
 * Double.doubleToLongBits and Double.longBitsToDouble conversions.
 * @author Laskin
 * @version 1.0
 * @since 06.06.2010 22:08:11
 */
public class Element {

    /** Значение элемента. */
    private AtomicLong value = new AtomicLong(0L);

    /**
     * Значение элемента.
     * @return значение элемента
     */
    public final double value() {
        return Double.longBitsToDouble(value.get());
    }

    public final double getValue() {
        return Double.longBitsToDouble(value.get());
    }

    /**
     * Установить значение.
     * @param newValue новое значение
     * @return новое значение
     */
    public final double setValue(final double newValue) {
        if (newValue > 0) {
            value.getAndSet(Double.doubleToLongBits(newValue));
        } else {
            value.getAndSet(Double.doubleToLongBits(0.0));
        }
        return Double.longBitsToDouble(value.get());
    }

    /**
     * Увеличить значение.
     * @return новое значение
     */
    public double incValue() {
        for (;;) {
            final long current = value.get();
            final long next = Double.doubleToLongBits(Double.longBitsToDouble(current) + 1);
            if (value.compareAndSet(current, next)) {
                return Double.longBitsToDouble(next);
            }
        }
    }

    /**
     * Увеличить значение на некоторую величину.
     * @param deltaValue величина увеличения
     * @return новое значение
     */
    public double incValue(final double deltaValue) {
        for (;;) {
            final long current = value.get();
            final long next = Double.doubleToLongBits(Double.longBitsToDouble(current) + deltaValue);
            if (value.compareAndSet(current, next)) {
                return Double.longBitsToDouble(next);
            }
        }
    }

    /**
     * Уменьшить значение.
     * @return новое значение
     */
    public double decValue() {
        if (value() > 0.00001) {
            for (;;) {
                final long current = value.get();
                double nextD = Double.longBitsToDouble(current);
                if (nextD > 1.0) {
                    nextD -= 1.0;
                } else {
                    nextD = 0.0;
                }
                final long next = Double.doubleToLongBits(nextD);
                if (value.compareAndSet(current, next)) {
                    return Double.longBitsToDouble(next);
                }
            }
        }
        return value();
    }

    /**
     * Уменьшить значение на некоторую величину.
     * @param deltaValue величина уменьшения
     * @return новое значение
     */
    public double decValue(final double deltaValue) {
        if (value() < deltaValue) {
            value.getAndSet(Double.doubleToLongBits(0.0));
        } else {
            for (;;) {
                final long current = value.get();
                double nextD = Double.longBitsToDouble(current);
                if (nextD > deltaValue) {
                    nextD -= deltaValue;
                } else {
                    nextD = 0.0;
                }
                final long next = Double.doubleToLongBits(nextD);
                if (value.compareAndSet(current, next)) {
                    return Double.longBitsToDouble(next);
                }
            }
        }
        return value();
    }

}
