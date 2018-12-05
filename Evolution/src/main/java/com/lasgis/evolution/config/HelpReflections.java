/**
 * @(#)HelpReflections.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2014 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.config;

import com.lasgis.evolution.object.AnimalManagerBehaviour;
import com.lasgis.evolution.object.PlantBehaviour;
import com.lasgis.evolution.object.animal.brain.AbstractRoutine;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The Class HelpReflections.
 * @author Vladimir Laskin
 * @version 1.0
 */
public final class HelpReflections {

    private static final Reflections REFLECTIONS = new Reflections("com.lasgis");
    private static final Set<Class<? extends AbstractRoutine>> ALL_ROUTINE_CLASSES =
        REFLECTIONS.getSubTypesOf(AbstractRoutine.class);
    private static final Map<String, Class<? extends AbstractRoutine>> MAP_ROUTINE_CLASSES = new ConcurrentHashMap<>();
    private static final Set<Class<? extends AnimalManagerBehaviour>> ALL_ANIMAL_CLASSES =
        REFLECTIONS.getSubTypesOf(AnimalManagerBehaviour.class);
    private static final Set<Class<? extends PlantBehaviour>> ALL_PLANT_CLASSES =
        REFLECTIONS.getSubTypesOf(PlantBehaviour.class);

    static {
        for (Class<? extends AbstractRoutine> routine : ALL_ROUTINE_CLASSES) {
            final String key = routine.getName();
            final int last = key.lastIndexOf('.');
            final String name = (last >= 0) ? key.substring(last + 1) : key;
            MAP_ROUTINE_CLASSES.put(name, routine);
        }
    }

    private HelpReflections() { }

    public static Reflections getReflections() {
        return REFLECTIONS;
    }

    public static Set<Class<? extends PlantBehaviour>> getPlantClasses() {
        return ALL_PLANT_CLASSES;
    }

    public static Set<Class<? extends AnimalManagerBehaviour>> getAnimalClasses() {
        return ALL_ANIMAL_CLASSES;
    }

    public static Set<Class<? extends AbstractRoutine>> getRoutineClasses() {
        return ALL_ROUTINE_CLASSES;
    }

    /**
     *
     * @param className
     * @return
     */
    public static Class<? extends AbstractRoutine> getRoutineClass(final String className) {
        return MAP_ROUTINE_CLASSES.get(className);
    }
}
