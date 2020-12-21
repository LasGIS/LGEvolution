/*
 * InfoType.java
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2020 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object;

/**
 * Тип обработки, для Info аннотации.
 *
 * @author Vladimir Laskin
 * @version 1.0
 */
public enum InfoType {
    /**
     * Это поле или класс нигде не участвует.
     */
    NONE,
    /**
     * Это поле или класс участвует в показе информации по объекту.
     */
    INFO,
    /**
     * Это поле или класс обрабатываются при разборе статистики.
     */
    STAT,
    /**
     * Это поле или класс сохраняется при сериализации.
     */
    SAVE
}
