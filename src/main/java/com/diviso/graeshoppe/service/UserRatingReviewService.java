package com.diviso.graeshoppe.service;

import com.diviso.graeshoppe.domain.UserRatingReview;
import com.diviso.graeshoppe.service.dto.UserRatingReviewDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing UserRatingReview.
 */
public interface UserRatingReviewService {

    /**
     * Save a userRatingReview.
     *
     * @param userRatingReviewDTO the entity to save
     * @return the persisted entity
     */
    UserRatingReviewDTO save(UserRatingReviewDTO userRatingReviewDTO);

    /**
     * Get all the userRatingReviews.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<UserRatingReviewDTO> findAll(Pageable pageable);


    /**
     * Get the "id" userRatingReview.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<UserRatingReviewDTO> findOne(Long id);

    /**
     * Delete the "id" userRatingReview.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the userRatingReview corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<UserRatingReviewDTO> search(String query, Pageable pageable);
    boolean isAlreadyRatedUser(Long storeId, String userName);

	Long findUserRatingReviwIdByStoreIdAndUserName(Long storeId, String userName);
}
