package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.MyUser;
import com.mycompany.myapp.repository.MyUserRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.MyUser}.
 */
@RestController
@RequestMapping("/api/my-users")
@Transactional
public class MyUserResource {

    private final Logger log = LoggerFactory.getLogger(MyUserResource.class);

    private static final String ENTITY_NAME = "myUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MyUserRepository myUserRepository;

    public MyUserResource(MyUserRepository myUserRepository) {
        this.myUserRepository = myUserRepository;
    }

    /**
     * {@code POST  /my-users} : Create a new myUser.
     *
     * @param myUser the myUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new myUser, or with status {@code 400 (Bad Request)} if the myUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MyUser> createMyUser(@RequestBody MyUser myUser) throws URISyntaxException {
        log.debug("REST request to save MyUser : {}", myUser);
        if (myUser.getId() != null) {
            throw new BadRequestAlertException("A new myUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        myUser = myUserRepository.save(myUser);
        return ResponseEntity.created(new URI("/api/my-users/" + myUser.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, myUser.getId().toString()))
            .body(myUser);
    }

    /**
     * {@code PUT  /my-users/:id} : Updates an existing myUser.
     *
     * @param id the id of the myUser to save.
     * @param myUser the myUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated myUser,
     * or with status {@code 400 (Bad Request)} if the myUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the myUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MyUser> updateMyUser(@PathVariable(value = "id", required = false) final Long id, @RequestBody MyUser myUser)
        throws URISyntaxException {
        log.debug("REST request to update MyUser : {}, {}", id, myUser);
        if (myUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, myUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!myUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        myUser = myUserRepository.save(myUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, myUser.getId().toString()))
            .body(myUser);
    }

    /**
     * {@code PATCH  /my-users/:id} : Partial updates given fields of an existing myUser, field will ignore if it is null
     *
     * @param id the id of the myUser to save.
     * @param myUser the myUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated myUser,
     * or with status {@code 400 (Bad Request)} if the myUser is not valid,
     * or with status {@code 404 (Not Found)} if the myUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the myUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MyUser> partialUpdateMyUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MyUser myUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update MyUser partially : {}, {}", id, myUser);
        if (myUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, myUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!myUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MyUser> result = myUserRepository
            .findById(myUser.getId())
            .map(existingMyUser -> {
                if (myUser.getName() != null) {
                    existingMyUser.setName(myUser.getName());
                }
                if (myUser.getSurname() != null) {
                    existingMyUser.setSurname(myUser.getSurname());
                }

                return existingMyUser;
            })
            .map(myUserRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, myUser.getId().toString())
        );
    }

    /**
     * {@code GET  /my-users} : get all the myUsers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of myUsers in body.
     */
    @GetMapping("")
    public List<MyUser> getAllMyUsers() {
        log.debug("REST request to get all MyUsers");
        return myUserRepository.findAll();
    }

    /**
     * {@code GET  /my-users/:id} : get the "id" myUser.
     *
     * @param id the id of the myUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the myUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MyUser> getMyUser(@PathVariable("id") Long id) {
        log.debug("REST request to get MyUser : {}", id);
        Optional<MyUser> myUser = myUserRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(myUser);
    }

    /**
     * {@code DELETE  /my-users/:id} : delete the "id" myUser.
     *
     * @param id the id of the myUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMyUser(@PathVariable("id") Long id) {
        log.debug("REST request to delete MyUser : {}", id);
        myUserRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
