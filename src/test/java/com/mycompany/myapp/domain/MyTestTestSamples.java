package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MyTestTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MyTest getMyTestSample1() {
        return new MyTest().id(1L).name("name1").questions("questions1");
    }

    public static MyTest getMyTestSample2() {
        return new MyTest().id(2L).name("name2").questions("questions2");
    }

    public static MyTest getMyTestRandomSampleGenerator() {
        return new MyTest().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).questions(UUID.randomUUID().toString());
    }
}
