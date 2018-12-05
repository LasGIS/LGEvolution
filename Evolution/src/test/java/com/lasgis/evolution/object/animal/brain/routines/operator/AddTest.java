/**
 * @(#)AddTest.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.routines.operator;

import com.lasgis.evolution.object.animal.TestAnimal;
import com.lasgis.evolution.object.animal.TestAnimalManager;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.script.SimpleBindings;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

public class AddTest {

    private final TestAnimal animal = new TestAnimal(1, 1, new TestAnimalManager());
    private final SimpleBindings param = new SimpleBindings();

    @BeforeClass()
    public void setUp() throws Exception {
    }

    @AfterClass
    public void tearDown() throws Exception {
    }

    @DataProvider
    public Object[][] addOperator() {
        return new Object[][] { {
            // "Первый " + "Второй" == "Первый Второй"
            new Add(animal, param).addInParam("Первый ").addInParam("Второй"), "Первый Второй"
        }, { // "Первый " + "Второй" + " Третий" == "Первый Второй Третий"
            new Add(animal, param).addInParam("Первый ").addInParam(
                new Add(animal, param).addInParam("Второй").addInParam(" Третий")
            ), "Первый Второй Третий"
        }, { // "Первый " + "Второй " + 55.55 == "Первый Второй 55.55"
            new Add(animal, param).addInParam("Первый ").addInParam(
                new Add(animal, param).addInParam("Второй ").addInParam(55.55)
            ), "Первый Второй 55.55"
        }, { // "Первый " + (777 + 55) == "Первый 832"
            new Add(animal, param).addInParam("Первый ").addInParam(
                new Add(animal, param).addInParam(777).addInParam(55)
            ), "Первый 832"
        }, { // ("Первый " + 777) + 55 == "Первый 77755"
            new Add(animal, param).addInParam(
                new Add(animal, param).addInParam("Первый ").addInParam(777)
            ).addInParam(55), "Первый 77755"
        }, { // 3 + 55 == 58
            new Add(animal, param).addInParam(3).addInParam(55), 58
        }, { // 3.1415926 + 55 == 58.1415926
            new Add(animal, param).addInParam(3.1415926).addInParam(55),
            58.1415926
        }, { // 3 + 55.2 == 58.2
            new Add(animal, param).addInParam(3).addInParam(55.2), 58.2
        }, { // 3.1415926 + 55.2 == 58.3415926
            new Add(animal, param).addInParam(3.1415926).addInParam(55.2), 58.3415926
        } };
    }

    @Test(dataProvider = "addOperator")
    public void testAddOperator(Add add, Object expected) throws Exception {
        add.act();
        switch (expected.getClass().getName()) {
            case "java.lang.String":
                final String outS = (String) add.getOut(0);
                assertEquals(outS, (String) expected);
                break;
            case "java.lang.Double":
                Double outD = (Double) add.getOut(0);
                assertEquals(outD, (Double) expected, 0.00000001);
                break;
            case "java.lang.Integer":
                Integer outI = (Integer) add.getOut(0);
                assertEquals(outI, (Integer) expected);
                break;
            default:
                fail("undefined Object Type");
        }
    }

}