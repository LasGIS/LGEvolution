/**
 * @(#)CellIteratorTest.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.map;

import com.lasgis.evolution.panels.ScalableMock;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author Laskin
 * @version 1.0
 * @since 23.01.2011 14:57:11
 */
public class CellIteratorTest {

    private static final Logger LOG = Logger.getLogger(CellIteratorTest.class);

    ScalableMock norm;

    @BeforeMethod
    public void setUp() throws Exception {
        norm = new ScalableMock(
            Matrix.CELL_SIZE * 20 - 10.0,
            Matrix.CELL_SIZE * 10 + 10.0,
            Matrix.CELL_SIZE * 10 + 10.0,
            Matrix.CELL_SIZE * 20 - 10.0
        );
    }

    @AfterMethod
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetInterval() throws Exception {
        CellIterator interval = CellIterator.getInterval(norm);
        assertEquals(100, interval.getCount());
        for (int indX = 10; indX < 20; indX++) {
            for (int indY = 10; indY < 20; indY++) {
                assertTrue(interval.hasNext());
                Cell cell = interval.next();
                assertNotNull(cell);
                assertEquals(indX, cell.getIndX());
                assertEquals(indY, cell.getIndY());
//                log.info("IndX = " + cell.getIndX() + "; IndY = " + cell.getIndY() + ";");
            }
        }
        assertFalse(interval.hasNext());
    }

}
