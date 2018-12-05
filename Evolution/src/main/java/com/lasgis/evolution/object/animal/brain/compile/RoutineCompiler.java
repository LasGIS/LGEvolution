/**
 * @(#)RoutineCompiler.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.compile;

import com.lasgis.evolution.config.HelpReflections;
import com.lasgis.evolution.object.animal.AbstractAnimal;
import com.lasgis.evolution.object.animal.brain.AbstractOperator;
import com.lasgis.evolution.object.animal.brain.AbstractRoutine;
import com.lasgis.evolution.object.animal.brain.OperatorType;
import com.lasgis.evolution.object.animal.brain.Param;
import com.lasgis.evolution.object.animal.brain.ParamType;
import com.lasgis.evolution.object.animal.brain.Routine;
import com.lasgis.evolution.object.animal.brain.routines.operator.*;
import lombok.extern.slf4j.Slf4j;

import javax.script.SimpleBindings;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Здесь текст программы превращается в код.
 *
 * @author Vladimir Laskin
 * @version 1.0
 */
@Slf4j
public final class RoutineCompiler extends TokenParser {

    private final Map<String, Routine> routines = new HashMap<>();
    private final Map<String, RoutineDefinition> routineDefinitions = new HashMap<>();
    private final Map<String, String> imports = new HashMap<>();
    private final AbstractAnimal owner;
    private final SimpleBindings param; // parser
    private String lastFunctionName;
    private Stack<Cycle> breakStack = new Stack<>();
    private Stack<DefineFunction> returnStack = new Stack<>();

    /**
     *
     * @param owner животное
     * @param param куча параметров
     */
    private RoutineCompiler(
        final AbstractAnimal owner, final SimpleBindings param
    ) {
        this.owner = owner;
        this.param = param;
    }

    /**
     * Разбор текста программы и создание набора правил.
     * @param owner животное
     * @param param куча параметров
     * @param text текст программы
     * @return список возможных стратегий
     * @throws RoutineCompilerException Ошибка при разборе
     */
    public static RoutineCompiler createRoutines(
        final AbstractAnimal owner,
        final SimpleBindings param,
        final StringBuilder text
    ) throws RoutineCompilerException {
        final RoutineCompiler rf = new RoutineCompiler(owner, param);
        rf.setProgramCode(text);
        rf.parse();
        return rf;
    }

    private void parse() throws RoutineCompilerException {
        final int beg = 0;
        final int end = sb.length() - 1;
        findMethods(beg, end);
        for (RoutineDefinition definition : routineDefinitions.values()) {
            if (definition.isMain()) {
                final Routine routine = createRoutineByDefinition(definition, null, null);
                routines.put(definition.getName(), routine);
            }
        }
    }

    /**
     * Создаём вызов стратегии по определению стратегии.
     * @param definition определение стратегии
     * @param inpParams входные параметры верхнего уровня
     * @param outParams выходные параметры верхнего уровня
     * @return объект вызова стратегии
     * @throws RoutineCompilerException Ошибка при разборе.
     */
    private AbstractOperator createRoutineByDefinition(
        final RoutineDefinition definition, final List<Param> inpParams, final List<Param> outParams
    ) throws RoutineCompilerException {
        final Token token = definition.getBody();
        final List<Param> inpParamsTo = new ArrayList<>();
        final List<Param> outParamsTo = new ArrayList<>();
        for (int i = 0; i < definition.getInpParam().size(); i++) {
            final String paramName = definition.getInpParam().get(i);
            final Param prm = (inpParams != null) ? inpParams.get(i) : null;
            if (prm != null) {
                inpParamsTo.add(Param.createLink(paramName, prm));
            } else {
                inpParamsTo.add(Param.createKey(paramName));
            }
        }
        for (int i = 0; i < definition.getOutParam().size(); i++) {
            final String paramName = definition.getOutParam().get(i);
            final Param prm = (outParams != null) ? outParams.get(i) : null;
            if (prm != null) {
                outParamsTo.add(Param.createLink(paramName, prm));
            } else {
                outParamsTo.add(Param.createKey(paramName));
            }
        }
        lastFunctionName = definition.getName();
        final OperatorWrapper wrapper = createRoutine(token, token.end, inpParamsTo, outParamsTo);
        returnStack.pop();
        return wrapper.getRoutine();
    }

