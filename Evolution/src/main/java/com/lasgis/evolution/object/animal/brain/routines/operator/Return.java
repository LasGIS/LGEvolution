/**
 * @(#)Return.java 1.0
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
 * The Class Return. Останавливает выполнение функции (routine)
 * и возвращает значение функции.
 *
 * @author Vladimir Laskin
 * @version 1.0
 */
@Slf4j
public class Return extends AbstractRoutine {

    private DefineFunction function;

    /**
     * Конструктор.
     *
     * @param owner животное
     * @param param куча параметров
     */
    public Return(final AbstractAnimal owner, final SimpleBindings param) {
        super(owner, param);
    }

    @Override
    public boolean act() {
        return false;
    }

    public void setFunction(final DefineFunction function) {
        this.function = function;
    }
}
