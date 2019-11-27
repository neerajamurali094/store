package com.diviso.graeshoppe.web.rest;

import com.diviso.graeshoppe.StoreApp;

import com.diviso.graeshoppe.domain.PreOrderSettings;
import com.diviso.graeshoppe.repository.PreOrderSettingsRepository;
import com.diviso.graeshoppe.repository.search.PreOrderSettingsSearchRepository;
import com.diviso.graeshoppe.service.PreOrderSettingsService;
import com.diviso.graeshoppe.service.dto.PreOrderSettingsDTO;
import com.diviso.graeshoppe.service.mapper.PreOrderSettingsMapper;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;


import static com.diviso.graeshoppe.web.rest.TestUtil.sameInstant;
import static com.diviso.graeshoppe.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PreOrderSettingsResource REST controller.
 *
 * @see PreOrderSettingsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StoreApp.class)
public class PreOrderSettingsResourceIntTest {

    private static final Boolean DEFAULT_IS_PRE_ORDER_AVAILABLE = false;
    private static final Boolean UPDATED_IS_PRE_ORDER_AVAILABLE = true;

    private static final ZonedDateTime DEFAULT_FROM_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FROM_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_TO_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TO_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private PreOrderSettingsRepository preOrderSettingsRepository;

    @Autowired
    private PreOrderSettingsMapper preOrderSettingsMapper;

    @Autowired
    private PreOrderSettingsService preOrderSettingsService;

    /**
     * This repository is mocked in the com.diviso.graeshoppe.repository.search test package.
     *
     * @see com.diviso.graeshoppe.repository.search.PreOrderSettingsSearchRepositoryMockConfiguration
     */
    @Autowired
    private PreOrderSettingsSearchRepository mockPreOrderSettingsSearchRepository;

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

    private MockMvc restPreOrderSettingsMockMvc;

    private PreOrderSettings preOrderSettings;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PreOrderSettingsResource preOrderSettingsResource = new PreOrderSettingsResource(preOrderSettingsService);
        this.restPreOrderSettingsMockMvc = MockMvcBuilders.standaloneSetup(preOrderSettingsResource)
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
    public static PreOrderSettings createEntity(EntityManager em) {
        PreOrderSettings preOrderSettings = new PreOrderSettings()
            .isPreOrderAvailable(DEFAULT_IS_PRE_ORDER_AVAILABLE)
            .fromTime(DEFAULT_FROM_TIME)
            .toTime(DEFAULT_TO_TIME);
        return preOrderSettings;
    }

    @Before
    public void initTest() {
        preOrderSettings = createEntity(em);
    }

