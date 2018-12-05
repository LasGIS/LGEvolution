/**
 * @(#)AbstractRoutine.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain;

import com.lasgis.evolution.object.animal.AbstractAnimal;

import javax.script.SimpleBindings;

/**
 * The Class AbstractRoutine.
 *
 * @author Vladimir Laskin
 * @version 1.0
 */

public abstract class AbstractRoutine extends AbstractOperator implements Operator {

    /**
     * Конструктор.
     *
     * @param owner животное
     * @param param куча параметров
     */
    protected AbstractRoutine(final AbstractAnimal owner, final SimpleBindings param) {
        super(owner, param);
    }

    @Override
    public OperatorType operatorType() {
        return OperatorType.Routine;
    }

    @Override
    public int operatorLevel() {
        return 0;
    }
}
