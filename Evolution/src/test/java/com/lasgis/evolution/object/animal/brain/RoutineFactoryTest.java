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
          "/** Проверка на вставку. */\n"
        + "import com.lasgis.evolution.object.animal.organs.CellStack;\n"
        + "import com.lasgis.evolution.object.animal.organs.Legs;\n"
        + "\n"
        + "/** Стратегия умного перехода. */\n"
        + "routine smartRunTo(endCell, foodName) {\n"
        + "    Log(\"info\", \"foodName = \" + foodName);\n"
        + "}\n"
        + "/**\n"
        + " * Стратегия поедания пищи.\n"
        + " */\n"
        + "routine main DineRout() {\n"
        + "    smartRunTo(ячейка_c_едой, название_еды);\n"
        + "}\n"
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
