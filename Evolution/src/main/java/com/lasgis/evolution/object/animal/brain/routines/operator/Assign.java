/**
 * @(#)Assign.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.routines.operator;

import com.lasgis.evolution.object.animal.AbstractAnimal;
import com.lasgis.evolution.object.animal.brain.AbstractOperator;
import com.lasgis.evolution.object.animal.brain.Operator;
import com.lasgis.evolution.object.animal.brain.OperatorType;
import com.lasgis.evolution.object.animal.brain.Param;
import com.lasgis.evolution.object.animal.brain.ParamType;
import com.lasgis.evolution.object.animal.brain.RoutineRunTimeException;
import lombok.extern.slf4j.Slf4j;

import javax.script.SimpleBindings;

/**
 * Присвоение значения.
 * @author Vladimir Laskin
 * @version 1.0
 */
@Slf4j
public class Assign extends AbstractOperator {

    /**
     * Дополнительное действие.
     */
    public enum Type {
        /** дополнительное действие. */
        ass, add, sub, mlt, div
    }

    private Type type;

    /**
     * Конструктор.
     * @param owner животное
     * @param param куча параметров
     */
    public Assign(final AbstractAnimal owner, final SimpleBindings param) {
        super(owner, param);
        type = Type.ass;
    }

    @Override
    public OperatorType operatorType() {
        return OperatorType.Assign;
    }

    @Override
    public int operatorLevel() {
        return 7;
    }

    @Override
    public boolean act() throws RoutineRunTimeException {
        final Param toPar = inKeys.get(0);
        final Object fromPar = in(1);
        switch (toPar.type) {
            case Key:
                param.put(toPar.key, fromPar);
                break;
            case Link:
                Param prm = toPar;
                do {
                    prm = prm.link;
                } while (prm.type == ParamType.Link);
                param.put(prm.key, fromPar);
                break;
            case Operator:
                final Operator operator = toPar.operator;
                if (operator instanceof PointVar) {
                    final PointVar pv = (PointVar) operator;
                    pv.set(fromPar);
                } else if (operator instanceof Array) {
                    final Array array = (Array) operator;
                    array.set(fromPar);
                }
                break;
            default:
                throw new RoutineRunTimeException("Invalid type of parameter for assign", this);
        }
        return true;
    }

    /**
     * Получаем тип дополнительного действия.
     * @param aType тип дополнительного действия.
     * @return this for chaining
     */
    public Assign setType(final Type aType) {
        this.type = aType;
        return this;
    }
}
