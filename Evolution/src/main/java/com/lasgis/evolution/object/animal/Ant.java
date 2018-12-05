/**
 * @(#)Ant.java 1.0
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
import com.lasgis.evolution.object.animal.brain.Routine;
import com.lasgis.evolution.object.animal.brain.RoutineFactory;
import com.lasgis.evolution.object.animal.brain.compile.RoutineCompilerException;
import com.lasgis.evolution.object.animal.organs.Stomach;
import com.lasgis.evolution.panels.Scalable;
import lombok.extern.slf4j.Slf4j;

import javax.script.SimpleBindings;
import java.awt.*;
import java.util.Map;

import static com.lasgis.evolution.object.animal.AnimalState.analise;
import static com.lasgis.evolution.object.animal.AnimalState.died;
import static com.lasgis.evolution.object.animal.AnimalState.fecal;
import static com.lasgis.evolution.object.animal.AnimalState.sleep;

/**
 * The Class Ant.
 * @author Vladimir Laskin
 * @version 1.0
 */
@Slf4j
@Info(name = EvolutionConstants.ANT_KEY, type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
public class Ant extends AbstractAnimal implements CallBack, EvolutionConstants {

    private static final Color YOUNG_COLOR = new Color(50, 100, 150);
    private static final Color OLD_COLOR = new Color(255, 255, 255);

    @Info(name = "возраст", rate = 0.01, type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private int age = 0;
    @Info(name = "обедов", type = { InfoType.STAT, InfoType.SAVE })
    private int dines = 0;
    @Info(name = "рождений", type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private int births = 0;

    // параметры, определяемые геномом
    /** Энергия бега за едой. */
//    @Info(name = "энергия бега", rate = 1000, type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
//    private double runEnergy = 0.21;
    /** Энергия передвижения в момент задумчивости. */
//    @Info(name = "энергия пешком", rate = 1000, type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
//    private double walkEnergy = 0.07;
    /** Необходимая масса для начала родов. */
    @Info(name = "масса родителя", type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private double birthMass = 100;
    /** Начальная масса новой свиньи. */
    @Info(name = "масса детеныша", type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private double birthBabyMass = 30;
    /** Время жизни. */
    @Info(name = "время жизни", rate = .01, type = { InfoType.INFO, InfoType.STAT, InfoType.SAVE })
    private double maximalAge = 10000.0;
    Routine routine = null;

    /**
     * Конструктор.
     * @param latitude координата X
     * @param longitude координата Y
     * @param manager an Animal Manager
     */
    public Ant(final double latitude, final double longitude, final AbstractAnimalManager manager) {
        super(latitude, longitude, manager);
        setMass(30);
        setEnergy(30);
        stomach = new Stomach(0.5, this);
        stomach.setDigestion(CHAMOMILE_LEAF_KEY, 0.5);
//        stomach.setDigestion(CHAMOMILE_FLOWER_KEY, 0.3);
        stomach.setDigestion(NECTAR_KEY, 0.2);
//        stomach.setDigestion(HONEY_KEY, 0.1);
        stomach.digestionNormalizing();

        try {
            final Map<String, Routine> routines = RoutineFactory.createActualRoutines(
                this, new SimpleBindings(), "MainRout.rout", true
            );
            routine = routines.get("DineRout");
        } catch (final RoutineCompilerException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    @Override
    protected boolean action() {
        final Cell cell = getCell();
        if (fundamentalInstinct(cell)) {
            return getState() != died;
        }
        if (routine != null) {
            long nanoTime = System.nanoTime();
            final long runTime = nanoTime + 1000000;
            while (runTime > nanoTime && nanoTime > getSkip2NanoTime()) {
                routine.debugAct();
                //System.out.print("d");
                nanoTime = System.nanoTime();
            }
            //System.out.print(":");
        }
        return true;
    }

    /**
     * Проверка состояния организма
     * и определение его глубинных инстинктов.
     * @return true если организм занят или умер
     */
    private boolean fundamentalInstinct(final Cell cell) {

        if (age++ > maximalAge) {
            setState(died);
        } else if (getEnergy() <= 5) {
            setState(died);
        } else if (getMass() <= 5) {
            setState(died);
        } else if (stomach.isNeedToDigestion()) {
            setState(sleep);
        } else if (stomach.isNeedToDefecation()) {
            setState(fecal);
        }
        stomach.action(cell);
        switch (getState()) {
            case sleep:
                if (getEnergy() < getMass()) {
                    changeEnergy(1);
                    changeMass(-0.2);
                    return true;
                } else if (!stomach.isEmpty()) {
                    return true;
                }
                break;
            case fecal:
                if (!stomach.defecation(cell)) {
                    return true;
                }
                break;
            case died:
                cell.element(MEAT_PLANT_KEY).incValue(getMass());
                cell.element(EXCREMENT_KEY).incValue(stomach.getMass());
                cell.element(ANT_TARGET_KEY).setValue(0);
                return true;
            default:
                break;
        }
        setState(analise);
        return false;
    }

    /**
     * Check if it cell is Appropriate for move.
     * @param cell checked cell
     * @param isSame свинья находится на этом месте
     * @return true if checked cell is Appropriate for move.
     */
    private boolean checkOnAppropriate(final Cell cell, final boolean isSame) {
        return cell.element(ANT_KEY).value() - (isSame ? 1 : 0) < 0.9
            && cell.element(ANT_TARGET_KEY).value() < 0.9;
    }

    @Override
    public void draw(final Graphics gr, final Scalable interval) {
        final int x = interval.toScreenX(getLatitude(), getLongitude());
        final int y = interval.toScreenY(getLatitude(), getLongitude());
        final int cellSize = interval.getCellSize();
        final double massSize = cellSize * getFullMass() / 400;
        final double energySize = cellSize * getEnergy() / 400;
        gr.setColor(calcColor(YOUNG_COLOR, OLD_COLOR, age / maximalAge));
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
    public void toBirth() {
        final Ant newAnt = new Ant(getLatitude(), getLongitude(), getManager());

        // передача массы и энергии
        newAnt.setMass(birthBabyMass);
        changeMass(-birthBabyMass);
        changeEnergy(-25);

        // передача генотипа
        newAnt.setStomach(new Stomach(stomach, newAnt));
        getManager().addNewAnimal(newAnt, false);
        births++;
    }

    @Override
    public double getFullMass() {
        return getMass();
    }

    @Override
    public void incDines() {
        dines++;
    }

    @Override
    public double getRelativeAge() {
        return age / maximalAge;
    }

    @Override
    public boolean callBackAction(final CallActionType actionType) {
        switch (actionType) {
            case onMoveTo:
                if (getLegs().getAfterMoveState() != fecal) {
                    stomach.action(getCell());
                }
                //stomach.dine(getCell());
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

    public double getBirthMass() {
        return birthMass;
    }

    public void setBirthMass(final double birthMass) {
        this.birthMass = birthMass;
    }

    /**
     * учётчик рождений.
     */
    public void addBirth() {
        births++;
    }
}
