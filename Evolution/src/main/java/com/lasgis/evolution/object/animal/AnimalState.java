/**
 * @(#)AnimalState.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal;

import com.lasgis.evolution.object.Info;
import com.lasgis.evolution.object.InfoType;

/**
 * Состояния животного.
 *
 * @author Laskin
 * @version 1.0
 * @since 08.12.12 9:21
 */
public enum AnimalState {
    /** спит. */
    sleep("спит"),
    /** анализ ситуации. */
    analise("поиск"),
    /** ест. */
    eat("ест"),
    /** бежит | перемещается. */
    run("бежит"),
    /** бежит | перемещается. */
    move("двигается"),
    /** рождение нового животного. */
    birth("роды"),
    /** какает :). */
    fecal("очищение"),
    /** животное в панике. */
    stress("паника"),
    /** умер. */
    died("умер");

    @Info(type = { InfoType.INFO, InfoType.SAVE })
    String name;

    AnimalState(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
