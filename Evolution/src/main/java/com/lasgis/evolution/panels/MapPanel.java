/**
 * @(#)MapPanel.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.panels;

import com.lasgis.evolution.config.ConfigLocale;
import com.lasgis.evolution.map.Cell;
import com.lasgis.evolution.map.CellHelper;
import com.lasgis.evolution.map.Matrix;
import com.lasgis.evolution.map.element.Parameters;
import com.lasgis.evolution.object.AnimalBehaviour;
import com.lasgis.evolution.object.AnimalManagerBehaviour;
import com.lasgis.evolution.object.LiveObjectElement;
import com.lasgis.evolution.object.LiveObjectManager;
import com.lasgis.evolution.object.PlantBehaviour;
import com.lasgis.evolution.statistic.StatThread;
import com.lasgis.util.SettingMenuItem;
import com.lasgis.util.Util;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.lasgis.evolution.panels.EvolutionEventRegime.APPEND_ENTITY;
import static com.lasgis.evolution.panels.EvolutionEventRegime.NAVIGATION;

/**
 * Окно вывода карты.
 * @author VLaskin
 * @version 1.0
 */
@Slf4j
public class MapPanel extends JPanel
    implements MouseMotionListener, KeyListener, FocusListener, MouseListener, MouseWheelListener, Scalable,
        EvolutionListener {

    /** серый цвет фона. */
    public static final Color PANEL_GRAY_COLOR = new Color(220, 220, 220);
    /** цвет фона для выбранной панели. */
    public static final Color PANEL_FOCUSED_COLOR = new Color(255, 220, 220);
    /** светло-серый цвет фона. */
    public static final Color PANEL_LIGHT_GRAY_COLOR = new Color(240, 240, 240);
    /** Размер зарамочного оформления. */
    private static final int SIZE_BORDER = 15;
    /** ссылка на MainFrame. */
    private MainFrame mainFrame = null;

    /** настройка приложения. */
    private ConfigLocale local = ConfigLocale.getLocale();
    /** Если true, то будем перегружать. */
    private boolean isRedrawMap = true;
    /** сохраненное изображение. */
    private BufferedImage grBackgroundImage = null;
    /** признак, что панель в фокусе. */
    private boolean focused = false;
    /** Текущая мышковая координата X для показа в инфо. */
    int currentXScreen;
    /** Текущая мышковая координата Y для показа в инфо. */
    int currentYScreen;
    /** если true, то показываем всю широту. */
    private boolean isFullLatitude;
    /** если true, то показываем всю долготу. */
    private boolean isFullLongitude;
    /** Пойманные животные для показа в инфо. */
    private final List<AnimalBehaviour> animals = new ArrayList<>();
    /** Следить за выбранным животным или нет?. */
    private boolean isWatchForSelected = false;
    /** скорость отрисовки. */
    long speedDraw = 100;
    /** Если false, то принципиально не перерисовываем. */
    AtomicBoolean isAutoDraw = new AtomicBoolean(true);

    /** . */
    Thread drawRun;

    /** Настройка выпадающего меню (пока только заготовка). */
    private SettingMenuItem[] setPopUpMenu = {
        new SettingMenuItem(
            "Добавить животное", "pig.gif", "Добавить выделенное животное в данное место",
            MapPanel.this::addElement, null
        ),
/*        new SettingMenuItem(
                "File", "openFile.gif", "", null,
            new SettingMenuItem[] {
                new SettingMenuItem(
                    "Open", "openFile.gif", "Открываем приложение", null, null
                ),
                new SettingMenuItem(
                    "Exit", "closeFile.gif", "Закрываем приложение",
                    null, null
                )
            }
        )*/
    };

    private void addElement(final ActionEvent event) {
        final Point point = PopupListener.mousePoint;
        final double latitude = toMapLatitude(point.x, point.y);
        final double longitude = toMapLongitude(point.x, point.y);
        final Cell cell = CellHelper.getCell(latitude, longitude);
        addOrRemoveElement(true, latitude, longitude, cell, 1);
    }

    /**
     * только заготовка.
     * @param e ActionEvent
    public void jMenuSetUnVisible_action(ActionEvent e) {

    }
     */

    /**
     * Конструктор.
     */
    public MapPanel() {
        try {
            setBackground(Color.white);
            setBorder(BorderFactory.createLoweredBevelBorder());
            setLayout(new BorderLayout());
            addMouseMotionListener(this);
            addMouseListener(this);
            addMouseWheelListener(this);
            addKeyListener(this);
            addFocusListener(this);
            setFocusable(true);
            EvolutionEventDispatcher.singletonLasgisEventDispatcher().add(this);
            createPopupMenu();
            drawRun = new Thread(new DrawRun(this));
            drawRun.start();
            final Thread statRun = new Thread(new StatThread());
            statRun.start();
        } catch (final Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    /**
     * стандартный вход для рисования.
     * @param gr контекст вывода
     */
    public void paint(final Graphics gr) {
        final Dimension dim = getSize();
        if (focused) {
            gr.setColor(MapPanel.PANEL_FOCUSED_COLOR);
        } else {
            gr.setColor(MapPanel.PANEL_GRAY_COLOR);
        }
        gr.fillRect(0, 0, dim.width, dim.height);
        gr.draw3DRect(
            SIZE_BORDER,
            SIZE_BORDER,
            dim.width - (SIZE_BORDER * 2) - 1,
            dim.height - (SIZE_BORDER * 2) - 1,
            false
        );
        gr.setColor(MapPanel.PANEL_GRAY_COLOR);
        gr.fillRect(
            SIZE_BORDER + 1,
            SIZE_BORDER + 1,
            dim.width - (SIZE_BORDER * 2) - 2,
            dim.height - (SIZE_BORDER * 2) - 2
        );
        gr.clipRect(
            SIZE_BORDER + 1,
            SIZE_BORDER + 1,
            dim.width - (SIZE_BORDER * 2) - 2,
            dim.height - (SIZE_BORDER * 2) - 2
        );
        if (isRedrawMap || grBackgroundImage == null) {
            grBackgroundImage = new BufferedImage(
                dim.width, dim.height, BufferedImage.TYPE_INT_RGB
            );
            LiveObjectManager.drawPlants(grBackgroundImage.getGraphics(), this);
            LiveObjectManager.drawAnimals(grBackgroundImage.getGraphics(), this);
            isRedrawMap = false;
        }
        gr.drawImage(grBackgroundImage, 0, 0, dim.width, dim.height, null);
        //drawing.grid();
    }

    /**
     * Class for draw map every 0.5 sec.
     */
    class DrawRun implements Runnable {
        private MapPanel panel;

        DrawRun(final MapPanel panel) {
            this.panel = panel;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    if (isAutoDraw.get()) {
                        panel.isRedrawMap = true;
                        panel.correctCoordinate();
                        panel.repaint();
                        panel.showCellInfo();
                    }
                    // скорость отрисовки
                    Thread.sleep(speedDraw);
                }
            } catch (final InterruptedException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
    }

    /**
     * Invert value of the isWatchForSelected attribute.
     * @return isWatchForSelected value
     */
    public boolean invertWatchForSelected() {
        isWatchForSelected ^= true;
        return isWatchForSelected;
    }

    /**
     * Корректируем координаты по выбранному животному.
     */
    private void correctCoordinate() {
        if (isWatchForSelected && animals.size() > 0) {
            final AnimalBehaviour animal = animals.get(0);
            final double latitude = animal.getLatitude();
            final double longitude = animal.getLongitude();
            final Dimension dim = getSize();
            local.setLatitude(
                latitude + local.getDelta() * dim.height / 2
            );
            local.setLongitude(
                local.getDelta() * dim.width / 2 - longitude
            );
            coordinateValidate();
            showCoordinates(currentXScreen, currentYScreen);
        }
    }

    /**
     * Вернуть экранную координату X.
     * @param latit широта
     * @param longit долгота
     * @return координата X на экране
     */
    public int toScreenX(final double latit, final double longit) {
        final double x = (local.getLongitude() + longit + local.getDelta() / 2) / local.getDelta();
        final double maxLon = Parameters.MAX_LONGITUDE / local.getDelta();
        final double cellSize = Matrix.CELL_SIZE / local.getDelta();
        if (isFullLongitude) {
            return (int) x;
        } else if (x < -cellSize && x + maxLon < getWidth() + cellSize) {
            return (int) (x + maxLon);
        } else {
            return (int) x;
        }
    }

    /**
     * Вернуть экранную координату Y.
     * @param latit широта
     * @param longit долгота
     * @return координата Y на экране
     */
    public int toScreenY(final double latit, final double longit) {
        final double y = (local.getLatitude() - latit + local.getDelta() / 2) / local.getDelta();
        final double maxLat = Parameters.MAX_LATITUDE / local.getDelta();
        final double cellSize = Matrix.CELL_SIZE / local.getDelta();
        if (isFullLatitude) {
            return (int) y;
        } else if (y < -cellSize && y + maxLat < getHeight() + cellSize) {
            return (int) (y + maxLat);
        } else {
            return (int) y;
        }
    }

    /**
     * Вернуть широту по экранным координатам.
     * @param x координата X на экране (вниз)
     * @param y координата Y на экране (влево)
     * @return широта
     */
    public double toMapLatitude(final int x, final int y) {
        return local.getLatitude() - local.getDelta() * y;
    }

    /**
     * Вернуть долготу по экранным координатам.
     * @param x координата X на экране (вниз)
     * @param y координата Y на экране (влево)
     * @return долгота
     */
    public double toMapLongitude(final int x, final int y) {
        return local.getDelta() * x - local.getLongitude();
    }

    /**
     * Вернуть текущий уровень карты (от 1 до 9
     * в соответствии со спецификацией, описанной в {@link ScaleManager}).
     * @return текущий уровень карты
     * @see ScaleManager
     */
    public int getLevel() {
        return ScaleManager.getScaleManager().getLevel();
    }

    @Override
    public int getCellSize() {
        return (int) (Matrix.CELL_SIZE / local.getDelta());
    }

    @Override
    public boolean isInto(final double latitude, final double longitude) {
        final double north = getNorth();
        final double south = getSouth();
        final double west = getWest();
        final double east = getEast();
        boolean isIntoLat;
        if (!isFullLatitude && south < 0 ) {
            isIntoLat = (north >= latitude && latitude >= 0)
                || (south + Parameters.MAX_LATITUDE <= latitude && latitude <= Parameters.MAX_LATITUDE);
        } else {
            isIntoLat = north >= latitude && south <= latitude;
        }
        boolean isIntoLng;
        if (!isFullLongitude && east > Parameters.MAX_LONGITUDE) {
            isIntoLng = (west <= longitude && longitude <= Parameters.MAX_LONGITUDE)
                || (east - Parameters.MAX_LONGITUDE >= longitude && longitude >= 0);
        } else {
            isIntoLng = east >= longitude && west <= longitude;
        }
        return isIntoLat && isIntoLng;
    }

    /**
     * Print some message for out in status box.
     * @param out string for message
     * @param numItem номер элемента статусной строки
     */
    private void outStatus(final String out, final int numItem) {
        if (mainFrame != null) {
            mainFrame.outStatus(out, numItem);
        }
    } // outStatus(String)

    /**
     * Установить добавить ссылку на главное окно.
     * @param mainFrame главное окно
     */
    public void setMainFrame(final MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    /**
     * Invoked when a key has been typed.
     * @param e An event which indicates that a keystroke occurred in
     * a component.
     */
    public void keyTyped(final KeyEvent e) {
    }

    /**
     * Invoked when a key has been pressed.
     * @param e An event which indicates that a keystroke occurred in
     * a component.
     */
    public void keyPressed(final KeyEvent e) {
        final Dimension dim = this.getBounds().getSize();
        double multi;
        if (e.isControlDown()) {
            multi = 0.3;
        } else {
            multi = 0.1;
        }
        switch (e.getKeyCode()) {
            case KeyEvent.VK_NUMPAD4 :
            case KeyEvent.VK_LEFT :
                // влево
                local.setLongitude(
                    local.getLongitude() + local.getDelta() * dim.width * multi
                );
                isRedrawMap = true;
                break;
            case KeyEvent.VK_NUMPAD6:
            case KeyEvent.VK_RIGHT:
                // вправо
                local.setLongitude(
                    local.getLongitude() - local.getDelta() * dim.width * multi
                );
                isRedrawMap = true;
                break;
            case KeyEvent.VK_NUMPAD8:
            case KeyEvent.VK_UP:
                // вверх
                local.setLatitude(
                    local.getLatitude() + local.getDelta() * dim.height * multi
                );
                isRedrawMap = true;
                break;
            case KeyEvent.VK_NUMPAD2:
            case KeyEvent.VK_DOWN:
                // вниз
                local.setLatitude(
                    local.getLatitude() - local.getDelta() * dim.height * multi
                );
                isRedrawMap = true;
                break;
            case KeyEvent.VK_DIVIDE:
                // уменьшить масштаб
                changeScale(false);
                isRedrawMap = true;
                break;
            case KeyEvent.VK_MULTIPLY:
                // увеличить масштаб
                changeScale(true);
                isRedrawMap = true;
                break;
            default:
                break;
        }
        if (isRedrawMap) {
            coordinateValidate();
            showCoordinates(dim.width / 2, dim.height / 2);
            this.repaint();
        }
    }

    private void changeScale(final boolean isIncrease) {
        double newDelta;
        final Rectangle rect = this.getBounds();
        final ScaleManager scale = ScaleManager.getScaleManager();
        if (isIncrease) {
            newDelta = scale.increaseScale();
        } else {
            newDelta = scale.decreaseScale();
        }
        local.setLongitude(
            local.getLongitude() + (newDelta - local.getDelta()) * rect.getCenterX()
        );
        local.setLatitude(
            local.getLatitude() + (newDelta - local.getDelta()) * rect.getCenterY()
        );
        local.setDelta(newDelta);
        showScale();

    }

    /**
     * Invoked when a key has been released.
     * @param e An event which indicates that a keystroke occurred in
     * a component.
     */
    public void keyReleased(final KeyEvent e) {
    }

    /**
     * Invoked when the mouse wheel is rotated.
     * @see MouseWheelEvent
     * @param e MouseWheelEvent
     */
    public void mouseWheelMoved(final MouseWheelEvent e) {
        final Dimension dim = this.getBounds().getSize();
        //final int modif = e.getModifiersEx();
        final int rotation = e.getWheelRotation();
        Point mouspnt = e.getPoint();

        if (local.getRegime() == APPEND_ENTITY && (e.isAltGraphDown() || e.isAltDown())) {
            final double latitude = toMapLatitude(mouspnt.x, mouspnt.y);
            final double longitude = toMapLongitude(mouspnt.x, mouspnt.y);
            final Cell cell = CellHelper.getCell(latitude, longitude);
            addOrRemoveElement(rotation < 0, latitude, longitude, cell, e.isControlDown() ? 10 : 1);
        } else if (isWatchForSelected || e.isControlDown()) {
            double newDelta;
            if (isWatchForSelected) {
                mouspnt = new Point(dim.width / 2, dim.height / 2);
            }
            final ScaleManager scale = ScaleManager.getScaleManager();
            if (rotation > 0) {
                newDelta = scale.increaseScale();
            } else {
                newDelta = scale.decreaseScale();
            }
            local.setLongitude(
                local.getLongitude() + (newDelta - local.getDelta()) * mouspnt.x
            );
            local.setLatitude(
                local.getLatitude() + (newDelta - local.getDelta()) * mouspnt.y
            );
            local.setDelta(newDelta);
            showScale();
        } else if (e.isShiftDown()) {
            local.setLongitude(
                local.getLongitude() - rotation * local.getDelta() * dim.width / 10.
            );
        } else {
            local.setLatitude(
                local.getLatitude()
                - rotation * local.getDelta() * dim.height / 10.
            );
        }
        isRedrawMap = true;
        coordinateValidate();
        showCoordinates(mouspnt.x, mouspnt.y);
        this.repaint();
    }

    /**
     * Поправляем координаты верхнего левого угла.
     */
    private void coordinateValidate() {

        // поправляем широту
        final double latitDim = getHeight() * local.getDelta();
        final double latitLoc = local.getLatitude();
        isFullLatitude = false;
        if (latitDim > Parameters.MAX_LATITUDE) {
            local.setLatitude((latitDim + Parameters.MAX_LATITUDE) / 2);
            isFullLatitude = true;
        } else if (latitLoc > Parameters.MAX_LATITUDE) {
            local.setLatitude(latitLoc - Parameters.MAX_LATITUDE);
        } else if (latitLoc < Parameters.MIN_LATITUDE) {
            local.setLatitude(latitLoc + Parameters.MAX_LATITUDE);
        }

        // поправляем долготу
        isFullLongitude = false;
        final double longtDim = getWidth() * local.getDelta();
        final double longtLoc = local.getLongitude();
        if (longtDim > Parameters.MAX_LONGITUDE) {
            local.setLongitude((longtDim - Parameters.MAX_LONGITUDE) / 2);
            isFullLongitude = true;
        } else if (longtLoc > Parameters.MIN_LONGITUDE) {
            local.setLongitude(longtLoc - Parameters.MAX_LONGITUDE);
        } else if (longtLoc < -Parameters.MAX_LONGITUDE) {
            local.setLongitude(longtLoc + Parameters.MAX_LONGITUDE);
        }
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
        showCoordinates(e.getX(), e.getY());
    }

    /**
     * Выводим координаты в StatusString.
     * @param xScreen экранная координата X
     * @param yScreen экранная координата Y
     */
    public void showCoordinates(final int xScreen, final int yScreen) {
        // save current coordinates
        currentXScreen = xScreen;
        currentYScreen = yScreen;
        final StringBuilder sb = new StringBuilder();
        double latit = (local.getLatitude() - local.getDelta() * yScreen);
        if (latit > Parameters.MAX_LATITUDE) {
            latit -= Parameters.MAX_LATITUDE;
        } else if (latit < Parameters.MIN_LATITUDE) {
            latit += Parameters.MAX_LATITUDE;
        }
        double longt = (local.getDelta() * xScreen - local.getLongitude());
        if (longt > Parameters.MAX_LONGITUDE) {
            longt -= Parameters.MAX_LONGITUDE;
        } else if (longt < Parameters.MIN_LONGITUDE) {
            longt += Parameters.MAX_LONGITUDE;
        }
        sb.append(getGradeString(latit)).append("  ");
        sb.append(getGradeString(longt));
        outStatus(sb.toString(), 2);
        showCellInfo(xScreen, yScreen);
    }

    /**
     * Выводим Масштаб карты.
     */
    public void showScale() {
        final double scale = local.getDelta() / ScaleManager.PIXEL_IN_METR;
        outStatus(String.format("M1:%1$,.0f", scale), 1);
    }

    /**
     * Выводим содержимое ячейки в Info Panel.
     * @param xScreen экранная координата X
     * @param yScreen экранная координата Y
     */
    public void showCellInfo(final int xScreen, final int yScreen) {
        if (mainFrame != null && mainFrame.getConfigPanel() != null) {
            final JTextArea plantInfo = mainFrame.getConfigPanel().getPlantInfo();
            final double lat = toMapLatitude(xScreen, yScreen);
            final double lon = toMapLongitude(xScreen, yScreen);
            final int indX = CellHelper.getSignDegree(lat, true);
            final int indY = CellHelper.getSignDegree(lon, false);
            final Cell cell = CellHelper.getCell(lat, lon);
            final StringBuilder sbp = new StringBuilder();
            sbp.append("X = ").append(indX).append("; Y = ").append(indY).append("; ");
            if (cell != null) {
                sbp.append("животных = ").append(cell.getAnimals().size()).append(";\n");
                cell.elements(sbp);
            } else {
                sbp.append("\nВне зоны видимости\n");
            }
            plantInfo.setText(sbp.toString());

            final StringBuilder sba = new StringBuilder();
            final JTextArea animalInfo = mainFrame.getConfigPanel().getAnimalInfo();
            synchronized (animals) {
                for (AnimalBehaviour animal : animals) {
                    animal.getInfo(sba);
                }
            }
            animalInfo.setText(sba.toString());
        }
    }

    /**
     * Выводим содержимое ячейки в Info Panel для текущей позиции мышки.
     */
    public void showCellInfo() {
        showCellInfo(currentXScreen, currentYScreen);
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     * @param e MouseEvent
     */
    public void mousePressed(final MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            boolean isRedraw = false;
            final Rectangle rec = this.getBounds();
            final Point mouspnt = e.getPoint();

            if ((mouspnt.x < rec.x + SIZE_BORDER)
                || (mouspnt.x > rec.x + rec.width - SIZE_BORDER)
                || (mouspnt.y < rec.y + SIZE_BORDER)
                || (mouspnt.y > rec.y + rec.height - SIZE_BORDER)
            ) {
                // Попали в обрамляющее поле. Переходим по стрелке
                final Point cntrpnt = new Point(
                    (rec.x + rec.width) / 2,
                    (rec.y + rec.height) / 2
                );
                //outStatus("Move v");
                local.setLongitude(local.getLongitude() + local.getDelta() * (cntrpnt.x - mouspnt.x) / 2);
                local.setLatitude(local.getLatitude() + local.getDelta() * (cntrpnt.y - mouspnt.y) / 2);
                requestFocusInWindow();
                isRedrawMap = true;
                isRedraw = true;
            } else {
                // Попали в поле карты
                final double latitude = toMapLatitude(mouspnt.x, mouspnt.y);
                final double longitude = toMapLongitude(mouspnt.x, mouspnt.y);
                final Cell cell = CellHelper.getCell(latitude, longitude);

                switch (local.getRegime()) {
                    case NAVIGATION:
                        synchronized (animals) {
                            for (AnimalBehaviour animal : animals) {
                                animal.setSelected(false);
                            }
                            animals.clear();
                            for (AnimalBehaviour animal : cell.getAnimals()) {
                                if (animal.getManager().isShow()) {
                                    animals.add(animal);
                                    animal.setSelected(true);
                                    requestFocusInWindow();
                                }
                            }
                        }
                        break;
                    case APPEND_ENTITY:
                        if (cell != null) {
                            addOrRemoveElement(!e.isShiftDown(), latitude, longitude, cell, e.isAltGraphDown() ? 5 : 1);
                            requestFocusInWindow();
                        }
                        break;
                    default:
                        break;
                }
            }
            if (isRedraw) {
                coordinateValidate();
                showCoordinates(mouspnt.x, mouspnt.y);
                repaint();
            }
        }
    }

    /**
     * Добавляем или удаляем выбранный элемент на карте.
     * @param isGrowing true if добавляем
     * @param latitude широта
     * @param longitude долгота
     * @param cell ячейка
     * @param count количество добавляемых элементов
     */
    private void addOrRemoveElement(
        final boolean isGrowing, final double latitude, final double longitude, final Cell cell, final int count
    ) {
        if (isGrowing) {
            final List<LiveObjectElement> elements =
                mainFrame.getConfigPanel().getSelectedElements();
            for (LiveObjectElement element : elements) {
                if (element instanceof PlantBehaviour) {
                    final PlantBehaviour plant = (PlantBehaviour) element;
                    cell.element(plant.getName()).incValue(count);
                } else if (element instanceof AnimalManagerBehaviour) {
                    final AnimalManagerBehaviour animal = (AnimalManagerBehaviour) element;
                    for (int i = 0; i < count; i++) {
                        animal.createNewAnimal(latitude, longitude);
                    }
                } else if (element != null) {
                    cell.element(element.getName()).incValue(count);
                }
            }
        } else {
            final List<LiveObjectElement> elements = mainFrame.getConfigPanel().getSelectedElements();
            for (LiveObjectElement element : elements) {
                if (element instanceof PlantBehaviour) {
                    final PlantBehaviour plant = (PlantBehaviour) element;
                    cell.element(plant.getName()).decValue(count);
                } else if (element instanceof AnimalManagerBehaviour) {
                    final AnimalManagerBehaviour manager = (AnimalManagerBehaviour) element;
                    int i = 0;
                    for (AnimalBehaviour animal : cell.getAnimals()) {
                        if (manager.equals(animal.getManager()) && manager.isShow()) {
                            manager.killAnimal(animal.getUniqueName());
                            if (i < count) {
                                i++;
                            } else {
                                break;
                            }
                        }
                    }
                } else if (element != null) {
                    cell.element(element.getName()).decValue(count);
                }
            }
        }
    }

    /**
     * Invoked when a mouse button has been released on a component.
     * @param e MouseEvent
     */
    public void mouseReleased(final MouseEvent e) {
//        outStatus(e.paramString());
    }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     * @param e MouseEvent
     */
    public void mouseClicked(final MouseEvent e) {
//        outStatus(e.paramString());
    }

    /**
     * Invoked when the mouse enters a component.
     * @param e MouseEvent
     */
    public void mouseEntered(final MouseEvent e) {
//        outStatus(e.paramString());
    }

    /**
     * Invoked when the mouse exits a component.
     * @param e MouseEvent
     */
    public void mouseExited(final MouseEvent e) {
//        outStatus(e.paramString());
    }

    /**
     * создаём и настраиваем выпадающее меню.
     */
    public void createPopupMenu() {

        // создаём и настраиваем выпадающее меню
        final JPopupMenu popup = new JPopupMenu();
        for (SettingMenuItem aSetMenu : setPopUpMenu) {
            popup.add(Util.createImageMenuItem(aSetMenu));
        }

        //Add listener to the text area so the popup menu can come up.
        final MouseListener popupListener = new PopupListener(popup);
        this.addMouseListener(popupListener);
    }

    /**
     * Вернуть северную широту интервала.
     * @return северная широта
     */
    public double getNorth() {
        return local.getLatitude();
    }

    /**
     * Вернуть южную широту интервала.
     * @return южная широта
     */
    public double getSouth() {
        return local.getLatitude() - getHeight() * local.getDelta();
    }

    /**
     * Вернуть западную долготу интервала.
     * @return западная долгота
     */
    public double getWest() {
        return -local.getLongitude();
    }

    /**
     * Вернуть восточную долготу интервала.
     * @return восточная долгота
     */
    public double getEast() {
        return getWidth() * local.getDelta() - local.getLongitude();
    }

    /**
     * выдать строку с показом градусов.
     * @param coord координата в метрах местности
     * @return строка с индексом координаты
     */
    public static String getGradeString(final double coord) {
        final int grad = (int) Math.floor(coord / Matrix.CELL_SIZE);
        return String.format("%1$03d", grad);
    }

    /**
     * Invoked when a component gains the keyboard focus.
     * @param e A low-level event
     */
    public void focusGained(final FocusEvent e) {
        focused = true;
        final boolean oldRedrawMap = isRedrawMap;
        isRedrawMap = false;
        repaint();
        isRedrawMap = oldRedrawMap;
    }

    /**
     * Invoked when a component loses the keyboard focus.
     * @param e A low-level event
     */
    public void focusLost(final FocusEvent e) {
        focused = false;
        final boolean oldRedrawMap = isRedrawMap;
        isRedrawMap = false;
        repaint();
        isRedrawMap = oldRedrawMap;
    }

    /**
     * Устанавливаем признак перерисовки.
     * @param redrawMap if true, then redraw component
     */
    public void setRedrawMap(final boolean redrawMap) {
        this.isRedrawMap = redrawMap;
    }

    public void setSpeedDraw(final long speedDraw) {
        this.speedDraw = speedDraw;
    }

    /**
     * Устанавливаем autoDraw.
     * @param autoDraw autoDraw
     */
    public void setAutoDraw(final boolean autoDraw) {
        isAutoDraw.getAndSet(autoDraw);
    }

    /**
     * Обработка события при смене режима.
     * @param event новый режим
     */
    public void lgEventRegimeChanged(final EvolutionEventRegime event) {
        local.setRegime(event.getNumber());
    }

}