/**
 * @(#)PigManager.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal;

import com.lasgis.evolution.object.EvolutionConstants;
import com.lasgis.util.Util;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.IOException;

/**
 * Всеядное животное - свинья.
 *
 * @author Laskin
 * @version 1.0
 * @since 02.12.12 15:57
 */
@Slf4j
public class PigManager extends AbstractAnimalManager {

    @Override
    public String getName() {
        return EvolutionConstants.PIG_KEY;
    }

    @Override
    public int getIndex() {
        return 30;
    }

    @Override
    public Icon getIcon() {
        try {
            return Util.loadImageIcon("pig.png");
        } catch (final IOException ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    protected AbstractAnimal createNew(final double latitude, final double longitude) {
        return new Pig(latitude, longitude, this);
    }

}
