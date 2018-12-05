/**
 * @(#)EvolutionEventRegime.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.panels;

/**
 * Событие в системе LasGIS Java.
 * Описание изменения режима.
 *
 * @author VLaskin
 * @version 1.0 (01.11.2006 0:34:40)
 */
public class EvolutionEventRegime implements EvolutionEvent {

    // валидные типы режимов
    /** режим навигации по карте. */
    public static final int NAVIGATION = 0;
    /** режим добавления животного или растения на карту. */
    public static final int APPEND_ENTITY = 1;

    /** Список режимов в системе. */
    public static final String[] REGIME_LIST = {
        "Навигация",
        "Добавить сущность"
    };

    /** номер режима. */
    private int number;

    /** описание режима. */
    private String descriptor;

    /**
     * Вернуть тип события.
     * @return тип события как integer
     */
    public int geTypeEvent() {
        return EvolutionEvent.REGIME_CHANGED;
    }

    /**
     * конструктор события режима.
     * @param number номер режима
     * @param descriptor описание режима
     */
    public EvolutionEventRegime(final int number, final String descriptor) {
        this.number = number;
        this.descriptor = descriptor;
    }

    /**
     * Вернуть номер режима.
     * @return номер режима
     */
    public int getNumber() {
        return number;
    }

    /**
     * Вернуть описание режима.
     * @return описание режима
     */
    public String getDescriptor() {
        return descriptor;
    }

}
