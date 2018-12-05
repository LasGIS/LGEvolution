/**
 * @(#)DrawPrimitive.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.draw;

import com.lasgis.evolution.object.EvolutionConstants;

import java.awt.*;
import java.util.Map;

import static com.lasgis.evolution.object.EvolutionConstants.BARLEY_PLANT_KEY;
import static com.lasgis.evolution.object.EvolutionConstants.CHAMOMILE_LEAF_KEY;
import static com.lasgis.evolution.object.EvolutionConstants.GRASS_PLANT_KEY;
import static com.lasgis.evolution.object.EvolutionConstants.HAY_PLANT_KEY;
import static com.lasgis.evolution.object.EvolutionConstants.MEAT_PLANT_KEY;

/**
 * Главный класс для рисования всякого рода животных, растений.
 * @author vladimir.laskin
 * @version 1.0
 */
public class DrawPrimitive {

    private static final Color STRESS_COLOR = new Color(255, 255, 0);
    private static final Color SELECTED_COLOR = new Color(255, 0, 0);

    /**
     * Примитив Свинья на распутье...
     */
    public static final Primitive PIG_PRIMITIVE = new Primitive(
        new PrimitivePath(
            PrimitiveType.area,
            new ParamColor("возраст", 0.0,
                new ParamColorPoint(new Color(255, 180, 255), 0.0),
                new RatesColor(0.7,
                    new RatesColorPoint(new Color(0, 0, 255), GRASS_PLANT_KEY),
                    new RatesColorPoint(new Color(0, 255, 0), BARLEY_PLANT_KEY),
                    new RatesColorPoint(new Color(255, 0, 0), MEAT_PLANT_KEY),
                    new RatesColorPoint(new Color(0, 255, 255), CHAMOMILE_LEAF_KEY),
                    new RatesColorPoint(new Color(255, 255, 0), HAY_PLANT_KEY)
                ),
                new ParamColorPoint(new Color(255, 255, 255), 1.0)
            ),
            new PrimitiveAddon("масса", "энергия", 73, 55, 0.00005, 0.00007,
                new PrimitivePoint(28, 23),
                new PrimitivePoint(42, 16),
                new PrimitivePoint(57, 17),
                new PrimitivePoint(80, 25),
                new PrimitivePoint(77, 30),
                new PrimitivePoint(79, 25),
                new PrimitivePoint(83, 25),
                new PrimitivePoint(81, 15),
                new PrimitivePoint(87, 2),
                new PrimitivePoint(94, 14),
                new PrimitivePoint(96, 19),
                new PrimitivePoint(109, 18),
                new PrimitivePoint(116, 3),
                new PrimitivePoint(113, 20),
                new PrimitivePoint(122, 31),
                new PrimitivePoint(120, 39),
                new PrimitivePoint(122, 41),
                new PrimitivePoint(128, 42),
                new PrimitivePoint(132, 46),
                new PrimitivePoint(132, 51),
                new PrimitivePoint(131, 54),
                new PrimitivePoint(128, 57),
                new PrimitivePoint(118, 58),
                new PrimitivePoint(118, 72),
                new PrimitivePoint(116, 73),
                new PrimitivePoint(105, 64),
                new PrimitivePoint(109, 68),
                new PrimitivePoint(105, 70),
                new PrimitivePoint(101, 70),
                new PrimitivePoint(97, 67),
                new PrimitivePoint(102, 71),
                new PrimitivePoint(99, 74),
                new PrimitivePoint(94, 78),
                new PrimitivePoint(83, 78),
                new PrimitivePoint(84, 80),
                new PrimitivePoint(86, 91),
                new PrimitivePoint(97, 101),
                new PrimitivePoint(104, 102),
                new PrimitivePoint(104, 98),
                new PrimitivePoint(98, 92),
                new PrimitivePoint(94, 78),
                new PrimitivePoint(83, 78),
                new PrimitivePoint(84, 80),
                new PrimitivePoint(83, 82),
                new PrimitivePoint(78, 82),
                new PrimitivePoint(72, 86),
                new PrimitivePoint(73, 97),
                new PrimitivePoint(85, 105),
                new PrimitivePoint(79, 106),
                new PrimitivePoint(76, 103),
                new PrimitivePoint(79, 108),
                new PrimitivePoint(71, 107),
                new PrimitivePoint(62, 102),
                new PrimitivePoint(63, 92),
                new PrimitivePoint(58, 83),
                new PrimitivePoint(59, 79),
                new PrimitivePoint(57, 83),
                new PrimitivePoint(46, 82),
                new PrimitivePoint(39, 80),
                new PrimitivePoint(39, 65),
                new PrimitivePoint(40, 79),
                new PrimitivePoint(37, 80),
                new PrimitivePoint(33, 83),
                new PrimitivePoint(36, 93),
                new PrimitivePoint(41, 94),
                new PrimitivePoint(43, 99),
                new PrimitivePoint(40, 99),
                new PrimitivePoint(36, 97),
                new PrimitivePoint(39, 98),
                new PrimitivePoint(40, 102),
                new PrimitivePoint(34, 102),
                new PrimitivePoint(26, 97),
                new PrimitivePoint(15, 71),
                new PrimitivePoint(17, 67),
                new PrimitivePoint(16, 54),
                new PrimitivePoint(16, 40),
                new PrimitivePoint(28, 25)
            )
        ),
        new PrimitivePath(
            PrimitiveType.line,
            new ParamColorPoint(new Color(0, 0, 0), 0.0),
            new PrimitiveAddon("масса", "энергия", 73, 55, 0.00005, 0.00007,
                new PrimitivePoint(116, 46),
                new PrimitivePoint(122, 40),
                new PrimitivePoint(128, 42),
                new PrimitivePoint(132, 46),
                new PrimitivePoint(132, 51),
                new PrimitivePoint(131, 54),
                new PrimitivePoint(128, 57),
                new PrimitivePoint(118, 57),
                new PrimitivePoint(116, 48)
            )
        )
    );

