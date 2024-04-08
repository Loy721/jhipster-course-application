package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.FileAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.File;
import com.mycompany.myapp.repository.FileRepository;
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
 * Integration tests for the {@link FileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FileResourceIT {

    private static final String DEFAULT_TOPIC = "AAAAAAAAAA";
    private static final String UPDATED_TOPIC = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/files";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFileMockMvc;

    private File file;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static File createEntity(EntityManager em) {
        File file = new File().topic(DEFAULT_TOPIC).content(DEFAULT_CONTENT);
        return file;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static File createUpdatedEntity(EntityManager em) {
        File file = new File().topic(UPDATED_TOPIC).content(UPDATED_CONTENT);
        return file;
    }

    @BeforeEach
    public void initTest() {
        file = createEntity(em);
    }

    @Test
    @Transactional
    void createFile() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the File
        var returnedFile = om.readValue(
            restFileMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(file)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            File.class
        );

        // Validate the File in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFileUpdatableFieldsEquals(returnedFile, getPersistedFile(returnedFile));
    }

    @Test
    @Transactional
    void createFileWithExistingId() throws Exception {
        // Create the File with an existing ID
        file.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(file)))
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFiles() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        // Get all the fileList
        restFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(file.getId().intValue())))
            .andExpect(jsonPath("$.[*].topic").value(hasItem(DEFAULT_TOPIC)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)));
    }

    @Test
    @Transactional
    void getFile() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        // Get the file
        restFileMockMvc
            .perform(get(ENTITY_API_URL_ID, file.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(file.getId().intValue()))
            .andExpect(jsonPath("$.topic").value(DEFAULT_TOPIC))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT));
    }

    @Test
    @Transactional
    void getNonExistingFile() throws Exception {
        // Get the file
        restFileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFile() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the file
        File updatedFile = fileRepository.findById(file.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFile are not directly saved in db
        em.detach(updatedFile);
        updatedFile.topic(UPDATED_TOPIC).content(UPDATED_CONTENT);

        restFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFile.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFile))
            )
            .andExpect(status().isOk());

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFileToMatchAllProperties(updatedFile);
    }

    @Test
    @Transactional
    void putNonExistingFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        file.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(put(ENTITY_API_URL_ID, file.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(file)))
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        file.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(file))
            )
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        file.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(file)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFileWithPatch() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the file using partial update
        File partialUpdatedFile = new File();
        partialUpdatedFile.setId(file.getId());

        restFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFile))
            )
            .andExpect(status().isOk());

        // Validate the File in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFileUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFile, file), getPersistedFile(file));
    }

    @Test
    @Transactional
    void fullUpdateFileWithPatch() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the file using partial update
        File partialUpdatedFile = new File();
        partialUpdatedFile.setId(file.getId());

        partialUpdatedFile.topic(UPDATED_TOPIC).content(UPDATED_CONTENT);

        restFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFile))
            )
            .andExpect(status().isOk());

        // Validate the File in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFileUpdatableFieldsEquals(partialUpdatedFile, getPersistedFile(partialUpdatedFile));
    }

    @Test
    @Transactional
    void patchNonExistingFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        file.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(patch(ENTITY_API_URL_ID, file.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(file)))
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        file.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(file))
            )
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        file.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(file)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFile() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the file
        restFileMockMvc
            .perform(delete(ENTITY_API_URL_ID, file.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return fileRepository.count();
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

    protected File getPersistedFile(File file) {
        return fileRepository.findById(file.getId()).orElseThrow();
    }

    protected void assertPersistedFileToMatchAllProperties(File expectedFile) {
        assertFileAllPropertiesEquals(expectedFile, getPersistedFile(expectedFile));
    }

    protected void assertPersistedFileToMatchUpdatableProperties(File expectedFile) {
        assertFileAllUpdatablePropertiesEquals(expectedFile, getPersistedFile(expectedFile));
    }
}
