/**
 * @(#)AnimalManipulator.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2014 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object;

/**
 * The Class AnimalManipulator.
 * @author Vladimir Laskin
 * @version 1.0
 */
public interface AnimalManipulator {

    /**
     * Обрабатываем динамичные объекты (например, животные).
     * @param animal динамичные объекты
     */
    void manipulate(AnimalBehaviour animal);
}
