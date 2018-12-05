/**
 * @(#)Rabbit.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal;

import com.lasgis.evolution.map.Cell;
import com.lasgis.evolution.object.EvolutionConstants;
import com.lasgis.evolution.object.Info;
import com.lasgis.evolution.object.InfoType;
import com.lasgis.evolution.object.animal.organs.CellNearStack;
import com.lasgis.evolution.object.animal.organs.Legs;
import com.lasgis.evolution.object.animal.organs.Stomach;
import com.lasgis.evolution.panels.Scalable;
import com.lasgis.util.LGFormatter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

import static com.lasgis.evolution.object.animal.AnimalState.analise;
import static com.lasgis.evolution.object.animal.AnimalState.birth;
import static com.lasgis.evolution.object.animal.AnimalState.died;
import static com.lasgis.evolution.object.animal.AnimalState.eat;
import static com.lasgis.evolution.object.animal.AnimalState.fecal;
import static com.lasgis.evolution.object.animal.AnimalState.run;
import static com.lasgis.evolution.object.animal.AnimalState.sleep;
import static com.lasgis.evolution.object.animal.AnimalState.stress;

/**
 * Кролик ест траву и передвигается.
 * @author vladimir.laskin
 * @version 1.0
 */
@Slf4j
@Info(name = EvolutionConstants.RABBIT_KEY, type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
public class Rabbit extends AbstractAnimal implements EvolutionConstants {

    private static final Color STRESS_COLOR = new Color(187, 110, 200);
    private static final Color YOUNG_COLOR = new Color(0, 100, 200);
    private static final Color OLD_COLOR = new Color(220, 220, 220);

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
    private int sleepTime = 0;

    // параметры, определяемые геномом
    /** минимальное количество травы для поедания. */
    @Info(name = "минимум пищи", head = "ГЕНОТИП", type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private double minGrassForEat = 20;
    /** скорость бега. */
    @Info(name = "скорость бегом", type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private double runVelocity = 100;
    //private double runEnergy = 0.05;
    /** скорость передвижения в момент задумчивости. */
    @Info(name = "скорость пешком", type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private double walkVelocity = 30;
    //private double walkEnergy = 0.015;
    /** Необходимая масса для начала родов. */
    @Info(name = "масса родителя", type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private double birthMass = 300;
    /** Начальная масса нового кролика. */
    @Info(name = "масса детеныша", type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private double birthBabyMass = 50;
    /** Время жизни кролика. */
    @Info(name = "время жизни", rate = .01, type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private double maximalAge = 10000.0;

    /** Карта памяти кролика. Здесь находятся ячейки, отмеченные кроликом как перспективные для посещения. */
    @Info(type = InfoType.SAVE)
    CellNearStack map = new CellNearStack();

    /**
     * В момент создания присваиваем уникальное имя.
     * @param latitude широта точки
     * @param longitude долгота точки
     * @param manager манагер всех кроликов для
     * самоубийства и рождения нового.
     */
    protected Rabbit(final double latitude, final double longitude, final RabbitManager manager) {
        super(latitude, longitude, manager);
        setMass(100);
        setEnergy(100);
        stomach = new Stomach(0.5, this);
        stomach.setDigestion(GRASS_PLANT_KEY, 1.0);
        stomach.digestionNormalizing();
    }

    @Override
    public double getRelativeAge() {
        return age / maximalAge;
    }

    public void setMinGrassForEat(final double minGrassForEat) {
        this.minGrassForEat = minGrassForEat;
    }

    public void setRunVelocity(final double runVelocity) {
        this.runVelocity = runVelocity;
    }

    public void setWalkVelocity(final double walkVelocity) {
        this.walkVelocity = walkVelocity;
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
    public void draw(final Graphics gr, final Scalable interval) {
        final int x = interval.toScreenX(getLatitude(), getLongitude());
        final int y = interval.toScreenY(getLatitude(), getLongitude());
        final int cellSize = interval.getCellSize();
        final double massSize = cellSize * getFullMass() / 400;
        final double energySize = cellSize * getEnergy() / 300;
        if (getState() == stress) {
            gr.setColor(STRESS_COLOR);
        } else {
            gr.setColor(calcColor(YOUNG_COLOR, OLD_COLOR, age / maximalAge));
        }
        gr.fillOval((int) (x - massSize), (int) (y - energySize), (int) (massSize * 2), (int) (energySize * 2));
        if (isSelected()) {
            gr.setColor(SELECTED_COLOR);
            gr.drawOval((int) (x - massSize), (int) (y - energySize), (int) (massSize * 2), (int) (energySize * 2));
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

        if (age++ > maximalAge) {
            setState(died);
        } else if (getEnergy() < 40) {
            setState(sleep);
            sleeps++;
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
        double grass;

        stomach.action(cell);
        switch (getState()) {
            case analise:
                // последовательность перебора ячеек
                final int[][] sequence = {{1, 1}, {1, 0}, {1, -1}, {0, 1}, {0, -1}, {-1, 1}, {-1, 0}, {-1, -1}};
                grass = 0;
                Cell cellBest = null;

                for (int[] seq : sequence) {
                    final Cell cellInd = cell.getCell(seq[0], seq[1]);
                    final double grassT = cellInd.element(GRASS_PLANT_KEY).value();
                    final double rabbits = cellInd.element(getManager().getName()).value();
                    if (grass < grassT && rabbits < 0.9) {
                        grass = grassT;
                        cellBest = cellInd;
                    }
                    if (grassT >= minGrassForEat && rabbits < 0.9) {
                        map.add(cellInd, cell);
                    } else {
                        map.del(cellInd);
                    }
                }
                if (map.size() > 0) {
                    setState(run);
                    break;
                } else if (cellBest != null) {
                    if (grass < minGrassForEat) {
                        if (map.size() > 0) {
                            setState(run);
                        } else {
                            setState(analise);
                            getLegs().moveTo(cellBest, walkVelocity, Legs.MoveType.velocity);
                        }
                    } else {
                        setState(eat);
                        getLegs().moveTo(cellBest, runVelocity, Legs.MoveType.velocity);
                        map.del(cellBest);
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
                    toBirth();
                    sleepTime = 2000;
                }
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
                if (sleepTime > 0) {
                    sleepTime -= 100;
                } else if (getEnergy() >= getMass() && stomach.isEmpty()) {
                    setState(analise);
                }
                break;
            case fecal:
                if (stomach.defecation(cell)) {
                    setState(analise);
                }
                break;
            case run:
                final Cell cellGrass = map.getNearby(cell);
                if (cellGrass != null) {
                    final double grassT = cellGrass.element(GRASS_PLANT_KEY).value();
                    final double rabbits = cellGrass.element(getManager().getName()).value();
                    if (grassT >= minGrassForEat && rabbits < 0.9) {
                        setState(eat);
                        getLegs().moveTo(cellGrass, runVelocity, Legs.MoveType.velocity);
                    } else {
                        setState(analise);
                    }
                } else {
                    setState(analise);
                }
                break;
            case eat:
                stomach.dine(cell, GRASS_PLANT_KEY);
                break;
            case died:
                cell.element(MEAT_PLANT_KEY).incValue(getMass());
                cell.element(EXCREMENT_KEY).incValue(stomach.getMass());
                cell.element(RABBIT_TARGET_KEY).setValue(0);
                return false;
            default:
                break;
        }
        return true;
    }

    /**
     * Рожаем нового кролика с наследованием от этого.
     */
    public void toBirth() {
        final Rabbit newRabbit = new Rabbit(getLatitude(), getLongitude(), (RabbitManager) getManager());

        // передача массы и энергии
        final double mass = getMass();
        if (mass - birthBabyMass < 50) {
            birthBabyMass = mass - 50;
        }
        newRabbit.setMass((int) birthBabyMass);
        changeMass(-birthBabyMass);
        changeEnergy(-50);

        // передача генотипа
        newRabbit.setStomach(new Stomach(stomach, newRabbit));
        newRabbit.setMinGrassForEat(inheritGene(minGrassForEat));
        newRabbit.setRunVelocity(inheritGene(runVelocity));
        newRabbit.setWalkVelocity(inheritGene(walkVelocity));
        newRabbit.setBirthBabyMass(inheritGene(birthBabyMass));
        newRabbit.setBirthMass(inheritGene(birthMass));
        newRabbit.setMaximalAge(inheritGene(maximalAge));

        getManager().addNewAnimal(newRabbit, false);
        births++;
    }

    @Override
    public boolean callBackAction(final CallActionType actionType) {
        switch (actionType) {
            case onMoveTo:
                stomach.action(getCell());
                age++;
                if (getEnergy() < 40) {
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
        sb.append("\"скорость бегом\":").append(LGFormatter.formatLog(runVelocity)).append(",");
        sb.append("\"скорость пешком\":").append(LGFormatter.formatLog(walkVelocity)).append(",");
        sb.append("\"масса родителя для начала родов\":")
            .append(LGFormatter.formatLog(birthMass)).append(",");
        sb.append("\"масса новорожденного\":")
            .append(LGFormatter.formatLog(birthBabyMass)).append(",");
        sb.append("\"время жизни\":").append(LGFormatter.formatLog(maximalAge / 100)).append(",");
        sb.append("\"память\":").append(map.size()).append(",");
        sb.append("\"возраст\":").append(age / 100.).append(",");
        sb.append("\"снов\":").append(sleeps).append(",");
        sb.append("\"стрессов\":").append(stresses).append(",");
        sb.append("\"обедов\":").append(dines).append(",");
        sb.append("\"родов\":").append(births);
        sb.append("}");
        return sb;
    }

    @Override
    public void incDines() {
        dines++;
    }

}
