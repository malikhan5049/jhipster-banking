package com.banking.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banking.domain.Operation;
import com.banking.repository.OperationRepository;
import com.banking.security.SecurityUtils;
import com.banking.service.BalanceService;
import com.banking.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing Operation.
 */
@RestController
@RequestMapping("/api")
public class OperationResource {

    private final Logger log = LoggerFactory.getLogger(OperationResource.class);

    @Inject
    private OperationRepository operationRepository;
    @Inject
    private BalanceService balanceService;

    /**
     * POST  /operations -> Create a new operation.
     */
    @RequestMapping(value = "/operations",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody final Operation operation) throws URISyntaxException {
        log.debug("REST request to save Operation : {}", operation);
        if (operation.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new operation cannot already have an ID").build();
        }
        balanceService.add(operation);
        return ResponseEntity.created(new URI("/api/operations/" + operation.getId())).build();
    }

    /**
     * PUT  /operations -> Updates an existing operation.
     */
    @RequestMapping(value = "/operations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody final Operation operation) throws URISyntaxException {
        log.debug("REST request to update Operation : {}", operation);
        if (operation.getId() == null) {
            return create(operation);
        }
        operationRepository.save(operation);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /operations -> get all the operations.
     */
    @RequestMapping(value = "/operations",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Operation>> getAll(@RequestParam(value = "page" , required = false) final Integer offset,
                                  @RequestParam(value = "per_page", required = false) final Integer limit)
        throws URISyntaxException {
        Page<Operation> page = operationRepository.findByBankAccountUserLoginOrderByDateAsc(SecurityUtils.getCurrentLogin(), PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/operations", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /operations/:id -> get the "id" operation.
     */
    @RequestMapping(value = "/operations/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Operation> get(@PathVariable final Long id) {
        log.debug("REST request to get Operation : {}", id);
        return Optional.ofNullable(operationRepository.findOneWithEagerRelationships(id))
            .map(operation -> new ResponseEntity<>(
                operation,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /operations/:id -> delete the "id" operation.
     */
    @RequestMapping(value = "/operations/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable final Long id) {
        log.debug("REST request to delete Operation : {}", id);
        operationRepository.delete(id);
    }
}
