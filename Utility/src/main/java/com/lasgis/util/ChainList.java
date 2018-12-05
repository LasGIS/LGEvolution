/*
 * @(#)ChainList.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */
package com.lasgis.util;

import java.util.Iterator;

/**
 * Реализация интерфейса Chain&ln;Elem&gt;.
 * @author VLaskin
 * @version 1.0 (28.07.2005 11:40:52)
 * @param <Elem> specific Chains element
 */
public class ChainList<Elem> implements Chain<Elem> {

    /**
     * The array buffer into which the elements of the ArrayList are stored.
     * The capacity of the ArrayList is the length of this array buffer.
     */
    private Elem[] elementData;

    /**
     * The size of the ArrayList (the number of elements it contains).
     *
     * @serial
     */
    private int size = 0;

    /** Изначальный размер списка. */
    private static final int DELTA_SIZE = 10;

    /**
     *
     */
    private static final double INCREASE_NEW_CAPACITY = 1.5;

    /**
     * Constructs an empty list with the specified initial capacity.
     * @param   initialCapacity   the initial capacity of the list.
     * @see IllegalArgumentException if the specified initial capacity is negative
     */
    public ChainList(final int initialCapacity) {
        super();
        if (initialCapacity < 0) {
            throw new IllegalArgumentException(
                "Illegal Capacity: " + initialCapacity
            );
        }
        //noinspection unchecked
        elementData = (Elem[]) new Object[initialCapacity];
    } // ChainList(int)

    /**
     * Constructs an empty list with an initial capacity of ten.
     */
    public ChainList() {
        this(DELTA_SIZE);
    } // ChainList()

