/**
 * @(#)DoUntilTest.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.routines.operator;

import com.lasgis.evolution.object.animal.TestAnimal;
import com.lasgis.evolution.object.animal.TestAnimalManager;
import com.lasgis.evolution.object.animal.brain.Param;
import com.lasgis.evolution.object.animal.brain.routines.TestOperator;
import com.lasgis.evolution.object.animal.brain.routines.TestRoutine;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.script.SimpleBindings;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class DoUntilTest {

    private DoWhile doWhile;
    private final TestRoutine routine = new TestRoutine();
    private final TestOperator check = new TestOperator();
    private final Param checkParam = Param.createOperator(check);

    @BeforeClass()
    public void setUp() throws Exception {
        SimpleBindings param = new SimpleBindings();
        doWhile = new DoWhile(new TestAnimal(1, 1, new TestAnimalManager()), param);
    }

    @AfterClass
    public void tearDown() throws Exception {

    }

    @Test(invocationCount = 2)
    public void testCheckByRoutine() throws Exception {
        doWhile.setCheck(checkParam);
        doWhile.setRoutine(routine);
        check.setRetValue(true);
        check.setCount(0);
        routine.setRetValue(false);
        routine.setCount(0);
        // start routines
        assertFalse(doWhile.act());
        assertEquals(routine.getCount(), 1);
        assertEquals(check.getCount(), 0);
        routine.setRetValue(true);
        assertFalse(doWhile.act());
        assertEquals(routine.getCount(), 2);
        assertEquals(check.getCount(), 1);
        assertFalse(doWhile.act());
        assertEquals(routine.getCount(), 3);
        assertEquals(check.getCount(), 2);
        check.setRetValue(false);
        assertTrue(doWhile.act());
        assertEquals(routine.getCount(), 4);
        assertEquals(check.getCount(), 3);
    }

}