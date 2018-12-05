/*
 * @(#)AnimationFrame.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.animation.panels;

import com.lasgis.component.StatusBar;
import com.lasgis.util.Log;
import com.lasgis.util.SettingMenuItem;
import com.lasgis.util.SettingToolBarItem;
import com.lasgis.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

/**
 * Created by IntelliJ IDEA.
 *
 * @author VLaskin
 * @version 1.0
 */
public class AnimationFrame extends JFrame implements  ActionListener {

    /** размеры строки состояния. */
    private static final int[] STATUS_BAR_SIZES = new int[] {0, 100, 200};
    /** Строка состояния. */
    private StatusBar statusBar = new StatusBar(STATUS_BAR_SIZES);
    /** Предыдущий кадр. */
    private JButton prevButton = new JButton();
    /** Следующий кадр. */
    private JButton nextButton = new JButton();
    /** Предыдущий кадр. */
    private JButton animationButton = new JButton();
    /** Окошко для рисования анимации. */
    private AnimationView animePanel;
    /** панель конфигурации. */
    private JPanel configPanel = new JPanel();

    /** ширина кнопки на главной панели инструментов. */
    private static final int TOOL_BAR_WIDTH = 27;
    /** высота кнопки на главной панели инструментов. */
    private static final int TOOL_BAR_HEIGHT = 27;

    /** Настройка главного меню. */
    private final SettingMenuItem[] menuSetting = {
        new SettingMenuItem(
            "File", null, "", null,
            new SettingMenuItem[] {
                new SettingMenuItem(
                    "Load", "openFile.gif", "Прочитать мультик из файла",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            animePanel.load();
                        }
                    }, null
                ),
                new SettingMenuItem(
                    "Save", "closeFile.gif", "Записать мультик в файл",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            animePanel.save();
                        }
                    }, null
                ),
                new SettingMenuItem(
                    "Save as", "closeFile.gif", "Записать мультик в другой файл",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            animePanel.saveAs();
                        }
                    }, null
                ),
                new SettingMenuItem(
                    "Exit", null, "Закрываем приложение",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            System.exit(0);
                        }
                    }, null
                ),
            }
        ),
        new SettingMenuItem(
            "Фильм", null, "Управлять фильмом, используемым как подложка к мультику", null,
            new SettingMenuItem[] {
                new SettingMenuItem(
                    "Выбрать фильм", null, "Выбрать фильм как подложку к мультику",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            animePanel.attachMoves();
                        }
                    }, null
                ),
                new SettingMenuItem(
                    "Убрать текущий фильм", null, "Убрать фильм как подложку к мультику",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            animePanel.detachMoves();
                        }
                    }, null
                ),
            }
        ),
        new SettingMenuItem(
            "Help", null, "Всякого рода вспоможение", null,
            new SettingMenuItem[]{
                new SettingMenuItem("About", "help.gif", "Кто ЭТО сделал!",
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            //jMenuHelpAboutAction(e);
                        }
                    }, null
                )
            }
        )
    };

    /** Настройка главной панели инструментов. */
    private final SettingToolBarItem[] toolBarSetting = {
        new SettingToolBarItem(
            "Load", "openFile.gif", "Прочитать мультик из файла",
            TOOL_BAR_WIDTH, TOOL_BAR_HEIGHT,
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    animePanel.load();
                }
            }
        ),
        new SettingToolBarItem(
            "Save", "closeFile.gif", "Записать мультик в файл",
            TOOL_BAR_WIDTH, TOOL_BAR_HEIGHT,
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    animePanel.save();
                }
            }
        ),
        new SettingToolBarItem(
            "Save as", "closeFile.gif", "Записать мультик в другой файл",
            TOOL_BAR_WIDTH, TOOL_BAR_HEIGHT,
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    animePanel.saveAs();
                }
            }
        ),
