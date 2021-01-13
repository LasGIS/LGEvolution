/*
 * BarleyPlant.java
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2021 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.plant;

import com.lasgis.evolution.map.Cell;
import com.lasgis.evolution.object.EvolutionConstants;
import lombok.extern.slf4j.Slf4j;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.Arrays;

import static com.lasgis.evolution.object.EvolutionValues.GROUND_PLAN_FACTOR;

/**
 * The Class BarleyPlant (овес или ячмень).
 * @author vladimir.laskin
 * @version 1.0
 */
@Slf4j
public class BarleyPlant extends AbstractPlant {

    private static final Color GREEN = new Color(250, 200, 100);
    private static final Color DARK_GREEN = new Color(180, 160, 80);

    private static final int[] X_TEMP     = {  0,  20,  40,  60,  80, 100};
    private static final int[] Y_TEMP     = {100, 100, 100, 100, 100, 100};
    private static final int[] SIM_Y_TEMP = {  0,  -2,   0,   0,  -3,   0};

    @Override
    public String getName() {
        return EvolutionConstants.BARLEY_PLANT_KEY;
    }

    @Override
    public int getIndex() {
        return 9;
    }

    @Override
    public void drawPlant(final Graphics gr, final Rectangle rec, final Cell cell) {
        final double simCount = cell.element(BARLEY_PLANT_KEY).value();
        if (simCount > 0.9) {
            final Polygon polygon = createPolygon(simCount, rec);
            gr.setColor(GREEN);
            gr.fillPolygon(polygon);
            gr.setColor(DARK_GREEN);
            gr.drawPolyline(polygon.xpoints, polygon.ypoints, polygon.npoints);
        }
    }

    private Polygon createPolygon(final double simCount, final Rectangle rec) {
        final Polygon polygon = new Polygon();
        for (int i = 0; i < X_TEMP.length; i++) {
            polygon.addPoint(
                rec.x + X_TEMP[i] * rec.height / 100,
                (int) (rec.y + (Y_TEMP[i] + SIM_Y_TEMP[i] * simCount) * rec.width / 100)
            );
        }
        return polygon;
    }

    @Override
    public void cellProcessing(final Cell cell) {
        double barley = cell.element(BARLEY_PLANT_KEY).value();
        if (barley > 0.00001) {
            final double plantForbid = cell.plantForbid(Arrays.asList(BARLEY_PLANT_KEY));
            final double obstacle = cell.obstacle();
            final double ground = cell.element(GROUND_KEY).value();

            // факторы, препятствующие росту
            final double forbid = Math.pow(plantForbid, 2) / 10 + Math.pow(barley, 2) / 20 + obstacle * 30;
            // факторы, способствующие росту
            final double favour = ground * 1 + obstacle * 0.5;
            final double balance = (favour - forbid) / 100;
            double incDelta = barley * balance / 30.0;

            // растение забирает сок земли
            cell.element(GROUND_KEY).decValue(barley * GROUND_PLAN_FACTOR);

            if (incDelta > 0) {
/*
                if (incDelta > barley * 0.01) {
                    incDelta = barley * 0.01;
                }
*/
                barley = cell.element(BARLEY_PLANT_KEY).incValue(incDelta);
            } else {
                incDelta = -incDelta * 0.8;
/*
                if (incDelta > barley * 0.01) {
                    incDelta = barley * 0.01;
                }
*/
                barley = cell.element(BARLEY_PLANT_KEY).decValue(incDelta);
                cell.element(HAY_PLANT_KEY).incValue(0.6 * incDelta);
            }

            // расселение овса на соседние ячейки
            final double sets = barley / 100.;
            cell.getCell(-1, 0).element(BARLEY_PLANT_KEY).incValue(sets);
            cell.getCell(1, 0).element(BARLEY_PLANT_KEY).incValue(sets);
            cell.element(BARLEY_PLANT_KEY).decValue(sets * 4);
            cell.getCell(0, 1).element(BARLEY_PLANT_KEY).incValue(sets);
            cell.getCell(0, -1).element(BARLEY_PLANT_KEY).incValue(sets);
        }
    }

}
