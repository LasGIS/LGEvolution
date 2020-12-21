/*
 * AntManager.java
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2020 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal;

import com.lasgis.evolution.object.EvolutionConstants;

/**
 * Простое травоядное животное - муравей.
 *
 * @author Laskin
 * @version 1.0
 * @since 02.12.12 15:57
 */
public class AntManager extends AbstractAnimalManager {

    @Override
    public String getName() {
        return EvolutionConstants.ANT_KEY;
    }

    @Override
    public int getIndex() {
        return 20;
    }

    @Override
    protected AbstractAnimal createNew(final double latitude, final double longitude) {
        return new Ant(latitude, longitude, this);
    }

}
