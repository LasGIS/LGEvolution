/**
 * @(#)Equal.java 1.0
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
 * Проверка на совпадение двух параметров.
 * @author Vladimir Laskin
 * @version 1.0
 */
@Slf4j
public class Equal extends AbstractOperator {

    private boolean isNot = false;

    /**
     * Конструктор.
     *
     * @param owner животное
     * @param param куча параметров
     */
    public Equal(final AbstractAnimal owner, final SimpleBindings param) {
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
        final Object o1 = in(0);
        final Object o2 = in(1);
        final boolean isEqual = o1.equals(o2);
        out(0, isNot ? !isEqual : isEqual);
        return true;
    }

    /**
     * Отрицание
     * @return ссылка на самоё себя
     */
    public Equal not() {
        isNot = true;
        return this;
    }

}
