/**
 * @(#)AbstractAnimalTest.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

/**
 * The Class AbstractAnimalTest.
 * @author Vladimir Laskin
 * @version 1.0
 */
@Slf4j
public class AbstractAnimalTest {

    private static final Logger LOG = Logger.getLogger(AbstractAnimalTest.class);

    @Test
    public void testGravityConstructor() throws Exception {
        Pig pig = new Pig(1,1, new PigManager());
        StringBuilder sb = new StringBuilder();
        pig.getInfo(sb);
        log.info(sb.toString());
    }

}
