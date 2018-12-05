/**
 * @(#)Break.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.routines.operator;

import com.lasgis.evolution.object.animal.AbstractAnimal;
import com.lasgis.evolution.object.animal.brain.AbstractRoutine;
import lombok.extern.slf4j.Slf4j;

import javax.script.SimpleBindings;

/**
 * The Class Break. Выходим из циклов (for, while, until).
 *
 * @author Vladimir Laskin
 * @version 1.0
 */
@Slf4j
public class Break extends AbstractRoutine {

    private Cycle cycle;

    /**
     * Конструктор.
     *
     * @param owner животное
     * @param param куча параметров
     */
    public Break(final AbstractAnimal owner, final SimpleBindings param) {
        super(owner, param);
    }

    @Override
    public boolean act() {
        if (cycle != null) {
            cycle.stop();
        }
        return false;
    }

    /**
     * Устанавливаем циклический оператор.
     * @param aCycle циклический оператор
     * @return сам себя для нанизывания вызова
     */
    public Break setCycle(final Cycle aCycle) {
        this.cycle = aCycle;
        return this;
    }
}