    private List<Param> toParams(final List<String> params) {
        final List<Param> result = new ArrayList<>();
        for (String key : params) {
            result.add(Param.createKey(key));
        }
        return result;
    }

    /**
     * Создаём стратегию.
     * @param tokenIn указывает на точку, сразу после разбора предыдущей стратегии.
     * @param routEnd конец разбора
     * @param inpParam входные параметры  верхнего уровня
     * @param outParam выходные параметры верхнего уровня
     * @return обёртка созданного Routine и Token.
     * Token указывает на точку, сразу после разбора данной стратегии.
     * @throws RoutineCompilerException Ошибка при разборе.
     */
    private OperatorWrapper createRoutine(
        final Token tokenIn, final int routEnd, final List<Param> inpParam, final List<Param> outParam
    ) throws RoutineCompilerException {
        Token token = tokenIn.SkipComment(routEnd);
        // разбор блока
        if (token.is(TokenType.block, "{")) {
            return createSequence(token, routEnd, inpParam, outParam);
        // разбор вызова функции
        } else if (token.is(TokenType.name)) {
            final String name = token.getString();
            token = nextToken(token.end + 1, routEnd);
            if (token.is(TokenType.block, "(")) {
                final OperatorWrapper wrapper = callFunction(name, token, routEnd, inpParam, outParam);
                token = wrapper.getToken();
                token.assertion(TokenType.delimit, ";");
                return OperatorWrapper.of(wrapper.getRoutine(), token.next(routEnd));
            } else if (token.is(TokenType.block, "[") || token.is(TokenType.operator)) {
                //return createAssignStatement(name, token, routEnd, inpParam, outParam);
                final ParamWrapper wrapper = createStatement(tokenIn.SkipComment(routEnd), routEnd, inpParam, outParam);
                final Param prm = wrapper.getParam();
                if (prm.type == ParamType.Operator) {
                    token = wrapper.getToken();
                    token.assertion(TokenType.delimit, ";");
                    return OperatorWrapper.of((AbstractOperator) prm.operator, token.next(routEnd));
                }
            }
        // разбор ключевых слов
        } else if (token.is(TokenType.keyword)) {
            final String keyword = token.getString();
            switch (keyword) {
                case "until":
                    return createUntil(token, routEnd, inpParam, outParam);
                case "do":
                    return createDoWhile(token, routEnd, inpParam, outParam);
                case "if":
                    return createIf(token, routEnd, inpParam, outParam);
                case "for":
                    return createFor(token, routEnd, inpParam, outParam);
                case "break":
                    return createBreak(token, routEnd, inpParam, outParam);
                case "return":
                    return createReturn(token, routEnd, inpParam, outParam);
                default:
                    throw new RoutineCompilerException(token, "Неизвестное ключевое слово \"" + keyword + "\"");
            }
        }
        throw new RoutineCompilerException(token, "Undefined situation! Stop compilation.");
    }

    private OperatorWrapper createSequence(
        final Token tokenInp, final int routEnd, final List<Param> inpParam, final List<Param> outParam
    ) throws RoutineCompilerException {
        final Sequence sequence;
        if (lastFunctionName != null) {
            final DefineFunction defFun = new DefineFunction(owner, param);
            sequence = defFun;
            defFun.setName(lastFunctionName);
            lastFunctionName = null;
            returnStack.push(defFun);
        } else {
            sequence = new Sequence(owner, param);
        }
        final int end = tokenInp.end;
        Token token = tokenInp.first(end);
        OperatorWrapper wrapper;
        do {
            wrapper = createRoutine(token, end, inpParam, outParam);
            if (wrapper != null) {
                sequence.addRoutine(wrapper.getRoutine());
                token = wrapper.getToken().SkipComment(routEnd);
            }
        } while (wrapper != null && !token.is(TokenType.delimit, "}"));
        return OperatorWrapper.of(sequence, tokenInp.next(routEnd));
    }

