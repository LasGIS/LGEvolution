/**
 * @(#)PointVar.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.routines.operator;

import com.lasgis.evolution.map.Matrix;
import com.lasgis.evolution.object.animal.AbstractAnimal;
import com.lasgis.evolution.object.animal.brain.AbstractOperator;
import com.lasgis.evolution.object.animal.brain.OperatorType;
import com.lasgis.evolution.object.animal.brain.Param;
import com.lasgis.evolution.object.animal.brain.ParamType;
import lombok.extern.slf4j.Slf4j;

import javax.script.SimpleBindings;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Это процедура, которая получает значение из объекта.
 * В языке это вызывается через точку (. point).
 * @author Vladimir Laskin
 * @version 1.0
 */
@Slf4j
public class PointVar extends AbstractOperator {

    private Method cachedSetMethod = null;
    private Method cachedGetMethod = null;

    /**
     * Конструктор.
     * @param owner животное
     * @param param куча параметров
     */
    public PointVar(final AbstractAnimal owner, final SimpleBindings param) {
        super(owner, param);
    }

    @Override
    public OperatorType operatorType() {
        return OperatorType.Point;
    }

    @Override
    public int operatorLevel() {
        return 0;
    }

    @Override public boolean act() {
        final Param prmParam = getIn().get(1);
        final Object object = findObject();
        String prmName = prmParam.key;
        switch (prmParam.type) {
            case Link:
                Param prm = prmParam.link;
                do {
                    prm = prm.link;
                } while (prm.type == ParamType.Link);
                prmName = prm.key;
            case Key:
                if (object != null) {
/*
                    if (object instanceof Cell) {
                        final double value = ((Cell) object).element(prmName).value();
                        out(0, value);
                    } else {
*/
                    try {
                        final Method method = findGetMethod(object, prmName);
                        method.setAccessible(true);
                        out(0, method.invoke(object));
                    } catch (final NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
                        log.warn(ex.getClass().getSimpleName() + ": " + ex.getMessage());
                        out(0, null);
                    }
//                    }
                }
                break;
            case Operator:
                final AbstractOperator operator = (AbstractOperator) prmParam.operator;
                if (operator.operatorType() == OperatorType.Function) {
                    final RuntimeFunction function = (RuntimeFunction) operator;
                    function.setObject(object);
                    function.debugAct();
                    out(0, function.getOut(0));
                }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * Сохраняем значение в объект.
     * @param value значение
     */
    public void set(final Object value) {
        final Param prmParam = getIn().get(1);
        final String prmName = prmParam.key;
        final Object object = findObject();
        if (object != null) {
            try {
                final Method method = findSetMethod(object, value, prmName);
                if (method != null) {
                    method.setAccessible(true);
                    method.invoke(object, value);
                }
            } catch (final InvocationTargetException | IllegalAccessException ex) {
                log.warn(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            }
        }
    }

    private Method findGetMethod(final Object object, final String prmName) throws NoSuchMethodException {
        if (cachedGetMethod == null) {
            cachedGetMethod = object.getClass().getMethod(toGetName(prmName));
        }
        return cachedGetMethod;
    }

    private Method findSetMethod(final Object object, final Object value, final String prmName) {
        if (cachedSetMethod == null) {
            final Class valueClass = value.getClass();
            final String methodName = toSetName(prmName);
            Method method = getSetMethod(object.getClass(), methodName, valueClass);
            if (method == null) {
                switch (valueClass.getName()) {
                    case "java.lang.Double":
                        method = getSetMethod(object.getClass(), methodName, double.class);
                        break;
                    case "java.lang.Float":
                        method = getSetMethod(object.getClass(), methodName, float.class);
                        break;
                    case "java.lang.Long":
                        method = getSetMethod(object.getClass(), methodName, long.class);
                        break;
                    case "java.lang.Integer":
                        method = getSetMethod(object.getClass(), methodName, int.class);
                        break;
                    default:
                }
            }
            if (method == null) {
                log.warn(
                    "NoSuchMethodException: "
                        + object.getClass().getName() + "." + methodName
                        + "(" + valueClass.getName() + ")"
                );
            }
            cachedSetMethod = method;
        }
        return cachedSetMethod;
    }

    private Object findObject() {
        final Param objectParam = getIn().get(0);
        Object object = null;
        switch (objectParam.type) {
            case Key:
                object = extractObject(objectParam.key);
                break;
            case Link:
                Param prm = objectParam.link;
                do {
                    prm = prm.link;
                } while (prm.type == ParamType.Link);
                object = extractObject(prm.key);
                break;
            case Operator:
                final AbstractOperator operator = (AbstractOperator) objectParam.operator;
                if (operator.operatorType() == OperatorType.Point) {
                    operator.debugAct();
                    object = operator.getOut(0);
                }
                break;
        }
        return object;
    }

    private Object extractObject(final String objectParam) {
        switch (objectParam) {
            case "owner":
                return owner;
            case "matrix":
                return Matrix.getMatrix();
            default:
                return param.get(objectParam);
        }
    }

    private Method getSetMethod(final Class<?> cls, final String methodName, final Class valueClass) {
        try {
            return cls.getMethod(methodName, valueClass);
        } catch (final NoSuchMethodException e) {
            return null;
        }
    }

    private String toGetName(final String prmName) {
        return "get" + prmName.substring(0, 1).toUpperCase() + prmName.substring(1);
    }

    private String toSetName(final String prmName) {
        return "set" + prmName.substring(0, 1).toUpperCase() + prmName.substring(1);
    }

}
