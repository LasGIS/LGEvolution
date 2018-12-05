/**
 * @(#)RoutineDefinition.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.compile;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Здесь лежит определение Routine.
 * @author Vladimir Laskin
 * @version 1.0
 */
@Data
public class RoutineDefinition {
    private boolean main;
    private String name;
    private List<String> inpParam = new ArrayList<>();
    private List<String> outParam = new ArrayList<>();
    private TokenParser.Token body;

    public void addInpParam(String string) {
        inpParam.add(string);
    }

    public void addOutParam(String string) {
        outParam.add(string);
    }
}
