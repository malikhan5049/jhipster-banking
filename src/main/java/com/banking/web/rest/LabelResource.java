package com.banking.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.banking.domain.Label;
import com.banking.repository.LabelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Label.
 */
@RestController
@RequestMapping("/api")
public class LabelResource {

    private final Logger log = LoggerFactory.getLogger(LabelResource.class);

    @Inject
    private LabelRepository labelRepository;

    /**
     * POST  /labels -> Create a new label.
     */
    @RequestMapping(value = "/labels",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Label label) throws URISyntaxException {
        log.debug("REST request to save Label : {}", label);
        if (label.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new label cannot already have an ID").build();
        }
        labelRepository.save(label);
        return ResponseEntity.created(new URI("/api/labels/" + label.getId())).build();
    }

    /**
     * PUT  /labels -> Updates an existing label.
     */
    @RequestMapping(value = "/labels",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Label label) throws URISyntaxException {
        log.debug("REST request to update Label : {}", label);
        if (label.getId() == null) {
            return create(label);
        }
        labelRepository.save(label);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /labels -> get all the labels.
     */
    @RequestMapping(value = "/labels",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Label> getAll() {
        log.debug("REST request to get all Labels");
        return labelRepository.findAll();
    }

    /**
     * GET  /labels/:id -> get the "id" label.
     */
    @RequestMapping(value = "/labels/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Label> get(@PathVariable Long id) {
        log.debug("REST request to get Label : {}", id);
        return Optional.ofNullable(labelRepository.findOne(id))
            .map(label -> new ResponseEntity<>(
                label,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /labels/:id -> delete the "id" label.
     */
    @RequestMapping(value = "/labels/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Label : {}", id);
        labelRepository.delete(id);
    }
}
