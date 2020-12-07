/**
 * @(#)GrassPlant.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.plant;

import com.lasgis.evolution.map.Cell;
import com.lasgis.evolution.object.EvolutionConstants;
import lombok.extern.slf4j.Slf4j;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Collections;

/**
 * Простой класс растений.
 *
 * @author Laskin
 * @version 1.0
 * @since 02.12.12 15:56
 */
@Slf4j
public class GrassPlant extends AbstractPlant {

    private static final Color GREEN = new Color(0, 250, 100);
    private static final Color DARK_GREEN = new Color(20, 120, 80);

    @Override
    public String getName() {
        return EvolutionConstants.GRASS_PLANT_KEY;
    }

    @Override
    public int getIndex() {
        return 10;
    }

    @Override
    public void drawPlant(final Graphics gr, final Rectangle rec, final Cell cell) {
        final double grassCount = cell.element(GRASS_PLANT_KEY).value();
        if (grassCount > 0.9) {
            final int[] x = {rec.x, rec.x + rec.height / 2, rec.x + rec.height, rec.x};
            final int[] y = {rec.y + rec.width, (int) (rec.y + rec.width - rec.width * grassCount / 100),
                rec.y + rec.width, rec.y + rec.width};
            gr.setColor(GREEN);
            gr.fillPolygon(x, y, 4);
            gr.setColor(DARK_GREEN);
            gr.drawPolyline(x, y, 4);
        }
    }

    @Override
    public void cellProcessing(final Cell cell) {
        double grass = cell.element(GRASS_PLANT_KEY).value();
        if (grass > 0.00001) {
            final double plantForbid = cell.plantForbid(Collections.singletonList(GRASS_PLANT_KEY));
            final double obstacle = cell.obstacle();
            final double ground = cell.element(GROUND_KEY).value();

            // факторы, препятствующие росту
            final double forbid = Math.pow(plantForbid, 2) / 10 + Math.pow(grass, 2) / 20 + obstacle * 30;
            // факторы, способствующие росту
            final double favour = ground * 1;
            final double balance = (favour - forbid) / 100;
            final double incDelta = grass * balance / 30.0;

            // растение забирает сок земли
            cell.element(GROUND_KEY).decValue(grass / GROUND_PLAN_DELIMITER);

            if (incDelta > 0) {
                grass = cell.element(GRASS_PLANT_KEY).incValue(incDelta);
            } else {
                grass = cell.element(GRASS_PLANT_KEY).decValue(0.7 * -incDelta);
                cell.element(HAY_PLANT_KEY).incValue(0.4 * -incDelta);
            }

            // расселение травы на соседние ячейки
            final double sets = grass / 100.;
            cell.getCell(-1, 0).element(GRASS_PLANT_KEY).incValue(sets);
            cell.getCell(1, 0).element(GRASS_PLANT_KEY).incValue(sets);
            cell.element(GRASS_PLANT_KEY).decValue(sets * 4);
            cell.getCell(0, 1).element(GRASS_PLANT_KEY).incValue(sets);
            cell.getCell(0, -1).element(GRASS_PLANT_KEY).incValue(sets);
        }
    }

}
