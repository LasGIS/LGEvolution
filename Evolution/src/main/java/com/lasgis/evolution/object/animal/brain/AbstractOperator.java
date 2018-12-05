/**
 * @(#)AbstractOperator.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain;

import com.lasgis.evolution.object.animal.AbstractAnimal;
import lombok.extern.slf4j.Slf4j;

import javax.script.SimpleBindings;
import java.util.ArrayList;
import java.util.List;

/**
 * Общая часть для всех стратегий.
 * @author Vladimir Laskin
 * @version 1.0
 */
@Slf4j
public abstract class AbstractOperator implements Operator {

    private static boolean isDebug = false;

    private final StringBuilder debugSb = new StringBuilder();
    protected final String routineName;

    /** Животное, которое что-то делает. */
    protected AbstractAnimal owner;
    /** параметры, необходимые для обслуживания поведения. */
    protected SimpleBindings param;
    /** список ключей входных параметров. */
    protected List<Param> inKeys = new ArrayList<>();
    /** список ключей выходных параметров. */
    protected List<String> outKeys = new ArrayList<>();
    /** Выходные Объекты для организации цепочки вычисления. */
    protected List<Object> outObjects = new ArrayList<>();

    /**
     * Конструктор.
     * @param owner животное
     * @param param куча параметров
     */
    protected AbstractOperator(final AbstractAnimal owner, final SimpleBindings param) {
        this.owner = owner;
        this.param = param;
        routineName = this.getClass().getSimpleName();
    }

    /**
     * Добавляем входной параметр.
     * @param inParam входной параметр
     * @return ссылка на самого себя для нанизывания вызова
     */
    public AbstractOperator addInParam(final Param inParam) {
        inKeys.add(inParam);
        return this;
    }

    /**
     * Добавляем ключ входного параметра.
     * @param inKey ключ входного параметра
     * @return ссылка на самого себя для нанизывания вызова
     */
    public AbstractOperator addInKey(final String inKey) {
        inKeys.add(Param.createKey(inKey));
        return this;
    }

    /**
     * Добавляем значение параметра.
     * @param value целое значение
     * @return ссылка на самого себя для нанизывания вызова
     */
    public AbstractOperator addInParam(final Integer value) {
        inKeys.add(Param.createInteger(value));
        return this;
    }

    /**
     * Добавляем значение параметра.
     * @param value действительное значение
     * @return ссылка на самого себя для нанизывания вызова
     */
    public AbstractOperator addInParam(final Double value) {
        inKeys.add(Param.createDouble(value));
        return this;
    }

    /**
     * Добавляем значение параметра.
     * @param value действительное значение
     * @return ссылка на самого себя для нанизывания вызова
     */
    public AbstractOperator addInParam(final String value) {
        inKeys.add(Param.createString(value));
        return this;
    }

    /**
     * Добавляем значение параметра.
     * @param value действительное значение
     * @return ссылка на самого себя для нанизывания вызова
     */
    public AbstractOperator addInParam(final Operator value) {
        inKeys.add(Param.createOperator(value));
        return this;
    }

    /**
     * Добавляем ключ выходного параметра.
     * @param outKey ключ выходного параметра
     * @return ссылка на самого себя для нанизывания вызова
     */
    public AbstractOperator addOutKey(final String outKey) {
        outKeys.add(outKey);
        return this;
    }

    /**
     *
     * @param par
     * @return
     */
    public AbstractOperator addOutKey(final Param par) {
        switch (par.type) {
            case Key:
                outKeys.add(par.key);
                break;
            case Link:
                Param prm = par;
                do {
                    prm = prm.link;
                } while (prm.type == ParamType.Link);
                outKeys.add(prm.key);
                break;
            default:
                return null; // todo надо добавить run time exception
        }
        return this;
    }

    @Override
    public Object getOut(final int index) {
        if (index >= 0 && index < outKeys.size()) {
            return param.get(outKeys.get(index));
        } else {
            return outObjects.get(index);
        }
    }

    /**
     * Вернуть входной параметр из кучи или значение параметра.
     * @param index позиция параметра в описании стратегии
     * @param <PAR> класс параметра
     * @return параметр
     * @throws RoutineRunTimeException Routine Run Time Exception
     */
    @SuppressWarnings("unchecked")
    protected <PAR> PAR in(final int index) throws RoutineRunTimeException {
        if (index < 0 || index >= inKeys.size()) {
            return null;
        }
        final Param par = inKeys.get(index);
        if (isDebug) {
            debugSb.append("in(").append(index).append(") ");
            if (par.type == ParamType.Key) {
                debugSb.append(par).append(" = ").append(param.get(par.key));
            } else {
                debugSb.append(par);
            }
            debugSb.append("; ");
        }
        return (PAR) getInParam(par);
    }

    @SuppressWarnings("unchecked")
    protected  <PAR> PAR getInParam(final Param par) throws RoutineRunTimeException {
        switch (par.type) {
            case Key:
                return (PAR) param.get(par.key);
            case Link:
                Param prm = par;
                do {
                    prm = prm.link;
                } while (prm.type == ParamType.Link);
                return (PAR) getInParam(prm);
            case Integer:
                return (PAR) par.iVal;
            case Double:
                return (PAR) par.dVal;
            case String:
                return (PAR) par.str;
            case Operator:
                // Запускаем вычисление и забираем результат.
                // Считаем, что оператор выполняется сразу.
                par.operator.debugAct();
                return (PAR) par.operator.getOut(0);
            default:
                return null;
        }
    }

