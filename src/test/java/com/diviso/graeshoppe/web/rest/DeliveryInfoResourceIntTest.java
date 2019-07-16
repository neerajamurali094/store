package com.diviso.graeshoppe.web.rest;

import com.diviso.graeshoppe.StoreApp;

import com.diviso.graeshoppe.domain.DeliveryInfo;
import com.diviso.graeshoppe.repository.DeliveryInfoRepository;
import com.diviso.graeshoppe.repository.search.DeliveryInfoSearchRepository;
import com.diviso.graeshoppe.service.DeliveryInfoService;
import com.diviso.graeshoppe.service.dto.DeliveryInfoDTO;
import com.diviso.graeshoppe.service.mapper.DeliveryInfoMapper;
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
import java.time.temporal.ChronoUnit;
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
 * Test class for the DeliveryInfoResource REST controller.
 *
 * @see DeliveryInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StoreApp.class)
public class DeliveryInfoResourceIntTest {

    private static final Instant DEFAULT_STARTING_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_STARTING_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private DeliveryInfoRepository deliveryInfoRepository;

    @Autowired
    private DeliveryInfoMapper deliveryInfoMapper;

    @Autowired
    private DeliveryInfoService deliveryInfoService;

    /**
     * This repository is mocked in the com.diviso.graeshoppe.repository.search test package.
     *
     * @see com.diviso.graeshoppe.repository.search.DeliveryInfoSearchRepositoryMockConfiguration
     */
    @Autowired
    private DeliveryInfoSearchRepository mockDeliveryInfoSearchRepository;

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

    private MockMvc restDeliveryInfoMockMvc;

    private DeliveryInfo deliveryInfo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DeliveryInfoResource deliveryInfoResource = new DeliveryInfoResource(deliveryInfoService);
        this.restDeliveryInfoMockMvc = MockMvcBuilders.standaloneSetup(deliveryInfoResource)
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
    public static DeliveryInfo createEntity(EntityManager em) {
        DeliveryInfo deliveryInfo = new DeliveryInfo()
            .startingTime(DEFAULT_STARTING_TIME)
            .endTime(DEFAULT_END_TIME);
        return deliveryInfo;
    }

