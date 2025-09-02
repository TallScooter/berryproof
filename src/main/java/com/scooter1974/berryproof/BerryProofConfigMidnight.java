package com.scooter1974.berryproof;

import eu.midnightdust.lib.config.MidnightConfig;

public class BerryProofConfigMidnight extends MidnightConfig {
    public static final String GENERAL = "general";

    @Entry(category = GENERAL, name = "Enable sound when damage is negated")
    public static boolean enableSound = true;

    @Entry(category = GENERAL, name = "Sound volume (0.0 to 1.0)", min = 0.0, max = 1.0, isSlider = true)
    public static double soundVolume = 0.5;

    @Entry(category = GENERAL, name = "Enable verbose logging for tag matching")
    public static boolean logTagMatches = false;

    @Entry(category = GENERAL, name = "Enable protection for villagers, golems, and wandering traders")
    public static boolean enableProtectedEntities = true;
}
