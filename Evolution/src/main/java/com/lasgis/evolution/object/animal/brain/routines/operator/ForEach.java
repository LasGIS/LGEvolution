/**
 * @(#)ForEach.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.routines.operator;

import com.lasgis.evolution.object.animal.AbstractAnimal;
import com.lasgis.evolution.object.animal.brain.AbstractOperator;
import com.lasgis.evolution.object.animal.brain.AbstractRoutine;
import com.lasgis.evolution.object.animal.brain.Param;
import lombok.extern.slf4j.Slf4j;

import javax.script.SimpleBindings;

/**
 * The Class For.
 * @author Vladimir Laskin
 * @version 1.0
 */
@Slf4j
public class ForEach extends AbstractRoutine implements Cycle {

    private AbstractOperator routine;
    private Param array;
    private boolean isFirst = true;
    private boolean isRoutineEnd = false;
    private Object[] realArray;
    private int count;
    private int size;
    /** останавливаем цикл по break. */
    private boolean isStop = false;


    /**
     * Конструктор.
     * @param owner животное
     * @param param куча параметров
     */
    public ForEach(final AbstractAnimal owner, final SimpleBindings param) {
        super(owner, param);
    }

    @Override
    public boolean act() {
        if (isStop) {
            clear();
            return true;
        }
        if (isFirst) {
            final Object some = getInParam(array);
            if (some instanceof Object[]) {
                realArray = (Object[]) some;
                size = realArray.length;
                count = 0;
                out(0, realArray[count]);
            }
            isFirst = false;
        }
        if (routine.debugAct()) {
            count++;
            if (count < size) {
                out(0, realArray[count]);
            } else {
                isFirst = true;
                count = 0;
                out(0, realArray[count]);
                return true;  // стратегия окончена
            }
        }
        return false;  // стратегия продолжается
    }

    @Override
    public void stop() {
        isStop = true;
    }

    public void setRoutine(final AbstractOperator routine) {
        this.routine = routine;
    }

    public AbstractOperator getRoutine() {
        return routine;
    }

    public void setVariable(final Param variable) {
        addOutKey(variable);
    }

    public void setArray(final Param array) {
        this.array = array;
    }

    @Override public void clear() {
        isStop = false;
        isFirst = true;
        count = 0;
    }

}
