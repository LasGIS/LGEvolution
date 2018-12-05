/**
 * @(#)AbstractPlantBehaviour.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object;

import com.lasgis.evolution.map.Cell;

import javax.swing.*;
import java.awt.*;

/**
 * The Class AbstractPlantBehaviour.
 *
 * @author Laskin
 * @version 1.0
 * @since 18.12.13 1:41
 */
public abstract class AbstractPlantBehaviour implements PlantBehaviour {

    private boolean isShow = true;

    @Override
    public int getIndex() {
        return 10;
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public boolean isShow() {
        return isShow;
    }

    @Override
    public void setShow(final boolean show) {
        isShow = show;
    }

    @Override
    public PlantBehaviour[] subElements() {
        return new PlantBehaviour[0];
    }

    @Override
    public void drawPlant(final Graphics gr, final Rectangle rec, final Cell cell) {
    }

    @Override
    public void cellProcessing(final Cell cell) {
    }

    @Override
    public void init() {
    }

    @Override
    public void stop() {
    }

    @Override
    public String toString() {
        return getName();
    }
}
