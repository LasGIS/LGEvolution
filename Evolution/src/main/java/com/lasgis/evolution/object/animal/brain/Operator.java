/**
 * @(#)Operator.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain;

/**
 * The Class Operator.
 * @author Vladimir Laskin
 * @version 1.0
 */
public interface Operator extends Routine {

    /**
     * Получить результат выполнения операции.
     * @param num порядковый номер результата
     * @return результат выполнения операции
     */
    Object getOut(int num);

    /**
     * Вернуть тип оператора.
     * @return тип оператора
     */
    OperatorType operatorType();

    /**
     * уровень первоочерёдности выполнения операции.
     * [., []] - 0 точка и квадратные скобки массива
     * [-89, +56] - 1 (унитарный минус или прлюс)
     * ["*", "/"] - 2
     * ["+", "-"] - 3
     * ["<", "<=", ">", ">=", "==", "!="] - 4
     * ["&&"] - 5
     * ["||"] - 6
     * ["=", "+=", "-=", "*=", "/="] - 7
     * @return уровень первоочередности
     */
    int operatorLevel();
}
