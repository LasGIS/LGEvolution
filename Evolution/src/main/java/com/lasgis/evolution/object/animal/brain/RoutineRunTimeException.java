/**
 * @(#)RoutineRunTimeException.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2014 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain;

/**
 * The Class RoutineRunTimeException.
 * @author Vladimir Laskin
 * @version 1.0
 */
public class RoutineRunTimeException extends RuntimeException {

    final AbstractOperator operator;

    /**
     * Constructor.
     * @param message message
     * @param aOperator operator
     */
    public RoutineRunTimeException(final String message, final AbstractOperator aOperator) {
        super(message);
        operator = aOperator;
    }

    @Override public String toString() {
        return "RoutineRunTimeException message: " + getMessage() + "; operator: " +  operator.toString()
            + super.toString();
    }
}
