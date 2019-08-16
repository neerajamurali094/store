package com.diviso.graeshoppe.web.rest;
import com.diviso.graeshoppe.service.StoreSettingsService;
import com.diviso.graeshoppe.web.rest.errors.BadRequestAlertException;
import com.diviso.graeshoppe.web.rest.util.HeaderUtil;
import com.diviso.graeshoppe.web.rest.util.PaginationUtil;
import com.diviso.graeshoppe.service.dto.BannerDTO;
import com.diviso.graeshoppe.service.dto.StoreSettingsDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing StoreSettings.
 */
@RestController
@RequestMapping("/api")
public class StoreSettingsResource {

    private final Logger log = LoggerFactory.getLogger(StoreSettingsResource.class);

    private static final String ENTITY_NAME = "storeStoreSettings";

    private final StoreSettingsService storeSettingsService;

    public StoreSettingsResource(StoreSettingsService storeSettingsService) {
        this.storeSettingsService = storeSettingsService;
    }

    /**
     * POST  /store-settings : Create a new storeSettings.
     *
     * @param storeSettingsDTO the storeSettingsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new storeSettingsDTO, or with status 400 (Bad Request) if the storeSettings has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/store-settings")
    public ResponseEntity<StoreSettingsDTO> createStoreSettings(@RequestBody StoreSettingsDTO storeSettingsDTO) throws URISyntaxException {
        log.debug("REST request to save StoreSettings : {}", storeSettingsDTO);
        if (storeSettingsDTO.getId() != null) {
            throw new BadRequestAlertException("A new storeSettings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StoreSettingsDTO result1 = storeSettingsService.save(storeSettingsDTO);
		if (result1.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		StoreSettingsDTO result = storeSettingsService.save(result1);
        return ResponseEntity.created(new URI("/api/store-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /store-settings : Updates an existing storeSettings.
     *
     * @param storeSettingsDTO the storeSettingsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated storeSettingsDTO,
     * or with status 400 (Bad Request) if the storeSettingsDTO is not valid,
     * or with status 500 (Internal Server Error) if the storeSettingsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/store-settings")
    public ResponseEntity<StoreSettingsDTO> updateStoreSettings(@RequestBody StoreSettingsDTO storeSettingsDTO) throws URISyntaxException {
        log.debug("REST request to update StoreSettings : {}", storeSettingsDTO);
        if (storeSettingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StoreSettingsDTO result = storeSettingsService.save(storeSettingsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, storeSettingsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /store-settings : get all the storeSettings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of storeSettings in body
     */
    @GetMapping("/store-settings")
    public ResponseEntity<List<StoreSettingsDTO>> getAllStoreSettings(Pageable pageable) {
        log.debug("REST request to get a page of StoreSettings");
        Page<StoreSettingsDTO> page = storeSettingsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/store-settings");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /store-settings/:id : get the "id" storeSettings.
     *
     * @param id the id of the storeSettingsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the storeSettingsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/store-settings/{id}")
    public ResponseEntity<StoreSettingsDTO> getStoreSettings(@PathVariable Long id) {
        log.debug("REST request to get StoreSettings : {}", id);
        Optional<StoreSettingsDTO> storeSettingsDTO = storeSettingsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(storeSettingsDTO);
    }

    /**
     * DELETE  /store-settings/:id : delete the "id" storeSettings.
     *
     * @param id the id of the storeSettingsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/store-settings/{id}")
    public ResponseEntity<Void> deleteStoreSettings(@PathVariable Long id) {
        log.debug("REST request to delete StoreSettings : {}", id);
        storeSettingsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/store-settings?query=:query : search for the storeSettings corresponding
     * to the query.
     *
     * @param query the query of the storeSettings search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/store-settings")
    public ResponseEntity<List<StoreSettingsDTO>> searchStoreSettings(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of StoreSettings for query {}", query);
        Page<StoreSettingsDTO> page = storeSettingsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/store-settings");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