    /**
     * Примитив Орёл на распутье...
     */
    public static final Primitive VULTURE_PRIMITIVE = new Primitive(
        new PrimitivePath(
            PrimitiveType.area,
            new ParamColor("возраст", 0.0,
                new ParamColorPoint(new Color(100, 130, 130), 0.0),
                new ParamColorPoint(new Color(255, 255, 255), 1.0)
            ),
            new PrimitiveAddon("масса", "энергия", 50, 42, 0.00005, 0.00007,
                new PrimitivePoint(12, 22),
                new PrimitivePoint(16, 16),
                new PrimitivePoint(25, 13),
                new PrimitivePoint(34, 6),
                new PrimitivePoint(43, 3),
                new PrimitivePoint(53, 4),
                new PrimitivePoint(59, 9),
                new PrimitivePoint(60, 15),
                new PrimitivePoint(60, 20),
                new PrimitivePoint(65, 17),
                new PrimitivePoint(69, 19),
                new PrimitivePoint(71, 25),
                new PrimitivePoint(72, 33),
                new PrimitivePoint(68, 41),
                new PrimitivePoint(67, 44),
                new PrimitivePoint(76, 45),
                new PrimitivePoint(88, 54),
                new PrimitivePoint(106, 69),
                new PrimitivePoint(71, 65),
                new PrimitivePoint(39, 71),
                new PrimitivePoint(22, 81),
                new PrimitivePoint(19, 73),
                new PrimitivePoint(19, 58),
                new PrimitivePoint(21, 51),
                new PrimitivePoint(25, 47),
                new PrimitivePoint(30, 45),
                new PrimitivePoint(30, 33),
                new PrimitivePoint(32, 27),
                new PrimitivePoint(16, 27),
                new PrimitivePoint(16, 31),
                new PrimitivePoint(13, 26)
            )
        )
    );

