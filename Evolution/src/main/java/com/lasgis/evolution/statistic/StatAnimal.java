/**
 * @(#)StatAnimal.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.statistic;

import com.lasgis.evolution.object.AnimalBehaviour;
import com.lasgis.evolution.object.Info;
import com.lasgis.evolution.object.InfoType;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class StatAnimal.
 * @author vladimir.laskin
 * @version 1.0
 */
@Slf4j
public class StatAnimal extends StatObject {

    Map<String, StatValue> organs = new HashMap<>();

    public StatAnimal(final String name) {
        super(name);
    }

    /**
     * Запускаем сбор статистики для конкретного животного
     * @param animal животное
     */
    public void statistic(final AnimalBehaviour animal) {
        super.statistic();
        statistic(animal, animal.getClass());
    }

    private void statistic(final Object obj, final Class<?> aClass) {
        final Class<?> superclass = aClass.getSuperclass();
        Info info;
        if (superclass != null) {
            info = superclass.getAnnotation(Info.class);
            if (info != null && (Arrays.binarySearch(info.type(), InfoType.STAT) >= 0)) {
                statistic(obj, superclass);
            }
        }
        final Field[] annotations = aClass.getDeclaredFields();
        for (Field field : annotations) {
            info = field.getAnnotation(Info.class);
            if (info != null && (Arrays.binarySearch(info.type(), InfoType.STAT) >= 0)) {
                String name = info.name();
                if (name.length() == 0) {
                    name = field.getName();
                }

                Double value = null;
                field.setAccessible(true);
                try {
                    switch (field.getType().getName()) {
                        case "int" :
                            if (info.rate() == 1) {
                                value = (double) field.getInt(obj);
                            } else {
                                value = field.getDouble(obj) * info.rate();
                             }
                            break;
                        case "double" :
                            value = field.getDouble(obj) * info.rate();
                            break;
                        case "java.lang.String" :
                            break;
                        default:
                            final Object fieldObj = field.get(obj);
                            if (fieldObj instanceof Map) {
                                @SuppressWarnings("unchecked")
                                final Map<Object, Object> map = (Map<Object, Object>) fieldObj;
                                for (Map.Entry<Object, Object> entry : map.entrySet()) {
                                    final String key = name + '.' + entry.getKey().toString();
                                    Double valDouble = null;
                                    final Object val = entry.getValue();
                                    if (val instanceof Double) {
                                        valDouble = (Double) val * info.rate();
                                    } else if (val instanceof Integer) {
                                        valDouble = (Integer) val * info.rate();
                                    }
                                    StatValue statMap = organs.get(key);
                                    if (statMap == null) {
                                        statMap = new StatValue(key);
                                        organs.put(key, statMap);
                                    }
                                    if (valDouble != null) {
                                        statMap.statistic(valDouble);
                                    }
                                }
                            } else {
                                statistic(fieldObj, fieldObj.getClass());
                            }
                            break;
                    }
                } catch (final IllegalAccessException ex) {
                    log.error(ex.getMessage(), ex);
                }
                if (value != null) {
                    StatValue stat = organs.get(name);
                    if (stat == null) {
                        stat = new StatValue(name);
                        organs.put(name, stat);
                    }
                    stat.statistic(value);
                }
            }
        }
    }

    /**
     * Заполняем StringBuilder содержимым.
     * @param sb StringBuilder object
     * @return заполненный StringBuilder object
     */
    public StringBuilder createJson(final StringBuilder sb) {
        sb.append("  \"").append(name).append("\":{\n    ");
        super.createJson(sb).append(",\n");
        for (Map.Entry<String, StatValue> entry : organs.entrySet()) {
            entry.getValue().createJson(sb);
        }
        removeLast(sb, ",\n");
        sb.append("\n  }, \n");
        return sb;
    }

}
