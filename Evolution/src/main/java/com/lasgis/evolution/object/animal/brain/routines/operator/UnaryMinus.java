/**
 * @(#)UnaryMinus.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.routines.operator;

import com.lasgis.evolution.object.animal.AbstractAnimal;
import com.lasgis.evolution.object.animal.brain.AbstractOperator;
import com.lasgis.evolution.object.animal.brain.OperatorType;
import lombok.extern.slf4j.Slf4j;

import javax.script.SimpleBindings;

/**
 * @author Vladimir Laskin
 * @version 1.0
 */
@Slf4j
public class UnaryMinus extends AbstractOperator {

    /**
     * Конструктор.
     * @param owner животное
     * @param param куча параметров
     */
    public UnaryMinus(final AbstractAnimal owner, final SimpleBindings param) {
        super(owner, param);
    }

    @Override
    public OperatorType operatorType() {
        return OperatorType.Math;
    }

    @Override
    public int operatorLevel() {
        return 1;
    }

    @Override
    public boolean act() {
        final Object obj = in(0);
        if (obj instanceof Double) {
            out(0, - (Double) obj);
        } else if (obj instanceof Integer) {
            out(0, - (Integer) obj);
        }
        return true;
    }
}