    /**
     * Примитив Свинья на распутье...
     */
    public static final Primitive STONE_PRIMITIVE = new Primitive(
        new PrimitivePath(
            PrimitiveType.area,
            new ParamColorPoint(new Color(187, 187, 187), 0.0),
            new PrimitiveAddon("масса", "масса", 2, 3, 0.01, -0.005,
                new PrimitivePoint(2, 1),
                new PrimitivePoint(2, 4),
                new PrimitivePoint(1, 5),
                new PrimitivePoint(1, 2)
            )
        ),
        new PrimitivePath(
            PrimitiveType.area,
            new ParamColorPoint(new Color(64, 96, 128), 0.0),
            new PrimitiveAddon("масса", "масса", 2, 3, 0.01, -0.005,
                new PrimitivePoint(2, 1),
                new PrimitivePoint(3, 2),
                new PrimitivePoint(3, 5),
                new PrimitivePoint(2, 4)
            )
        ),
        new PrimitivePath(
            PrimitiveType.area,
            new ParamColorPoint(new Color(255, 255, 255), 0.0),
            new PrimitiveAddon("масса", "масса", 2, 3, 0.01, -0.005,
                new PrimitivePoint(2, 4),
                new PrimitivePoint(3, 5),
                new PrimitivePoint(2, 6),
                new PrimitivePoint(1, 5)
            )
        ),
        new PrimitivePath(
            PrimitiveType.line,
            new ParamColorPoint(new Color(0, 0, 0), 0.0),
            new PrimitiveAddon("масса", "масса", 2, 3, 0.01, -0.005,
                new PrimitivePoint(1, 2),
                new PrimitivePoint(2, 1),
                new PrimitivePoint(3, 2),
                new PrimitivePoint(3, 5),
                new PrimitivePoint(2, 6),
                new PrimitivePoint(1, 5),
                new PrimitivePoint(1, 2)
            )
        )
    );

    /**
     * Примитив листья Ромашки.
     */
    public static final Primitive CHAMOMILE_LEAF_PRIMITIVE = new Primitive(
        new PrimitivePath(
            PrimitiveType.areaLine,
            new ParamColorPoint(new Color(0, 250, 200), 0.0),
            new ParamColorPoint(new Color(20, 120, 140), 0.0),
            new PrimitiveAddon("масса", "масса", 0, 0, 0.002, 0.002,
                new PrimitivePoint(1, 0),
                new PrimitivePoint(5, 2),
                new PrimitivePoint(4, 3),
                new PrimitivePoint(5, 5),
                new PrimitivePoint(3, 4),
                new PrimitivePoint(2, 5),
                new PrimitivePoint(0, 1),
                new PrimitivePoint(-2, 5),
                new PrimitivePoint(-3, 4),
                new PrimitivePoint(-5, 5),
                new PrimitivePoint(-4, 3),
                new PrimitivePoint(-5, 2),
                new PrimitivePoint(-1, 0),
                new PrimitivePoint(-5, -2),
                new PrimitivePoint(-4, -3),
                new PrimitivePoint(-5, -5),
                new PrimitivePoint(-3, -4),
                new PrimitivePoint(-2, -5),
                new PrimitivePoint(0, -1),
                new PrimitivePoint(2, -5),
                new PrimitivePoint(3, -4),
                new PrimitivePoint(5, -5),
                new PrimitivePoint(4, -3),
                new PrimitivePoint(5, -2),
                new PrimitivePoint(1, 0)
            )
        )
    );

