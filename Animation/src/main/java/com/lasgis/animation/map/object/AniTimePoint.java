/*
 * @(#)AniTimePoint.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.animation.map.object;

import java.awt.*;
import java.util.ArrayList;

/**
 * линия точки во времени.
 * @author Vladimir Laskin
 * @version 1.0
 * @since 27.01.2011 : 15:07:56
 */
public class AniTimePoint extends ArrayList<AniPoint> {

    /**
     * вернуть координаты точки для данного времени.
     * @param time время
     * @return точка
     */
    public final AniPoint getTime(double time) {
        AniPoint retPnt = null;
        AniPoint lastPnt = this.get(0);
        for (AniPoint pnt : this) {
            if (time >= lastPnt.getTime() && time <= pnt.getTime()) {
                if (pnt == lastPnt) {
                    retPnt = new AniPoint();
                    retPnt.setTime(time);
                    retPnt.setX(pnt.getX());
                    retPnt.setY(pnt.getY());
                } else {
                    double del = (time - lastPnt.getTime()) / (pnt.getTime() - lastPnt.getTime());
                    retPnt = new AniPoint();
                    retPnt.setTime(time);
                    retPnt.setX(lastPnt.getX() + (pnt.getX() - lastPnt.getX()) * del);
                    retPnt.setY(lastPnt.getY() + (pnt.getY() - lastPnt.getY()) * del);
                }
            }
            lastPnt = pnt;
        }
        return retPnt;
    }

    /**
     * Добавить точку по времени.
     * @param pnt декартовые координаты
     * @param time время
     * @return добавленная точка
     */
    public AniPoint add(
        Point pnt,
        double time
    ) {
        AniPoint ret = new AniPoint(pnt.getX(), pnt.getY(), time);
        if (this.size() == 0) {
            this.add(ret);
        } else if (time < this.get(0).getTime()) {
            this.add(0, ret);
        } else if (time > this.get(this.size() - 1).getTime()) {
            this.add(this.size(), ret);
        } else {
            for (int ib = 0, ie = 1; ie < this.size(); ib++, ie++) {
                if ((time >= this.get(ib).getTime()) && (time <= this.get(ie).getTime())) {
                    this.add(ie, ret);
                    return ret;
                }
            }
        }
        return ret;
    }

}
