package com.diviso.graeshoppe.web.rest;

import com.diviso.graeshoppe.StoreApp;

import com.diviso.graeshoppe.domain.UniqueStoreID;
import com.diviso.graeshoppe.repository.UniqueStoreIDRepository;
import com.diviso.graeshoppe.repository.search.UniqueStoreIDSearchRepository;
import com.diviso.graeshoppe.service.UniqueStoreIDService;
import com.diviso.graeshoppe.service.dto.UniqueStoreIDDTO;
import com.diviso.graeshoppe.service.mapper.UniqueStoreIDMapper;
import com.diviso.graeshoppe.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.diviso.graeshoppe.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UniqueStoreIDResource REST controller.
 *
 * @see UniqueStoreIDResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StoreApp.class)
public class UniqueStoreIDResourceIntTest {

    @Autowired
    private UniqueStoreIDRepository uniqueStoreIDRepository;

    @Autowired
    private UniqueStoreIDMapper uniqueStoreIDMapper;

    @Autowired
    private UniqueStoreIDService uniqueStoreIDService;

    /**
     * This repository is mocked in the com.diviso.graeshoppe.repository.search test package.
     *
     * @see com.diviso.graeshoppe.repository.search.UniqueStoreIDSearchRepositoryMockConfiguration
     */
    @Autowired
    private UniqueStoreIDSearchRepository mockUniqueStoreIDSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restUniqueStoreIDMockMvc;

    private UniqueStoreID uniqueStoreID;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UniqueStoreIDResource uniqueStoreIDResource = new UniqueStoreIDResource(uniqueStoreIDService);
        this.restUniqueStoreIDMockMvc = MockMvcBuilders.standaloneSetup(uniqueStoreIDResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UniqueStoreID createEntity(EntityManager em) {
        UniqueStoreID uniqueStoreID = new UniqueStoreID();
        return uniqueStoreID;
    }

    @Before
    public void initTest() {
        uniqueStoreID = createEntity(em);
    }

    @Test
    @Transactional
    public void createUniqueStoreID() throws Exception {
        int databaseSizeBeforeCreate = uniqueStoreIDRepository.findAll().size();

        // Create the UniqueStoreID
        UniqueStoreIDDTO uniqueStoreIDDTO = uniqueStoreIDMapper.toDto(uniqueStoreID);
        restUniqueStoreIDMockMvc.perform(post("/api/unique-store-ids")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(uniqueStoreIDDTO)))
            .andExpect(status().isCreated());

        // Validate the UniqueStoreID in the database
        List<UniqueStoreID> uniqueStoreIDList = uniqueStoreIDRepository.findAll();
        assertThat(uniqueStoreIDList).hasSize(databaseSizeBeforeCreate + 1);
        UniqueStoreID testUniqueStoreID = uniqueStoreIDList.get(uniqueStoreIDList.size() - 1);

        // Validate the UniqueStoreID in Elasticsearch
        verify(mockUniqueStoreIDSearchRepository, times(1)).save(testUniqueStoreID);
    }

    @Test
    @Transactional
    public void createUniqueStoreIDWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = uniqueStoreIDRepository.findAll().size();

