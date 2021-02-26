package com.particle.util.math;

import java.util.Random;

public class GlobalRandom {

    private static Random random = new Random(37);

    public static double nextDouble() {
        return random.nextDouble();
    }

}
