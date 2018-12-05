/**
 * @(#)DefineFunction.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.routines.operator;

import com.lasgis.evolution.object.animal.AbstractAnimal;
import com.lasgis.evolution.object.animal.brain.Routine;
import lombok.extern.slf4j.Slf4j;

import javax.script.SimpleBindings;

/**
 * The Class DefineFunction.
 * @author Vladimir Laskin
 * @version 1.0
 */
@Slf4j
public class DefineFunction extends Sequence {

    private String name;
    /**
     * Конструктор.
     * @param owner животное
     * @param param куча параметров
     */
    public DefineFunction(
        final AbstractAnimal owner, final SimpleBindings param
    ) {
        super(owner, param);
    }

    void stop() {

    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(name != null ? name : "DefineFunction").append("{\n");
        for (Routine routine : sequence) {
            sb.append("  ").append(routine).append(";\n");
        }
        sb.append("}");
        return sb.toString();
    }

}
