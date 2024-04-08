package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class MyTestAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMyTestAllPropertiesEquals(MyTest expected, MyTest actual) {
        assertMyTestAutoGeneratedPropertiesEquals(expected, actual);
        assertMyTestAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMyTestAllUpdatablePropertiesEquals(MyTest expected, MyTest actual) {
        assertMyTestUpdatableFieldsEquals(expected, actual);
        assertMyTestUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMyTestAutoGeneratedPropertiesEquals(MyTest expected, MyTest actual) {
        assertThat(expected)
            .as("Verify MyTest auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMyTestUpdatableFieldsEquals(MyTest expected, MyTest actual) {
        assertThat(expected)
            .as("Verify MyTest relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMyTestUpdatableRelationshipsEquals(MyTest expected, MyTest actual) {
        assertThat(expected)
            .as("Verify MyTest relationships")
            .satisfies(e -> assertThat(e.getFile()).as("check file").isEqualTo(actual.getFile()));
    }
}
