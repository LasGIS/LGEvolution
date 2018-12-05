/*
 * @(#)ChainIterator.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * ChainIterator - это упорядоченная цепочка элементов - звеньев, которая поддерживает интерфейс Iterator.
 * @author Vladimir Laskin
 * @version 1.0
 * @since 28.07.2005 12:35:01
 *
 * @param <Elem> class одного звена.
 */
public class ChainIterator<Elem> implements Iterator<Elem> {

    private Elem[] elements;
    private int size;
    private int count = 0;

    /**
     * Constructor.
     * @param elem массив элементов
     * @param size размер массива элементов
     */
    public ChainIterator(Elem[] elem, int size) {
        elements = elem;
        this.size = size;
        count = 0;
    }

    /**
     * Возвращает <tt>true</tt> если в итераторе есть элементы и <tt>true</tt>
     * в противном случае.
     *
     * @return <tt>true</tt> Если в итераторе есть элементы.
     */
    public boolean hasNext() {
        return count < size;
    } // hasNext():boolean

    /**
     * Вернуть следующий элемент из итератора. Вызывайте этот метод
     * последовательно пока метод {@link #hasNext()} не вернёт false для
     * последовательного получения каждого элемента из списка.
     *
     * @return следующий элемент из итератора.
     * @see NoSuchElementException в итераторе больше нет элементов.
     */
    public Elem next() {
        if (hasNext()) {
            return elements[count++];
        } else {
            // "cartridges is empty"
            throw new NoSuchElementException("Кончились патрончики");
        }
    } // next():Elem

    /**
     * Оператор удаления здесь не используется. Необходимо удалять в классе
     * 'ChainList'
     * @see UnsupportedOperationException Этот Exception показывает, что
     * оператор удаления здесь не используется.
     * @see ChainList#remove(int)
     * @see ChainList#remove(int, int)
     */
    public void remove() {
        throw new UnsupportedOperationException("Здесь удалить нельзя");
    }
}
