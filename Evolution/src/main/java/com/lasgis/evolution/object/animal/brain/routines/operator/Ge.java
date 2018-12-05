/**
 * @(#)Ge.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2014 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.routines.operator;

import com.lasgis.evolution.object.animal.AbstractAnimal;
import com.lasgis.evolution.object.animal.brain.AbstractOperator;
import com.lasgis.evolution.object.animal.brain.OperatorType;
import lombok.extern.slf4j.Slf4j;

import javax.script.SimpleBindings;

/**
 * Boolean`овский оператор больше или равно (>=).
 * @author Vladimir Laskin
 * @version 1.0
 */
@Slf4j
public class Ge extends AbstractOperator {

    /**
     * Конструктор.
     * @param owner животное
     * @param param куча параметров
     */
    public Ge(final AbstractAnimal owner, final SimpleBindings param) {
        super(owner, param);
    }

    @Override
    public OperatorType operatorType() {
        return OperatorType.Boolean;
    }

    @Override
    public int operatorLevel() {
        return 4;
    }

    @Override
    public boolean act() {
        final Comparable o1 = in(0);
        final Comparable o2 = in(1);
        @SuppressWarnings("unchecked")
        final int compare = o1.compareTo(o2);
        out(0, compare >= 0);
        return true;
    }

}
