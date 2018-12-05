/**
 * @(#)AnimalManagerBehaviour.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object;

import com.lasgis.evolution.map.Cell;
import com.lasgis.evolution.object.animal.AbstractAnimal;
import com.lasgis.evolution.panels.Scalable;

import java.awt.*;
import java.util.List;

/**
 * Поведение животных объектов. Животное движется,
 * поэтому для каждого животного используется один
 * объект, один thread. Животное может делать следующее:<br/>
 *  1 - питается и растёт. Если растение растёт на пустом месте,
 *  то животное должно обязательно что-то съесть для роста
 *  (например, растение [травоядные] или другое животное
 *  [плотоядные]);<br/>
 *  2 - худеет и умирает.
 *  (например траву съели или нехватка ресурсов);<br/>
 *  3 - движется т.е. меняет свои координаты.
 *  На передвижение расходуется энергия
 *  (масса тела при этом должна уменьшаться)<br/>
 *
 * @author Laskin
 * @version 1.0
 * @since 02.12.12 15:39
 */
public interface AnimalManagerBehaviour extends EvolutionConstants, LiveObjectElement {

    /**
     * Initialise Plant Behaviour class.
     */
    void init();

    /**
     * Stop threads Plant Behaviour class.
     */
    void stop();

    /**
     * Родить новое животное волшебным способом.
     * @param latitude широта точки
     * @param longitude долгота точки
     */
    void createNewAnimal(double latitude, double longitude);

    /**
     * Убить группу животных волшебным способом.
     * @param uid широта точки
     * @return true if животное убито и false if оно не найдено
     */
    boolean killAnimal(String uid);

    /**
     * Рисуем перемещения животных.
     * @param graphics контекст вывода.
     * @param interval квадратный диапазон, в который должно входить ячейки
     */
    void drawAnimals(Graphics graphics, Scalable interval);

    /**
     * Добавить новое животное в процесс.
     * @param animal новое животное
     * @param isHandMade true if create by god and false if was born from animal.
     */
    void addNewAnimal(AbstractAnimal animal, boolean isHandMade);

    /**
     * Найти все животные внутри данной ячейки.
     * @param cell ячейка
     * @return список животных
     */
    List<AnimalBehaviour> findAnimals(Cell cell);

    /**
     * Некоторая абстрактная манипуляция со всем списком животных.
     * @param manipulator объект манипулятора
     */
    void manipulationAnimals(AnimalManipulator manipulator);
}
