package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MyUserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MyUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MyUser.class);
        MyUser myUser1 = getMyUserSample1();
        MyUser myUser2 = new MyUser();
        assertThat(myUser1).isNotEqualTo(myUser2);

        myUser2.setId(myUser1.getId());
        assertThat(myUser1).isEqualTo(myUser2);

        myUser2 = getMyUserSample2();
        assertThat(myUser1).isNotEqualTo(myUser2);
    }
}
