/**
 * @(#)AnimalBehaviour.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object;

import com.lasgis.evolution.map.Cell;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The Class AnimalBehaviour.
 * @author vladimir.laskin
 * @version 1.0
 */
public interface AnimalBehaviour {

    /**
     * Уникальное имя животного.
     * В простейшем случае название типа + номер.
     * @return уникальное имя животного.
     */
    String getUniqueName();

    /**
     * элементарное движение животного.
     * @return if false then died
     */
    boolean run();

    /**
     * Возвращаем широту.
     * @return широта
     */
    double getLatitude();

    /**
     * устанавливаем широту.
     * @param latitude широта
     */
    void setLatitude(double latitude);

    /**
     * изменяем широту.
     * @param value значение изменения широты
     */
    void changeLatitude(double value);

    /**
     * Возвращаем долготу.
     * @return долгота
     */
    double getLongitude();

    /**
     * устанавливаем долготу.
     * @param longitude долгота
     */
    void setLongitude(double longitude);

    /**
     * изменяем долготу.
     * @param value значение изменения долготы
     */
    void changeLongitude(double value);

    /**
     * получить массу животного.
     * @return массу животного
     */
    double getMass();

    /**
     * получить энергию животного.
     * @return энергию животного
     */
    double getEnergy();

    /**
     * Относительный возраст животного
     * в процентах от максимального.
     * @return относительный возраст животного
     */
    double getRelativeAge();

    /**
     * Вернуть ячейку, над которой в данный момент
     * находится животное.
     * @return ячейка матрицы
     */
    Cell getCell();

    /**
     * Вернуть манагера этого животного.
     * @return Animal Manager
     */
    AnimalManagerBehaviour getManager();

    /**
     * Вернуть признак выбора.
     * @return признак выбора.
     */
    boolean isSelected();

    /**
     * Установить признак выбора.
     * @param selected признак выбора
     */
    void setSelected(boolean selected);

    /**
     * Показать информацию о животном.
     * @param sb StringBuilder
     * @return тот же StringBuilder
     */
    StringBuilder getInfo(StringBuilder sb);

    /**
     * Показать информацию о животном.
     * @param state текущее состояние животного
     * @return StringBuilder class
     */
    StringBuilder getJsonInfo(String state);

    /**
     * Получить JSONObject объект для записи.
     * @return JSONObject объект
     */
    JSONObject getJsonObject() throws JSONException;
}
