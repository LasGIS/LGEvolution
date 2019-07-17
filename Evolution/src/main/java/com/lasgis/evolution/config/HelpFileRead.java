/**
 * @(#)HelpFileRead.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.config;

import java.io.UnsupportedEncodingException;

/**
 * Класс статических утилит для разбора
 * прочитанного блока из файлов конфигурации.
 *
 * @author vlaskin
 * @version 1.0
 * @since Apr 26, 2006 : 6:38:12 PM
 */
public class HelpFileRead {

    /**
     * конструктор для утилитного класса.
     */
    private HelpFileRead() {
    }

    /** char set for DOS coding. */
    public static final String CHAR_SET_DOS = "CP866";
    /** char set for WINDOWS coding. */
    public static final String CHAR_SET_WIN = "windows-1251";
    /** маска для выделения байта. */
    public static final int MASK_OF_BYTE = 0x000000ff;

    /**
     * Вернуть значение int из dos буфера.
     * @param buf входной dos буфера
     * @param offset смещение в блоке
     * @return int значение
     */
    public static int toInt(byte[] buf, int offset) {
        return ((buf[offset] & MASK_OF_BYTE)
             | ((buf[offset + 1] & MASK_OF_BYTE) << 8)
             | ((buf[offset + 2] & MASK_OF_BYTE) << 16)
             | ((buf[offset + 3] & MASK_OF_BYTE) << 24));
    }

    /**
     * Вернуть значение int из dos буфера.
     * @param buf входной dos буфера
     * @return int значение
     */
    public static int toInt(byte[] buf) {
        return toInt(buf, 0);
    }

    /**
     * Вернуть значение short из dos буфера.
     * @param buf входной dos буфера
     * @param offset смещение в блоке
     * @return short значение
     */
    public static short toShort(byte[] buf, int offset) {
        return (short) ((buf[offset] & MASK_OF_BYTE)
                     | ((buf[offset + 1] & MASK_OF_BYTE) << 8));
    }

    /**
     * Вернуть значение short из dos буфера.
     * Последовательность байт в буфере - обратная.
     * Т.е. сначала идёт старший бай, а затем - младший.
     *
     * @param buf входной dos буфера
     * @param offset смещение в блоке
     * @return short значение
     */
    public static short toShortInv(byte[] buf, int offset) {
        return (short) ((buf[offset + 1] & MASK_OF_BYTE)
                     | ((buf[offset] & MASK_OF_BYTE) << 8));
    }

    /**
     * Вернуть значение short из dos буфера.
     * @param buf входной dos буфера
     * @return short значение
     */
    public static short toShort(byte[] buf) {
        return toShort(buf, 0);
    }

    /**
     * Вернуть строку из dos буфера.
     * @param buf входной dos буфера
     * @param offset смещение в буфере
     * @return строку
     */
    public static String toString(byte[] buf, int offset) {
        int len = 0;
        for (int i = offset; (i < buf.length) && (buf[i] != 0); i++) {
            len++;
        }
        try {
            return new String(buf, offset, len, CHAR_SET_DOS);
        } catch (UnsupportedEncodingException exe) {
            return "Unsupported Encoding";
        }
    }

    /**
     * Вернуть строку из dos буфера.
     * @param buf входной dos буфера
     * @return строку
     */
    public static String toString(byte[] buf) {
        return toString(buf, 0);
    }

    /**
     * Получить расширение для данного имени файла.<br/>
     * Если имя файла is null, то возвращается null.<br/>
     * Если у файла нет расширения,
     * то возвращается пустая строка ("").<br/>
     * Если с расширением всё нормально, то возвращается расширение.
     * @param fileName имя файла
     * @return расширение
     */
    public static String getExtension(String fileName) {
        if (fileName == null) {
            return null;
        }
        int ind = fileName.lastIndexOf('.');
        return ind > 0 ? fileName.substring(ind + 1) : "";
    }

    /**
     * Получить имя файла без расширения для данного имени файла.<br/>
     * Если имя файла is null, то возвращается null.<br/>
     * Если у файла нет расширения,
     * то возвращается пустая строка ("").<br/>
     * Если с расширением всё нормально, то возвращается расширение.
     * @param fileName полный путь к файлу
     * @return имя файла без расширения
     */
    public static String getNameOnly(String fileName) {
        if (fileName == null) {
            return null;
        }
        final int ind = fileName.lastIndexOf('.');
        return ind > 0 ? fileName.substring(0, ind) : fileName;
    }

}