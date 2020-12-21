/*
 * RoutineCompilerException.java
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2020 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.compile;

/**
 * Ошибка при разборе.
 *
 * @author Vladimir Laskin
 * @version 1.0
 */
public class RoutineCompilerException extends Exception {

    private final Line[] lines = new Line[3];
    private int row = 1;
    private int col = 1;

    /**
     * @param token   token
     * @param message описание ошибки
     */
    public RoutineCompilerException(final TokenParser.Token token, final String message) {
        super(message);
        final StringBuilder sb = token.getSb();
        Line curLine = new Line();
        curLine.beg = 0;
        char ch0 = ' ';
        int i;
        for (i = 0; i < token.beg; i++) {
            final char ch = sb.charAt(i);
            if (ch == '\r' || ch == '\n') {
                if (!(ch0 == '\r' && ch == '\n')) {
                    curLine.end = i;
                    curLine.number = row++;
                    lines[0] = lines[1];
                    lines[1] = curLine;
                    curLine = new Line();
                }
                curLine.beg = i + 1;
                col = 1;
            } else {
                curLine.end = i;
                col++;
            }
            ch0 = ch;
        }
        lines[0] = lines[1];
        for (int j = 1; i < sb.length() && j < 3; i++) {
            final char ch = sb.charAt(i);
            if (ch == '\r' || ch == '\n') {
                if (!(ch0 == '\r' && ch == '\n')) {
                    curLine.end = i;
                    curLine.number = row + j - 1;
                    lines[j] = curLine;
                    curLine = new Line();
                    curLine.beg = i + 1;
                    j++;
                }
                curLine.beg = i + 1;
            } else {
                curLine.end = i;
            }
            ch0 = ch;
        }
        for (final Line line : lines) {
            if (line != null) {
                line.str = sb.substring(line.beg, line.end);
            }
        }
    }

    /**
     * Строка с ошибкой и указанием на неё.
     * <pre>
     * 19  :     routine smartRunTo(endCell) {
     * 20  :         until(endCell != nextPoint) {
     *                             ^
     * 21  :             FindWay(endCell) nextPoint;
     * </pre>
     *
     * @return строки с информацией об ошибки
     */
    @Override
    public String getMessage() {
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("Parser error in(%1$d,%2$d) - ", row, col));
        sb.append(super.getMessage());
        sb.append(":\n\n");
        for (int i = 0; i < 3; i++) {
            if (lines[i] != null) {
                sb.append(String.format("%1$4d: %2$s\n", lines[i].number, lines[i].str));
            }
            if (i == 1) {
                for (int j = 0; j < col + 5; j++) {
                    sb.append(' ');
                }
                sb.append("^\n");
            }
        }
        //sb.append("\n");
        return sb.toString();
    }

    /**
     * Описание одной строки.
     */
    private static class Line {
        /**
         * номер строки
         */
        int number;
        int beg;
        int end;
        String str;
    }
}
