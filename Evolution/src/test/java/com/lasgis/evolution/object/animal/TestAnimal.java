/**
 * @(#)TestAnimal.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal;

import com.lasgis.evolution.panels.Scalable;

import java.awt.*;

/**
 * The Class TestAnimal.
 * @author Vladimir Laskin
 * @version 1.0
 */
public class TestAnimal extends AbstractAnimal {

    private double birthMass = 100.0;

    /**
     * В момент создания присваиваем уникальное имя.
     * @param latitude широта точки
     * @param longitude долгота точки
     * @param manager сохраняем менеджер для рождения и умирания
     */
    public TestAnimal(
        final double latitude, final double longitude, final TestAnimalManager manager
    ) {
        super(latitude, longitude, manager);
    }

    @Override protected boolean action() {
        return false;
    }

    @Override public void draw(final Graphics graphics, final Scalable interval) {

    }

    @Override public void toBirth() {

    }

    @Override public double getFullMass() {
        return 0;
    }

    @Override public void incDines() {

    }

    @Override public double getRelativeAge() {
        return 0;
    }

    @Override public boolean callBackAction(final CallActionType actionType) {
        return false;
    }

    public double getBirthMass() {
        return birthMass;
    }

    public void setBirthMass(final double birthMass) {
        this.birthMass = birthMass;
    }
}
