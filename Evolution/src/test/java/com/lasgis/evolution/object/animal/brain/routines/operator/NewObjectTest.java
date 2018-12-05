/**
 * @(#)NewObjectTest.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.routines.operator;

import com.lasgis.evolution.object.animal.TestAnimal;
import com.lasgis.evolution.object.animal.TestAnimalManager;
import com.lasgis.evolution.object.animal.brain.Param;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.script.SimpleBindings;

public class NewObjectTest {

    private final TestAnimal animal = new TestAnimal(1, 1, new TestAnimalManager());
    private final SimpleBindings param = new SimpleBindings();

    @Test public void testToString() throws Exception {
        NewObject object = new NewObject(animal, param);
        Assert.assertEquals(object.toString(), "new NewObject()");
        object.setClassName("SomeObject");
        Assert.assertEquals(object.toString(), "new SomeObject()");
        object.addInParam(Param.createDouble(3.14));
        Assert.assertEquals(object.toString(), "new SomeObject(3.14)");
        object.addInParam(Param.createInteger(3));
        Assert.assertEquals(object.toString(), "new SomeObject(3.14, 3)");
    }
}