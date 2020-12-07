/**
 * @(#)CamomilePlant.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.plant;

import com.lasgis.evolution.draw.DrawPrimitive;
import com.lasgis.evolution.map.Cell;
import com.lasgis.evolution.map.CellHelper;
import com.lasgis.evolution.map.NearPoint;
import com.lasgis.evolution.object.AbstractPlantBehaviour;
import com.lasgis.evolution.object.EvolutionConstants;
import com.lasgis.evolution.object.PlantBehaviour;
import lombok.extern.slf4j.Slf4j;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Простой класс растений (Ромашка).
 *
 * @author Laskin
 * @version 1.0
 * @since 02.12.12 15:56
 */
@Slf4j
public class ChamomilePlant extends AbstractPlant {

    @Override
    public String getName() {
        return EvolutionConstants.CHAMOMILE_KEY;
    }

    @Override
    public int getIndex() {
        return 10;
    }

    @Override
    public PlantBehaviour[] subElements() {
        return subElements;
    }

    private final PlantBehaviour[] subElements = new PlantBehaviour[]{
        new AbstractPlantBehaviour() {
            @Override
            public String getName() {
                return CHAMOMILE_LEAF_KEY;
            }

            @Override
            public void drawPlant(final Graphics gr, final Rectangle rec, final Cell cell) {
                final double chamomileLeaf = cell.element(CHAMOMILE_LEAF_KEY).value();
                if (chamomileLeaf > 0.9) {
                    final int x = (int) (rec.getX() + rec.getHeight() / 2);
                    final int y = (int) (rec.getY() + rec.getWidth() / 2);
                    final double cellSize = rec.getWidth();
                    final double massSize = cellSize * chamomileLeaf;
                    final Map<String, Double> prop = new HashMap<>();
                    prop.put("масса", massSize);
                    DrawPrimitive.drawPolygon(gr, DrawPrimitive.CHAMOMILE_LEAF_PRIMITIVE, prop, x, y);
                }
            }

            @Override
            public void cellProcessing(final Cell cell) {
                double leaf = cell.element(CHAMOMILE_LEAF_KEY).value();
                if (leaf > 0.00001) {
                    final double plantForbid = cell.plantForbid(
                        Arrays.asList(CHAMOMILE_LEAF_KEY, CHAMOMILE_FLOWER_KEY, NECTAR_KEY)
                    );
                    final double obstacle = cell.obstacle();
                    final double ground = cell.element(GROUND_KEY).value();

                    // факторы, препятствующие росту
                    final double forbid = Math.pow(plantForbid, 2) / 10 + Math.pow(leaf, 2) / 20 + obstacle * 30;
                    // факторы, способствующие росту
                    final double favour = ground * 1;
                    final double balance = (favour - forbid) / 100;
                    final double incDelta = leaf * balance / 30.0;

                    // растение забирает сок земли
                    cell.element(GROUND_KEY).decValue(leaf / GROUND_PLAN_DELIMITER);

                    if (incDelta > 0) {
                        leaf = cell.element(CHAMOMILE_LEAF_KEY).incValue(incDelta);
                    } else {
                        leaf = cell.element(CHAMOMILE_LEAF_KEY).decValue(0.7 * -incDelta);
                        cell.element(HAY_PLANT_KEY).incValue(0.4 * -incDelta);
                    }

                    // расселение травы на соседние ячейки
                    final double sets = leaf / 100.;
                    cell.getCell(-1, 0).element(CHAMOMILE_LEAF_KEY).incValue(sets);
                    cell.getCell(1, 0).element(CHAMOMILE_LEAF_KEY).incValue(sets);
                    cell.element(CHAMOMILE_LEAF_KEY).decValue(sets * 4);
                    cell.getCell(0, 1).element(CHAMOMILE_LEAF_KEY).incValue(sets);
                    cell.getCell(0, -1).element(CHAMOMILE_LEAF_KEY).incValue(sets);
                }
            }
        },
        new AbstractPlantBehaviour() {
            @Override
            public String getName() {
                return CHAMOMILE_FLOWER_KEY;
            }

            @Override
            public void drawPlant(final Graphics gr, final Rectangle rec, final Cell cell) {
                final double chamomileLeaf = cell.element(CHAMOMILE_FLOWER_KEY).value();
                if (chamomileLeaf > 0.9) {
                    final int x = (int) (rec.getX() + rec.getHeight() / 2);
                    final int y = (int) (rec.getY() + rec.getWidth() / 2);
                    final double cellSize = rec.getWidth();
                    final Map<String, Double> prop = new HashMap<>();
                    prop.put("цвет", cellSize * chamomileLeaf);
                    DrawPrimitive.drawPolygon(gr, DrawPrimitive.CHAMOMILE_FLOWER_PRIMITIVE, prop, x, y);
                }
            }

            @Override
            public void cellProcessing(final Cell cell) {
                final double leaf = cell.element(CHAMOMILE_LEAF_KEY).value();
                final double flower = cell.element(CHAMOMILE_FLOWER_KEY).value();
                if (flower > 0.0001) {
                    final NearPoint[] nearPoints = CellHelper.getNearPoints(18, true);
                    double leafs = 0.0;
                    for (NearPoint point : nearPoints) {
                        final Cell cellPoint = cell.getCell(point);
                        final double factor = (point.getDistance() > 1 ? 1 / Math.pow(point.getDistance(), 2) : 1);
                        final double leafPoint = cellPoint.element(CHAMOMILE_LEAF_KEY).value();
                        if (leafPoint > flower * factor) {
                            cellPoint.element(CHAMOMILE_LEAF_KEY).decValue((leafPoint - flower * factor) / 50.);
                            leafs += (leafPoint - flower * factor) / 50.;
                        }
                    }
                    if (leafs > 0) {
                        cell.element(CHAMOMILE_FLOWER_KEY).incValue(leafs / 50.0);
                    }
                } else if (leaf > 50.0 && Math.random() < .1) {
                    final NearPoint[] nearPoints = CellHelper.getNearPoints(30, true);
                    double flowers = 0.0;
                    for (NearPoint point : nearPoints) {
                        flowers += cell.getCell(point).element(CHAMOMILE_FLOWER_KEY).value();
                    }
                    if (flowers < 0.0001) {
                        cell.element(CHAMOMILE_FLOWER_KEY).incValue();
                    }
                }
            }
        },
        new AbstractPlantBehaviour() {
            @Override
            public String getName() {
                return NECTAR_KEY;
            }

            @Override
            public void drawPlant(final Graphics gr, final Rectangle rec, final Cell cell) {
                final double honey = cell.element(NECTAR_KEY).value();
                if (honey > 0.9) {
                    final int x = (int) (rec.getX() + rec.getHeight() / 2);
                    final int y = (int) (rec.getY() + rec.getWidth() / 2);
                    final double cellSize = rec.getWidth();
                    final Map<String, Double> prop = new HashMap<>();
                    prop.put(NECTAR_KEY, cellSize * honey);
                    DrawPrimitive.drawPolygon(gr, DrawPrimitive.NECTAR_PRIMITIVE, prop, x, y);
                }
            }

            @Override
            public void cellProcessing(final Cell cell) {
                final double flower = cell.element(CHAMOMILE_FLOWER_KEY).value();
                if (flower > 0.0001) {
                    //cell.element(NECTAR_KEY).setValue(0.0);
                    cell.element(NECTAR_KEY).incValue(flower / 500.0);
                    cell.element(CHAMOMILE_FLOWER_KEY).decValue(flower / 50.0);
                }
            }
        }
    };

}
