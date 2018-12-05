/**
 * @(#)ParamTest.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ParamTest {
    @DataProvider
    public Object[][] testToString() {
        return new Object[][] {
            {Param.createString("First String param"), "\"First String param\""},
            {Param.createKey("firstKey"), "firstKey"},
            {Param.createLink("subKey", Param.createKey("firstKey")), "subKey.firstKey"},
            {Param.createLink("subKey", Param.createString("firstKey")), "subKey.\"firstKey\""},
            {Param.createLink("sub1", Param.createLink("sub2", Param.createKey("firstKey"))), "sub1.sub2.firstKey"},
            {Param.createDouble(3.1415926), "3.1415926"},
            {Param.createInteger(31415926), "31415926"},
         };
    }

    @Test(dataProvider = "testToString")
    public void testToString(final Param param, final String value) throws Exception {
        assertEquals(param.toString(), value);
    }
}