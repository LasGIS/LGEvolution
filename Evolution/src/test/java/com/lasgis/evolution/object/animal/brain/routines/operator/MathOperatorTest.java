/**
 * @(#)MathOperatorTest.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.routines.operator;

import com.lasgis.evolution.object.animal.TestAnimal;
import com.lasgis.evolution.object.animal.TestAnimalManager;
import com.lasgis.evolution.object.animal.brain.AbstractOperator;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.script.SimpleBindings;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

/**
 * The Class MathOperatorTest.
 * @author Vladimir Laskin
 * @version 1.0
 */
public class MathOperatorTest {

    private final TestAnimal animal = new TestAnimal(1, 1, new TestAnimalManager());
    private final SimpleBindings param = new SimpleBindings();

    @BeforeClass()
    public void setUp() throws Exception {
//        AbstractRoutine.setIsDebug(true);
    }

    @AfterClass
    public void tearDown() throws Exception {
    }

    @DataProvider
    public Object[][] mathOperator() {
        return new Object[][] { {
             // 3 + 2 == 5
            new Add(animal, param).addInParam(3).addInParam(2), 5
        }, { // 3. + 2 == 5.0
            new Add(animal, param).addInParam(3.0).addInParam(2), 5.0
        }, { // 3 + 2. == 5.0
            new Add(animal, param).addInParam(3).addInParam(2.), 5.0
        }, { // 3.1 + 2.1 == 5.2
            new Add(animal, param).addInParam(3.1).addInParam(2.1), 5.2
        }, { // 3 - 2 == 1
            new Subtract(animal, param).addInParam(3).addInParam(2), 1
        }, { // 3. - 2 == 1.0
            new Subtract(animal, param).addInParam(3.).addInParam(2), 1.0
        }, { // 3 - 2. == 1.0
            new Subtract(animal, param).addInParam(3).addInParam(2.), 1.0
        }, { // 3. - 2. == 1.0
            new Subtract(animal, param).addInParam(3.).addInParam(2.), 1.0
        }, { // 3 * 2 == 6
            new Multiply(animal, param).addInParam(3).addInParam(2), 6
        }, { // 3. * 2 == 6
            new Multiply(animal, param).addInParam(3.).addInParam(2), 6.0
        }, { // 3 * 2. == 6
            new Multiply(animal, param).addInParam(3).addInParam(2.), 6.0
        }, { // 3.1 * 2.1 == 6
            new Multiply(animal, param).addInParam(3.1).addInParam(2.1), 6.51
        }, { // 3 / 2 == 1
            new Divide(animal, param).addInParam(3).addInParam(2), 1
        }, { // 3. / 2 == 1.5
            new Divide(animal, param).addInParam(3.).addInParam(2), 1.5
        }, { // 3 / 2. == 1.5
            new Divide(animal, param).addInParam(3).addInParam(2.), 1.5
        }, { // 3.1 / 2.1 == 1
            new Divide(animal, param).addInParam(3.1).addInParam(2.1), 1.476190476
        /* операции со скобками. */
        }, { // 2 + 2 * 2 == 6
            new Add(animal, param).addInParam(2).addInParam(
                new Multiply(animal, param).addInParam(2).addInParam(2)
            ), 6
        }, { // (2 + 2) * 2 == 8
            new Multiply(animal, param).addInParam(
                new Add(animal, param).addInParam(2).addInParam(2)
            ).addInParam(2), 8
        } };
    }

    @Test(dataProvider = "mathOperator")
    public void testMathOperator(AbstractOperator operator, Object expected) throws Exception {
        operator.debugAct();
        switch (expected.getClass().getName()) {
            case "java.lang.Double":
                Double outD = (Double) operator.getOut(0);
                assertEquals(outD, (Double) expected, 0.00000001);
                break;
            case "java.lang.Integer":
                Integer outI = (Integer) operator.getOut(0);
                assertEquals(outI, (Integer) expected);
                break;
            default:
                fail("undefined Object Type");
        }
    }

}
