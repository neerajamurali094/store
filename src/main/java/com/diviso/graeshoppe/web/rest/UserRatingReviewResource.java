package com.diviso.graeshoppe.web.rest;
import com.diviso.graeshoppe.service.UserRatingReviewService;
import com.diviso.graeshoppe.web.rest.errors.BadRequestAlertException;
import com.diviso.graeshoppe.web.rest.util.HeaderUtil;
import com.diviso.graeshoppe.web.rest.util.PaginationUtil;
import com.diviso.graeshoppe.service.dto.UserRatingReviewDTO;
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
 * REST controller for managing UserRatingReview.
 */
@RestController
@RequestMapping("/api")
public class UserRatingReviewResource {

    private final Logger log = LoggerFactory.getLogger(UserRatingReviewResource.class);

    private static final String ENTITY_NAME = "storeUserRatingReview";

    private final UserRatingReviewService userRatingReviewService;

    public UserRatingReviewResource(UserRatingReviewService userRatingReviewService) {
        this.userRatingReviewService = userRatingReviewService;
    }

    /**
     * POST  /user-rating-reviews : Create a new userRatingReview.
     *
     * @param userRatingReviewDTO the userRatingReviewDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userRatingReviewDTO, or with status 400 (Bad Request) if the userRatingReview has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-rating-reviews")
    public ResponseEntity<UserRatingReviewDTO> createUserRatingReview(@RequestBody UserRatingReviewDTO userRatingReviewDTO) throws URISyntaxException {
        log.debug("REST request to save UserRatingReview : {}", userRatingReviewDTO);
        if (userRatingReviewDTO.getId() != null) {
            throw new BadRequestAlertException("A new userRatingReview cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserRatingReviewDTO result = userRatingReviewService.save(userRatingReviewDTO);
        return ResponseEntity.created(new URI("/api/user-rating-reviews/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-rating-reviews : Updates an existing userRatingReview.
     *
     * @param userRatingReviewDTO the userRatingReviewDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userRatingReviewDTO,
     * or with status 400 (Bad Request) if the userRatingReviewDTO is not valid,
     * or with status 500 (Internal Server Error) if the userRatingReviewDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-rating-reviews")
    public ResponseEntity<UserRatingReviewDTO> updateUserRatingReview(@RequestBody UserRatingReviewDTO userRatingReviewDTO) throws URISyntaxException {
        log.debug("REST request to update UserRatingReview : {}", userRatingReviewDTO);
        if (userRatingReviewDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserRatingReviewDTO result = userRatingReviewService.save(userRatingReviewDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userRatingReviewDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-rating-reviews : get all the userRatingReviews.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userRatingReviews in body
     */
    @GetMapping("/user-rating-reviews")
    public ResponseEntity<List<UserRatingReviewDTO>> getAllUserRatingReviews(Pageable pageable) {
        log.debug("REST request to get a page of UserRatingReviews");
        Page<UserRatingReviewDTO> page = userRatingReviewService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-rating-reviews");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /user-rating-reviews/:id : get the "id" userRatingReview.
     *
     * @param id the id of the userRatingReviewDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userRatingReviewDTO, or with status 404 (Not Found)
     */
    @GetMapping("/user-rating-reviews/{id}")
    public ResponseEntity<UserRatingReviewDTO> getUserRatingReview(@PathVariable Long id) {
        log.debug("REST request to get UserRatingReview : {}", id);
        Optional<UserRatingReviewDTO> userRatingReviewDTO = userRatingReviewService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userRatingReviewDTO);
    }

    /**
     * DELETE  /user-rating-reviews/:id : delete the "id" userRatingReview.
     *
     * @param id the id of the userRatingReviewDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-rating-reviews/{id}")
    public ResponseEntity<Void> deleteUserRatingReview(@PathVariable Long id) {
        log.debug("REST request to delete UserRatingReview : {}", id);
        userRatingReviewService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/user-rating-reviews?query=:query : search for the userRatingReview corresponding
     * to the query.
     *
     * @param query the query of the userRatingReview search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/user-rating-reviews")
    public ResponseEntity<List<UserRatingReviewDTO>> searchUserRatingReviews(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of UserRatingReviews for query {}", query);
        Page<UserRatingReviewDTO> page = userRatingReviewService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/user-rating-reviews");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
