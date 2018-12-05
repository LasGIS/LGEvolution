/**
 * @(#)If.java 1.0
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
 * Реализация ветвлений стратегий животного.
 * Тут нужно понимать, что выбор ветвления происходит один раз,
 * т.е. при входе в стратегию, а акция передаётся в выбранную стратегию.
 * @author Vladimir Laskin
 * @version 1.0
 */
@Slf4j
public class If extends AbstractRoutine {

    /** если true, то выбор сделан. **/
    private boolean isChecked = false;
    /** сделанный выбор. **/
    private boolean isCheck = true;
    /** стратегия выбора */
    private Param check;
    /** стратегия выполняемая при положительном выборе. */
    private Routine onTrue;
    /** стратегия выполняемая при отрицательном выборе */
    private Routine onFalse;

    /**
     * Конструктор.
     * @param owner животное
     * @param param куча параметров
     */
    public If(final AbstractAnimal owner, final SimpleBindings param) {
        super(owner, param);
    }

    @Override
    public boolean act() {
        boolean retAction = true;
        if (!isChecked) {
            if (check != null) {
                isCheck = getInParam(check);
            } else {
                return true;
            }
            isChecked = true;
        }
        if (isCheck) {
            // Положительный выбор
            if (onTrue != null) {
                retAction = onTrue.debugAct();
            }
        } else {
            // Отрицательный выбор
            if (onFalse != null) {
                retAction = onFalse.debugAct();
            }
        }
        if (retAction) {
            isChecked = false;
        }
        return retAction;
    }

    /**
     *
     * @param aCheck
     * @return
     */
    public If setCheck(final Param aCheck) {
        this.check = aCheck;
        return this;
    }

    /**
     *
     * @param routineOnTrue
     * @return
     */
    public If setOnTrue(final Routine routineOnTrue) {
        this.onTrue = routineOnTrue;
        return this;
    }

    /**
     *
     * @param routineOnFalse
     * @return
     */
    public If setOnFalse(final Routine routineOnFalse) {
        this.onFalse = routineOnFalse;
        return this;
    }

    @Override
    public void clear() {
        isChecked = false;
        isCheck = true;
        if (onTrue != null) {
            onTrue.clear();
        }
        if (onFalse != null) {
            onFalse.clear();
        }
    }
}
