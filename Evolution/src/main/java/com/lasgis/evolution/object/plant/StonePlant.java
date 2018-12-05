/**
 * @(#)StonePlant.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.plant;

import com.lasgis.evolution.draw.DrawPrimitive;
import com.lasgis.evolution.map.Cell;
import com.lasgis.evolution.object.AbstractPlantBehaviour;
import com.lasgis.evolution.object.PlantBehaviour;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Земля отвечает за 3 субстанции:<br/>
 *  1 - земля или чернозём. Земля нужна для роста растений;<br/>
 *  2 - кал или навоз, это то, что остаётся от животных,
 *  в том числе трупы самих животных;<br/>
 *  3 - сено, это то, что остаётся от растений.<br/>
 *  Кал и сено постепенно превращаются в землю.
 *
 * @author Laskin
 * @version 1.0
 * @since 03.12.12 23:07
 */
public class StonePlant extends AbstractPlant {

    private static final Color ROAD_COLOR = new Color(184, 118, 184);
    private final PlantBehaviour[] subElements = new PlantBehaviour[] {
        new AbstractPlantBehaviour() {
            @Override
            public String getName() {
                return STONE_KEY;
            }

            @Override
            public void drawPlant(final Graphics gr, final Rectangle rec, final Cell cell) {
                final double stone = cell.element(STONE_KEY).value();
                if (stone > 1) {
                    final int x = (int) (rec.getX() + rec.getHeight() / 2);
                    final int y = (int) (rec.getY() + rec.getWidth() / 2);
                    final double cellSize = rec.getWidth();
                    final double massSize = cellSize * stone;
                    final Map<String, Double> prop = new HashMap<>();
                    prop.put("масса", massSize);
                    DrawPrimitive.drawPolygon(gr, DrawPrimitive.STONE_PRIMITIVE, prop, x, y);
                }
            }
        }, new AbstractPlantBehaviour() {
            @Override
            public String getName() {
                return ROAD_KEY;
            }

            @Override
            public void drawPlant(final Graphics gr, final Rectangle rec, final Cell cell) {
                final double road = cell.element(ROAD_KEY).value();
                gr.setColor(ROAD_COLOR);
                if (road <= 100) {
                    final int r2x = (int) (rec.width * road / 100);
                    final int dx = (rec.width - r2x) / 2;
                    final int r2y = (int) (rec.height * road / 100);
                    final int dy = (rec.height - r2y) / 2;
                    gr.fillOval(rec.x + dx, rec.y + dy, r2x, r2y);
                } else {
                    final int r = 200 - (int) road;
                    if (r <= 0) {
                        gr.fillRect(rec.x, rec.y, rec.width, rec.height);
                    } else {
                        final int rx = rec.width * r / 200;
                        final int ry = rec.height * r / 200;
                        gr.fillRoundRect(rec.x, rec.y, rec.width, rec.height, rx, ry);
                    }
                }
            }
        }
    };

    @Override
    public String getName() {
        return OBSTACLE_KEY;
    }

    @Override
    public int getIndex() {
        return 1;
    }

    @Override
    public void drawPlant(final Graphics gr, final Rectangle rec, final Cell cell) {
    }

    @Override
    public void cellProcessing(final Cell cell) {
        final double road = cell.element(ROAD_KEY).value();
        if (road > 50.) {
            cell.element(ROAD_KEY).setValue(50.);
        } else if (road > 0.00001) {
            // случай зарастания дороги
            //final double delta = road / 100;
            cell.element(ROAD_KEY).decValue(0.1);
        }
    }

    @Override
    public PlantBehaviour[] subElements() {
        return subElements;
    }
}
