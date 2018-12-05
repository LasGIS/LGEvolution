/**
 * @(#)ElementTest.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.map;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import static org.testng.Assert.assertEquals;

/**
 * The Class ElementTest.
 * @author vladimir.laskin
 * @version 1.0
 */
@Slf4j
public class ElementTest {

    private static final Logger LOG = Logger.getLogger(ElementTest.class);
    private static final double DELTA = 0.0000001;

    Element element;

    @BeforeMethod
    public void setUp() throws Exception {
        element = new Element();
        element.setValue(22);
    }

    @Test
    public void testValue() throws Exception {
        assertEquals(22.0, element.value(), DELTA);
    }

    @Test
    public void testSetValue() throws Exception {
        assertEquals(33.0, element.setValue(33), DELTA);
        assertEquals(33.0, element.value(), DELTA);
    }

    @Test
    public void testIncOne() throws Exception {
        assertEquals(23.0, element.incValue(), DELTA);
    }

    @Test
    public void testIncValue() throws Exception {
        assertEquals(32.0, element.incValue(10), DELTA);
    }

    @Test
    public void testIncDoubleValue() throws Exception {
        double val = 0;
        for (int i = 0; i < 1000; i++) {
            val = element.incValue(1.1);
        }
        assertEquals(1100.0, val - 22.0, 30.0);
        log.info("IncDouble: " + val + " ~= " + (1100 + 22));
    }

    @Test
    public void testDecOne() throws Exception {
        assertEquals(21.0, element.decValue(), DELTA);
        assertEquals(0.0, element.setValue(0), DELTA);
        assertEquals(0.0, element.decValue(), DELTA);
    }

    @Test
    public void testDecValue() throws Exception {
        assertEquals(12.0, element.decValue(10.0), DELTA);
        assertEquals(0.0, element.decValue(20.0), DELTA);
    }

    @Test
    public void testDecDoubleValue() throws Exception {
        element.setValue(3000);
        double val = 0;
        for (int i = 0; i < 1000; i++) {
            val = element.decValue(1.2);
        }
        assertEquals(1200., 3000. - val, 30.0);
        log.info("DecDouble: " + val + " ~= " + (3000 - 1200));
    }
}