        // Create the UniqueStoreID with an existing ID
        uniqueStoreID.setId(1L);
        UniqueStoreIDDTO uniqueStoreIDDTO = uniqueStoreIDMapper.toDto(uniqueStoreID);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUniqueStoreIDMockMvc.perform(post("/api/unique-store-ids")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(uniqueStoreIDDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UniqueStoreID in the database
        List<UniqueStoreID> uniqueStoreIDList = uniqueStoreIDRepository.findAll();
        assertThat(uniqueStoreIDList).hasSize(databaseSizeBeforeCreate);

        // Validate the UniqueStoreID in Elasticsearch
        verify(mockUniqueStoreIDSearchRepository, times(0)).save(uniqueStoreID);
    }

    @Test
    @Transactional
    public void getAllUniqueStoreIDS() throws Exception {
        // Initialize the database
        uniqueStoreIDRepository.saveAndFlush(uniqueStoreID);

        // Get all the uniqueStoreIDList
        restUniqueStoreIDMockMvc.perform(get("/api/unique-store-ids?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uniqueStoreID.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getUniqueStoreID() throws Exception {
        // Initialize the database
        uniqueStoreIDRepository.saveAndFlush(uniqueStoreID);

        // Get the uniqueStoreID
        restUniqueStoreIDMockMvc.perform(get("/api/unique-store-ids/{id}", uniqueStoreID.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(uniqueStoreID.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUniqueStoreID() throws Exception {
        // Get the uniqueStoreID
        restUniqueStoreIDMockMvc.perform(get("/api/unique-store-ids/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUniqueStoreID() throws Exception {
        // Initialize the database
        uniqueStoreIDRepository.saveAndFlush(uniqueStoreID);

        int databaseSizeBeforeUpdate = uniqueStoreIDRepository.findAll().size();

        // Update the uniqueStoreID
        UniqueStoreID updatedUniqueStoreID = uniqueStoreIDRepository.findById(uniqueStoreID.getId()).get();
        // Disconnect from session so that the updates on updatedUniqueStoreID are not directly saved in db
        em.detach(updatedUniqueStoreID);
        UniqueStoreIDDTO uniqueStoreIDDTO = uniqueStoreIDMapper.toDto(updatedUniqueStoreID);

        restUniqueStoreIDMockMvc.perform(put("/api/unique-store-ids")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(uniqueStoreIDDTO)))
            .andExpect(status().isOk());

        // Validate the UniqueStoreID in the database
        List<UniqueStoreID> uniqueStoreIDList = uniqueStoreIDRepository.findAll();
        assertThat(uniqueStoreIDList).hasSize(databaseSizeBeforeUpdate);
        UniqueStoreID testUniqueStoreID = uniqueStoreIDList.get(uniqueStoreIDList.size() - 1);

        // Validate the UniqueStoreID in Elasticsearch
        verify(mockUniqueStoreIDSearchRepository, times(1)).save(testUniqueStoreID);
    }

    @Test
    @Transactional
    public void updateNonExistingUniqueStoreID() throws Exception {
        int databaseSizeBeforeUpdate = uniqueStoreIDRepository.findAll().size();

        // Create the UniqueStoreID
        UniqueStoreIDDTO uniqueStoreIDDTO = uniqueStoreIDMapper.toDto(uniqueStoreID);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUniqueStoreIDMockMvc.perform(put("/api/unique-store-ids")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(uniqueStoreIDDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UniqueStoreID in the database
        List<UniqueStoreID> uniqueStoreIDList = uniqueStoreIDRepository.findAll();
        assertThat(uniqueStoreIDList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UniqueStoreID in Elasticsearch
        verify(mockUniqueStoreIDSearchRepository, times(0)).save(uniqueStoreID);
    }

    @Test
    @Transactional
    public void deleteUniqueStoreID() throws Exception {
        // Initialize the database
        uniqueStoreIDRepository.saveAndFlush(uniqueStoreID);

        int databaseSizeBeforeDelete = uniqueStoreIDRepository.findAll().size();

        // Delete the uniqueStoreID
        restUniqueStoreIDMockMvc.perform(delete("/api/unique-store-ids/{id}", uniqueStoreID.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UniqueStoreID> uniqueStoreIDList = uniqueStoreIDRepository.findAll();
        assertThat(uniqueStoreIDList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the UniqueStoreID in Elasticsearch
        verify(mockUniqueStoreIDSearchRepository, times(1)).deleteById(uniqueStoreID.getId());
    }

    @Test
    @Transactional
    public void searchUniqueStoreID() throws Exception {
        // Initialize the database
        uniqueStoreIDRepository.saveAndFlush(uniqueStoreID);
        when(mockUniqueStoreIDSearchRepository.search(queryStringQuery("id:" + uniqueStoreID.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(uniqueStoreID), PageRequest.of(0, 1), 1));
        // Search the uniqueStoreID
        restUniqueStoreIDMockMvc.perform(get("/api/_search/unique-store-ids?query=id:" + uniqueStoreID.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uniqueStoreID.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UniqueStoreID.class);
        UniqueStoreID uniqueStoreID1 = new UniqueStoreID();
        uniqueStoreID1.setId(1L);
        UniqueStoreID uniqueStoreID2 = new UniqueStoreID();
        uniqueStoreID2.setId(uniqueStoreID1.getId());
        assertThat(uniqueStoreID1).isEqualTo(uniqueStoreID2);
        uniqueStoreID2.setId(2L);
        assertThat(uniqueStoreID1).isNotEqualTo(uniqueStoreID2);
        uniqueStoreID1.setId(null);
        assertThat(uniqueStoreID1).isNotEqualTo(uniqueStoreID2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UniqueStoreIDDTO.class);
        UniqueStoreIDDTO uniqueStoreIDDTO1 = new UniqueStoreIDDTO();
        uniqueStoreIDDTO1.setId(1L);
        UniqueStoreIDDTO uniqueStoreIDDTO2 = new UniqueStoreIDDTO();
        assertThat(uniqueStoreIDDTO1).isNotEqualTo(uniqueStoreIDDTO2);
        uniqueStoreIDDTO2.setId(uniqueStoreIDDTO1.getId());
        assertThat(uniqueStoreIDDTO1).isEqualTo(uniqueStoreIDDTO2);
        uniqueStoreIDDTO2.setId(2L);
        assertThat(uniqueStoreIDDTO1).isNotEqualTo(uniqueStoreIDDTO2);
        uniqueStoreIDDTO1.setId(null);
        assertThat(uniqueStoreIDDTO1).isNotEqualTo(uniqueStoreIDDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(uniqueStoreIDMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(uniqueStoreIDMapper.fromId(null)).isNull();
    }
}
