/**
 * @(#)MeatPlant.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.plant;

import com.lasgis.evolution.map.Cell;
import com.lasgis.evolution.object.EvolutionConstants;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * Это мясо. Т.е. то, что остаётся от животного. Его можно есть...
 * @author vladimir.laskin
 * @version 1.0
 */
@Slf4j
public class MeatPlant extends AbstractPlant {

    private static final Color RED = new Color(255, 0, 0);
    private static final Color DARK_RED = new Color(180, 0, 0);
    private static final Color DARK_DIED = new Color(100, 0, 0);

    private static final int[] X_TEMP     = { 50, 50, 50, 50, 50, 50, 50, 50, 50};
    private static final int[] Y_TEMP     = { 50, 50, 50, 50, 50, 50, 50, 50, 50};
    private static final int[] SIM_X_TEMP = { -1, -2,  0,  2,  1,  2,  0, -2, -1};
    private static final int[] SIM_Y_TEMP = {  0, -2, -1, -2,  0,  2,  1,  2,  0};

    private static final String[] TARGET_KEY = {
        PIG_TARGET_KEY,
        RABBIT_TARGET_KEY,
        MOUSE_TARGET_KEY,
        VULTURE_TARGET_KEY
    };

    @Override
    public String getName() {
        return EvolutionConstants.MEAT_PLANT_KEY;
    }

    @Override
    public int getIndex() {
        return 1000;
    }

    @Override
    public void drawPlant(final Graphics gr, final Rectangle rec, final Cell cell) {
        final double simCount = cell.element(MEAT_PLANT_KEY).value();
        if (simCount > 0.9) {
            final Polygon polygon = createPolygon(simCount / 5, rec);
            gr.setColor(RED);
            gr.fillPolygon(polygon);
            gr.setColor(DARK_RED);
            gr.drawPolyline(polygon.xpoints, polygon.ypoints, polygon.npoints);
        }
    }

    private Polygon createPolygon(final double simCount, final Rectangle rec) {
        final Polygon polygon = new Polygon();
        for (int i = 0; i < X_TEMP.length; i++) {
            polygon.addPoint(
                rec.x + (int) ((X_TEMP[i] + SIM_X_TEMP[i] * simCount) * rec.height / 100),
                rec.y + (int) ((Y_TEMP[i] + SIM_Y_TEMP[i] * simCount) * rec.width / 100)
            );
        }
        return polygon;
    }

    @Override
    public void cellProcessing(final Cell cell) {
        final double meat = cell.element(MEAT_PLANT_KEY).value();
        if (meat > 0.0) {
            cell.element(MEAT_PLANT_KEY).decValue(1);
            cell.element(EXCREMENT_KEY).incValue(1);
        }
        for (String targetKey : TARGET_KEY) {
            final double target = cell.element(targetKey).value();
            if (target > 0.00001) {
                cell.element(targetKey).decValue(1);
            }
        }
    }

}
