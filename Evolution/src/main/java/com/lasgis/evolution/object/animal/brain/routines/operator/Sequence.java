/**
 * @(#)Sequence.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.routines.operator;

import com.lasgis.evolution.object.animal.AbstractAnimal;
import com.lasgis.evolution.object.animal.brain.AbstractRoutine;
import com.lasgis.evolution.object.animal.brain.Routine;
import lombok.extern.slf4j.Slf4j;

import javax.script.SimpleBindings;
import java.util.ArrayList;
import java.util.List;

/**
 * Последовательность выполнения стратегий.
 * @author Vladimir Laskin
 * @version 1.0
 */
@Slf4j
public class Sequence extends AbstractRoutine {

    /** индекс текущей стратегии. */
    int index;
    /** список последовательно выполняющихся стратегий. */
    List<Routine> sequence;

    /**
     * Конструктор.
     * @param owner животное
     * @param param куча параметров
     */
    public Sequence(final AbstractAnimal owner, final SimpleBindings param) {
        super(owner, param);
        index = 0;
        sequence = new ArrayList<>();
    }

    @Override
    public boolean act() {
        if (sequence.isEmpty()) {
            return true;
        }
        if (sequence.get(index).debugAct()) {
            index++;
            if (index >= sequence.size()) {
                index = 0;
                return true;  // стратегия окончена
            }
        }
        return false;  // стратегия продолжается
    }

    /**
     * Добавляем очередную стратегию в конец списка.
     * @param routine стратегия
     * @return возвращаем самого себя для нанизывания вызова
     */
    public Sequence addRoutine(final Routine routine) {
        sequence.add(routine);
        return this;
    }

    @Override public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Sequence").append("{\n");
        for (Routine routine : sequence) {
            sb.append("  ").append(routine).append(";\n");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override public void clear() {
        index = 0;
        for (Routine routine : sequence) {
            routine.clear();
        }
    }
}
