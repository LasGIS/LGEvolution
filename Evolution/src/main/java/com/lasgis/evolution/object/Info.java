/**
 * @(#)Info.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2014 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Эта аннотация предполагает, что эти поля показываются в информационном поле.
 * @author Vladimir Laskin
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.TYPE })
public @interface Info {

    /**
     *
     * @return
     */
    InfoType[] type() default { InfoType.NONE };

    /**
     * Название свойства для вывода.
     * @return
     */
    String name() default "";

    /**
     *
     * @return
     */
    double rate() default 1;

    /**
     * Заголовок
     * @return
     */
    String head() default "";
}
