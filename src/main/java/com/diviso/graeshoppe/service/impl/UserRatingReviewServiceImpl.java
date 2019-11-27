package com.diviso.graeshoppe.service.impl;

import com.diviso.graeshoppe.service.UserRatingReviewService;
import com.diviso.graeshoppe.domain.UserRatingReview;
import com.diviso.graeshoppe.repository.UserRatingReviewRepository;
import com.diviso.graeshoppe.repository.search.UserRatingReviewSearchRepository;
import com.diviso.graeshoppe.service.dto.UserRatingReviewDTO;
import com.diviso.graeshoppe.service.mapper.UserRatingReviewMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing UserRatingReview.
 */
@Service
@Transactional
public class UserRatingReviewServiceImpl implements UserRatingReviewService {

    private final Logger log = LoggerFactory.getLogger(UserRatingReviewServiceImpl.class);

    private final UserRatingReviewRepository userRatingReviewRepository;

    private final UserRatingReviewMapper userRatingReviewMapper;

    private final UserRatingReviewSearchRepository userRatingReviewSearchRepository;

    public UserRatingReviewServiceImpl(UserRatingReviewRepository userRatingReviewRepository, UserRatingReviewMapper userRatingReviewMapper, UserRatingReviewSearchRepository userRatingReviewSearchRepository) {
        this.userRatingReviewRepository = userRatingReviewRepository;
        this.userRatingReviewMapper = userRatingReviewMapper;
        this.userRatingReviewSearchRepository = userRatingReviewSearchRepository;
    }

    /**
     * Save a userRatingReview.
     *
     * @param userRatingReviewDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public UserRatingReviewDTO save(UserRatingReviewDTO userRatingReviewDTO) {
        log.debug("Request to save UserRatingReview : {}", userRatingReviewDTO);
        UserRatingReview userRatingReview = userRatingReviewMapper.toEntity(userRatingReviewDTO);
        userRatingReview = userRatingReviewRepository.save(userRatingReview);
        UserRatingReviewDTO result = userRatingReviewMapper.toDto(userRatingReview);
        userRatingReviewSearchRepository.save(userRatingReview);
        return result;
    }

    /**
     * Get all the userRatingReviews.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserRatingReviewDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserRatingReviews");
        return userRatingReviewRepository.findAll(pageable)
            .map(userRatingReviewMapper::toDto);
    }


    /**
     * Get one userRatingReview by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UserRatingReviewDTO> findOne(Long id) {
        log.debug("Request to get UserRatingReview : {}", id);
        return userRatingReviewRepository.findById(id)
            .map(userRatingReviewMapper::toDto);
    }

    /**
     * Delete the userRatingReview by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserRatingReview : {}", id);        userRatingReviewRepository.deleteById(id);
        userRatingReviewSearchRepository.deleteById(id);
    }

    /**
     * Search for the userRatingReview corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserRatingReviewDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UserRatingReviews for query {}", query);
        return userRatingReviewSearchRepository.search(queryStringQuery(query), pageable)
            .map(userRatingReviewMapper::toDto);
    }
}
