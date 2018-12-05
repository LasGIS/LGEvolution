/**
 * @(#)LiveObjectManager.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object;

import com.lasgis.evolution.config.HelpReflections;
import com.lasgis.evolution.draw.Drawing;
import com.lasgis.evolution.map.CellIterator;
import com.lasgis.evolution.map.Matrix;
import com.lasgis.evolution.panels.Scalable;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Менеджер живых объектов.<br/>
 * Этот класс содержит и управляет списками растений и животных.
 * @author vladimir.laskin
 * @version 1.0
 */
@Slf4j
public final class LiveObjectManager {

    /**
     * Список всех классов, которые ответственны за растения.
     */
    public static final List<PlantBehaviour> PLANTS = new ArrayList<>();

    /**
     * Список всех классов, которые ответственны за животных.
     */
    public static final List<AnimalManagerBehaviour> ANIMALS = new ArrayList<>();

    static {
        try {
            for (Class<? extends PlantBehaviour> plantClass : HelpReflections.getPlantClasses()) {
                if (((plantClass.getModifiers() & Modifier.ABSTRACT) == 0)
                    && !plantClass.isMemberClass() && !plantClass.isAnonymousClass() && !plantClass.isLocalClass()) {
                    PLANTS.add(plantClass.newInstance());
                }
            }
            PLANTS.sort(Comparator.comparingInt(LiveObjectElement::getIndex));
            for (Class<? extends AnimalManagerBehaviour> animalClass : HelpReflections.getAnimalClasses()) {
                if ((animalClass.getModifiers() & Modifier.ABSTRACT) == 0) {
                    ANIMALS.add(animalClass.newInstance());
                }
            }
            ANIMALS.sort(new Comparator<AnimalManagerBehaviour>() {
                @Override
                public int compare(final AnimalManagerBehaviour o1, final AnimalManagerBehaviour o2) {
                    return o1.getIndex() - o2.getIndex();
                }
            });
        } catch (final InstantiationException | IllegalAccessException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    /**
     *
     */
    private LiveObjectManager() {
    }

    /**
     * Создать новое животное.
     * @param animalName общее название животного
     * @param x Координата по высоте
     * @param y Координата по ширине
     * @return true if all right
     */
    public static boolean addAnimal(final String animalName, final int x, final int y) {
        for (AnimalManagerBehaviour animal : ANIMALS) {
            if (animalName.equals(animal.getName())) {
                animal.createNewAnimal(x * Matrix.CELL_SIZE, y * Matrix.CELL_SIZE);
                return true;
            }
        }
        return false;
    }

    /**
     * Рисуем растения.
     * @param graphics контекст вывода.
     * @param interval квадратный диапазон, в который должны входить ячейки
     */
    public static void drawPlants(final Graphics graphics, final Scalable interval) {
        final Drawing drawing = new Drawing(graphics, interval);
        final CellIterator itr = CellIterator.getInterval(interval);
        drawing.fillRectangles(itr);
        for (PlantBehaviour plant : PLANTS) {
            if (plant.isShow()) {
                drawing.drawPlant(plant, itr);
                for (PlantBehaviour elem : plant.subElements()) {
                    if (elem.isShow()) {
                        drawing.drawPlant(elem, itr);
                    }
                }
            }
        }

    }

    /**
     * Рисуем животных.
     * @param graphics контекст вывода.
     * @param interval квадратный диапазон, в который должно входить ячейки
     */
    public static void drawAnimals(final Graphics graphics, final Scalable interval) {
        for (AnimalManagerBehaviour animal : ANIMALS) {
            if (animal.isShow()) {
                animal.drawAnimals(graphics, interval);
            }
        }
    }

    /**
     * Некоторая абстрактная манипуляция со всем списком животных.
     * @param manipulator AnimalManipulator
     */
    public static void manipulationAnimals(final AnimalManipulator manipulator) {
        for (AnimalManagerBehaviour animal : ANIMALS) {
            animal.manipulationAnimals(manipulator);
        }
    }
}
