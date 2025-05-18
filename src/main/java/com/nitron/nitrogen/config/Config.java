package com.nitron.nitrogen.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class Config extends MidnightConfig {
    public static final String MAIN = "main";

    @Entry(category = MAIN) public static boolean screenshake = true;
    @Entry(category = MAIN) public static float mult = 1.0F;
    @Entry(category = MAIN) public static boolean trailRenderers = true;
}
