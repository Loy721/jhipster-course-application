package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MyUser.
 */
@Entity
@Table(name = "my_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MyUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "myUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "myUser", "myTest" }, allowSetters = true)
    private Set<MyUserMyTest> myTests = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MyUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public MyUser name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public MyUser surname(String surname) {
        this.setSurname(surname);
        return this;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Set<MyUserMyTest> getMyTests() {
        return this.myTests;
    }

    public void setMyTests(Set<MyUserMyTest> myUserMyTests) {
        if (this.myTests != null) {
            this.myTests.forEach(i -> i.setMyUser(null));
        }
        if (myUserMyTests != null) {
            myUserMyTests.forEach(i -> i.setMyUser(this));
        }
        this.myTests = myUserMyTests;
    }

    public MyUser myTests(Set<MyUserMyTest> myUserMyTests) {
        this.setMyTests(myUserMyTests);
        return this;
    }

    public MyUser addMyTests(MyUserMyTest myUserMyTest) {
        this.myTests.add(myUserMyTest);
        myUserMyTest.setMyUser(this);
        return this;
    }

    public MyUser removeMyTests(MyUserMyTest myUserMyTest) {
        this.myTests.remove(myUserMyTest);
        myUserMyTest.setMyUser(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MyUser)) {
            return false;
        }
        return getId() != null && getId().equals(((MyUser) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MyUser{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", surname='" + getSurname() + "'" +
            "}";
    }
}
