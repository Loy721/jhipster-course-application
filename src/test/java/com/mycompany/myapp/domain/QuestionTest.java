package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MyTestTestSamples.*;
import static com.mycompany.myapp.domain.QuestionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Question.class);
        Question question1 = getQuestionSample1();
        Question question2 = new Question();
        assertThat(question1).isNotEqualTo(question2);

        question2.setId(question1.getId());
        assertThat(question1).isEqualTo(question2);

        question2 = getQuestionSample2();
        assertThat(question1).isNotEqualTo(question2);
    }

    @Test
    void myTestTest() throws Exception {
        Question question = getQuestionRandomSampleGenerator();
        MyTest myTestBack = getMyTestRandomSampleGenerator();

        question.setMyTest(myTestBack);
        assertThat(question.getMyTest()).isEqualTo(myTestBack);

        question.myTest(null);
        assertThat(question.getMyTest()).isNull();
    }
}
