/**
 * @(#)StatValue.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.statistic;

import com.lasgis.util.LGFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class StatValue.
 * @author vladimir.laskin
 * @version 1.0
 */
public class StatValue extends StatObject {

    private static final int MINIMAL_STAT_SIZE = 5;
    private static final int DISTRIBUTION_STAT_SIZE = 50;

    private double common;
    private double average;
    private double max;
    private double min;
    /** среднеквадратичное отклонение в минус (deviation). */
    private double devMin = 0;
    /** среднеквадратичное отклонение в плюс. */
    private double devMax = 0;

    /** Список всех значений. */
    private List<Double> values = new ArrayList<>();
    /** матрица распределение по значениям. */
    private int[] distributions = null;
    /** рейтинг из Info annotation. */
    private double rate = 1.0;

    /**
     * Конструктор.
     * @param name ключ
     */
    public StatValue(final String name) {
        super(name);
        common = 0.0;
        average = 0.0;
        max = Double.MIN_VALUE;
        min = Double.MAX_VALUE;
    }

    /**
     * Обработка статистики.
     * @param value значение
     */
    public void statistic(final double value) {
        super.statistic();
        common += value;
        average = common / count;
        if (max < value) {
            max = value;
        }
        if (min > value) {
            min = value;
        }
        values.add(value);
    }

    /**
     * Заполняем StringBuilder содержимым.
     * @param sb StringBuilder object
     * @return заполненный StringBuilder object
     */
    public StringBuilder createJson(final StringBuilder sb) {
        calculate();
        sb.append("    \"").append(name).append("\":{");
        super.createJson(sb).append(",")
            .append("\"минимум\":").append(LGFormatter.format(min * rate)).append(",")
            .append("\"среднее\":").append(LGFormatter.format(average * rate)).append(",")
            .append("\"максимум\":").append(LGFormatter.format(max * rate)).append(",")
            .append("\"квадр.мини\":").append(LGFormatter.format(devMin * rate)).append(",")
            .append("\"квадр.макси\":").append(LGFormatter.format(devMax * rate)).append(",");
        if (distributions != null) {
            sb.append("\"распределение\":[");
            for (int count : distributions) {
                sb.append(count).append(",");
            }
            StatObject.removeLast(sb, ",");
            sb.append("]");
        } else {
            sb.append("\"значения\":[");
            for (Double val : values) {
                sb.append(LGFormatter.format(val * rate)).append(",");
            }
            StatObject.removeLast(sb, ",");
            sb.append("]");
        }
        sb.append("},\n");
        return sb;
    }

    private void calculate() {
        // распределение по значениям
        if (values.size() > MINIMAL_STAT_SIZE) {
            distributions = new int[DISTRIBUTION_STAT_SIZE];
            final double delta = (max - min) / (DISTRIBUTION_STAT_SIZE - 1);
            for (Double val : values) {
                int i = (int) Math.ceil((val - min) / delta);
                if (i < 0) {
                    i = 0;
                }
                if (i >= DISTRIBUTION_STAT_SIZE) {
                    i = DISTRIBUTION_STAT_SIZE - 1;
                }
                distributions[i]++;
            }
        }

        // среднеквадратичное отклонение значения
        devMin = 0;
        devMax = 0;
        int countMin = 0;
        int countMax = 0;
        for (Double val : values) {
            if (val > average) {
                devMax += Math.pow(val - average, 2);
                countMax++;
            } else {
                devMin += Math.pow(val - average, 2);
                countMin++;
            }
        }
        // в минус
        if (countMin > 0) {
            devMin = average - Math.sqrt(devMin / countMin);
        } else {
            devMin = average;
        }
        // в плюс
        if (countMax > 0) {
            devMax = average + Math.sqrt(devMax / countMax);
        } else {
            devMax = average;
        }

    }

    public void setRate(final double rate) {
        this.rate = rate;
    }

    public double getRate() {
        return rate;
    }
}
