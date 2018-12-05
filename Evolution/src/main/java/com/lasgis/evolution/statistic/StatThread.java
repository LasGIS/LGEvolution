/**
 * @(#)StatThread.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.statistic;

import com.lasgis.evolution.map.Cell;
import com.lasgis.evolution.map.CellIterator;
import com.lasgis.evolution.object.AnimalBehaviour;
import com.lasgis.evolution.object.AnimalManipulator;
import com.lasgis.evolution.object.Info;
import com.lasgis.evolution.object.InfoType;
import com.lasgis.evolution.object.LiveObjectManager;
import com.lasgis.evolution.object.PlantBehaviour;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Запускаем процедуру сохранения статистики.
 * @author vladimir.laskin
 * @version 1.0
 */
@Slf4j
public class StatThread implements Runnable {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss.SSS");
    private static final long SLEEP_TIME = 60000;
    long time = 0L;
    Map<String, StatValue> mapPlant = new HashMap<>();
    Map<String, StatAnimal> mapAnimal = new HashMap<>();
    static {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    @Override
    public void run() {
        long startTime;
        try {
            while (true) {
                // засыпаем на SLEEP_TIME
                Thread.sleep(SLEEP_TIME);
                time += SLEEP_TIME;
                startTime = System.currentTimeMillis();
                log.info("---------- Вывод Статистики (время = " + DATE_FORMAT.format(time) + ") ----------");
                loadPlant();
                mapAnimal.clear();
                final StringBuilder sb = new StringBuilder("{\n");
                sb.append("\"время\":").append(time).append(",\n");
                treatPlant(sb);
                sb.append(",\n");
                treatAnimal(sb);
                sb.append("}");
                log.info(sb.toString());
                time += System.currentTimeMillis() - startTime;
            }
        } catch (final InterruptedException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    /**
     * Получаем данные для обработки статических объектов.
     */
    private void loadPlant() {
        mapPlant.clear();
        for (PlantBehaviour plant : LiveObjectManager.PLANTS) {
            loadPlant(plant, plant.getClass());
            final PlantBehaviour[] subElements = plant.subElements();
            for (PlantBehaviour element : subElements) {
                loadPlant(element, element.getClass());
            }
        }
    }

    private void loadPlant(final Object obj, final Class<?> aClass) {
        Info info;
        final Class<?> superclass = aClass.getSuperclass();
        if (superclass != null) {
            info = superclass.getAnnotation(Info.class);
            if (info != null && (Arrays.binarySearch(info.type(), InfoType.STAT) >= 0)) {
                loadPlant(obj, superclass);
            }
        }
        final Class<?>[] interfaces = aClass.getInterfaces();
        for (Class<?> intFace : interfaces) {
            info = intFace.getAnnotation(Info.class);
            if (info != null && (Arrays.binarySearch(info.type(), InfoType.STAT) >= 0)) {
                loadPlant(obj, intFace);
            }
        }
        final Field[] annotations = aClass.getDeclaredFields();
        for (Field field : annotations) {
            info = field.getAnnotation(Info.class);
            if (info != null && (Arrays.binarySearch(info.type(), InfoType.STAT) >= 0)) {
                field.setAccessible(true);
                if ("java.lang.String".equals(field.getType().getName())) {
                    try {
                        final String key = (String) field.get(obj);
                        StatValue stat = mapPlant.get(key);
                        if (stat == null) {
                            stat = new StatValue(key);
                            stat.setRate(info.rate());
                            mapPlant.put(key, stat);
                        }
                    } catch (final IllegalAccessException ex) {
                        log.error(ex.getMessage(), ex);
                    }
                }
            }
        }
    }

    /** Обрабатываем статичные объекты.  **/
    private void treatPlant(final StringBuilder sb) {
        final CellIterator itr = CellIterator.getInterval();
        while (itr.hasNext()) {
            final Cell cell = itr.next();
            final Map<String, Double> map = cell.getMapElements();
            for (String key : map.keySet()) {
                final StatValue stat = mapPlant.get(key);
                if (stat != null) {
                    final double value = map.get(key);
                    if (value > 0.001 / stat.getRate()) {
                        stat.statistic(value);
                    }
                }
            }
        }
        sb.append("\"растения\" : {\n");
        for (Map.Entry<String, StatValue> entry : mapPlant.entrySet()) {
            final StatValue stat = entry.getValue();
            if (stat.count > 0) {
                stat.createJson(sb);
            }
        }
        StatObject.removeLast(sb, ",\n");
        sb.append("\n}");
    }

    /** обрабатываем динамичные объекты (животные). **/
    private void treatAnimal(final StringBuilder sb) {

        LiveObjectManager.manipulationAnimals(new AnimalManipulator() {
            @Override
            public void manipulate(final AnimalBehaviour animal) {
                final String key = animal.getManager().getName();
                StatAnimal stat = mapAnimal.get(key);
                if (stat == null) {
                    stat = new StatAnimal(key);
                    mapAnimal.put(key, stat);
                }
                stat.statistic(animal);
            }
        });
        sb.append("\"животные\" : {\n");
        for (Map.Entry<String, StatAnimal> entry : mapAnimal.entrySet()) {
            final StatAnimal stat = entry.getValue();
            stat.createJson(sb);
        }
        StatObject.removeLast(sb, ", \n");
        sb.append("\n}");
    }

}