    /**
     * Check if the given index is in range.  If not, throw an appropriate
     * runtime exception.  This method does *not* check if the index is
     * negative: It is always used immediately prior to an array access,
     * which throws an ArrayIndexOutOfBoundsException if index is negative.
     * @param index проверяемый индекс
     * throws IndexOutOfBoundsException To indicate that index is out of range.
     */
    private void rangeCheck(
        int index
    ) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException(
                "Index: " + index + ", Size: " + size
            );
        }
    } // rangeCheck(int)

    /**
     * Проверка на валидность индекса.
     * @param index проверяемый индекс
     * throws IndexOutOfBoundsException To indicate that index is out of range.
     */
    private void rangeCheckPlus(int index) {
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException(
                "Index: " + index + ", Size: " + size
            );
        }
    } // rangeCheckPlus(int)

    /**
     * Increases the capacity of this <tt>ArrayList</tt> instance, if
     * necessary, to ensure  that it can hold at least the number of elements
     * specified by the minimum capacity argument.
     *
     * @param   minCapacity   the desired minimum capacity.
     */
    private void ensureCapacity(int minCapacity) {
        int oldCapacity = elementData.length;
        if (minCapacity > oldCapacity) {
            Elem[] oldData = elementData;
            int newCapacity = (int) (oldCapacity * INCREASE_NEW_CAPACITY) + 1;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            //noinspection unchecked
            elementData = (Elem[]) new Object[newCapacity];
            System.arraycopy(oldData, 0, elementData, 0, size);
        }
    } // ensureCapacity(int)

    /**
     * Вернуть размер массива.
     * @return размер
     */
    public final int size() {
        return size;
    } // size():int

    /**
     * Вернуть конкретный элемент.
     * @param index индекс элемента
     * @return конкретный элемент
     */
    public final Elem get(final int index) {
        rangeCheck(index);
        return elementData[index];
    } // get(int):Elem

    /**
     * Вернуть первый элемент цепочки.
     * @return элемент цепочки
     */
    public final Elem getFirst() {
        return elementData[0];
    }

    /**
     * Вернуть последний элемент цепочки.
     * @return элемент цепочки
     */
    public final Elem getLast() {
        return elementData[size - 1];
    }

    /**
     * Добавляем загружаемый элемент в конец списка.
     * @param element загружаемый элемент
     * @return признак успеха операции
     */
    public final boolean add(final Elem element) {
        ensureCapacity(size + 1);
        elementData[size++] = element;
        return true;
    } // add(Elem):boolean

    /**
     * Добавляем загружаемый элемент в конкретное место списка. При этом вся
     * цепочка будет сдвинута на одну позицию влево
     * @param index загружаемый элемент
     * @param element загружаемая точка
     */
    public final void add(final int index, final Elem element) {
        rangeCheckPlus(index);
        ensureCapacity(size + 1);
        System.arraycopy(
            elementData, index, elementData, index + 1, size - index
        );
        elementData[index] = element;
        size++;
    } // add(int, Elem):boolean

    /**
     * Добавляем загружаемый список в конец списка.
     * @param elements загружаемый список
     * @return true если
     */
    public final boolean add(final Chain<? extends Elem> elements) {
        Elem[] arr = elements.toArray();
        int numNew = arr.length;
        ensureCapacity(size + numNew);
        System.arraycopy(arr, 0, elementData, size, numNew);
        size += numNew;
        return numNew > 0;
    } // add(Chain<Elem>):boolean

    /**
     * Добавляем список в конкретное место списка.
     * @param index место для загрузки
     * @param elements загружаемый список
     * @return true если было добавление элементов
     */
    public boolean add(int index, Chain<? extends Elem> elements) {
        rangeCheckPlus(index);
        Elem[] arr = elements.toArray();
        int numNew = arr.length;
        ensureCapacity(size + numNew);

        int numMoved = size - index;
        if (numMoved > 0) {
            System.arraycopy(
                elementData, index, elementData, index + numNew, numMoved
            );
        }
        System.arraycopy(arr, 0, elementData, index, numNew);
        size += numNew;
        return numNew > 0;
    } // add(int, Chain<Elem>):boolean

    /**
     * Добавляем загружаемый элемент в конкретное место списка.
     * При этом цепочка не сдвигается.
     * @param index загружаемый элемент
     * @param element загружаемая точка
     * @return старый элемент, находящийся в этом месте
     */
    public final Elem set(final int index, final Elem element) {
        rangeCheck(index);
        Elem oldElement = elementData[index];
        elementData[index] = element;
        return oldElement;
    } // add(int, Elem):boolean

    /**
     * Очищаем весь список.
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            elementData[i] = null;
        }
        size = 0;
    } // clear()

    /**
     * Удаляем только данный элемент.
     * @param index индекс элемента
     * @return возвращаем удалённый элемент
     */
    public Elem remove(int index) {
        rangeCheck(index);
        Elem oldValue = elementData[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(
                elementData, index + 1, elementData, index, numMoved
            );
        }
        elementData[--size] = null;
        return oldValue;
    } // remove(int):Elem

    /**
     * Удаляем всё от fromIndex до toIndex включительно.
     * @param fromIndex начальный индекс
     * @param toIndex конечный индекс
     */
    public void remove(int fromIndex, int toIndex) {
        if (fromIndex > toIndex) {
            throw new IndexOutOfBoundsException(
                "Индекс fromIndex:= ("
                + fromIndex
                + ") должен быть меньше чем индекс toIndex:= ("
                + toIndex
                + ")."
            );
        }
        rangeCheck(fromIndex);
        rangeCheck(toIndex);
        int numMoved = size - toIndex - 1;
        if (numMoved > 0) {
            System.arraycopy(
                elementData, toIndex + 1, elementData, fromIndex, numMoved
            );
        }
        for (int i = fromIndex + numMoved; i < size; i++) {
            elementData[i] = null;
        }
    } // remove(int, int)

    /**
     * Проверяем на пустой список.
     * @return true если пустой
     */
    public boolean isEmpty() {
        return size == 0;
    } // isEmpty():boolean

    /**
     * Возвращаем iterator элементов.
     * @return iterator данных элементов
     */
    public Iterator<Elem> iterator() {
        return new ChainIterator<Elem>(elementData, size);
    } // iterator():Iterator<Elem>

    /**
     * Возвращаем новый массив элементов.
     * @return массив данных элементов
     */
    public Elem[] toArray() {
        //noinspection unchecked
        Elem[] result = (Elem[]) new Object[size];
        System.arraycopy(elementData, 0, result, 0, size);
        return result;
    } // toArray():Elem[]

    /**
     * Возвращаем кусочек списка.
     * @param fromIndex первый элемент нового списка
     * @param toIndex последний элемент нового списка
     * @return часть списка от fromIndex до toIndex
     */
    public ChainList<Elem> subList(int fromIndex, int toIndex) {
        if (fromIndex > toIndex) {
            throw new IndexOutOfBoundsException(
                "Индекс fromIndex:= ("
                + fromIndex
                + ") должен быть меньше чем индекс toIndex:= ("
                + toIndex
                + ")."
            );
        }
        rangeCheck(fromIndex);
        rangeCheck(toIndex);
        int numMoved = fromIndex - toIndex + 1;
        ChainList<Elem> newList = new ChainList<Elem>(numMoved);
        if (numMoved > 0) {
            System.arraycopy(elementData, fromIndex, newList, 0, numMoved);
        }
        return newList;
    } // subList(int, int):Chain<Elem>

    /**
     * Вернуть полный список элементов.
     * @return список элементов
     */
    public Elem[] getElementData() {
        return elementData;
    }

    /**
     * Установить весь список элементов.
     * @param elementData список элементов
     */
    public void setElementData(Elem[] elementData) {
        this.elementData = elementData;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < size(); i++) {
            if (i == 0) {
                sb.append("  ");
            } else {
                sb.append("\n  ");
            }
            Elem elm = get(i); 
            sb.append(elm);
        }
        return sb.toString();
    }
}
