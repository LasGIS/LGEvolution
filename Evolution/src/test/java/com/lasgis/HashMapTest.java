/**
 * @(#)HashMapTest.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * The Class HashMapTest.
 * @author vladimir.laskin
 * @version 1.0
 */
@Slf4j
public class HashMapTest {

    private static final Logger LOG = Logger.getLogger(HashMapTest.class);

    @BeforeMethod
    public void setUp() throws Exception {
    }

    @AfterMethod
    public void tearDown() throws Exception {
    }

    @Test(enabled = false)
    public void testGetInterval() throws Exception {
        Method hash = HashMap.class.getDeclaredMethod("hash", int.class);
        hash.setAccessible(true);
        for (int i = 0; i < 100; i++) {
            log.info("hash(" + i + ") = " + hash.invoke(null, i) + "; ");
        }
    }
}
