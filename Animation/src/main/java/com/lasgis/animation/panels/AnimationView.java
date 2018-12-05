/*
 * @(#)AnimationView.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2013 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.animation.panels;

import com.lasgis.animation.AnimationXMLHelper;
import com.lasgis.animation.map.Animation;
import com.lasgis.animation.map.object.AniPoint;
import com.lasgis.animation.map.object.AniTimeAbstractLine;
import com.lasgis.animation.map.object.AniTimeLine;
import com.lasgis.animation.map.object.AniTimePoint;
import com.lasgis.animation.map.object.AniTimePolygon;
import com.lasgis.animation.map.object.CapturedObject;
import com.lasgis.util.Log;
import com.lasgis.util.SettingMenuItem;
import com.lasgis.util.Util;

import javax.media.Buffer;
import javax.media.CannotRealizeException;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.Time;
import javax.media.control.FrameGrabbingControl;
import javax.media.format.VideoFormat;
import javax.media.util.BufferToImage;
import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Панель для просмотра и редактирования анимации.
 * @author Vladimir Laskin
 * @version 1.0
 * @since 27.01.2011 : 18:35:01
 */
public class AnimationView extends JPanel implements ActionListener, PopupMenuListener {

    /** выпадающее меню. */
    private JPopupMenu popUpMenu = new JPopupMenu();
    /** Вызывающий Frame. */
    private JFrame parentFrame = null;
    /** поле для показа времени. */
    private JTextField showTime = null;
    /**  */
    private NumberFormat nf = new DecimalFormat(
        "###0.00", new DecimalFormatSymbols(Locale.ENGLISH)
    );
    /** Набор объектов анимации. */
    private Animation anime = new Animation();
    /** размер одного градуса в радианах. */
    private static final double ONE_GRAD = Math.PI / 180;
    /** текущее время. */
    private double curTime = 0;
    /** Максимально возможное время. */
    private static final double MAXIMAL_TIME = 100.0;
    /** Минимально возможное время. */
    private static final double MINIMAL_TIME = 0.0;

    /** захваченная точка, мировая линия и полигон. */
    private CapturedObject captured = null;
    /** признак, что окно находится в режиме показа мультика. */
    private boolean isShow = false;
    /** таймер, который запускает мультик. */
    private Timer timer;
    /**  */
    private int mouseButton = MouseEvent.NOBUTTON;

