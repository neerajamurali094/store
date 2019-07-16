package com.diviso.graeshoppe.web.rest;

import com.diviso.graeshoppe.StoreApp;

import com.diviso.graeshoppe.domain.StoreAddress;
import com.diviso.graeshoppe.repository.StoreAddressRepository;
import com.diviso.graeshoppe.repository.search.StoreAddressSearchRepository;
import com.diviso.graeshoppe.service.StoreAddressService;
import com.diviso.graeshoppe.service.dto.StoreAddressDTO;
import com.diviso.graeshoppe.service.mapper.StoreAddressMapper;
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
 * Test class for the StoreAddressResource REST controller.
 *
 * @see StoreAddressResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StoreApp.class)
public class StoreAddressResourceIntTest {

    private static final String DEFAULT_CUSTOMER_ID = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_ID = "BBBBBBBBBB";

    private static final Long DEFAULT_PINCODE = 1L;
    private static final Long UPDATED_PINCODE = 2L;

    private static final String DEFAULT_HOUSE_NO_OR_BUILDING_NAME = "AAAAAAAAAA";
    private static final String UPDATED_HOUSE_NO_OR_BUILDING_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ROAD_NAME_AREA_OR_STREET = "AAAAAAAAAA";
    private static final String UPDATED_ROAD_NAME_AREA_OR_STREET = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_LANDMARK = "AAAAAAAAAA";
    private static final String UPDATED_LANDMARK = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_PHONE = 1L;
    private static final Long UPDATED_PHONE = 2L;

    private static final Long DEFAULT_ALTERNATE_PHONE = 1L;
    private static final Long UPDATED_ALTERNATE_PHONE = 2L;

    private static final String DEFAULT_ADDRESS_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_TYPE = "BBBBBBBBBB";

    @Autowired
    private StoreAddressRepository storeAddressRepository;

    @Autowired
    private StoreAddressMapper storeAddressMapper;

    @Autowired
    private StoreAddressService storeAddressService;

    /**
     * This repository is mocked in the com.diviso.graeshoppe.repository.search test package.
     *
     * @see com.diviso.graeshoppe.repository.search.StoreAddressSearchRepositoryMockConfiguration
     */
    @Autowired
    private StoreAddressSearchRepository mockStoreAddressSearchRepository;

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

    private MockMvc restStoreAddressMockMvc;

    private StoreAddress storeAddress;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StoreAddressResource storeAddressResource = new StoreAddressResource(storeAddressService);
        this.restStoreAddressMockMvc = MockMvcBuilders.standaloneSetup(storeAddressResource)
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
    public static StoreAddress createEntity(EntityManager em) {
        StoreAddress storeAddress = new StoreAddress()
            .customerId(DEFAULT_CUSTOMER_ID)
            .pincode(DEFAULT_PINCODE)
            .houseNoOrBuildingName(DEFAULT_HOUSE_NO_OR_BUILDING_NAME)
            .roadNameAreaOrStreet(DEFAULT_ROAD_NAME_AREA_OR_STREET)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .landmark(DEFAULT_LANDMARK)
            .name(DEFAULT_NAME)
            .phone(DEFAULT_PHONE)
            .alternatePhone(DEFAULT_ALTERNATE_PHONE)
            .addressType(DEFAULT_ADDRESS_TYPE);
        return storeAddress;
    }

    @Before
    public void initTest() {
        storeAddress = createEntity(em);
    }

