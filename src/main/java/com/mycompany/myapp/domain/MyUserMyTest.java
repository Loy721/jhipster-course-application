package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MyUserMyTest.
 */
@Entity
@Table(name = "my_user_my_test")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MyUserMyTest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "grade")
    private Integer grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "myTests" }, allowSetters = true)
    private MyUser myUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "questions", "myUsers", "file" }, allowSetters = true)
    private MyTest myTest;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MyUserMyTest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGrade() {
        return this.grade;
    }

    public MyUserMyTest grade(Integer grade) {
        this.setGrade(grade);
        return this;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public MyUser getMyUser() {
        return this.myUser;
    }

    public void setMyUser(MyUser myUser) {
        this.myUser = myUser;
    }

    public MyUserMyTest myUser(MyUser myUser) {
        this.setMyUser(myUser);
        return this;
    }

    public MyTest getMyTest() {
        return this.myTest;
    }

    public void setMyTest(MyTest myTest) {
        this.myTest = myTest;
    }

    public MyUserMyTest myTest(MyTest myTest) {
        this.setMyTest(myTest);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MyUserMyTest)) {
            return false;
        }
        return getId() != null && getId().equals(((MyUserMyTest) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MyUserMyTest{" +
            "id=" + getId() +
            ", grade=" + getGrade() +
            "}";
    }
}
