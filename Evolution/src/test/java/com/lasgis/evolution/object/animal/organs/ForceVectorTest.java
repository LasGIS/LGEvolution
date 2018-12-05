/**
 * @(#)ForceVectorTest.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.organs;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * The Class ForceVectorTest.
 *
 * @author Laskin
 * @version 1.0
 * @since 23.03.13 19:22
 */
public class ForceVectorTest {

    @Test
    public void testGravityConstructor() throws Exception {
        ForceVector force;

        force = new ForceVector(0, 1, 1);
        assertEquals(1, force.force, 0.00001);
        assertEquals(0, force.fx, 0.00001);
        assertEquals(1, force.fy, 0.00001);

        force = new ForceVector(0, -1, 1);
        assertEquals(1, force.force, 0.00001);
        assertEquals(0, force.fx, 0.00001);
        assertEquals(-1, force.fy, 0.00001);

        force = new ForceVector(1, 0, 1);
        assertEquals(1, force.force, 0.00001);
        assertEquals(1, force.fx, 0.00001);
        assertEquals(0, force.fy, 0.00001);

        force = new ForceVector(-1, 0, 1);
        assertEquals(1, force.force, 0.00001);
        assertEquals(-1, force.fx, 0.00001);
        assertEquals(0, force.fy, 0.00001);

        force = new ForceVector(1, 1, 1);
        assertEquals(0.5, force.force, 0.00001);
        assertEquals(0.35355, force.fx, 0.00001);
        assertEquals(0.35355, force.fy, 0.00001);

        force = new ForceVector(-1, 1, 1);
        assertEquals(0.5, force.force, 0.00001);
        assertEquals(-0.35355, force.fx, 0.00001);
        assertEquals(0.35355, force.fy, 0.00001);

        force = new ForceVector(1, -1, 1);
        assertEquals(0.5, force.force, 0.00001);
        assertEquals(0.35355, force.fx, 0.00001);
        assertEquals(-0.35355, force.fy, 0.00001);

        force = new ForceVector(-1, -1, 1);
        assertEquals(0.5, force.force, 0.00001);
        assertEquals(-0.35355, force.fx, 0.00001);
        assertEquals(-0.35355, force.fy, 0.00001);

        force = new ForceVector(2, 4, 2);
        assertEquals(0.1, force.force, 0.00001);
        assertEquals(0.044721, force.fx, 0.00001);
        assertEquals(0.089443, force.fy, 0.00001);
    }
}
