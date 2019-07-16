/**
 * @(#)ConfigLocale.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.config;

import com.lasgis.evolution.map.Matrix;
import com.lasgis.evolution.panels.ScaleManager;
import lombok.Data;

/**
 * Это класс для хранения локальных параметров.
 * Класс должен содержать в себе все локальные
 * параметры и только параметры.
 * За создание и возвращение объекта класса отвечает
 * статический класс {@link com.lasgis.evolution.config.ConfigLocaleFactory}.
 * За чтение и запись новой локальной конфигурации
 * в формате XML отвечает класс
 * {@link com.lasgis.evolution.config.XmlStorage}.
 *
 * @author vlaskin
 * @version 1.0
 * @since 27.10.2008 : 17:15:31
 */
@Data
public class ConfigLocale {

    /** Правильное расширение для файла локальной конфигурации. */
    public static final String REGULAR_LOCALE_EXTENSION = "locale";

    /** Полный путь к файлу локальной конфигурации. */
    private String fileName;
    /** режим работы окна вывода карты. */
    private int regime;
    /** широта верхнего левого угла. */
    private double latitude;
    /** долгота верхнего левого угла. */
    private double longitude;
    /** размер пикселя в координатах карты. */
    private double delta;

    /**
     * Конструктор.
     * @param configFileName Полный путь к файлу локальной конфигурации
     */
    public ConfigLocale(String configFileName) {
        setFileName(HelpFileRead.getNameOnly(configFileName) + "." + REGULAR_LOCALE_EXTENSION);
        fillDefault();
    }

    /**
     * Заполняем поля по умолчанию.
     */
    public void fillDefault() {
        // широта верхнего левого угла
        latitude = 32.0 * Matrix.CELL_SIZE;
        // долгота верхнего левого угла
        longitude = -29.0 * Matrix.CELL_SIZE;
        // размер пикселя в координатах карты
        ScaleManager sm = ScaleManager.getScaleManager();
        sm.setScale(100000.);
        delta = sm.getDelta();
    }

    /**
     * Дублируем вызов Фабрики для создания и заполнения.
     * @return обязательно заполненный объект
     * с локальной конфигурацией
     */
    public static ConfigLocale getLocale() {
        return ConfigLocaleFactory.getLocale();
    }

    /**
     *
     */
    public static void save() {
        ConfigLocaleFactory.saveLocale();
    }
}