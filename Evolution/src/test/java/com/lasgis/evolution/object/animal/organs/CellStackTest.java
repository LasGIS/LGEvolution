/**
 * @(#)CellStackTest.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.organs;

import com.lasgis.evolution.map.CellHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CellStackTest {

    @Test public void testAdd() throws Exception {
        final CellStack stack = new CellStack(3);
        Assert.assertEquals(stack.getSize(), 0);
        stack.add(CellHelper.getCell(1, 1));
        Assert.assertEquals(stack.getSize(), 1);
        stack.add(CellHelper.getCell(1, 2));
        Assert.assertEquals(stack.getSize(), 2);
        stack.add(CellHelper.getCell(1, 3));
        Assert.assertEquals(stack.getSize(), 3);
        stack.add(CellHelper.getCell(1, 4));
        Assert.assertEquals(stack.getSize(), 3);
        stack.del(CellHelper.getCell(1, 3));
        Assert.assertEquals(stack.getSize(), 2);
        stack.add(CellHelper.getCell(1, 2));
        Assert.assertEquals(stack.getSize(), 2);
        stack.add(CellHelper.getCell(1, 5));
        Assert.assertEquals(stack.getSize(), 3);
        stack.del();
        Assert.assertEquals(stack.getSize(), 2);
        Assert.assertFalse(stack.contain(CellHelper.getCell(1, 1)));
        Assert.assertTrue(stack.contain(CellHelper.getCell(1, 2)));
        Assert.assertFalse(stack.contain(CellHelper.getCell(1, 3)));
        Assert.assertFalse(stack.contain(CellHelper.getCell(1, 4)));
        Assert.assertTrue(stack.contain(CellHelper.getCell(1, 5)));
    }

    @Test public void testDel() throws Exception {

    }

    @Test public void testDel1() throws Exception {

    }
}