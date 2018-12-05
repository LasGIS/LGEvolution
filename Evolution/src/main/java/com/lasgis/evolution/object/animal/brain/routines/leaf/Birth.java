/**
 * @(#)Birth.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.routines.leaf;

import com.lasgis.evolution.object.animal.AbstractAnimal;
import com.lasgis.evolution.object.animal.brain.AbstractRoutine;
import lombok.extern.slf4j.Slf4j;

import javax.script.SimpleBindings;
import java.lang.reflect.Field;

/**
 * Рожаем.
 *
 * @author Vladimir Laskin
 * @version 1.0
 */
@Slf4j
public class Birth extends AbstractRoutine {

    /**
     * Конструктор.
     * @param owner животное
     * @param param куча параметров
     */
    public Birth(final AbstractAnimal owner, final SimpleBindings param) {
        super(owner, param);
    }

    @Override
    public boolean act() {
        double birthMass = 200;
        try {
            final Field birthMassField = owner.getClass().getDeclaredField("birthMass");
            birthMassField.setAccessible(true);
            birthMass = birthMassField.getDouble(owner);
        } catch (final Exception ex) {
            log.debug(ex.getMessage(), ex);
        }
        if (owner.getMass() > birthMass) {
            owner.toBirth();
            owner.addSkipNanoTime(2000000);
        }
        return true;
    }
}
