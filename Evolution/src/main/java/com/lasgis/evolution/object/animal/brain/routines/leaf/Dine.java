/**
 * @(#)Dine.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.routines.leaf;

import com.lasgis.evolution.map.Cell;
import com.lasgis.evolution.object.animal.AbstractAnimal;
import com.lasgis.evolution.object.animal.AnimalState;
import com.lasgis.evolution.object.animal.brain.AbstractRoutine;

import javax.script.SimpleBindings;

/**
 * Поедаем выбранное.
 <PRE>
     Dine(название_еды);
 </PRE>
 *
 * @author Vladimir Laskin
 * @version 1.0
 */
public class Dine extends AbstractRoutine {

    /**
     * Конструктор.
     * @param owner животное
     * @param param куча параметров
     */
    public Dine(final AbstractAnimal owner, final SimpleBindings param) {
        super(owner, param);
    }

    @Override
    public boolean act() {
        final Cell cell = owner.getCell();
        final String foodName = in(0);
        owner.setState(AnimalState.eat);
        final boolean ret = owner.getStomach().dine(cell, foodName, true);
        owner.addSkipNanoTime(3000000);
        return !ret;
    }
}
