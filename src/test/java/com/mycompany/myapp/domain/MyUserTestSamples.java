package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MyUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MyUser getMyUserSample1() {
        return new MyUser().id(1L).name("name1").surname("surname1");
    }

    public static MyUser getMyUserSample2() {
        return new MyUser().id(2L).name("name2").surname("surname2");
    }

    public static MyUser getMyUserRandomSampleGenerator() {
        return new MyUser().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).surname(UUID.randomUUID().toString());
    }
}
