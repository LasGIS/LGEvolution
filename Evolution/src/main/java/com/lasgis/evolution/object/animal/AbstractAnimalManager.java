/*
 * AbstractAnimalManager.java
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2020 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal;

import com.lasgis.evolution.map.Cell;
import com.lasgis.evolution.map.CellHelper;
import com.lasgis.evolution.object.AnimalBehaviour;
import com.lasgis.evolution.object.AnimalManagerBehaviour;
import com.lasgis.evolution.object.AnimalManipulator;
import com.lasgis.evolution.panels.Scalable;
import com.lasgis.util.ResourceLoader;
import lombok.extern.slf4j.Slf4j;

import javax.swing.Icon;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Общее поведение для животных.
 * @author vladimir.laskin
 * @version 1.0
 */
@Slf4j
public abstract class AbstractAnimalManager implements AnimalManagerBehaviour {

    private static final int MAXIMUM_THREAD_POOL_SIZE = ResourceLoader.getInteger("maximum.thread.pool.size");

    /** ThreadPoolExecutor. */
    private ThreadPoolExecutor executor;
    /** Список животных. */
    private final List<AnimalRun> animalRuns = new CopyOnWriteArrayList<>();
    private final BlockingQueue<Runnable> workQueue = new SynchronousQueue<>();
    // ArrayBlockingQueue<Runnable>(50);
    private AtomicInteger count = new AtomicInteger(0);
    private boolean isShow = true;

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public void init() {
        executor = new ThreadPoolExecutor(10, MAXIMUM_THREAD_POOL_SIZE, 60, TimeUnit.SECONDS, workQueue);
    }

    @Override
    public void stop() {
        executor.shutdownNow();
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean isShow() {
        return isShow;
    }

    @Override
    public void setShow(final boolean show) {
        isShow = show;
    }

    @Override
    public void createNewAnimal(final double latitude, final double longitude) {
        final AbstractAnimal animal = createNew(latitude, longitude);
        addNewAnimal(animal, true);
    }

    @Override
    public boolean killAnimal(final String uid) {
        for (AnimalRun animalRun : animalRuns) {
            for (AbstractAnimal animal : animalRun.getAnimals()) {
                if (animal.getUniqueName().equals(uid)) {
                    animalRun.deleteAnimal(animal);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void addNewAnimal(final AbstractAnimal animal, final boolean isHandMade) {
        try {
            if (animalRuns.size() < MAXIMUM_THREAD_POOL_SIZE) {
                final AnimalRun animalRun = new AnimalRun(this);
                animalRun.addAnimal(animal);
                animalRuns.add(animalRun);
                executor.execute(animalRun);
            } else {
                int minSize = Integer.MAX_VALUE;
                AnimalRun selAnimalRun = animalRuns.get(0);
                for (AnimalRun animalRun : animalRuns) {
                    final int size = animalRun.getAnimals().size();
                    if (minSize > size) {
                        minSize = size;
                        selAnimalRun = animalRun;
                    }
                }
                selAnimalRun.addAnimal(animal);
            }
            incrementCount();
            CellHelper.getCell(animal.getLatitude(), animal.getLongitude()).element(getName()).incValue();
            //log.debug((isHandMade ? "Стандартный " : "Новый ")
            //    + animal.getUniqueName() + " = " + animal.getJsonInfo("родился"));
        } catch (final Throwable ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    /**
     * Закрыть пустой worker.
     * @param animalRun пустой worker
     */
    public void killAnimalRun(final AnimalRun animalRun) {
        animalRuns.remove(animalRun);
        executor.remove(animalRun);
    }

    @Override
    public void drawAnimals(final Graphics graphics, final Scalable interval) {
        for (AnimalRun animalRun : animalRuns) {
            for (AbstractAnimal animal : animalRun.getAnimals()) {
                if (interval.isInto(animal.getLatitude(), animal.getLongitude())) {
                    animal.draw(graphics, interval);
                }
            }
        }
    }

    /**
     * Создаём новое животное.
     * @param latitude широта точки
     * @param longitude долгота точки
     * @return новое животное
     */
    protected abstract AbstractAnimal createNew(double latitude, double longitude);

    @Override
    public List<AnimalBehaviour> findAnimals(final Cell cell) {
        final List<AnimalBehaviour> list = new ArrayList<>();
        final double north = cell.getNorth();
        final double south = cell.getSouth();
        final double west = cell.getWest();
        final double east = cell.getEast();
        for (AnimalRun animalRun : animalRuns) {
            for (AnimalBehaviour animal : animalRun.getAnimals()) {
                final double latitude = animal.getLatitude();
                final double longitude = animal.getLongitude();
                if (north > latitude && latitude > south && east > longitude && longitude > west) {
                    animal.setSelected(true);
                    list.add(animal);
                }
            }
        }
        return list;
    }

    @Override
    public void manipulationAnimals(final AnimalManipulator manipulator) {
        for (AnimalRun animalRun : animalRuns) {
            for (AnimalBehaviour animal : animalRun.getAnimals()) {
                manipulator.manipulate(animal);
            }
        }
    }

    public int getCount() {
        return count.get();
    }

    /**
     * увеличиваем число всех животных данного типа.
     */
    public void incrementCount() {
        count.incrementAndGet();
    }

    /**
     * уменьшаем число всех животных данного типа.
     */
    public void decrementCount() {
        count.decrementAndGet();
    }
}
