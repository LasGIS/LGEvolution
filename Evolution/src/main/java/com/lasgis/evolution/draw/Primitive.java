/**
 * @(#)Primitive.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.draw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Class Primitive.
 * @author vladimir.laskin
 * @version 1.0
 */
public class Primitive {

    private List<PrimitivePath> paths;

    /**
     * Конструктор.
     * @param aPaths
     */
    public Primitive(PrimitivePath... aPaths) {
        this.paths = new ArrayList<PrimitivePath>(aPaths.length);
        Collections.addAll(paths, aPaths);
    }

    public List<PrimitivePath> getPaths() {
        return paths;
    }

    public void setPaths(List<PrimitivePath> paths) {
        this.paths = paths;
    }
}
