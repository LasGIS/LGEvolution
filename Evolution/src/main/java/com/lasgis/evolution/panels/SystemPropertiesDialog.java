/*
 * SystemPropertiesDialog.java
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2021 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.panels;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.lasgis.evolution.object.EvolutionValues.GROUND_PLAN_FACTOR;
import static com.lasgis.evolution.object.EvolutionValues.MAX_GROUND_VALUE;

/**
 * The Class SystemPropertyDialog definition.
 *
 * @author Vladimir Laskin
 * @since 13.01.2021 : 22:39
 */
public class SystemPropertiesDialog extends JDialog implements ActionListener {

    /** Максимально возможное количество земли. */
    private final JTextField maxGroundValue = new JTextField(5);
    /** коэффициент: растения забирают сок земли */
    private final JTextField groundPlanDelimiter = new JTextField(5);

    /** кнопка для закрытия. */
    private final JButton okButton = new JButton();
    /** кнопка для закрытия. */
    private final JButton cancelButton = new JButton();

    /**
     * Create and show dialog.
     *
     * @param parent parent frame
     */
    public SystemPropertiesDialog(final Frame parent) {
        super(parent);
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        this.setTitle("Системные Свойства");

        final JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        final JPanel centralPanel = new JPanel();
        centralPanel.setLayout(new GridLayout(0, 2, 5, 5));
        centralPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(centralPanel, BorderLayout.CENTER);

        centralPanel.add(new JLabel("Максимально возможное количество земли:", JLabel.RIGHT));
        maxGroundValue.setText(Double.toString(MAX_GROUND_VALUE));
        maxGroundValue.setHorizontalAlignment(JTextField.LEFT);
        maxGroundValue.setMaximumSize(new Dimension(100, 20));
        centralPanel.add(maxGroundValue);

        centralPanel.add(new JLabel("коэффициент: растения забирают сок земли:", JLabel.RIGHT));
        groundPlanDelimiter.setText(Double.toString(GROUND_PLAN_FACTOR));
        groundPlanDelimiter.setHorizontalAlignment(JTextField.LEFT);
        groundPlanDelimiter.setMaximumSize(new Dimension(100, 20));
        centralPanel.add(groundPlanDelimiter);


        //Lay out the buttons from left to right.
        final JPanel buttonPane = new JPanel();
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

        //setResizable(false);
        //setSize(220, 150);
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        if (event.getSource() == okButton) {
            MAX_GROUND_VALUE = Double.parseDouble(maxGroundValue.getText());
            GROUND_PLAN_FACTOR = Double.parseDouble(groundPlanDelimiter.getText());
            dispose();
        } else if (event.getSource() == cancelButton) {
            dispose();
        }
    }
}
