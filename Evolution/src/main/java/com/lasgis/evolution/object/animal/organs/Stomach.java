/**
 * @(#)Stomach.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.organs;

import com.lasgis.evolution.map.Cell;
import com.lasgis.evolution.object.EvolutionConstants;
import com.lasgis.evolution.object.Info;
import com.lasgis.evolution.object.InfoType;
import com.lasgis.evolution.object.animal.AbstractAnimal;
import com.lasgis.util.LGFormatter;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static com.lasgis.evolution.object.EvolutionConstants.BARLEY_NUTRITIONAL;
import static com.lasgis.evolution.object.EvolutionConstants.BARLEY_PLANT_KEY;
import static com.lasgis.evolution.object.EvolutionConstants.CHAMOMILE_FLOWER_KEY;
import static com.lasgis.evolution.object.EvolutionConstants.CHAMOMILE_FLOWER_NUTRITIONAL;
import static com.lasgis.evolution.object.EvolutionConstants.CHAMOMILE_LEAF_KEY;
import static com.lasgis.evolution.object.EvolutionConstants.CHAMOMILE_LEAF_NUTRITIONAL;
import static com.lasgis.evolution.object.EvolutionConstants.GRASS_NUTRITIONAL;
import static com.lasgis.evolution.object.EvolutionConstants.GRASS_PLANT_KEY;
import static com.lasgis.evolution.object.EvolutionConstants.HAY_NUTRITIONAL;
import static com.lasgis.evolution.object.EvolutionConstants.HAY_PLANT_KEY;
import static com.lasgis.evolution.object.EvolutionConstants.HONEY_KEY;
import static com.lasgis.evolution.object.EvolutionConstants.HONEY_NUTRITIONAL;
import static com.lasgis.evolution.object.EvolutionConstants.MEAT_NUTRITIONAL;
import static com.lasgis.evolution.object.EvolutionConstants.MEAT_PLANT_KEY;
import static com.lasgis.evolution.object.EvolutionConstants.NECTAR_KEY;
import static com.lasgis.evolution.object.EvolutionConstants.NECTAR_NUTRITIONAL;
import static com.lasgis.evolution.object.animal.AnimalState.analise;
import static com.lasgis.evolution.object.animal.AnimalState.fecal;

/**
 * Желудочно-кишечный тракт определяет пищеварение.
 * Разделяется на два отдела:<br/>
 * желудок - в нём хранится съеденная животным пища
 * (трава, мясо, овёс...);<br/>
 * кишечник - здесь храниться переработанные остатки (фекалии).<br/>
 * В процессе жизни пища перерабатывается в остатки
 * с выделением энергии.
 * @author vladimir.laskin
 * @version 1.0
 */
public class Stomach {

    private static final double FOOD_PRESS = 0.999;
    private static final double FOOD_DINE = 5;
    private static final double FOOD_STOMACH = FOOD_DINE * FOOD_PRESS;
    private static final Map<String, Double> NUTRITIONAL = new HashMap<String, Double>();

