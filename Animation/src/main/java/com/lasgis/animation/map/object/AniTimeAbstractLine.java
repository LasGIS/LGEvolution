/*
 * @(#)AniTimeAbstractLine.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.animation.map.object;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * The Class AniTimeAbstractLine. Абстрактная линия.
 * @author Vladimir Laskin
 * @version 1.0
 * @since 27.01.2011 : 16:30:17
 */
public class AniTimeAbstractLine extends ArrayList<AniTimePoint> {

    /** допустимая погрешность для поиска точки. */
    private static final double DELTA = 3.0;

    /**
     * получить слепок линии для данного времени.
     * @param time данное время
     * @return список точек для фиксированного времени
     */
    public ArrayList<AniPoint> getTime(double time) {
        ArrayList<AniPoint> retList = new ArrayList<AniPoint>(this.size());
        for (AniTimePoint timePnt : this) {
            AniPoint pnt = timePnt.getTime(time);
            if (pnt != null) {
                retList.add(timePnt.getTime(time));
            }
        }
        return retList;
    }

    /**
     * Находим точку линии в окрестностях заданной точки и интервала по времени.
     * @param pnt координаты точки
     * @param curTime текущее время (для создания новой точки)
     * @param evn MouseEvent для определения реакции на создание новой точки или удаления старой.
     * @return ссылка на найденную точку
     */
    public CapturedObject findPoint(
        Point pnt,
        double curTime,
        MouseEvent evn
    ) {
        // время начала поиска
        double begTime = curTime - 30;
        // время конца поиска
        double endTime = curTime + 30;
        for (AniTimePoint timePoint : this) {
            for (AniPoint point : timePoint) {
                double time = point.getTime();
                double x = point.getX();
                double y = point.getY();
                if ((time >= begTime) && (time <= endTime)
                    && (x >= pnt.getX() - DELTA) && (x <= pnt.getX() + DELTA)
                    && (y >= pnt.getY() - DELTA) && (y <= pnt.getY() + DELTA)) {
                    if (evn != null) {
/*
                        if (!evn.isControlDown() && !evn.isShiftDown()
                            && evn.getButton() == MouseEvent.BUTTON3) {
                            // удаляем точку
                            timePoint.remove(point);
                            if (timePoint.isEmpty()) {
                                this.remove(timePoint);
                            }
                        } else {
*/
                        if (evn.isControlDown()) {
                            // добавляем новую мировую линию справа или слева от данной точки
                            int ind = this.indexOf(timePoint);
                            if (ind == 0) {
                                // добавляем спереди
                                AniTimePoint newTimePoint = new AniTimePoint();
                                AniPoint newPoint = newTimePoint.add(pnt, time);
                                this.add(0, newTimePoint);
                                return (new CapturedObject(this, newTimePoint, newPoint));
                            } else if (ind == this.size() - 1) {
                                // добавляем сзади
                                AniTimePoint newTimePoint = new AniTimePoint();
                                AniPoint newPoint = newTimePoint.add(pnt, time);
                                this.add(newTimePoint);
                                return (new CapturedObject(this, newTimePoint, newPoint));
                            }
                        } else if (evn.isShiftDown()) {
                            // добавляем новую точку для данной мировой линии с текущим временем
                            AniPoint newPoint = timePoint.add(pnt, curTime);
                            return (new CapturedObject(this, timePoint, newPoint));
                        }
//                        }
                    }
                    return (new CapturedObject(this, timePoint, point));
                }
            }
        }
        return null;
    }

    /**
     * Находим точку на линии в окрестностях заданной точки и интервала по времени.
     * @param pnt координаты точки
     * @param time время поиска
     * @return номер точки для этой линии. Если это полигон, то точка может быть между
     * последней и первой.
     */
    public CapturedObject findLine(Point pnt, double time) {
        int fst = 0;
        int middle = -1;
        int nxt = 1;
        int lst = this.size();
        if (this instanceof AniTimePolygon) {
            fst = lst - 1;
            nxt = 0;
        }
        for (int i = nxt; i < lst; i++) {
            AniPoint pnt1 = this.get(fst).getTime(time);
            AniPoint pnt2 = this.get(i).getTime(time);
            if (pnt1 != null) {
                if (pnt2 != null) {
                    // и первая точка найдена, и вторая.
                    if (calcDistance(pnt1, pnt2, pnt) < DELTA) {
                        AniPoint ret;
                        AniTimePoint timePoint;
                        if (middle >= 0) {
                            // средний AniTimePoint существует под номером middle
                            timePoint = this.get(middle);
                            ret = timePoint.add(pnt, time);
                        } else {
                            timePoint = new AniTimePoint();
                            ret = timePoint.add(pnt, time);
                            this.add(i, timePoint);
                        }
                        return (new CapturedObject(this, timePoint, ret));
                    }
                    fst = i;
                    middle = -1;
                } else {
                    // первая точка найдена, а вторая нет
                    middle = i;
                }
            } else {
                // первая точка не найдена
                fst = i;
            }
        }
        return null;
    }

