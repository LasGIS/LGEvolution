/**
 * @(#)CellHelperTest.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.map;

import com.lasgis.evolution.map.element.Parameters;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * The Class CellHelperTest.
 * @author Laskin
 * @version 1.0
 * @since 07.03.13 2:25
 */
@Slf4j
@Test
public class CellHelperTest {

    @Test
    public void testGetSignDegree() throws Exception {

    }

    @Test
    public void testGetCell() throws Exception {

    }

    @Test
    public void testGetCell1() throws Exception {

    }

    @Test
    public void testGetCellLatitude() throws Exception {

    }

    @Test
    public void testGetCellLongitude() throws Exception {

    }

    /** fgh */
    private static final Object[][] NEAR_POINTS_OBJECTS = {
        {0, 0, 0.000},
        {1, 0, 1.000}, {-1,  0, 1.000}, {0, -1, 1.000}, { 0, 1, 1.000},
        {1, 1, 1.414}, {-1, -1, 1.414}, {1, -1, 1.414}, {-1, 1, 1.414},
        {2, 0, 2.000}, {-2,  0, 2.000}, {0, -2, 2.000}, { 0, 2, 2.000},
        {2, 1, 2.236}, {-2, -1, 2.236}, {1, -2, 2.236}, {-1, 2, 2.236},
        {1, 2, 2.236}, {-1, -2, 2.236}, {2, -1, 2.236}, {-2, 1, 2.236},
        {2, 2, 2.828}, {-2, -2, 2.828}, {2, -2, 2.828}, {-2, 2, 2.828},
        {3, 0, 3.000}, {-3,  0, 3.000}, {0, -3, 3.000}, { 0, 3, 3.000},
        {3, 1, 3.162}, {-3, -1, 3.162}, {1, -3, 3.162}, {-1, 3, 3.162},
        {1, 3, 3.162}, {-1, -3, 3.162}, {3, -1, 3.162}, {-3, 1, 3.162},
        {3, 2, 3.606}, {-3, -2, 3.606}, {2, -3, 3.606}, {-2, 3, 3.606},
        {2, 3, 3.606}, {-2, -3, 3.606}, {3, -2, 3.606}, {-3, 2, 3.606},
        {4, 0, 4.000}, {-4,  0, 4.000}, {0, -4, 4.000}, { 0, 4, 4.000},
        {4, 1, 4.124}, {-4, -1, 4.124}, {1, -4, 4.124}, {-1, 4, 4.124},
        {1, 4, 4.124}, {-1, -4, 4.124}, {4, -1, 4.124}, {-4, 1, 4.124},
        {3, 3, 4.243}, {-3, -3, 4.243}, {3, -3, 4.243}, {-3, 3, 4.243},
        {4, 2, 4.472}, {-4, -2, 4.472}, {2, -4, 4.472}, {-2, 4, 4.472},
        {2, 4, 4.472}, {-2, -4, 4.472}, {4, -2, 4.472}, {-4, 2, 4.472},
        {4, 3, 5.000}, {-4, -3, 5.000}, {3, -4, 5.000}, {-3, 4, 5.000},
        {3, 4, 5.000}, {-3, -4, 5.000}, {4, -3, 5.000}, {-4, 3, 5.000},
        {5, 0, 5.000}, {-5,  0, 5.000}, {0, -5, 5.000}, { 0, 5, 5.000},
        {5, 1, 5.099}, {-5, -1, 5.099}, {1, -5, 5.099}, {-1, 5, 5.099},
        {1, 5, 5.099}, {-1, -5, 5.099}, {5, -1, 5.099}, {-5, 1, 5.099},
        {5, 2, 5.385}, {-5, -2, 5.385}, {2, -5, 5.385}, {-2, 5, 5.385},
        {2, 5, 5.385}, {-2, -5, 5.385}, {5, -2, 5.385}, {-5, 2, 5.385},
        {4, 4, 5.657}, {-4, -4, 5.657}, {4, -4, 5.657}, {-4, 4, 5.657},
        {5, 3, 5.831}, {-5, -3, 5.831}, {3, -5, 5.831}, {-3, 5, 5.831},
        {3, 5, 5.831}, {-3, -5, 5.831}, {5, -3, 5.831}, {-5, 3, 5.831},
        {6, 0, 6.000}, {-6,  0, 6.000}, {0, -6, 6.000}, { 0, 6, 6.000},
        {6, 1, 6.082}, {-6, -1, 6.082}, {1, -6, 6.082}, {-1, 6, 6.082},
        {1, 6, 6.082}, {-1, -6, 6.082}, {6, -1, 6.082}, {-6, 1, 6.082},
        {6, 2, 6.324}, {-6, -2, 6.324}, {2, -6, 6.324}, {-2, 6, 6.324},
        {2, 6, 6.324}, {-2, -6, 6.324}, {6, -2, 6.324}, {-6, 2, 6.324},
        {5, 4, 6.403}, {-5, -4, 6.403}, {4, -5, 6.403}, {-4, 5, 6.403},
        {4, 5, 6.403}, {-4, -5, 6.403}, {5, -4, 6.403}, {-5, 4, 6.403},
        {6, 3, 6.708}, {-6, -3, 6.708}, {3, -6, 6.708}, {-3, 6, 6.708},
        {3, 6, 6.708}, {-3, -6, 6.708}, {6, -3, 6.708}, {-6, 3, 6.708},
        {7, 0, 7.000}, {-7,  0, 7.000}, {0, -7, 7.000}, { 0, 7, 7.000},
        {5, 5, 7.071}, {-5, -5, 7.071}, {5, -5, 7.071}, {-5, 5, 7.071},
    };

