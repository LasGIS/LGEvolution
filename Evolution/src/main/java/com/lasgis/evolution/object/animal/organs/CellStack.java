/**
 * @(#)CellStack.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.organs;

import com.lasgis.evolution.map.Cell;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Стек памяти кролика.
 * Здесь он хранит лучшие картины своего прошлого...<br/>
 * Точнее, здесь хранятся те ячейки,
 * на которых была вкусная капуста.
 * @author vladimir.laskin
 * @version 1.0
 */
public class CellStack {

    private final int maxSize;
    private Queue<Cell> queue = new LinkedList<>();
    private int count = 0;

    /**
     * Конструктор.
     */
    public CellStack() {
        maxSize = 12;
    }

    /**
     * Конструктор.
     * @param maxSize размер буфера стека
     */
    public CellStack(final int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * запоминаем капусту.
     * @param cell ячейка
     */
    public void add(final Cell cell) {
        del(cell);
        if (queue.size() >= maxSize) {
            del();
        }
        queue.add(cell);
    }

    /**
     * удаляем память о первой ячейке.
     */
    public void del() {
        queue.poll();
    }

    /**
     * удаляем память об этой ячейке.
     * @param cell ячейка
     */
    public void del(final Cell cell) {
        queue.remove(cell);
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getSize() {
        return queue.size();
    }

    /**
     * Проверяем, осталась ли данная ячейка.
     * @param cell ячейка для проверки
     * @return Returns <tt>true</tt> if this collection contains the specified element.
     */
    public boolean contain(final Cell cell) {
        return queue.contains(cell);
    }
    public boolean notContain(final Cell cell) {
        return !queue.contains(cell);
    }
}
