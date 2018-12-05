/**
 * @(#)For.java 1.0
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
 * The Class For.
 * @author Vladimir Laskin
 * @version 1.0
 */
@Slf4j
public class For extends AbstractRoutine implements Cycle {

    /** останавливаем цикл по break. */
    private boolean isStop = false;

    /**
     * Конструктор.
     * @param owner животное
     * @param param куча параметров
     */
    public For(final AbstractAnimal owner, final SimpleBindings param) {
        super(owner, param);
    }

    @Override
    public boolean act() {
        if (isStop) {
            clear();
            return true;
        }

        return false;
    }

    @Override
    public void stop() {
        isStop = true;
    }

    @Override public void clear() {
        isStop = false;
    }
}
