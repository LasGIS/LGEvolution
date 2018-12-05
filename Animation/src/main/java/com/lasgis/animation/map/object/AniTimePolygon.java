/*
 * @(#)AniTimePolygon.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.animation.map.object;

import java.awt.*;

/**
 * The Class AniTimePolygon. Полигон с заливкой
 * @author Vladimir Laskin
 * @version 1.0
 * @since 27.01.2011 : 15:33:28
 */
public class AniTimePolygon extends AniTimeLine {

    /** Цвет заливки. */
    private Color fillColor;

    /**
     * Вернуть Цвет заливки.
     * @return Цвет заливки
     */
    public Color getFillColor() {
        return fillColor;
    }

    /**
     * Установить Цвет заливки.
     * @param fillColor Цвет заливки
     */
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

}
