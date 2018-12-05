/**
 * @(#)ScalableMock.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.panels;

import com.lasgis.evolution.map.element.Parameters;

/**
 * @author Laskin
 * @version 1.0
 * @since 23.01.2011 15:47:59
 */
public class ScalableMock implements Scalable {
    private double north = Parameters.MAX_LATITUDE;
    private double south = Parameters.MIN_LATITUDE;
    private double west = Parameters.MIN_LONGITUDE;
    private double east = Parameters.MAX_LONGITUDE;

    public ScalableMock(double north, double south, double west, double east) {
        this.north = north;
        this.south = south;
        this.west = west;
        this.east = east;
    }

    public int toScreenX(double latit, double longit) {
        return 0;
    }

    public int toScreenY(double latit, double longit) {
        return 0;
    }

    public double toMapLatitude(int x, int y) {
        return 0;
    }

    public double toMapLongitude(int x, int y) {
        return 0;
    }

    public int getLevel() {
        return 5;
    }

    @Override
    public int getCellSize() {
        return 10;
    }

    @Override
    public boolean isInto(double latitude, double longitude) {
        return true;
    }

    /**
     * Вернуть северную широту интервала
     * @return северная широта
     */
    public double getNorth() {
        if (north < Parameters.MAX_LATITUDE) {
            return north;
        } else {
            return Parameters.MAX_LATITUDE;
        }
    }

    /**
     * Вернуть южную широту интервала
     * @return южная широта
     */
    public double getSouth() {
        if (south > Parameters.MIN_LATITUDE) {
            return south;
        } else {
            return Parameters.MIN_LATITUDE;
        }
    }

    /**
     * Вернуть западную долготу интервала
     * @return западная долгота
     */
    public double getWest() {
        if (west > Parameters.MIN_LONGITUDE) {
            return west;
        } else {
            return Parameters.MIN_LONGITUDE;
        }
    }

    /**
     * Вернуть восточную долготу интервала
     * @return восточная долгота
     */
    public double getEast() {
        if (east < Parameters.MAX_LONGITUDE) {
            return east;
        } else {
            return Parameters.MAX_LONGITUDE;
        }
    }

}
