/**
 * @(#)ParamWrapper.java 1.0
 *
 * Title: LG Evolution powered by Java
 * Description: Program for imitation of evolutions process.
 * Copyright (c) 2012-2015 LasGIS Company. All Rights Reserved.
 */

package com.lasgis.evolution.object.animal.brain.compile;

import com.lasgis.evolution.object.animal.brain.Param;

/**
 * The Class ParamWrapper.
 * @author Vladimir Laskin
 * @version 1.0
 */
public class ParamWrapper {

    private Param param;
    private TokenParser.Token token;

    public ParamWrapper(final Param param, final TokenParser.Token token) {
        this.param = param;
        this.token = token;
    }

    public Param getParam() {
        return param;
    }

    public TokenParser.Token getToken() {
        return token;
    }
}
