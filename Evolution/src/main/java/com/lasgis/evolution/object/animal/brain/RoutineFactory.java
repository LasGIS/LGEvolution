/**
 * @(#)RoutineFactory.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain;

import com.lasgis.evolution.object.animal.AbstractAnimal;
import com.lasgis.evolution.object.animal.brain.compile.RoutineCompiler;
import com.lasgis.evolution.object.animal.brain.compile.RoutineCompilerException;
import com.lasgis.evolution.object.animal.brain.routines.leaf.Birth;
import com.lasgis.evolution.object.animal.brain.routines.leaf.Dine;
import com.lasgis.evolution.object.animal.brain.routines.leaf.FindFood;
import com.lasgis.evolution.object.animal.brain.routines.leaf.FindWay;
import com.lasgis.evolution.object.animal.brain.routines.leaf.RunTo;
import com.lasgis.evolution.object.animal.brain.routines.operator.DoWhile;
import com.lasgis.evolution.object.animal.brain.routines.operator.Equal;
import com.lasgis.evolution.object.animal.brain.routines.operator.Sequence;
import com.lasgis.evolution.object.animal.brain.routines.operator.Until;
import com.lasgis.util.ResourceLoader;
import com.lasgis.util.Util;

import javax.script.SimpleBindings;
import java.util.Map;

/**
 * Здесь создаётся главный Routine.
 *
 * @author Vladimir Laskin
 * @version 1.0
 */
public final class RoutineFactory {


    private static final String ROOT_ROUTINES = ResourceLoader.getResource("root.evolution.routines");
    /** общий путь. */
    private final String prefixPath;

    private RoutineFactory() {
        prefixPath = "";
    }

    /**
     * Конструктор с указанием общего пути.
     * @param prefixPath общий путь
     */
    private RoutineFactory(final String prefixPath) {
        this.prefixPath = prefixPath;
    }

    /**
     * Создаём стратегию по ссылке на программный файл.
     *
     * @param owner животное
     * @param param куча параметров
     * @param programName имя программного файла
     * @param isFromFile если true, то читаем из файла, а иначе из classpath
     * @return стратегия поедания пищи
     * @throws RoutineCompilerException если ошибка
     */
    public static Map<String, Routine> createActualRoutines(
        final AbstractAnimal owner, final SimpleBindings param, final String programName, final boolean ... isFromFile
    ) throws RoutineCompilerException {
        final String className = owner.getClass().getSimpleName();
        final StringBuilder program;
        if (isFromFile[0]) {
            final RoutineFactory factory = new RoutineFactory(ROOT_ROUTINES + className + '/');
            program = factory.loadProgramFromFile(programName);
        } else {
            final RoutineFactory factory = new RoutineFactory();
            program = factory.loadProgramFromClassPath(programName);
        }
        final RoutineCompiler rc = RoutineCompiler.createRoutines(owner, param, program);
        return rc.getRoutines();
    }

    /**
     * Создаём стратегию по ссылке на программный файл.
     *
     * @param owner животное
     * @param param куча параметров
     * @param programName имя программного файла
     * @param isFromFile если true, то читаем из файла, а иначе из classpath
     * @return стратегия поедания пищи
     * @throws RoutineCompilerException если ошибка
     */
    public static StringBuilder testActualRoutinesPreprocessor(
        final AbstractAnimal owner, final SimpleBindings param, final String programName, final boolean ... isFromFile
    ) throws RoutineCompilerException {
        final String className = owner.getClass().getSimpleName();
        final StringBuilder program;
        if (isFromFile[0]) {
            final RoutineFactory factory = new RoutineFactory(ROOT_ROUTINES + className + '/');
            program = factory.loadProgramFromFile(programName);
        } else {
            final RoutineFactory factory = new RoutineFactory();
            program = factory.loadProgramFromClassPath(programName);
        }
        return program;
    }

