/**
 * @(#)StatObject.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.statistic;

/**
 * The Class StatObject.
 * @author vladimir.laskin
 * @version 1.0
 */
public class StatObject {

    protected int count;
    protected String name;

    /**
     * Конструктор.
     * @param name имя параметра
     */
    public StatObject(final String name) {
        this.count = 0;
        this.name = name;
    }

    /**
     *
     */
    public void statistic() {
        count++;
    }

    /**
     * Заполняем StringBuilder содержимым.
     * @param sb StringBuilder object
     * @return заполненный StringBuilder object
     */
    public StringBuilder createJson(final StringBuilder sb) {
        sb.append("\"count\":").append(count);
        return sb;
    }

    /**
     * Удаляем последние символы, если это 'str'.
     * @param sb StringBuilder
     * @param str символы для удаления
     * @return StringBuilder
     */
    public static StringBuilder removeLast(final StringBuilder sb, final String str) {
        final int start = sb.length() - str.length();
        final int end = sb.length();
        if (str.equals(sb.substring(start, end))) {
            sb.delete(start, end);
        }
        return sb;
    }
}
