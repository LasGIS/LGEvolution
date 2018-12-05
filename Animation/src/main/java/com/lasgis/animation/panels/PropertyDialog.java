/**
 * @(#)PropertyDialog.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2014 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.animation.panels;

import com.lasgis.animation.map.object.AniTimeAbstractLine;
import com.lasgis.animation.map.object.AniTimeLine;
import com.lasgis.animation.map.object.AniTimePolygon;
import com.lasgis.util.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The Class PropertyDialog.
 * @author Vladimir Laskin
 * @version 1.0
 * @since 12.04.2011 : 18:09:08
 */
public class PropertyDialog extends JDialog implements ActionListener {

    /** Поле для показа цвета линии. */
    private JTextField lineColorField = new JTextField(5);
    /** Поле для показа цвета заливки. */
    private JTextField fillColorField = new JTextField(5);
    /** Поле для показа толщины линии. */
    private JTextField thickField = new JTextField(5);

    /** кнопка для закрытия. */
    private JButton okButton = new JButton();
    /** кнопка для закрытия. */
    private JButton cancelButton = new JButton();

    /** Цвет линии. */
    private Color lineColor;

    /** Толщина линии. */
    private int thick;

    /** Цвет заливки. */
    private Color fillColor;

    /**
     *
     * @param aniTime AniTimeAbstractLine
     */
    public PropertyDialog(AniTimeAbstractLine aniTime) {
        super();
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            if (aniTime instanceof AniTimeLine) {
                this.setTitle("Свойства Линии");
                AniTimeLine line = (AniTimeLine) aniTime;
                lineColor = line.getLineColor();
                thick = line.getThick();
            } else if (aniTime instanceof AniTimePolygon) {
                this.setTitle("Свойства Полигона");
                AniTimePolygon polygon = (AniTimePolygon) aniTime;
                lineColor = polygon.getLineColor();
                thick = polygon.getThick();
                fillColor = polygon.getFillColor();
            }

            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());

            JPanel centralPanel = new JPanel();
            centralPanel.setLayout(new GridLayout(0, 2, 5, 5));
            centralPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            panel.add(centralPanel, BorderLayout.CENTER);

            centralPanel.add(new JLabel("Цвет линии:", JLabel.RIGHT));
            lineColorField.setHorizontalAlignment(JTextField.LEFT);
            lineColorField.setMaximumSize(new Dimension(100, 20));
            centralPanel.add(lineColorField);

            centralPanel.add(new JLabel("Толщина линии:", JLabel.RIGHT));
            thickField.setHorizontalAlignment(JTextField.LEFT);
            thickField.setMaximumSize(new Dimension(100, 20));
            centralPanel.add(thickField);

            centralPanel.add(new JLabel("Цвет заливки:", JLabel.RIGHT));
            fillColorField.setHorizontalAlignment(JTextField.LEFT);
            fillColorField.setMaximumSize(new Dimension(100, 20));
            centralPanel.add(fillColorField);

            //Lay out the buttons from left to right.
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
            buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
            buttonPane.add(Box.createHorizontalGlue());
            panel.add(buttonPane, BorderLayout.SOUTH);

            cancelButton.setText("Закрыть");
            cancelButton.addActionListener(this);
            cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            buttonPane.add(cancelButton);

            buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));

            okButton.setText("Сохранить");
            okButton.addActionListener(this);
            okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            buttonPane.add(okButton);

            getContentPane().add(panel, null);

            setResizable(false);
            setSize(220, 150);
            setLocation(MouseInfo.getPointerInfo().getLocation());
        } catch (Exception e) {
            Log.stackTrace(this.getClass(), e);
        }
    }

    /**
     *
     * @param event {@link ActionEvent}
     */
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == lineColorField) {
            dispose();
        } else if (event.getSource() == okButton) {
            dispose();
        } else if (event.getSource() == cancelButton) {
            dispose();
        }
    }
}
