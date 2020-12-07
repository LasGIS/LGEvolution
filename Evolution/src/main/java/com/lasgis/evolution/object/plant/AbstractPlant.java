/**
 * @(#)AbstractPlant.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.plant;

import com.lasgis.evolution.map.Cell;
import com.lasgis.evolution.map.CellIterator;
import com.lasgis.evolution.map.Element;
import com.lasgis.evolution.map.Matrix;
import com.lasgis.evolution.object.Info;
import com.lasgis.evolution.object.InfoType;
import com.lasgis.evolution.object.PlantBehaviour;
import lombok.extern.slf4j.Slf4j;

import javax.swing.Icon;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Общая часть для всех растений и минералов, привязанных к земле.
 *
 * @author vladimir.laskin
 * @version 1.0
 */
@Slf4j
@Info(type = InfoType.STAT)
public abstract class AbstractPlant implements PlantBehaviour {

    private Thread thread;
    private boolean isShow = true;

    @Override
    public PlantBehaviour[] subElements() {
        return new PlantBehaviour[0];
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public void init() {
        thread = new Thread(new PlantRun(this));
        thread.start();
    }

    @Override
    public void stop() {
        thread.interrupt();
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean isShow() {
        return isShow;
    }

    @Override
    public void setShow(final boolean show) {
        isShow = show;
    }

    /**
     *
     */
    class PlantRun implements Runnable {

        AbstractPlant plant;

        PlantRun(final AbstractPlant plant) {
            this.plant = plant;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    final CellIterator itr = CellIterator.getInterval();
                    while (itr.hasNext()) {
                        final Cell cell = itr.next();
                        if (cell != null) {
                            synchronized (Matrix.getMatrix()) {
                                plant.cellProcessing(cell);
                            }
                        }
                    }
                    Thread.sleep(2000);
                }
            } catch (final InterruptedException ex) {
                log.error(ex.getMessage(), ex);
            }
        }

    }

    @Override
    public void drawPlant(final Graphics gr, final Rectangle rec, final Cell cell) {
    }

    @Override
    public void cellProcessing(final Cell cell) {
        for (PlantBehaviour plant : subElements()) {
            plant.cellProcessing(cell);
        }

    }

    /**
     * Сползание по склону в 45 град.<br/>
     * Перемещение вещества происходит из центральной ячейки
     * во внешние ячейки по 1 единице за вызов.
     * Параметр angle указывает минимальную разницу значений
     * для перемещения. Так, если angle = 10, то перемещение
     * происходит при разнице, как показано на рисунке ниже:
     * <pre>
     * +----+----+----+
     * | 14 | 10 | 14 |
     * +----+----+----+
     * | 10 | XX | 10 |
     * +----+----+----+
     * | 14 | 10 | 14 |
     * +----+----+----+
     * </pre>
     *
     * @param cell  центральная ячейка
     * @param key   ключевое слово для поиска вещества
     * @param angle минимальная разница значений для перемещения
     */
    protected void sliding(final Cell cell, final String key, final int angle) {
        final double angle45 = angle * 1.4;
        sliding(cell.element(key), cell.getCell(1, 1).element(key), angle45);
        sliding(cell.element(key), cell.getCell(1, 0).element(key), angle);
        sliding(cell.element(key), cell.getCell(1, -1).element(key), angle45);
        sliding(cell.element(key), cell.getCell(0, 1).element(key), angle);
        sliding(cell.element(key), cell.getCell(0, -1).element(key), angle);
        sliding(cell.element(key), cell.getCell(-1, 1).element(key), angle45);
        sliding(cell.element(key), cell.getCell(-1, 0).element(key), angle);
        sliding(cell.element(key), cell.getCell(-1, -1).element(key), angle45);
    }

    /**
     * Сползание по склону в 45 град.
     *
     * @param from  откуда
     * @param to    куда
     * @param angle разница уровней для сползания
     */
    private void sliding(final Element from, final Element to, final double angle) {
        if (from.value() - to.value() > angle) {
            double delta = (from.value() - to.value()) / 100;
            if (delta < 1) {
                delta = 1;
            }
            to.incValue(delta);
            from.decValue(delta);
        }
    }

}
