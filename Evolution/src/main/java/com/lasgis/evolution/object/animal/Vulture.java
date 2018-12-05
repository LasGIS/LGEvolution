/**
 * @(#)Vulture.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal;

import com.lasgis.evolution.draw.DrawPrimitive;
import com.lasgis.evolution.map.Cell;
import com.lasgis.evolution.map.CellHelper;
import com.lasgis.evolution.map.Matrix;
import com.lasgis.evolution.map.NearPoint;
import com.lasgis.evolution.object.AnimalBehaviour;
import com.lasgis.evolution.object.EvolutionConstants;
import com.lasgis.evolution.object.Info;
import com.lasgis.evolution.object.InfoType;
import com.lasgis.evolution.object.animal.organs.FoodIndex;
import com.lasgis.evolution.object.animal.organs.ForceVector;
import com.lasgis.evolution.object.animal.organs.Legs;
import com.lasgis.evolution.object.animal.organs.Stomach;
import com.lasgis.evolution.panels.Scalable;
import com.lasgis.util.LGFormatter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lasgis.evolution.object.animal.AnimalState.analise;
import static com.lasgis.evolution.object.animal.AnimalState.birth;
import static com.lasgis.evolution.object.animal.AnimalState.died;
import static com.lasgis.evolution.object.animal.AnimalState.eat;
import static com.lasgis.evolution.object.animal.AnimalState.fecal;
import static com.lasgis.evolution.object.animal.AnimalState.sleep;
import static com.lasgis.evolution.object.animal.AnimalState.stress;

//import org.slf4j.Logger; import org.slf4j.LoggerFactory;

/**
 * Гриф не ест травы, только мясо, но сам его не добывает.
 *
 * @author Laskin
 * @version 1.0
 * @since 23.03.13 10:43
 */
