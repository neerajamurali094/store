package com.diviso.graeshoppe.web.rest;

import com.diviso.graeshoppe.domain.Store;
import com.diviso.graeshoppe.domain.StoreSettings;
import com.diviso.graeshoppe.repository.StoreRepository;
import com.diviso.graeshoppe.repository.search.StoreSearchRepository;
import com.diviso.graeshoppe.service.StoreService;
import com.diviso.graeshoppe.service.StoreSettingsService;
import com.diviso.graeshoppe.web.rest.errors.BadRequestAlertException;
import com.diviso.graeshoppe.web.rest.util.HeaderUtil;
import com.diviso.graeshoppe.web.rest.util.PaginationUtil;
import com.diviso.graeshoppe.service.dto.StoreDTO;
import com.diviso.graeshoppe.service.dto.StoreSettingsDTO;

import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Store.
 */
@RestController
@RequestMapping("/api")
public class StoreResource {

	@Autowired
	StoreSearchRepository storeSearchRepository;

	@Autowired
	StoreRepository storeRepo;

	private final Logger log = LoggerFactory.getLogger(StoreResource.class);

	private static final String ENTITY_NAME = "storeStore";

	private final StoreService storeService;

	@Autowired
	private StoreSettingsService storeSettingsService;

	public StoreResource(StoreService storeService) {
		this.storeService = storeService;
	}

	/**
	 * POST /stores : Create a new store.
	 *
	 * @param storeDTO
	 *            the storeDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new storeDTO, or with status 400 (Bad Request) if the store has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/stores")
	public ResponseEntity<StoreDTO> createStore(@RequestBody StoreDTO storeDTO) throws URISyntaxException {
		
		log.debug("REST request to save Store : {}", storeDTO);
		
		storeDTO.setTotalRating(0.0);

		if (storeDTO.getId() != null) {
			throw new BadRequestAlertException("A new store cannot already have an ID", ENTITY_NAME, "idexists");
		}
		List<Store> stores = storeRepo.findAll();

		stores.forEach(s -> {
			if (storeDTO.getRegNo().equals(s.getRegNo())) {

				throw new BadRequestAlertException("Already have a customer with the same name", ENTITY_NAME,
						"nameexists");
			}

		});

		StoreSettingsDTO storeSettings = new StoreSettingsDTO();

		storeSettings.setOrderAcceptType("automatic");

		storeSettings.setDeliveryCharge(0.0);

		storeSettings.setServiceCharge(0.0);

		StoreSettingsDTO storeSettingsDTO = storeSettingsService.save(storeSettings);

		storeDTO.setStoreSettingsId(storeSettingsDTO.getId());

		StoreDTO result = storeService.save(storeDTO);

		return ResponseEntity.created(new URI("/api/stores/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	@PostMapping("/stores-denormalized")
	public ResponseEntity<Store> createDeNormalizedStore(@RequestBody Store store) throws URISyntaxException {
		log.debug("REST request to save Store : {}", store);
		if (store.getId() != null) {
			throw new BadRequestAlertException("A new store cannot already have an ID", ENTITY_NAME, "idexists");
		}
		Store result = storeService.saveStore(store);
		return ResponseEntity.created(new URI("/api/stores-denormalized/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /stores : Updates an existing store.
	 *
	 * @param storeDTO
	 *            the storeDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         storeDTO, or with status 400 (Bad Request) if the storeDTO is not
	 *         valid, or with status 500 (Internal Server Error) if the storeDTO
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/stores")
	public ResponseEntity<StoreDTO> updateStore(@RequestBody StoreDTO storeDTO) throws URISyntaxException {
		log.debug("REST request to update Store : {}", storeDTO);
		if (storeDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		StoreDTO result = storeService.save(storeDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, storeDTO.getId().toString()))
				.body(result);
	}

	@PutMapping("/stores-denormalized")
	public ResponseEntity<Store> updateStoreDeNormalized(@RequestBody Store store) throws URISyntaxException {
		log.debug("REST request to update StoreDenormalized : {}", store);
		if (store.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		Store result = storeService.saveStore(store);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, store.getId().toString()))
				.body(result);
	}

	/**
	 * GET /stores : get all the stores.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of stores in
	 *         body
	 */
	@GetMapping("/stores")
	public ResponseEntity<List<StoreDTO>> getAllStores(Pageable pageable) {
		log.debug("REST request to get a page of Stores");
		Page<StoreDTO> page = storeService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stores");
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	/**
	 * GET /stores/:id : get the "id" store.
	 *
	 * @param id
	 *            the id of the storeDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         storeDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/stores/{id}")
	public ResponseEntity<StoreDTO> getStore(@PathVariable Long id) {
		log.debug("REST request to get Store : {}", id);
		Optional<StoreDTO> storeDTO = storeService.findOne(id);
		return ResponseUtil.wrapOrNotFound(storeDTO);
	}

	/**
	 * DELETE /stores/:id : delete the "id" store.
	 *
	 * @param id
	 *            the id of the storeDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/stores/{id}")
	public ResponseEntity<Void> deleteStore(@PathVariable Long id) {
		log.debug("REST request to delete Store : {}", id);
		storeService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/stores?query=:query : search for the store corresponding
	 * to the query.
	 *
	 * @param query
	 *            the query of the store search
	 * @param pageable
	 *            the pagination information
	 * @return the result of the search
	 */
	@GetMapping("/_search/stores")
	public ResponseEntity<List<StoreDTO>> searchStores(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search for a page of Stores for query {}", query);
		Page<StoreDTO> page = storeService.search(query, pageable);
		HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/stores");
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	// for testing purpose DeleteMe
	@PostMapping("/createStores")
	public void create() {
		Store t1 = new Store();
		t1.setId(367l);
		t1.setName("grilchicken");
		storeSearchRepository.save(t1);

		Store t2 = new Store();
		t2.setId(368l);
		t2.setName("grilgchi[cken");
		storeSearchRepository.save(t2);

		Store t3 = new Store();
		t3.setId(367l);
		t3.setName("rrun'neschicken");
		storeSearchRepository.save(t3);
	}

	@GetMapping("/storesettings/{storeId}")
	public StoreSettingsDTO findStoreSettingsByStoreId(@PathVariable String storeId){
		return storeService.findStoreSettingsByStoreId(storeId);
	}
	
	@GetMapping("/findByRegNo/{regNo}")
	public Store findByRegNo(@PathVariable String regNo){
		return storeService.findByRegNo(regNo);
	}
}
