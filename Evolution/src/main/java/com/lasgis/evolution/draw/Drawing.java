/**
 * @(#)Drawing.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.draw;

import com.lasgis.evolution.map.Cell;
import com.lasgis.evolution.map.CellIterator;
import com.lasgis.evolution.map.Matrix;
import com.lasgis.evolution.object.PlantBehaviour;
import com.lasgis.evolution.panels.Scalable;

import java.awt.*;

/**
 * Класс для рисования объектов карты.
 * @author vlaskin
 * @version 1.0
 */
public class Drawing {

    /** контекст вывода. */
    private Graphics2D gr;
    /** объект, который содержит формулы преобразования. */
    private Scalable patron = null;

    /**
     * Конструктор с установками.
     * @param graphics контекст вывода
     * @param scalable объект, который содержит формулы преобразования
     * объектов карты
     */
    public Drawing(final Graphics graphics, final Scalable scalable) {
        gr = (Graphics2D) graphics;
        patron = scalable;
    }

    /**
     * Возвращаем прямоугольник для ячейки.
     * @param cell ячейка
     * @return прямоугольник
     */
    public Rectangle getRect(final Cell cell) {
        final int x0 = patron.toScreenX(cell.getNorth(), cell.getWest());
        final int y0 = patron.toScreenY(cell.getNorth(), cell.getWest());
        final int x1 = patron.toScreenX(cell.getSouth(), cell.getEast());
        final int y1 = patron.toScreenY(cell.getSouth(), cell.getEast());
        return new Rectangle(x0, y0, x1 - x0, y1 - y0);
    }

    /**
     * Рисуем отдельную ячейку на карте.
     * @param cell ячейка карты
     */
    public void drawCell(final Cell cell) {
        cell.drawPlant(gr, getRect(cell));
    }

    /**
     * Сначала заливаем белым.
     * @param iterator интервал для рисования
     */
    public void fillRectangles(final CellIterator iterator) {
        iterator.clean();
        while (iterator.hasNext()) {
            final Cell cell = iterator.next();
            if (cell != null) {
                final Rectangle rect = getRect(cell);
                gr.setColor(Color.WHITE);
                gr.fillRect(rect.x, rect.y, rect.width, rect.height);
            }
        }
    }

    /**
     * Рисуем растения в ячейках, определённых интервалом.
     * @param plant тип растения
     * @param iterator интервал для рисования
     */
    public void drawPlant(final PlantBehaviour plant, final CellIterator iterator) {
        iterator.clean();
        while (iterator.hasNext()) {
            final Cell cell = iterator.next();
            if (cell != null) {
                plant.drawPlant(gr, getRect(cell), cell);
            }
        }
    }

    /**
     * Рисуем решётку.
     */
    public void grid() {
        if (patron.getLevel() != 5) {
            return;
        }
        final double north = patron.getNorth();
        final double south = patron.getSouth();
        final double west = patron.getWest();
        final double east = patron.getEast();
        final double deltaLat = Matrix.CELL_SIZE;
        final double deltaLng = Matrix.CELL_SIZE;
        double latit = Math.ceil(south / deltaLat) * deltaLat;
        double longit = Math.ceil(west / deltaLng) * deltaLng;
        int x1, y1, x2, y2;
        gr.setColor(new Color(255, 100, 0));
        // рисуем горизонтали
        x1 = patron.toScreenX(north, west);
        x2 = patron.toScreenX(south, east);
        for (; latit <= north + .001; latit += deltaLat) {
            y1 = patron.toScreenY(latit, west);
            y2 = patron.toScreenY(latit, east);
            gr.drawLine(x1, y1, x2, y2);
        }
        // рисуем вертикали
        y1 = patron.toScreenY(north, west);
        y2 = patron.toScreenY(south, east);
        for (; longit <= east + .001; longit += deltaLng) {
            x1 = patron.toScreenX(north, longit);
            x2 = patron.toScreenX(south, longit);
            gr.drawLine(x1, y1, x2, y2);
        }
    }
}