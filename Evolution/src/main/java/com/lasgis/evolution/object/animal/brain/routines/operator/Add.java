/**
 * @(#)Add.java 1.0
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
public class Add extends AbstractOperator {

    /**
     * Конструктор.
     * @param owner животное
     * @param param куча параметров
     */
    public Add(final AbstractAnimal owner, final SimpleBindings param) {
        super(owner, param);
    }

    @Override
    public OperatorType operatorType() {
        return OperatorType.Math;
    }

    @Override
    public int operatorLevel() {
        return 3;
    }

    @Override
    public boolean act() {
        final Object o1 = in(0);
        final Object o2 = in(1);
        if (o1 instanceof String) {
            out(0, ((String) o1).concat(convertToString(o2)));
        } else if (o1 instanceof Double || o2 instanceof Double) {
            out(0, convertToDouble(o1) + convertToDouble(o2));
        } else if (o1 instanceof Integer) {
            out(0, (Integer) o1 + convertToInteger(o2));
        }
        return true;
    }
}
