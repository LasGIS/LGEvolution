/**
 * @(#)NewObject.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.routines.operator;

import com.lasgis.evolution.object.animal.AbstractAnimal;
import com.lasgis.evolution.object.animal.brain.AbstractOperator;
import com.lasgis.evolution.object.animal.brain.OperatorType;
import com.lasgis.evolution.object.animal.brain.Param;
import lombok.extern.slf4j.Slf4j;

import javax.script.SimpleBindings;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * The Class NewObject.
 * @author Vladimir Laskin
 * @version 1.0
 */
@Slf4j
public class NewObject extends AbstractOperator {

    private String className;
    private Constructor<Object> cachedConstructor = null;

    /**
     * Конструктор.
     * @param owner животное
     * @param param куча параметров
     */
    public NewObject(
        final AbstractAnimal owner, final SimpleBindings param
    ) {
        super(owner, param);
    }

    @Override public OperatorType operatorType() {
        return OperatorType.Point;
    }

    @Override public int operatorLevel() {
        return 0;
    }

    @Override public boolean act() {
        try {
            final Object[] parameters = createParameters();
            final Constructor<Object> constructor = findConstructor(parameters);
            final Object object = constructor.newInstance(parameters);
            out(0, object);
        } catch (final ClassNotFoundException ex) {
            log.error("class \"" + className + "\" is not found.");
        } catch (final NoSuchMethodException ex) {
            log.error("For class \"" + className + "\" not found appropriate constructor.");
        } catch (final InvocationTargetException | InstantiationException | IllegalAccessException ex) {
            log.error(ex.getMessage() + ": for class \"" + className + "\".");
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private Constructor<Object> findConstructor(final Object[] parameters)
    throws ClassNotFoundException, NoSuchMethodException {
        if (cachedConstructor == null) {
            final Class clazz = Class.forName(className);
            final Class<?>[] parameterTypes = createParameterClasses(parameters);
            cachedConstructor = clazz.getDeclaredConstructor(parameterTypes);
        }
        return cachedConstructor;
    }

    public void setClassName(final String className) {
        this.className = className;
    }

    @Override public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("new ").append(className != null ? className : "NewObject").append("(");
        for (Param param : inKeys) {
            sb.append(param).append(", ");
        }
        final int last = sb.lastIndexOf(", ");
        if (last >= 0) {
            sb.delete(last, last + 2);
        }
        sb.append(")");
        return sb.toString();
    }
}
