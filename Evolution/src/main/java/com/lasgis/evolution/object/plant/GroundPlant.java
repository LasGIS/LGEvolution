/*
 * GroundPlant.java
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2021 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.plant;

import com.lasgis.evolution.map.Cell;
import com.lasgis.evolution.object.AbstractPlantBehaviour;
import com.lasgis.evolution.object.PlantBehaviour;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import static com.lasgis.evolution.object.EvolutionValues.MAX_GROUND_VALUE;

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
public class GroundPlant extends AbstractPlant {

    private static final Color EXCREMENT_COLOR = new Color(200, 130, 0);
    private static final Color HAY_COLOR = new Color(250, 230, 100);
    private static final int[] HAY_PNT = {2, 1, 8, 5, 0, 3, 6, 2};
    private final PlantBehaviour[] subElements = new PlantBehaviour[] {
        new AbstractPlantBehaviour() {
            @Override
            public String getName() {
                return GROUND_KEY;
            }
            @Override
            public void drawPlant(final Graphics gr, final Rectangle rec, final Cell cell) {
                int ground = 255 - (int) cell.element(GROUND_KEY).value();
                if (ground < 0) {
                    ground = 0;
                }
                gr.setColor(new Color(ground, ground, ground));
                gr.fillRect(rec.x, rec.y, rec.width, rec.height);
            }
        }, new AbstractPlantBehaviour() {
            @Override
            public String getName() {
                return EXCREMENT_KEY;
            }
            @Override
            public void drawPlant(final Graphics gr, final Rectangle rec, final Cell cell) {
                // рисуем говно
                final double excrement = cell.element(EXCREMENT_KEY).value();
                if (excrement > 0.9) {
                    gr.setColor(EXCREMENT_COLOR);
                    if (excrement <= 100) {
                        final int r2x = (int) (rec.width * excrement / 100);
                        final int dx = (rec.width - r2x) / 2;
                        final int r2y = (int) (rec.height * excrement / 100);
                        final int dy = (rec.height - r2y) / 2;
                        gr.fillOval(rec.x + dx, rec.y + dy, r2x, r2y);
                    } else {
                        final int r = 200 - (int) excrement;
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
        }, new AbstractPlantBehaviour() {
            @Override
            public String getName() {
                return HAY_PLANT_KEY;
            }
            @Override
            public void drawPlant(final Graphics gr, final Rectangle rec, final Cell cell) {
                // рисуем сено
                final double hay = cell.element(HAY_PLANT_KEY).value();
                if (hay > 0.9) {
                    int i = 0;
                    final double dx = rec.width / 100.;
                    final double dy = rec.height / 100.;
                    gr.setColor(HAY_COLOR);
                    for (int x = 0; x < 100; x += 7) {
                        for (int y = 0; y < 100; y += 5) {
                            gr.drawLine((int) (rec.x + (x + HAY_PNT[0]) * dx), (int) (rec.y + (y + HAY_PNT[1]) * dy),
                                (int) (rec.x + (x + HAY_PNT[2]) * dx), (int) (rec.y + (y + HAY_PNT[3]) * dy));
                            if (++i > hay) {
                                break;
                            }
                            gr.drawLine((int) (rec.x + (x + HAY_PNT[4]) * dx), (int) (rec.y + (y + HAY_PNT[5]) * dy),
                                (int) (rec.x + (x + HAY_PNT[6]) * dx), (int) (rec.y + (y + HAY_PNT[7]) * dy));
                            if (++i > hay) {
                                break;
                            }
                        }
                        if (i > hay) {
                            break;
                        }
                    }
                }
            }
        }
    };

    @Override
    public String getName() {
        return GROUND_KEY;
    }

    @Override
    public int getIndex() {
        return 0;
    }

    @Override
    public void cellProcessing(final Cell cell) {
        double excrement = cell.element(EXCREMENT_KEY).value();
        final double random = Math.random();

        // случай растекания говна.
        if (excrement > 50) {
            //Cell[][] cells = cell.getCells(3);
            final double dx = (excrement - 50) / 12;
            cell.getCell(-1, -1).element(EXCREMENT_KEY).incValue(dx);
            cell.getCell(-1, 0).element(EXCREMENT_KEY).incValue(dx * 2);
            cell.getCell(-1, 1).element(EXCREMENT_KEY).incValue(dx);
            cell.getCell(0, -1).element(EXCREMENT_KEY).incValue(dx * 2);
            excrement = cell.element(EXCREMENT_KEY).decValue(dx * 12);
            cell.getCell(0, 1).element(EXCREMENT_KEY).incValue(dx * 2);
            cell.getCell(1, -1).element(EXCREMENT_KEY).incValue(dx);
            cell.getCell(1, 0).element(EXCREMENT_KEY).incValue(dx * 2);
            cell.getCell(1, 1).element(EXCREMENT_KEY).incValue(dx);
        }

        // случай преобразования экскрементов в землю
        if (excrement > 0.00001) {
            final double delta = excrement / 20;
            if (excrement >= 20) {
                cell.element(EXCREMENT_KEY).decValue(delta);
                cell.element(GROUND_KEY).incValue(delta);
            } else {
                cell.element(EXCREMENT_KEY).decValue(1);
                cell.element(GROUND_KEY).incValue(1);
            }
        }

        // случай преобразования сена в землю
        final double hay = cell.element(HAY_PLANT_KEY).value();
        if (hay > 0.00001 && hay > random * 20) {
            cell.element(HAY_PLANT_KEY).decValue();
            cell.element(GROUND_KEY).incValue();
        }

        final double ground = cell.element(GROUND_KEY).value();
        if (ground > MAX_GROUND_VALUE) {
            cell.element(GROUND_KEY).setValue(MAX_GROUND_VALUE);
        }

        // случай распространения земли.
        if (ground > 10) {
            sliding(cell, GROUND_KEY, 10);
        }
    }

    @Override
    public PlantBehaviour[] subElements() {
        return subElements;
    }

}
