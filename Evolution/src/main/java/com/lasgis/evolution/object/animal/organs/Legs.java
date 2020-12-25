/**
 * @(#)Legs.java 1.0
 * <p>
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.organs;

import com.lasgis.evolution.map.Cell;
import com.lasgis.evolution.map.element.Parameters;
import com.lasgis.evolution.object.Info;
import com.lasgis.evolution.object.InfoType;
import com.lasgis.evolution.object.animal.AbstractAnimal;
import com.lasgis.evolution.object.animal.AnimalState;
import lombok.Getter;

/**
 * Ноги животного. Этот класс отвечает за перемещение.
 *
 * @author Laskin
 * @version 1.0
 * @since 25.02.13 1:54
 */
public class Legs {

    private final AbstractAnimal owner;

    private double dx;

    private double dy;

    private double dEnergy = 0.0;

    @Info(type = InfoType.SAVE)
    @Getter
    private AnimalState afterMoveState;

    private double radius;

    @Info(type = InfoType.SAVE)
    @Getter
    private double angle;

    /**
     * Constructor.
     * @param owner держатель ног
     */
    public Legs(final AbstractAnimal owner) {
        this.owner = owner;
    }

    /**
     * Перемещение в заданную ячейку.
     * @param cell ячейка, куда идти
     * @param value скорость передвижения или энергия
     * @param type kind of value - velocity or energy.
     */
    public void moveTo(final Cell cell, final double value, final MoveType type) {
        moveTo(
            (cell.getNorth() + cell.getSouth()) / 2,
            (cell.getWest() + cell.getEast()) / 2,
            value, type
        );
    }

    /**
     * Перемещение в заданную точку.
     * @param x координата x (широта)
     * @param y координата y (долгота)
     * @param value скорость передвижения или энергия
     * @param type kind of value - velocity or energy.
    public void moveTo(int x, int y, double value, MoveType type) {
    double newLatitude = CellHelper.getCellLatitude(x, y);
    double newLongitude = CellHelper.getCellLongitude(x, y);
    moveTo(newLatitude, newLongitude, value, type);
    }
     */

    /**
     * Перемещение в заданную точку.
     * @param newLatitude координата x (широта)
     * @param newLongitude координата y (долгота)
     * @param value скорость передвижения или энергия
     * @param type kind of value - velocity or energy.
     * @return true если движение было
     */
    public boolean moveTo(
        final double newLatitude, final double newLongitude,
        final double value, final MoveType type
    ) {
        // вычисление направления
        double delLat = newLatitude - owner.getLatitude();
        double delLon = newLongitude - owner.getLongitude();

        if (delLat > Parameters.MAX_LATITUDE / 2) {
            delLat -= Parameters.MAX_LATITUDE;
        } else if (delLat < -Parameters.MAX_LATITUDE / 2) {
            delLat += Parameters.MAX_LATITUDE;
        }
        if (delLon > Parameters.MAX_LONGITUDE / 2) {
            delLon -= Parameters.MAX_LONGITUDE;
        } else if (delLon < -Parameters.MAX_LONGITUDE / 2) {
            delLon += Parameters.MAX_LONGITUDE;
        }

        angle = Math.atan2(delLat, delLon);
        radius = Math.sqrt(delLat * delLat + delLon * delLon);
        double velocity;
        final double groundFactor = 5000000.0;
        if (type == MoveType.velocity) {
            velocity = value;
            dEnergy = (owner.getFullMass() * velocity * velocity) / groundFactor;
        } else { //if (type == MoveType.energy) {
            dEnergy = value;
            velocity = Math.sqrt((dEnergy * groundFactor) / owner.getFullMass());
        }
        dx = velocity * Math.sin(angle);
        dy = velocity * Math.cos(angle);
        int count = (int) (radius / velocity);
        if (count > 0) {
            afterMoveState = owner.getState();
            owner.setState(AnimalState.move);
            return true;
        }
        return false;
    }

    /**
     * Элементарное перемещение
     */
    public void move() {
        if (radius > 0.0) {
            final Cell cellBefore = owner.getCell();
            final double trouble = cellBefore.moveTrouble();
            final double dxt = dx * trouble;
            final double dyt = dy * trouble;
            owner.changeLatitude(dxt);
            owner.changeLongitude(dyt);
            final Cell cellAfter = owner.getCell();
            if (!cellAfter.equals(cellBefore)) {
                cellBefore.element(owner.getManager().getName()).decValue();
                cellAfter.element(owner.getManager().getName()).incValue();
                cellBefore.removeAnimal(owner);
                cellAfter.addAnimal(owner);
            }
            owner.changeEnergy(-dEnergy);
            radius -= Math.sqrt(dxt * dxt + dyt * dyt);
        } else {
            owner.setState(afterMoveState == AnimalState.move ? AnimalState.analise : afterMoveState);
        }
    }

    /**
     * тип передвижения.
     */
    public enum MoveType {
        /** с постоянной скоростью. */
        velocity,
        /** с постоянной потерей энергии. */
        energy
    }
}
