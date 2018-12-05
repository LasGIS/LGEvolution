/*
 * @(#)Chain.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.util;

import java.util.Iterator;

/**
 * Объект содержащий цепочку элементов. Элементом цепочки может являться любой
 * Java объект. Элементы можно добавлять, удалять
 * @param <Elem> элемент цепочки
 * @author VLaskin
 * @version 1.0
 * @since 22.07.2005 12:46:03
 */
public interface Chain<Elem> extends Iterable<Elem> {

    /**
     * Вернуть размер массива.
     * @return размер массива
     */
    int size();

    /**
     * Вернуть конкретный элемент цепочки.
     * @param index номер элемента
     * @return элемент цепочки
     */
    Elem get(int index);

    /**
     * Вернуть первый элемент цепочки.
     * @return элемент цепочки
     */
    Elem getFirst();

    /**
     * Вернуть последний элемент цепочки.
     * @return элемент цепочки
     */
    Elem getLast();

    /**
     * Добавляем элемент цепочки в конец списка.
     * @param element загружаемая элемент цепочки
     * @return признак успеха операции
     */
    boolean add(Elem element);

    /**
     * Добавляем элементарную точку в конкретное место списка.
     * @param index номер элемента
     * @param element загружаемая точка
     */
    void add(int index, Elem element);

    /**
     * Добавляем загружаемый список в конец списка.
     * @param elements загружаемый список
     * @return признак успеха операции
     */
    boolean add(Chain<? extends Elem> elements);

    /**
     * Добавляем новый список в конкретное место списка.
     * @param index место для загрузки
     * @param elements загружаемая точка
     * @return признак успеха операции
     */
    boolean add(int index, Chain<? extends Elem> elements);

    /**
     * Очищаем весь список.
     */
    void clear();

    /**
     * Удаляем только данный элемент.
     * @param index номер элемента
     * @return удалённый элемент цепочки
     */
    Elem remove(int index);

    /**
     * Удаляем всё от fromIndex до toIndex включительно.
     * @param fromIndex начальный номер элемента.
     * Элемент цепочки с этим номером тоже удалиться.
     * @param toIndex конечный номер элемента.
     * Элемент цепочки с этим номером тоже удалиться.
     */
    void remove(int fromIndex, int toIndex);

    /**
     * Проверяем на пустой список.
     * @return true если пустой
     */
    boolean isEmpty();

    /**
     * Возвращаем iterator элементов.
     * @return iterator данных элементов
     */
    Iterator<Elem> iterator();

    /**
     * Возвращаем массив элементов.
     * @return массив данных элементов
     */
    Elem[] toArray();

    /**
     * Возвращаем кусочек списка.
     * @param fromIndex первый элемент нового списка
     * @param toIndex последний элемент нового списка
     * @return часть списка от fromIndex до toIndex
     */
    Chain<Elem> subList(int fromIndex, int toIndex);

}

/*
 $Log: Chain.java,v $
 Revision 1.2  2005/07/28 08:44:07  VLaskin
 Add ChainList class and delete ChainArray class.

 Revision 1.1  2005/07/25 08:06:05  VLaskin
 Add Chain and ChainArray classes.

*/
