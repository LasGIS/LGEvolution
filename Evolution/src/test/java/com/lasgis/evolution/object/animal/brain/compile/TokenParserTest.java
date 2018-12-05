/**
 * @(#)TokenParserTest.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.compile;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@Slf4j
public class TokenParserTest {

    @BeforeMethod
    public void setUp() throws Exception {
    }

    @AfterMethod
    public void tearDown() throws Exception {
    }

    @DataProvider
    public Object[][] tokenAssertion() {
        return new Object[][] {
            {"oken;111:12.3!\n", TokenParser.TokenType.delimit, new String[] {"oken", "ocen"}, false},
            {"oken;111:12.3!\n", TokenParser.TokenType.name, new String[] {"oken", "ocen"}, true},
            {"oken;111:12.3!\n", TokenParser.TokenType.name, new String[] {"ocen", "oken"}, true},
            {"oken;111:12.3!\n", TokenParser.TokenType.name, new String[] {"aken", "ocen"}, false},
            {";111:12.3!\n", TokenParser.TokenType.delimit, new String[] {",", ";"}, true},
            {";111:12.3!\n", TokenParser.TokenType.delimit, new String[] {";", ","}, true},
            {";111:12.3!\n", TokenParser.TokenType.delimit, new String[] {";"}, true},
            {";111:12.3!\n", TokenParser.TokenType.delimit, new String[] {","}, false},
            {";111:12.3!\n", TokenParser.TokenType.delimit, new String[] {",", ":"}, false},
        };
    }

    @Test(dataProvider = "tokenAssertion")
    public void testTokenAssertion(
        final String prg, final TokenParser.TokenType tokenType, String[] value, final boolean isOk
    ) throws Exception {
        TokenParser parser = new TokenParser(new StringBuilder(prg));
        int end = prg.length() - 1;
        int i = 0;
        TokenParser.Token token = parser.nextToken(i, end);
        if (isOk) {
            token.assertion(tokenType, value);
        } else {
            try {
                token.assertion(tokenType, value);
            } catch (final RoutineCompilerException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    @DataProvider
    public Object[][] nextToken() {
        return new Object[][] {
            {",-oken;111:12.3!", new Object[][] {
                {0, 0, TokenParser.TokenType.delimit, ","},
                {1, 1, TokenParser.TokenType.operator, "-"},
                {2, 5, TokenParser.TokenType.name, "oken"},
                {6, 6, TokenParser.TokenType.delimit, ";"},
                {7, 9, TokenParser.TokenType.number, "111"},
                {10, 10, TokenParser.TokenType.delimit, ":"},
                {11, 14, TokenParser.TokenType.real, "12.3"},
                {15, 15, TokenParser.TokenType.operator, "!"},
                {15, 15, TokenParser.TokenType.end, "!"}
            }},
            {" token 111 12.3 ", new Object[][] {
                {1, 5, TokenParser.TokenType.name, "token"},
                {7, 9, TokenParser.TokenType.number, "111"},
                {11, 14, TokenParser.TokenType.real, "12.3"},
                {15, 15, TokenParser.TokenType.end, " "}
            }},
            // success block
            {"123fun = fun123(fun32(111, 12.3))", new Object[][] {
                {0, 2, TokenParser.TokenType.number, "123"},
                {3, 5, TokenParser.TokenType.name, "fun"},
                {7, 7, TokenParser.TokenType.operator, "="},
                {9, 14, TokenParser.TokenType.name, "fun123"},
                {15, 32, TokenParser.TokenType.block, "(fun32(111, 12.3))"},
                {32, 32, TokenParser.TokenType.end, ")"}
            }},
            // success block extra char
            {"123fun = fun123{fun32{111, 12.3}}}", new Object[][] {
                {0, 2, TokenParser.TokenType.number, "123"},
                {3, 5, TokenParser.TokenType.name, "fun"},
                {7, 7, TokenParser.TokenType.operator, "="},
                {9, 14, TokenParser.TokenType.name, "fun123"},
                {15, 32, TokenParser.TokenType.block, "{fun32{111, 12.3}}"},
                {33, 33, TokenParser.TokenType.delimit, "}"},
                {33, 33, TokenParser.TokenType.end, "}"}
            }},
            // failure block
            {"123fun = fun123{fun32{{111, 12.3}}", new Object[][] {
                {0, 2, TokenParser.TokenType.number, "123"},
                {3, 5, TokenParser.TokenType.name, "fun"},
                {7, 7, TokenParser.TokenType.operator, "="},
                {9, 14, TokenParser.TokenType.name, "fun123"},
                {15, 33, TokenParser.TokenType.error, "{fun32{{111, 12.3}}"},
                {33, 33, TokenParser.TokenType.end, "}"}
            }},
            // success keyword
            {"keyword = routine, function, do, for,while", new Object[][] {
                {0, 6, TokenParser.TokenType.name, "keyword"},
                {8, 8, TokenParser.TokenType.operator, "="},
                {10, 16, TokenParser.TokenType.keyword, "routine"},
                {17, 17, TokenParser.TokenType.delimit, ","},
                {19, 26, TokenParser.TokenType.keyword, "function"},
                {27, 27, TokenParser.TokenType.delimit, ","},
                {29, 30, TokenParser.TokenType.keyword, "do"},
                {31, 31, TokenParser.TokenType.delimit, ","},
                {33, 35, TokenParser.TokenType.keyword, "for"},
                {36, 36, TokenParser.TokenType.delimit, ","},
                {37, 41, TokenParser.TokenType.keyword, "while"},
                {41, 41, TokenParser.TokenType.end, "e"}
            }},
            // success string
            {"keyword = \"routine\" \"\\\"function\\\"\" \"do, \\\"for\\\"while\"", new Object[][] {
                {0, 6, TokenParser.TokenType.name, "keyword"},
                {8, 8, TokenParser.TokenType.operator, "="},
                {10, 18, TokenParser.TokenType.string, "\"routine\""},
                {20, 33, TokenParser.TokenType.string, "\"\\\"function\\\"\""},
                {35, 52, TokenParser.TokenType.string, "\"do, \\\"for\\\"while\""},
                {52, 52, TokenParser.TokenType.end, "\""}
            }},
            // failure string
            {"keyword = \"routine\" \"\\\"function\"\" \"do, \\\"for\\\"while\"", new Object[][] {
                {0, 6, TokenParser.TokenType.name, "keyword"},
                {8, 8, TokenParser.TokenType.operator, "="},
                {10, 18, TokenParser.TokenType.string, "\"routine\""},
                {20, 31, TokenParser.TokenType.string, "\"\\\"function\""},
                {32, 34, TokenParser.TokenType.string, "\" \""},
                {35, 36, TokenParser.TokenType.keyword, "do"},
                {37, 37, TokenParser.TokenType.delimit, ","},
                {39, 40, TokenParser.TokenType.delimit, "\\\""},
                {41, 43, TokenParser.TokenType.keyword, "for"},
                {44, 45, TokenParser.TokenType.delimit, "\\\""},
                {46, 50, TokenParser.TokenType.keyword, "while"},
                {51, 51, TokenParser.TokenType.error, "\""},
                //{40, 52, TokenParser.TokenType.delimit, "for\\\"while\""},
                {51, 51, TokenParser.TokenType.end, "\""}
            }},
            {"operators = var + a * 567/67.8 -v=v+=v-=v*=A/=A+A-A*A/A&&A||A>=A<A<=A>A>=A==A!=A", new Object[][] {
                {0, 8, TokenParser.TokenType.name, "operators"},
                {10, 10, TokenParser.TokenType.operator, "="},
                {12, 14, TokenParser.TokenType.name, "var"},
                {16, 16, TokenParser.TokenType.operator, "+"},
                {18, 18, TokenParser.TokenType.name, "a"},
                {20, 20, TokenParser.TokenType.operator, "*"},
                {22, 24, TokenParser.TokenType.number, "567"},
                {25, 25, TokenParser.TokenType.operator, "/"},
                {26, 29, TokenParser.TokenType.real, "67.8"},
                {31, 31, TokenParser.TokenType.operator, "-"},
                {32, 32, TokenParser.TokenType.name, "v"},
                {33, 33, TokenParser.TokenType.operator, "="},
                {34, 34, TokenParser.TokenType.name, "v"},
                {35, 36, TokenParser.TokenType.operator, "+="},
                {37, 37, TokenParser.TokenType.name, "v"},
                {38, 39, TokenParser.TokenType.operator, "-="},
                {40, 40, TokenParser.TokenType.name, "v"},
                {41, 42, TokenParser.TokenType.operator, "*="},
                {43, 43, TokenParser.TokenType.name, "A"},
                {44, 45, TokenParser.TokenType.operator, "/="},
                {46, 46, TokenParser.TokenType.name, "A"},
                {47, 47, TokenParser.TokenType.operator, "+"},
                {48, 48, TokenParser.TokenType.name, "A"},
                {49, 49, TokenParser.TokenType.operator, "-"},
                {50, 50, TokenParser.TokenType.name, "A"},
                {51, 51, TokenParser.TokenType.operator, "*"},
                {52, 52, TokenParser.TokenType.name, "A"},
                {53, 53, TokenParser.TokenType.operator, "/"},
                {54, 54, TokenParser.TokenType.name, "A"},
                {55, 56, TokenParser.TokenType.operator, "&&"},
                {57, 57, TokenParser.TokenType.name, "A"},
                {58, 59, TokenParser.TokenType.operator, "||"},
                {60, 60, TokenParser.TokenType.name, "A"},
                {61, 62, TokenParser.TokenType.operator, ">="},
                {63, 63, TokenParser.TokenType.name, "A"},
                {64, 64, TokenParser.TokenType.operator, "<"},
                {65, 65, TokenParser.TokenType.name, "A"},
                {66, 67, TokenParser.TokenType.operator, "<="},
                {68, 68, TokenParser.TokenType.name, "A"},
                {69, 69, TokenParser.TokenType.operator, ">"},
                {70, 70, TokenParser.TokenType.name, "A"},
                {71, 72, TokenParser.TokenType.operator, ">="},
                {73, 73, TokenParser.TokenType.name, "A"},
                {74, 75, TokenParser.TokenType.operator, "=="},
                {76, 76, TokenParser.TokenType.name, "A"},
                {77, 78, TokenParser.TokenType.operator, "!="},
                {79, 79, TokenParser.TokenType.name, "A"},
                {79, 79, TokenParser.TokenType.end, "A"}
            }},
            {"comment = /* внутренний комментарий var + a * 567/67.8 -v=v+=v*/" +
                " routine // наружный комментарий\n var /*********/  ", new Object[][] {
                {0, 6, TokenParser.TokenType.name, "comment"},
                {8, 8, TokenParser.TokenType.operator, "="},
                {10, 63, TokenParser.TokenType.comment, "/* внутренний комментарий var + a * 567/67.8 -v=v+=v*/"},
                {65, 71, TokenParser.TokenType.keyword, "routine"},
                {73, 96, TokenParser.TokenType.comment, "// наружный комментарий\n"},
                {98, 100, TokenParser.TokenType.name, "var"},
                {102, 112, TokenParser.TokenType.comment, "/*********/"},
                {114, 114, TokenParser.TokenType.end, " "}
            }},
        };
    }

    @Test(dataProvider = "nextToken")
    public void testNextToken(
        final String prg, final Object[][] tokens
    ) throws Exception {
        int end = prg.length() - 1;
        int i = 0;
        log.info("---= \"" + prg + "\" =---");
        TokenParser parser = new TokenParser(new StringBuilder(prg));
        for (Object[] tok : tokens) {
            int tokBeg = (int) tok[0];
            int tokEnd = (int) tok[1];
            TokenParser.TokenType tokType = (TokenParser.TokenType) tok[2];
            String tokString = (String) tok[3];
            TokenParser.Token token = parser.nextToken(i, end);

            assertNotNull(token);
            assertEquals(token.beg, tokBeg, "\"" + token.getString() + " begin");
            assertEquals(token.end, tokEnd, "\"" + token.getString() + " end");
            assertEquals(token.type, tokType, "\"" + token.getString() + "\" type");
            assertEquals(token.getString(), tokString, "value");

            i = token.end + 1;
            log.info("" + token.beg + "-" + token.end + " " + token.type + "(" + token.getString() + ") = OK");
        }
    }

}