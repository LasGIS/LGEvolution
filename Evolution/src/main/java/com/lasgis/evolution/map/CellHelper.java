/*
 * CellHelper.java
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2020 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.map;

import com.lasgis.evolution.map.element.Parameters;

/**
 * Помощник для определения координат ячейки и наоборот,
 * ячейку по координатам.
 * @author vladimir.laskin
 * @version 1.0
 */
public final class CellHelper {

    /**
     * Последовательность просмотра животным ячеек.
     *
     */
    private static final NearPoint[] SECTOR_NEAR_POINTS = {
        // первый квадрат [1, 2]
        new NearPoint(1, 0, 1.0),
        new NearPoint(1, 1, Math.sqrt(2)),                                      // 1 + 1
        // второй квадрат: [3, 5, 6]
        new NearPoint(2, 0, 2.0),
        new NearPoint(2, 1, Math.sqrt(5)), new NearPoint(1, 2, Math.sqrt(5)),   // 4 + 1
        new NearPoint(2, 2, Math.sqrt(8)),                                      // 4 + 4
        // третий квадрат: [7, 9, 11]
        new NearPoint(3, 0, 3.0),                                               // 9
        new NearPoint(3, 1, Math.sqrt(10)), new NearPoint(1, 3, Math.sqrt(10)), // 9 + 1
        new NearPoint(3, 2, Math.sqrt(13)), new NearPoint(2, 3, Math.sqrt(13)), // 9 + 4
        // четвёртый квадрат: [12, 14, 15, 17, 19]
        new NearPoint(4, 0, 4.0),                                               // 16
        new NearPoint(4, 1, Math.sqrt(17)), new NearPoint(1, 4, Math.sqrt(17)), // 16 + 1
        new NearPoint(3, 3, Math.sqrt(18)),                                     // 9 + 9
        new NearPoint(4, 2, Math.sqrt(20)), new NearPoint(2, 4, Math.sqrt(20)), // 16 + 4
        new NearPoint(4, 3, Math.sqrt(25)), new NearPoint(3, 4, Math.sqrt(25)), // 16 + 9
        // пятый квадрат: [20, 22, 24, 25, 27]
        new NearPoint(5, 0, 5.0),                                               // 25
        new NearPoint(5, 1, Math.sqrt(26)), new NearPoint(1, 5, Math.sqrt(26)), // 25 + 1
        new NearPoint(5, 2, Math.sqrt(29)), new NearPoint(2, 5, Math.sqrt(29)), // 25 + 4
        new NearPoint(4, 4, Math.sqrt(32)),                                     // 16 + 16
        new NearPoint(5, 3, Math.sqrt(34)), new NearPoint(3, 5, Math.sqrt(34)), // 25 + 9
        // шестой квадрат: [28, 30, 32, 34, 36]
        new NearPoint(6, 0, 6.0),                                               // 36
        new NearPoint(6, 1, Math.sqrt(37)), new NearPoint(1, 6, Math.sqrt(37)), // 36 + 1
        new NearPoint(6, 2, Math.sqrt(40)), new NearPoint(2, 6, Math.sqrt(40)), // 36 + 4
        new NearPoint(5, 4, Math.sqrt(41)), new NearPoint(4, 5, Math.sqrt(41)), // 25 + 16
        new NearPoint(6, 3, Math.sqrt(45)), new NearPoint(3, 6, Math.sqrt(45)), // 36 + 9
        // седьмой квадрат: [37, 38, 40, 42, 44, 46, 48]
        new NearPoint(7, 0, 7.0),                                               // 49
        new NearPoint(5, 5, Math.sqrt(50)),                                     // 25 + 25
        new NearPoint(7, 1, Math.sqrt(50)), new NearPoint(1, 7, Math.sqrt(50)), // 49 + 1
        new NearPoint(6, 4, Math.sqrt(52)), new NearPoint(4, 6, Math.sqrt(52)), // 36 + 16
        new NearPoint(7, 2, Math.sqrt(53)), new NearPoint(2, 7, Math.sqrt(53)), // 49 + 4
        new NearPoint(7, 3, Math.sqrt(58)), new NearPoint(3, 7, Math.sqrt(58)), // 49 + 9
        new NearPoint(6, 5, Math.sqrt(61)), new NearPoint(5, 6, Math.sqrt(61)), // 36 + 25
        // восьмой квадрат: [49, 51, 53, 55, 56, 58, 60, 62, 64]
        new NearPoint(8, 0, 8.0),                                               // 64
        new NearPoint(7, 4, Math.sqrt(65)), new NearPoint(4, 7, Math.sqrt(65)), // 49 + 16
        new NearPoint(8, 1, Math.sqrt(65)), new NearPoint(1, 8, Math.sqrt(65)), // 64 + 1
        new NearPoint(8, 2, Math.sqrt(68)), new NearPoint(2, 8, Math.sqrt(68)), // 64 + 4
        new NearPoint(6, 6, Math.sqrt(72)),                                     // 36 + 36
        new NearPoint(8, 3, Math.sqrt(73)), new NearPoint(3, 8, Math.sqrt(73)), // 64 + 9
        new NearPoint(7, 5, Math.sqrt(74)), new NearPoint(5, 7, Math.sqrt(74)), // 49 + 25
        new NearPoint(8, 4, Math.sqrt(80)), new NearPoint(4, 8, Math.sqrt(80)), // 64 + 16
        new NearPoint(9, 1, Math.sqrt(82)), new NearPoint(1, 9, Math.sqrt(82)), // 81 + 1
        // девятый квадрат: [65, 67, 69, 71, 73, 75, 76, 78]
        new NearPoint(9, 0, 9.0),                                               // 81
        new NearPoint(7, 6, Math.sqrt(85)), new NearPoint(6, 7, Math.sqrt(85)), // 49 + 36
        new NearPoint(9, 2, Math.sqrt(85)), new NearPoint(2, 9, Math.sqrt(85)), // 81 + 4
        new NearPoint(8, 5, Math.sqrt(89)), new NearPoint(5, 8, Math.sqrt(89)), // 64 + 25
        new NearPoint(9, 3, Math.sqrt(90)), new NearPoint(3, 9, Math.sqrt(90)), // 81 + 9
        new NearPoint(9, 4, Math.sqrt(97)), new NearPoint(4, 9, Math.sqrt(97)), // 81 + 16
        new NearPoint(7, 7, Math.sqrt(98)),                                     // 49 + 49
        new NearPoint(8, 6, Math.sqrt(100)), new NearPoint(6, 8, Math.sqrt(100)), // 64 + 36
        // десятый квадрат: [79]
        new NearPoint(10, 0, 10.0),                                             // 100
//        new NearPoint(9, 5, Math.sqrt(106)), new NearPoint(5, 9, Math.sqrt(106)), // 81 + 25
//        new NearPoint(8, 7, Math.sqrt(113)), new NearPoint(7, 8, Math.sqrt(113)), // 64 + 49
//        new NearPoint(9, 6, Math.sqrt(117)), new NearPoint(6, 9, Math.sqrt(117)), // 81 + 36
//        new NearPoint(8, 8, Math.sqrt(128)),                                      // 64 + 64
//        new NearPoint(9, 7, Math.sqrt(130)), new NearPoint(7, 9, Math.sqrt(130)), // 81 + 49
//        new NearPoint(9, 8, Math.sqrt(145)), new NearPoint(8, 9, Math.sqrt(145)), // 81 + 64
//        new NearPoint(9, 9, Math.sqrt(162)),                                     // 81 + 81
    };