    /**
     * Примитив лепески Ромашки.
     */
    public static final Primitive CHAMOMILE_FLOWER_PRIMITIVE = new Primitive(
        new PrimitivePath(
            PrimitiveType.areaLine,
            new ParamColorPoint(new Color(240, 255, 255), 0.0),
            new ParamColorPoint(new Color(225, 240, 240), 0.0),
            new PrimitiveAddon("цвет", "цвет", 0, 0, 0.002, 0.002,
                new PrimitivePoint(0.00, 6.00),
                new PrimitivePoint(1.00, 5.00),
                new PrimitivePoint(1.20, 4.00),
                new PrimitivePoint(1.00, 3.00),
                new PrimitivePoint(0.40, 1.00),
                new PrimitivePoint(2.25, 3.75),
                new PrimitivePoint(3.25, 4.25),
                new PrimitivePoint(4.25, 4.25),
                new PrimitivePoint(4.25, 3.25),
                new PrimitivePoint(3.75, 2.25),
                new PrimitivePoint(1.00, 0.40),
                new PrimitivePoint(3.00, 1.00),
                new PrimitivePoint(4.00, 1.20),
                new PrimitivePoint(5.00, 1.00),
// ------------------------------------------------------------------------
                new PrimitivePoint(6.00, -0.00),
                new PrimitivePoint(5.00, -1.00),
                new PrimitivePoint(4.00, -1.20),
                new PrimitivePoint(3.00, -1.00),
                new PrimitivePoint(1.00, -0.40),
                new PrimitivePoint(3.75, -2.25),
                new PrimitivePoint(4.25, -3.25),
                new PrimitivePoint(4.25, -4.25),
                new PrimitivePoint(3.25, -4.25),
                new PrimitivePoint(2.25, -3.75),
                new PrimitivePoint(0.40, -1.00),
                new PrimitivePoint(1.00, -3.00),
                new PrimitivePoint(1.20, -4.00),
                new PrimitivePoint(1.00, -5.00),
// ------------------------------------------------------------------------
                new PrimitivePoint(-0.00, -6.00),
                new PrimitivePoint(-1.00, -5.00),
                new PrimitivePoint(-1.20, -4.00),
                new PrimitivePoint(-1.00, -3.00),
                new PrimitivePoint(-0.40, -1.00),
                new PrimitivePoint(-2.25, -3.75),
                new PrimitivePoint(-3.25, -4.25),
                new PrimitivePoint(-4.25, -4.25),
                new PrimitivePoint(-4.25, -3.25),
                new PrimitivePoint(-3.75, -2.25),
                new PrimitivePoint(-1.00, -0.40),
                new PrimitivePoint(-3.00, -1.00),
                new PrimitivePoint(-4.00, -1.20),
                new PrimitivePoint(-5.00, -1.00),
// ------------------------------------------------------------------------
                new PrimitivePoint(-6.00, 0.00),
                new PrimitivePoint(-5.00, 1.00),
                new PrimitivePoint(-4.00, 1.20),
                new PrimitivePoint(-3.00, 1.00),
                new PrimitivePoint(-1.00, 0.40),
                new PrimitivePoint(-3.75, 2.25),
                new PrimitivePoint(-4.25, 3.25),
                new PrimitivePoint(-4.25, 4.25),
                new PrimitivePoint(-3.25, 4.25),
                new PrimitivePoint(-2.25, 3.75),
                new PrimitivePoint(-0.40, 1.00),
                new PrimitivePoint(-1.00, 3.00),
                new PrimitivePoint(-1.20, 4.00),
                new PrimitivePoint(-1.00, 5.00)
            )
        )
    );

    /**
     * Примитив центральная часть Ромашки (нектар/мед).
     */
    public static final Primitive NECTAR_PRIMITIVE = new Primitive(
        new PrimitivePath(
            PrimitiveType.areaLine,
            new ParamColorPoint(new Color(240, 255, 0), 0.0),
            new ParamColorPoint(new Color(200, 230, 0), 0.0),
            new PrimitiveAddon(EvolutionConstants.NECTAR_KEY, EvolutionConstants.NECTAR_KEY, 0, 0, 0.005, 0.005,
                new PrimitivePoint(0.40, 1.00),
                new PrimitivePoint(1.00, 0.40),
                new PrimitivePoint(1.00, -0.40),
                new PrimitivePoint(0.40, -1.00),
                new PrimitivePoint(-0.40, -1.00),
                new PrimitivePoint(-1.00, -0.40),
                new PrimitivePoint(-1.00, 0.40),
                new PrimitivePoint(-0.40, 1.00)
            )
        )
    );

    private DrawPrimitive() {
    }

