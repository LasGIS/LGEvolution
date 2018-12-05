/**
 * @(#)Matrix.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.map;

import com.lasgis.evolution.map.element.Parameters;
import com.lasgis.util.ResourceLoader;

/**
 * Матрица элементов карты.
 * @author Laskin
 * @version 1.0
 * @since 06.06.2010 22:24:26
 */
public final class Matrix {

    /** размер матрицы по вертикали (низ - ноль). */
    public static final int MATRIX_SIZE_X = ResourceLoader.getInteger("matrix.latitude.size");
    /** размер матрицы по горизонтали (лево - ноль). */
    public static final int MATRIX_SIZE_Y = ResourceLoader.getInteger("matrix.longitude.size");
    /** размер элементарной ячейки. */
    public static final double CELL_SIZE = Parameters.GRADE_2_METER / 100;
    /** сама матрица. */
    private final Cell[][] cells = new Cell[MATRIX_SIZE_X][MATRIX_SIZE_Y];
    /** singleton матрицы. */
    private static final Matrix MATRIX = new Matrix();

    /**
     * Создаем и заполняем матрицу.
     */
    private Matrix() {
        for (int x = 0; x < MATRIX_SIZE_X; x++) {
            for (int y = 0; y < MATRIX_SIZE_Y; y++) {
                cells[x][y] = new Cell(x, y);
            }
        }
    }

    /**
     * @return выдаем singleton матрицы по первому требованию.
     */
    public static Matrix getMatrix() {
        return MATRIX;
    }

    /**
     * Вернуть ячейку по индексам.
     * @param x индекс широты ячейки
     * @param y индекс долготы ячейки
     * @return ячейка или NULL если вышли из диапазона
     */
    public Cell getCell(final int x, final int y) {
        return isValidIndex(x, y) ? cells[x][y] : null;
    }

    /**
     * Вернуть ячейку по индексам.
     * @param x индекс широты ячейки
     * @param y индекс долготы ячейки
     * @return ячейка или NULL если вышли из диапазона
     */
    public boolean isValidIndex(final int x, final int y) {
        return x >= 0 && x < MATRIX_SIZE_X && y >= 0 && y < MATRIX_SIZE_Y;
    }
}
