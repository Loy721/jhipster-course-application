package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MyTestTestSamples.*;
import static com.mycompany.myapp.domain.MyUserMyTestTestSamples.*;
import static com.mycompany.myapp.domain.MyUserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MyUserMyTestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MyUserMyTest.class);
        MyUserMyTest myUserMyTest1 = getMyUserMyTestSample1();
        MyUserMyTest myUserMyTest2 = new MyUserMyTest();
        assertThat(myUserMyTest1).isNotEqualTo(myUserMyTest2);

        myUserMyTest2.setId(myUserMyTest1.getId());
        assertThat(myUserMyTest1).isEqualTo(myUserMyTest2);

        myUserMyTest2 = getMyUserMyTestSample2();
        assertThat(myUserMyTest1).isNotEqualTo(myUserMyTest2);
    }

    @Test
    void myUserTest() throws Exception {
        MyUserMyTest myUserMyTest = getMyUserMyTestRandomSampleGenerator();
        MyUser myUserBack = getMyUserRandomSampleGenerator();

        myUserMyTest.setMyUser(myUserBack);
        assertThat(myUserMyTest.getMyUser()).isEqualTo(myUserBack);

        myUserMyTest.myUser(null);
        assertThat(myUserMyTest.getMyUser()).isNull();
    }

    @Test
    void myTestTest() throws Exception {
        MyUserMyTest myUserMyTest = getMyUserMyTestRandomSampleGenerator();
        MyTest myTestBack = getMyTestRandomSampleGenerator();

        myUserMyTest.setMyTest(myTestBack);
        assertThat(myUserMyTest.getMyTest()).isEqualTo(myTestBack);

        myUserMyTest.myTest(null);
        assertThat(myUserMyTest.getMyTest()).isNull();
    }
}
