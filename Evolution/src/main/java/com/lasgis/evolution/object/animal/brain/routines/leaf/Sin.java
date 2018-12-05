/**
 * @(#)Sin.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2014 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.routines.leaf;

import com.lasgis.evolution.object.animal.AbstractAnimal;
import com.lasgis.evolution.object.animal.brain.AbstractRoutine;

import javax.script.SimpleBindings;

/**
 * Пример простой функции.
 * @author Vladimir Laskin
 * @version 1.0
 */
public class Sin extends AbstractRoutine {

    /**
     * Конструктор.
     * @param owner животное
     * @param param куча параметров
     */
    public Sin(final AbstractAnimal owner, final SimpleBindings param) {
        super(owner, param);
    }

    @Override
    public boolean act() {
        out(0, Math.sin((double) in(0)));
        return true;
    }
}