    /**
     *
     * @param prim .
     * @param iX   .
     * @param iY   .
     * @param prop   .
     * @return        .
     */
    public static Polygon createPolygon(
        final Primitive prim, final Map<String, Double> prop, final int iX, final int iY
    ) {
        final Polygon polygon = new Polygon();
        PrimitivePoint[] points = null;
        for (PrimitivePath path : prim.getPaths()) {
            final Color color = path.getFillColor().calcColor(prop);
            for (PrimitiveAddon addon : path.getAddons()) {
                final double multX = addon.getMultiplierX() * prop.get(addon.getKeyX());
                final double multY = addon.getMultiplierY() * prop.get(addon.getKeyY());
                if (points == null) {
                    points = new PrimitivePoint[addon.getPoints().size()];
                    int  i = 0;
                    for (PrimitivePoint pnt : addon.getPoints()) {
                        final double x = (pnt.getX() - addon.getCenterX()) * multX;
                        final double y = (pnt.getY() - addon.getCenterY()) * multY;
                        points[i] = new PrimitivePoint(x, y);
                        i++;
                    }
                } else {
                    int  i = 0;
                    for (PrimitivePoint pnt : addon.getPoints()) {
                        points[i].incX((pnt.getX() - addon.getCenterX()) * multX);
                        points[i].incY((pnt.getY() - addon.getCenterY()) * multY);
                        i++;
                    }
                }
            }
        }
        for (PrimitivePoint point : points) {
            polygon.addPoint(iX + (int) point.getX(), iY + (int) point.getY());
        }
        return polygon;
    }

    /**
     *
     * @param gr   .
     * @param prim .
     * @param iX   .
     * @param iY   .
     * @param prop .
     */
    public static void drawPolygon(
        final Graphics gr, final Primitive prim, final Map<String, Double> prop, final int iX, final int iY
    ) {
        for (PrimitivePath path : prim.getPaths()) {
            PrimitivePoint[] points = null;
            path.getType();
            for (PrimitiveAddon addon : path.getAddons()) {
                final double multX = addon.getMultiplierX() * prop.get(addon.getKeyX());
                final double multY = addon.getMultiplierY() * prop.get(addon.getKeyY());
                if (points == null) {
                    points = new PrimitivePoint[addon.getPoints().size()];
                    int  i = 0;
                    for (PrimitivePoint pnt : addon.getPoints()) {
                        final double x = (pnt.getX() - addon.getCenterX()) * multX;
                        final double y = (pnt.getY() - addon.getCenterY()) * multY;
                        points[i] = new PrimitivePoint(x, y);
                        i++;
                    }
                } else {
                    int  i = 0;
                    for (PrimitivePoint pnt : addon.getPoints()) {
                        points[i].incX((pnt.getX() - addon.getCenterX()) * multX);
                        points[i].incY((pnt.getY() - addon.getCenterY()) * multY);
                        i++;
                    }
                }
            }
/*
            Polygon polygon = DrawPrimitive.createPolygon(DrawPrimitive.PIG_PRIMITIVE, prop, x, y);
            if (getState() == stress) {
                gr.setFillColor(STRESS_COLOR);
            } else {
                gr.setFillColor(calcColor(YOUNG_COLOR, OLD_COLOR, age / maximalAge));
            }
            gr.fillPolygon(polygon);
            if (isSelected()) {
                gr.setFillColor(SELECTED_COLOR);
                gr.drawPolygon(polygon);
            }
*/
            if (points != null) {
                final boolean selected = prop.get("selected") != null;
                final boolean stress = prop.get("stress") != null;
                final Polygon polygon = new Polygon();
                for (PrimitivePoint point : points) {
                    polygon.addPoint(iX + (int) point.getX(), iY + (int) point.getY());
                }
                final PrimitiveType type = path.getType();
                if (type == PrimitiveType.area || type == PrimitiveType.areaLine) {
                    if (stress) {
                        gr.setColor(STRESS_COLOR);
                    } else {
                        gr.setColor(path.getFillColor().calcColor(prop));
                    }
                    gr.fillPolygon(polygon);
                }
                if (type == PrimitiveType.areaLine || type == PrimitiveType.line || selected) {
                    if (selected) {
                        gr.setColor(SELECTED_COLOR);
                    } else {
                        gr.setColor(path.getLineColor().calcColor(prop));
                    }
                    if (type == PrimitiveType.line) {
                        gr.drawPolyline(polygon.xpoints, polygon.ypoints, polygon.npoints);
                    } else {
                        gr.drawPolygon(polygon);
                    }
                }
            }
        }
    }

}
