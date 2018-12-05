/**
 * @(#)RuntimeFunction.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.routines.operator;

import com.lasgis.evolution.object.animal.AbstractAnimal;
import com.lasgis.evolution.object.animal.brain.AbstractOperator;
import com.lasgis.evolution.object.animal.brain.OperatorType;
import com.lasgis.evolution.object.animal.brain.RoutineRunTimeException;
import lombok.extern.slf4j.Slf4j;

import javax.script.SimpleBindings;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Пример простой функции.
 * @author Vladimir Laskin
 * @version 1.0
 */
@Slf4j
public class RuntimeFunction extends AbstractOperator {

    private String functionName;
    private Object object;
    private Method cachedMethod = null;

    /**
     * Конструктор.
     * @param owner животное
     * @param param куча параметров
     */
    public RuntimeFunction(final AbstractAnimal owner, final SimpleBindings param) {
        super(owner, param);
    }

    @Override public OperatorType operatorType() {
        return OperatorType.Function;
    }

    @Override public int operatorLevel() {
        return 0;
    }

    @Override
    public boolean act() {
        final Object[] parameters = createParameters();
        final Method method = findMethod(parameters);
        try {
            out(0, method.invoke(object, parameters));
        } catch (final IllegalAccessException e) {
            throw new RoutineRunTimeException(
                "Illegal Access for run method \"" + functionName + "\";", this
            );
        } catch (final InvocationTargetException e) {
            throw new RoutineRunTimeException(
                "Invocation Target Exception on run method \"" + functionName + "\";", this
            );
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private Method findMethod(final Object[] parameters) {
        final Class cls = object.getClass();
        if (cachedMethod == null) {
            final Class<?>[] parameterTypes = createParameterClasses(parameters);
            try {
                cachedMethod = cls.getMethod(functionName, parameterTypes);
            } catch (final NoSuchMethodException e) {
                throw new RoutineRunTimeException(
                    "No Such Method \"" + functionName + "\" for class: \"" + cls.getName() + "\".", this
                );
            }
        }
        return cachedMethod;
    }

    public void setFunctionName(final String functionName) {
        this.functionName = functionName;
    }

    public void setObject(final Object object) {
        this.object = object;
    }
}
