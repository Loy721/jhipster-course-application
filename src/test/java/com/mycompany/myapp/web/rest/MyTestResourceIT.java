package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.MyTestAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.MyTest;
import com.mycompany.myapp.repository.MyTestRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MyTestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MyTestResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_QUESTIONS = "AAAAAAAAAA";
    private static final String UPDATED_QUESTIONS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/my-tests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MyTestRepository myTestRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMyTestMockMvc;

    private MyTest myTest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MyTest createEntity(EntityManager em) {
        MyTest myTest = new MyTest().name(DEFAULT_NAME).questions(DEFAULT_QUESTIONS);
        return myTest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MyTest createUpdatedEntity(EntityManager em) {
        MyTest myTest = new MyTest().name(UPDATED_NAME).questions(UPDATED_QUESTIONS);
        return myTest;
    }

    @BeforeEach
    public void initTest() {
        myTest = createEntity(em);
    }

    @Test
    @Transactional
    void createMyTest() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MyTest
        var returnedMyTest = om.readValue(
            restMyTestMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(myTest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MyTest.class
        );

        // Validate the MyTest in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMyTestUpdatableFieldsEquals(returnedMyTest, getPersistedMyTest(returnedMyTest));
    }

    @Test
    @Transactional
    void createMyTestWithExistingId() throws Exception {
        // Create the MyTest with an existing ID
        myTest.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMyTestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(myTest)))
            .andExpect(status().isBadRequest());

        // Validate the MyTest in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMyTests() throws Exception {
        // Initialize the database
        myTestRepository.saveAndFlush(myTest);

        // Get all the myTestList
        restMyTestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(myTest.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].questions").value(hasItem(DEFAULT_QUESTIONS)));
    }

    @Test
    @Transactional
    void getMyTest() throws Exception {
        // Initialize the database
        myTestRepository.saveAndFlush(myTest);

        // Get the myTest
        restMyTestMockMvc
            .perform(get(ENTITY_API_URL_ID, myTest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(myTest.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.questions").value(DEFAULT_QUESTIONS));
    }

    @Test
    @Transactional
    void getNonExistingMyTest() throws Exception {
        // Get the myTest
        restMyTestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMyTest() throws Exception {
        // Initialize the database
        myTestRepository.saveAndFlush(myTest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the myTest
        MyTest updatedMyTest = myTestRepository.findById(myTest.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMyTest are not directly saved in db
        em.detach(updatedMyTest);
        updatedMyTest.name(UPDATED_NAME).questions(UPDATED_QUESTIONS);

        restMyTestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMyTest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMyTest))
            )
            .andExpect(status().isOk());

        // Validate the MyTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMyTestToMatchAllProperties(updatedMyTest);
    }

    @Test
    @Transactional
    void putNonExistingMyTest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        myTest.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMyTestMockMvc
            .perform(put(ENTITY_API_URL_ID, myTest.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(myTest)))
            .andExpect(status().isBadRequest());

        // Validate the MyTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMyTest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        myTest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyTestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(myTest))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMyTest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        myTest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyTestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(myTest)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MyTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMyTestWithPatch() throws Exception {
        // Initialize the database
        myTestRepository.saveAndFlush(myTest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the myTest using partial update
        MyTest partialUpdatedMyTest = new MyTest();
        partialUpdatedMyTest.setId(myTest.getId());

        partialUpdatedMyTest.name(UPDATED_NAME);

        restMyTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMyTest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMyTest))
            )
            .andExpect(status().isOk());

        // Validate the MyTest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMyTestUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMyTest, myTest), getPersistedMyTest(myTest));
    }

    @Test
    @Transactional
    void fullUpdateMyTestWithPatch() throws Exception {
        // Initialize the database
        myTestRepository.saveAndFlush(myTest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the myTest using partial update
        MyTest partialUpdatedMyTest = new MyTest();
        partialUpdatedMyTest.setId(myTest.getId());

        partialUpdatedMyTest.name(UPDATED_NAME).questions(UPDATED_QUESTIONS);

        restMyTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMyTest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMyTest))
            )
            .andExpect(status().isOk());

        // Validate the MyTest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMyTestUpdatableFieldsEquals(partialUpdatedMyTest, getPersistedMyTest(partialUpdatedMyTest));
    }

    @Test
    @Transactional
    void patchNonExistingMyTest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        myTest.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMyTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, myTest.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(myTest))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMyTest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        myTest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(myTest))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMyTest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        myTest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyTestMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(myTest)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MyTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMyTest() throws Exception {
        // Initialize the database
        myTestRepository.saveAndFlush(myTest);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the myTest
        restMyTestMockMvc
            .perform(delete(ENTITY_API_URL_ID, myTest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return myTestRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected MyTest getPersistedMyTest(MyTest myTest) {
        return myTestRepository.findById(myTest.getId()).orElseThrow();
    }

    protected void assertPersistedMyTestToMatchAllProperties(MyTest expectedMyTest) {
        assertMyTestAllPropertiesEquals(expectedMyTest, getPersistedMyTest(expectedMyTest));
    }

    protected void assertPersistedMyTestToMatchUpdatableProperties(MyTest expectedMyTest) {
        assertMyTestAllUpdatablePropertiesEquals(expectedMyTest, getPersistedMyTest(expectedMyTest));
    }
}
