/**
 * @(#)RoutineFactoryTest.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2014 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain;

import com.lasgis.evolution.object.animal.TestAnimal;
import com.lasgis.evolution.object.animal.TestAnimalManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import javax.script.SimpleBindings;

/**
 * The Class RoutineFactoryTest.
 * @author Vladimir Laskin
 * @version 1.0
 */
public class RoutineFactoryTest {

    private static final Logger LOG = Logger.getLogger(RoutineFactoryTest.class);

    private final TestAnimal animal = new TestAnimal(1, 1, new TestAnimalManager());
    private final StringBuilder expectedProgram = new StringBuilder(
          "/** Проверка на вставку. */\r\n"
        + "import com.lasgis.evolution.object.animal.organs.CellStack;\r\n"
        + "import com.lasgis.evolution.object.animal.organs.Legs;\r\n"
        + "\r\n"
        + "/** Стратегия умного перехода. */\r\n"
        + "routine smartRunTo(endCell, foodName) {\r\n"
        + "    Log(\"info\", \"foodName = \" + foodName);\r\n"
        + "}\r\n"
        + "/**\r\n"
        + " * Стратегия поедания пищи.\r\n"
        + " */\r\n"
        + "routine main DineRout() {\r\n"
        + "    smartRunTo(ячейка_c_едой, название_еды);\r\n"
        + "}\r\n"
    );

    @BeforeClass
    public void beforeClass() throws Exception {

    }

    @Test public void testCreateActualRoutines() throws Exception {
        final StringBuilder program = RoutineFactory.testActualRoutinesPreprocessor(
            animal, new SimpleBindings(), "TestInclude.rout", true
        );
        Assert.assertEquals(program.toString(), expectedProgram.toString());
    }
}
