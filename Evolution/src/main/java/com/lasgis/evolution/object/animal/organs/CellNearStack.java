/**
 * @(#)CellNearStack.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.organs;

import com.lasgis.evolution.map.Cell;
import com.lasgis.evolution.object.Info;
import com.lasgis.evolution.object.InfoType;

/**
 * Стек памяти кролика.
 * Здесь он хранит лучшие картины своего прошлого...<br/>
 * Точнее, здесь хранятся те ячейки,
 * на которых была вкусная капуста.
 * @author vladimir.laskin
 * @version 1.0
 */
@Info(type = InfoType.SAVE)
public class CellNearStack {
    private static final int MAP_SIZE = 12;

    @Info(type = InfoType.SAVE)
    private int mapSize;
    @Info(type = InfoType.SAVE)
    private Cell[] map;
    @Info(type = InfoType.SAVE)
    private int count = 0;

    /**
     * Конструктор стека памяти.
     */
    public CellNearStack() {
        mapSize = MAP_SIZE;
        map = new Cell[mapSize];
    }

    /**
     * Конструктор стека памяти.
     * @param mapSize размер стека памяти
     */
    public CellNearStack(final int mapSize) {
        this.mapSize = mapSize;
        map = new Cell[mapSize];
    }

    /**
     * запоминаем капусту.
     * @param cell ячейка
     * @param current текущая ячейка
     */
    public void add(final Cell cell, final Cell current) {
        int sel = -1;
        double distance = -1.0;
        for (int i = 0; i < mapSize; i++) {
            if (map[i] == null) {
                // выбираем пустую
                count++;
                sel = i;
                break;
            } else if (cell.equals(map[i])) {
                // такая уже есть
                return;
            } else {
                // выбираем самую дальнюю
                final double dis = current.distance(map[i]);
                if (dis > distance) {
                    distance = dis;
                    sel = i;
                }
            }
        }
        if (sel >= 0) {
            map[sel] = cell;
        }
    }

    /**
     * удаляем память об этой ячейке. Капусты здесь уже нет
     * @param delCell ячейка для удаления
     */
    public void del(final Cell delCell) {
        for (int i = 0; i < mapSize; i++) {
            if (delCell.equals(map[i])) {
                map[i] = null;
                count--;
                break;
            }
        }
    }

    /**
     * Вернуть ближайшую ячейку. При этом она стирается из памяти.
     * @param fromCell ячейка от которой ищем ближайшую
     * @return ячейка
     */
    public Cell getNearby(final Cell fromCell) {
        int sel = -1;
        double distance = Double.MAX_VALUE;
        for (int i = 0; i < mapSize; i++) {
            if (map[i] != null) {
                final double disInd = fromCell.distance(map[i]);
                if (disInd < distance) {
                    distance = disInd;
                    sel = i;
                }
            }
        }
        if (sel >= 0) {
            final Cell cell = map[sel];
            map[sel] = null;
            count--;
            return cell;
        } else {
            return null;
        }
    }

    /**
     * скока ячеек в памяти.
     * @return количество
     */
    public int size() {
        return count;
    }

}
