/**
 * @(#)EvolutionEventDispatcher.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.panels;

import com.lasgis.util.ChainList;

import java.util.Iterator;

/**
 * Класс, который содержит получателей и раздаёт события.
 *
 * @author vlaskin
 * @version 1.0 from Oct 31, 2006 : 4:40:00 PM
 */
public final class EvolutionEventDispatcher {

    /** список получателей. */
    private ChainList<EvolutionListener> listeners = new ChainList<>();

    /** единственный объект класса. */
    private static EvolutionEventDispatcher lasgisEventDispatcher = null;

    /**
     * Гарантированное создание единичного объекта EvolutionEventDispatcher.
     * @return единственный класс EvolutionEventDispatcher
     */
    public static EvolutionEventDispatcher singletonLasgisEventDispatcher() {
        if (lasgisEventDispatcher == null) {
            lasgisEventDispatcher = new EvolutionEventDispatcher();
        }
        return lasgisEventDispatcher;
    }

    /**
     * Блокирование открытого конструктора.
     */
    private EvolutionEventDispatcher() {
    }

    /**
     * Добавление очередного получателя.
     * @param listener получатель {@link EvolutionListener}
     */
    public synchronized void add(final EvolutionListener listener) {
        listeners.add(listener);
    }

    /**
     * Запуск процедуры отправки событий.
     * @param event получатель {@link EvolutionListener}
     */
    public synchronized void dispatch(final EvolutionEvent event) {
        final Iterator<EvolutionListener> itr = listeners.iterator();
        if (event.geTypeEvent() == EvolutionEvent.REGIME_CHANGED) {
            while (itr.hasNext()) {
                final EvolutionListener listener = itr.next();
                listener.lgEventRegimeChanged(
                    (EvolutionEventRegime) event
                );
            }
        }
    }

}
