/**
 * @(#)LiveObjectElement.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object;

import javax.swing.*;

/**
 * Эти .
 * @author vladimir.laskin
 * @version 1.0
 */
public interface LiveObjectElement {

    /**
     * Название животного, растения или субстанции.
     * @return Название сущности.
     */
    String getName();

    /**
     * Индекс, необходимый для определения
     * последовательности вывода на экран.
     * @return индекс
     */
    int getIndex();

    /**
     * Иконка для показа в меню.
     * @return Иконка
     */
    Icon getIcon();

    /**
     * .
     * @return .
     */
    boolean isShow();

    /**
     * .
     * @param isShow .
     */
    void setShow(boolean isShow);

}
