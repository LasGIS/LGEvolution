/**
 * @(#)Pig.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal;

import com.lasgis.evolution.draw.DrawPrimitive;
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
import com.lasgis.util.LGFormatter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static com.lasgis.evolution.object.animal.AnimalState.analise;
import static com.lasgis.evolution.object.animal.AnimalState.birth;
import static com.lasgis.evolution.object.animal.AnimalState.died;
import static com.lasgis.evolution.object.animal.AnimalState.eat;
import static com.lasgis.evolution.object.animal.AnimalState.fecal;
import static com.lasgis.evolution.object.animal.AnimalState.sleep;
import static com.lasgis.evolution.object.animal.AnimalState.stress;

/**
 * Свинья ест траву и передвигается.
 * @author vladimir.laskin
 * @version 1.0
 */
@Slf4j
@Info(name = EvolutionConstants.PIG_KEY, type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
public class Pig extends AbstractAnimal implements CallBack, EvolutionConstants {

    @Info(name = "возраст", rate = 0.01, type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private int age = 0;
    @Info(name = "снов", type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private int sleeps = 0;
    @Info(name = "обедов", type = { InfoType.STAT, InfoType.SAVE })
    private int dines = 0;
    @Info(name = "рождений", type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private int births = 0;
    @Info(name = "стрессов", type = { InfoType.STAT, InfoType.SAVE })
    private int stresses = 0;
    @Info(type = { InfoType.SAVE })
    private int sleepTime = 0;
    @Info(type = { InfoType.SAVE })
    private String keyBestFood = null;

    // параметры, определяемые геномом
    /** минимальное количество травы для начала поедания. */
    @Info(name = "минимум пищи", head = "ГЕНОТИП", type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private double minGrassForEat = 20;
    /** остающееся количество травы после поедания. */
    @Info(name = "остаток пищи", type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private double minGrassLeave = 10;
    /** Энергия бега за едой. */
    @Info(name = "энергия бега", rate = 1000, type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private double runEnergy = 0.21;
    /** Энергия передвижения в момент задумчивости. */
    @Info(name = "энергия пешком", rate = 1000, type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private double walkEnergy = 0.07;
    /** Необходимая масса для начала родов. */
    @Info(name = "масса родителя", type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private double birthMass = 300;
    /** Начальная масса новой свиньи. */
    @Info(name = "масса детеныша", type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private double birthBabyMass = 50;
    /** Время жизни свиньи. */
    @Info(name = "время жизни", rate = .01, type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private double maximalAge = 10000.0;
    /** Это ссылка на вождя. */
    //private Pig leader = null;
    /** число пропусков еды по причине лидера. */
    @Info(type = InfoType.SAVE)
    private int notEatCount = 0;

    /**
     * В момент создания присваиваем уникальное имя.
     * @param latitude широта точки
     * @param longitude долгота точки
     * @param manager манагер всех кроликов
     * для самоубийства и рождения нового.
     */
    protected Pig(final double latitude, final double longitude, final PigManager manager) {
        super(latitude, longitude, manager);
        setMass(100);
        setEnergy(100);
        stomach = new Stomach(0.5, this);
        stomach.setDigestion(GRASS_PLANT_KEY, 0.45);
        stomach.setDigestion(BARLEY_PLANT_KEY, 0.45);
//        stomach.setDigestion(CHAMOMILE_LEAF_KEY, 0.45);
//        stomach.setDigestion(MEAT_PLANT_KEY, 0.0);
        stomach.digestionNormalizing();
    }

    @Override
    public double getRelativeAge() {
        return age / maximalAge;
    }

    /**
     * Поиск лидера группы.
     * @return лидера
     */
/*
    private Pig getLeader() {
        if (leader != null && leader.getState() != died
            && getCell().distance(leader.getCell()) < Matrix.CELL_SIZE * 10 && notEatCount < 10) {
            return leader;
        } else {
            notEatCount = 0;
            NearPoint[] sequence = CellHelper.getNearPoints(20, true);
            Cell cell = getCell();
            for (NearPoint nearPoint : sequence) {
                Cell near = cell.getCell(nearPoint);
                List<AnimalBehaviour> animals = near.getAnimals();
                for (AnimalBehaviour animal : animals) {
                    if (animal instanceof Pig
                        && !animal.equals(this)
                        && ((Pig) animal).isLeader()
                        && (leader == null || !animal.equals(leader))) {
                        leader = (Pig) animal;
                        return leader;
                    }
                }
            }
            leader = this;
            return leader;
        }
    }
*/

    /**
     * Это лидер!
     * @ return true если это лидер.
     */
/*
    public boolean isLeader() {
        return leader != null && leader.equals(this);
    }
*/

    public void setMinGrassForEat(final double minGrassForEat) {
        this.minGrassForEat = minGrassForEat;
    }

    public void setMinGrassLeave(final double minGrassLeave) {
        this.minGrassLeave = minGrassLeave;
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
        prop.put(GRASS_PLANT_KEY, stomach.getDigestion(GRASS_PLANT_KEY));
        prop.put(CHAMOMILE_LEAF_KEY, stomach.getDigestion(CHAMOMILE_LEAF_KEY));
        prop.put(BARLEY_PLANT_KEY, stomach.getDigestion(BARLEY_PLANT_KEY));
        prop.put(MEAT_PLANT_KEY, stomach.getDigestion(MEAT_PLANT_KEY));
        prop.put(HAY_PLANT_KEY, stomach.getDigestion(HAY_PLANT_KEY));
        if (isSelected()) {
            prop.put("selected", 1.0);
        }
        if (getState() == stress) {
            prop.put("stress", 1.0);
        }
        DrawPrimitive.drawPolygon(gr, DrawPrimitive.PIG_PRIMITIVE, prop, x, y);
        final Font font = new Font("Arial", Font.PLAIN, (int) (massSize / 1000));
        gr.setFont(font);
        gr.setColor(Color.black);
        gr.drawString(getState().toString(), (int) (x - massSize / 400 + 2), y + 2);
        gr.setColor(Color.cyan);
/*
        if (isLeader()) {
            int dx = (int) (massSize / 2000);
            int dy = (int) (energySize / 1000);
            gr.fillOval(x - dx / 2, y - dy / 2, dx, dy);
        } else if (leader != null) {
            double leaderLatitude = CellHelper.normLatitude(leader.getLatitude(), getLatitude());
            double leaderLongitude = CellHelper.normLongitude(leader.getLongitude(), getLongitude());
            int x1 = interval.toScreenX(leaderLatitude, leaderLongitude);
            int y1 = interval.toScreenY(leaderLatitude, leaderLongitude);
            gr.drawLine(x, y, x1, y1);
        }
*/
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
            setState(died);
        } else if (getMass() <= 40) {
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

        stomach.action(cell);
        switch (getState()) {
            case analise:
                keyBestFood = null;
//                Pig lead = getLeader();
                final Cell cellLeader = cell;
//                if (lead != null) {
//                    cellLeader = lead.getCell();
//                }
                // последовательность перебора ячеек
                final NearPoint[] sequence = CellHelper.getNearPoints(20, false);
                Cell cellBest = cell;
                if (checkOnAppropriate(cell, true)) {
                    foodIndex = stomach.bestFoodIndex(cell, 0.0);
                } else {
                    foodIndex = new FoodIndex();
                }

                // находим самое вкусное место
                for (NearPoint seq : sequence) {
                    final Cell cellInd = cellLeader.getCell(seq);
                    final FoodIndex foodIndexT = stomach.bestFoodIndex(cellInd, cell.distance(cellInd));

                    if (foodIndex.getBestIndex() < foodIndexT.getBestIndex() && checkOnAppropriate(cellInd, false)) {
                        foodIndex = foodIndexT;
                        cellBest = cellInd;
                    }
                }

                if (foodIndex.getBestKey() != null) {
                    if (foodIndex.getValue() < minGrassForEat
                        /*&& !MEAT_PLANT_KEY.equals(foodIndex.getBestKey())*/) {
                        setState(analise);
                        notEatCount++;
                        if (cellBest != cell && notEatCount > 9) {
                            cellBest.element(PIG_TARGET_KEY).incValue(getMass() / 30);
                            getLegs().moveTo(cellBest, walkEnergy, Legs.MoveType.energy);
                        }
                    } else {
                        keyBestFood = foodIndex.getBestKey();
                        setState(eat);
                        notEatCount = 0;
                        if (cellBest != cell) {
                            cellBest.element(PIG_TARGET_KEY).incValue(getMass() / 30);
                            getLegs().moveTo(cellBest, runEnergy, Legs.MoveType.energy);
                        }
                    }
                } else {
                    setState(stress);
                    stresses++;
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
                //if (moveToFecal(cell)) {
                if (stomach.defecation(cell)) {
                    if (keyBestFood != null) {
                        setState(eat);
                    } else {
                        setState(analise);
                    }
                }
                //}
                break;
            case eat:
                if (keyBestFood == null || cell.element(keyBestFood).value() > minGrassLeave) {
                    stomach.dine(cell, keyBestFood);
                } else {
                    setState(analise);
                }
                break;
            case died:
                cell.element(MEAT_PLANT_KEY).incValue(getMass());
                cell.element(EXCREMENT_KEY).incValue(stomach.getMass());
                cell.element(PIG_TARGET_KEY).setValue(0);
                return false;
            default:
                break;
        }
        return true;
    }

    /**
     * Бежать до ближайшего туалета.
     * @param cell текущее местоположение
     * @return if true then туалет найден
     */
    private boolean moveToFecal(final Cell cell) {
        if (cell.element(EXCREMENT_KEY).value() > 1.0) {
            return true;
        }
        final NearPoint[] sequence = CellHelper.getNearPoints(5, false);
        for (NearPoint pnt : sequence) {
            final Cell moveCell = cell.getCell(pnt);
            if (moveCell.element(EXCREMENT_KEY).value() > 1.0) {
                getLegs().moveTo(moveCell, runEnergy, Legs.MoveType.energy);
                return false;
            }
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
            /*cell.element(MEAT_PLANT_KEY).value() > 1.0 ||*/ (
                cell.element(PIG_KEY).value() - (isSame ? 1 : 0) < 0.9
                && cell.element(PIG_TARGET_KEY).value() < 0.9
                //&& cell.element(EXCREMENT_KEY).value() < 0.9
            )
        );
    }

    /**
     * Рожаем новую свинью с наследованием от этой.
     */
    public void toBirth() {
        final Pig newPig = new Pig(getLatitude(), getLongitude(), (PigManager) getManager());

        // передача массы и энергии
        final double mass = getMass();
        if (mass - birthBabyMass < 50) {
            birthBabyMass = mass - 50;
        }
        newPig.setMass(birthBabyMass);
        newPig.setEnergy(birthBabyMass);
        changeMass(-birthBabyMass);
        changeEnergy(-birthBabyMass);

        // передача генотипа
        newPig.setStomach(new Stomach(stomach, newPig));
        newPig.setMinGrassForEat(inheritGene(minGrassForEat));
        newPig.setMinGrassLeave(inheritGene(minGrassLeave));
        newPig.setRunEnergy(inheritGene(runEnergy));
        newPig.setWalkEnergy(inheritGene(walkEnergy));
        newPig.setBirthBabyMass(inheritGene(birthBabyMass));
        newPig.setBirthMass(inheritGene(birthMass));
        newPig.setMaximalAge(inheritGene(maximalAge));

        getManager().addNewAnimal(newPig, false);
    }

    @Override
    public boolean callBackAction(final CallActionType actionType) {
        switch (actionType) {
            case onMoveTo:
                if (getLegs().getAfterMoveState() != fecal) {
                    stomach.action(getCell());
                    //stomach.dine(getCell());
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
        sb.append("\"стрессов\":").append(stresses).append(",");
        sb.append("\"обедов\":").append(dines).append(",");
        sb.append("\"родов\":").append(births);
        sb.append("}");
        return sb;
    }

}
