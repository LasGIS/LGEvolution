/**
 * @(#)EvolutionEvent.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.panels;

/**
 * @author vlaskin
 * @version 1.0 from Oct 31, 2006 : 4:27:09 PM
 */
public interface EvolutionEvent {

    // Что конкретно сменилось.
    /** Сменился режим. */
    int REGIME_CHANGED = 1;

    /**
     * Вернуть тип события, например: REGIME_CHANGED.
     * @return тип события как integer
     */
    int geTypeEvent();

}
