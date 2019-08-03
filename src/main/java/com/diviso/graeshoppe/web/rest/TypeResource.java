package com.diviso.graeshoppe.web.rest;
import com.diviso.graeshoppe.domain.DeliveryInfo;
import com.diviso.graeshoppe.domain.Type;
import com.diviso.graeshoppe.service.TypeService;
import com.diviso.graeshoppe.web.rest.errors.BadRequestAlertException;
import com.diviso.graeshoppe.web.rest.util.HeaderUtil;
import com.diviso.graeshoppe.web.rest.util.PaginationUtil;

import com.diviso.graeshoppe.service.dto.TypeDTO;
import com.diviso.graeshoppe.service.mapper.TypeMapper;

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
 * REST controller for managing Type.
 */
@RestController
@RequestMapping("/api")
public class TypeResource {

    private final Logger log = LoggerFactory.getLogger(TypeResource.class);

    private static final String ENTITY_NAME = "storeType";

    private final TypeService typeService;
    @Autowired
    private TypeMapper typeMapper;

    public TypeResource(TypeService typeService) {
        this.typeService = typeService;
    }

    /**
     * POST  /types : Create a new type.
     *
     * @param typeDTO the typeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new typeDTO, or with status 400 (Bad Request) if the type has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/types")
    public ResponseEntity<TypeDTO> createType(@RequestBody TypeDTO typeDTO) throws URISyntaxException {
        log.debug("REST request to save Type : {}", typeDTO);
        if (typeDTO.getId() != null) {
            throw new BadRequestAlertException("A new type cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeDTO result = typeService.save(typeDTO);
        return ResponseEntity.created(new URI("/api/types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /types : Updates an existing type.
     *
     * @param typeDTO the typeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated typeDTO,
     * or with status 400 (Bad Request) if the typeDTO is not valid,
     * or with status 500 (Internal Server Error) if the typeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/types")
    public ResponseEntity<TypeDTO> updateType(@RequestBody TypeDTO typeDTO) throws URISyntaxException {
        log.debug("REST request to update Type : {}", typeDTO);
        if (typeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TypeDTO result = typeService.save(typeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, typeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /types : get all the types.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of types in body
     */
    @GetMapping("/types")
    public ResponseEntity<List<TypeDTO>> getAllTypes(Pageable pageable) {
        log.debug("REST request to get a page of Types");
        Page<TypeDTO> page = typeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/types");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /types/:id : get the "id" type.
     *
     * @param id the id of the typeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the typeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/types/{id}")
    public ResponseEntity<TypeDTO> getType(@PathVariable Long id) {
        log.debug("REST request to get Type : {}", id);
        Optional<TypeDTO> typeDTO = typeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeDTO);
    }

    /**
     * DELETE  /types/:id : delete the "id" type.
     *
     * @param id the id of the typeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/types/{id}")
    public ResponseEntity<Void> deleteType(@PathVariable Long id) {
        log.debug("REST request to delete Type : {}", id);
        typeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/types?query=:query : search for the type corresponding
     * to the query.
     *
     * @param query the query of the type search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/types")
    public ResponseEntity<List<TypeDTO>> searchTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Types for query {}", query);
        Page<TypeDTO> page = typeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/types");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/type/toDto")
	public ResponseEntity<List<TypeDTO>> listToDto(@RequestBody List<Type> types) {
		log.debug("REST request to convert to DTO");
		List<TypeDTO> dtos = new ArrayList<>();
		types.forEach(a -> {
			dtos.add(typeMapper.toDto(a));
		});
		return ResponseEntity.ok().body(dtos);
	}
}