    private OperatorWrapper createUntil(
        final Token tokenInp, final int routEnd, final List<Param> inpParam, final List<Param> outParam
    ) throws RoutineCompilerException {
        final Until until = new Until(owner, param);
        Token token = tokenInp.next(routEnd);
        token.assertion(TokenType.block, "(");
        final int end = token.end - 1;
        final ParamWrapper paramWrapper = createBooleanStatement(token.first(end), end, inpParam, outParam);
        if (paramWrapper != null) {
            until.setCheck(paramWrapper.getParam());
        }
        token = token.next(routEnd);
        breakStack.push(until);
        final OperatorWrapper wrapper = createRoutine(token, routEnd, inpParam, outParam);
        if (wrapper != null) {
            until.setRoutine(wrapper.getRoutine());
            token = wrapper.getToken();
        }
        breakStack.pop();
        return OperatorWrapper.of(until, token);
    }

    private OperatorWrapper createDoWhile(
        final Token tokenInp, final int routEnd, final List<Param> inpParam, final List<Param> outParam
    ) throws RoutineCompilerException {
        final DoWhile doWhile = new DoWhile(owner, param);
        Token token = tokenInp.next(routEnd);
        breakStack.push(doWhile);
        final OperatorWrapper wrapper = createRoutine(token, routEnd, inpParam, outParam);
        if (wrapper != null) {
            doWhile.setRoutine(wrapper.getRoutine());
            token = wrapper.getToken();
        }
        breakStack.pop();
        token.assertion(TokenType.keyword, "while");
        token = token.next(routEnd);
        token.assertion(TokenType.block, "(");
        final int end = token.end - 1;
        final ParamWrapper paramWrapper = createBooleanStatement(token.first(end), end, inpParam, outParam);
        if (paramWrapper != null) {
            doWhile.setCheck(paramWrapper.getParam());
        }
        token = token.next(routEnd);
        token.assertion(TokenType.delimit, ";");
        return OperatorWrapper.of(doWhile, token.next(routEnd));
    }

    private OperatorWrapper createFor(
        final Token tokenInp, final int routEnd, final List<Param> inpParam, final List<Param> outParam
    ) throws RoutineCompilerException {
        final ForEach forEach = new ForEach(owner, param);
        final Token tokenBlock = tokenInp.next(routEnd);
        tokenBlock.assertion(TokenType.block, "(");
        final int end = tokenBlock.end - 1;
        final ParamWrapper paramWrapper = createStatement(tokenBlock.first(end), end, inpParam, outParam);
        forEach.setVariable(paramWrapper.getParam());
        Token token = paramWrapper.getToken();
        token.assertion(TokenType.delimit, ":");
        token = token.next(end);
        final ParamWrapper arrayWrapper = createStatement(token, end, inpParam, outParam);
        forEach.setArray(arrayWrapper.getParam());
        token = arrayWrapper.getToken();
        token.assertion(TokenType.end);

        breakStack.push(forEach);
        token = tokenBlock.next(routEnd);
        final OperatorWrapper wrapper = createRoutine(token, routEnd, inpParam, outParam);
        forEach.setRoutine(wrapper.getRoutine());
        token = wrapper.getToken();
        breakStack.pop();
        return OperatorWrapper.of(forEach, token);
    }

    private OperatorWrapper createBreak(
        final Token tokenInp, final int routEnd, final List<Param> inpParam, final List<Param> outParam
    ) throws RoutineCompilerException {
        final Break brk = new Break(owner, param);
        brk.setCycle(breakStack.peek());
        final Token token = tokenInp.next(routEnd);
        token.assertion(TokenType.delimit, ";");
        return OperatorWrapper.of(brk, token.next(routEnd));
    }

    private OperatorWrapper createReturn(
        final Token tokenInp, final int routEnd, final List<Param> inpParam, final List<Param> outParam
    ) throws RoutineCompilerException {
        final Return ret = new Return(owner, param);
        ret.setFunction(returnStack.peek());
        final Token token = tokenInp.next(routEnd);
        token.assertion(TokenType.delimit, ";");
        return OperatorWrapper.of(ret, token.next(routEnd));
    }

