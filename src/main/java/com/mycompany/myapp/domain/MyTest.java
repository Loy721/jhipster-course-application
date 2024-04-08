package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MyTest.
 */
@Entity
@Table(name = "my_test")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MyTest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "myTest")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "myTest" }, allowSetters = true)
    private Set<Question> questions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "myTest")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "myUser", "myTest" }, allowSetters = true)
    private Set<MyUserMyTest> myUsers = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "myTests" }, allowSetters = true)
    private File file;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MyTest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public MyTest name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Question> getQuestions() {
        return this.questions;
    }

    public void setQuestions(Set<Question> questions) {
        if (this.questions != null) {
            this.questions.forEach(i -> i.setMyTest(null));
        }
        if (questions != null) {
            questions.forEach(i -> i.setMyTest(this));
        }
        this.questions = questions;
    }

    public MyTest questions(Set<Question> questions) {
        this.setQuestions(questions);
        return this;
    }

    public MyTest addQuestions(Question question) {
        this.questions.add(question);
        question.setMyTest(this);
        return this;
    }

    public MyTest removeQuestions(Question question) {
        this.questions.remove(question);
        question.setMyTest(null);
        return this;
    }

    public Set<MyUserMyTest> getMyUsers() {
        return this.myUsers;
    }

    public void setMyUsers(Set<MyUserMyTest> myUserMyTests) {
        if (this.myUsers != null) {
            this.myUsers.forEach(i -> i.setMyTest(null));
        }
        if (myUserMyTests != null) {
            myUserMyTests.forEach(i -> i.setMyTest(this));
        }
        this.myUsers = myUserMyTests;
    }

    public MyTest myUsers(Set<MyUserMyTest> myUserMyTests) {
        this.setMyUsers(myUserMyTests);
        return this;
    }

    public MyTest addMyUsers(MyUserMyTest myUserMyTest) {
        this.myUsers.add(myUserMyTest);
        myUserMyTest.setMyTest(this);
        return this;
    }

    public MyTest removeMyUsers(MyUserMyTest myUserMyTest) {
        this.myUsers.remove(myUserMyTest);
        myUserMyTest.setMyTest(null);
        return this;
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public MyTest file(File file) {
        this.setFile(file);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MyTest)) {
            return false;
        }
        return getId() != null && getId().equals(((MyTest) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MyTest{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