    /**
     * Найти расстояние до ближайшей точки на отрезке.
     * @param p1 первая точка отрезка
     * @param p2 вторая точка отрезка
     * @param mouse точка мышки
     * @return расстояние до ближайшей точки
     */
    public static double calcDistance(AniPoint p1, AniPoint p2, Point mouse) {
        AniPoint normal = calcNormalPoint(p1, p2, mouse, true);
        if (normal != null) {
            // получаем расстояние
            return Math.sqrt(
                  Math.pow(mouse.getX() - normal.getX(), 2)
                + Math.pow(mouse.getY() - normal.getY(), 2)
            );
        }
        return Double.MAX_VALUE;
    }

    /**
     * Найти ближайшую точку (перпендикуляр).
     * @param p1 первая точка отрезка
     * @param p2 вторая точка отрезка
     * @param mouse точка мышки
     * @param normalOnly для true возвращаем только если нормаль лежит внутри отрезка,
     *        а иначе - ближайшая точка это одна из конечных отрезка.
     * @return ближайшая точка на отрезке к точке мышки (нормаль или крайняя точка отрезка)
     */
    public static AniPoint calcNormalPoint(
        AniPoint p1,
        AniPoint p2,
        Point mouse,
        boolean normalOnly
    ) {

        // точные значения средней точки
        AniPoint normal = new AniPoint();

        // коэффициенты уравнения линии ( x = K * y + C ) для отрезка
        double k1;
        double c1;

        // коэффициенты уравнения линии для нормали от точки
        double k2;
        double c2;

        // определяем относительное расположение точки m_pNormal
        if ((p1.getX() == p2.getX()) && (p1.getY() == p2.getY())) {
            // обнаружили, что линия коллапсировала в точку
            normal.setX(p1.getX());
            normal.setY(p1.getY());
        } else if (p1.getX() == p2.getX()) {
            // обнаружили, что линия параллельна оси Y
            normal.setX(p1.getX());
            normal.setY(mouse.getY());
        } else if (p1.getY() == p2.getY()) {
            // обнаружили, что линия параллельна оси X
            normal.setY(p1.getY());
            normal.setX(mouse.getX());
        } else {
            // линия расположена произвольно

            // определяем первые коэффициенты
            k1 = (p1.getY() - p2.getY()) / (p1.getX() - p2.getX());
            k2 = -1.0 / k1;

            // определяем вторые коэффициенты
            c1 = p1.getY() - k1 * p1.getX();
            c2 = mouse.getY() - k2 * mouse.getX();
            normal.setX((c2 - c1) / (k1 - k2));
            normal.setY(k2 * normal.getX() + c2);
        }

        // проверяем относительное положение точек
        if (((normal.getX() > p1.getX()) && (p1.getX() > p2.getX()))
            || ((normal.getY() > p1.getY()) && (p1.getY() > p2.getY()))
            || ((normal.getX() < p1.getX()) && (p1.getX() < p2.getX()))
            || ((normal.getY() < p1.getY()) && (p1.getY() < p2.getY()))
        ) {
            // нормальная точка вышла за рамки отрезка со стороны первой точки
            if (normalOnly) {
                return null;
            } else {
                normal.setX(p1.getX());
                normal.setY(p1.getY());
            }
        } else if (((normal.getX() > p2.getX()) && (p2.getX() > p1.getX()))
            || ((normal.getY() > p2.getY()) && (p2.getY() > p1.getY()))
            || ((normal.getX() < p2.getX()) && (p2.getX() < p1.getX()))
            || ((normal.getY() < p2.getY()) && (p2.getY() < p1.getY()))
        ) {
            // нормальная точка вышла за рамки отрезка со стороны второй точки
            if (normalOnly) {
                return null;
            } else {
                normal.setX(p2.getX());
                normal.setY(p2.getY());
            }
        }

        return normal;
    } // calcPositions()

}
