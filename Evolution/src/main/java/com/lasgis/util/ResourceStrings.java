/*
 * ResourceStrings.java
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2021 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.util;

/**
 * Общие настройки.
 * @author vlaskin
 * @version 1.0
 * @since May 31, 2010 : 7:04:15 PM
 */
class EvolutionResourceStrings implements ResourceStrings {

    /** Resource files. */
    private static final String[] RESOURCES = {
        "Evolution.properties"
    };

    @Override
    public String[] resourceFiles() {
        return RESOURCES;
    }
}