    @Test
    @Transactional
    public void createStoreAddress() throws Exception {
        int databaseSizeBeforeCreate = storeAddressRepository.findAll().size();

        // Create the StoreAddress
        StoreAddressDTO storeAddressDTO = storeAddressMapper.toDto(storeAddress);
        restStoreAddressMockMvc.perform(post("/api/store-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeAddressDTO)))
            .andExpect(status().isCreated());

        // Validate the StoreAddress in the database
        List<StoreAddress> storeAddressList = storeAddressRepository.findAll();
        assertThat(storeAddressList).hasSize(databaseSizeBeforeCreate + 1);
        StoreAddress testStoreAddress = storeAddressList.get(storeAddressList.size() - 1);
        assertThat(testStoreAddress.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testStoreAddress.getPincode()).isEqualTo(DEFAULT_PINCODE);
        assertThat(testStoreAddress.getHouseNoOrBuildingName()).isEqualTo(DEFAULT_HOUSE_NO_OR_BUILDING_NAME);
        assertThat(testStoreAddress.getRoadNameAreaOrStreet()).isEqualTo(DEFAULT_ROAD_NAME_AREA_OR_STREET);
        assertThat(testStoreAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testStoreAddress.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testStoreAddress.getLandmark()).isEqualTo(DEFAULT_LANDMARK);
        assertThat(testStoreAddress.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStoreAddress.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testStoreAddress.getAlternatePhone()).isEqualTo(DEFAULT_ALTERNATE_PHONE);
        assertThat(testStoreAddress.getAddressType()).isEqualTo(DEFAULT_ADDRESS_TYPE);

        // Validate the StoreAddress in Elasticsearch
        verify(mockStoreAddressSearchRepository, times(1)).save(testStoreAddress);
    }

    @Test
    @Transactional
    public void createStoreAddressWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = storeAddressRepository.findAll().size();

        // Create the StoreAddress with an existing ID
        storeAddress.setId(1L);
        StoreAddressDTO storeAddressDTO = storeAddressMapper.toDto(storeAddress);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStoreAddressMockMvc.perform(post("/api/store-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeAddressDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StoreAddress in the database
        List<StoreAddress> storeAddressList = storeAddressRepository.findAll();
        assertThat(storeAddressList).hasSize(databaseSizeBeforeCreate);

        // Validate the StoreAddress in Elasticsearch
        verify(mockStoreAddressSearchRepository, times(0)).save(storeAddress);
    }

    @Test
    @Transactional
    public void getAllStoreAddresses() throws Exception {
        // Initialize the database
        storeAddressRepository.saveAndFlush(storeAddress);

        // Get all the storeAddressList
        restStoreAddressMockMvc.perform(get("/api/store-addresses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storeAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID.toString())))
            .andExpect(jsonPath("$.[*].pincode").value(hasItem(DEFAULT_PINCODE.intValue())))
            .andExpect(jsonPath("$.[*].houseNoOrBuildingName").value(hasItem(DEFAULT_HOUSE_NO_OR_BUILDING_NAME.toString())))
            .andExpect(jsonPath("$.[*].roadNameAreaOrStreet").value(hasItem(DEFAULT_ROAD_NAME_AREA_OR_STREET.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].landmark").value(hasItem(DEFAULT_LANDMARK.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.intValue())))
            .andExpect(jsonPath("$.[*].alternatePhone").value(hasItem(DEFAULT_ALTERNATE_PHONE.intValue())))
            .andExpect(jsonPath("$.[*].addressType").value(hasItem(DEFAULT_ADDRESS_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getStoreAddress() throws Exception {
        // Initialize the database
        storeAddressRepository.saveAndFlush(storeAddress);

        // Get the storeAddress
        restStoreAddressMockMvc.perform(get("/api/store-addresses/{id}", storeAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(storeAddress.getId().intValue()))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID.toString()))
            .andExpect(jsonPath("$.pincode").value(DEFAULT_PINCODE.intValue()))
            .andExpect(jsonPath("$.houseNoOrBuildingName").value(DEFAULT_HOUSE_NO_OR_BUILDING_NAME.toString()))
            .andExpect(jsonPath("$.roadNameAreaOrStreet").value(DEFAULT_ROAD_NAME_AREA_OR_STREET.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.landmark").value(DEFAULT_LANDMARK.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.intValue()))
            .andExpect(jsonPath("$.alternatePhone").value(DEFAULT_ALTERNATE_PHONE.intValue()))
            .andExpect(jsonPath("$.addressType").value(DEFAULT_ADDRESS_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStoreAddress() throws Exception {
        // Get the storeAddress
        restStoreAddressMockMvc.perform(get("/api/store-addresses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStoreAddress() throws Exception {
        // Initialize the database
        storeAddressRepository.saveAndFlush(storeAddress);

        int databaseSizeBeforeUpdate = storeAddressRepository.findAll().size();

        // Update the storeAddress
        StoreAddress updatedStoreAddress = storeAddressRepository.findById(storeAddress.getId()).get();
        // Disconnect from session so that the updates on updatedStoreAddress are not directly saved in db
        em.detach(updatedStoreAddress);
        updatedStoreAddress
            .customerId(UPDATED_CUSTOMER_ID)
            .pincode(UPDATED_PINCODE)
            .houseNoOrBuildingName(UPDATED_HOUSE_NO_OR_BUILDING_NAME)
            .roadNameAreaOrStreet(UPDATED_ROAD_NAME_AREA_OR_STREET)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .landmark(UPDATED_LANDMARK)
            .name(UPDATED_NAME)
            .phone(UPDATED_PHONE)
            .alternatePhone(UPDATED_ALTERNATE_PHONE)
            .addressType(UPDATED_ADDRESS_TYPE);
        StoreAddressDTO storeAddressDTO = storeAddressMapper.toDto(updatedStoreAddress);

        restStoreAddressMockMvc.perform(put("/api/store-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeAddressDTO)))
            .andExpect(status().isOk());

        // Validate the StoreAddress in the database
        List<StoreAddress> storeAddressList = storeAddressRepository.findAll();
        assertThat(storeAddressList).hasSize(databaseSizeBeforeUpdate);
        StoreAddress testStoreAddress = storeAddressList.get(storeAddressList.size() - 1);
        assertThat(testStoreAddress.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testStoreAddress.getPincode()).isEqualTo(UPDATED_PINCODE);
        assertThat(testStoreAddress.getHouseNoOrBuildingName()).isEqualTo(UPDATED_HOUSE_NO_OR_BUILDING_NAME);
        assertThat(testStoreAddress.getRoadNameAreaOrStreet()).isEqualTo(UPDATED_ROAD_NAME_AREA_OR_STREET);
        assertThat(testStoreAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testStoreAddress.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testStoreAddress.getLandmark()).isEqualTo(UPDATED_LANDMARK);
        assertThat(testStoreAddress.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStoreAddress.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testStoreAddress.getAlternatePhone()).isEqualTo(UPDATED_ALTERNATE_PHONE);
        assertThat(testStoreAddress.getAddressType()).isEqualTo(UPDATED_ADDRESS_TYPE);

        // Validate the StoreAddress in Elasticsearch
        verify(mockStoreAddressSearchRepository, times(1)).save(testStoreAddress);
    }

    @Test
    @Transactional
    public void updateNonExistingStoreAddress() throws Exception {
        int databaseSizeBeforeUpdate = storeAddressRepository.findAll().size();

        // Create the StoreAddress
        StoreAddressDTO storeAddressDTO = storeAddressMapper.toDto(storeAddress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStoreAddressMockMvc.perform(put("/api/store-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeAddressDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StoreAddress in the database
        List<StoreAddress> storeAddressList = storeAddressRepository.findAll();
        assertThat(storeAddressList).hasSize(databaseSizeBeforeUpdate);

        // Validate the StoreAddress in Elasticsearch
        verify(mockStoreAddressSearchRepository, times(0)).save(storeAddress);
    }

    @Test
    @Transactional
    public void deleteStoreAddress() throws Exception {
        // Initialize the database
        storeAddressRepository.saveAndFlush(storeAddress);

        int databaseSizeBeforeDelete = storeAddressRepository.findAll().size();

        // Delete the storeAddress
        restStoreAddressMockMvc.perform(delete("/api/store-addresses/{id}", storeAddress.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StoreAddress> storeAddressList = storeAddressRepository.findAll();
        assertThat(storeAddressList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the StoreAddress in Elasticsearch
        verify(mockStoreAddressSearchRepository, times(1)).deleteById(storeAddress.getId());
    }

    @Test
    @Transactional
    public void searchStoreAddress() throws Exception {
        // Initialize the database
        storeAddressRepository.saveAndFlush(storeAddress);
        when(mockStoreAddressSearchRepository.search(queryStringQuery("id:" + storeAddress.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(storeAddress), PageRequest.of(0, 1), 1));
        // Search the storeAddress
        restStoreAddressMockMvc.perform(get("/api/_search/store-addresses?query=id:" + storeAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storeAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].pincode").value(hasItem(DEFAULT_PINCODE.intValue())))
            .andExpect(jsonPath("$.[*].houseNoOrBuildingName").value(hasItem(DEFAULT_HOUSE_NO_OR_BUILDING_NAME)))
            .andExpect(jsonPath("$.[*].roadNameAreaOrStreet").value(hasItem(DEFAULT_ROAD_NAME_AREA_OR_STREET)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].landmark").value(hasItem(DEFAULT_LANDMARK)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.intValue())))
            .andExpect(jsonPath("$.[*].alternatePhone").value(hasItem(DEFAULT_ALTERNATE_PHONE.intValue())))
            .andExpect(jsonPath("$.[*].addressType").value(hasItem(DEFAULT_ADDRESS_TYPE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StoreAddress.class);
        StoreAddress storeAddress1 = new StoreAddress();
        storeAddress1.setId(1L);
        StoreAddress storeAddress2 = new StoreAddress();
        storeAddress2.setId(storeAddress1.getId());
        assertThat(storeAddress1).isEqualTo(storeAddress2);
        storeAddress2.setId(2L);
        assertThat(storeAddress1).isNotEqualTo(storeAddress2);
        storeAddress1.setId(null);
        assertThat(storeAddress1).isNotEqualTo(storeAddress2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StoreAddressDTO.class);
        StoreAddressDTO storeAddressDTO1 = new StoreAddressDTO();
        storeAddressDTO1.setId(1L);
        StoreAddressDTO storeAddressDTO2 = new StoreAddressDTO();
        assertThat(storeAddressDTO1).isNotEqualTo(storeAddressDTO2);
        storeAddressDTO2.setId(storeAddressDTO1.getId());
        assertThat(storeAddressDTO1).isEqualTo(storeAddressDTO2);
        storeAddressDTO2.setId(2L);
        assertThat(storeAddressDTO1).isNotEqualTo(storeAddressDTO2);
        storeAddressDTO1.setId(null);
        assertThat(storeAddressDTO1).isNotEqualTo(storeAddressDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(storeAddressMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(storeAddressMapper.fromId(null)).isNull();
    }
}
