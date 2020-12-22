/*
 * MainFrame.java
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2020 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.panels;

import com.lasgis.component.StatusBar;
import com.lasgis.evolution.config.ConfigLocale;
import com.lasgis.evolution.map.Cell;
import com.lasgis.evolution.map.Matrix;
import com.lasgis.evolution.map.MatrixHelper;
import com.lasgis.util.SettingMenuItem;
import com.lasgis.util.SettingToolBarItem;
import com.lasgis.util.Util;
import lombok.extern.slf4j.Slf4j;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.lasgis.evolution.object.EvolutionConstants.CHAMOMILE_LEAF_KEY;
import static com.lasgis.evolution.object.EvolutionConstants.GRASS_PLANT_KEY;
import static com.lasgis.evolution.object.EvolutionConstants.GROUND_KEY;

/**
 * Created by IntelliJ IDEA.
 *
 * @author VLaskin
 * @version 1.0
 */
@Slf4j
public class MainFrame extends JFrame implements ComponentListener {

    private static final Color WATCH_FOR_SELECTED_ON = new Color(200, 150, 150);
    private static final Color WATCH_FOR_SELECTED_OFF = new Color(240, 240, 240);

    /** размеры строки состояния. */
    private static final int[] STATUS_BAR_SIZES = new int[] {0, 100, 200};
    /** Строка состояния. */
    private final StatusBar jStatusBar = new StatusBar(STATUS_BAR_SIZES);
    /** Панель с картой. */
    private final MapPanel mapPanel = new MapPanel();
    /** панель конфигурации. */
    private final ConfigPanel configPanel = new ConfigPanel();

    /** ширина кнопки на главной панели инструментов. */
    private static final int TOOL_BAR_WIDTH = 27;
    /** высота кнопки на главной панели инструментов. */
    private static final int TOOL_BAR_HEIGHT = 27;

    /** Настройка главного меню. */
    private final SettingMenuItem[] menuSetting = {
        new SettingMenuItem(
            "File", "openFile.gif", "", null,
            new SettingMenuItem[] {
                new SettingMenuItem(
                    "Load Matrix", "openFile.gif", "читаем матрицу", this::jMenuContextLoad, null
                ),
                new SettingMenuItem(
                    "Save Matrix", "closeFile.gif", "запоминаем матрицу", this::jMenuContextSave, null
                ),
                new SettingMenuItem(
                    "Exit", null, "Закрываем приложение", this::jMenuFileExitAction, null
                ),
            }
        ),
        new SettingMenuItem(
            "Help", "help.gif", "Всякого рода вспоможение", null,
            new SettingMenuItem[] {
                new SettingMenuItem(
                    "About", "help.gif", "Кто ЭТО сделал!", this::jMenuHelpAboutAction, null
                )
            }
        )
    };

    /** Настройка главной панели инструментов. */
    private final SettingToolBarItem[] toolBarSetting = {
        new SettingToolBarItem(
            "Помощь", "help.gif", "Help",
            TOOL_BAR_WIDTH, TOOL_BAR_HEIGHT, MainFrame.this::jMenuHelpAboutAction
        ),
        new SettingToolBarItem(
            "Load Matrix", "openFile.gif", "читаем матрицу",
            TOOL_BAR_WIDTH, TOOL_BAR_HEIGHT, MainFrame.this::jMenuContextLoad
        ),
        new SettingToolBarItem(
            "Save Matrix", "closeFile.gif", "запоминаем матрицу",
            TOOL_BAR_WIDTH, TOOL_BAR_HEIGHT, MainFrame.this::jMenuContextSave
        ),
        new SettingToolBarItem(
            "Следить", null, "Следить за выбранным животным",
            TOOL_BAR_WIDTH, TOOL_BAR_HEIGHT, MainFrame.this::jWatchForSelectedAnimal
        ),
        new SettingToolBarItem(
            "Exit", null, "Exit from programm",
            TOOL_BAR_WIDTH, TOOL_BAR_HEIGHT, MainFrame.this::jMenuFileExitAction
        )
    };

    /**
     * Construct the frame.
     */
    public MainFrame() {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            final Dimension size = new Dimension(800, 600);
            final JPanel contentPane = (JPanel) this.getContentPane();
            contentPane.setLayout(new BorderLayout());
            this.setSize(size);
            this.setTitle("Evolution");

            /* настраиваем главное меню */
            final JMenuBar menuBar = new JMenuBar();
            for (SettingMenuItem aSetMenu : menuSetting) {
                menuBar.add(Util.createImageMenuItem(aSetMenu));
            }
            /* настраиваем главный ToolBar */
            final JToolBar toolBar = new JToolBar();
            for (SettingToolBarItem aSetToolBar : toolBarSetting) {
                toolBar.add(Util.createImageButton(aSetToolBar));
            }
            toolBar.add(createShowVelocity(), toolBar.getComponentCount() - 1);
            /* разделительная панелька */
            final JSplitPane splitPane = new JSplitPane();
            splitPane.setContinuousLayout(true);
            mapPanel.setMainFrame(this);
            mapPanel.addComponentListener(this);
            //Make textField get the focus whenever frame is activated.
            this.addWindowFocusListener(new WindowAdapter() {
                public void windowGainedFocus(final WindowEvent e) {
                    mapPanel.requestFocusInWindow();
                }
            });
            this.setJMenuBar(menuBar);
            contentPane.add(toolBar, BorderLayout.NORTH);
            contentPane.add(jStatusBar, BorderLayout.SOUTH);
            contentPane.add(splitPane, BorderLayout.CENTER);
            splitPane.add(configPanel, JSplitPane.RIGHT);
            splitPane.add(mapPanel, JSplitPane.LEFT);
            splitPane.setLastDividerLocation(size.width - 250);
            splitPane.setDividerLocation(size.width - 250);
            splitPane.setResizeWeight(1);
            mapPanel.showScale();
            matrixInitialise();
        } catch (final Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    } // constructor::MainFrame()

