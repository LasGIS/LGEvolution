/**
 * @(#)RoutineRunTest.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.compile;

import com.lasgis.evolution.object.animal.TestAnimal;
import com.lasgis.evolution.object.animal.TestAnimalManager;
import com.lasgis.evolution.object.animal.brain.Routine;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.script.SimpleBindings;
import java.util.Map;

/**
 * Упор делается на выполнении операций, а не на компиляции.
 *
 * @author Vladimir Laskin
 * @version 1.0
 */
public class RoutineRunTest {

    private final TestAnimal animal = new TestAnimal(1, 1, new TestAnimalManager());
    private SimpleBindings bind = new SimpleBindings();

    @DataProvider
    public Object[][] runRoutines() {
        return new Object[][] {{
            new StringBuilder("routine main OperatorTest(первый, второй) выходной {\n"
                + "  yesNotGe=!(2>=3);\n"
                + "  add235 = 2 * 3 - 5;\n"
                + "  add236 = -2 * -3 - -5;\n"
                + "  выходной = 2 + 3 * 5;\n"
                + "  notGt = 2 > 3;\n"
                + "  notGe = 2 >= 3;\n"
                + "  yesGt = 3 > 2;\n"
                + "  yesGe = 3 >= 2;\n"
                + "  yesGe1 = 3 >= 3;\n"
                + "  notLt = 3 < 2;\n"
                + "  notLe = 3 <= 2;\n"
                + "  yesLt = 2 < 3;\n"
                + "  yesLe = 2 <= 3;\n"
                + "  yesLe1 = 3 <= 3;\n"
                + "  yesAnd=2<3&&3>2;\n"
                + "  notAnd=2<3&&2>3;\n"
                + "  yesOr=2<3||3<2;\n"
                + "  notOr=2>3||2>3;\n"
                + "  yesAndOrAnd=(2 < 3 && 3 > 2)||(2 < 3 && 2 > 3);\n"
                + "  notAndOrAnd=(2 < 3 && 3 < 2)||(2 < 3 && 2 > 3);\n"
                + "  yesAndOrAnd2=2<3&&3>2||2<3&&2>3;\n"
                + "  notAndOrAnd2=2<3&&3<2||2<3&&2>3;\n"
                + "  notAndOrAnd3=(2<3&&3<2||2<3&&2>3);\n"
                + "  stateName = owner.state.name;\n"
                + "  birthMass = owner.birthMass;\n"
                + "  owner.birthMass = birthMass + 100;\n"
                + "  birthMass2 = owner.birthMass;\n"
                + "  object = new com.lasgis.evolution.object.animal.organs.CellStack();\n"
                + "  objectMaxSize = object.maxSize;\n"
                + "  object = new com.lasgis.evolution.object.animal.organs.CellStack(22);\n"
                + "  objectMaxSize2 = object.maxSize;\n"
                + "}\n"),
            new Object[][] {{
                "OperatorTest",
                new Object[][] {
                    {"выходной", 17},
                    {"add235", 1},
                    {"add236", 11},
                    {"notGt", false},
                    {"notGe", false},
                    {"yesNotGe", true},
                    {"yesGt", true},
                    {"yesGe", true},
                    {"yesGe1", true},
                    {"notLt", false},
                    {"notLe", false},
                    {"yesLt", true},
                    {"yesLe", true},
                    {"yesLe1", true},
                    {"yesAnd", true},
                    {"notAnd", false},
                    {"yesOr", true},
                    {"notOr", false},
                    {"yesAndOrAnd", true},
                    {"notAndOrAnd", false},
                    {"yesAndOrAnd2", true},
                    {"notAndOrAnd2", false},
                    {"notAndOrAnd3", false},
                    {"birthMass", 100.0},
                    {"birthMass2", 200.0},
                    {"stateName", "поиск"},
                    {"objectMaxSize", 12},
                    {"objectMaxSize2", 22},
                }
            }}
        }, {
            new StringBuilder("routine main OperatorTest(первый, второй) выходной {\n"
                + "  groundMass = cell[-23,-45].element(\"groundMass\").value;\n"
                + "  cell[23,45].element(\"groundMass\").value = groundMass + 123;\n"
                + "  groundMass2 = cell[23,45].element(\"groundMass\").value;\n"
                + "  owner.cell.element(\"ground\").value = 345.7;\n"
                + "  ownerCellGround = owner.cell.element(\"ground\").value;\n"
                + "  owner.cell.offset(1,3).element(\"ground\").value = 314.15926;\n"
                + "  ownerCellOffsetGround = owner.cell.offset(1,3).element(\"ground\").value;\n"
                + "  ownerCellOffsetP1P3X = owner.cell.offset(+1,+3).indX;\n"
                + "  ownerCellOffsetP1P3Y = owner.cell.offset(+1,+3).indY;\n"
                + "  ownerCellOffsetP1M3X = owner.cell.offset(+1,-3).indX;\n"
                + "  ownerCellOffsetP1M3Y = owner.cell.offset(+1,-3).indY;\n"
                + "  ownerCellOffsetM1M3X = owner.cell.offset(-1,-3).indX;\n"
                + "  ownerCellOffsetM1M3Y = owner.cell.offset(-1,-3).indY;\n"
                + "  ownerCellOffsetM1P3X = owner.cell.offset(-1,+3).indX;\n"
                + "  ownerCellOffsetM1P3Y = owner.cell.offset(-1,+3).indY;\n"
                + "}\n"),
            new Object[][] {{
                "OperatorTest",
                new Object[][] {
                    {"groundMass", 0.0},
                    {"groundMass2", 123.0},
                    {"ownerCellGround", 345.7},
                    {"ownerCellOffsetGround", 314.15926},
                    {"ownerCellOffsetP1P3X", 1},
                    {"ownerCellOffsetP1P3Y", 3},
                    {"ownerCellOffsetP1M3X", 1},
                    {"ownerCellOffsetP1M3Y", 72},
                    {"ownerCellOffsetM1M3X", 49},
                    {"ownerCellOffsetM1M3Y", 72},
                    {"ownerCellOffsetM1P3X", 49},
                    {"ownerCellOffsetM1P3Y", 3},
                }
            }}
        }, {
            new StringBuilder("import com.lasgis.evolution.object.animal.organs.CellStack;\n"
            + "routine main OperatorTest(первый, второй) выходной {\n"
            + "  object = new CellStack();\n"
            + "  objectMaxSize = object.maxSize;\n"
            + "  object = new CellStack(22);\n"
            + "  objectMaxSize2 = object.maxSize;\n"
            + "}\n"),
            new Object[][] {{
                "OperatorTest",
                new Object[][] {
                    {"objectMaxSize", 12},
                    {"objectMaxSize2", 22},
                }
            }}
        }, {
            new StringBuilder("routine main OperatorTest(первый, второй) выходной {\n"
            + "  Log();\n"
            + "  Log(\"без уровня\");\n"
            + "  Log(\"DEBUG\", \"уровень DEBUG\");\n"
            + "  Log(\"INFO\", \"уровень INFO\");\n"
            + "  Log(\"WARN\", \"уровень WARN\");\n"
            + "  Log(\"ERROR\", \"уровень ERROR\");\n"
            + "  Log(\"FATAL\", \"уровень FATAL\");\n"
            + "}\n"),
            new Object[][] {{
                "OperatorTest",
                new Object[][] { }
            }}
        }, {
            new StringBuilder("routine main BreakTest() выходной {\n"
            + "  iBreak = 0;\n"
            + "  do {\n"
            + "    if (iBreak >= 5) break;\n"
            + "    iBreak = iBreak + 1;\n"
            + "  } while(iBreak < 10);\n"
            + "}\n"),
            new Object[][] {{
                "BreakTest",
                new Object[][] {
                    {"iBreak", 5},
                }
            }}
        }};
    }

    @Test(dataProvider = "runRoutines")
    public void testRunRoutines(
        final StringBuilder program,
        Object[][] methods
    ) throws Exception {
        bind.clear();
        final RoutineCompiler rc = RoutineCompiler.createRoutines(animal, bind, program);
        Map<String, Routine> routines = rc.getRoutines();
        for (Object[] rout : methods) {
            final String methodName = (String) rout[0];
            final Routine routine = routines.get(methodName);
            boolean isAct;
            do {
                isAct = routine.debugAct();
            } while (!isAct);
            for (Object[] params : (Object[][]) rout[1]) {
                final String key = (String) params[0];
                final Object value = params[1];
                Assert.assertEquals(bind.get(key), value, "for " + key);
            }
        }
    }

}
