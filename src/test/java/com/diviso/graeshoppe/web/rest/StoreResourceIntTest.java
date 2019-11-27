package com.diviso.graeshoppe.web.rest;

import com.diviso.graeshoppe.StoreApp;

import com.diviso.graeshoppe.domain.Store;
import com.diviso.graeshoppe.repository.StoreRepository;
import com.diviso.graeshoppe.repository.search.StoreSearchRepository;
import com.diviso.graeshoppe.service.StoreService;
import com.diviso.graeshoppe.service.dto.StoreDTO;
import com.diviso.graeshoppe.service.mapper.StoreMapper;
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
import org.springframework.util.Base64Utils;
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
 * Test class for the StoreResource REST controller.
 *
 * @see StoreResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StoreApp.class)
public class StoreResourceIntTest {

    private static final String DEFAULT_REG_NO = "AAAAAAAAAA";
    private static final String UPDATED_REG_NO = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final Double DEFAULT_TOTAL_RATING = 1D;
    private static final Double UPDATED_TOTAL_RATING = 2D;

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_CONTACT_NO = 1L;
    private static final Long UPDATED_CONTACT_NO = 2L;

    private static final Instant DEFAULT_OPENING_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_OPENING_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Instant DEFAULT_CLOSING_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CLOSING_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_INFO = "AAAAAAAAAA";
    private static final String UPDATED_INFO = "BBBBBBBBBB";

    private static final Double DEFAULT_MIN_AMOUNT = 1D;
    private static final Double UPDATED_MIN_AMOUNT = 2D;

    private static final Instant DEFAULT_MAX_DELIVERY_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MAX_DELIVERY_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_STORE_UNIQUE_ID = "AAAAAAAAAA";
    private static final String UPDATED_STORE_UNIQUE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_LINK = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_LINK = "BBBBBBBBBB";

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreMapper storeMapper;

    @Autowired
    private StoreService storeService;

    /**
     * This repository is mocked in the com.diviso.graeshoppe.repository.search test package.
     *
     * @see com.diviso.graeshoppe.repository.search.StoreSearchRepositoryMockConfiguration
     */
    @Autowired
    private StoreSearchRepository mockStoreSearchRepository;

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

    private MockMvc restStoreMockMvc;

    private Store store;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StoreResource storeResource = new StoreResource(storeService);
        this.restStoreMockMvc = MockMvcBuilders.standaloneSetup(storeResource)
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
    public static Store createEntity(EntityManager em) {
        Store store = new Store()
            .regNo(DEFAULT_REG_NO)
            .name(DEFAULT_NAME)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .totalRating(DEFAULT_TOTAL_RATING)
            .location(DEFAULT_LOCATION)
            .locationName(DEFAULT_LOCATION_NAME)
            .contactNo(DEFAULT_CONTACT_NO)
            .openingTime(DEFAULT_OPENING_TIME)
            .email(DEFAULT_EMAIL)
            .closingTime(DEFAULT_CLOSING_TIME)
            .info(DEFAULT_INFO)
            .minAmount(DEFAULT_MIN_AMOUNT)
            .maxDeliveryTime(DEFAULT_MAX_DELIVERY_TIME)
            .storeUniqueId(DEFAULT_STORE_UNIQUE_ID)
            .imageLink(DEFAULT_IMAGE_LINK);
        return store;
    }

    @Before
    public void initTest() {
        store = createEntity(em);
    }

