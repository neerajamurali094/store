package com.diviso.graeshoppe.web.rest;

import com.diviso.graeshoppe.StoreApp;

import com.diviso.graeshoppe.domain.StoreSettings;
import com.diviso.graeshoppe.repository.StoreSettingsRepository;
import com.diviso.graeshoppe.repository.search.StoreSettingsSearchRepository;
import com.diviso.graeshoppe.service.StoreSettingsService;
import com.diviso.graeshoppe.service.dto.StoreSettingsDTO;
import com.diviso.graeshoppe.service.mapper.StoreSettingsMapper;
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
 * Test class for the StoreSettingsResource REST controller.
 *
 * @see StoreSettingsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StoreApp.class)
public class StoreSettingsResourceIntTest {

    private static final Double DEFAULT_DELIVERY_CHARGE = 1D;
    private static final Double UPDATED_DELIVERY_CHARGE = 2D;

    private static final Double DEFAULT_SERVICE_CHARGE = 1D;
    private static final Double UPDATED_SERVICE_CHARGE = 2D;

    private static final String DEFAULT_ORDER_ACCEPT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_ACCEPT_TYPE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_INVENTORY_REQUIRED = false;
    private static final Boolean UPDATED_IS_INVENTORY_REQUIRED = true;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Autowired
    private StoreSettingsRepository storeSettingsRepository;

    @Autowired
    private StoreSettingsMapper storeSettingsMapper;

    @Autowired
    private StoreSettingsService storeSettingsService;

    /**
     * This repository is mocked in the com.diviso.graeshoppe.repository.search test package.
     *
     * @see com.diviso.graeshoppe.repository.search.StoreSettingsSearchRepositoryMockConfiguration
     */
    @Autowired
    private StoreSettingsSearchRepository mockStoreSettingsSearchRepository;

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

    private MockMvc restStoreSettingsMockMvc;

    private StoreSettings storeSettings;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StoreSettingsResource storeSettingsResource = new StoreSettingsResource(storeSettingsService);
        this.restStoreSettingsMockMvc = MockMvcBuilders.standaloneSetup(storeSettingsResource)
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
    public static StoreSettings createEntity(EntityManager em) {
        StoreSettings storeSettings = new StoreSettings()
            .deliveryCharge(DEFAULT_DELIVERY_CHARGE)
            .serviceCharge(DEFAULT_SERVICE_CHARGE)
            .orderAcceptType(DEFAULT_ORDER_ACCEPT_TYPE)
            .isInventoryRequired(DEFAULT_IS_INVENTORY_REQUIRED)
            .isActive(DEFAULT_IS_ACTIVE);
        return storeSettings;
    }

    @Before
    public void initTest() {
        storeSettings = createEntity(em);
    }

