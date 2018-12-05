/**
 * @(#)Scalable.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.panels;

import com.lasgis.evolution.map.element.IInterval2D;

/**
 * Интерфейс, который обязует производить преобразование
 * координат в направлении из координат карты
 * в экранные координаты.
 * @author VLaskin
 * @version 1.0 (Feb 1, 2006 6:28:12 PM)
 */
public interface Scalable extends IInterval2D {

    /**
     * Вернуть экранную координату X.
     * @param latit широта
     * @param longit долгота
     * @return координата X на экране
     */
    int toScreenX(double latit, double longit);

    /**
     * Вернуть экранную координату Y.
     * @param latit широта
     * @param longit долгота
     * @return координата Y на экране
     */
    int toScreenY(double latit, double longit);

    /**
     * Вернуть широту по экранным координатам.
     * @param x координата X на экране (вниз)
     * @param y координата Y на экране (влево)
     * @return широта
     */
    double toMapLatitude(int x, int y);

    /**
     * Вернуть долготу по экранным координатам.
     * @param x координата X на экране (вниз)
     * @param y координата Y на экране (влево)
     * @return долгота
     */
    double toMapLongitude(int x, int y);

    /**
     * Вернуть текущий уровень карты (от 1 до 9
     * в соответствии со спецификацией, описанной в
     * {@link com.lasgis.evolution.panels.ScaleManager}).
     * @see com.lasgis.evolution.panels.ScaleManager
     * @return текущий уровень карты
     */
    int getLevel();

    /**
     * Вернуть размер ячейки в экранных размерах.
     * @return число пикселей на ячейку
     */
    int getCellSize();

    /**
     * Проверить, находится ли точка внутри интервала.
     * @param latitude широта
     * @param longitude долгота
     * @return true если входит
     */
    boolean isInto(double latitude, double longitude);
}