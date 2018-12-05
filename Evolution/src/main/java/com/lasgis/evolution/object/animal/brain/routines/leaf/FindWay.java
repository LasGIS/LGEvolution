/**
 * @(#)FindWay.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.routines.leaf;

import com.lasgis.evolution.map.Cell;
import com.lasgis.evolution.map.CellHelper;
import com.lasgis.evolution.map.NearPoint;
import com.lasgis.evolution.object.animal.AbstractAnimal;
import com.lasgis.evolution.object.animal.brain.AbstractRoutine;

import javax.script.SimpleBindings;

/**
 * Находим следующую ячейку.
 *
 * @author Vladimir Laskin
 * @version 1.0
 */
public class FindWay extends AbstractRoutine {

    // последовательность перебора ячеек
    private static final NearPoint[] SEQUENCE = CellHelper.getNearPoints(2, true);

    /**
     * Конструктор.
     * @param owner животное
     * @param param куча параметров
     */
    public FindWay(final AbstractAnimal owner, final SimpleBindings param) {
        super(owner, param);
    }

    @Override
    public boolean act() {
        final Cell cell = owner.getCell();
        final Cell endCell = in(0);
        double minDist = Double.MAX_VALUE;
        Cell cellBest = null;

        // находим ближайшее место
        for (NearPoint seq : SEQUENCE) {
            final Cell cellInd = cell.getCell(seq);
            final double distance = cellInd.distance(endCell);
            if (minDist > distance) {
                minDist = distance;
                cellBest = cellInd;
            }
        }
        out(0, cellBest);
        return true;
    }
}
