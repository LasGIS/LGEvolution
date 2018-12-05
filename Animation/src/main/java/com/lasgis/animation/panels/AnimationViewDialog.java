/*
 * @(#)AnimationViewDialog.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.animation.panels;

import com.lasgis.util.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

/**
 * Created by IntelliJ IDEA.
 * @author VLaskin
 * @version 1.0
 */
public class AnimationViewDialog extends JDialog implements ActionListener {

    /** Поле для показа времени. */
    private JTextField showTime = new JTextField(5);
    /** Предыдущий кадр. */
    private JButton prevButton = new JButton();
    /** Следующий кадр. */
    private JButton nextButton = new JButton();
    /** Предыдущий кадр. */
    private JButton animationButton = new JButton();
    /** прочитать сохраненный мультик. */
    private JButton loadAnime = new JButton();
    /** записать мультик. */
    private JButton saveAnime = new JButton();
    /** записать мультик в другой файл. */
    private JButton saveAnimeAs = new JButton();
    /** кнопка для закрытия. */
    private JButton okButton = new JButton();
    /** Окошко для рисования анимации. */
    private AnimationView animate;

    /**
     * Constructor of the Animation View Dialog.
     * @param parent parent Frame
     */
    public AnimationViewDialog(JFrame parent) {
        super(parent);
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            this.setTitle("Animation View Dialog");
            animate = new AnimationView();
            animate.setParentFrame(parent);
            animate.setShowTime(showTime);

            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.add(animate, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
            panel.add(buttonPanel, BorderLayout.EAST);

            buttonPanel.add(new JLabel("Время:", JLabel.LEFT));
            showTime.setHorizontalAlignment(JTextField.RIGHT);
            showTime.setMaximumSize(new Dimension(100, 20));
            buttonPanel.add(showTime);

            loadAnime.setText("Load");
            loadAnime.addActionListener(this);
            buttonPanel.add(loadAnime);

            saveAnime.setText("Save");
            saveAnime.addActionListener(this);
            buttonPanel.add(saveAnime);

            saveAnimeAs.setText("Save as");
            saveAnimeAs.addActionListener(this);
            buttonPanel.add(saveAnimeAs);

            prevButton.setText("Prev");
            prevButton.addActionListener(this);
            buttonPanel.add(prevButton);

            nextButton.setText("Next");
            nextButton.addActionListener(this);
            buttonPanel.add(nextButton);

            animationButton.setText("Mult");
            animationButton.addActionListener(this);
            buttonPanel.add(animationButton);

            okButton.setText("Close");
            okButton.addActionListener(this);
            buttonPanel.add(okButton);

            this.getContentPane().add(panel, null);
            setResizable(true);
            setSize(400, 400);
        } catch (Exception e) {
            Log.stackTrace(this.getClass(), e);
        }
    }

    /**
     * Overridden so we can exit when window is closed.
     * @param e WindowEvent
     */
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            cancel();
        }
        super.processWindowEvent(e);
    }

    /**
     * Close the dialog.
     */
    void cancel() {
        dispose();
    }

    /**
     * Close the dialog on a button event.
     * @param e ActionEvent
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == prevButton) {
            animate.prevTime(AnimationView.TIME_STEP_MAX);
        } else if (e.getSource() == nextButton) {
            animate.nextTime(AnimationView.TIME_STEP_MAX);
        } else if (e.getSource() == animationButton) {
            animate.startAnimation();
        } else if (e.getSource() == loadAnime) {
            animate.load();
        } else if (e.getSource() == saveAnime) {
            animate.save();
        } else if (e.getSource() == saveAnimeAs) {
            animate.saveAs();
        } else if (e.getSource() == okButton) {
            cancel();
        }
    }
}