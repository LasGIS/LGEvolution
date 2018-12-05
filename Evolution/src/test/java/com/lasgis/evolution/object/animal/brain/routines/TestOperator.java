/**
 * @(#)TestOperator.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.routines;

import com.lasgis.evolution.object.animal.brain.Operator;
import com.lasgis.evolution.object.animal.brain.OperatorType;

/**
 * The Class TestRoutine.
 *
 * @author Vladimir Laskin
 * @version 1.0
 */
public class TestOperator implements Operator {

    private boolean retValue;
    private int count;
    private OperatorType operatorType;
    private int operatorLevel;

    @Override
    public boolean act() {
        count++;
        return true;
    }

    @Override
    public boolean debugAct() {
        count++;
        return true;
    }

    @Override
    public Object getOut(final int num) {
        return retValue;
    }

    public boolean isRetValue() {
        return retValue;
    }

    public void setRetValue(boolean retValue) {
        this.retValue = retValue;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setOperatorType(OperatorType operatorType) {
        this.operatorType = operatorType;
    }

    public void setOperatorLevel(int operatorLevel) {
        this.operatorLevel = operatorLevel;
    }

    @Override
    public OperatorType operatorType() {
        return operatorType;
    }

    @Override
    public int operatorLevel() {
        return operatorLevel;
    }

    @Override
    public void clear() { }
}
