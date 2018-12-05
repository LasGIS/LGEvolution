/**
 * @(#)Mouse.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal;

import com.lasgis.evolution.map.Cell;
import com.lasgis.evolution.map.CellHelper;
import com.lasgis.evolution.map.NearPoint;
import com.lasgis.evolution.object.EvolutionConstants;
import com.lasgis.evolution.object.Info;
import com.lasgis.evolution.object.InfoType;
import com.lasgis.evolution.object.animal.organs.FoodIndex;
import com.lasgis.evolution.object.animal.organs.Legs;
import com.lasgis.evolution.object.animal.organs.Stomach;
import com.lasgis.evolution.panels.Scalable;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

import static com.lasgis.evolution.object.animal.AnimalState.analise;
import static com.lasgis.evolution.object.animal.AnimalState.birth;
import static com.lasgis.evolution.object.animal.AnimalState.died;
import static com.lasgis.evolution.object.animal.AnimalState.eat;
import static com.lasgis.evolution.object.animal.AnimalState.fecal;
import static com.lasgis.evolution.object.animal.AnimalState.sleep;
import static com.lasgis.evolution.object.animal.AnimalState.stress;

/**
 * Мышка ест овёс и шевелится.
 * @author vladimir.laskin
 * @version 1.0
 */
@Slf4j
@Info(name = EvolutionConstants.MOUSE_KEY, type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
public class Mouse extends AbstractAnimal implements CallBack, EvolutionConstants {

    private static final Color STRESS_COLOR = new Color(187, 110, 200);
    private static final Color YOUNG_COLOR = new Color(153, 153, 0);
    private static final Color OLD_COLOR = new Color(240, 240, 220);
    private static final double MAXIMAL_AGE = 10000.0;
    private static final double MIN_GRASS_FOR_EAT = 20.0;

    @Info(name = "возраст", rate = 0.01, type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private int age = 0;
    @Info(name = "снов", type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private int sleeps = 0;
    @Info(name = "обедов", type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private int dines = 0;
    @Info(name = "рождений", type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private int births = 0;
    @Info(name = "стрессов", type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private int stresses = 0;
    @Info(type = { InfoType.SAVE })
    private String keyBestFood = null;

    /**
     * В момент создания присваиваем уникальное имя.
     * @param latitude широта точки
     * @param longitude долгота точки
     * @param manager манагер всех мышек
     * для самоубийства и рождения нового.
     */
    protected Mouse(final double latitude, final double longitude, final MouseManager manager) {
        super(latitude, longitude, manager);
        setMass(50);
        setEnergy(50);
        stomach = new Stomach(0.5, this);
        stomach.setDigestion(GRASS_PLANT_KEY, 0.4);
        stomach.setDigestion(BARLEY_PLANT_KEY, 0.4);
        stomach.setDigestion(HAY_PLANT_KEY, 0.2);
        stomach.digestionNormalizing();
    }

    @Override
    public double getRelativeAge() {
        return age / MAXIMAL_AGE;
    }

    @Override
    public void draw(final Graphics gr, final Scalable interval) {
        final int x = interval.toScreenX(getLatitude(), getLongitude());
        final int y = interval.toScreenY(getLatitude(), getLongitude());
        final int cellSize = interval.getCellSize();
        final double massSize = cellSize * getFullMass() / 400;
        final double energySize = cellSize * getEnergy() / 300;
        if (getState() == stress) {
            gr.setColor(STRESS_COLOR);
        } else {
            gr.setColor(stomach.calcColor(OLD_COLOR, age / MAXIMAL_AGE));
        }
        final int[] xPnt = {(int) (x - massSize), x, (int) (x + massSize)};
        final int[] yPnt = {(int) (y + energySize), (int) (y - energySize), (int) (y + energySize)};
        gr.fillPolygon(xPnt, yPnt, 3);
        if (isSelected()) {
            gr.setColor(SELECTED_COLOR);
            gr.drawPolygon(xPnt, yPnt, 3);
        }
        final Font font = new Font("Arial", Font.PLAIN, (int) (massSize / 2.5));
        gr.setFont(font);
        gr.setColor(Color.black);
        gr.drawString(getState().toString(), (int) (x - massSize + 2), y + 2);
    }

    @Override
    public double getFullMass() {
        return getMass() + stomach.getMass();
    }

    @Override
    protected boolean action() {

        final Cell cell = getCell();

        if (age++ > MAXIMAL_AGE) {
            setState(died);
        } else if (getEnergy() < 10) {
            setState(sleep);
            sleeps++;
        } else if (getMass() < 10) {
            setState(died);
        } else if (stomach.isNeedToDigestion()) {
            setState(sleep);
            sleeps++;
        } else if (stomach.isNeedToDefecation()) {
            setState(fecal);
        } else if (getMass() > 100 && getState() != sleep) {
            setState(birth);
        }
        FoodIndex foodIndex;
        //double barley;

        stomach.action(cell);
        switch (getState()) {
            case analise:
                keyBestFood = null;
                // последовательность перебора ячеек
                final NearPoint[] sequence = CellHelper.getNearPoints(5, false);
                foodIndex = stomach.bestFoodIndex(cell, 0.0);
                Cell cellBest = cell;

                // находим самое вкусное место
                for (NearPoint seq : sequence) {
                    final Cell cellInd = cell.getCell(seq);
                    final FoodIndex foodIndexT = stomach.bestFoodIndex(cellInd, seq.getDistance());
                    if (foodIndex.getBestIndex() < foodIndexT.getBestIndex() && checkOnAppropriate(cellInd, false)) {
                        foodIndex = foodIndexT;
                        cellBest = cellInd;
                    }
                }
                if (foodIndex.getBestKey() != null) {
                    if (foodIndex.getValue() < MIN_GRASS_FOR_EAT) {
                        setState(analise);
                        if (cellBest != cell) {
                            getLegs().moveTo(cellBest, 20, Legs.MoveType.velocity);
                        }
                    } else {
                        keyBestFood = foodIndex.getBestKey();
                        setState(eat);
                        if (cellBest != cell) {
                            getLegs().moveTo(cellBest, 50, Legs.MoveType.velocity);
                        }
                    }
                } else {
                    setState(stress);
                    stresses++;
                }
                break;
            case birth:
                toBirth();
                setState(sleep);
                sleeps++;
                break;
            case stress:
                changeEnergy(-1);
                changeMass(-1);
                age += 20;
                setState(eat);
                break;
            case sleep:
                if (getEnergy() < getMass()) {
                    changeEnergy(1);
                    changeMass(-0.2);
                }
                if ((getEnergy() >= getMass()) && stomach.isEmpty()) {
                    if (keyBestFood != null) {
                        setState(eat);
                    } else {
                        setState(analise);
                    }
                }
                break;
            case run:
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
                //cell.element(MOUSE_TARGET_KEY).setValue(0);
                return false;
            default:
                break;
        }
        return true;
    }

    /**
     * Check if it cell is Appropriate for move.
     * @param cell checked cell
     * @param isSame свинья находится на этом месте
     * @return true if checked cell is Appropriate for move.
     */
    private boolean checkOnAppropriate(final Cell cell, final boolean isSame) {
        return (
            cell.element(MOUSE_KEY).value() - (isSame ? 1 : 0) < 0.9
            //&& cell.element(MOUSE_TARGET_KEY).value() < 0.9
        );
    }

    /**
     * Рожаем новую мышку с наследованием.
     */
    public void toBirth() {
        final Mouse newMouse = new Mouse(getLatitude(), getLongitude(), (MouseManager) getManager());
        newMouse.setMass(20);
        newMouse.setEnergy(20);
        changeMass(-20);
        changeEnergy(-20);

        // передача генотипа
        newMouse.setStomach(new Stomach(stomach, newMouse));

        getManager().addNewAnimal(newMouse, false);
        births++;
    }

    @Override
    public boolean callBackAction(final CallActionType actionType) {
        switch (actionType) {
            case onMoveTo:
                stomach.action(getCell());
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
        sb.append("}");
        return sb;
    }

    @Override
    public void incDines() {
        dines++;
    }
}
