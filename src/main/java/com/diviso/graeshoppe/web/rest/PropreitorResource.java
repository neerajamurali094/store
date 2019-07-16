package com.diviso.graeshoppe.web.rest;
import com.diviso.graeshoppe.service.PropreitorService;
import com.diviso.graeshoppe.web.rest.errors.BadRequestAlertException;
import com.diviso.graeshoppe.web.rest.util.HeaderUtil;
import com.diviso.graeshoppe.web.rest.util.PaginationUtil;
import com.diviso.graeshoppe.service.dto.PropreitorDTO;
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
 * REST controller for managing Propreitor.
 */
@RestController
@RequestMapping("/api")
public class PropreitorResource {

    private final Logger log = LoggerFactory.getLogger(PropreitorResource.class);

    private static final String ENTITY_NAME = "storePropreitor";

    private final PropreitorService propreitorService;

    public PropreitorResource(PropreitorService propreitorService) {
        this.propreitorService = propreitorService;
    }

    /**
     * POST  /propreitors : Create a new propreitor.
     *
     * @param propreitorDTO the propreitorDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new propreitorDTO, or with status 400 (Bad Request) if the propreitor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/propreitors")
    public ResponseEntity<PropreitorDTO> createPropreitor(@RequestBody PropreitorDTO propreitorDTO) throws URISyntaxException {
        log.debug("REST request to save Propreitor : {}", propreitorDTO);
        if (propreitorDTO.getId() != null) {
            throw new BadRequestAlertException("A new propreitor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PropreitorDTO result = propreitorService.save(propreitorDTO);
        return ResponseEntity.created(new URI("/api/propreitors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /propreitors : Updates an existing propreitor.
     *
     * @param propreitorDTO the propreitorDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated propreitorDTO,
     * or with status 400 (Bad Request) if the propreitorDTO is not valid,
     * or with status 500 (Internal Server Error) if the propreitorDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/propreitors")
    public ResponseEntity<PropreitorDTO> updatePropreitor(@RequestBody PropreitorDTO propreitorDTO) throws URISyntaxException {
        log.debug("REST request to update Propreitor : {}", propreitorDTO);
        if (propreitorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PropreitorDTO result = propreitorService.save(propreitorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, propreitorDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /propreitors : get all the propreitors.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of propreitors in body
     */
    @GetMapping("/propreitors")
    public ResponseEntity<List<PropreitorDTO>> getAllPropreitors(Pageable pageable) {
        log.debug("REST request to get a page of Propreitors");
        Page<PropreitorDTO> page = propreitorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/propreitors");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /propreitors/:id : get the "id" propreitor.
     *
     * @param id the id of the propreitorDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the propreitorDTO, or with status 404 (Not Found)
     */
    @GetMapping("/propreitors/{id}")
    public ResponseEntity<PropreitorDTO> getPropreitor(@PathVariable Long id) {
        log.debug("REST request to get Propreitor : {}", id);
        Optional<PropreitorDTO> propreitorDTO = propreitorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(propreitorDTO);
    }

    /**
     * DELETE  /propreitors/:id : delete the "id" propreitor.
     *
     * @param id the id of the propreitorDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/propreitors/{id}")
    public ResponseEntity<Void> deletePropreitor(@PathVariable Long id) {
        log.debug("REST request to delete Propreitor : {}", id);
        propreitorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/propreitors?query=:query : search for the propreitor corresponding
     * to the query.
     *
     * @param query the query of the propreitor search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/propreitors")
    public ResponseEntity<List<PropreitorDTO>> searchPropreitors(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Propreitors for query {}", query);
        Page<PropreitorDTO> page = propreitorService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/propreitors");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
