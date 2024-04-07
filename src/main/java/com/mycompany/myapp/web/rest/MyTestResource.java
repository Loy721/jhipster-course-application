package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.MyTest;
import com.mycompany.myapp.repository.MyTestRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.MyTest}.
 */
@RestController
@RequestMapping("/api/my-tests")
@Transactional
public class MyTestResource {

    private final Logger log = LoggerFactory.getLogger(MyTestResource.class);

    private static final String ENTITY_NAME = "myTest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MyTestRepository myTestRepository;

    public MyTestResource(MyTestRepository myTestRepository) {
        this.myTestRepository = myTestRepository;
    }

    /**
     * {@code POST  /my-tests} : Create a new myTest.
     *
     * @param myTest the myTest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new myTest, or with status {@code 400 (Bad Request)} if the myTest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MyTest> createMyTest(@RequestBody MyTest myTest) throws URISyntaxException {
        log.debug("REST request to save MyTest : {}", myTest);
        if (myTest.getId() != null) {
            throw new BadRequestAlertException("A new myTest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        myTest = myTestRepository.save(myTest);
        return ResponseEntity.created(new URI("/api/my-tests/" + myTest.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, myTest.getId().toString()))
            .body(myTest);
    }

    /**
     * {@code PUT  /my-tests/:id} : Updates an existing myTest.
     *
     * @param id the id of the myTest to save.
     * @param myTest the myTest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated myTest,
     * or with status {@code 400 (Bad Request)} if the myTest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the myTest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MyTest> updateMyTest(@PathVariable(value = "id", required = false) final Long id, @RequestBody MyTest myTest)
        throws URISyntaxException {
        log.debug("REST request to update MyTest : {}, {}", id, myTest);
        if (myTest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, myTest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!myTestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        myTest = myTestRepository.save(myTest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, myTest.getId().toString()))
            .body(myTest);
    }

    /**
     * {@code PATCH  /my-tests/:id} : Partial updates given fields of an existing myTest, field will ignore if it is null
     *
     * @param id the id of the myTest to save.
     * @param myTest the myTest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated myTest,
     * or with status {@code 400 (Bad Request)} if the myTest is not valid,
     * or with status {@code 404 (Not Found)} if the myTest is not found,
     * or with status {@code 500 (Internal Server Error)} if the myTest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MyTest> partialUpdateMyTest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MyTest myTest
    ) throws URISyntaxException {
        log.debug("REST request to partial update MyTest partially : {}, {}", id, myTest);
        if (myTest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, myTest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!myTestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MyTest> result = myTestRepository
            .findById(myTest.getId())
            .map(existingMyTest -> {
                if (myTest.getName() != null) {
                    existingMyTest.setName(myTest.getName());
                }
                if (myTest.getQuestions() != null) {
                    existingMyTest.setQuestions(myTest.getQuestions());
                }

                return existingMyTest;
            })
            .map(myTestRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, myTest.getId().toString())
        );
    }

    /**
     * {@code GET  /my-tests} : get all the myTests.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of myTests in body.
     */
    @GetMapping("")
    public List<MyTest> getAllMyTests() {
        log.debug("REST request to get all MyTests");
        return myTestRepository.findAll();
    }

    /**
     * {@code GET  /my-tests/:id} : get the "id" myTest.
     *
     * @param id the id of the myTest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the myTest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MyTest> getMyTest(@PathVariable("id") Long id) {
        log.debug("REST request to get MyTest : {}", id);
        Optional<MyTest> myTest = myTestRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(myTest);
    }

    /**
     * {@code DELETE  /my-tests/:id} : delete the "id" myTest.
     *
     * @param id the id of the myTest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMyTest(@PathVariable("id") Long id) {
        log.debug("REST request to delete MyTest : {}", id);
        myTestRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