    /** тип рисования по времени - как есть. */
    private static final int TYPE_DRAW_ASIS = 0;
    /** тип рисования по времени - ближайшее.  */
    private static final int TYPE_DRAW_SHORT = 1;
    /** тип рисования по времени - недалёкое. */
    private static final int TYPE_DRAW_NEAR = 2;
    /** тип рисования по времени - далёкое. */
    private static final int TYPE_DRAW_LONG = 3;
    /**  */
    private static final Stroke[] TYPE_DRAW_STROKES = {
        new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND),
        new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND),
        new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
            10.0f, new float[] {10.0f, 10.0f}, 0.0f
        ),
        new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
            10.0f, new float[] {1.0f, 10.0f}, 0.0f
        )
    };

    /** шаг по времени минимальный. */
    public static final double TIME_STEP_MIN = 0.033;
    /** шаг по времени максимальный. */
    public static final double TIME_STEP_MAX = 10.0;

    /** проигрыватель фильма. */
    private Player player = null;

    /** Задаем параметры движения линии. */
    private void fillLines() {
        AniTimeLine line = new AniTimeLine();
        line.setLineColor(new Color(50, 20, 255));
        line.setThick(3);
        AniTimePoint timePnt1 = new AniTimePoint();
        timePnt1.add(new AniPoint(100, 100, 0));
        timePnt1.add(new AniPoint(90, 120, 10));
        timePnt1.add(new AniPoint(80, 130, 20));
        timePnt1.add(new AniPoint(50, 160, 30));
        AniTimePoint timePnt2 = new AniTimePoint();
        timePnt2.add(new AniPoint(100, 100, 0));
        timePnt2.add(new AniPoint(100, 130, 10));
        timePnt2.add(new AniPoint(100, 140, 20));
        timePnt2.add(new AniPoint(100, 150, 30));
        AniTimePoint timePnt3 = new AniTimePoint();
        timePnt3.add(new AniPoint(100, 100, 0));
        timePnt3.add(new AniPoint(110, 120, 10));
        timePnt3.add(new AniPoint(130, 140, 20));
        timePnt3.add(new AniPoint(160, 160, 30));
        line.add(timePnt1);
        line.add(timePnt2);
        line.add(timePnt3);
        anime.add(line);

        AniTimePolygon polygon = new AniTimePolygon();
        polygon.setFillColor(Color.yellow);
        polygon.setLineColor(Color.red);
        polygon.setThick(7);
        AniTimePoint[] points = new AniTimePoint[10];
        for (int i = 0; i < 10; i++) {
            points[i] = new AniTimePoint();
            polygon.add(points[i]);
        }
        for (double time = MINIMAL_TIME; time <= MAXIMAL_TIME; time += 10) {
            for (int i = 0; i < 10; i++) {
                double rad = 10;
                if (i % 2 == 0) {
                    rad = 10 + time * 4;
                } else {
                    rad = 410 - time * 4;
                }
                double alf = (i * 36 + time * 3.96) * ONE_GRAD;
                AniPoint pnt = new AniPoint();
                pnt.setTime(time);
                pnt.setX(400 + rad * Math.cos(alf));
                pnt.setY(400 + rad * Math.sin(alf));
                points[i].add(pnt);
            }
        }
        anime.add(polygon);

    }

    /** Настройка выпадающего меню. */
    private SettingMenuItem[] settingPopUpMenu = {
        new SettingMenuItem("Удалить", null, "удалить чего-нибудь", null,
            new SettingMenuItem[] {
                new SettingMenuItem(
                    "Удалить точку", null, "удалить найденную точку из мировой линии", e -> {
                        if (captured != null) {
                            final AniPoint point = captured.getPoint();
                            final AniTimePoint timePoint = captured.getTimePoint();
                            final AniTimeAbstractLine polygon = captured.getPolygon();
                            if (point != null && timePoint != null && polygon != null) {
                                timePoint.remove(point);
                                if (timePoint.isEmpty()) {
                                    polygon.remove(timePoint);
                                }
                            }
                        }
                        captured = null;
                        repaint();
                    }, null
                ), new SettingMenuItem(
                    "Удалить мировую линию", null,
                    "Удалить найденную мировую линию из полигона (полилинии)", e -> {
                        if (captured != null) {
                            final AniTimePoint timePoint = captured.getTimePoint();
                            final AniTimeAbstractLine polygon = captured.getPolygon();
                            if (polygon != null && timePoint != null) {
                                polygon.remove(timePoint);
                            }
                        }
                        captured = null;
                        repaint();
                    }, null
                ), new SettingMenuItem(
                    "Удалить полигон", null,
                    "Удалить полигон или полилинию", e -> {
                        if (captured != null) {
                            final AniTimeAbstractLine poly = captured.getPolygon();
                            if (poly instanceof AniTimeLine) {
                                anime.getLines().remove(poly);
                            } else if (poly instanceof AniTimePolygon) {
                                anime.getPolygons().remove(poly);
                            }
                        }
                        captured = null;
                        repaint();
                    }, null
                )
            }
        ), new SettingMenuItem(
            "Добавить по времени", null, "Добавить точку в мировую линию по времени", e -> {
                //jMenuSetUnVisible_action(e);
            }, null
        ), new SettingMenuItem(
            "Добавить мировую линию", null, "Добавить мировую линию для этого времени", e -> {
                //jMenuSetUnVisible_action(e);
            }, null
        ), new SettingMenuItem(
            "Свойства", null, "Свойства этого объекта Полигона или Линии", e -> {
                if (captured != null) {
                    final AniTimeAbstractLine poly = captured.getPolygon();
                    final PropertyDialog dlg = new PropertyDialog(poly);
                    dlg.setVisible(true);
                }
                captured = null;
                repaint();
            }, null
        )
    };

    /**
     * Конструктор.
     */
    public AnimationView() {
        try {
            setPreferredSize(new Dimension(700, 700));
            setBackground(Color.white);
            setBorder(BorderFactory.createLoweredBevelBorder());
            setLayout(new BorderLayout());

            final MyMouseAdapter mouseListener = new MyMouseAdapter();
            addMouseMotionListener(mouseListener);
            addMouseListener(mouseListener);
            addMouseWheelListener(mouseListener);

            final MyKeyAdapter keyListener = new MyKeyAdapter();
            addKeyListener(keyListener);

            setFocusable(true);
            createPopupMenu();
            fillLines();
            // таймер для показа мультика
            timer = new Timer(
                33, new ActionListener() {
                    public void actionPerformed(final ActionEvent evt) {
                        curTime += TIME_STEP_MIN;
                        if (curTime >= MAXIMAL_TIME) {
                            curTime = MINIMAL_TIME;
                            timer.stop();
                            isShow = false;
                        }
                        repaint();
                    }
                }
            );
        } catch (final Exception ex) {
            Log.stackTrace(this.getClass(), ex);
        }
    }

    /**
     * Устанавливаем родительский Frame.
     * @param aParentFrame родительский Frame
     */
    public void setParentFrame(final JFrame aParentFrame) {
        parentFrame = aParentFrame;
    }

    /**
     * Устанавливаем поле для показа времени.
     * @param aShowTime поле для показа времени
     */
    public void setShowTime(final JTextField aShowTime) {
        showTime = aShowTime;
        showTime.setText(nf.format(curTime));
        showTime.addActionListener(this);
    }

    @Override
    public void paint(final Graphics g) {
        super.paint(g);
        if (anime != null) {
            final Graphics2D graph = (Graphics2D) g;
            if (isShow) {
                drawMovies(graph);
                drawAnime(graph, curTime, TYPE_DRAW_ASIS);
            } else {
                drawMovies(graph);
                drawTimeLines(graph);
                for (double tm = MINIMAL_TIME; tm <= MAXIMAL_TIME; tm += 10) {
                    int typeDraw;
                    final double timeDist = Math.abs(tm - curTime);
                    if (timeDist < 5) {
                        typeDraw = TYPE_DRAW_SHORT;
                    } else if (timeDist < 15) {
                        typeDraw = TYPE_DRAW_NEAR;
                    } else if (timeDist <= 30) {
                        typeDraw = TYPE_DRAW_LONG;
                    } else {
                        continue;
                    }
                    drawAnime(graph, tm, typeDraw);
                }
                drawAnime(graph, curTime, TYPE_DRAW_ASIS);
                drawTimePoints(graph);
                if (captured != null && mouseButton == MouseEvent.BUTTON3) {
                    drawCaptured(graph);
                }
            }
        }
    }

    /**
     * Показать кадр из фильма.
     * @param g  the <code>Graphics2D</code> context in which to paint
     */
    private void drawMovies(Graphics2D g) {
        if (player == null) {
            return;
        }
        Time time = new Time(curTime);
        Time duration = player.getDuration();
        if (curTime >= 0.0 && duration.getSeconds() >= curTime) {
            //time = player.getMediaTime();
            player.setMediaTime(time);
            FrameGrabbingControl fgc = (FrameGrabbingControl) player.getControl(
                "javax.media.control.FrameGrabbingControl"
            );
            if (fgc != null) {
                Buffer buffer = fgc.grabFrame();
                BufferToImage btoi = new BufferToImage((VideoFormat) buffer.getFormat());
                Image img = btoi.createImage(buffer);
                g.drawImage(img, 0, 0 , 400, 400, null);
            } else {
                player = null;
            }
        }
    }

    /**
     * Нарисовать объект для данного времени.
     * @param g Graphics2D
     * @param time время
     * @param typeDrawing тип рисования
     */
    private void drawAnime(Graphics2D g, double time, int typeDrawing) {
        for (AniTimePolygon polygon : anime.getPolygons()) {
            ArrayList<AniPoint> pnts = polygon.getTime(time);
            int nPoints = pnts.size(), i = 0;
            int[] xPoints = new int[nPoints];
            int[] yPoints = new int[nPoints];
            for (AniPoint pnt : pnts) {
                xPoints[i] = (int) pnt.getX();
                yPoints[i] = (int) pnt.getY();
                i++;
            }
            if (typeDrawing == TYPE_DRAW_ASIS) {
                g.setStroke(
                    new BasicStroke(
                        polygon.getThick(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND
                    )
                );
                if (isShow) {
                    g.setColor(polygon.getFillColor());
                    g.fillPolygon(xPoints, yPoints, nPoints);
                }
                g.setColor(polygon.getLineColor());
                g.drawPolygon(xPoints, yPoints, nPoints);
            } else {
                g.setStroke(TYPE_DRAW_STROKES[typeDrawing]);
                g.setColor(calcTimeColor(time));
                g.drawPolygon(xPoints, yPoints, nPoints);
            }
        }
        for (AniTimeLine line : anime.getLines()) {
            ArrayList<AniPoint> pnts = line.getTime(time);
            int nPoints = pnts.size(), i = 0;
            int[] xPoints = new int[nPoints];
            int[] yPoints = new int[nPoints];
            for (AniPoint pnt : pnts) {
                xPoints[i] = (int) pnt.getX();
                yPoints[i] = (int) pnt.getY();
                i++;
            }
            if (typeDrawing == TYPE_DRAW_ASIS) {
                g.setColor(line.getLineColor());
                g.setStroke(
                    new BasicStroke(
                        line.getThick(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND
                    )
                );
            } else {
                g.setStroke(TYPE_DRAW_STROKES[typeDrawing]);
                g.setColor(calcTimeColor(time));
            }
            g.drawPolyline(xPoints, yPoints, nPoints);
        }
    }

    /**
     * Нарисовать ближайшие к данному времени точки времени.
     * @param g Graphics2D
     */
    private void drawTimeLines(Graphics2D g) {
        for (AniTimePolygon polygon : anime.getPolygons()) {
            for (AniTimePoint timePoints : polygon) {
                drawTimeLine(g, timePoints, null);
            }
        }
        for (AniTimeLine line : anime.getLines()) {
            for (AniTimePoint timePoints : line) {
                drawTimeLine(g, timePoints, null);
            }
        }
    }

    /**
     * Нарисовать ближайшие к данному времени точки времени.
     * @param g Graphics2D
     * @param timePoints линия точки во времени
     * @param color цвет линия точки
     */
    private void drawTimeLine(Graphics2D g, AniTimePoint timePoints, Color color) {
        int x1 = 0, y1 = 0, x2, y2;
        double tm1 = MINIMAL_TIME, tm2;
        boolean isFirst = true;
        for (AniPoint pnt : timePoints) {
            tm2 = (int) pnt.getTime();
            x2 = (int) pnt.getX();
            y2 = (int) pnt.getY();
            if (!isFirst) {
                if ((Math.abs(tm1 - curTime) <= 30) && (Math.abs(tm2 - curTime) <= 30)) {
                    if (color != null) {
                        g.setColor(color);
                    } else {
                        g.setColor(calcTimeColor((tm1 + tm2) / 2));
                    }
                    g.drawLine(x1, y1, x2, y2);
                }
            }
            tm1 = tm2;
            x1 = x2;
            y1 = y2;
            isFirst = false;
        }
    }

    /**
     * Ставим ближайшие к данному времени точки во времени.
     * @param g Graphics2D
     */
    private void drawTimePoints(Graphics2D g) {
        for (AniTimePolygon polygon : anime.getPolygons()) {
            for (AniTimePoint timePoints : polygon) {
                drawTimePoint(g, timePoints);
            }
        }
        for (AniTimeLine line : anime.getLines()) {
            for (AniTimePoint timePoints : line) {
                drawTimePoint(g, timePoints);
            }
        }
    }

    /**
     * Нарисовать ближайшие к данному времени точки времени.
     * @param g Graphics2D
     * @param timePoints линия точки во времени
     */
    private void drawTimePoint(Graphics2D g, AniTimePoint timePoints) {
        int x, y;
        double tm;
        for (AniPoint pnt : timePoints) {
            tm = (int) pnt.getTime();
            x = (int) pnt.getX();
            y = (int) pnt.getY();
            if (Math.abs(tm - curTime) <= 30) {
                g.setColor(calcTimeColor(tm));
                g.drawRect(x - 2, y - 2, 5, 5);
            }
        }
    }

    /**
     * Выделить цветом выбранную точку и линию.
     * @param g Graphics2D
     */
    private void drawCaptured(Graphics2D g) {
        if (captured == null) {
            return;
        }
        Color color = new Color(255, 0, 0);
        AniTimePoint timePoint = captured.getTimePoint();
        if (timePoint != null) {
            drawTimeLine(g, timePoint, color);
        }
        AniPoint pnt = captured.getPoint();
        if (pnt != null) {
            g.setColor(color);
            int x, y;
            x = (int) pnt.getX();
            y = (int) pnt.getY();
            g.drawRect(x - 3, y - 3, 7, 7);
        }
    }

    /**
     * Вычисление цвета по степени удалённости от текущего времени.
     * @param time время для вычисления
     * @return цвет для данного времени
     */
    private Color calcTimeColor(double time) {
        final int[] colLst =  {220, 255, 220};
        final int[] colCLst = {100, 255, 100};
        final int[] colCur =  {255, 220, 220};
        final int[] colCFtr = {100, 100, 255};
        final int[] colFtr =  {220, 220, 255};
        int r = 0, g = 0, b = 0;
        int dtm = (int) (curTime - time);
        if (dtm == 0) {
            r = colCur[0];
            g = colCur[1];
            b = colCur[2];
        } else if (dtm < 0 && dtm >= -30) {
            r = colCLst[0] + (int) ((colCLst[0] - colLst[0]) * (double) dtm / 30);
            g = colCLst[1] + (int) ((colCLst[1] - colLst[1]) * (double) dtm / 30);
            b = colCLst[2] + (int) ((colCLst[2] - colLst[2]) * (double) dtm / 30);
        } else if (dtm <= 30) {
            r = colCFtr[0] + (int) ((colFtr[0] - colCFtr[0]) * (double) dtm / 30);
            g = colCFtr[1] + (int) ((colFtr[1] - colCFtr[1]) * (double) dtm / 30);
            b = colCFtr[2] + (int) ((colFtr[2] - colCFtr[2]) * (double) dtm / 30);
        }
        return new Color(r, g, b);
    }

    /**
     * установить / показать следующее время.
     * @param delta временное смещение
     */
    public void nextTime(double delta) {
        isShow = false;
        if (delta == TIME_STEP_MIN) {
            curTime += delta;
        } else {
            curTime = Math.round(curTime / delta) * delta + delta;
        }
        if (curTime > MAXIMAL_TIME) {
            curTime = MAXIMAL_TIME;
        }
        if (showTime != null) {
            showTime.setText(nf.format(curTime));
        }
        timer.stop();
        repaint();
    }

    /**
     * установить / показать предыдущее время.
     * @param delta временное смещение
     */
    public void prevTime(double delta) {
        isShow = false;
        if (delta == TIME_STEP_MIN) {
            curTime -= delta;
        } else {
            curTime = Math.round(curTime / delta) * delta - delta;
        }
        if (curTime < MINIMAL_TIME) {
            curTime = MINIMAL_TIME;
        }
        if (showTime != null) {
            showTime.setText(nf.format(curTime));
        }
        timer.stop();
        repaint();
    }

    /**
     * Запускаем анимацию.
     */
    public void startAnimation() {
        if (curTime >= MAXIMAL_TIME) {
            curTime = MINIMAL_TIME;
        }
        if (isShow) {
            isShow = false;
            if (showTime != null) {
                showTime.setText(nf.format(curTime));
            }
            timer.stop();
            repaint();
        } else {
            isShow = true;
            timer.start();
        }
    }

    /**
     * Class for implemented MouseListener, MouseWheelListener, MouseMotionListener interfaces.
     */
    class MyMouseAdapter extends MouseAdapter {

        /**
         *
         * @param e A mouse action occurred event
         */
        public void mousePressed(MouseEvent e) {
            Point mousePnt = e.getPoint();
            mouseButton = e.getButton();
            switch (mouseButton) {
                case MouseEvent.BUTTON1:
                    for (AniTimeLine line : anime.getLines()) {
                        captured = line.findPoint(mousePnt, curTime, e);
                        if (captured == null) {
                            captured = line.findLine(mousePnt, curTime);
                        }
                        if (captured != null) {
                            repaint();
                            return;
                        }
                    }
                    for (AniTimePolygon polygon : anime.getPolygons()) {
                        captured = polygon.findPoint(mousePnt, curTime, e);
                        if (captured == null) {
                            captured = polygon.findLine(mousePnt, curTime);
                        }
                        if (captured != null) {
                            repaint();
                            return;
                        }
                    }
                    break;
                case MouseEvent.BUTTON3:
                    for (AniTimeLine line : anime.getLines()) {
                        captured = line.findPoint(mousePnt, curTime, e);
                        if (captured != null) {
                            repaint();
                            return;
                        }
                    }
                    for (AniTimePolygon polygon : anime.getPolygons()) {
                        captured = polygon.findPoint(mousePnt, curTime, e);
                        if (captured != null) {
                            repaint();
                            return;
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        /**
         * отжали кнопку мышки.
         * @param e A mouse action occurred event
         */
        public void mouseReleased(MouseEvent e) {
            if (maybeShowPopup(e)) {
                if (captured != null) {
                    repaint();
                }
            } else {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (captured != null) {
                        AniPoint point = captured.getPoint();
                        if (point != null) {
                            Point mousePnt = e.getPoint();
                            point.setX(mousePnt.getX());
                            point.setY(mousePnt.getY());
                            repaint();
                        }
                        captured = null;
                    }
                }
            }
        }

        /**
         * Перемещаем выбранную точку мышки.
         * @param e A mouse action occurred event
         */
        public void mouseDragged(MouseEvent e) {
            if (captured != null && (e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0) {
                AniPoint point = captured.getPoint();
                if (point != null) {
                    Point mousePnt = e.getPoint();
                    point.setX(mousePnt.getX());
                    point.setY(mousePnt.getY());
                    repaint();
                }
                //Immediately(this.getVisibleRect());
            }
        }

        /**
         * Invoked when the mouse wheel is rotated.
         * @see MouseWheelListener
         * @param e MouseWheelEvent
         */
        public void mouseWheelMoved(MouseWheelEvent e) {
            double timeStep = TIME_STEP_MIN;
            if (e.isShiftDown()) {
                timeStep = TIME_STEP_MIN * 10;
            }
            if (e.getWheelRotation() > 0) {
                prevTime(timeStep);
            } else {
                nextTime(timeStep);
            }
        }

    }

    /**
     * Class for implemented KeyListener interfaces.
     */
    class MyKeyAdapter extends KeyAdapter {

        /**
         * Interface KeyListener
         * Invoked when a key has been typed.
         * @param e An event which indicates that a keystroke occurred in a component.
         */
        public void keyTyped(KeyEvent e) {
        }

        /**
         * Invoked when a key has been pressed.
         * @see KeyListener
         * @param e An event which indicates that a keystroke occurred in a component.
         */
        public void keyPressed(KeyEvent e) {
        }

        /**
         * Invoked when a key has been released.
         * @see KeyListener
         * @param e An event which indicates that a keystroke occurred in a component.
         */
        public void keyReleased(KeyEvent e) {
        }
    }

    /**
     * создаём и настраиваем выпадающее меню.
     */
    public void createPopupMenu() {
        // создаём и настраиваем выпадающее меню
        for (SettingMenuItem aSetMenu : settingPopUpMenu) {
            popUpMenu.add(Util.createImageMenuItem(aSetMenu));
        }
        popUpMenu.addPopupMenuListener(this);
    }

    /**
     * Попытка вызвать Pop Up Menu.
     * @param e MouseEvent
     * @return true if menu is showed.
     */
    private boolean maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            popUpMenu.show(e.getComponent(), e.getX(), e.getY());

            return true;
        }
        return false;
    }

    /**
     *  This method is called before the popup menu becomes visible.
     * @param event {@link PopupMenuEvent}
     */
    public void popupMenuWillBecomeVisible(PopupMenuEvent event) {
        JMenu main = (JMenu) popUpMenu.getComponent(0);
        JMenuItem menuPoint = (JMenuItem) main.getMenuComponent(0);
        JMenuItem menuTimePnt = (JMenuItem) main.getMenuComponent(1);
        JMenuItem menuPolygon = (JMenuItem) main.getMenuComponent(2);
        if (captured != null) {
            if (captured.getPoint() != null) {
                menuPoint.setEnabled(true);
            } else {
                menuPoint.setEnabled(false);
            }
            if (captured.getTimePoint() != null) {
                menuTimePnt.setEnabled(true);
            } else {
                menuTimePnt.setEnabled(false);
            }
            if (captured.getPolygon() != null) {
                menuPolygon.setEnabled(true);
            } else {
                menuPolygon.setEnabled(false);
            }
        } else {
            menuPoint.setEnabled(false);
            menuTimePnt.setEnabled(false);
            menuPolygon.setEnabled(false);
        }
    }

    /**
     * This method is called before the popup menu becomes invisible.
     * Note that a JPopupMenu can become invisible any time
     * @param event {@link PopupMenuEvent}
     */
    public void popupMenuWillBecomeInvisible(PopupMenuEvent event) {
    }

    /**
     * This method is called when the popup menu is canceled.
     * @param event {@link PopupMenuEvent}
     */
    public void popupMenuCanceled(PopupMenuEvent event) {
        if (captured != null) {
            captured = null;
            repaint();
        }
    }

    /**
     *
     * @param e ActionEvent
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == showTime) {
            curTime = Double.valueOf(showTime.getText());
            if (curTime > MAXIMAL_TIME) {
                curTime = MAXIMAL_TIME;
            } else if (curTime < MINIMAL_TIME) {
                curTime = MINIMAL_TIME;
            }
            showTime.setText(nf.format(curTime));
            timer.stop();
            repaint();
        }
    }

    /**
     * прочитать сохраненный мультик.
     */
    public void load() {
        FileDialog dlg = new FileDialog(
            parentFrame,
            "Читать АНИМЕ из файла",
            FileDialog.LOAD
        );
        dlg.setFile("*.anime");
        dlg.setVisible(true);
        if (dlg.getFile() != null) {
            String fileName = dlg.getDirectory() + dlg.getFile();
            anime = AnimationXMLHelper.load(fileName);
        }
        repaint();
    }

    /**
     * записать мультик.
     */
    public void save() {
        String fileName = anime.getFileName();
        if (fileName != null && !fileName.isEmpty()) {
            AnimationXMLHelper.save(anime, fileName);
        } else {
            saveAs();
        }
    }

    /**
     * записать мультик в другой файл.
     */
    public void saveAs() {
        FileDialog dlg = new FileDialog(
            parentFrame,
            "Записать АНИМЕ в файл",
            FileDialog.SAVE
        );
        dlg.setFile("*.anime");
        dlg.setVisible(true);
        if (dlg.getFile() != null) {
            String fileName = dlg.getDirectory() + dlg.getFile();
            AnimationXMLHelper.save(anime, fileName);
        }
    }

    /**
     * прикрепить фильм к мультику.
     */
    public void attachMoves() {
        FileDialog dlg = new FileDialog(
            parentFrame,
            "Прикрепить фильм к мультику",
            FileDialog.LOAD
        );
        dlg.setFile("*.avi;*.mov;*.mpg");
        dlg.setVisible(true);
        if (dlg.getFile() != null) {
            String fileName = dlg.getDirectory() + dlg.getFile();
            //String fileName = "C:\\Documents and Settings\\vlaskin\\My Documents\\FOX.AVI";
            try {
                URL url = new URL("file", "", fileName);
                player = Manager.createRealizedPlayer(url);
                player.start();
                player.stop();
                //player.realize();
            } catch (IOException e) {
                e.printStackTrace();
                player = null;
            } catch (NoPlayerException e) {
                e.printStackTrace();
                player = null;
            } catch (CannotRealizeException e) {
                e.printStackTrace();
                player = null;
            }
        }
        repaint();
    }

    /**
     * открепить фильм от мультика.
     */
    public void detachMoves() {
        player.stop();
        player.close();
        player = null;
    }

}
