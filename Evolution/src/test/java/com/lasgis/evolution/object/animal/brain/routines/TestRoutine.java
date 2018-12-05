/**
 * @(#)TestRoutine.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.routines;

import com.lasgis.evolution.object.animal.brain.Routine;

/**
 * The Class TestRoutine.
 *
 * @author Vladimir Laskin
 * @version 1.0
 */
public class TestRoutine implements Routine {

    private boolean retValue;
    private int count;

    @Override
    public boolean act() {
        count++;
        return retValue;
    }

    @Override
    public boolean debugAct() {
        count++;
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

    @Override
    public void clear() { }
}