    /**
     * for example:
     * import include/Import.rout;
     * @param programName имя файла программы
     * @return содержимое прочитанного исходного кода
     */
    private StringBuilder loadProgramFromFile(final String programName) {
        final StringBuilder program = Util.loadStringFromFile(prefixPath + programName);
        int from = 0;
        int index;
        while ((index = program.indexOf("include", from)) >= 0) {
            final int start = index + "include".length();
            final int end = program.indexOf(";", start);
            final String subProgramName = program.substring(start, end).trim();
            final StringBuilder subProgram = loadProgramFromFile(subProgramName);
            program.delete(index, end + 1);
            program.insert(index, subProgram);
            from = index;
        }
        return program;
    }

    private StringBuilder loadProgramFromClassPath(final String programName) {
        return Util.loadString(programName);
    }

    /**
     * Создаём стратегию поедания пищи.
     * Эта стратегия может быть получена путём компиляции скрипта:
<PRE>
routine dinerRout {
    FindFood():ячейка_c_едой, название_еды;
    smartRunTo(ячейка_c_едой);
    Dine(название_еды);
    Birth();
}
</PRE>
     * @param owner животное
     * @param param куча параметров
     * @return стратегия поедания пищи
     */
    public static Routine dinerRout(final AbstractAnimal owner, final SimpleBindings param) {
        // {
        final Sequence dinerRout = new Sequence(owner, param);
        // FindFood():ячейка_c_едой, название_еды;
        dinerRout.addRoutine((new FindFood(owner, param)).addOutKey(Routine.FOOD_CELL).addOutKey(Routine.FOOD_NAME));
        // RunTo(ячейка_c_едой);
        dinerRout.addRoutine(smartRunTo(owner, param, Routine.FOOD_CELL));
//      dinerRout.addRoutine((new RunTo(owner, param)).addInKey(Routine.FOOD_CELL));
        // Dine(название_еды);
        dinerRout.addRoutine((new Dine(owner, param)).addInKey(Routine.FOOD_NAME));
        // Birth();
        dinerRout.addRoutine(new Birth(owner, param));
        return dinerRout;
    }

    /**
     * Стратегия умного перехода.
     * Эта стратегия может быть получена путём компиляции скрипта:
     <PRE>
routine smartRunTo(endCell) {
    until(endCell != nextPoint) {
        FindWay(endCell) nextPoint;
        RunTo(nextPoint);
    }
}
     </PRE>
     * @param owner животное
     * @param param куча параметров
     * @param endCell конечная ячейка
     * @return стратегия умного перехода
     */
    public static Routine smartRunTo(final AbstractAnimal owner, final SimpleBindings param, final String endCell) {
        final String nextPoint = "промежуточная";
        return new DoWhile(owner, param)
            .setCheck(Param.createOperator((new Equal(owner, param) { {
                addInKey(endCell).addInKey(nextPoint);
            } }).not()))
            .setRoutine(new Sequence(owner, param).addRoutine((new FindWay(owner, param)).addInKey(endCell)
                    .addOutKey(nextPoint)).addRoutine((new RunTo(owner, param)).addInKey(nextPoint)));
    }

    /**
     * Стратегия поедания пищи.
     * Эта стратегия может быть получена путём компиляции скрипта:
     <PRE>
routine smartDine(foodName) {
    until(cell(foodName) > 5.0) {
        Dine(foodName);
    }
}
     </PRE>
     * @param owner животное
     * @param param куча параметров
     * @param endCell конечная ячейка
     * @param foodName название еды
     * @return стратегия поедания пищи
     */
    public static Routine smartDine(
        final AbstractAnimal owner,
        final SimpleBindings param,
        final String endCell,
        final String foodName
    ) {
        final String nextPoint = "промежуточная";
        return new Until(owner, param)
            .setCheck(Param.createOperator((new Equal(owner, param) { { addInKey(endCell).addInParam(5.0); } } ).not()))
            .setRoutine((new Dine(owner, param)).addInKey(foodName));
    }
}