    @Test
    @Transactional
    public void createStore() throws Exception {
        int databaseSizeBeforeCreate = storeRepository.findAll().size();

        // Create the Store
        StoreDTO storeDTO = storeMapper.toDto(store);
        restStoreMockMvc.perform(post("/api/stores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeDTO)))
            .andExpect(status().isCreated());

        // Validate the Store in the database
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeCreate + 1);
        Store testStore = storeList.get(storeList.size() - 1);
        assertThat(testStore.getRegNo()).isEqualTo(DEFAULT_REG_NO);
        assertThat(testStore.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStore.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testStore.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testStore.getTotalRating()).isEqualTo(DEFAULT_TOTAL_RATING);
        assertThat(testStore.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testStore.getLocationName()).isEqualTo(DEFAULT_LOCATION_NAME);
        assertThat(testStore.getContactNo()).isEqualTo(DEFAULT_CONTACT_NO);
        assertThat(testStore.getOpeningTime()).isEqualTo(DEFAULT_OPENING_TIME);
        assertThat(testStore.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testStore.getClosingTime()).isEqualTo(DEFAULT_CLOSING_TIME);
        assertThat(testStore.getInfo()).isEqualTo(DEFAULT_INFO);
        assertThat(testStore.getMinAmount()).isEqualTo(DEFAULT_MIN_AMOUNT);
        assertThat(testStore.getMaxDeliveryTime()).isEqualTo(DEFAULT_MAX_DELIVERY_TIME);
        assertThat(testStore.getStoreUniqueId()).isEqualTo(DEFAULT_STORE_UNIQUE_ID);
        assertThat(testStore.getImageLink()).isEqualTo(DEFAULT_IMAGE_LINK);

        // Validate the Store in Elasticsearch
        verify(mockStoreSearchRepository, times(1)).save(testStore);
    }

    @Test
    @Transactional
    public void createStoreWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = storeRepository.findAll().size();

        // Create the Store with an existing ID
        store.setId(1L);
        StoreDTO storeDTO = storeMapper.toDto(store);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStoreMockMvc.perform(post("/api/stores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Store in the database
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Store in Elasticsearch
        verify(mockStoreSearchRepository, times(0)).save(store);
    }

    @Test
    @Transactional
    public void checkStoreUniqueIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = storeRepository.findAll().size();
        // set the field null
        store.setStoreUniqueId(null);

        // Create the Store, which fails.
        StoreDTO storeDTO = storeMapper.toDto(store);

        restStoreMockMvc.perform(post("/api/stores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeDTO)))
            .andExpect(status().isBadRequest());

        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkImageLinkIsRequired() throws Exception {
        int databaseSizeBeforeTest = storeRepository.findAll().size();
        // set the field null
        store.setImageLink(null);

        // Create the Store, which fails.
        StoreDTO storeDTO = storeMapper.toDto(store);

        restStoreMockMvc.perform(post("/api/stores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeDTO)))
            .andExpect(status().isBadRequest());

        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStores() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList
        restStoreMockMvc.perform(get("/api/stores?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(store.getId().intValue())))
            .andExpect(jsonPath("$.[*].regNo").value(hasItem(DEFAULT_REG_NO.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].totalRating").value(hasItem(DEFAULT_TOTAL_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].locationName").value(hasItem(DEFAULT_LOCATION_NAME.toString())))
            .andExpect(jsonPath("$.[*].contactNo").value(hasItem(DEFAULT_CONTACT_NO.intValue())))
            .andExpect(jsonPath("$.[*].openingTime").value(hasItem(DEFAULT_OPENING_TIME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].closingTime").value(hasItem(DEFAULT_CLOSING_TIME.toString())))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO.toString())))
            .andExpect(jsonPath("$.[*].minAmount").value(hasItem(DEFAULT_MIN_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].maxDeliveryTime").value(hasItem(DEFAULT_MAX_DELIVERY_TIME.toString())))
            .andExpect(jsonPath("$.[*].storeUniqueId").value(hasItem(DEFAULT_STORE_UNIQUE_ID.toString())))
            .andExpect(jsonPath("$.[*].imageLink").value(hasItem(DEFAULT_IMAGE_LINK.toString())));
    }
    
    @Test
    @Transactional
    public void getStore() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get the store
        restStoreMockMvc.perform(get("/api/stores/{id}", store.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(store.getId().intValue()))
            .andExpect(jsonPath("$.regNo").value(DEFAULT_REG_NO.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.totalRating").value(DEFAULT_TOTAL_RATING.doubleValue()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.locationName").value(DEFAULT_LOCATION_NAME.toString()))
            .andExpect(jsonPath("$.contactNo").value(DEFAULT_CONTACT_NO.intValue()))
            .andExpect(jsonPath("$.openingTime").value(DEFAULT_OPENING_TIME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.closingTime").value(DEFAULT_CLOSING_TIME.toString()))
            .andExpect(jsonPath("$.info").value(DEFAULT_INFO.toString()))
            .andExpect(jsonPath("$.minAmount").value(DEFAULT_MIN_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.maxDeliveryTime").value(DEFAULT_MAX_DELIVERY_TIME.toString()))
            .andExpect(jsonPath("$.storeUniqueId").value(DEFAULT_STORE_UNIQUE_ID.toString()))
            .andExpect(jsonPath("$.imageLink").value(DEFAULT_IMAGE_LINK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStore() throws Exception {
        // Get the store
        restStoreMockMvc.perform(get("/api/stores/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStore() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        int databaseSizeBeforeUpdate = storeRepository.findAll().size();

        // Update the store
        Store updatedStore = storeRepository.findById(store.getId()).get();
        // Disconnect from session so that the updates on updatedStore are not directly saved in db
        em.detach(updatedStore);
        updatedStore
            .regNo(UPDATED_REG_NO)
            .name(UPDATED_NAME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .totalRating(UPDATED_TOTAL_RATING)
            .location(UPDATED_LOCATION)
            .locationName(UPDATED_LOCATION_NAME)
            .contactNo(UPDATED_CONTACT_NO)
            .openingTime(UPDATED_OPENING_TIME)
            .email(UPDATED_EMAIL)
            .closingTime(UPDATED_CLOSING_TIME)
            .info(UPDATED_INFO)
            .minAmount(UPDATED_MIN_AMOUNT)
            .maxDeliveryTime(UPDATED_MAX_DELIVERY_TIME)
            .storeUniqueId(UPDATED_STORE_UNIQUE_ID)
            .imageLink(UPDATED_IMAGE_LINK);
        StoreDTO storeDTO = storeMapper.toDto(updatedStore);

        restStoreMockMvc.perform(put("/api/stores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeDTO)))
            .andExpect(status().isOk());

        // Validate the Store in the database
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeUpdate);
        Store testStore = storeList.get(storeList.size() - 1);
        assertThat(testStore.getRegNo()).isEqualTo(UPDATED_REG_NO);
        assertThat(testStore.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStore.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testStore.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testStore.getTotalRating()).isEqualTo(UPDATED_TOTAL_RATING);
        assertThat(testStore.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testStore.getLocationName()).isEqualTo(UPDATED_LOCATION_NAME);
        assertThat(testStore.getContactNo()).isEqualTo(UPDATED_CONTACT_NO);
        assertThat(testStore.getOpeningTime()).isEqualTo(UPDATED_OPENING_TIME);
        assertThat(testStore.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testStore.getClosingTime()).isEqualTo(UPDATED_CLOSING_TIME);
        assertThat(testStore.getInfo()).isEqualTo(UPDATED_INFO);
        assertThat(testStore.getMinAmount()).isEqualTo(UPDATED_MIN_AMOUNT);
        assertThat(testStore.getMaxDeliveryTime()).isEqualTo(UPDATED_MAX_DELIVERY_TIME);
        assertThat(testStore.getStoreUniqueId()).isEqualTo(UPDATED_STORE_UNIQUE_ID);
        assertThat(testStore.getImageLink()).isEqualTo(UPDATED_IMAGE_LINK);

        // Validate the Store in Elasticsearch
        verify(mockStoreSearchRepository, times(1)).save(testStore);
    }

    @Test
    @Transactional
    public void updateNonExistingStore() throws Exception {
        int databaseSizeBeforeUpdate = storeRepository.findAll().size();

        // Create the Store
        StoreDTO storeDTO = storeMapper.toDto(store);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStoreMockMvc.perform(put("/api/stores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Store in the database
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Store in Elasticsearch
        verify(mockStoreSearchRepository, times(0)).save(store);
    }

    @Test
    @Transactional
    public void deleteStore() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        int databaseSizeBeforeDelete = storeRepository.findAll().size();

        // Delete the store
        restStoreMockMvc.perform(delete("/api/stores/{id}", store.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Store in Elasticsearch
        verify(mockStoreSearchRepository, times(1)).deleteById(store.getId());
    }

    @Test
    @Transactional
    public void searchStore() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);
        when(mockStoreSearchRepository.search(queryStringQuery("id:" + store.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(store), PageRequest.of(0, 1), 1));
        // Search the store
        restStoreMockMvc.perform(get("/api/_search/stores?query=id:" + store.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(store.getId().intValue())))
            .andExpect(jsonPath("$.[*].regNo").value(hasItem(DEFAULT_REG_NO)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].totalRating").value(hasItem(DEFAULT_TOTAL_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].locationName").value(hasItem(DEFAULT_LOCATION_NAME)))
            .andExpect(jsonPath("$.[*].contactNo").value(hasItem(DEFAULT_CONTACT_NO.intValue())))
            .andExpect(jsonPath("$.[*].openingTime").value(hasItem(DEFAULT_OPENING_TIME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].closingTime").value(hasItem(DEFAULT_CLOSING_TIME.toString())))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO)))
            .andExpect(jsonPath("$.[*].minAmount").value(hasItem(DEFAULT_MIN_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].maxDeliveryTime").value(hasItem(DEFAULT_MAX_DELIVERY_TIME.toString())))
            .andExpect(jsonPath("$.[*].storeUniqueId").value(hasItem(DEFAULT_STORE_UNIQUE_ID)))
            .andExpect(jsonPath("$.[*].imageLink").value(hasItem(DEFAULT_IMAGE_LINK)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Store.class);
        Store store1 = new Store();
        store1.setId(1L);
        Store store2 = new Store();
        store2.setId(store1.getId());
        assertThat(store1).isEqualTo(store2);
        store2.setId(2L);
        assertThat(store1).isNotEqualTo(store2);
        store1.setId(null);
        assertThat(store1).isNotEqualTo(store2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StoreDTO.class);
        StoreDTO storeDTO1 = new StoreDTO();
        storeDTO1.setId(1L);
        StoreDTO storeDTO2 = new StoreDTO();
        assertThat(storeDTO1).isNotEqualTo(storeDTO2);
        storeDTO2.setId(storeDTO1.getId());
        assertThat(storeDTO1).isEqualTo(storeDTO2);
        storeDTO2.setId(2L);
        assertThat(storeDTO1).isNotEqualTo(storeDTO2);
        storeDTO1.setId(null);
        assertThat(storeDTO1).isNotEqualTo(storeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(storeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(storeMapper.fromId(null)).isNull();
    }
}
