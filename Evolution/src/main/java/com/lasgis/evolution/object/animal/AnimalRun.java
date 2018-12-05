/**
 * @(#)AnimalRun.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal;

import com.lasgis.evolution.map.Cell;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;

/**
 * The Class AnimalRun.
 *
 * @author Laskin
 * @version 1.0
 * @since 25.02.13 22:36
 */
@Slf4j
public class AnimalRun implements Runnable {

    private final Map<String, AbstractAnimal> animals = new ConcurrentHashMap<>();
    private AbstractAnimalManager manager;

    /**
     * Constructor.
     * @param manager сохраняем менеджер для рождения и умирания
     */
    public AnimalRun(final AbstractAnimalManager manager) {
        this.manager = manager;
    }

    @Override
    public void run() {
        try {
            for (;;) {
                final List<AbstractAnimal> toRemove = new ArrayList<>();
                for (AbstractAnimal animal : animals.values()) {
                    if (!animal.run()) {
                        toRemove.add(animal);
                    }
                }
                boolean isBreak = false;
                for (AbstractAnimal animal : toRemove) {
                    isBreak = deleteAnimal(animal);
                }
                if (isBreak) {
                    break;
                }
                Thread.sleep(100);
            }

        } catch (final RejectedExecutionException ex) {
            log.error("RejectedExecutionException: " + ex.getMessage(), ex);
        } catch (final InterruptedException ex) {
            log.error("InterruptedException: " + ex.getMessage(), ex);
        } catch (final Throwable ex) {
            log.error(ex.getMessage(), ex);
        }
        manager.killAnimalRun(this);
    }

    public Collection<AbstractAnimal> getAnimals() {
        return animals.values();
    }

    /**
     * Добавляем глупое животное.
     * @param animal животное
     */
    public void addAnimal(final AbstractAnimal animal) {
        animals.put(animal.getUniqueName(), animal);
    }

    /**
     * Удаляем бедное животное.
     * @param animal животное
     * @return true, если надо закрыть лавочку
     */
    public boolean deleteAnimal(final AbstractAnimal animal) {
        final Cell cell = animal.getCell();
        cell.element(manager.getName()).decValue();
        cell.removeAnimal(animal);
        manager.decrementCount();
        //log.debug("Умер " + animal.getUniqueName() + ", осталось " + manager.getCount()
        //    + " = " + animal.getJsonInfo("умер"));
        animals.remove(animal.getUniqueName());
        if (animals.isEmpty()) {
            //log.debug("<<<<<<<<< ----Закрываем AnimalRun---- >>>>>>>>>");
            manager.killAnimalRun(this);
            return true;
        } else {
            return false;
        }
    }
}
