/*
 * @(#)AniTimeLine.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.animation.map.object;

import java.awt.*;

/**
 * The Class AniTimeLine. Линия с определённой толщиной
 * @author Vladimir Laskin
 * @version 1.0
 * @since 27.01.2011 : 15:33:13
 */
public class AniTimeLine extends AniTimeAbstractLine {

    /** Цвет линии. */
    private Color lineColor;

    /** Толщина линии. */
    private int thick;

    /**
     * Вернуть Цвет линии.
     * @return Цвет линии
     */
    public Color getLineColor() {
        return lineColor;
    }

    /**
     * Установить Цвет линии.
     * @param lineColor Цвет линии
     */
    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    /**
     * Вернуть Толщина линии.
     * @return Толщина линии
     */
    public int getThick() {
        return thick;
    }

    /**
     * Установить Толщина линии.
     * @param thick Толщина линии
     */
    public void setThick(int thick) {
        this.thick = thick;
    }

}
