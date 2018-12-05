/**
 * @(#)RoutineCompilerTest.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.compile;

import com.lasgis.evolution.object.animal.TestAnimal;
import com.lasgis.evolution.object.animal.TestAnimalManager;
import com.lasgis.evolution.object.animal.brain.AbstractOperator;
import com.lasgis.evolution.object.animal.brain.Param;
import com.lasgis.evolution.object.animal.brain.ParamType;
import com.lasgis.evolution.object.animal.brain.Routine;
import com.lasgis.evolution.object.animal.brain.routines.leaf.Birth;
import com.lasgis.evolution.object.animal.brain.routines.leaf.Dine;
import com.lasgis.evolution.object.animal.brain.routines.leaf.FindFood;
import com.lasgis.evolution.object.animal.brain.routines.leaf.FindWay;
import com.lasgis.evolution.object.animal.brain.routines.leaf.RunTo;
import com.lasgis.evolution.object.animal.brain.routines.leaf.Sin;
import com.lasgis.evolution.object.animal.brain.routines.operator.Add;
import com.lasgis.evolution.object.animal.brain.routines.operator.Array;
import com.lasgis.evolution.object.animal.brain.routines.operator.Assign;
import com.lasgis.evolution.object.animal.brain.routines.operator.DefineFunction;
import com.lasgis.evolution.object.animal.brain.routines.operator.DoWhile;
import com.lasgis.evolution.object.animal.brain.routines.operator.Equal;
import com.lasgis.evolution.object.animal.brain.routines.operator.Gt;
import com.lasgis.evolution.object.animal.brain.routines.operator.If;
import com.lasgis.evolution.object.animal.brain.routines.operator.Multiply;
import com.lasgis.evolution.object.animal.brain.routines.operator.PointVar;
import com.lasgis.evolution.object.animal.brain.routines.operator.Sequence;
import com.lasgis.evolution.object.animal.brain.routines.operator.Until;
import com.lasgis.util.Util;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.script.SimpleBindings;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class RoutineCompilerTest.
 * @author Vladimir Laskin
 * @version 1.0
 */
public class RoutineCompilerTest {

    private static Field inKeysField;
    private static Field outKeysField;
    private static Field sequenceSequenceField;
    private static Field untilCheckField;
    private static Field untilRoutineField;
    private static Field doWhileCheckField;
    private static Field doWhileRoutineField;
    private static Field ifCheckField;
    private static Field ifOnTrueRoutineField;
    private static Field ifOnFalseRoutineField;
    private static final StringBuilder DINE_ROUTE_STRING = new StringBuilder(
         "/****************************\n"
        +" * Стратегия поедания пищи. *\n"
        +" ****************************/\n"
        +"routine main DineRout(\n"
        +"  первый_входной_параметр,\n"
        +"  второй_input\n"
        +") первый_выходной_параметр,\n"
        +"  второй_output\n"
        +"{\n"
        +"    FindFood() ячейка_c_едой, название_еды;\n"
        +"    smartRunTo(ячейка_c_едой);\n"
        +"    Dine(название_еды);\n"
        +"    Birth();\n"
        +"}\n"
        +"\n"
        +"/*\n"
        +" * Стратегия умного перехода.\n"
        +" */\n"
        +"routine smartRunTo(endCell) {\n"
        +"    until(endCell != nextPoint) {\n"
        +"        FindWay(endCell) nextPoint;\n"
        +"        RunTo(nextPoint);\n"
        +"    }\n"
        +"    do {\n"
        +"        FindWay(endCell) nextPoint;\n"
        +"        RunTo(nextPoint);\n"
        +"    } while (endCell == 5);\n"
        +"\n"
        +"    until(endCell != nextPoint)\n"
        +"        FindWay(endCell) nextPoint;\n"
        +"    do RunTo(nextPoint); while (\"5\" == nextPoint);\n"
        +"\n"
        +"    birthMass = owner.birthMass;\n"
        +"    owner.birthMass = birthMass + 100;\n"
        +"    groundMass = cell[23,45].groundMass;\n"
        +"    cell[23,45].groundMass = groundMass + 100;\n"
        +"}\n");
    private final TestAnimal animal = new TestAnimal(1, 1, new TestAnimalManager());
    private SimpleBindings bind = new SimpleBindings();

