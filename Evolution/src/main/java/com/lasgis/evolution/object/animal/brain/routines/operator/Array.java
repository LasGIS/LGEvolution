/**
 * @(#)Array.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2014 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.routines.operator;

import com.lasgis.evolution.map.Cell;
import com.lasgis.evolution.map.CellHelper;
import com.lasgis.evolution.object.animal.AbstractAnimal;
import com.lasgis.evolution.object.animal.brain.AbstractOperator;
import com.lasgis.evolution.object.animal.brain.OperatorType;
import com.lasgis.evolution.object.animal.brain.Param;
import com.lasgis.evolution.object.animal.brain.ParamType;
import com.lasgis.evolution.object.animal.brain.RoutineRunTimeException;
import lombok.extern.slf4j.Slf4j;

import javax.script.SimpleBindings;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Array это квадратные скобочки (например: cell[23,34]),
 * которые позволяют выбирать значение из массива.
 * @author Vladimir Laskin
 * @version 1.0
 */
@Slf4j
public final class Array extends AbstractOperator {

    /**
     * Конструктор.
     * @param owner животное
     * @param param куча параметров
     */
    public Array(final AbstractAnimal owner, final SimpleBindings param) {
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

    @Override
    public boolean act() throws RoutineRunTimeException {
        final ArrayList<Integer> dimension = new ArrayList<>();
        for (int i = 1; i < getIn().size(); i++) {
            final int dim = in(i);
            dimension.add(dim);
        }
        final Object object = findObject(dimension);
        if (object != null) {
            if (object instanceof Cell) {
                out(0, object);
/*
            } else {
                try {
                    final Method method = object.getClass().getMethod(toGetName(prmName));
                    method.setAccessible(true);
                    out(0, method.invoke(object));
                } catch (final NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
                    log.warn(ex.getClass().getSimpleName() + ": " + ex.getMessage());
                    out(0, null);
                }
*/
            }
        }
        return true;
    }

    /**
     * Сохраняем значение в объект.
     * @param value значение
     */
    public void set(final Object value) {
        final ArrayList<Integer> dimension = new ArrayList<>();
        for (int i = 1; i < getIn().size(); i++) {
            final int dim = in(i);
            dimension.add(dim);
        }
        final Object object = findObject(dimension);
/*
        if (object != null) {
            try {
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
                if (method != null) {
                    method.setAccessible(true);
                    method.invoke(object, value);
                } else {
                    log.warn("NoSuchMethodException: "
                        + object.getClass().getName() + "." + methodName
                        + "("  + valueClass.getName() + ")");
                }
            } catch (final InvocationTargetException | IllegalAccessException ex) {
                log.warn(ex.getClass().getSimpleName() + ": " + ex.getMessage());
                out(0, null);
            }
        }
*/
    }

    private Object findObject(final ArrayList<Integer> dimension) {
        final Param objectParam = getIn().get(0);
        Object object = null;
        if (objectParam.type == ParamType.Key) {
            object = extractObject(objectParam.key, dimension);
        } else if (objectParam.type == ParamType.Link) {
            Param prm = objectParam.link;
            do {
                prm = prm.link;
            } while (prm.type == ParamType.Link);
            object = extractObject(prm.key, dimension);
        } else if (objectParam.type == ParamType.Operator) {
            final AbstractOperator operator = (AbstractOperator) objectParam.operator;
            if (operator.operatorType() == OperatorType.Point) {
                operator.debugAct();
                object = operator.getOut(0);
            }
        }
        return object;
    }

    private Object extractObject(final String objectParam, final ArrayList<Integer> dimension) {
        switch (objectParam) {
            case "cell":
                return CellHelper.getCell(dimension.get(0), dimension.get(1));
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
