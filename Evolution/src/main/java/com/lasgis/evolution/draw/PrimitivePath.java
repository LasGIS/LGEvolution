/**
 * @(#)PrimitivePath.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.draw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Class PrimitivePath.
 * @author vladimir.laskin
 * @version 1.0
 */
public class PrimitivePath {
    PrimitiveType type;
    PrimitiveColor fillColor;
    PrimitiveColor lineColor;
    List<PrimitiveAddon> addons;

    /**
     * .
     * @param type .
     * @param color .
     * @param aAddons  .
     */
    public PrimitivePath(
        final PrimitiveType type,
        final PrimitiveColor color,
        final PrimitiveAddon... aAddons
    ) {
        this.type = type;
        this.fillColor = this.lineColor = color;
        this.addons = new ArrayList<>(aAddons.length);
        Collections.addAll(addons, aAddons);
    }

    /**
     * .
     * @param type .
     * @param fillColor .
     * @param lineColor .
     * @param aAddons  .
     */
    public PrimitivePath(
        final PrimitiveType type,
        final PrimitiveColor fillColor,
        final PrimitiveColor lineColor,
        final PrimitiveAddon... aAddons
    ) {
        this.type = type;
        this.fillColor = fillColor;
        this.lineColor = lineColor;
        this.addons = new ArrayList<>(aAddons.length);
        Collections.addAll(addons, aAddons);
    }

    public PrimitiveType getType() {
        return type;
    }

    public void setType(final PrimitiveType type) {
        this.type = type;
    }

    public PrimitiveColor getFillColor() {
        return fillColor;
    }

    public void setFillColor(final PrimitiveColor fillColor) {
        this.fillColor = fillColor;
    }

    public PrimitiveColor getLineColor() {
        return lineColor;
    }

    public void setLineColor(final PrimitiveColor lineColor) {
        this.lineColor = lineColor;
    }

    public List<PrimitiveAddon> getAddons() {
        return addons;
    }

    public void setAddons(final List<PrimitiveAddon> addons) {
        this.addons = addons;
    }

}
