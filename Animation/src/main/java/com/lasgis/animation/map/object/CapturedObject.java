/*
 * @(#)CapturedObject.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.animation.map.object;

/**
 * Это объект, который в данный момент был захвачен мышкой.
 * @author Vladimir Laskin
 * @version 1.0
 * @since 11.04.2011 : 17:10:29
 */
public class CapturedObject {

    /** Захваченный полигон или полилиния. */
    private AniTimeAbstractLine polygon;

    /** временная линия внутри этого полигона. */
    private AniTimePoint timePoint;

    /** точка во времени на этой временной линии. */
    private AniPoint point;

    /**
     * Конструктор объекта.
     * @param aPolygon Захваченный полигон или полилиния
     * @param aTimePoint временная линия внутри этого полигона
     * @param aPoint точка во времени на временной линии
     */
    public CapturedObject(
        AniTimeAbstractLine aPolygon,
        AniTimePoint aTimePoint,
        AniPoint aPoint
    ) {
        polygon = aPolygon;
        timePoint = aTimePoint;
        point = aPoint;
    }

    public AniTimeAbstractLine getPolygon() {
        return polygon;
    }

    public void setPolygon(AniTimeAbstractLine polygon) {
        this.polygon = polygon;
    }

    public AniTimePoint getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(AniTimePoint timePoint) {
        this.timePoint = timePoint;
    }

    public AniPoint getPoint() {
        return point;
    }

    public void setPoint(AniPoint point) {
        this.point = point;
    }
}
