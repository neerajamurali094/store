package com.diviso.graeshoppe.web.rest;

import com.diviso.graeshoppe.StoreApp;

import com.diviso.graeshoppe.domain.Propreitor;
import com.diviso.graeshoppe.repository.PropreitorRepository;
import com.diviso.graeshoppe.repository.search.PropreitorSearchRepository;
import com.diviso.graeshoppe.service.PropreitorService;
import com.diviso.graeshoppe.service.dto.PropreitorDTO;
import com.diviso.graeshoppe.service.mapper.PropreitorMapper;
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
 * Test class for the PropreitorResource REST controller.
 *
 * @see PropreitorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StoreApp.class)
public class PropreitorResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private PropreitorRepository propreitorRepository;

    @Autowired
    private PropreitorMapper propreitorMapper;

    @Autowired
    private PropreitorService propreitorService;

    /**
     * This repository is mocked in the com.diviso.graeshoppe.repository.search test package.
     *
     * @see com.diviso.graeshoppe.repository.search.PropreitorSearchRepositoryMockConfiguration
     */
    @Autowired
    private PropreitorSearchRepository mockPropreitorSearchRepository;

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

    private MockMvc restPropreitorMockMvc;

    private Propreitor propreitor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PropreitorResource propreitorResource = new PropreitorResource(propreitorService);
        this.restPropreitorMockMvc = MockMvcBuilders.standaloneSetup(propreitorResource)
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
    public static Propreitor createEntity(EntityManager em) {
        Propreitor propreitor = new Propreitor()
            .name(DEFAULT_NAME);
        return propreitor;
    }

    @Before
    public void initTest() {
        propreitor = createEntity(em);
    }

    @Test
    @Transactional
    public void createPropreitor() throws Exception {
        int databaseSizeBeforeCreate = propreitorRepository.findAll().size();

        // Create the Propreitor
        PropreitorDTO propreitorDTO = propreitorMapper.toDto(propreitor);
        restPropreitorMockMvc.perform(post("/api/propreitors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(propreitorDTO)))
            .andExpect(status().isCreated());

        // Validate the Propreitor in the database
        List<Propreitor> propreitorList = propreitorRepository.findAll();
        assertThat(propreitorList).hasSize(databaseSizeBeforeCreate + 1);
        Propreitor testPropreitor = propreitorList.get(propreitorList.size() - 1);
        assertThat(testPropreitor.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Propreitor in Elasticsearch
        verify(mockPropreitorSearchRepository, times(1)).save(testPropreitor);
    }

    @Test
    @Transactional
    public void createPropreitorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = propreitorRepository.findAll().size();

        // Create the Propreitor with an existing ID
        propreitor.setId(1L);
        PropreitorDTO propreitorDTO = propreitorMapper.toDto(propreitor);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPropreitorMockMvc.perform(post("/api/propreitors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(propreitorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Propreitor in the database
        List<Propreitor> propreitorList = propreitorRepository.findAll();
        assertThat(propreitorList).hasSize(databaseSizeBeforeCreate);

        // Validate the Propreitor in Elasticsearch
        verify(mockPropreitorSearchRepository, times(0)).save(propreitor);
    }

    @Test
    @Transactional
    public void getAllPropreitors() throws Exception {
        // Initialize the database
        propreitorRepository.saveAndFlush(propreitor);

        // Get all the propreitorList
        restPropreitorMockMvc.perform(get("/api/propreitors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(propreitor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getPropreitor() throws Exception {
        // Initialize the database
        propreitorRepository.saveAndFlush(propreitor);

        // Get the propreitor
        restPropreitorMockMvc.perform(get("/api/propreitors/{id}", propreitor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(propreitor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPropreitor() throws Exception {
        // Get the propreitor
        restPropreitorMockMvc.perform(get("/api/propreitors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePropreitor() throws Exception {
        // Initialize the database
        propreitorRepository.saveAndFlush(propreitor);

        int databaseSizeBeforeUpdate = propreitorRepository.findAll().size();

        // Update the propreitor
        Propreitor updatedPropreitor = propreitorRepository.findById(propreitor.getId()).get();
        // Disconnect from session so that the updates on updatedPropreitor are not directly saved in db
        em.detach(updatedPropreitor);
        updatedPropreitor
            .name(UPDATED_NAME);
        PropreitorDTO propreitorDTO = propreitorMapper.toDto(updatedPropreitor);

        restPropreitorMockMvc.perform(put("/api/propreitors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(propreitorDTO)))
            .andExpect(status().isOk());

        // Validate the Propreitor in the database
        List<Propreitor> propreitorList = propreitorRepository.findAll();
        assertThat(propreitorList).hasSize(databaseSizeBeforeUpdate);
        Propreitor testPropreitor = propreitorList.get(propreitorList.size() - 1);
        assertThat(testPropreitor.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Propreitor in Elasticsearch
        verify(mockPropreitorSearchRepository, times(1)).save(testPropreitor);
    }

    @Test
    @Transactional
    public void updateNonExistingPropreitor() throws Exception {
        int databaseSizeBeforeUpdate = propreitorRepository.findAll().size();

        // Create the Propreitor
        PropreitorDTO propreitorDTO = propreitorMapper.toDto(propreitor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPropreitorMockMvc.perform(put("/api/propreitors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(propreitorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Propreitor in the database
        List<Propreitor> propreitorList = propreitorRepository.findAll();
        assertThat(propreitorList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Propreitor in Elasticsearch
        verify(mockPropreitorSearchRepository, times(0)).save(propreitor);
    }

    @Test
    @Transactional
    public void deletePropreitor() throws Exception {
        // Initialize the database
        propreitorRepository.saveAndFlush(propreitor);

        int databaseSizeBeforeDelete = propreitorRepository.findAll().size();

        // Delete the propreitor
        restPropreitorMockMvc.perform(delete("/api/propreitors/{id}", propreitor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Propreitor> propreitorList = propreitorRepository.findAll();
        assertThat(propreitorList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Propreitor in Elasticsearch
        verify(mockPropreitorSearchRepository, times(1)).deleteById(propreitor.getId());
    }

    @Test
    @Transactional
    public void searchPropreitor() throws Exception {
        // Initialize the database
        propreitorRepository.saveAndFlush(propreitor);
        when(mockPropreitorSearchRepository.search(queryStringQuery("id:" + propreitor.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(propreitor), PageRequest.of(0, 1), 1));
        // Search the propreitor
        restPropreitorMockMvc.perform(get("/api/_search/propreitors?query=id:" + propreitor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(propreitor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Propreitor.class);
        Propreitor propreitor1 = new Propreitor();
        propreitor1.setId(1L);
        Propreitor propreitor2 = new Propreitor();
        propreitor2.setId(propreitor1.getId());
        assertThat(propreitor1).isEqualTo(propreitor2);
        propreitor2.setId(2L);
        assertThat(propreitor1).isNotEqualTo(propreitor2);
        propreitor1.setId(null);
        assertThat(propreitor1).isNotEqualTo(propreitor2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PropreitorDTO.class);
        PropreitorDTO propreitorDTO1 = new PropreitorDTO();
        propreitorDTO1.setId(1L);
        PropreitorDTO propreitorDTO2 = new PropreitorDTO();
        assertThat(propreitorDTO1).isNotEqualTo(propreitorDTO2);
        propreitorDTO2.setId(propreitorDTO1.getId());
        assertThat(propreitorDTO1).isEqualTo(propreitorDTO2);
        propreitorDTO2.setId(2L);
        assertThat(propreitorDTO1).isNotEqualTo(propreitorDTO2);
        propreitorDTO1.setId(null);
        assertThat(propreitorDTO1).isNotEqualTo(propreitorDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(propreitorMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(propreitorMapper.fromId(null)).isNull();
    }
}