    /**
     * <pre>
     *     if (condition) {
     *         fun1();
     *     } else {
     *         fun2();
     *     }
     * </pre>.
     * @param tokenInp указывает на точку, сразу после разбора предыдущей стратегии.
     * @param routEnd конец разбора
     * @param inpParam входные параметры верхнего уровня.
     * @param outParam выходные параметры верхнего уровня
     * @return If operand
     */
    private OperatorWrapper createIf(
        final Token tokenInp, final int routEnd, final List<Param> inpParam, final List<Param> outParam
    ) throws RoutineCompilerException {
        final If ifOp = new If(owner, param);
        Token token = tokenInp.next(routEnd);
        token.assertion(TokenType.block, "(");
        final int end = token.end - 1;
        final ParamWrapper paramWrapper = createBooleanStatement(token.first(end), end, inpParam, outParam);
        ifOp.setCheck(paramWrapper.getParam());
        token = token.next(routEnd);
        OperatorWrapper wrapper = createRoutine(token, routEnd, inpParam, outParam);
        ifOp.setOnTrue(wrapper.getRoutine());
        token = wrapper.getToken();
        if (token.is(TokenType.keyword, "else")) {
            token = token.next(routEnd);
            wrapper = createRoutine(token, routEnd, inpParam, outParam);
            ifOp.setOnFalse(wrapper.getRoutine());
            token = wrapper.getToken();
        }
        return OperatorWrapper.of(ifOp, token);
    }

    private ParamWrapper createNewObject(
        final Token tokenInp, final int routEnd, final List<Param> inpParam, final List<Param> outParam
    ) throws RoutineCompilerException {
        final NewObject object = new NewObject(owner, param);
        final StringBuilder sbName = new StringBuilder();
        tokenInp.assertion(TokenType.name);
        Token token = tokenInp;
        do {
            switch (token.type) {
                case name:
                    sbName.append(token.getString());
                    break;
                case operator:
                    token.assertion(TokenType.operator, ".");
                    sbName.append('.');
                    break;
                case block:
                    token.assertion(TokenType.block, "(");
                    final String key = sbName.toString();
                    if (imports.containsKey(key)) {
                        object.setClassName(imports.get(key));
                    } else {
                        object.setClassName(key);
                    }
                    final Token tokenBlock = token;
                    final int last = tokenBlock.end;
                    token = tokenBlock.first(last);
                    while (!(token.is(TokenType.delimit, ")") || token.is(TokenType.end))) {
                        final ParamWrapper wrapper = createStatement(token, last, inpParam, outParam);
                        object.addInParam(wrapper.getParam());
                        token = wrapper.getToken();
                        if (token.is(TokenType.delimit, ",")) {
                            token = token.next(last);
                        }
                    }
                    token = tokenBlock.next(routEnd);
                    return new ParamWrapper(Param.createOperator(object), token);
                default:
                    throw new RoutineCompilerException(
                        token, "Error on Create New Object with name \"" + sbName.toString() + "\""
                    );
            }
            token = token.next(routEnd);
        } while (true);
    }

