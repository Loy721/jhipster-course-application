package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MyUserMyTestTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MyUserMyTest getMyUserMyTestSample1() {
        return new MyUserMyTest().id(1L).grade("grade1");
    }

    public static MyUserMyTest getMyUserMyTestSample2() {
        return new MyUserMyTest().id(2L).grade("grade2");
    }

    public static MyUserMyTest getMyUserMyTestRandomSampleGenerator() {
        return new MyUserMyTest().id(longCount.incrementAndGet()).grade(UUID.randomUUID().toString());
    }
}