    /**
     * Для утилитных классов закрываем конструктор.
     */
    private CellHelper() {
    }

    /**
     * Вернуть значение широты или долготы в градусах.
     * @param value начальная широта или долгота в метрах местности
     * @param isX признак, что это по X
     * @return широта или долгота в градусах
     */
    public static int getSignDegree(final double value, final boolean isX) {
        final int maxIndex = isX ? Matrix.MATRIX_SIZE_X : Matrix.MATRIX_SIZE_Y;
        int val = (int) Math.floor(value / Matrix.CELL_SIZE);
        if (val >= maxIndex) {
            val %= maxIndex;
        } else if (val < 0) {
            val += maxIndex;
        }
        return val;
    }

    /**
     * Вернуть ячейку по координатам широты или долготы в градусах.
     * @param latitude широта
     * @param longitude долгота
     * @return Ячейка
     */
    public static Cell getCell(final double latitude, final double longitude) {
        final int x = (int) Math.floor(latitude / Matrix.CELL_SIZE);
        final int y = (int) Math.floor(longitude / Matrix.CELL_SIZE);
        return getCell(x, y);
    }

    /**
     * Вернуть ячейку по условным координатам (x, y).
     * @param x координата x (широта)
     * @param y  координата y (долгота)
     * @return Ячейка
     */
    public static Cell getCell(final int x, final int y) {
        if (x >= 0 && x < Matrix.MATRIX_SIZE_X && y >= 0 && y < Matrix.MATRIX_SIZE_Y) {
            return Matrix.cell(x, y);
        } else {
            final int xc, yc;
            if (x < 0) {
                xc = x + Matrix.MATRIX_SIZE_X;
            } else if (x >= Matrix.MATRIX_SIZE_X) {
                xc = x % Matrix.MATRIX_SIZE_X;
            } else {
                xc = x;
            }
            if (y < 0) {
                yc = y + Matrix.MATRIX_SIZE_Y;
            } else if (y >= Matrix.MATRIX_SIZE_Y) {
                yc = y % Matrix.MATRIX_SIZE_Y;
            } else {
                yc = y;
            }
            return Matrix.cell(xc, yc);
        }
    }

