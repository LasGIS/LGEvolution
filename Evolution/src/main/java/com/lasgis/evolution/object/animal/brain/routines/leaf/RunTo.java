/**
 * @(#)RunTo.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.routines.leaf;

import com.lasgis.evolution.map.Cell;
import com.lasgis.evolution.object.animal.AbstractAnimal;
import com.lasgis.evolution.object.animal.brain.AbstractRoutine;
import com.lasgis.evolution.object.animal.organs.Legs;

import javax.script.SimpleBindings;

/**
 * Переходим к ячейке.
 <PRE>
     RunTo(ячейка_c_едой);
 </PRE>
 *
 *  @author Vladimir Laskin
 * @version 1.0
 */
public class RunTo extends AbstractRoutine {

    /** Энергия бега за едой. */
    private static final double RUN_ENERGY = 0.21;
    /** Энергия передвижения в момент задумчивости. */
    private static final double DEFAULT_WALK_ENERGY = 0.04;

    /**
     * Конструктор.
     * @param owner животное
     * @param param куча параметров
     */
    public RunTo(final AbstractAnimal owner, final SimpleBindings param) {
        super(owner, param);
    }

    @Override
    public boolean act() {
        final Cell cell = in(0);
        final Object energy1 = in(1);
        final double energy;
        if (energy1 == null) {
            energy = DEFAULT_WALK_ENERGY;
        } else {
            energy = (Double) energy1;
        }
        if (!owner.getCell().equals(cell)) {
            owner.getLegs().moveTo(cell, energy, Legs.MoveType.energy);
            return false;
        } else {
            return true;
        }
    }
}
