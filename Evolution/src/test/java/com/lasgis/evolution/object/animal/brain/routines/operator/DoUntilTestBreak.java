/**
 * @(#)DoUntilTestBreak.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.routines.operator;

import com.lasgis.evolution.object.animal.AbstractAnimal;
import com.lasgis.evolution.object.animal.TestAnimal;
import com.lasgis.evolution.object.animal.TestAnimalManager;
import com.lasgis.evolution.object.animal.brain.Param;
import com.lasgis.evolution.object.animal.brain.Routine;
import com.lasgis.evolution.object.animal.brain.routines.leaf.Log;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.script.SimpleBindings;

import static org.testng.Assert.assertEquals;

@Slf4j
public class DoUntilTestBreak {

    private DoWhile doWhile;
    private Routine routine;
    private SimpleBindings param = new SimpleBindings();
    private AbstractAnimal animal = new TestAnimal(1, 1, new TestAnimalManager());
//    private final TestRoutine routine = new TestRoutine();
//    private final TestOperator check = new TestOperator();
//    private final Param checkParam = Param.createOperator(check);
    private String code = " {\n" +
    "     i = 0;\n" +
    "     do {\n" +
    "        if (i > 5) break;\n" +
    "        i += 1;\n" +
    "     } while(i < 10);\n" +
    " }\n";

    /**
     * Выполняем следуюющий код:
<pre>
 {
     i = 0;
     do {
        if (i >= 5) break;
        i += 1;
     } while(i < 10);
 }
</pre>
     * @throws Exception
     */
    @BeforeClass()
    public void setUp() throws Exception {

        doWhile = new DoWhile(animal, param);
        //doWhile.setCheck(checkParam);
        routine = new Sequence(animal, param).addRoutine(
            new Assign(animal, param).addInKey("i").addInParam(0)
        ).addRoutine(
            doWhile.setCheck(
                Param.createOperator(new Lt(animal, param).addInKey("i").addInParam(10))
            ).setRoutine(
                new Sequence(animal, param).addRoutine(
                    new Log(animal, param).addInParam(new Add(animal, param).addInParam("i = ").addInKey("i"))
                ).addRoutine(
                    new If(animal, param).setCheck(
                        Param.createOperator(new Ge(animal, param).addInKey("i").addInParam(5))
                    ).setOnTrue(
                        new Break(animal, param).setCycle(doWhile)
                    )
                ).addRoutine(
                    new Assign(animal, param).addInKey("i").addInParam(
                        new Add(animal, param).addInKey("i").addInParam(1)
                    )
                )
            )
        );
    }

    @AfterClass
    public void tearDown() throws Exception {

    }

    @Test
    public void testCheckByRoutine() throws Exception {
        int count = 0;
        while (!routine.act()) {
            count++;
        }
        assertEquals(param.get("i"), 5);
        assertEquals(count, 18);
        while (!routine.act()) {
            count++;
        }
        assertEquals(param.get("i"), 5);
        assertEquals(count, 36);
        while (!routine.act()) {
            count++;
        }
        assertEquals(param.get("i"), 5);
        assertEquals(count, 54);
    }

}