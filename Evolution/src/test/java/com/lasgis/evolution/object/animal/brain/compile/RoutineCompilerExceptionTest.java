/*
 * RoutineCompilerExceptionTest.java
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2020 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.compile;

import com.lasgis.util.Util;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

public class RoutineCompilerExceptionTest {

    private static final Logger LOG = Logger.getLogger(RoutineCompilerExceptionTest.class);

    private final TokenParser parser = new TokenParser(
        Util.loadString("routines/RoutineCompilerExceptionTest.rout")
    );

    @BeforeClass
    public void beforeClass() throws Exception {
        //TokenParser.Token token = parser.nextToken(0,0);

    }

    @DataProvider
    public Object[][] getMessage() {
        return new Object[][] {
            {0, "начало", "Parser error in(1,1) - начало:\n\n"
                + "   1: /****************************\n"
                + "      ^\n"
                + "   2:  * Стратегия поедания пищи. *\n"},
            {402, "середина", "Parser error in(20,19) - середина:\n\n"
                + "  19: routine smartRunTo(endCell) {\n"
                + "  20:     until(endCell != nextPoint) {\n"
                + "                        ^\n"
                + "  21:         FindWay(endCell) nextPoint;\n"},
            {486, "конец", "Parser error in(24,1) - конец:\n\n"
                + "  23:     }\n"
                + "  24: }\n"
                + "      ^\n"},
            {487, "самый кончик", "Parser error in(24,2) - самый кончик:\n\n"
                + "  23:     }\n"
                + "  24: }\n"
                + "       ^\n"}
        };
    }

    @Test(dataProvider = "getMessage")
    public void testGetMessage(final int num, final String message,final String expectedMessage) throws Exception {
        TokenParser.Token token = parser.nextToken(0, 0);
        token.beg = num;
        RoutineCompilerException ex = new RoutineCompilerException(token, message);
        Assert.assertEquals(ex.getMessage(), expectedMessage);
    }
}
