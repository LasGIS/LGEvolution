/**
 * @(#)OperatorWrapper.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.compile;

import com.lasgis.evolution.object.animal.brain.AbstractOperator;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The Class RoutineWrapper.
 * @author Vladimir Laskin
 * @version 1.0
 */
@Getter
@AllArgsConstructor(staticName = "of")
public class OperatorWrapper {
    private AbstractOperator routine;
    private TokenParser.Token token;
}