    @Test
    public void testGetNearPointsZero() throws Exception {
        NearPoint[] points = CellHelper.getNearPoints(38, true);
        assertEquals(points.length, 153);
        for (int i = 0; i < points.length; i++) {
            NearPoint point = points[i];
            Object[] objects = NEAR_POINTS_OBJECTS[i];
            String message = "Actual := " + point.toString()
                + ", but expected ( " + objects[0] + " : " + objects[1] + " )";
            assertEquals(point.getX(), objects[0], message);
            assertEquals(point.getY(), objects[1], message);
            assertEquals(point.getDistance(), (double) objects[2], 0.001, message);
            double distance = Math.sqrt(Math.pow((int) objects[0], 2) + Math.pow((int) objects[1], 2));
            assertEquals(point.getDistance(), distance, 0.001, message);
            //log.info(point.toString());
        }
    }

    @Test
    public void testGetNearPoints() throws Exception {
        NearPoint[] points = CellHelper.getNearPoints(38, false);
        assertEquals(points.length, 152);
        for (int i = 0; i < points.length; i++) {
            NearPoint point = points[i];
            Object[] objects = NEAR_POINTS_OBJECTS[i + 1];
            String message = "Actual := " + point.toString()
                + ", but expected ( " + objects[0] + " : " + objects[1] + " )";
            assertEquals(point.getX(), objects[0], message);
            assertEquals(point.getY(), objects[1], message);
            assertEquals(point.getDistance(), (double) objects[2], 0.001, message);
            double distance = Math.sqrt(Math.pow((int) objects[0], 2) + Math.pow((int) objects[1], 2));
            assertEquals(point.getDistance(), distance, 0.001, message);
            //log.info(point.toString());
        }
    }


    @Test
    public void testNormLatitude() throws Exception {
        assertEquals(10.0,
            CellHelper.normLatitude(10.0, Parameters.MAX_LATITUDE / 2 + 9), 0.01
        );
        assertEquals(Parameters.MAX_LATITUDE + 10.0,
            CellHelper.normLatitude(10.0, Parameters.MAX_LATITUDE / 2 + 11), 0.01
        );
    }

    @Test
    public void testNormLongitude() throws Exception {
        assertEquals(10.0,
            CellHelper.normLongitude(10.0, Parameters.MAX_LONGITUDE / 2 + 9.9), 0.01
        );
        assertEquals(Parameters.MAX_LONGITUDE + 10.0,
            CellHelper.normLongitude(10.0, Parameters.MAX_LONGITUDE / 2 + 10.1), 0.01
        );
    }
}
