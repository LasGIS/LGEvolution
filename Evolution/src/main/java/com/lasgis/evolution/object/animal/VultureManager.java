/**
 * @(#)VultureManager.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal;

import com.lasgis.evolution.object.EvolutionConstants;

/**
 * Гриф - это не тот орёл что парит, а тот что каку клюёт.
 *
 * @author Laskin
 * @version 1.0
 * @since 23.03.13 10:51
 */
public class VultureManager extends AbstractAnimalManager {

    @Override
    public String getName() {
        return EvolutionConstants.VULTURE_KEY;
    }

    @Override
    public int getIndex() {
        return 40;
    }

    @Override
    protected AbstractAnimal createNew(double latitude, double longitude) {
        return new Vulture(latitude, longitude, this);
    }

}
