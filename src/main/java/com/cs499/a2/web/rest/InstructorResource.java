package com.cs499.a2.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cs499.a2.domain.Instructor;

import com.cs499.a2.repository.InstructorRepository;
import com.cs499.a2.web.rest.util.HeaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Instructor.
 */
@RestController
@RequestMapping("/api")
public class InstructorResource {

    private final Logger log = LoggerFactory.getLogger(InstructorResource.class);
        
    @Inject
    private InstructorRepository instructorRepository;

    /**
     * POST  /instructors : Create a new instructor.
     *
     * @param instructor the instructor to create
     * @return the ResponseEntity with status 201 (Created) and with body the new instructor, or with status 400 (Bad Request) if the instructor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/instructors")
    @Timed
    public ResponseEntity<Instructor> createInstructor(@RequestBody Instructor instructor) throws URISyntaxException {
        log.debug("REST request to save Instructor : {}", instructor);
        if (instructor.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("instructor", "idexists", "A new instructor cannot already have an ID")).body(null);
        }
        Instructor result = instructorRepository.save(instructor);
        return ResponseEntity.created(new URI("/api/instructors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("instructor", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /instructors : Updates an existing instructor.
     *
     * @param instructor the instructor to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated instructor,
     * or with status 400 (Bad Request) if the instructor is not valid,
     * or with status 500 (Internal Server Error) if the instructor couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/instructors")
    @Timed
    public ResponseEntity<Instructor> updateInstructor(@RequestBody Instructor instructor) throws URISyntaxException {
        log.debug("REST request to update Instructor : {}", instructor);
        if (instructor.getId() == null) {
            return createInstructor(instructor);
        }
        Instructor result = instructorRepository.save(instructor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("instructor", instructor.getId().toString()))
            .body(result);
    }

    /**
     * GET  /instructors : get all the instructors.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of instructors in body
     */
    @GetMapping("/instructors")
    @Timed
    public List<Instructor> getAllInstructors() {
        log.debug("REST request to get all Instructors");
        List<Instructor> instructors = instructorRepository.findAll();
        return instructors;
    }

    /**
     * GET  /instructors/:id : get the "id" instructor.
     *
     * @param id the id of the instructor to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the instructor, or with status 404 (Not Found)
     */
    @GetMapping("/instructors/{id}")
    @Timed
    public ResponseEntity<Instructor> getInstructor(@PathVariable Long id) {
        log.debug("REST request to get Instructor : {}", id);
        Instructor instructor = instructorRepository.findOne(id);
        return Optional.ofNullable(instructor)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /instructors/:id : delete the "id" instructor.
     *
     * @param id the id of the instructor to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/instructors/{id}")
    @Timed
    public ResponseEntity<Void> deleteInstructor(@PathVariable Long id) {
        log.debug("REST request to delete Instructor : {}", id);
        instructorRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("instructor", id.toString())).build();
    }

}
