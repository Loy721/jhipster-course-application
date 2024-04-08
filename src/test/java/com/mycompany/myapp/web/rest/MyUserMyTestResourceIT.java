package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.MyUserMyTestAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.MyUserMyTest;
import com.mycompany.myapp.repository.MyUserMyTestRepository;
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
 * Integration tests for the {@link MyUserMyTestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MyUserMyTestResourceIT {

    private static final Integer DEFAULT_GRADE = 1;
    private static final Integer UPDATED_GRADE = 2;

    private static final String ENTITY_API_URL = "/api/my-user-my-tests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MyUserMyTestRepository myUserMyTestRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMyUserMyTestMockMvc;

    private MyUserMyTest myUserMyTest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MyUserMyTest createEntity(EntityManager em) {
        MyUserMyTest myUserMyTest = new MyUserMyTest().grade(DEFAULT_GRADE);
        return myUserMyTest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MyUserMyTest createUpdatedEntity(EntityManager em) {
        MyUserMyTest myUserMyTest = new MyUserMyTest().grade(UPDATED_GRADE);
        return myUserMyTest;
    }

    @BeforeEach
    public void initTest() {
        myUserMyTest = createEntity(em);
    }

    @Test
    @Transactional
    void createMyUserMyTest() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MyUserMyTest
        var returnedMyUserMyTest = om.readValue(
            restMyUserMyTestMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(myUserMyTest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MyUserMyTest.class
        );

        // Validate the MyUserMyTest in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMyUserMyTestUpdatableFieldsEquals(returnedMyUserMyTest, getPersistedMyUserMyTest(returnedMyUserMyTest));
    }

    @Test
    @Transactional
    void createMyUserMyTestWithExistingId() throws Exception {
        // Create the MyUserMyTest with an existing ID
        myUserMyTest.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMyUserMyTestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(myUserMyTest)))
            .andExpect(status().isBadRequest());

        // Validate the MyUserMyTest in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMyUserMyTests() throws Exception {
        // Initialize the database
        myUserMyTestRepository.saveAndFlush(myUserMyTest);

        // Get all the myUserMyTestList
        restMyUserMyTestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(myUserMyTest.getId().intValue())))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE)));
    }

    @Test
    @Transactional
    void getMyUserMyTest() throws Exception {
        // Initialize the database
        myUserMyTestRepository.saveAndFlush(myUserMyTest);

        // Get the myUserMyTest
        restMyUserMyTestMockMvc
            .perform(get(ENTITY_API_URL_ID, myUserMyTest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(myUserMyTest.getId().intValue()))
            .andExpect(jsonPath("$.grade").value(DEFAULT_GRADE));
    }

    @Test
    @Transactional
    void getNonExistingMyUserMyTest() throws Exception {
        // Get the myUserMyTest
        restMyUserMyTestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMyUserMyTest() throws Exception {
        // Initialize the database
        myUserMyTestRepository.saveAndFlush(myUserMyTest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the myUserMyTest
        MyUserMyTest updatedMyUserMyTest = myUserMyTestRepository.findById(myUserMyTest.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMyUserMyTest are not directly saved in db
        em.detach(updatedMyUserMyTest);
        updatedMyUserMyTest.grade(UPDATED_GRADE);

        restMyUserMyTestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMyUserMyTest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMyUserMyTest))
            )
            .andExpect(status().isOk());

        // Validate the MyUserMyTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMyUserMyTestToMatchAllProperties(updatedMyUserMyTest);
    }

    @Test
    @Transactional
    void putNonExistingMyUserMyTest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        myUserMyTest.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMyUserMyTestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, myUserMyTest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(myUserMyTest))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyUserMyTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMyUserMyTest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        myUserMyTest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyUserMyTestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(myUserMyTest))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyUserMyTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMyUserMyTest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        myUserMyTest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyUserMyTestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(myUserMyTest)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MyUserMyTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMyUserMyTestWithPatch() throws Exception {
        // Initialize the database
        myUserMyTestRepository.saveAndFlush(myUserMyTest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the myUserMyTest using partial update
        MyUserMyTest partialUpdatedMyUserMyTest = new MyUserMyTest();
        partialUpdatedMyUserMyTest.setId(myUserMyTest.getId());

        restMyUserMyTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMyUserMyTest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMyUserMyTest))
            )
            .andExpect(status().isOk());

        // Validate the MyUserMyTest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMyUserMyTestUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMyUserMyTest, myUserMyTest),
            getPersistedMyUserMyTest(myUserMyTest)
        );
    }

    @Test
    @Transactional
    void fullUpdateMyUserMyTestWithPatch() throws Exception {
        // Initialize the database
        myUserMyTestRepository.saveAndFlush(myUserMyTest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the myUserMyTest using partial update
        MyUserMyTest partialUpdatedMyUserMyTest = new MyUserMyTest();
        partialUpdatedMyUserMyTest.setId(myUserMyTest.getId());

        partialUpdatedMyUserMyTest.grade(UPDATED_GRADE);

        restMyUserMyTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMyUserMyTest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMyUserMyTest))
            )
            .andExpect(status().isOk());

        // Validate the MyUserMyTest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMyUserMyTestUpdatableFieldsEquals(partialUpdatedMyUserMyTest, getPersistedMyUserMyTest(partialUpdatedMyUserMyTest));
    }

    @Test
    @Transactional
    void patchNonExistingMyUserMyTest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        myUserMyTest.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMyUserMyTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, myUserMyTest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(myUserMyTest))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyUserMyTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMyUserMyTest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        myUserMyTest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyUserMyTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(myUserMyTest))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyUserMyTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMyUserMyTest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        myUserMyTest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyUserMyTestMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(myUserMyTest)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MyUserMyTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMyUserMyTest() throws Exception {
        // Initialize the database
        myUserMyTestRepository.saveAndFlush(myUserMyTest);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the myUserMyTest
        restMyUserMyTestMockMvc
            .perform(delete(ENTITY_API_URL_ID, myUserMyTest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return myUserMyTestRepository.count();
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

    protected MyUserMyTest getPersistedMyUserMyTest(MyUserMyTest myUserMyTest) {
        return myUserMyTestRepository.findById(myUserMyTest.getId()).orElseThrow();
    }

    protected void assertPersistedMyUserMyTestToMatchAllProperties(MyUserMyTest expectedMyUserMyTest) {
        assertMyUserMyTestAllPropertiesEquals(expectedMyUserMyTest, getPersistedMyUserMyTest(expectedMyUserMyTest));
    }

    protected void assertPersistedMyUserMyTestToMatchUpdatableProperties(MyUserMyTest expectedMyUserMyTest) {
        assertMyUserMyTestAllUpdatablePropertiesEquals(expectedMyUserMyTest, getPersistedMyUserMyTest(expectedMyUserMyTest));
    }
}
