package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.MyUserMyTest;
import com.mycompany.myapp.repository.MyUserMyTestRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.MyUserMyTest}.
 */
@RestController
@RequestMapping("/api/my-user-my-tests")
@Transactional
public class MyUserMyTestResource {

    private final Logger log = LoggerFactory.getLogger(MyUserMyTestResource.class);

    private static final String ENTITY_NAME = "myUserMyTest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MyUserMyTestRepository myUserMyTestRepository;

    public MyUserMyTestResource(MyUserMyTestRepository myUserMyTestRepository) {
        this.myUserMyTestRepository = myUserMyTestRepository;
    }

    /**
     * {@code POST  /my-user-my-tests} : Create a new myUserMyTest.
     *
     * @param myUserMyTest the myUserMyTest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new myUserMyTest, or with status {@code 400 (Bad Request)} if the myUserMyTest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MyUserMyTest> createMyUserMyTest(@RequestBody MyUserMyTest myUserMyTest) throws URISyntaxException {
        log.debug("REST request to save MyUserMyTest : {}", myUserMyTest);
        if (myUserMyTest.getId() != null) {
            throw new BadRequestAlertException("A new myUserMyTest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        myUserMyTest = myUserMyTestRepository.save(myUserMyTest);
        return ResponseEntity.created(new URI("/api/my-user-my-tests/" + myUserMyTest.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, myUserMyTest.getId().toString()))
            .body(myUserMyTest);
    }

    /**
     * {@code PUT  /my-user-my-tests/:id} : Updates an existing myUserMyTest.
     *
     * @param id the id of the myUserMyTest to save.
     * @param myUserMyTest the myUserMyTest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated myUserMyTest,
     * or with status {@code 400 (Bad Request)} if the myUserMyTest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the myUserMyTest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MyUserMyTest> updateMyUserMyTest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MyUserMyTest myUserMyTest
    ) throws URISyntaxException {
        log.debug("REST request to update MyUserMyTest : {}, {}", id, myUserMyTest);
        if (myUserMyTest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, myUserMyTest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!myUserMyTestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        myUserMyTest = myUserMyTestRepository.save(myUserMyTest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, myUserMyTest.getId().toString()))
            .body(myUserMyTest);
    }

    /**
     * {@code PATCH  /my-user-my-tests/:id} : Partial updates given fields of an existing myUserMyTest, field will ignore if it is null
     *
     * @param id the id of the myUserMyTest to save.
     * @param myUserMyTest the myUserMyTest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated myUserMyTest,
     * or with status {@code 400 (Bad Request)} if the myUserMyTest is not valid,
     * or with status {@code 404 (Not Found)} if the myUserMyTest is not found,
     * or with status {@code 500 (Internal Server Error)} if the myUserMyTest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MyUserMyTest> partialUpdateMyUserMyTest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MyUserMyTest myUserMyTest
    ) throws URISyntaxException {
        log.debug("REST request to partial update MyUserMyTest partially : {}, {}", id, myUserMyTest);
        if (myUserMyTest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, myUserMyTest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!myUserMyTestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MyUserMyTest> result = myUserMyTestRepository
            .findById(myUserMyTest.getId())
            .map(existingMyUserMyTest -> {
                if (myUserMyTest.getGrade() != null) {
                    existingMyUserMyTest.setGrade(myUserMyTest.getGrade());
                }

                return existingMyUserMyTest;
            })
            .map(myUserMyTestRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, myUserMyTest.getId().toString())
        );
    }

    /**
     * {@code GET  /my-user-my-tests} : get all the myUserMyTests.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of myUserMyTests in body.
     */
    @GetMapping("")
    public List<MyUserMyTest> getAllMyUserMyTests() {
        log.debug("REST request to get all MyUserMyTests");
        return myUserMyTestRepository.findAll();
    }

    /**
     * {@code GET  /my-user-my-tests/:id} : get the "id" myUserMyTest.
     *
     * @param id the id of the myUserMyTest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the myUserMyTest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MyUserMyTest> getMyUserMyTest(@PathVariable("id") Long id) {
        log.debug("REST request to get MyUserMyTest : {}", id);
        Optional<MyUserMyTest> myUserMyTest = myUserMyTestRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(myUserMyTest);
    }

    /**
     * {@code DELETE  /my-user-my-tests/:id} : delete the "id" myUserMyTest.
     *
     * @param id the id of the myUserMyTest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMyUserMyTest(@PathVariable("id") Long id) {
        log.debug("REST request to delete MyUserMyTest : {}", id);
        myUserMyTestRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