    @Test
    @Transactional
    public void createStoreSettings() throws Exception {
        int databaseSizeBeforeCreate = storeSettingsRepository.findAll().size();

        // Create the StoreSettings
        StoreSettingsDTO storeSettingsDTO = storeSettingsMapper.toDto(storeSettings);
        restStoreSettingsMockMvc.perform(post("/api/store-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeSettingsDTO)))
            .andExpect(status().isCreated());

        // Validate the StoreSettings in the database
        List<StoreSettings> storeSettingsList = storeSettingsRepository.findAll();
        assertThat(storeSettingsList).hasSize(databaseSizeBeforeCreate + 1);
        StoreSettings testStoreSettings = storeSettingsList.get(storeSettingsList.size() - 1);
        assertThat(testStoreSettings.getDeliveryCharge()).isEqualTo(DEFAULT_DELIVERY_CHARGE);
        assertThat(testStoreSettings.getServiceCharge()).isEqualTo(DEFAULT_SERVICE_CHARGE);
        assertThat(testStoreSettings.getOrderAcceptType()).isEqualTo(DEFAULT_ORDER_ACCEPT_TYPE);
        assertThat(testStoreSettings.isIsInventoryRequired()).isEqualTo(DEFAULT_IS_INVENTORY_REQUIRED);
        assertThat(testStoreSettings.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);

        // Validate the StoreSettings in Elasticsearch
        verify(mockStoreSettingsSearchRepository, times(1)).save(testStoreSettings);
    }

    @Test
    @Transactional
    public void createStoreSettingsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = storeSettingsRepository.findAll().size();

        // Create the StoreSettings with an existing ID
        storeSettings.setId(1L);
        StoreSettingsDTO storeSettingsDTO = storeSettingsMapper.toDto(storeSettings);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStoreSettingsMockMvc.perform(post("/api/store-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeSettingsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StoreSettings in the database
        List<StoreSettings> storeSettingsList = storeSettingsRepository.findAll();
        assertThat(storeSettingsList).hasSize(databaseSizeBeforeCreate);

        // Validate the StoreSettings in Elasticsearch
        verify(mockStoreSettingsSearchRepository, times(0)).save(storeSettings);
    }

    @Test
    @Transactional
    public void getAllStoreSettings() throws Exception {
        // Initialize the database
        storeSettingsRepository.saveAndFlush(storeSettings);

        // Get all the storeSettingsList
        restStoreSettingsMockMvc.perform(get("/api/store-settings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storeSettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].deliveryCharge").value(hasItem(DEFAULT_DELIVERY_CHARGE.doubleValue())))
            .andExpect(jsonPath("$.[*].serviceCharge").value(hasItem(DEFAULT_SERVICE_CHARGE.doubleValue())))
            .andExpect(jsonPath("$.[*].orderAcceptType").value(hasItem(DEFAULT_ORDER_ACCEPT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].isInventoryRequired").value(hasItem(DEFAULT_IS_INVENTORY_REQUIRED.booleanValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getStoreSettings() throws Exception {
        // Initialize the database
        storeSettingsRepository.saveAndFlush(storeSettings);

        // Get the storeSettings
        restStoreSettingsMockMvc.perform(get("/api/store-settings/{id}", storeSettings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(storeSettings.getId().intValue()))
            .andExpect(jsonPath("$.deliveryCharge").value(DEFAULT_DELIVERY_CHARGE.doubleValue()))
            .andExpect(jsonPath("$.serviceCharge").value(DEFAULT_SERVICE_CHARGE.doubleValue()))
            .andExpect(jsonPath("$.orderAcceptType").value(DEFAULT_ORDER_ACCEPT_TYPE.toString()))
            .andExpect(jsonPath("$.isInventoryRequired").value(DEFAULT_IS_INVENTORY_REQUIRED.booleanValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingStoreSettings() throws Exception {
        // Get the storeSettings
        restStoreSettingsMockMvc.perform(get("/api/store-settings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStoreSettings() throws Exception {
        // Initialize the database
        storeSettingsRepository.saveAndFlush(storeSettings);

        int databaseSizeBeforeUpdate = storeSettingsRepository.findAll().size();

        // Update the storeSettings
        StoreSettings updatedStoreSettings = storeSettingsRepository.findById(storeSettings.getId()).get();
        // Disconnect from session so that the updates on updatedStoreSettings are not directly saved in db
        em.detach(updatedStoreSettings);
        updatedStoreSettings
            .deliveryCharge(UPDATED_DELIVERY_CHARGE)
            .serviceCharge(UPDATED_SERVICE_CHARGE)
            .orderAcceptType(UPDATED_ORDER_ACCEPT_TYPE)
            .isInventoryRequired(UPDATED_IS_INVENTORY_REQUIRED)
            .isActive(UPDATED_IS_ACTIVE);
        StoreSettingsDTO storeSettingsDTO = storeSettingsMapper.toDto(updatedStoreSettings);

        restStoreSettingsMockMvc.perform(put("/api/store-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeSettingsDTO)))
            .andExpect(status().isOk());

        // Validate the StoreSettings in the database
        List<StoreSettings> storeSettingsList = storeSettingsRepository.findAll();
        assertThat(storeSettingsList).hasSize(databaseSizeBeforeUpdate);
        StoreSettings testStoreSettings = storeSettingsList.get(storeSettingsList.size() - 1);
        assertThat(testStoreSettings.getDeliveryCharge()).isEqualTo(UPDATED_DELIVERY_CHARGE);
        assertThat(testStoreSettings.getServiceCharge()).isEqualTo(UPDATED_SERVICE_CHARGE);
        assertThat(testStoreSettings.getOrderAcceptType()).isEqualTo(UPDATED_ORDER_ACCEPT_TYPE);
        assertThat(testStoreSettings.isIsInventoryRequired()).isEqualTo(UPDATED_IS_INVENTORY_REQUIRED);
        assertThat(testStoreSettings.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);

        // Validate the StoreSettings in Elasticsearch
        verify(mockStoreSettingsSearchRepository, times(1)).save(testStoreSettings);
    }

    @Test
    @Transactional
    public void updateNonExistingStoreSettings() throws Exception {
        int databaseSizeBeforeUpdate = storeSettingsRepository.findAll().size();

        // Create the StoreSettings
        StoreSettingsDTO storeSettingsDTO = storeSettingsMapper.toDto(storeSettings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStoreSettingsMockMvc.perform(put("/api/store-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeSettingsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StoreSettings in the database
        List<StoreSettings> storeSettingsList = storeSettingsRepository.findAll();
        assertThat(storeSettingsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the StoreSettings in Elasticsearch
        verify(mockStoreSettingsSearchRepository, times(0)).save(storeSettings);
    }

    @Test
    @Transactional
    public void deleteStoreSettings() throws Exception {
        // Initialize the database
        storeSettingsRepository.saveAndFlush(storeSettings);

        int databaseSizeBeforeDelete = storeSettingsRepository.findAll().size();

        // Delete the storeSettings
        restStoreSettingsMockMvc.perform(delete("/api/store-settings/{id}", storeSettings.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StoreSettings> storeSettingsList = storeSettingsRepository.findAll();
        assertThat(storeSettingsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the StoreSettings in Elasticsearch
        verify(mockStoreSettingsSearchRepository, times(1)).deleteById(storeSettings.getId());
    }

    @Test
    @Transactional
    public void searchStoreSettings() throws Exception {
        // Initialize the database
        storeSettingsRepository.saveAndFlush(storeSettings);
        when(mockStoreSettingsSearchRepository.search(queryStringQuery("id:" + storeSettings.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(storeSettings), PageRequest.of(0, 1), 1));
        // Search the storeSettings
        restStoreSettingsMockMvc.perform(get("/api/_search/store-settings?query=id:" + storeSettings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storeSettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].deliveryCharge").value(hasItem(DEFAULT_DELIVERY_CHARGE.doubleValue())))
            .andExpect(jsonPath("$.[*].serviceCharge").value(hasItem(DEFAULT_SERVICE_CHARGE.doubleValue())))
            .andExpect(jsonPath("$.[*].orderAcceptType").value(hasItem(DEFAULT_ORDER_ACCEPT_TYPE)))
            .andExpect(jsonPath("$.[*].isInventoryRequired").value(hasItem(DEFAULT_IS_INVENTORY_REQUIRED.booleanValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StoreSettings.class);
        StoreSettings storeSettings1 = new StoreSettings();
        storeSettings1.setId(1L);
        StoreSettings storeSettings2 = new StoreSettings();
        storeSettings2.setId(storeSettings1.getId());
        assertThat(storeSettings1).isEqualTo(storeSettings2);
        storeSettings2.setId(2L);
        assertThat(storeSettings1).isNotEqualTo(storeSettings2);
        storeSettings1.setId(null);
        assertThat(storeSettings1).isNotEqualTo(storeSettings2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StoreSettingsDTO.class);
        StoreSettingsDTO storeSettingsDTO1 = new StoreSettingsDTO();
        storeSettingsDTO1.setId(1L);
        StoreSettingsDTO storeSettingsDTO2 = new StoreSettingsDTO();
        assertThat(storeSettingsDTO1).isNotEqualTo(storeSettingsDTO2);
        storeSettingsDTO2.setId(storeSettingsDTO1.getId());
        assertThat(storeSettingsDTO1).isEqualTo(storeSettingsDTO2);
        storeSettingsDTO2.setId(2L);
        assertThat(storeSettingsDTO1).isNotEqualTo(storeSettingsDTO2);
        storeSettingsDTO1.setId(null);
        assertThat(storeSettingsDTO1).isNotEqualTo(storeSettingsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(storeSettingsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(storeSettingsMapper.fromId(null)).isNull();
    }
}
