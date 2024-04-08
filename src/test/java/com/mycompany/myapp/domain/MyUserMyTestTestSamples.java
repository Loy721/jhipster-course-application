package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MyUserMyTestTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MyUserMyTest getMyUserMyTestSample1() {
        return new MyUserMyTest().id(1L).grade(1);
    }

    public static MyUserMyTest getMyUserMyTestSample2() {
        return new MyUserMyTest().id(2L).grade(2);
    }

    public static MyUserMyTest getMyUserMyTestRandomSampleGenerator() {
        return new MyUserMyTest().id(longCount.incrementAndGet()).grade(intCount.incrementAndGet());
    }
}
