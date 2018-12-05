/**
 * @(#)Param.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain;

/**
 * The Class Param.
 *
 * @author Vladimir Laskin
 * @version 1.0
 */
public class Param {

    /** тип параметра. */
    public ParamType type;
    public String key;
    public Integer iVal;
    public Double dVal;
    public String str;

    /**
     * ссылка на параметр верхнего уровня.
     */
    public Param link;

    /**
     * для создания цепочки вычисления,
     * в параметр можно положить оператор.
     */
    public Operator operator;

    private Param() {
    }

    public static Param createKey(final String key) {
        final Param par = new Param();
        par.type = ParamType.Key;
        par.key = key;
        return par;
    }

    public static Param createLink(final String key, final Param param) {
        final Param par = new Param();
        par.type = ParamType.Link;
        par.key = key;
        par.link = param;
        return par;
    }

    public static Param createInteger(final Integer iVal) {
        final Param par = new Param();
        par.type = ParamType.Integer;
        par.iVal = iVal;
        return par;
    }

    public static Param createDouble(final Double dVal) {
        final Param par = new Param();
        par.type = ParamType.Double;
        par.dVal = dVal;
        return par;
    }

    public static Param createString(final String sVal) {
        final Param par = new Param();
        par.type = ParamType.String;
        String value = sVal;
        if (value.charAt(0) == '"' || value.charAt(0) == '\'') {
            value = value.substring(1);
        }
        final int last = value.length() - 1;
        if (value.charAt(last) == '"' || value.charAt(last) == '\'') {
            value = value.substring(0, last);
        }
        par.str = value;
        return par;
    }

    public static Param createOperator(final Operator aOperator) {
        final Param par = new Param();
        par.type = ParamType.Operator;
        par.operator = aOperator;
        return par;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        switch (type) {
            case Key:
                return key;
            case Link:
                Param param = this;
                do {
                    sb.append(param.key).append('.');
                    param = param.link;
                } while (param.type == ParamType.Link);
                sb.append(param.toString());
                break;
            case Integer:
                sb/*append("(integer) ").*/.append(iVal);
                break;
            case Double:
                sb/*.append("(double) ")*/.append(dVal);
                break;
            case String:
                sb.append('\"').append(str).append('\"');
                break;
            case Operator:
                sb/*.append("(operator) ")*/.append(operator);
                break;
            default:
                return "null";
        }
        return sb.toString();
    }

}
