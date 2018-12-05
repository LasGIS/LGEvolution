/*
 * @(#)ListChildNodes.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.util.xml;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Iterator;

/**
 * Этот класс делает .
 * @author Vladimir Laskin
 * @version 1.0
 * @since 04.03.2011 : 14:33:30
 */
public class ListChildNodes implements Iterable<Node>, Iterator<Node> {

    /** список элементов. */
    private NodeList childNodes;
    /** признак наступления конца. */
    private boolean isEnd = false;
    /** указание на следующий элемент. */
    private Node next = null;
    /** счётчик. */
    private int counter = 0;

    /**
     * Конструктор.
     * @param parentElement родительский Node элемент
     */
    public ListChildNodes(Node parentElement) {
        childNodes = parentElement.getChildNodes();
        isEnd = false;
        next = null;
        counter = 0;
    }

    /**
     * реализуем Iterable&lt;Node&gt; interface.
     * @return reference to itself
     */
    public Iterator<Node> iterator() {
        return this;
    }

    /**
     * Returns <tt>true</tt> if the iteration has more elements.
     * @return true if has more elements.
     */
    public boolean hasNext() {
        if (next != null) {
            return true;
        } else if (isEnd) {
            return false;
        } else {
            for (int i = counter; i < childNodes.getLength(); i++) {
                Node nod = childNodes.item(i);
                if (nod.getNodeType() == Node.ELEMENT_NODE) {
                    isEnd = false;
                    next = nod;
                    counter = i + 1;
                    return true;
                }
            }
            isEnd = true;
            next = null;
            return false;
        }
    }

    /**
     * @return next element
     */
    public Node next() {
        if (next != null) {
            Node nod = next;
            next = null;
            return nod;
        } else {
            if (hasNext()) {
                Node nod = next;
                next = null;
                return nod;
            }
        }
        return null;
    }

    /**
     * Ничего не делаем.
     */
    public void remove() {
    }
}