    /**
     * Вернуть центральные координаты ячейки
     * по условным координатам (x, y).
     * @param x координата x (широта)
     * @param y координата y (долгота)
     * @return широту центра ячейки
     */
    public static double getCellLatitude(final int x, final int y) {
        return x * Matrix.CELL_SIZE + Matrix.CELL_SIZE / 2;
    }

    /**
     * Вернуть центральные координаты ячейки
     * по условным координатам (x, y).
     * @param x координата x (широта)
     * @param y координата y (долгота)
     * @return долготу центра ячейки
     */
    public static double getCellLongitude(final int x, final int y) {
        return y * Matrix.CELL_SIZE + Matrix.CELL_SIZE / 2;
    }

    /**
     * Вернуть массив относительных координат ячеек
     * при заданной остроте зрения.
     * Доступные значения для остроты зрения:<br/>
     * первый квадрат [1, 2]<br/>
     * второй квадрат: [3, 5, 6]<br/>
     * третий квадрат: [7, 9, 11]<br/>
     * четвёртый квадрат: [12, 14, 15, 17, 19]<br/>
     * пятый квадрат: [20, 22, 24, 25, 27, 29, 30]<br/>
     *
     * @param sight острота зрения
     * @param isViewZeroCell если true, то
     *  рассматриваем центральную ячейку, а иначе - нет.
     * @return массив координат ячеек
     */
    public static NearPoint[] getNearPoints(final int sight, final boolean isViewZeroCell) {

        final int size = sight * 4 + (isViewZeroCell ? 1 : 0);
        final NearPoint[] nearPoints = new NearPoint[size];
        int count = 0;
        if (isViewZeroCell) {
            nearPoints[count++] = new NearPoint(0, 0, 0.0);
        }
        for (int i = 0; i < sight; i++) {
            final NearPoint pnt = SECTOR_NEAR_POINTS[i];
            //{{1, 0}, {0, -1}, {-1, 0}, {0, 1}, {1, 1}, {1, -1}, {-1, -1}, {-1, 1}}
            nearPoints[count++] = new NearPoint(+pnt.x, +pnt.y, pnt.distance);
            nearPoints[count++] = new NearPoint(-pnt.x, -pnt.y, pnt.distance);
            nearPoints[count++] = new NearPoint(+pnt.y, -pnt.x, pnt.distance);
            nearPoints[count++] = new NearPoint(-pnt.y, +pnt.x, pnt.distance);
        }
        return nearPoints;
    }

    /**
     * Нормализация одной широты относительно другой широты.
     * @param lat широта для нормализации
     * @param nearLat ближайшая широта
     * @return нормализованная широта
     */
    public static double normLatitude(final double lat, final double nearLat) {
        if (Math.abs(nearLat - lat) > Parameters.MAX_LATITUDE / 2.0) {
            if (nearLat > lat) {
                return lat + Parameters.MAX_LATITUDE;
            } else {
                return lat - Parameters.MAX_LATITUDE;
            }
        }
        return lat;
    }

    /**
     * Нормализация одной долготы относительно другой долготы.
     * @param lng долгота для нормализации
     * @param nearLng ближайшая долгота
     * @return нормализованная долгота
     */
    public static double normLongitude(final double lng, final double nearLng) {
        if (Math.abs(nearLng - lng) > Parameters.MAX_LONGITUDE / 2.0) {
            if (nearLng > lng) {
                return lng + Parameters.MAX_LONGITUDE;
            } else {
                return lng - Parameters.MAX_LONGITUDE;
            }
        }
        return lng;
    }
}
