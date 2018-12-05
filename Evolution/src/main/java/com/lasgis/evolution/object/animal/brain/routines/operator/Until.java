/**
 * @(#)Until.java 1.0
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
 * Цикл выполнения стратегии при выполнении условия.
 * Проверка условия происходит в начале цикла.
 * @author Vladimir Laskin
 * @version 1.0
 */
@Slf4j
public class Until extends AbstractRoutine implements Cycle {

    /** если true, то выбор сделан. */
    private boolean isChecked = false;
    /** останавливаем цикл по break. */
    private boolean isStop = false;
    /** проверка продолжения цикла. */
    private Param check;
    /** выполняемая стратегия. */
    private Routine routine;

    /**
     * Конструктор.
     * @param owner животное
     * @param param куча параметров
     */
    public Until(final AbstractAnimal owner, final SimpleBindings param) {
        super(owner, param);
    }

    @Override
    public boolean act() {
        if (isStop) {
            clear();
            return true;
        }
        if (!isChecked) {
            boolean isCheck;
            isChecked = true;
            if (check != null) {
                isCheck = getInParam(check);
            } else {
                return true;
            }
            if (!isCheck) {
                clear();
                return true;
            }
        }
        if (routine != null) {
            if (routine.debugAct()) {
                isChecked = false;
            }
        } else {
            return true;
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
    public Until setCheck(final Param aCheck) {
        this.check = aCheck;
        return this;
    }

    /**
     * Установить стратегию внутри цикла.
     * Простой setter но с возвратом себя для нанизывания.
     * @param aRoutine стратегия.
     * @return сам для нанизывания.
     */
    public Until setRoutine(final Routine aRoutine) {
        this.routine = aRoutine;
        return this;
    }

    @Override public void clear() {
        isChecked = false;
        isStop = false;
        routine.clear();
    }
}