    @Before
    public void initTest() {
        deliveryInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createDeliveryInfo() throws Exception {
        int databaseSizeBeforeCreate = deliveryInfoRepository.findAll().size();

        // Create the DeliveryInfo
        DeliveryInfoDTO deliveryInfoDTO = deliveryInfoMapper.toDto(deliveryInfo);
        restDeliveryInfoMockMvc.perform(post("/api/delivery-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deliveryInfoDTO)))
            .andExpect(status().isCreated());

        // Validate the DeliveryInfo in the database
        List<DeliveryInfo> deliveryInfoList = deliveryInfoRepository.findAll();
        assertThat(deliveryInfoList).hasSize(databaseSizeBeforeCreate + 1);
        DeliveryInfo testDeliveryInfo = deliveryInfoList.get(deliveryInfoList.size() - 1);
        assertThat(testDeliveryInfo.getStartingTime()).isEqualTo(DEFAULT_STARTING_TIME);
        assertThat(testDeliveryInfo.getEndTime()).isEqualTo(DEFAULT_END_TIME);

        // Validate the DeliveryInfo in Elasticsearch
        verify(mockDeliveryInfoSearchRepository, times(1)).save(testDeliveryInfo);
    }

    @Test
    @Transactional
    public void createDeliveryInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = deliveryInfoRepository.findAll().size();

        // Create the DeliveryInfo with an existing ID
        deliveryInfo.setId(1L);
        DeliveryInfoDTO deliveryInfoDTO = deliveryInfoMapper.toDto(deliveryInfo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeliveryInfoMockMvc.perform(post("/api/delivery-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deliveryInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DeliveryInfo in the database
        List<DeliveryInfo> deliveryInfoList = deliveryInfoRepository.findAll();
        assertThat(deliveryInfoList).hasSize(databaseSizeBeforeCreate);

        // Validate the DeliveryInfo in Elasticsearch
        verify(mockDeliveryInfoSearchRepository, times(0)).save(deliveryInfo);
    }

    @Test
    @Transactional
    public void getAllDeliveryInfos() throws Exception {
        // Initialize the database
        deliveryInfoRepository.saveAndFlush(deliveryInfo);

        // Get all the deliveryInfoList
        restDeliveryInfoMockMvc.perform(get("/api/delivery-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deliveryInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].startingTime").value(hasItem(DEFAULT_STARTING_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())));
    }
    
    @Test
    @Transactional
    public void getDeliveryInfo() throws Exception {
        // Initialize the database
        deliveryInfoRepository.saveAndFlush(deliveryInfo);

        // Get the deliveryInfo
        restDeliveryInfoMockMvc.perform(get("/api/delivery-infos/{id}", deliveryInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(deliveryInfo.getId().intValue()))
            .andExpect(jsonPath("$.startingTime").value(DEFAULT_STARTING_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDeliveryInfo() throws Exception {
        // Get the deliveryInfo
        restDeliveryInfoMockMvc.perform(get("/api/delivery-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeliveryInfo() throws Exception {
        // Initialize the database
        deliveryInfoRepository.saveAndFlush(deliveryInfo);

        int databaseSizeBeforeUpdate = deliveryInfoRepository.findAll().size();

        // Update the deliveryInfo
        DeliveryInfo updatedDeliveryInfo = deliveryInfoRepository.findById(deliveryInfo.getId()).get();
        // Disconnect from session so that the updates on updatedDeliveryInfo are not directly saved in db
        em.detach(updatedDeliveryInfo);
        updatedDeliveryInfo
            .startingTime(UPDATED_STARTING_TIME)
            .endTime(UPDATED_END_TIME);
        DeliveryInfoDTO deliveryInfoDTO = deliveryInfoMapper.toDto(updatedDeliveryInfo);

        restDeliveryInfoMockMvc.perform(put("/api/delivery-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deliveryInfoDTO)))
            .andExpect(status().isOk());

        // Validate the DeliveryInfo in the database
        List<DeliveryInfo> deliveryInfoList = deliveryInfoRepository.findAll();
        assertThat(deliveryInfoList).hasSize(databaseSizeBeforeUpdate);
        DeliveryInfo testDeliveryInfo = deliveryInfoList.get(deliveryInfoList.size() - 1);
        assertThat(testDeliveryInfo.getStartingTime()).isEqualTo(UPDATED_STARTING_TIME);
        assertThat(testDeliveryInfo.getEndTime()).isEqualTo(UPDATED_END_TIME);

        // Validate the DeliveryInfo in Elasticsearch
        verify(mockDeliveryInfoSearchRepository, times(1)).save(testDeliveryInfo);
    }

    @Test
    @Transactional
    public void updateNonExistingDeliveryInfo() throws Exception {
        int databaseSizeBeforeUpdate = deliveryInfoRepository.findAll().size();

        // Create the DeliveryInfo
        DeliveryInfoDTO deliveryInfoDTO = deliveryInfoMapper.toDto(deliveryInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliveryInfoMockMvc.perform(put("/api/delivery-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deliveryInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DeliveryInfo in the database
        List<DeliveryInfo> deliveryInfoList = deliveryInfoRepository.findAll();
        assertThat(deliveryInfoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DeliveryInfo in Elasticsearch
        verify(mockDeliveryInfoSearchRepository, times(0)).save(deliveryInfo);
    }

    @Test
    @Transactional
    public void deleteDeliveryInfo() throws Exception {
        // Initialize the database
        deliveryInfoRepository.saveAndFlush(deliveryInfo);

        int databaseSizeBeforeDelete = deliveryInfoRepository.findAll().size();

        // Delete the deliveryInfo
        restDeliveryInfoMockMvc.perform(delete("/api/delivery-infos/{id}", deliveryInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DeliveryInfo> deliveryInfoList = deliveryInfoRepository.findAll();
        assertThat(deliveryInfoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DeliveryInfo in Elasticsearch
        verify(mockDeliveryInfoSearchRepository, times(1)).deleteById(deliveryInfo.getId());
    }

    @Test
    @Transactional
    public void searchDeliveryInfo() throws Exception {
        // Initialize the database
        deliveryInfoRepository.saveAndFlush(deliveryInfo);
        when(mockDeliveryInfoSearchRepository.search(queryStringQuery("id:" + deliveryInfo.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(deliveryInfo), PageRequest.of(0, 1), 1));
        // Search the deliveryInfo
        restDeliveryInfoMockMvc.perform(get("/api/_search/delivery-infos?query=id:" + deliveryInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deliveryInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].startingTime").value(hasItem(DEFAULT_STARTING_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeliveryInfo.class);
        DeliveryInfo deliveryInfo1 = new DeliveryInfo();
        deliveryInfo1.setId(1L);
        DeliveryInfo deliveryInfo2 = new DeliveryInfo();
        deliveryInfo2.setId(deliveryInfo1.getId());
        assertThat(deliveryInfo1).isEqualTo(deliveryInfo2);
        deliveryInfo2.setId(2L);
        assertThat(deliveryInfo1).isNotEqualTo(deliveryInfo2);
        deliveryInfo1.setId(null);
        assertThat(deliveryInfo1).isNotEqualTo(deliveryInfo2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeliveryInfoDTO.class);
        DeliveryInfoDTO deliveryInfoDTO1 = new DeliveryInfoDTO();
        deliveryInfoDTO1.setId(1L);
        DeliveryInfoDTO deliveryInfoDTO2 = new DeliveryInfoDTO();
        assertThat(deliveryInfoDTO1).isNotEqualTo(deliveryInfoDTO2);
        deliveryInfoDTO2.setId(deliveryInfoDTO1.getId());
        assertThat(deliveryInfoDTO1).isEqualTo(deliveryInfoDTO2);
        deliveryInfoDTO2.setId(2L);
        assertThat(deliveryInfoDTO1).isNotEqualTo(deliveryInfoDTO2);
        deliveryInfoDTO1.setId(null);
        assertThat(deliveryInfoDTO1).isNotEqualTo(deliveryInfoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(deliveryInfoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(deliveryInfoMapper.fromId(null)).isNull();
    }
}
