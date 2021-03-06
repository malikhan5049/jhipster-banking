package com.banking.web.rest;

import com.banking.Application;
import com.banking.domain.Operation;
import com.banking.repository.OperationRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OperationResource REST controller.
 *
 * @see OperationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class OperationResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    private static final DateTime DEFAULT_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_DATE_STR = dateTimeFormatter.print(DEFAULT_DATE);
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    private static final BigDecimal DEFAULT_AMOUNT = BigDecimal.ZERO;
    private static final BigDecimal UPDATED_AMOUNT = BigDecimal.ONE;

    @Inject
    private OperationRepository operationRepository;

    private MockMvc restOperationMockMvc;

    private Operation operation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OperationResource operationResource = new OperationResource();
        ReflectionTestUtils.setField(operationResource, "operationRepository", operationRepository);
        this.restOperationMockMvc = MockMvcBuilders.standaloneSetup(operationResource).build();
    }

    @Before
    public void initTest() {
        operation = new Operation();
        operation.setDate(DEFAULT_DATE);
        operation.setDescription(DEFAULT_DESCRIPTION);
        operation.setAmount(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createOperation() throws Exception {
        int databaseSizeBeforeCreate = operationRepository.findAll().size();

        // Create the Operation
        restOperationMockMvc.perform(post("/api/operations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(operation)))
                .andExpect(status().isCreated());

        // Validate the Operation in the database
        List<Operation> operations = operationRepository.findAll();
        assertThat(operations).hasSize(databaseSizeBeforeCreate + 1);
        Operation testOperation = operations.get(operations.size() - 1);
        assertThat(testOperation.getDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_DATE);
        assertThat(testOperation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOperation.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(operationRepository.findAll()).hasSize(0);
        // set the field null
        operation.setAmount(null);

        // Create the Operation, which fails.
        restOperationMockMvc.perform(post("/api/operations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(operation)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Operation> operations = operationRepository.findAll();
        assertThat(operations).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllOperations() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get all the operations
        restOperationMockMvc.perform(get("/api/operations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(operation.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));
    }

    @Test
    @Transactional
    public void getOperation() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get the operation
        restOperationMockMvc.perform(get("/api/operations/{id}", operation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(operation.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE_STR))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOperation() throws Exception {
        // Get the operation
        restOperationMockMvc.perform(get("/api/operations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOperation() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

		int databaseSizeBeforeUpdate = operationRepository.findAll().size();

        // Update the operation
        operation.setDate(UPDATED_DATE);
        operation.setDescription(UPDATED_DESCRIPTION);
        operation.setAmount(UPDATED_AMOUNT);
        restOperationMockMvc.perform(put("/api/operations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(operation)))
                .andExpect(status().isOk());

        // Validate the Operation in the database
        List<Operation> operations = operationRepository.findAll();
        assertThat(operations).hasSize(databaseSizeBeforeUpdate);
        Operation testOperation = operations.get(operations.size() - 1);
        assertThat(testOperation.getDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_DATE);
        assertThat(testOperation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOperation.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void deleteOperation() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

		int databaseSizeBeforeDelete = operationRepository.findAll().size();

        // Get the operation
        restOperationMockMvc.perform(delete("/api/operations/{id}", operation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Operation> operations = operationRepository.findAll();
        assertThat(operations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
