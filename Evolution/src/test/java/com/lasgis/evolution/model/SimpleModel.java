package com.lasgis.evolution.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Простая Модель данных для теста чтения / сохранения.
 *
 * @author vlaskin
 * @since <pre>22.07.2019</pre>
 */
@Data
@Builder
public class SimpleModel {
    private String name;
    private int intNumber;
    private Integer intNumberNull;
    private double dblNumber;
    private Double dblNumberNull;
    private boolean bool;
    private Boolean boolNull;
    private SimpleType type;
    private List<SimpleModel> array;
    private Map<String, SimpleModel> map;
}
