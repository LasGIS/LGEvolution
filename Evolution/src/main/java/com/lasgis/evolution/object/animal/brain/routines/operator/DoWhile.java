/**
 * @(#)DoWhile.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.routines.operator;

import com.lasgis.evolution.object.animal.AbstractAnimal;
import com.lasgis.evolution.object.animal.brain.AbstractRoutine;
import com.lasgis.evolution.object.animal.brain.Param;
import com.lasgis.evolution.object.animal.brain.Routine;
import lombok.extern.slf4j.Slf4j;

import javax.script.SimpleBindings;

/**
 * Цикл выполнения стратегии до выполнения условия.
 * Проверка условия происходит в конце цикла.
 * @author Vladimir Laskin
 * @version 1.0
 */
@Slf4j
public class DoWhile extends AbstractRoutine implements Cycle {

     /** проверка продолжения цикла. */
    private Param check = null;
    /** останавливаем цикл по break. */
    private boolean isStop = false;
    /** выполняемая стратегия. */
    private Routine routine = null;

    /**
     * Конструктор.
     * @param owner животное
     * @param param куча параметров
     */
    public DoWhile(final AbstractAnimal owner, final SimpleBindings param) {
        super(owner, param);
    }

    @Override
    public boolean act() {
        if (isStop) {
            clear();
            return true;
        }
        boolean retAction = true;
        if (routine != null) {
            retAction = routine.debugAct();
        }
        if (retAction) {
            boolean isCheck;
            if (check != null) {
                isCheck = getInParam(check);
            } else {
                clear();
                return true;
            }
            return !isCheck;
        }
        return false;
    }

    @Override
    public void stop() {
        isStop = true;
    }

    /**
     * Установить стратегию проверки.
     * Простой setter но с возвратом себя для нанизывания.
     * @param aCheck стратегия.
     * @return сам для нанизывания.
     */
    public DoWhile setCheck(final Param aCheck) {
        this.check = aCheck;
        return this;
    }

    /**
     * Установить стратегию внутри цикла.
     * Простой setter но с возвратом себя для нанизывания.
     * @param aRoutine стратегия.
     * @return сам для нанизывания.
     */
    public DoWhile setRoutine(final Routine aRoutine) {
        this.routine = aRoutine;
        return this;
    }

    @Override public void clear() {
        isStop = false;
        routine.clear();
    }
}