    /**
     * Создаём оператор вызова функции.
     * @param functionName имя функции или стратегии
     * @param tokenInp указывает на точку, сразу после разбора предыдущей стратегии.
     * @param routEnd конец разбора
     * @param inpParam входные параметры верхнего уровня.
     * @param outParam выходные параметры верхнего уровня
     * @return обёртка созданного Routine и Token
     * Token указывает на точку, сразу после разбора данной стратегии.
     * @throws RoutineCompilerException
     */
    private OperatorWrapper callFunction(
        final String functionName, final Token tokenInp, final int routEnd,
        final List<Param> inpParam, final List<Param> outParam
    ) throws RoutineCompilerException {
        /* Находим входные параметры. Если имя параметра совпадает с параметром верхнего уровня... */
        final List<Param> inParams = new ArrayList<>();
        final List<Param> outParams = new ArrayList<>();
        final int last = tokenInp.end;
        Token token = tokenInp.first(last);
        while (!(token.is(TokenType.delimit, ")") || token.is(TokenType.end))) {
            final ParamWrapper wrapper = createStatement(token, last, inpParam, outParam);
            inParams.add(wrapper.getParam());
            token = wrapper.getToken();
            if (token.is(TokenType.delimit, ",")) {
                token = token.next(last);
            }
        }
        token = nextToken(last + 1, routEnd);
        while (token.is(TokenType.name)) {
            outParams.add(createCallParam(token, inpParam, outParam));
            token = token.next(routEnd);
            if (token.is(TokenType.delimit, ",")) {
                token = token.next(routEnd);
            }
        }
        /* Создаём вызов по описанию в *.rout файлах. */
        final RoutineDefinition definition = routineDefinitions.get(functionName);
        if (definition != null) {
            final AbstractOperator operator = createRoutineByDefinition(
                definition, inParams, outParams
            );
            return OperatorWrapper.of(operator, token);
        }
        try {
            /* Создаём вызов по имени класса (extends AbstractRoutine). */
            final Class<? extends AbstractRoutine> routineClass = HelpReflections.getRoutineClass(functionName);
            final AbstractOperator routine;
            if (routineClass != null) {
                final Constructor<? extends AbstractOperator> constructor = routineClass.getDeclaredConstructor(
                    AbstractAnimal.class, SimpleBindings.class
                );
                routine = constructor.newInstance(owner, param);
            } else {
                /* Пытаемся вызвать функцию в режиме выполнения. (наверно зря пытаемся?) */
                routine = new RuntimeFunction(owner, param);
                ((RuntimeFunction) routine).setFunctionName(functionName);
            }
            for (final Param inp : inParams) {
                routine.addInParam(inp);
            }
            for (final Param out : outParams) {
                routine.addOutKey(out.key);
            }
            return OperatorWrapper.of(routine, token);

        } catch (final ReflectiveOperationException ex) {
            log.error(ex.getMessage(), ex);
            token.beg = tokenInp.beg;
            token.type = TokenType.error;
            throw new RoutineCompilerException(
                token, "Error on filling Routine with name \"" + functionName + "\"."
            );
        }
    }

/*
    private OperatorWrapper createAssignStatement(
        final String name, final Token tokenInp, final int routEnd,
        final List<Param> inpParam, final List<Param> outParam
    ) throws RoutineCompilerException {
        Token token = tokenInp;
        final Param first = getKeyParam(name, inpParam, outParam);
        if (!(first.type == ParamType.Link || first.type == ParamType.Key)) {
            throw new RoutineCompilerException(
                token, "Incorrect left operand for assign operation: " + first + "."
            );
        }
        final String assignOperator = token.getString();
        token = token.next(routEnd);
        final ParamWrapper statement = createStatement(token, routEnd, inpParam, outParam);
        final Param last = statement.getParam();
        final AbstractOperator operand;
        switch (assignOperator) {
            case "=":
                operand = new Assign(owner, param).addOutKey(first).addInParam(last);
                break;
            case "+=":
                operand = new Assign(owner, param).addOutKey(first).addInParam(
                    new Add(owner, param).addInParam(first).addInParam(last)
                );
                break;
            case "-=":
                operand = new Assign(owner, param).addOutKey(first).addInParam(
                    new Subtract(owner, param).addInParam(first).addInParam(last)
                );
                break;
            case "*=":
                operand = new Assign(owner, param).addOutKey(first).addInParam(
                    new Multiply(owner, param).addInParam(first).addInParam(last)
                );
                break;
            case "/=":
                operand = new Assign(owner, param).addOutKey(first).addInParam(
                    new Divide(owner, param).addInParam(first).addInParam(last)
                );
                break;
            default:
                throw new RoutineCompilerException(
                    token, "Incorrect assign operator \"" + token.getString() + "\"."
                );
        }
        token = statement.getToken();
        token.assertion(TokenType.delimit, ";");
        return OperatorWrapper.of(operand, token.next(routEnd));
    }
*/

