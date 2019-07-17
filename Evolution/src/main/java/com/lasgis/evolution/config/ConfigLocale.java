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
import com.lasgis.util.ResourceLoader;
import lombok.Data;

import java.io.File;

/**
 * Это класс для хранения локальных параметров.
 * Класс должен содержать в себе все локальные
 * параметры и только параметры.
 * За чтение и запись новой локальной конфигурации
 * в формате XML отвечает класс
 * {@link XmlStorageLocale}.
 *
 * @author vlaskin
 * @version 1.0
 * @since 27.10.2008 : 17:15:31
 */
@Data
public class ConfigLocale {

    /** Единственный объект класса ConfigLocale. */
    private static ConfigLocale singleton = null;

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
    private void fillDefault() {
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
     * Получаем созданный ранее объект локальной конфигурации.
     * Если ранее объект не был создан, то выбираем локальную конфигурацию по умолчанию.
     * @return обязательно заполненный объект с локальной конфигурацией
     */
    public static ConfigLocale getLocale() {
        if (singleton == null) {
            final String fileName = ResourceLoader.getResource("default.config.locale");
            final String normalFileName = HelpFileRead.getNameOnly(fileName) + "." + REGULAR_LOCALE_EXTENSION;
            singleton = new ConfigLocale(normalFileName);
            // читаем по новому
            final File file = new File(singleton.getFileName());
            if (file.exists() && file.isFile()) {
                XmlStorageLocale.load(singleton, file);
            }
        }
        return singleton;
    }

    /**
     * Сохраняем текущее состояние.
     */
    public static void save() {
        XmlStorageLocale.save(getLocale());
    }
}