@Slf4j
@Info(name = EvolutionConstants.VULTURE_KEY, type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
public class Vulture extends AbstractAnimal implements CallBack, EvolutionConstants {

    private static final double MOVE_COEFFICIENT = Matrix.CELL_SIZE * 2;
    private static final double MASS_FACTOR = 1 / (Matrix.CELL_SIZE * Matrix.CELL_SIZE * 300);

    double angle = 0.0;

    @Info(name = "возраст", rate = 0.01, type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private int age = 0;
    @Info(name = "снов", type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private int sleeps = 0;
    @Info(name = "обедов", type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private int dines = 0;
    @Info(name = "рождений", type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private int births = 0;
/*  @Info(name = "стрессов", type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private int stresses = 0; */
    @Info(type = { InfoType.SAVE })
    private int sleepTime = 0;
    @Info(type = { InfoType.SAVE })
    private String keyBestFood = null;

    // параметры, определяемые геномом
    /** минимальное количество травы для поедания. */
    @Info(name = "минимум пищи", head = "ГЕНОТИП", type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private double minGrassForEat = 30;
    /** энергия бега за едой. */
    @Info(name = "энергия бега", rate = 1000, type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private double runEnergy = 0.21;
    /** энергия передвижения в момент задумчивости. */
    @Info(name = "энергия пешком", rate = 1000, type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private double walkEnergy = 0.05;
    /** Необходимая масса для начала родов. */
    @Info(name = "масса родителя", type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private double birthMass = 200;
    /** Начальная масса нового грифа. */
    @Info(name = "масса детеныша", type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private double birthBabyMass = 100;
    /** Время жизни грифа. */
    @Info(name = "время жизни", rate = .01, type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private double maximalAge = 15000.0;

    /**
     * В момент создания присваиваем уникальное имя.
     * @param latitude широта точки
     * @param longitude долгота точки
     * @param manager манагер всех кроликов
     * для самоубийства и рождения нового.
     */
    protected Vulture(final double latitude, final double longitude, final VultureManager manager) {
        super(latitude, longitude, manager);
        setMass(100);
        setEnergy(100);
        stomach = new Stomach(0.5, this);
        //stomach.setDigestion(GRASS_PLANT_KEY, 0.4);
        //stomach.setDigestion(BARLEY_PLANT_KEY, 0.2);
        stomach.setDigestion(MEAT_PLANT_KEY, 0.4);
        stomach.digestionNormalizing();
    }

    @Override
    public double getRelativeAge() {
        return age / maximalAge;
    }

    public void setMinGrassForEat(final double minGrassForEat) {
        this.minGrassForEat = minGrassForEat;
    }

    public void setRunEnergy(final double runEnergy) {
        this.runEnergy = runEnergy;
    }

    public void setWalkEnergy(final double walkEnergy) {
        this.walkEnergy = walkEnergy;
    }

    public void setBirthMass(final double birthMass) {
        this.birthMass = birthMass;
    }

    public void setBirthBabyMass(final double birthBabyMass) {
        this.birthBabyMass = birthBabyMass;
    }

    public void setMaximalAge(final double maximalAge) {
        this.maximalAge = maximalAge;
    }

    @Override
    public void incDines() {
        dines++;
    }

    @Override
    public void draw(final Graphics gr, final Scalable interval) {
        final int x = interval.toScreenX(getLatitude(), getLongitude());
        final int y = interval.toScreenY(getLatitude(), getLongitude());
        final int cellSize = interval.getCellSize();
        final double massSize = cellSize * getFullMass();
        final double energySize = cellSize * getEnergy();
        final Map<String, Double> prop = new HashMap<>();
        prop.put("масса", massSize);
        prop.put("энергия", energySize);
        prop.put("возраст", age / maximalAge);
        if (isSelected()) {
            prop.put("selected", 1.0);
        }
        if (getState() == stress) {
            prop.put("stress", 1.0);
        }
        DrawPrimitive.drawPolygon(gr, DrawPrimitive.VULTURE_PRIMITIVE, prop, x, y);
        final Font font = new Font("Arial", Font.PLAIN, (int) (massSize / 1000));
        gr.setFont(font);
        gr.setColor(Color.black);
        gr.drawString(getState().toString(), (int) (x - massSize / 400 + 2), y + 2);
    }

    public double getFullMass() {
        return getMass() + stomach.getMass();
    }

    @Override
    protected boolean action() {

        final Cell cell = getCell();

        if (age++ > maximalAge) {
            setState(died);
        } else if (getEnergy() <= 10) {
            setState(sleep);
            sleeps++;
        } else if (getMass() <= 10) {
            setState(died);
        } else if (stomach.isNeedToDigestion()) {
            setState(sleep);
            sleeps++;
        } else if (stomach.isNeedToDefecation()) {
            setState(fecal);
        } else if (getMass() > birthMass && getState() != sleep) {
            setState(birth);
        }
        FoodIndex foodIndex;
        ForceVector force;

        stomach.action(cell);
        switch (getState()) {
            case analise:
                keyBestFood = null;
                // последовательность перебора ячеек
                final NearPoint[] sequence = CellHelper.getNearPoints(30, false);
                foodIndex = stomach.bestFoodIndex(cell, 0.0);
                Cell cellBest = cell;
                final double vulture = cell.element(VULTURE_KEY).value();
                force = new ForceVector(vulture - 1);

                // находим самое вкусное место
                for (NearPoint seq : sequence) {
                    final Cell cellInd = cell.getCell(seq.getX(), seq.getY());

                    final FoodIndex foodIndexT = stomach.bestFoodIndex(cellInd, seq.getDistance());

                    if (foodIndex.getBestIndex() < foodIndexT.getBestIndex() && checkOnAppropriate(cellInd, false)) {
                        foodIndex = foodIndexT;
                        cellBest = cellInd;
                    }
                    final List<AnimalBehaviour> animals = cellInd.getAnimals();
                    for (AnimalBehaviour animal : animals) {
                        final String name = animal.getManager().getName();
                        if (!this.getUniqueName().equals(animal.getUniqueName())) {
                            if (VULTURE_KEY.equals(name)) {
                                // встретил орла
                                force.sub(new ForceVector(
                                    animal.getLatitude() - this.getLatitude(),
                                    animal.getLongitude() - this.getLongitude(),
                                    animal.getMass() * MASS_FACTOR
                                ));
                            } else {
                                final double relativeAge = animal.getRelativeAge();
                                if (relativeAge > 0.8) {
                                    // встретил другое животное
                                    force.add(new ForceVector(
                                        animal.getLatitude() - this.getLatitude(),
                                        animal.getLongitude() - this.getLongitude(),
                                        animal.getMass() * MASS_FACTOR * (relativeAge - 0.8) / 0.2
                                    ));
                                }
                            }
                        }
                    }
/*
                    vulture = cellInd.element(VULTURE_KEY).value();
                    if (vulture > 0.9) {
                        force.sub(new ForceVector(seq.getX(), seq.getY(), vulture));
                    }
                    double target = cellInd.element(VULTURE_TARGET_KEY).value();
                    if (target > 0.9) {
                        force.sub(new ForceVector(seq.getX(), seq.getY(), target / 3));
                    }
                    target = cellInd.element(TARGET_OF_DEATH).value();
                    if (target > 0.9) {
                        force.add(new ForceVector(seq.getX(), seq.getY(), target / 3));
                    }
*/
                }
                boolean isNotMoved = true;
                if (foodIndex.getBestKey() != null) {
                    keyBestFood = foodIndex.getBestKey();
                    if (cellBest != cell) {
                        cellBest.element(VULTURE_TARGET_KEY).incValue(getMass() / 30);
                    }
                    setState(eat);
                    getLegs().moveTo(cellBest, runEnergy, Legs.MoveType.energy);
                    angle = getLegs().getAngle();
                    isNotMoved = false;
                } else if (force.getForce() > 0.0) {
                    if (force.getForce() > 0.5) {
                        force.setForce(0.5);
                    }
                    angle = force.getCourse();
                    final double latitude = getLatitude() + force.getFx() * MOVE_COEFFICIENT;
                    final double longitude = getLongitude() + force.getFy() * MOVE_COEFFICIENT;
                    isNotMoved = !getLegs().moveTo(latitude, longitude, walkEnergy, Legs.MoveType.energy);
                }
                if (isNotMoved) {
                    final double size = Matrix.CELL_SIZE;
                    final double latitude = getLatitude() + size * Math.sin(angle);
                    final double longitude = getLongitude() + size * Math.cos(angle);
                    getLegs().moveTo(latitude, longitude, walkEnergy / 3, Legs.MoveType.energy);
                    angle += 0.15;
                    if (angle > 2 * Math.PI) {
                        angle -= 2 * Math.PI;
                    }
                }
                break;
            case birth:
                if (getEnergy() < 60) {
                    setState(sleep);
                    sleeps++;
                } else {
                    for (int i = 0; i < 1; i++) {
                        toBirth();
                        births++;
                    }
                    setState(sleep);
                    sleepTime = 2000;
                }
                break;
            case stress:
                changeEnergy(-1);
                changeMass(-1);
                age += 20;
                keyBestFood = null;
                setState(eat);
                break;
            case sleep:
                if (getEnergy() < getMass()) {
                    changeEnergy(1);
                    changeMass(-0.2);
                }
                if (sleepTime > 0) {
                    sleepTime -= 100;
                } else if (getEnergy() >= getMass() && stomach.isEmpty()) {
                    if (keyBestFood != null) {
                        setState(eat);
                    } else {
                        setState(analise);
                    }
                }
                break;
            case fecal:
                if (stomach.defecation(cell)) {
                    if (keyBestFood != null) {
                        setState(eat);
                    } else {
                        setState(analise);
                    }
                }
                break;
            case eat:
                stomach.dine(cell, keyBestFood);
                break;
            case died:
                cell.element(MEAT_PLANT_KEY).incValue(getMass());
                cell.element(EXCREMENT_KEY).incValue(stomach.getMass());
                cell.element(VULTURE_TARGET_KEY).setValue(0);
                return false;
            default:
                break;
        }
        return true;
    }

    /**
     * Check if it cell is Appropriate for move.
     * @param cell checked cell
     * @param isSame животное находится на этом месте
     * @return true if checked cell is Appropriate for move.
     */
    private boolean checkOnAppropriate(final Cell cell, final boolean isSame) {
        return (
            cell.element(MEAT_PLANT_KEY).value() > 0.9
            && cell.element(VULTURE_KEY).value() - (isSame ? 1 : 0) < 0.9
            && cell.element(VULTURE_TARGET_KEY).value() < 0.9);
    }

    /**
     * Рожаем нового грифа с наследованием от этого.
     */
    public void toBirth() {
        final Vulture newVulture = new Vulture(getLatitude(), getLongitude(), (VultureManager) getManager());

        // передача массы и энергии
        final double mass = getMass();
        if (mass - birthBabyMass < 50) {
            birthBabyMass = mass - 50;
        }
        newVulture.setMass(birthBabyMass);
        changeMass(-birthBabyMass);
        changeEnergy(-25);

        // передача генотипа
        newVulture.setStomach(new Stomach(stomach, newVulture));
        newVulture.setMinGrassForEat(inheritGene(minGrassForEat));
        newVulture.setRunEnergy(inheritGene(runEnergy));
        newVulture.setWalkEnergy(inheritGene(walkEnergy));
        newVulture.setBirthBabyMass(inheritGene(birthBabyMass));
        newVulture.setBirthMass(inheritGene(birthMass));
        newVulture.setMaximalAge(inheritGene(maximalAge));

        getManager().addNewAnimal(newVulture, false);
    }

    @Override
    public boolean callBackAction(final CallActionType actionType) {
        switch (actionType) {
            case onMoveTo:
                if (getLegs().getAfterMoveState() != fecal) {
                    stomach.action(getCell());
                }
                age++;
                if (getEnergy() < 4) {
                    setState(sleep);
                    return false;
                }
                break;
            default:
        }
        return true;
    }

    @Override
    public StringBuilder getJsonInfo(final String curState) {
        final StringBuilder sb = super.getJsonInfo(curState);
        stomach.jsonInfo(sb);
        sb.append("\"минимум пищи для еды\":")
            .append(LGFormatter.formatLog(minGrassForEat)).append(",");
        sb.append("\"энергия бегом\":").append(LGFormatter.formatLog(runEnergy * 1000)).append(",");
        sb.append("\"энергия пешком\":").append(LGFormatter.formatLog(walkEnergy * 1000)).append(",");
        sb.append("\"масса родителя для начала родов\":")
            .append(LGFormatter.formatLog(birthMass)).append(",");
        sb.append("\"масса новорожденного\":")
            .append(LGFormatter.formatLog(birthBabyMass)).append(",");
        sb.append("\"время жизни\":").append(LGFormatter.formatLog(maximalAge / 100)).append(",");
        sb.append("\"возраст\":").append(age / 100.).append(",");
        sb.append("\"снов\":").append(sleeps).append(",");
        //sb.append("\"стрессов\":").append(stresses).append(",");
        sb.append("\"обедов\":").append(dines).append(",");
        sb.append("\"родов\":").append(births);
        sb.append("}");
        return sb;
    }

}