    @BeforeClass
    public void beforeClass() throws Exception {
        Field[] fields = AbstractOperator.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            switch (field.getName()) {
                case "inKeys":
                    inKeysField = field;
                    break;
                case "outKeys":
                    outKeysField = field;
                    break;
            }
        }
        fields = Sequence.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            switch (field.getName()) {
                case "sequence":
                    sequenceSequenceField = field;
                    break;
            }
        }
        fields = Until.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            switch (field.getName()) {
                case "check":
                    untilCheckField = field;
                    break;
                case "routine":
                    untilRoutineField = field;
                    break;
            }
        }
        fields = DoWhile.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            switch (field.getName()) {
                case "check":
                    doWhileCheckField = field;
                    break;
                case "routine":
                    doWhileRoutineField = field;
                    break;
            }
        }
        fields = If.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            switch (field.getName()) {
                case "check":
                    ifCheckField = field;
                    break;
                case "onTrue":
                    ifOnTrueRoutineField = field;
                    break;
                case "onFalse":
                    ifOnFalseRoutineField = field;
                    break;
            }
        }
    }

    @DataProvider
    public Object[][] createRoutines() {
        return new Object[][] {{
            /* проверка на компиляемость муравьиного кода */
            Util.loadString("routines/MainRout.rout"), null, null, null, null, null
        },{
            Util.loadString("routines/DineRout.rout"), null, null, null, null, null
        }, {
            DINE_ROUTE_STRING,
            new HashMap<String, RoutineDefinition>() {{
                put("DineRout",
                    new RoutineDefinition() {{
                        this.setName("DineRout");
                        this.setMain(true);
                        this.addInpParam("первый_входной_параметр");
                        this.addInpParam("второй_input");
                        this.addOutParam("первый_выходной_параметр");
                        this.addOutParam("второй_output");
                    }}
                );
                put("smartRunTo",
                    new RoutineDefinition() {{
                        this.setName("smartRunTo");
                        this.setMain(false);
                        this.addInpParam("endCell");
                    }}
                );
            }},
            new HashMap<String, Routine>() {{
                put("DineRout", new DefineFunction(animal, bind)
                    .addRoutine(new FindFood(animal, bind).addOutKey("ячейка_c_едой").addOutKey("название_еды"))
                    .addRoutine(new Sequence(animal, bind)
                        .addRoutine(
                            new Until(animal, bind).setCheck(
                                Param.createOperator(
                                    new Equal(animal, bind).addInKey("endCell.ячейка_c_едой").addInKey("nextPoint")
                                )
                            ).setRoutine(
                                new Sequence(animal, bind).addRoutine(
                                    new FindWay(animal, bind).addInKey("endCell.ячейка_c_едой").addOutKey("nextPoint")
                                ).addRoutine(new RunTo(animal, bind).addInKey("nextPoint"))
                            )
                        )
                        .addRoutine(
                            new DoWhile(animal, bind).setCheck(
                                Param.createOperator(
                                    new Equal(animal, bind).addInKey("endCell.ячейка_c_едой").addInParam(5)
                                )
                            ).setRoutine(
                                new Sequence(animal, bind).addRoutine(
                                    new FindWay(animal, bind).addInKey("endCell.ячейка_c_едой").addOutKey("nextPoint")
                                ).addRoutine(new RunTo(animal, bind).addInKey("nextPoint"))
                            )
                        )
                        .addRoutine(new Until(animal, bind)
                            .setCheck(
                                Param.createOperator(
                                    new Equal(animal, bind).addInKey("endCell.ячейка_c_едой").addInKey("nextPoint")
                                )
                            )
                            .setRoutine(new FindWay(animal, bind).addInKey("endCell.ячейка_c_едой").addOutKey("nextPoint"))
                        )
                        .addRoutine(new DoWhile(animal, bind)
                            .setCheck(
                                Param.createOperator(
                                    new Equal(animal, bind).addInParam("5").addInKey("endCell.ячейка_c_едой")
                                )
                            )
                            .setRoutine(new RunTo(animal, bind).addInKey("nextPoint"))
                        )
                        .addRoutine(new Assign(animal, bind).addInKey("birthMass")
                            .addInParam(new PointVar(animal, bind).addInKey("owner").addInKey("birthMass"))
                        )
                        .addRoutine(
                            new Assign(animal, bind).addInParam(
                                new PointVar(animal, bind).addInKey("owner").addInKey("birthMass")
                            ).addInParam(
                                new Add(animal, bind).addInKey("birthMass").addInParam(100)
                            )
                        ).addRoutine(
                            new Assign(animal, bind).addInKey("groundMass").addInParam(
                                new PointVar(animal, bind).addInParam(
                                    new Array(animal, bind).addInKey("cell").addInParam(23).addInParam(45)
                                ).addInKey("groundMass")
                            )
                        )
                        .addRoutine(
                            new Assign(animal, bind).addInParam(
                                new PointVar(animal, bind).addInParam(
                                    new Array(animal, bind).addInKey("cell").addInParam(23).addInParam(45)
                                ).addInKey("groundMass")
                            ).addInParam(
                                new Add(animal, bind).addInKey("groundMass").addInParam(100)
                            )
                        )
                    )
                    .addRoutine(new Dine(animal, bind).addInKey("название_еды")).addRoutine(new Birth(animal, bind))
                );
            }}, null, null, null
        }, {
            new StringBuilder("routine main OperatorTest(первый, второй) выход_ной {\n"
                + "  output1 = 2 + (3 * 5) + 4 + 5 + 6;\n"
                + "  output2 = 2 + 3 * Sin(5.0 + 200.) + 4;\n"
                + "}\n"),
            new HashMap<String, RoutineDefinition>() {{
                put("OperatorTest",
                    new RoutineDefinition() {{
                        this.setName("OperatorTest");
                        this.setMain(true);
                        this.addInpParam("первый");
                        this.addInpParam("второй");
                        this.addOutParam("выход_ной");
                    }}
                );
            }},
            new HashMap<String, Routine>() {{
                put("OperatorTest", new DefineFunction(animal, bind).addRoutine(
                        new Assign(animal, bind).addInKey("output1").addInParam(
                            new Add(animal, bind).addInParam(
                                new Add(animal, bind).addInParam(
                                    new Add(animal, bind).addInParam(
                                        new Add(animal, bind).addInParam(2).addInParam(
                                            new Multiply(animal, bind).addInParam(3).addInParam(5)
                                        )
                                    ).addInParam(4)
                                ).addInParam(5)
                            ).addInParam(6)
                        )
                    ).addRoutine(
                        new Assign(animal, bind).addInKey("output2").addInParam(
                            new Add(animal, bind).addInParam(
                                new Add(animal, bind).addInParam(2).addInParam(
                                    new Multiply(animal, bind).addInParam(3).addInParam(
                                        new Sin(animal, bind).addInParam(
                                            new Add(animal, bind).addInParam(5.0).addInParam(200.0)
                                        )
                                    )
                                )
                            ).addInParam(4)
                        )
                    )
                );
            }}, "OperatorTest", "output2", 3.8553074766967
        }, {
            new StringBuilder("routine main OperatorTest(первый, второй) выходной {\n"
            + "  if (2 > 3) {\n"
            + "     exitPool = 2 * 3 + 5;\n"
            + "  } else {\n"
            + "     exitPool = 2 * (3 + 5);\n"
            + "  }\n"
            + "}\n"),
            new HashMap<String, RoutineDefinition>() {{
                put("OperatorTest",
                    new RoutineDefinition() {{
                        this.setName("OperatorTest");
                        this.setMain(true);
                        this.addInpParam("первый");
                        this.addInpParam("второй");
                        this.addOutParam("выходной");
                    }}
                );
            }},
            new HashMap<String, Routine>() {{
                put("OperatorTest",
                    new DefineFunction(animal, bind).addRoutine(
                        new If(animal, bind).setCheck(Param.createOperator(
                                new Gt(animal, bind).addInParam(2).addInParam(3)
                        )).setOnTrue(
                            new Sequence(animal, bind).addRoutine(
                                new Assign(animal, bind).addInKey("exitPool").addInParam(
                                    new Add(animal, bind).addInParam(
                                        new Multiply(animal, bind).addInParam(2).addInParam(3)
                                    ).addInParam(5)
                                )
                            )
                        ).setOnFalse(
                            new Sequence(animal, bind).addRoutine(
                                new Assign(animal, bind).addInKey("exitPool").addInParam(
                                    new Multiply(animal, bind).addInParam(2).addInParam(
                                        new Add(animal, bind).addInParam(3).addInParam(5)
                                    )
                                )
                            )
                        )
                    )
                );
            }}, "OperatorTest", "exitPool", 16
        }};
    }

    @Test(dataProvider = "createRoutines")
    public void testCreateRoutines(
        final StringBuilder program,
        final HashMap<String, RoutineDefinition> expectedDefinitions,
        final HashMap<String, Routine> expectedRoutines,
        final String keyRoutine, final String key, final Object value
    ) throws Exception {

        final RoutineCompiler rc = RoutineCompiler.createRoutines(animal, bind, program);

        if (expectedDefinitions != null) {
            Map<String, RoutineDefinition> actualDefinitions = rc.getRoutineDefinitions();
            Assert.assertEquals(actualDefinitions.size(), expectedDefinitions.size());
            for (Map.Entry<String, RoutineDefinition> map : expectedDefinitions.entrySet()) {
                final RoutineDefinition actualDefinition = actualDefinitions.get(map.getKey());
                final RoutineDefinition expectedDefinition = map.getValue();
                Assert.assertEquals(actualDefinition.isMain(), expectedDefinition.isMain());
                Assert.assertEquals(actualDefinition.getName(), expectedDefinition.getName());
                assertEquals(actualDefinition.getInpParam(), expectedDefinition.getInpParam());
                assertEquals(actualDefinition.getOutParam(), expectedDefinition.getOutParam());
            }
        }

        if (expectedRoutines != null) {
            Map<String, Routine> actualRoutines = rc.getRoutines();
            Assert.assertEquals(actualRoutines.size(), expectedRoutines.size());
            for (Map.Entry<String, Routine> map : expectedRoutines.entrySet()) {
                final AbstractOperator actualRoutine = (AbstractOperator) actualRoutines.get(map.getKey());
                final AbstractOperator expectedRoutine = (AbstractOperator) map.getValue();
                assertEquals(actualRoutine, expectedRoutine);
            }
            if (keyRoutine != null) {
                boolean isAct;
                do {
                    isAct = actualRoutines.get(keyRoutine).debugAct();
                } while (!isAct);
                if (value instanceof Double) {
                    Assert.assertEquals((Double) bind.get(key), (Double) value, 0.000000001);
                } else {
                    Assert.assertEquals(bind.get(key), value);
                }
            }
        }
    }

    private void assertEquals(final AbstractOperator actualRoutine, final AbstractOperator expectedRoutine)
    throws IllegalAccessException {
        Class actualClass = actualRoutine.getClass();
        Class expectedClass = expectedRoutine.getClass();
        Assert.assertEquals(actualClass.getCanonicalName(), expectedClass.getCanonicalName());
        @SuppressWarnings("unchecked")
        final List<Param> actualInKeys = (List<Param>) inKeysField.get(actualRoutine);
        @SuppressWarnings("unchecked")
        final List<Param> expectedInKeys = (List<Param>) inKeysField.get(expectedRoutine);
        @SuppressWarnings("unchecked")
        final List<String> actualOutKeys = (List<String>) outKeysField.get(actualRoutine);
        @SuppressWarnings("unchecked")
        final List<String> expectedOutKeys = (List<String>) outKeysField.get(expectedRoutine);

        Assert.assertEquals(actualInKeys.size(), expectedInKeys.size());
        for (int i = 0; i < expectedInKeys.size(); i++) {
            final Param actualParam = actualInKeys.get(i);
            final Param expectedParam = expectedInKeys.get(i);
            Assert.assertEquals(actualParam.toString(), expectedParam.toString());
        }
        Assert.assertEquals(actualOutKeys.size(), expectedOutKeys.size());
        for (int i = 0; i < expectedOutKeys.size(); i++) {
            final String actualOutKey = actualOutKeys.get(i);
            final String expectedOutKey = expectedOutKeys.get(i);
            Assert.assertEquals(actualOutKey, expectedOutKey);
        }

        if (Sequence.class.equals(expectedClass)) {
            @SuppressWarnings("unchecked")
            List<Routine> actualSequence = (List<Routine>) sequenceSequenceField.get(actualRoutine);
            @SuppressWarnings("unchecked")
            List<Routine> expectedSequence = (List<Routine>) sequenceSequenceField.get(expectedRoutine);
            Assert.assertEquals(actualSequence.size(), expectedSequence.size());
            for (int i = 0; i < expectedSequence.size(); i++) {
                assertEquals((AbstractOperator) actualSequence.get(i), (AbstractOperator) expectedSequence.get(i));
            }
        } else if (Until.class.equals(expectedClass)) {
            @SuppressWarnings("unchecked")
            Param actualCheck = (Param) untilCheckField.get(actualRoutine);
            @SuppressWarnings("unchecked")
            Param expectedCheck = (Param) untilCheckField.get(expectedRoutine);
            assertEquals(actualCheck, expectedCheck);

            @SuppressWarnings("unchecked")
            Routine actualSubRoutine = (Routine) untilRoutineField.get(actualRoutine);
            @SuppressWarnings("unchecked")
            Routine expectedSubRoutine = (Routine) untilRoutineField.get(expectedRoutine);
            assertEquals((AbstractOperator) actualSubRoutine, (AbstractOperator) expectedSubRoutine);
        } else if (If.class.equals(expectedClass)) {
            @SuppressWarnings("unchecked")
            Param actualCheck = (Param) ifCheckField.get(actualRoutine);
            @SuppressWarnings("unchecked")
            Param expectedCheck = (Param) ifCheckField.get(expectedRoutine);
            assertEquals(actualCheck, expectedCheck);

            @SuppressWarnings("unchecked")
            Routine actualOnTrueRoutine = (Routine) ifOnTrueRoutineField.get(actualRoutine);
            @SuppressWarnings("unchecked")
            Routine expectedOnTrueRoutine = (Routine) ifOnTrueRoutineField.get(expectedRoutine);
            assertEquals((AbstractOperator) actualOnTrueRoutine, (AbstractOperator) expectedOnTrueRoutine);

            @SuppressWarnings("unchecked")
            Routine actualOnFalseRoutine = (Routine) ifOnFalseRoutineField.get(actualRoutine);
            @SuppressWarnings("unchecked")
            Routine expectedOnFalseRoutine = (Routine) ifOnFalseRoutineField.get(expectedRoutine);
            assertEquals((AbstractOperator) actualOnFalseRoutine, (AbstractOperator) expectedOnFalseRoutine);
        }
    }

    private void assertEquals(Param actualCheck, Param expectedCheck) throws IllegalAccessException {
        Assert.assertEquals(actualCheck.type, expectedCheck.type);
        if (actualCheck.type == ParamType.Operator) {
            AbstractOperator actualOperator = (AbstractOperator) actualCheck.operator;
            AbstractOperator expectedOperator = (AbstractOperator) expectedCheck.operator;
            assertEquals(actualOperator, expectedOperator);
        } else {
            Assert.assertEquals(actualCheck.toString(), expectedCheck.toString());
        }
    }

    private void assertEquals(final List<String> actualParam, final List<String> expectedParam) {
        Assert.assertEquals(actualParam.size(), expectedParam.size());
        for (int i = 0; i < expectedParam.size(); i++) {
            Assert.assertEquals(actualParam.get(i), expectedParam.get(i));
        }
    }
}
