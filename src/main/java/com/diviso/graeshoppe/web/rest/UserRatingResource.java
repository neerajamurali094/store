package com.diviso.graeshoppe.web.rest;
import com.diviso.graeshoppe.service.UserRatingService;
import com.diviso.graeshoppe.web.rest.errors.BadRequestAlertException;
import com.diviso.graeshoppe.web.rest.util.HeaderUtil;
import com.diviso.graeshoppe.web.rest.util.PaginationUtil;
import com.diviso.graeshoppe.service.dto.UserRatingDTO;
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
 * REST controller for managing UserRating.
 */
@RestController
@RequestMapping("/api")
public class UserRatingResource {

    private final Logger log = LoggerFactory.getLogger(UserRatingResource.class);

    private static final String ENTITY_NAME = "storeUserRating";

    private final UserRatingService userRatingService;

    public UserRatingResource(UserRatingService userRatingService) {
        this.userRatingService = userRatingService;
    }

    /**
     * POST  /user-ratings : Create a new userRating.
     *
     * @param userRatingDTO the userRatingDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userRatingDTO, or with status 400 (Bad Request) if the userRating has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-ratings")
    public ResponseEntity<UserRatingDTO> createUserRating(@RequestBody UserRatingDTO userRatingDTO) throws URISyntaxException {
        log.debug("REST request to save UserRating : {}", userRatingDTO);
        if (userRatingDTO.getId() != null) {
            throw new BadRequestAlertException("A new userRating cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserRatingDTO result1 = userRatingService.save(userRatingDTO);
        if (userRatingDTO.getId() == null) {
            throw new BadRequestAlertException("A new userRating cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserRatingDTO result = userRatingService.save(result1);
        return ResponseEntity.created(new URI("/api/user-ratings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-ratings : Updates an existing userRating.
     *
     * @param userRatingDTO the userRatingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userRatingDTO,
     * or with status 400 (Bad Request) if the userRatingDTO is not valid,
     * or with status 500 (Internal Server Error) if the userRatingDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-ratings")
    public ResponseEntity<UserRatingDTO> updateUserRating(@RequestBody UserRatingDTO userRatingDTO) throws URISyntaxException {
        log.debug("REST request to update UserRating : {}", userRatingDTO);
        if (userRatingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserRatingDTO result = userRatingService.save(userRatingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userRatingDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-ratings : get all the userRatings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userRatings in body
     */
    @GetMapping("/user-ratings")
    public ResponseEntity<List<UserRatingDTO>> getAllUserRatings(Pageable pageable) {
        log.debug("REST request to get a page of UserRatings");
        Page<UserRatingDTO> page = userRatingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-ratings");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /user-ratings/:id : get the "id" userRating.
     *
     * @param id the id of the userRatingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userRatingDTO, or with status 404 (Not Found)
     */
    @GetMapping("/user-ratings/{id}")
    public ResponseEntity<UserRatingDTO> getUserRating(@PathVariable Long id) {
        log.debug("REST request to get UserRating : {}", id);
        Optional<UserRatingDTO> userRatingDTO = userRatingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userRatingDTO);
    }

    /**
     * DELETE  /user-ratings/:id : delete the "id" userRating.
     *
     * @param id the id of the userRatingDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-ratings/{id}")
    public ResponseEntity<Void> deleteUserRating(@PathVariable Long id) {
        log.debug("REST request to delete UserRating : {}", id);
        userRatingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/user-ratings?query=:query : search for the userRating corresponding
     * to the query.
     *
     * @param query the query of the userRating search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/user-ratings")
    public ResponseEntity<List<UserRatingDTO>> searchUserRatings(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of UserRatings for query {}", query);
        Page<UserRatingDTO> page = userRatingService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/user-ratings");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