    private ParamWrapper createBooleanStatement(
        final Token tokenInp, final int routEnd, final List<Param> inpParam, final List<Param> outParam
    ) throws RoutineCompilerException {
        final ParamWrapper wrapper =  createStatement(tokenInp, routEnd, inpParam, outParam);
        final Param prm = wrapper.getParam();
        switch (prm.type) {
            case Boolean:
                return wrapper;
            case Operator:
                if (prm.operator.operatorType() == OperatorType.Boolean) {
                    return wrapper;
                }
            default:
                throw new RoutineCompilerException(
                    tokenInp, "Incorrect boolean operator \"" + tokenInp.getString() + "\"."
                );
        }
    }

    private ParamWrapper createStatement(
        final Token tokenInp, final int routEnd,
        final List<Param> inpParam, final List<Param> outParam
    ) throws RoutineCompilerException {
        final Stack<Param> stack = new Stack<>();
        boolean isUnary = true;
        Token token = tokenInp;
        do {
            switch (token.type) {
                case delimit:
                    token.assertion(TokenType.delimit, ";", ",", ")", ":");
                case end:
                    stackJoinOperand(token, stack, null);
                    return new ParamWrapper(stack.pop(), token);
                case name:
                    final String name = token.getString();
                    token = token.next(routEnd);
                    if (token.is(TokenType.block, "(")) {
                        final OperatorWrapper wrapper = callFunction(name, token, routEnd, inpParam, outParam);
                        stack.push(Param.createOperator(wrapper.getRoutine()));
                        token = wrapper.getToken();
                    } else {
                        stack.push(getKeyParam(name, inpParam, outParam));
                    }
                    isUnary = false;
                    break;
                case number:
                    stack.push(Param.createInteger(Integer.valueOf(token.getString())));
                    token = token.next(routEnd);
                    isUnary = false;
                    break;
                case real:
                    stack.push(Param.createDouble(Double.valueOf(token.getString())));
                    token = token.next(routEnd);
                    isUnary = false;
                    break;
                case string:
                    stack.push(Param.createString(token.getString()));
                    token = token.next(routEnd);
                    isUnary = false;
                    break;
                case block:
                    if (token.is(TokenType.block, "(")) {
                        final ParamWrapper block = createStatement(
                            token.first(routEnd), token.end - 1,  inpParam,  outParam
                        );
                        stack.push(block.getParam());
                        token = token.next(routEnd);
                    } else if (token.is(TokenType.block, "[")) {
                        final Array array = new Array(owner, param);
                        array.addInParam(stack.pop());
                        final int end = token.end - 1;
                        Token subToken = token.first(routEnd);
                        ParamWrapper block;
                        do {
                            block = createStatement(
                                subToken, end, inpParam, outParam
                            );
                            array.addInParam(block.getParam());
                            subToken = block.getToken().next(routEnd);
                        } while (block.getToken().is(TokenType.delimit, ","));
                        stackJoinOperand(token, stack, array);
                        token = token.next(routEnd);
                    } else {
                        throw new RoutineCompilerException(token, "Undefined block: " + token.getString());
                    }
                    isUnary = false;
                    break;
                case keyword:
                    final String word = token.getString();
                    switch (word) {
                        case "new":
                            final ParamWrapper wrapper = createNewObject(
                                token.next(routEnd), routEnd,  inpParam,  outParam
                            );
                            stack.push(wrapper.getParam());
                            token = wrapper.getToken();
                            break;
                        default:
                            break;
                    }
                    break;
                case operator:
                    final AbstractOperator operand;
                    switch (token.getString()) {
                        case "&&":
                            operand = new And(owner, param);
                            isUnary = true;
                            break;
                        case "||":
                            operand = new Or(owner, param);
                            isUnary = true;
                            break;
                        case "<":
                            operand = new Lt(owner, param);
                            isUnary = true;
                            break;
                        case "<=":
                            operand = new Le(owner, param);
                            isUnary = true;
                            break;
                        case ">":
                            operand = new Gt(owner, param);
                            isUnary = true;
                            break;
                        case ">=":
                            operand = new Ge(owner, param);
                            isUnary = true;
                            break;
                        case "==":
                            operand = new Equal(owner, param);
                            isUnary = true;
                            break;
                        case "!=":
                            operand = new Equal(owner, param).not();
                            isUnary = true;
                            break;
                        case "+":
                            if (isUnary) {
                                operand = new UnaryPlus(owner, param);
                            } else {
                                operand = new Add(owner, param);
                            }
                            isUnary = true;
                            break;
                        case "-":
                            if (isUnary) {
                                operand = new UnaryMinus(owner, param);
                            } else {
                                operand = new Subtract(owner, param);
                            }
                            isUnary = true;
                            break;
                        case "!":
                                operand = new Not(owner, param);
                            isUnary = true;
                            break;
                        case "*":
                            operand = new Multiply(owner, param);
                            isUnary = true;
                            break;
                        case "/":
                            operand = new Divide(owner, param);
                            isUnary = true;
                            break;
                        case ".":
                            operand = new PointVar(owner, param);
                            isUnary = false;
                            break;
                        case "=":
                            operand = new Assign(owner, param);
                            isUnary = true;
                            break;
                        case "+=":
                            operand = new Assign(owner, param).setType(Assign.Type.add);
                            isUnary = true;
                            break;
                        case "-=":
                            operand = new Assign(owner, param).setType(Assign.Type.sub);
                            isUnary = true;
                            break;
                        case "*=":
                            operand = new Assign(owner, param).setType(Assign.Type.mlt);
                            isUnary = true;
                            break;
                        case "/=":
                            operand = new Assign(owner, param).setType(Assign.Type.div);
                            isUnary = true;
                            break;
                        default:
                            throw new RoutineCompilerException(token,
                                "Incorrect operator \"" + token.getString() + "\".");
                    }
                    stackJoinOperand(token, stack, operand);
                    token = token.next(routEnd);
                    break;
                default:
                    throw new RoutineCompilerException(token, "Unexpected symbols");
            }
        } while (true);
    }

