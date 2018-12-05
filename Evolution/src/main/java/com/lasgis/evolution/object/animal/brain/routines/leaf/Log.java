/**
 * @(#)Log.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.routines.leaf;

import ch.qos.logback.classic.Level;
import com.lasgis.evolution.object.animal.AbstractAnimal;
import com.lasgis.evolution.object.animal.brain.AbstractRoutine;
import lombok.extern.slf4j.Slf4j;

import javax.script.SimpleBindings;

/**
 * Логирование состояния. Запуск в общем случае:
 * Log(level, <описание>);
 *
 * @author Vladimir Laskin
 * @version 1.0
 */
@Slf4j
public class Log extends AbstractRoutine {

    /**
     * Конструктор.
     * @param owner животное
     * @param param куча параметров
     */
    public Log(final AbstractAnimal owner, final SimpleBindings param) {
        super(owner, param);
    }

    @Override
    public boolean act() {
        final String level = in(0);
        final String message = in(1);
        if (level != null) {
            if (message == null) {
                log.debug(owner.getUniqueName() + ": " + level);
            } else {
                ((ch.qos.logback.classic.Logger) log).log(null,
                    ch.qos.logback.classic.Logger.FQCN,
                    Level.toLocationAwareLoggerInteger(Level.toLevel(level.toLowerCase())),
                    owner.getUniqueName() + ": " + message,
                    null,
                    null);
            }
        } else {
            log.info(owner.getUniqueName() + ": трассировка без описания");
        }
        return true;
    }
}
