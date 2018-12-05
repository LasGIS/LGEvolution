/*
 * @(#)Anime.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.animation;

import com.lasgis.animation.panels.AnimationFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Запуск окна редактирования мультиков.
 * @author vlaskin
 * @version 1.0
 * @since May 31, 2010 : 5:50:53 PM
 */
public class Anime {

    /**
     * if ... Validate panels that have preset sizes.
     */
    private boolean packFrame = false;

    /**
     * Construct the application.
     */
    private Anime() {

        AnimationFrame frame = new AnimationFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Validate panels that have preset sizes
        // Pack panels that have useful preferred size info,
        // e.g. from their layout
        if (packFrame) {
            frame.pack();
        } else {
            frame.validate();
        }
        //Center the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
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
    public static void main(String[] args) {
/*
        try {
            JFrame.setDefaultLookAndFeelDecorated(true);
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception e) {
            log.stackTrace(Anime.class, e);
        }
*/
        new Anime();
    }
}