    private void stackJoinOperand(final Token token, final Stack<Param> stack, final AbstractOperator operand)
    throws RoutineCompilerException {
        do {
            final int size = stack.size();
            if (size > 1) {
                final Param lastParam = stack.elementAt(size - 2);
                if (lastParam.type == ParamType.Operator) {
                    final AbstractOperator lastOperator = (AbstractOperator) lastParam.operator;
                    if (lastOperator.isUnary()) {
                        if (lastOperator.getIn().isEmpty()) {
                            // обработка унарных операций
                            final Param last = stack.pop();
                            stack.pop();
                            lastOperator.addInParam(last);
                            stack.push(Param.createOperator(lastOperator));
                            continue;
                        }
                    } else if (size > 2
                        && (operand == null || lastOperator.operatorLevel() <= operand.operatorLevel())) {
                        // обработка бинарных операций
                        final Param last = stack.pop();
                        stack.pop();
                        final Param first = stack.pop();
                        lastOperator.addInParam(first).addInParam(last);
                        stack.push(Param.createOperator(lastOperator));
                        continue;
                    }
                }
            }
            break;
        } while (true);
        if (operand != null) {
            stack.push(Param.createOperator(operand));
        }
    }

    private Param createCallParam(
        final Token token, final List<Param> inpParam, final List<Param> outParam
    ) throws RoutineCompilerException {
        switch (token.type) {
            case name:
                return getKeyParam(token.getString(), inpParam, outParam);
            case number:
                return Param.createInteger(Integer.valueOf(token.getString()));
            case real:
                return Param.createDouble(Double.valueOf(token.getString()));
            case string:
                return Param.createString(token.getString());
            default:
                token.type = TokenType.error;
                throw new RoutineCompilerException(token, "Invalid parameter");
        }
    }