    /** Размер желудка в процентах от общей массы животного. */
    @Info(name = "Размер желудка", type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private double massPercent;
    /** масса травы, овса или мяса, находящаяся в желудке. */
    @Info(name = "масса", type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private HashMap<String, Double> stomach = new HashMap<>();
    /** Степень переваривания отдельных видов пищи. */
    @Info(name = "степень", type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private HashMap<String, Double> digestion = new HashMap<>();
    /** масса фекалий, находящаяся в кишках. Если масса превышает порог, то необходима defecation */
    @Info(name = "масса фекалий", type = { InfoType.INFO, InfoType.SAVE })
    private double intestine = 0;
    /** коэффициент влияющий на выбор самого вкусного места. Чем он больше, тем ближе выбор. */
    @Info(name = "фактор дистанции.", rate = 100., type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private double distanceFactor = 0.5;
    private AbstractAnimal owner;
    /** цвет животного, различный от перевариваемости той или иной пищи. */
    private static final HashMap<String, Color> ANIMAL_COLORS = new HashMap<String, Color>();

    static {
        NUTRITIONAL.put(GRASS_PLANT_KEY, GRASS_NUTRITIONAL);
        NUTRITIONAL.put(BARLEY_PLANT_KEY, BARLEY_NUTRITIONAL);
        NUTRITIONAL.put(MEAT_PLANT_KEY, MEAT_NUTRITIONAL);
        NUTRITIONAL.put(HAY_PLANT_KEY, HAY_NUTRITIONAL);
        NUTRITIONAL.put(CHAMOMILE_LEAF_KEY, CHAMOMILE_LEAF_NUTRITIONAL);
        NUTRITIONAL.put(CHAMOMILE_FLOWER_KEY, CHAMOMILE_FLOWER_NUTRITIONAL);
        NUTRITIONAL.put(NECTAR_KEY, NECTAR_NUTRITIONAL);
        NUTRITIONAL.put(HONEY_KEY, HONEY_NUTRITIONAL);
        ANIMAL_COLORS.put(GRASS_PLANT_KEY, new Color(0, 0, 255));
        ANIMAL_COLORS.put(BARLEY_PLANT_KEY, new Color(0, 255, 0));
        ANIMAL_COLORS.put(MEAT_PLANT_KEY, new Color(255, 0, 0));
        ANIMAL_COLORS.put(HAY_PLANT_KEY, new Color(255, 255, 0));
        ANIMAL_COLORS.put(CHAMOMILE_LEAF_KEY, new Color(0, 253, 255));
        ANIMAL_COLORS.put(CHAMOMILE_FLOWER_KEY, new Color(200, 255, 230));
        ANIMAL_COLORS.put(NECTAR_KEY, new Color(253, 200, 200));
        ANIMAL_COLORS.put(HONEY_KEY, new Color(253, 150, 0));
    }

    /**
     * Конструктор.
     * @param massPercent Размер желудка в процентах
     * от общей массы животного
     * @param owner владелец желудка (бедное животное)
     * Может быть равно null. В этом случае нет предка.
     * Передаётся по наследству с мутационными изменениями.
     */
    public Stomach(final double massPercent, final AbstractAnimal owner) {
        this.massPercent = massPercent;
        this.owner = owner;
    }

    /**
     * Конструктор.
     * @param parent желудок родителя данного животного
     * (передаётся по наследству)
     * @param owner владелец желудка (бедное животное)
     * Может быть равно null. В этом случае нет предка.
     * Передаётся по наследству с мутационными изменениями.
     */
    public Stomach(final Stomach parent, final AbstractAnimal owner) {
        this.massPercent = AbstractAnimal.inheritGene(parent.massPercent);
        this.distanceFactor = AbstractAnimal.inheritGene(parent.distanceFactor);
        this.owner = owner;
        for (Map.Entry<String, Double> entry : parent.digestion.entrySet()) {
            double degree = entry.getValue();
            degree = degree + Math.random() * 0.1 - 0.05;
            if (degree > 1.0/* && !EvolutionConstants.MEAT_PLANT_KEY.equals(entry.getKey())*/) {
                degree = 1.0;
            }
            if (degree < 0.0) {
                degree = 0.0;
            }
            setDigestion(entry.getKey(), degree);
        }
        digestionNormalizing();
    }

    /**
     * Устанавливаем степень переваривания конкретной пищи.
     * @param key название пищи
     * @param degree степень переваривания
     */
    public void setDigestion(final String key, final double degree) {
        digestion.put(key, degree);
        stomach.put(key, 0.0);
    }

    /**
     * Нормализация степеней переваривания.
     * Т.е. в сумме они должны быть равны единице.
     */
    public void digestionNormalizing() {
        double sum = 0;
        for (Map.Entry<String, Double> entry : digestion.entrySet()) {
            //if (!entry.getKey().equals(EvolutionConstants.MEAT_PLANT_KEY)) {
            sum += entry.getValue();
            //}
        }
        for (Map.Entry<String, Double> entry : digestion.entrySet()) {
            //if (!entry.getKey().equals(EvolutionConstants.MEAT_PLANT_KEY)) {
            entry.setValue(entry.getValue() / sum);
            //}
        }
    }

    /**
     * Процесс переработки пищи. В результате которого животное
     * получает энергию и массу.
     * @param cell ячейка матрицы
     */
    public void action(final Cell cell) {
        // работа желудка
        for (Map.Entry<String, Double> entry : stomach.entrySet()) {
            final String key = entry.getKey();
            double mass = entry.getValue();
            final double nutritional = getNutritional(key);
            final double degree = digestion.get(key) / FOOD_PRESS;
            if (mass > 0) {
                mass -= 1;
                if (mass < 0) {
                    mass = 0;
                }
                entry.setValue(mass);
                intestine += 0.2;
                if (owner.getEnergy() < owner.getMass()) {
                    owner.changeEnergy(degree * nutritional);
                } else {
                    owner.changeMass(0.2 * degree * nutritional);
                }
            }
        }
        if (isNeedToDefecation()) {
            owner.setState(fecal);
        }
    }

    /**
     * Элементарный акт дефекации.
     * @return true if intestine is empty
     */
    public boolean defecation(final Cell cell) {
        // работа кишечника
        if (intestine > 0) {
            cell.element(EvolutionConstants.EXCREMENT_KEY).incValue();
            intestine --;
        }
        if (intestine <= 0) {
            intestine = 0;
            return true;
        } else {
            return  false;
        }
    }

    private double getNutritional(final String key) {
        final Double nutritional = NUTRITIONAL.get(key);
        if (nutritional == null) {
            return 1.0;
        } else {
            return nutritional;
        }
    }

    /**
     * Масса желудка со всем содержимым.
     * @return масса желудка
     */
    public double getMass() {
        double mass = 0;
        for (Map.Entry<String, Double> entry : stomach.entrySet()) {
            mass += entry.getValue();
        }
        return mass + intestine;
    }

    /**
     * Размер доступного места в желудке.
     * @return размер
     */
    public double getFreeSize() {
        double mass = 0;
        for (Map.Entry<String, Double> entry : stomach.entrySet()) {
            mass += entry.getValue();
        }
        return owner.getMass() * massPercent - mass;
    }

    /**
     * Процесс поедания пищи проходом, что  без установкой состояния.
     *
     * @param cell ячейка матрицы, на которой пасётся животное
     * @return если true - значит обедали.
     */
    public boolean dine(final Cell cell) {
        return dine(cell, null, false);
    }

    /**
     * Процесс поедания пищи по старому, с установкой состояния.
     *
     * @param cell ячейка матрицы, на которой пасётся животное
     * @param key название поедаемой пищи. Может быть null
     * @return если true - значит обедали.
     */
    public boolean dine(final Cell cell, final String key) {
        return dine(cell, key, true);
    }

    /**
     * Процесс поедания пищи.
     *
     * @param cell ячейка матрицы, на которой пасётся животное
     * @param key название поедаемой пищи. Может быть null
     * @param isState если true, то устанавливаем состояние analise("поиск")
     * @return если true - значит обедали.
     */
    public boolean dine(final Cell cell, String key, final boolean isState) {
        boolean isSetAnalise = false;
        boolean isDined = false;
        if (key == null) {
            key = bestFoodIndex(cell, 0.0).getBestKey();
        }
        if (key != null) {
            double dinePart = FOOD_DINE;
            if (cell.element(key).value() < FOOD_DINE * 2) {
                dinePart = cell.element(key).value() - FOOD_DINE;
                isSetAnalise = isState;
            } else {
                isDined = true;
            }
            if (dinePart > 0 && getFreeSize() >= dinePart * FOOD_PRESS) {
                owner.incDines();
                cell.element(key).decValue(dinePart);
                stomach.put(key, stomach.get(key) + dinePart * FOOD_PRESS);
//            } else {
//                isDined = false;
            }
            if (isSetAnalise) {
                owner.setState(analise);
            }
        } else if (isState) {
            owner.setState(analise);
        }
        return isDined;
    }

    /**
     * Определение индекса ячейки по еде. чем больше еды
     * и лучше она усваивается, тем больше индекс.
     *
     * @param cell ячейка матрицы, на которой пасётся животное
     * @param distance расстояние до еды в ячейках
     * @return индекс лучшей еды.
     */
    public FoodIndex bestFoodIndex(final Cell cell, final double distance) {
        final FoodIndex foodIndex = new FoodIndex();
        final double massCorrection = distance * distanceFactor; //  * owner.getFullMass() ???
        final double distanceCorrection = (massCorrection > 1) ? 1 / massCorrection : 1 + (1. - massCorrection) * .5;
        double sumIndex = 0.0;
        for (Map.Entry<String, Double> entry : digestion.entrySet()) {
            final String key = entry.getKey();
            final double degree = entry.getValue();
            final double nutritional = getNutritional(key);
            double mass = cell.element(key).value();
            mass = (mass > 0.001) ? mass : 0.0;
            // индекс соответствует энергии получаемой от переваривания
            final double index = mass * degree * nutritional;
            sumIndex += index;
            if (index > 0 && index * distanceCorrection > foodIndex.getBestIndex()) {
                foodIndex.setBestIndex(index * distanceCorrection);
//            if (index > 0 && index * distanceCorrection > foodIndex.getBestIndex()) {
//                foodIndex.setBestIndex(index * distanceCorrection);
                foodIndex.setBestKey(key);
                foodIndex.setValue(mass);
            }
        }
        //foodIndex.setBestIndex(sumIndex - distanceCorrection);
        return foodIndex;
    }

    /**
     * Необходима пауза для переваривания пиши. Т.е. желудок заполнен.
     * @return если true, то желудок заполнен.
     */
    public boolean isNeedToDigestion() {
        double mass = 0;
        for (Map.Entry<String, Double> entry : stomach.entrySet()) {
            mass += entry.getValue();
        }
        return mass > owner.getMass() * massPercent * 0.95;
    }

    /**
     * Необходима дефекация.
     * @return если true, то крайне желательна
     */
    public boolean isNeedToDefecation() {
        return intestine > owner.getMass() * massPercent * 0.5;
    }

    /**
     * проверка желудка.
     * @return true если желудок пуст
     */
    public boolean isEmpty() {
        double mass = 0;
        for (Map.Entry<String, Double> entry : stomach.entrySet()) {
            mass += entry.getValue();
        }
        return mass < 1;
    }

    /**
      * Показать информацию о желудке.
      * @param sb StringBuilder
      */
    public void getInfo(final StringBuilder sb) {
        double mass = 0;
        for (Map.Entry<String, Double> entry : stomach.entrySet()) {
            mass += entry.getValue();
        }
        sb.append("желудок \t").append(LGFormatter.format(mass)).append('\n');
        sb.append("кишечник \t").append(LGFormatter.format(intestine)).append('\n');
        for (Map.Entry<String, Double> entry : digestion.entrySet()) {
            sb.append("степень.").append(entry.getKey()).append(" \t")
                .append(LGFormatter.format(entry.getValue())).append('\n');
        }
    }

    /**
     * Возвращаем информацию о генотипе.
     * @param sb StringBuilder
     */
    public void jsonInfo(final StringBuilder sb) {
        sb.append("\"размер желудка\":").append(LGFormatter.formatLog(massPercent)).append(",");
        for (Map.Entry<String, Double> entry : digestion.entrySet()) {
            sb.append("\"желудок.степень.").append(entry.getKey()).append("\":")
                .append(LGFormatter.formatLog(entry.getValue())).append(",");
        }
    }

    /**
     * Вычисляем цвет животного по степени перевариваемости и возрасту.
     * @param oldColor цвет животного при смерти
     * @param age возраст животного
     * @return вычисленный цвет
     */
    public Color calcColor(final Color oldColor, final double age) {
        double r = 0;
        double g = 0;
        double b = 0;
        for (String key : digestion.keySet()) {
            final Color col = ANIMAL_COLORS.get(key);
            final double factor = digestion.get(key);
            r += col.getRed() * factor;
            g += col.getGreen() * factor;
            b += col.getBlue() * factor;
        }
        return new Color(
            AbstractAnimal.colorNormal(r + (oldColor.getRed() - r) * age),
            AbstractAnimal.colorNormal(g + (oldColor.getGreen() - g) * age),
            AbstractAnimal.colorNormal(b + (oldColor.getBlue() - b) * age)
        );
    }

    /**
     * n
     * @param key k
     * @return k
     */
    public double getDigestion(final String key) {
        final Double val = digestion.get(key);
        return val != null ? val : 0.0;
    }
}
