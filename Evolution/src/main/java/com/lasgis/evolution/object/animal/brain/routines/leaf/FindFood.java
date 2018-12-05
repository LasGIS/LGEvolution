/**
 * @(#)FindFood.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.routines.leaf;

import com.lasgis.evolution.map.Cell;
import com.lasgis.evolution.map.CellHelper;
import com.lasgis.evolution.map.NearPoint;
import com.lasgis.evolution.object.EvolutionConstants;
import com.lasgis.evolution.object.animal.AbstractAnimal;
import com.lasgis.evolution.object.animal.brain.AbstractRoutine;
import com.lasgis.evolution.object.animal.organs.FoodIndex;
import com.lasgis.evolution.object.animal.organs.Stomach;

import javax.script.SimpleBindings;

/**
 * Визуальный поиск еды в окрестностях животного.
<PRE>
    FindFood():ячейка_c_едой, название_еды;
</PRE>
 *
 * @author Vladimir Laskin
 * @version 1.0
 */
public class FindFood  extends AbstractRoutine {

    /**
     * Конструктор.
     * @param owner животное
     * @param param куча параметров
     */
    public FindFood(final AbstractAnimal owner, final SimpleBindings param) {
        super(owner, param);
    }

    @Override
    public boolean act() {
        // получаем параметры для работы
        final Cell cell = owner.getCell();
        final Stomach stomach = owner.getStomach();

        // последовательность перебора ячеек
        final NearPoint[] sequence = CellHelper.getNearPoints(20, false);
        Cell cellBest = cell;
        FoodIndex foodIndex = stomach.bestFoodIndex(cellBest, 0);
        // находим самое вкусное место
        for (NearPoint seq : sequence) {
            final Cell cellInd = cell.getCell(seq);
            final FoodIndex foodIndexT = stomach.bestFoodIndex(cellInd, cell.distance(cellInd));

            if (foodIndex.getBestIndex() < foodIndexT.getBestIndex()
                && cellInd.element(EvolutionConstants.STONE_KEY).value() < 5.0) {
                foodIndex = foodIndexT;
                cellBest = cellInd;
            }
        }

        // запоминаем в кучу полученные параметры
        if (foodIndex.getBestKey() != null) {
            out(0, cellBest);
            out(1, foodIndex.getBestKey());
        } else {
            remOut(0);
            remOut(1);
        }
        return true;
    }

}