    /**
     * Удаляем входной параметр из кучи.
     * @param index позиция параметра в описании стратегии
     * @param <PAR> класс параметра
     * @return удалённый параметр
     * @throws RoutineRunTimeException Routine Run Time Exception
     */
    @SuppressWarnings("unchecked")
    protected <PAR> PAR remIn(final int index) throws RoutineRunTimeException {
        if (index < 0 || index >= inKeys.size()) {
            return null;
        }
        final Param par = inKeys.get(index);
        return (PAR) remInParam(par);
    }

    @SuppressWarnings("unchecked")
    private <PAR> PAR remInParam(final Param par) throws RoutineRunTimeException {
        switch (par.type) {
            case Key:
                return (PAR) param.remove(par.key);
            case Link:
                Param prm = par;
                do {
                    prm = prm.link;
                } while (prm.type == ParamType.Link);
                return (PAR) remInParam(prm);
            case Integer:
                return (PAR) par.iVal;
            case Double:
                return (PAR) par.dVal;
            case String:
                return (PAR) par.str;
            case Operator:
                // Запускаем вычисление и забираем результат.
                // Считаем, что оператор выполняется сразу.
                par.operator.debugAct();
                return (PAR) par.operator.getOut(0);
            default:
                return null;
        }
    }

    /**
     * Запомнить выходной параметр в кучу или в себя для цепочки вычисления.
     * @param index позиция параметра в описании стратегии
     * @param obj параметр
     */
    protected void out(final int index, final Object obj) {
        if (index >= 0 && index < outKeys.size()) {
            if (isDebug) {
                debugSb.append("out(").append(index).append(") ")
                    .append(outKeys.get(index)).append(" = ").append(obj).append("; ");
            }
            param.put(outKeys.get(index), obj);
        } else {
            if (isDebug) {
                debugSb.append("out(").append(index).append(") ").append(" = ").append(obj).append("; ");
            }
            if (outObjects.size() > index) {
                outObjects.set(index, obj);
            } else {
                outObjects.add(index, obj);
            }
        }
    }

    /**
     * Удаляем выходной параметр из кучи.
     * @param index позиция параметра в описании стратегии
     * @param <PAR> класс параметра
     * @return удалённый параметр
     */
    @SuppressWarnings("unchecked")
    protected <PAR> PAR remOut(final int index) {
        return (PAR) param.remove(outKeys.get(index));
    }

    @Override
    public boolean debugAct() throws RoutineRunTimeException {
        if (isDebug) {
            debugSb.setLength(0);
            debugSb.append(this.getClass().getSimpleName()).append("(");
        }
        final boolean ret = act();
        if (isDebug) {
            debugSb.append(") ").append(ret);
            log.debug(debugSb.toString());
        }
        return ret;
    }

    @Override public String toString() {
        return routineName + inKeys;
    }

    public static void setIsDebug(final boolean isDebug) {
        AbstractOperator.isDebug = isDebug;
    }

    /**
     * Конвертировать в строку.
     * @param obj входной объект
     * @return строковое значение
     */
    protected String convertToString(final Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        } else if (obj instanceof Double) {
            return String.valueOf(obj);
        } else if (obj instanceof Integer) {
            return String.valueOf(obj);
        }
        return obj.toString();
    }

    /**
     * Конвертировать в Double.
     * @param obj входной объект
     * @return Double значение
     */
    protected Double convertToDouble(final Object obj) {
        if (obj instanceof String) {
            return Double.valueOf((String) obj);
        } else if (obj instanceof Double) {
            return (Double) obj;
        } else if (obj instanceof Integer) {
            return Double.valueOf((Integer) obj);
        }
        return 0.0;
    }

    /**
     * Конвертировать в Integer.
     * @param obj входной объект
     * @return Integer значение
     */
    protected Integer convertToInteger(final Object obj) {
        if (obj instanceof String) {
            return Integer.valueOf((String) obj);
        } else if (obj instanceof Double) {
            return ((Double) obj).intValue();
        } else if (obj instanceof Integer) {
            return (Integer) obj;
        }
        return 0;
    }

    public List<Param> getIn() {
        return inKeys;
    }

    public boolean isUnary() {
        return operatorLevel() == 1;
    }

    protected Object[] createParameters() {
        final List<Param> inParam = getIn();
        final Object[] ret = new Object[inParam.size()];
        for (int i = 0; i < inParam.size(); i++) {
            ret[i] = in(i);
        }
        return ret;
    }

    protected Class<?>[] createParameterClasses(final Object[] parameters) {
        final Class<?>[] classes = new Class<?>[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            classes[i] = normalizedClass(parameters[i]);
        }
        return classes;
    }

    private Class<?> normalizedClass(final Object parameter) {
        final Class<?> parameterClass = parameter.getClass();
        switch (parameterClass.getName()) {
            case "java.lang.Double":
                return double.class;
            case "java.lang.Float":
                return float.class;
            case "java.lang.Long":
                return long.class;
            case "java.lang.Integer":
                return int.class;
            default:
                return parameterClass;
        }
    }

    @Override public void clear() {

    }
}
