/**
 * @(#)PlantBehaviour.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object;

import com.lasgis.evolution.map.Cell;

import java.awt.*;

/**
 * Поведение растительных объектов. Растение стоит на одном
 * месте и может делать следующее:<br/>
 *  1 - увеличивается (растёт)<br/>
 *  2 - уменьшается и умирает (например траву съели
 *  или нехватка ресурсов)<br/>
 *  3 - распространяется на соседние клетки (размножение)<br/>
 *
 * @author Laskin
 * @version 1.0
 * @since 02.12.12 13:50
 */
@Info(type = InfoType.STAT)
public interface PlantBehaviour extends EvolutionConstants, LiveObjectElement {

    /**
     * Рисуем растение в текущем состоянии.
     * @param gr graphics contexts
     * @param rec квадрат, который относится к ячейке
     * @param cell ячейка для зарисовывания
     */
    void drawPlant(Graphics gr, Rectangle rec, Cell cell);

    /**
     * Initialise Plant Behaviour class.
     */
    void init();

    /**
     * Stop threads Plant Behaviour class.
     */
    void stop();

    /**
     * Подэлементы данного элемента.
     * @return список элементов
     */
    PlantBehaviour[] subElements();

    /**
     * Обрабатываем одну ячейку.
     * @param cell ячейка
     */
    void cellProcessing(Cell cell);
}
