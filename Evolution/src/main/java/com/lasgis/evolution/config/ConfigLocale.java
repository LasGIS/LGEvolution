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
        setFileName(configFileName);
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
     * Вернуть полный путь к файлу локальной конфигурации.
     * @return полный путь к файлу локальной конфигурации
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Сохранить полный путь к файлу локальной конфигурации.
     * @param fileName полный путь к файлу локальной конфигурации
     */
    public void setFileName(String fileName) {
        this.fileName = (
            HelpFileRead.getNameOnly(fileName) + "." + REGULAR_LOCALE_EXTENSION
        );
    }

    /**
     * Вернуть режим работы окна вывода карты.
     * @return режим работы окна вывода карты
     */
    public int getRegime() {
        return regime;
    }

    /**
     * Сохранить режим работы окна вывода карты.
     * @param regime режим работы окна вывода карты
     */
    public void setRegime(final int regime) {
        this.regime = regime;
    }

    /**
     * Вернуть широта верхнего левого угла.
     * @return широта верхнего левого угла
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Сохранить широта верхнего левого угла.
     * @param latitude широта верхнего левого угла
     */
    public void setLatitude(final double latitude) {
        this.latitude = latitude;
    }

    /**
     * Вернуть долгота верхнего левого угла.
     * @return долгота верхнего левого угла
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Сохранить долгота верхнего левого угла.
     * @param longitude долгота верхнего левого угла
     */
    public void setLongitude(final double longitude) {
        this.longitude = longitude;
    }

    /**
     * Вернуть размер пикселя в координатах карты.
     * @return размер пикселя в координатах карты
     */
    public double getDelta() {
        return delta;
    }

    /**
     * Сохранить размер пикселя в координатах карты.
     * @param delta размер пикселя в координатах карты
     */
    public void setDelta(final double delta) {
        this.delta = delta;
    }

    /**
     * Дублируем вызов Фабрики для создания и заполнения.
     * @return обязательно заполненный объект
     * с локальной конфигурацией
     */
    public static ConfigLocale getLocale() {
        return ConfigLocaleFactory.getLocale();
    }

}