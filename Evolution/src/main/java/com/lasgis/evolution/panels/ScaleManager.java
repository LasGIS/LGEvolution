/**
 * @(#)ScaleManager.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.panels;

import java.awt.*;

/**
 * Класс, который управляет масштабом.
 * @author vlaskin
 * @version 1.0
 * @since 21.03.2008 : 19:29:50
 */
public final class ScaleManager {

    /** Размер экранного пикселя в метрах. */
    public static final double PIXEL_IN_METR; // = 0.0002645833333333333;

    static {
        final Toolkit tk = Toolkit.getDefaultToolkit();
        final int pixelsPerInch = tk.getScreenResolution();
        PIXEL_IN_METR = 0.0254 / pixelsPerInch;
    }

    /** Единственный экземпляр данного класса. */
    private static final ScaleManager SINGLETON = new ScaleManager();

    /** текущее значение индекса масштаба. */
    private int scaleIndex = 7;

    /** допустимые масштабы карты. */
    private static int[] scaleTypes = {
        /*20, 30, 50, 70, 100,                     // 0 level
        150, 200, 300, 500,                      // 1 level
        700, 1000, 1500, 2000,                   // 2 level
        3000, 5000, 7000, 10000,                 // 3 level
        15000, 20000,*/ 30000, 50000,              // 4 level
        70000, 100000, 150000, 200000,           // 5 level
        300000, 500000/*, 700000, 1000000          // 6 level
        1500000, 2000000, 3000000, 5000000,      // 7 level
        7000000, 10000000, 15000000, 20000000,   // 8 level
        30000000, 50000000, 70000000, 100000000, // 9 level
        150000000, 200000000 */
    };

    /**
     * Маппирование на уровень карты.
     */
    private static int[] level = {
//        0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4,
        4, 4, 5, 5, 5, 5, 6, 6
//        , 6, 6, 7, 7, 7, 7, 8, 8, 8, 8, 9, 9, 9, 9, 9, 9
    };

    /**
     * Единственный закрытый конструктор.
     */
    private ScaleManager() {
    }

    /**
     * Доступ к единственному экземпляру класса.
     * @return ScaleManager
     */
    public static ScaleManager getScaleManager() {
        return SINGLETON;
    }

    /**
     * Установить масштаб по индексу.
     * @param aScaleIndex индекс масштаба
     * @return значение размера экранной точки
     */
    public double setScale(final int aScaleIndex) {
        if ((aScaleIndex >= 0) && (aScaleIndex < scaleTypes.length)) {
            this.scaleIndex = aScaleIndex;
        } else {
            throw new IndexOutOfBoundsException("Превышение индекс масштаба");
        }
        return getDelta();
    }

    /**
     * Установить нормированный масштаб.
     * @param scale значение масштаба
     * @return значение размера экранной точки
     */
    public double setScale(final double scale) {
        final double ind = Math.log10(scale);
        scaleIndex = (int) Math.floor(ind * 6 + 0.5) - 27;
        return getDelta();
    }

    /**
     * вернуть текущий масштаб.
     * @return значение размера экранной точки
     */
    public double getScale() {
        return scaleTypes[scaleIndex];
    }

    /**
     * Установить нормированный значение размера экранной точки.
     * @param delta значение размера экранной точки
     * @return значение размера экранной точки
     */
    public double setDelta(final double delta) {
        return setScale(delta / PIXEL_IN_METR);
    }

    /**
     * вернуть текущий масштаб.
     * @return значение размера экранной точки
     */
    public double getDelta() {
        return scaleTypes[scaleIndex] * PIXEL_IN_METR;
    }

    /**
     * Вернуть текущий уровень карты.
     *
     * В общем случае уровней может быть 10:
     * <table>
     * <tr><td>уровень</td><td>Основной масштаб</td>
     * <td> рекомендуемый размер блока [град°мин’сек”]</td></tr>
     * <tr><td>9</td><td>1:100 000 000</td><td>360°</td></tr>
     * <tr><td>8</td><td>1:20 000 000</td><td>72°</td></tr>
     * <tr><td>7 </td><td>1:5 000 000</td><td>18°</td></tr>
     * <tr><td>6</td><td>1:1 000 000</td><td>3°36’</td></tr>
     * <tr><td>5</td><td>1:200 000</td><td>54’</td></tr>
     * <tr><td>4</td><td>1:50 000</td><td>10’48”</td></tr>
     * <tr><td>3</td><td>1:10 000</td><td>2’42”</td></tr>
     * <tr><td>2</td><td>1:2 000</td><td>32.4”</td></tr>
     * <tr><td>1</td><td>1:500</td><td>8.1”</td></tr>
     * <tr><td>0</td><td>1:100</td><td>1.62”</td></tr>
     * </table>
     * @return текущий уровень карты
     */
    public int getLevel() {
        return level[scaleIndex];
    }

    /**
     * Увеличить текущий масштаб карты.
     * @return новый масштаб
     */
    public double increaseScale() {
        if (scaleIndex < scaleTypes.length - 1) {
            scaleIndex++;
        }
        return getDelta();
    }

    /**
     * Увеличить текущий масштаб карты.
     * @return новый масштаб
     */
    public double decreaseScale() {
        if (scaleIndex > 0) {
            scaleIndex--;
        }
        return getDelta();
    }

}