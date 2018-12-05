/**
 * @(#)ConfigLocaleFactory.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.config;

import com.lasgis.util.ResourceLoader;

import java.io.File;

/**
 * Это фабрика для объекта класса локальных параметров.
 * Класс должен содержать в себе все локальные параметры.
 *
 * @author vlaskin
 * @version 1.0
 * @since 27.10.2008 : 17:15:31
 */
public class ConfigLocaleFactory {

    /**
     * Единственный объект класса LocaleFile.
     */
    private static ConfigLocale singleton = null;

    /**
     * Пустой конструктор.
     */
    private ConfigLocaleFactory() {
    }

    /**
     * Фабрика для создания и заполнения локальных параметров.
     * Для чтения и сохранения в новом формате используем класс
     * {@link com.lasgis.evolution.config.XmlStorage}.
     * @param fileName путь к файлу локальной конфигурации
     * @return обязательно заполненный объект
     * с локальной конфигурацией
     */
    public static ConfigLocale getLocaleFile(final String fileName) {
        File file;
        final String extension = HelpFileRead.getExtension(fileName);
        singleton = new ConfigLocale(fileName);
        if (
            ConfigLocale.REGULAR_LOCALE_EXTENSION.equalsIgnoreCase(extension)
        ) {
            // читаем по новому
            file = new File(fileName);
            if (file.exists() && file.isFile()) {
                XmlStorage.load(singleton, file);
            }
        } else {
            // заполняем новый
            singleton.fillDefault();
        }
        return singleton;
    }

    /**
     * Получаем созданный ранее объект локальной конфигурации.
     * Если ранее объект не был создан, то выбираем
     * локальную конфигурацию по умолчанию.
     * @return обязательно заполненный объект
     * с локальной конфигурацией
     */
    public static ConfigLocale getLocale() {
        if (singleton == null) {
            singleton = getLocaleFile(
                ResourceLoader.getResource("default.config.locale")
            );
        }
        return singleton;
    }
}