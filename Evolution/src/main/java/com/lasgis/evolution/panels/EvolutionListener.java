/**
 * @(#)EvolutionListener.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.panels;

/**
 * @author vlaskin
 * @version 1.0 from Oct 31, 2006 : 4:06:22 PM
 */
public interface EvolutionListener {

    /**
     * Обработка события при смене режима.
     * @param event новый режим
     */
    void lgEventRegimeChanged(EvolutionEventRegime event);

}
