/**
 * @(#)TestAnimalManager.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal;

import com.lasgis.evolution.object.EvolutionConstants;

/**
 * The Class TestAnimalManager.
 * @author Vladimir Laskin
 * @version 1.0
 */
public class TestAnimalManager extends AbstractAnimalManager {

    @Override
    public String getName() {
        return EvolutionConstants.TEST_ANIMAL_KEY;
    }

    @Override
    public int getIndex() {
        return 20;
    }

    @Override
    protected AbstractAnimal createNew(double latitude, double longitude) {
        return new TestAnimal(latitude, longitude, this);
    }

}
