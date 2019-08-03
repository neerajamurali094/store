package com.diviso.graeshoppe.web.rest;
import com.diviso.graeshoppe.domain.StoreType;
import com.diviso.graeshoppe.domain.Type;
import com.diviso.graeshoppe.service.StoreTypeService;
import com.diviso.graeshoppe.web.rest.errors.BadRequestAlertException;
import com.diviso.graeshoppe.web.rest.util.HeaderUtil;
import com.diviso.graeshoppe.web.rest.util.PaginationUtil;
import com.diviso.graeshoppe.service.dto.StoreTypeDTO;
import com.diviso.graeshoppe.service.dto.TypeDTO;
import com.diviso.graeshoppe.service.mapper.StoreTypeMapper;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing StoreType.
 */
@RestController
@RequestMapping("/api")
public class StoreTypeResource {

    private final Logger log = LoggerFactory.getLogger(StoreTypeResource.class);

    private static final String ENTITY_NAME = "storeStoreType";

    private final StoreTypeService storeTypeService;

    @Autowired
    private StoreTypeMapper storeTypeMapper;
    
    public StoreTypeResource(StoreTypeService storeTypeService) {
        this.storeTypeService = storeTypeService;
    }

    /**
     * POST  /store-types : Create a new storeType.
     *
     * @param storeTypeDTO the storeTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new storeTypeDTO, or with status 400 (Bad Request) if the storeType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/store-types")
    public ResponseEntity<StoreTypeDTO> createStoreType(@RequestBody StoreTypeDTO storeTypeDTO) throws URISyntaxException {
        log.debug("REST request to save StoreType : {}", storeTypeDTO);
        if (storeTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new storeType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StoreTypeDTO result = storeTypeService.save(storeTypeDTO);
        return ResponseEntity.created(new URI("/api/store-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /store-types : Updates an existing storeType.
     *
     * @param storeTypeDTO the storeTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated storeTypeDTO,
     * or with status 400 (Bad Request) if the storeTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the storeTypeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/store-types")
    public ResponseEntity<StoreTypeDTO> updateStoreType(@RequestBody StoreTypeDTO storeTypeDTO) throws URISyntaxException {
        log.debug("REST request to update StoreType : {}", storeTypeDTO);
        if (storeTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StoreTypeDTO result = storeTypeService.save(storeTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, storeTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /store-types : get all the storeTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of storeTypes in body
     */
    @GetMapping("/store-types")
    public ResponseEntity<List<StoreTypeDTO>> getAllStoreTypes(Pageable pageable) {
        log.debug("REST request to get a page of StoreTypes");
        Page<StoreTypeDTO> page = storeTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/store-types");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /store-types/:id : get the "id" storeType.
     *
     * @param id the id of the storeTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the storeTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/store-types/{id}")
    public ResponseEntity<StoreTypeDTO> getStoreType(@PathVariable Long id) {
        log.debug("REST request to get StoreType : {}", id);
        Optional<StoreTypeDTO> storeTypeDTO = storeTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(storeTypeDTO);
    }

    /**
     * DELETE  /store-types/:id : delete the "id" storeType.
     *
     * @param id the id of the storeTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/store-types/{id}")
    public ResponseEntity<Void> deleteStoreType(@PathVariable Long id) {
        log.debug("REST request to delete StoreType : {}", id);
        storeTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/store-types?query=:query : search for the storeType corresponding
     * to the query.
     *
     * @param query the query of the storeType search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/store-types")
    public ResponseEntity<List<StoreTypeDTO>> searchStoreTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of StoreTypes for query {}", query);
        Page<StoreTypeDTO> page = storeTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/store-types");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    
    @PostMapping("/storeType/toDto")
	public ResponseEntity<List<StoreTypeDTO>> listToDto(@RequestBody List<StoreType> storeTypes) {
		log.debug("REST request to convert to DTO");
		List<StoreTypeDTO> dtos = new ArrayList<>();
		storeTypes.forEach(a -> {
			dtos.add(storeTypeMapper.toDto(a));
		});
		return ResponseEntity.ok().body(dtos);
	}
}
