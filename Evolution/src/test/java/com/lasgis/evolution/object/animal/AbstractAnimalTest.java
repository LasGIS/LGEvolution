/**
 * @(#)AbstractAnimalTest.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

/**
 * The Class AbstractAnimalTest.
 * @author Vladimir Laskin
 * @version 1.0
 */
@Slf4j
public class AbstractAnimalTest {

    @Test
    public void testGravityConstructor() throws Exception {
        Pig pig = new Pig(1,1, new PigManager());
        StringBuilder sb = new StringBuilder();
        pig.getInfo(sb);
        log.info(sb.toString());
    }

    @Test
    public void testSavePigAnimals() throws Exception {
        final Pig pig = new Pig(11,15, new PigManager());
        final JSONObject json = pig.getJsonObject();
        log.info(json.toString());
    }

}
