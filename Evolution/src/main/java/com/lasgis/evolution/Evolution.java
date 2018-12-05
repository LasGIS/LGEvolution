/**
 * @(#)Evolution.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright © 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution;

import com.lasgis.evolution.panels.MainFrame;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author vlaskin
 * @version 1.0
 * @since May 31, 2010 : 5:50:53 PM
 */
@Slf4j
public final class Evolution {

    /**
     * if ... Validate panels that have preset sizes.
     */
    private boolean packFrame = false;

    /**
     * Construct the application.
     */
    private Evolution() {

        final MainFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Validate panels that have preset sizes
        // Pack panels that have useful preferred size info,
        // e.g. from their layout
        if (packFrame) {
            frame.pack();
        } else {
            frame.validate();
        }
        //Center the window
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        frame.setLocation(
            (screenSize.width - frameSize.width) / 2,
            (screenSize.height - frameSize.height) / 2
        );
        frame.setVisible(true);
    }

    /**
     * Главный запуск программы.
     * @param args аргументы командной строки
     */
    public static void main(final String[] args) {

        try {
            JFrame.setDefaultLookAndFeelDecorated(true);
            UIManager.setLookAndFeel(
                 UIManager.getSystemLookAndFeelClassName()
            );
        } catch (final Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        new Evolution();
    }
}
