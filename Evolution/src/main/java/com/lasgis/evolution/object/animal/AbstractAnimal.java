/**
 * @(#)AbstractAnimal.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal;

import com.lasgis.evolution.map.Cell;
import com.lasgis.evolution.map.CellHelper;
import com.lasgis.evolution.map.element.Parameters;
import com.lasgis.evolution.object.AnimalBehaviour;
import com.lasgis.evolution.object.Info;
import com.lasgis.evolution.object.InfoType;
import com.lasgis.evolution.object.animal.organs.Legs;
import com.lasgis.evolution.object.animal.organs.Stomach;
import com.lasgis.evolution.panels.Scalable;
import com.lasgis.util.LGFormatter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.lasgis.evolution.object.animal.AnimalState.analise;
import static com.lasgis.evolution.object.animal.AnimalState.move;
import static com.lasgis.evolution.utils.ColorHelper.colorNormal;

/**
 * Общие свойства всех животных.
 * @author vladimir.laskin
 * @version 1.0
 */
@Slf4j
@Info(type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
public abstract class AbstractAnimal implements AnimalBehaviour, CallBack {

    protected static final Color SELECTED_COLOR = new Color(255, 0, 0);

    static AtomicInteger animalUid = new AtomicInteger(0);

    @Info(name = "кличка", type = { InfoType.SAVE })
    private String uidName;
    @Info(name = "широта", type = { InfoType.SAVE })
    private double latitude;
    @Info(name = "долгота", type = { InfoType.SAVE })
    private double longitude;
    private AbstractAnimalManager manager;
    private boolean selected = false;

    /** Масса. Если она меньше нуля, то животное умирает :( ... */
    @Info(name = "масса", type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private double mass;
    /** Энергия. Если она меньше нуля, то животное засыпает :) ... */
    @Info(name = "энергия", type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private double energy;
    /** Желудок. */
    @Info(name = "Желудок", type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    protected Stomach stomach;
    /** Ноги животного (шевели батонами). */
    @Info(type = { InfoType.SAVE })
    private Legs legs;
    /** Состояние животного. */
    @Info(name = "состояние", head = "Статистика", type = { InfoType.INFO, InfoType.SAVE })
    private AnimalState state;

    /** пропускаем ход до этого времени. */
    private long skip2NanoTime;

    /**
     * В момент создания присваиваем уникальное имя.
     * @param latitude широта точки
     * @param longitude долгота точки
     * @param manager сохраняем менеджер для рождения и умирания
     */
    protected AbstractAnimal(final double latitude, final double longitude, final AbstractAnimalManager manager) {
        uidName = manager.getName() + '_' + animalUid.incrementAndGet();
        setLatitude(latitude);
        setLongitude(longitude);
        legs = new Legs(this);
        this.manager = manager;
        setState(analise);
        getCell().addAnimal(this);
    }

    /**
     * Вернуть уникальное имя животного.
     * @return уникальное имя животного.
     */
    public String getUniqueName() {
        return uidName;
    }

    @Override
    public boolean run() {
        changeEnergy(-0.0005 * mass);
        if (state == move) {
            legs.move();
            callBackAction(CallActionType.onMoveTo);
        } else {
            return action();
        }
        return true;
    }

    /**
     * элементарное движение животного.
     * @return if false then died
     */
    protected abstract boolean action();

    /**
     * Рисуем животное в своём состоянии.
     * @param graphics контекст вывода.
     * @param interval квадратный диапазон,
     * в который должно входить животное
     */
    public abstract void draw(Graphics graphics, Scalable interval);

    /**
     * Рожаем новое животное на основании текущего.
     */
    public abstract void toBirth();

    public AbstractAnimalManager getManager() {
        return manager;
    }

    public Legs getLegs() {
        return legs;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public void setLatitude(final double latitude) {
        this.latitude = latitude;
    }

    @Override
    public void changeLatitude(final double value) {
        latitude += value;
        if (latitude >= Parameters.MAX_LATITUDE) {
            latitude -= Parameters.MAX_LATITUDE;
        } else if (latitude < 0) {
            latitude += Parameters.MAX_LATITUDE;
        }
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public void setLongitude(final double longitude) {
        this.longitude = longitude;
    }

    @Override
    public void changeLongitude(final double value) {
        longitude += value;
        if (longitude >= Parameters.MAX_LONGITUDE) {
            longitude -= Parameters.MAX_LONGITUDE;
        } else if (longitude < 0) {
            longitude += Parameters.MAX_LONGITUDE;
        }
    }

    @Override
    public Cell getCell() {
        return CellHelper.getCell(getLatitude(), getLongitude());
    }

    public double getMass() {
        return mass;
    }

    public void setMass(final double mass) {
        this.mass = mass;
    }

    /**
     * Вся масса, вместе с желудком и кишками.
     * @return Вся масса
     */
    public abstract double getFullMass();

    /**
     * Изменяем (добавляем или уменьшаем) массу.
     * @param delta добавочная масса
     * @return полученная масса
     */
    public double changeMass(final double delta) {
        mass += delta;
        if (mass < 0) {
            mass = 0;
        }
        return mass;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(final double energy) {
        this.energy = energy;
    }

    /**
     * Изменяем (добавляем или уменьшаем) энергию.
     * @param delta добавочная энергия
     * @return полученная энергия
     */
    public double changeEnergy(final double delta) {
        energy += delta;
        if (energy < 0) {
            energy = 0;
        }
        return energy;
    }

    public AnimalState getState() {
        return state;
    }

    public void setState(final AnimalState state) {
        this.state = state;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(final boolean selected) {
        this.selected = selected;
    }

    public long getSkip2NanoTime() {
        return skip2NanoTime;
    }

    public void setSkip2NanoTime(final long skip2NanoTime) {
        this.skip2NanoTime = skip2NanoTime;
    }

    /**
     * добавляем время для пропуска.
     * @param skipNanoTime время для пропуска
     */
    public void addSkipNanoTime(final long skipNanoTime) {
        skip2NanoTime = System.nanoTime() + skipNanoTime;
    }

    /**
     * Вычисляем цвет по рейтингу.
     * @param firstColor начальный цвет
     * @param lastColor конечный цвет
     * @param ratio рейтинг
     * @return вычисленный цвет
     */
    protected Color calcColor(final Color firstColor, final Color lastColor, final double ratio) {
        final int red = colorNormal(firstColor.getRed() + (lastColor.getRed() - firstColor.getRed()) * ratio);
        final int green = colorNormal(firstColor.getGreen() + (lastColor.getGreen() - firstColor.getGreen()) * ratio);
        final int blue = colorNormal(firstColor.getBlue() + (lastColor.getBlue() - firstColor.getBlue()) * ratio);
        return new Color(red, green, blue);
    }

    /**
     * Передать потомку свой немного изменённый ген.
     * @param gene ген родителя
     * @return ген потомка
     */
    public static double inheritGene(final double gene) {
        final double random = (Math.random() - 0.5) * gene * 5 / 100;
        return gene + random;
    }

    @Override
    public StringBuilder getInfo(final StringBuilder sb) {
        return getInfo(sb, this, this.getClass()).append('\n');
    }

    private StringBuilder getInfo(final StringBuilder sb, final Object obj, final Class<?> aClass) {
        Info info;
        info = aClass.getAnnotation(Info.class);
        if (info != null && (Arrays.binarySearch(info.type(), InfoType.INFO) >= 0)) {
            final String name = info.name();
            if (name.length() > 0) {
                sb.append("-----== ");
                if (obj instanceof AbstractAnimal) {
                    final String managerName = manager.getName();
                    final String number = ((AbstractAnimal) obj).getUniqueName().substring(managerName.length() + 1);
                    sb.append(managerName).append(" (").append(number).append(") ");
                } else {
                    sb.append(name);
                }
                sb.append("\t ==----\n");
            }
        }

        final Class<?> superclass = aClass.getSuperclass();
        if (superclass != null) {
            info = superclass.getAnnotation(Info.class);
            if (info != null && (Arrays.binarySearch(info.type(), InfoType.INFO) >= 0)) {
                getInfo(sb, obj, superclass);
            }
        }
        final Field[] annotations = aClass.getDeclaredFields();
        for (Field field : annotations) {
            info = field.getAnnotation(Info.class);
            if (info != null && (Arrays.binarySearch(info.type(), InfoType.INFO) >= 0)) {
                final String head = info.head();
                if (head.length() > 0) {
                    sb.append("--- ").append(head).append(" --- \n");
                }
                String name = info.name();
                if (name.length() == 0) {
                    name = field.getName();
                }
                String value = null;
                field.setAccessible(true);
                try {
                    switch (field.getType().getName()) {
                        case "int" :
                            if (info.rate() == 1) {
                                value = Integer.toString(field.getInt(obj));
                            } else {
                                value = LGFormatter.format(field.getDouble(obj) * info.rate());
                            }
                            break;
                        case "double" :
                            value = LGFormatter.format(field.getDouble(obj) * info.rate());
                            break;
                        case "java.lang.String" :
                            value = (java.lang.String) field.get(obj);
                            break;
                        default:
                            final Object fieldObj = field.get(obj);
                            if (fieldObj instanceof Map) {
                                @SuppressWarnings("unchecked")
                                final Map<Object, Object> map = (Map<Object, Object>) fieldObj;
                                for (Map.Entry<Object, Object> entry : map.entrySet()) {
                                    final String key = entry.getKey().toString();
                                    final String valStr;
                                    final Object val = entry.getValue();
                                    if (val instanceof Double) {
                                        valStr = LGFormatter.format((Double) val * info.rate());
                                    } else if (val instanceof Integer) {
                                        valStr = Integer.toString((Integer) val);
                                    } else {
                                        valStr = val.toString();
                                    }
                                    sb.append(name).append('.').append(key).append(" \t").append(valStr).append('\n');
                                }
                            } else if (fieldObj instanceof AnimalState) {
                                sb.append(name).append(" \t").append(((AnimalState) fieldObj).name).append('\n');
                            } else {
                                sb.append("--- ").append(name).append(" --- \n");
                                getInfo(sb, fieldObj, fieldObj.getClass());
                            }
                            break;
                    }
                } catch (final IllegalAccessException ex) {
                    log.error(ex.getMessage(), ex);
                }
                if (value != null) {
                    sb.append(name).append(" \t").append(value).append('\n');
                }
            }
        }
        return sb;
    }

    @Override
    public StringBuilder getJsonInfo(final String curState) {
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"животное\":\"").append(getManager().getName()).append("\",");
        sb.append("\"количество\":").append(getManager().getCount()).append(",");
        sb.append("\"кличка\":\"").append(getUniqueName()).append("\",");
        sb.append("\"состояние\":\"").append(curState).append("\",");
        sb.append("\"масса\":").append(LGFormatter.format(getMass())).append(",");
        sb.append("\"энергия\":").append(LGFormatter.format(getEnergy())).append(",");
        return sb;
    }

    /**
     * знак совершённого обеда (для отчётности).
     */
    public abstract void incDines();

    public Stomach getStomach() {
        return stomach;
    }

    public void setStomach(final Stomach stomach) {
        this.stomach = stomach;
    }
}