    @Test
    @Transactional
    public void createPreOrderSettings() throws Exception {
        int databaseSizeBeforeCreate = preOrderSettingsRepository.findAll().size();

        // Create the PreOrderSettings
        PreOrderSettingsDTO preOrderSettingsDTO = preOrderSettingsMapper.toDto(preOrderSettings);
        restPreOrderSettingsMockMvc.perform(post("/api/pre-order-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preOrderSettingsDTO)))
            .andExpect(status().isCreated());

        // Validate the PreOrderSettings in the database
        List<PreOrderSettings> preOrderSettingsList = preOrderSettingsRepository.findAll();
        assertThat(preOrderSettingsList).hasSize(databaseSizeBeforeCreate + 1);
        PreOrderSettings testPreOrderSettings = preOrderSettingsList.get(preOrderSettingsList.size() - 1);
        assertThat(testPreOrderSettings.isIsPreOrderAvailable()).isEqualTo(DEFAULT_IS_PRE_ORDER_AVAILABLE);
        assertThat(testPreOrderSettings.getFromTime()).isEqualTo(DEFAULT_FROM_TIME);
        assertThat(testPreOrderSettings.getToTime()).isEqualTo(DEFAULT_TO_TIME);

        // Validate the PreOrderSettings in Elasticsearch
        verify(mockPreOrderSettingsSearchRepository, times(1)).save(testPreOrderSettings);
    }

    @Test
    @Transactional
    public void createPreOrderSettingsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = preOrderSettingsRepository.findAll().size();

        // Create the PreOrderSettings with an existing ID
        preOrderSettings.setId(1L);
        PreOrderSettingsDTO preOrderSettingsDTO = preOrderSettingsMapper.toDto(preOrderSettings);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPreOrderSettingsMockMvc.perform(post("/api/pre-order-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preOrderSettingsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PreOrderSettings in the database
        List<PreOrderSettings> preOrderSettingsList = preOrderSettingsRepository.findAll();
        assertThat(preOrderSettingsList).hasSize(databaseSizeBeforeCreate);

        // Validate the PreOrderSettings in Elasticsearch
        verify(mockPreOrderSettingsSearchRepository, times(0)).save(preOrderSettings);
    }

    @Test
    @Transactional
    public void getAllPreOrderSettings() throws Exception {
        // Initialize the database
        preOrderSettingsRepository.saveAndFlush(preOrderSettings);

        // Get all the preOrderSettingsList
        restPreOrderSettingsMockMvc.perform(get("/api/pre-order-settings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(preOrderSettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].isPreOrderAvailable").value(hasItem(DEFAULT_IS_PRE_ORDER_AVAILABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].fromTime").value(hasItem(sameInstant(DEFAULT_FROM_TIME))))
            .andExpect(jsonPath("$.[*].toTime").value(hasItem(sameInstant(DEFAULT_TO_TIME))));
    }
    
    @Test
    @Transactional
    public void getPreOrderSettings() throws Exception {
        // Initialize the database
        preOrderSettingsRepository.saveAndFlush(preOrderSettings);

        // Get the preOrderSettings
        restPreOrderSettingsMockMvc.perform(get("/api/pre-order-settings/{id}", preOrderSettings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(preOrderSettings.getId().intValue()))
            .andExpect(jsonPath("$.isPreOrderAvailable").value(DEFAULT_IS_PRE_ORDER_AVAILABLE.booleanValue()))
            .andExpect(jsonPath("$.fromTime").value(sameInstant(DEFAULT_FROM_TIME)))
            .andExpect(jsonPath("$.toTime").value(sameInstant(DEFAULT_TO_TIME)));
    }

    @Test
    @Transactional
    public void getNonExistingPreOrderSettings() throws Exception {
        // Get the preOrderSettings
        restPreOrderSettingsMockMvc.perform(get("/api/pre-order-settings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePreOrderSettings() throws Exception {
        // Initialize the database
        preOrderSettingsRepository.saveAndFlush(preOrderSettings);

        int databaseSizeBeforeUpdate = preOrderSettingsRepository.findAll().size();

        // Update the preOrderSettings
        PreOrderSettings updatedPreOrderSettings = preOrderSettingsRepository.findById(preOrderSettings.getId()).get();
        // Disconnect from session so that the updates on updatedPreOrderSettings are not directly saved in db
        em.detach(updatedPreOrderSettings);
        updatedPreOrderSettings
            .isPreOrderAvailable(UPDATED_IS_PRE_ORDER_AVAILABLE)
            .fromTime(UPDATED_FROM_TIME)
            .toTime(UPDATED_TO_TIME);
        PreOrderSettingsDTO preOrderSettingsDTO = preOrderSettingsMapper.toDto(updatedPreOrderSettings);

        restPreOrderSettingsMockMvc.perform(put("/api/pre-order-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preOrderSettingsDTO)))
            .andExpect(status().isOk());

        // Validate the PreOrderSettings in the database
        List<PreOrderSettings> preOrderSettingsList = preOrderSettingsRepository.findAll();
        assertThat(preOrderSettingsList).hasSize(databaseSizeBeforeUpdate);
        PreOrderSettings testPreOrderSettings = preOrderSettingsList.get(preOrderSettingsList.size() - 1);
        assertThat(testPreOrderSettings.isIsPreOrderAvailable()).isEqualTo(UPDATED_IS_PRE_ORDER_AVAILABLE);
        assertThat(testPreOrderSettings.getFromTime()).isEqualTo(UPDATED_FROM_TIME);
        assertThat(testPreOrderSettings.getToTime()).isEqualTo(UPDATED_TO_TIME);

        // Validate the PreOrderSettings in Elasticsearch
        verify(mockPreOrderSettingsSearchRepository, times(1)).save(testPreOrderSettings);
    }

    @Test
    @Transactional
    public void updateNonExistingPreOrderSettings() throws Exception {
        int databaseSizeBeforeUpdate = preOrderSettingsRepository.findAll().size();

        // Create the PreOrderSettings
        PreOrderSettingsDTO preOrderSettingsDTO = preOrderSettingsMapper.toDto(preOrderSettings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPreOrderSettingsMockMvc.perform(put("/api/pre-order-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(preOrderSettingsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PreOrderSettings in the database
        List<PreOrderSettings> preOrderSettingsList = preOrderSettingsRepository.findAll();
        assertThat(preOrderSettingsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PreOrderSettings in Elasticsearch
        verify(mockPreOrderSettingsSearchRepository, times(0)).save(preOrderSettings);
    }

    @Test
    @Transactional
    public void deletePreOrderSettings() throws Exception {
        // Initialize the database
        preOrderSettingsRepository.saveAndFlush(preOrderSettings);

        int databaseSizeBeforeDelete = preOrderSettingsRepository.findAll().size();

        // Delete the preOrderSettings
        restPreOrderSettingsMockMvc.perform(delete("/api/pre-order-settings/{id}", preOrderSettings.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PreOrderSettings> preOrderSettingsList = preOrderSettingsRepository.findAll();
        assertThat(preOrderSettingsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PreOrderSettings in Elasticsearch
        verify(mockPreOrderSettingsSearchRepository, times(1)).deleteById(preOrderSettings.getId());
    }

    @Test
    @Transactional
    public void searchPreOrderSettings() throws Exception {
        // Initialize the database
        preOrderSettingsRepository.saveAndFlush(preOrderSettings);
        when(mockPreOrderSettingsSearchRepository.search(queryStringQuery("id:" + preOrderSettings.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(preOrderSettings), PageRequest.of(0, 1), 1));
        // Search the preOrderSettings
        restPreOrderSettingsMockMvc.perform(get("/api/_search/pre-order-settings?query=id:" + preOrderSettings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(preOrderSettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].isPreOrderAvailable").value(hasItem(DEFAULT_IS_PRE_ORDER_AVAILABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].fromTime").value(hasItem(sameInstant(DEFAULT_FROM_TIME))))
            .andExpect(jsonPath("$.[*].toTime").value(hasItem(sameInstant(DEFAULT_TO_TIME))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PreOrderSettings.class);
        PreOrderSettings preOrderSettings1 = new PreOrderSettings();
        preOrderSettings1.setId(1L);
        PreOrderSettings preOrderSettings2 = new PreOrderSettings();
        preOrderSettings2.setId(preOrderSettings1.getId());
        assertThat(preOrderSettings1).isEqualTo(preOrderSettings2);
        preOrderSettings2.setId(2L);
        assertThat(preOrderSettings1).isNotEqualTo(preOrderSettings2);
        preOrderSettings1.setId(null);
        assertThat(preOrderSettings1).isNotEqualTo(preOrderSettings2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PreOrderSettingsDTO.class);
        PreOrderSettingsDTO preOrderSettingsDTO1 = new PreOrderSettingsDTO();
        preOrderSettingsDTO1.setId(1L);
        PreOrderSettingsDTO preOrderSettingsDTO2 = new PreOrderSettingsDTO();
        assertThat(preOrderSettingsDTO1).isNotEqualTo(preOrderSettingsDTO2);
        preOrderSettingsDTO2.setId(preOrderSettingsDTO1.getId());
        assertThat(preOrderSettingsDTO1).isEqualTo(preOrderSettingsDTO2);
        preOrderSettingsDTO2.setId(2L);
        assertThat(preOrderSettingsDTO1).isNotEqualTo(preOrderSettingsDTO2);
        preOrderSettingsDTO1.setId(null);
        assertThat(preOrderSettingsDTO1).isNotEqualTo(preOrderSettingsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(preOrderSettingsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(preOrderSettingsMapper.fromId(null)).isNull();
    }
}
