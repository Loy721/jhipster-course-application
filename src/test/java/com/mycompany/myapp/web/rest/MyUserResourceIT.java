package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.MyUserAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.MyUser;
import com.mycompany.myapp.repository.MyUserRepository;
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
 * Integration tests for the {@link MyUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MyUserResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/my-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MyUserRepository myUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMyUserMockMvc;

    private MyUser myUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MyUser createEntity(EntityManager em) {
        MyUser myUser = new MyUser().name(DEFAULT_NAME).surname(DEFAULT_SURNAME);
        return myUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MyUser createUpdatedEntity(EntityManager em) {
        MyUser myUser = new MyUser().name(UPDATED_NAME).surname(UPDATED_SURNAME);
        return myUser;
    }

    @BeforeEach
    public void initTest() {
        myUser = createEntity(em);
    }

    @Test
    @Transactional
    void createMyUser() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MyUser
        var returnedMyUser = om.readValue(
            restMyUserMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(myUser)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MyUser.class
        );

        // Validate the MyUser in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMyUserUpdatableFieldsEquals(returnedMyUser, getPersistedMyUser(returnedMyUser));
    }

    @Test
    @Transactional
    void createMyUserWithExistingId() throws Exception {
        // Create the MyUser with an existing ID
        myUser.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMyUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(myUser)))
            .andExpect(status().isBadRequest());

        // Validate the MyUser in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMyUsers() throws Exception {
        // Initialize the database
        myUserRepository.saveAndFlush(myUser);

        // Get all the myUserList
        restMyUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(myUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME)));
    }

    @Test
    @Transactional
    void getMyUser() throws Exception {
        // Initialize the database
        myUserRepository.saveAndFlush(myUser);

        // Get the myUser
        restMyUserMockMvc
            .perform(get(ENTITY_API_URL_ID, myUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(myUser.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.surname").value(DEFAULT_SURNAME));
    }

    @Test
    @Transactional
    void getNonExistingMyUser() throws Exception {
        // Get the myUser
        restMyUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMyUser() throws Exception {
        // Initialize the database
        myUserRepository.saveAndFlush(myUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the myUser
        MyUser updatedMyUser = myUserRepository.findById(myUser.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMyUser are not directly saved in db
        em.detach(updatedMyUser);
        updatedMyUser.name(UPDATED_NAME).surname(UPDATED_SURNAME);

        restMyUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMyUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMyUser))
            )
            .andExpect(status().isOk());

        // Validate the MyUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMyUserToMatchAllProperties(updatedMyUser);
    }

    @Test
    @Transactional
    void putNonExistingMyUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        myUser.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMyUserMockMvc
            .perform(put(ENTITY_API_URL_ID, myUser.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(myUser)))
            .andExpect(status().isBadRequest());

        // Validate the MyUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMyUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        myUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(myUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMyUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        myUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(myUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MyUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMyUserWithPatch() throws Exception {
        // Initialize the database
        myUserRepository.saveAndFlush(myUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the myUser using partial update
        MyUser partialUpdatedMyUser = new MyUser();
        partialUpdatedMyUser.setId(myUser.getId());

        partialUpdatedMyUser.surname(UPDATED_SURNAME);

        restMyUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMyUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMyUser))
            )
            .andExpect(status().isOk());

        // Validate the MyUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMyUserUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMyUser, myUser), getPersistedMyUser(myUser));
    }

    @Test
    @Transactional
    void fullUpdateMyUserWithPatch() throws Exception {
        // Initialize the database
        myUserRepository.saveAndFlush(myUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the myUser using partial update
        MyUser partialUpdatedMyUser = new MyUser();
        partialUpdatedMyUser.setId(myUser.getId());

        partialUpdatedMyUser.name(UPDATED_NAME).surname(UPDATED_SURNAME);

        restMyUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMyUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMyUser))
            )
            .andExpect(status().isOk());

        // Validate the MyUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMyUserUpdatableFieldsEquals(partialUpdatedMyUser, getPersistedMyUser(partialUpdatedMyUser));
    }

    @Test
    @Transactional
    void patchNonExistingMyUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        myUser.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMyUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, myUser.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(myUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMyUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        myUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(myUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the MyUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMyUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        myUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMyUserMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(myUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MyUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMyUser() throws Exception {
        // Initialize the database
        myUserRepository.saveAndFlush(myUser);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the myUser
        restMyUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, myUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return myUserRepository.count();
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

    protected MyUser getPersistedMyUser(MyUser myUser) {
        return myUserRepository.findById(myUser.getId()).orElseThrow();
    }

    protected void assertPersistedMyUserToMatchAllProperties(MyUser expectedMyUser) {
        assertMyUserAllPropertiesEquals(expectedMyUser, getPersistedMyUser(expectedMyUser));
    }

    protected void assertPersistedMyUserToMatchUpdatableProperties(MyUser expectedMyUser) {
        assertMyUserAllUpdatablePropertiesEquals(expectedMyUser, getPersistedMyUser(expectedMyUser));
    }
}
