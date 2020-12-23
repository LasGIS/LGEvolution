/*
 * CellIterator.java
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2020 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.map;

import com.lasgis.evolution.map.element.Parameters;
import com.lasgis.evolution.panels.Scalable;

import java.util.Iterator;

/**
 * Итератор для одноградусной регулярной сети.
 * Каждый блок такой сети имеет размер 1 градус X 1 градус.
 * В каждом блоке содержится двухмерный массив высот,
 * включая края блока. Позволяет строить сечения, изолинии,
 * находить высоту в заданной точке.
 *
 * @author Vladimir Laskin
 * @version 1.0
 * @since 21.01.2011 : 17:06:58
 */
public final class CellIterator implements Iterator<Cell> {

    /** текущая широта в градусах. */
    private int indX;

    /** текущая долгота в градусах. */
    private int indY;

    /** максимальная  широта в градусах. */
    private int indXMin;

    /** максимальная долгота в градусах. */
    private int indYMin;

    /** максимальная  широта в градусах. */
    private int indXMax;

    /** максимальная долгота в градусах. */
    private int indYMax;
    /** если true, то внутри интервала. */
    private boolean isIntoInterval = true;

    /**
     * Iterator&lt;Cell&gt; итератор, который
     * по очереди возвращает все ячейки.
     */
    private CellIterator() {
        indXMax = Matrix.MATRIX_SIZE_X - 1;
        indXMin = 0;
        indYMax = Matrix.MATRIX_SIZE_Y - 1;
        indYMin = 0;

        clean();
    }

    /**
     * Iterator&lt;Cell&gt; итератор, который по очереди возвращает ячейки.
     * @param interval квадратный диапазон, в который должны входить ячейки
     */
    private CellIterator(final Scalable interval) {
        final double north = interval.getNorth();
        final double south = interval.getSouth();
        if (north - south > Parameters.MAX_LATITUDE) {
            indXMin = 0;
            indXMax = Matrix.MATRIX_SIZE_X - 1;
        } else {
            indXMax = CellHelper.getSignDegree(north, true);
            indXMin = CellHelper.getSignDegree(south, true);
        }
        final double west = interval.getWest();
        final double east = interval.getEast();
        if (east - west > Parameters.MAX_LONGITUDE) {
            indYMin = 0;
            indYMax = Matrix.MATRIX_SIZE_Y - 1;
        } else {
            indYMax = CellHelper.getSignDegree(east, false);
            indYMin = CellHelper.getSignDegree(west, false);
        }

        clean();
    }

    /**
     * Устанавливаем начальные значения.
     */
    public void clean() {
        indX = indXMin;
        indY = indYMin;
        isIntoInterval = true;
    }

    /**
     * Отдаём итератор всех ячеек.
     * @return Iterator&lt;Cell&gt; итератор,
     * который по очереди возвращает ячейки
     */
    public static CellIterator getInterval() {
        return new CellIterator();
    }

    /**
     * Отдаём список ячеек.
     * @param interval квадратный диапазон,
     * в который должны входить ячейки
     * @return Iterator&lt;Cell&gt; итератор,
     * который по очереди возвращает ячейки
     */
    public static CellIterator getInterval(final Scalable interval) {
        return new CellIterator(interval);
    }

    /**
     * Returns <tt>true</tt> if the iteration has more elements. (In other
     * words, returns <tt>true</tt> if <tt>next</tt> would return an element
     * rather than throwing an exception.)
     *
     * @return <tt>true</tt> if the iterator has more elements.
     */
    public boolean hasNext() {
        return isIntoInterval;
    }

    @Override
    public Cell next() {
        final Cell cell = Matrix.cell(indX, indY);
        indY++;
        if (indYMin > indYMax && indY == Matrix.MATRIX_SIZE_Y) {
            indY = 0;
        }
        if (indY == indYMax + 1) {
            indY = indYMin;
            indX++;
            if (indXMin > indXMax && indX == Matrix.MATRIX_SIZE_X) {
                indX = 0;
            }
            if (indX == indXMax + 1) {
                isIntoInterval = false;
            }
        }
        return cell;
    }

    /**
     * возвращаем число блоков.
     * @return число блоков
     */
    public int getCount() {
        int dy = indYMax - indYMin + 1;
        int dx = indXMax - indXMin + 1;
        if (dy < 0) {
            dy += Matrix.MATRIX_SIZE_Y;
        }
        if (dx < 0) {
            dx += Matrix.MATRIX_SIZE_X;
        }
        return dy * dx;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException(
            "Операция удаления здесь не поддерживается (:>)"
        );
    }

}
