/**
 * @(#)CallBack.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal;

/**
 * Организация обратного вызова.
 * @author vladimir.laskin
 * @version 1.0
 */
public interface CallBack {

    /**
     * Обратный вызов.
     * @param actionType тип активности
     * @return признак выхода из функции. Если true,
     * то продолжаем выполнение, если false,
     * то выходим из функции.
     */
    boolean callBackAction(CallActionType actionType);
}
