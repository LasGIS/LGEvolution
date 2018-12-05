/**
 * @(#)RegimePanel.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.panels;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Панель для выбора режима.
 * @author vlaskin
 * @version 1.0 from Oct 31, 2006 : 12:00:00 PM
 */
@Slf4j
public class RegimePanel extends JComboBox implements ActionListener {

    /**
     * Creates a <code>JComboBox</code> with a default data model.
     * The default data model is an empty list of objects.
     * Use <code>addItem</code> to add items.  By default the first item
     * in the data model becomes selected.
     *
     * @see javax.swing.DefaultComboBoxModel
     */
    public RegimePanel() {
        super(EvolutionEventRegime.REGIME_LIST);
        addActionListener(this);
    }

    /**
     * Invoked when an action occurs.
     * @param e Action Event
     */
    public void actionPerformed(final ActionEvent e) {
        final JComboBox cb = (JComboBox) e.getSource();
        EvolutionEventDispatcher.singletonLasgisEventDispatcher().dispatch(
            new EvolutionEventRegime(
                cb.getSelectedIndex(),
                (String) cb.getSelectedItem()
            )
        );
    }

    /**
     * Устанавливаем новый номер режима.
     * @param regNumber Номер нового режима
     * @return номер старого режима
     */
    public int setRegime(final int regNumber) {
        final int oldRegNumber = getSelectedIndex();
        try {
            setSelectedIndex(regNumber);
            EvolutionEventDispatcher.singletonLasgisEventDispatcher().dispatch(
                new EvolutionEventRegime(regNumber, (String) getSelectedItem())
            );
        } catch (final IllegalArgumentException ex) {
            log.error(ex.getMessage(), ex);
        }
        return oldRegNumber;
    }

    /**
     * Вернуть описание режима по номеру.
     * @param regNumber номер режима
     * @return описание режима
     */
    public static String getRegimeDescriptor(final int regNumber) {
        if ((regNumber >= 0) && (regNumber < EvolutionEventRegime.REGIME_LIST.length)) {
            return EvolutionEventRegime.REGIME_LIST[regNumber];
        } else {
            return "UNKNOWN";
        }
    }

}