    private Param getKeyParam(final String key, final List<Param> inpParam, final List<Param> outParam) {
        for (Param prm : inpParam) {
            if (prm.key.equals(key)) {
                return prm;
            }
        }
        for (Param prm : outParam) {
            if (prm.key.equals(key)) {
                return prm;
            }
        }
        return Param.createKey(key);
    }

    /**
     * Находим описание всех стратегий и функций.
     * @param beg начало поиска
     * @param end конец поиска
     * @throws RoutineCompilerException Ошибка при разборе
     */
    private void findMethods(final int beg, final int end) throws RoutineCompilerException {
        int i = beg;
        Token token = nextToken(i, end).SkipComment(end);
        token.assertion(TokenType.keyword);
        while (token.is(TokenType.keyword)) {
            final String key = token.getString();
            switch (key) {
                case "routine":
                    i = token.end + 1;
                    token = addRoutineDefinition(i, end);
                    break;
                case "function":
                    i = token.end + 1;
                    token = addFunctionDefinition(i, end);
                    break;
                case "import":
                    i = token.end + 1;
                    token = addIncludeDefinition(i, end);
                    break;
                default:
                    throw new RoutineCompilerException(
                        token, "Incorrect keyword \"" + token.getString() + "\"."
                    );
            }
            token = token.next(end).SkipComment(end);
        }
    }

     private Token addRoutineDefinition(final int beg, final int end) throws RoutineCompilerException {
        final RoutineDefinition routineDefinition = new RoutineDefinition();
        //int i = beg;
        Token token = nextToken(beg, end).SkipComment(end);
        if (token.is(TokenType.keyword, "main")) {
            routineDefinition.setMain(true);
            token = token.next(end).SkipComment(end);
        }
        token.assertion(TokenType.name);
        routineDefinition.setName(token.getString());
        token = token.next(end);
        if (token.is(TokenType.block, "(")) {
            token = token.first(end).SkipComment(end);
            while (token.is(TokenType.name)) {
                routineDefinition.addInpParam(token.getString());
                token = token.next(end).SkipComment(end);
                if (!token.is(TokenType.delimit, ",")) {
                    break;
                }
                token = token.next(end).SkipComment(end);
            }
            token.assertion(TokenType.delimit, ")");
            token = token.next(end).SkipComment(end);
        }
        while (token.is(TokenType.name)) {
            routineDefinition.addOutParam(token.getString());
            token = token.next(end).SkipComment(end);
            if (!token.is(TokenType.delimit, ",")) {
                break;
            }
            token = token.next(end).SkipComment(end);
        }
        if (token.is(TokenType.block, "{")) {
            routineDefinition.setBody(token);
        }
        routineDefinitions.put(routineDefinition.getName(), routineDefinition);
        return token;
    }

    private Token addIncludeDefinition(final int beg, final int routEnd) throws RoutineCompilerException {
        Token token = nextToken(beg, routEnd).SkipComment(routEnd);
        final StringBuilder sbName = new StringBuilder();
        String key = null;
        token.assertion(TokenType.name);
        do {
            switch (token.type) {
                case name:
                    key = token.getString();
                    sbName.append(key);
                    break;
                case operator:
                    token.assertion(TokenType.operator, ".");
                    sbName.append('.');
                    break;
                case delimit:
                    token.assertion(TokenType.delimit, ";");
                    if (key == null/* || imports.containsKey(key)*/) {
                        throw new RoutineCompilerException(token, "Error on load import \"" + sbName.toString() + "\"");
                    }
                    imports.put(key, sbName.toString());
                    return token;
                default:
                    throw new RoutineCompilerException(
                        token, "Error on load import \"" + sbName.toString() + "\""
                    );
            }
            token = token.next(routEnd);
        } while (true);
    }

    private Token addFunctionDefinition(final int beg, final int end) {
        int i = beg;
        Token token;
        do {
            token = nextToken(i, end);
            i = token.end + 1;
        } while (i < end);
        return null;
    }

    public Map<String, Routine> getRoutines() {
        return routines;
    }

    public Map<String, RoutineDefinition> getRoutineDefinitions() {
        return routineDefinitions;
    }
}
