/**
 * @(#)Routine.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2014 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain;

/**
 * Стратегия поведения животного в определённых ситуациях.<br/>
 * Короткий алгоритм поведения животного в данной ситуации, например:<br/>
 * ПОЕДАНИЕ: 1 - найти еду; 2 - дойти до неё; 3 - поесть; 4 - если сыт, то выйти из стратегии.<br/>
 * ТРЕВОГА: 1 - определить источник опасности; 2 - бежать; 3 - выйти из стратегии если убежал.<br/>
 * . . . <br/>
 *
 * @author Vladimir Laskin
 * @version 1.0
 */
public interface Routine {

    /** Место, где находится вкусная еда. */
    String FOOD_CELL = "ячейка_c_едой";
    /** Место, где находится вкусная еда. */
    String FOOD_NAME = "название_еды";

    /**
     * Элементарный акт поведения.
     * @return true - завершение стратегии, false - стратегия продолжается.
     * @throws RoutineRunTimeException Routine Run Time Exception
     */
    boolean act();

    /**
     * Элементарный акт поведения с отладкой (запись прохождения в log).
     * @return true - завершение стратегии, false - стратегия продолжается.
     * @throws RoutineRunTimeException Routine Run Time Exception
     */
    boolean debugAct();

    /**
     * Очищает состояние (например после break операции).
     */
    void clear();

}