/*
        new SettingToolBarItem(
            "Exit", null, "Exit from programm",
            TOOL_BAR_WIDTH, TOOL_BAR_HEIGHT,
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            }
        )
*/
    };

    /**
     * Construct the frame.
     */
    public AnimationFrame() {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            Dimension size = new Dimension(800, 600);
            /*com.lasgis.MainFrame.class.getResource("")*/
            //setIconImage(Toolkit.getDefaultToolkit()
            // .createImage(MainFrame.class.getResource("[Your Icon]")));
            animePanel = new AnimationView();
            animePanel.setParentFrame(this);
            JPanel contentPane = (JPanel) this.getContentPane();
            contentPane.setLayout(new BorderLayout());
            this.setSize(size);
            this.setTitle("Редакция Мультиков");

            /* набиваем панель конфигурации */
            BoxLayout layout = new BoxLayout(configPanel, BoxLayout.PAGE_AXIS);
            configPanel.setLayout(layout);
            configPanel.add(new JLabel("Время:", JLabel.LEFT));
            /* Поле для показа времени */
            JTextField showTime = new JTextField(5);
            animePanel.setShowTime(showTime);
            showTime.setHorizontalAlignment(JTextField.RIGHT);
            showTime.setMaximumSize(new Dimension(150, 20));
            //showTime.setAlignmentY(Component.CENTER_ALIGNMENT);
            configPanel.add(showTime);

            prevButton.setText("Prev");
            prevButton.addActionListener(this);
            prevButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            prevButton.setMaximumSize(new Dimension(80, 24));
            configPanel.add(prevButton);

            nextButton.setText("Next");
            nextButton.addActionListener(this);
            nextButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            nextButton.setMaximumSize(new Dimension(80, 24));
            configPanel.add(nextButton);

            animationButton.setText("Mult");
            animationButton.addActionListener(this);
            animationButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            animationButton.setMaximumSize(new Dimension(80, 24));
            configPanel.add(animationButton);

            /* настраиваем главное меню */
            JMenuBar menuBar = new JMenuBar();
            for (SettingMenuItem aSetMenu : menuSetting) {
                menuBar.add(Util.createImageMenuItem(aSetMenu));
            }
            /* настраиваем главный ToolBar */
            JToolBar toolBar = new JToolBar();
            for (SettingToolBarItem aSetToolBar : toolBarSetting) {
                toolBar.add(Util.createImageButton(aSetToolBar));
            }
            /* разделительная панелька */
            JSplitPane splitPane = new JSplitPane();
            splitPane.setToolTipText("setToolTipText");
            splitPane.setContinuousLayout(true);
            //animePanel.addComponentListener(this);

            this.setJMenuBar(menuBar);
            contentPane.add(toolBar, BorderLayout.NORTH);
            contentPane.add(statusBar, BorderLayout.SOUTH);
            contentPane.add(splitPane, BorderLayout.CENTER);
            splitPane.add(configPanel, JSplitPane.RIGHT);
            splitPane.add(animePanel, JSplitPane.LEFT);
            //splitPane.setLastDividerLocation(size.width - 70);
            //splitPane.setDividerLocation(size.width - 70);
            splitPane.setResizeWeight(1);
        } catch (Exception e) {
            Log.stackTrace(this.getClass(), e);
        }
    } // constructor::MainFrame()

    /**
     * Overridden so we can exit when window is closed.
     * @param e оконное событие
     */
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
/*
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
        }
*/
    }

    /**
     * Вывод сообщений на statusBar.
     * @param out строка сообщения
     * @param numItem номер элемента статусной строки
     */
    public void outStatus(String out, int numItem) {
        statusBar.setText(out, numItem);
    }

    /**
     * Вернуть панель с картой.
     * @return панель с картой
     */
    public AnimationView getAnimationPanel() {
        return animePanel;
    }

    /**
     * Вернуть панель конфигурации.
     * @return панель конфигурации
     */
    public JPanel getConfigPanel() {
        return configPanel;
    }

    /**
     * Close the dialog on a button event.
     * @param e ActionEvent
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == prevButton) {
            animePanel.prevTime(AnimationView.TIME_STEP_MAX);
        } else if (e.getSource() == nextButton) {
            animePanel.nextTime(AnimationView.TIME_STEP_MAX);
        } else if (e.getSource() == animationButton) {
            animePanel.startAnimation();
        }
    }

}
