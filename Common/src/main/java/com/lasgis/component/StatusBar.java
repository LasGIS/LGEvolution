/*
 * @(#)StatusBar.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.component;

import javax.swing.*;
import java.awt.*;

/**
 * Строка состояния, состоящая из нескольких разделов.
 *
 * @author vlaskin
 * @version 1.0
 * @since 18.03.2008 : 14:15:04
 */
public class StatusBar extends JComponent {

    /** размер разделителя между элементами. */
    private static final int SIZE_WEIGHT_DELIMITER = 5;

    /** размер разделителя между элементами. */
    public static final int SIZE_HEIGHT_STATUS_BAR = 24;

    /**
     * конструктор строки.
     * @param sizes число разделов
     */
    private StatusBar(final int[] sizes) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        add(new StatusBarItem(" ", 0));
        add(Box.createHorizontalGlue());
        for (int i = 1; i < sizes.length; i++) {
            add(new StatusBarItem(" ", sizes[i]));
            Box.Filler rigidArea = (Box.Filler) Box.createRigidArea(
                new Dimension(
                    SIZE_WEIGHT_DELIMITER,
                    SIZE_HEIGHT_STATUS_BAR
                )
            );
            rigidArea.setForeground(new Color(0xff, 0xff, 0xff));
            add(rigidArea);
        }
    }

    /**
     * Указать новое значение для конкретного раздела.
     * @param text значение
     * @param numItem номер элемента
     */
    public void setText(final String text, final int numItem) {
        final JLabel label = (JLabel) this.getComponent(numItem * 2);
        label.setText(StatusBarItem.stringDeNormalize(text));
    }
}