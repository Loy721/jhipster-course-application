package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A File.
 */
@Entity
@Table(name = "file")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "topic")
    private String topic;

    @Column(name = "content")
    private String content;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "file")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "questions", "myUsers", "file" }, allowSetters = true)
    private Set<MyTest> myTests = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public File id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTopic() {
        return this.topic;
    }

    public File topic(String topic) {
        this.setTopic(topic);
        return this;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getContent() {
        return this.content;
    }

    public File content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<MyTest> getMyTests() {
        return this.myTests;
    }

    public void setMyTests(Set<MyTest> myTests) {
        if (this.myTests != null) {
            this.myTests.forEach(i -> i.setFile(null));
        }
        if (myTests != null) {
            myTests.forEach(i -> i.setFile(this));
        }
        this.myTests = myTests;
    }

    public File myTests(Set<MyTest> myTests) {
        this.setMyTests(myTests);
        return this;
    }

    public File addMyTests(MyTest myTest) {
        this.myTests.add(myTest);
        myTest.setFile(this);
        return this;
    }

    public File removeMyTests(MyTest myTest) {
        this.myTests.remove(myTest);
        myTest.setFile(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof File)) {
            return false;
        }
        return getId() != null && getId().equals(((File) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "File{" +
            "id=" + getId() +
            ", topic='" + getTopic() + "'" +
            ", content='" + getContent() + "'" +
            "}";
    }
}
