/*
 * @(#)Animation.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.animation.map;

import com.lasgis.animation.map.object.AniTimeLine;
import com.lasgis.animation.map.object.AniTimePolygon;

import java.util.ArrayList;

/**
 * The Class Animation.
 * @author Vladimir Laskin
 * @version 1.0
 * @since 28.01.2011 : 15:37:48
 */
public class Animation {

    /** имя файла с анимацией (если таковой существует). */
    private String fileName = null;

    /** список линий. */
    private ArrayList<AniTimeLine> lines = new ArrayList<AniTimeLine>();

    /** список полигонов. */
    private ArrayList<AniTimePolygon> polygons = new ArrayList<AniTimePolygon>();

    public ArrayList<AniTimeLine> getLines() {
        return lines;
    }

    /**
     * Добавить временную линию.
     * @param line временная линия.
     */
    public void add(AniTimeLine line) {
        lines.add(line);
    }

    public ArrayList<AniTimePolygon> getPolygons() {
        return polygons;
    }

    /**
     * Добавить полигон к цепочке.
     * @param polygon полигон
     */
    public void add(AniTimePolygon polygon) {
        polygons.add(polygon);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