    private JComboBox<String> createShowVelocity() {
        final JComboBox<String> showVelocity = new JComboBox<>(
            new String[] {"высокая скорость", "средняя скорость", "малая скорость", "выключить"}
        );
        showVelocity.setToolTipText(
            "Скорость отрисовки ситуации на карте (развитие идёт независимо от скорости отрисовки)."
        );

        final Dimension dim = new Dimension(120, 20);
        showVelocity.setMaximumSize(dim);
        showVelocity.setMinimumSize(dim);
        showVelocity.setPreferredSize(dim);
        showVelocity.addActionListener(e -> {
                final JComboBox cb = (JComboBox) e.getSource();
                switch (cb.getSelectedIndex()) {
                    case 0:
                        mapPanel.setSpeedDraw(100);
                        mapPanel.setAutoDraw(true);
                        break;
                    case 1:
                        mapPanel.setSpeedDraw(200);
                        mapPanel.setAutoDraw(true);
                        break;
                    case 2:
                        mapPanel.setSpeedDraw(500);
                        mapPanel.setAutoDraw(true);
                        break;
                    case 3:
                    default:
                        mapPanel.setAutoDraw(false);
                        break;
                }
            }
        );
        return showVelocity;
    }

    /**
     * Первоначальная инициализация матрицы.
     */
    private void matrixInitialise() {
        Cell cell = Matrix.getMatrix().getCell(25, 25);
        cell.element(GROUND_KEY).incValue(5000);
        cell.element(GRASS_PLANT_KEY).incValue(30);

        cell = Matrix.getMatrix().getCell(25, 50);
        cell.element(GROUND_KEY).incValue(5000);
        cell.element(CHAMOMILE_LEAF_KEY).incValue(30);
    }

    /**
     * File | Exit action performed.
     *
     * @param event Action Event
     */
    public void jMenuFileExitAction(final ActionEvent event) {
        // сохраняем локальную конфигурацию
        ConfigLocale.save();
        System.exit(0);
    }

    /**
     *
     * @param event Action Event
     */
    public void jMenuContextLoad(final ActionEvent event) {
        final FileDialog dlg = new FileDialog(
            this, "Читать Контекст из файла",
            FileDialog.LOAD
        );
        dlg.setFile("*.json");
        dlg.setVisible(true);
        if (dlg.getFile() != null) {
            try {
                MatrixHelper.matrixContextLoad(dlg.getDirectory() + dlg.getFile());
            } catch (final Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }
        mapPanel.requestFocusInWindow();
    }

    /**
     * Сохраняем матрицу .
     * @param event Action Event
     */
    public void jMenuContextSave(final ActionEvent event) {
        final FileDialog dlg = new FileDialog(
            this, "Писать Контекст в файл",
            FileDialog.SAVE
        );
        dlg.setFile("*.json");
        dlg.setVisible(true);
        if (dlg.getFile() != null) {
            try {
                final String directory = dlg.getDirectory();
                String file = dlg.getFile();
                MatrixHelper.matrixContextSave(directory + file + (file.toLowerCase().endsWith(".json") ? "" : ".json"));
            } catch (final Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }
        mapPanel.requestFocusInWindow();
    }

    /**
     * .
     * @param event Action Event
     */
    public void jWatchForSelectedAnimal(final ActionEvent event) {
        final JButton source = (JButton) event.getSource();
        if (mapPanel.invertWatchForSelected()) {
            source.setBackground(WATCH_FOR_SELECTED_ON);
        } else {
            source.setBackground(WATCH_FOR_SELECTED_OFF);
        }
        mapPanel.requestFocusInWindow();
    }

    /**
     * Help | About action performed.
     * @param event Action Event
     */
    public void jMenuHelpAboutAction(final ActionEvent event) {
        final MainFrameAboutBox dlg = new MainFrameAboutBox(this);
        final Dimension dlgSize = dlg.getPreferredSize();
        final Dimension frmSize = getSize();
        final Point loc = getLocation();
        dlg.setLocation(
            (frmSize.width - dlgSize.width) / 2 + loc.x,
            (frmSize.height - dlgSize.height) / 2 + loc.y
        );
        dlg.setModal(true);
        dlg.pack();
        dlg.setVisible(true);
    }

    /**
     * Overridden so we can exit when window is closed.
     * @param e оконное событие
     */
    protected void processWindowEvent(final WindowEvent e) {
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
    public void outStatus(final String out, final int numItem) {
        jStatusBar.setText(out, numItem);
    }

    /**
     * Вернуть панель с картой.
     * @return панель с картой
     */
    public MapPanel getMapPanel() {
        return mapPanel;
    }

    /**
     * Вернуть панель конфигурации.
     * @return панель конфигурации
     */
    public ConfigPanel getConfigPanel() {
        return configPanel;
    }

    /**
     * Invoked when the component's size changes.
     * @param e event which indicates that a component moved
     */
    public void componentResized(final ComponentEvent e) {
        if (e.getComponent().equals(mapPanel)) {
            mapPanel.setRedrawMap(true);
        }
    }

    /**
     * Invoked when the component's position changes.
     * @param e event which indicates that a component moved
     */
    public void componentMoved(final ComponentEvent e) {

    }

    /**
     * Invoked when the component has been made visible.
     * @param e event which indicates that a component moved
     */
    public void componentShown(final ComponentEvent e) {

    }

    /**
     * Invoked when the component has been made invisible.
     * @param e event which indicates that a component moved
     */
    public void componentHidden(final ComponentEvent e) {

    }
}
