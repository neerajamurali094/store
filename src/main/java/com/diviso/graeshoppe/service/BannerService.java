package com.diviso.graeshoppe.service;

import com.diviso.graeshoppe.service.dto.BannerDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Banner.
 */
public interface BannerService {

    /**
     * Save a banner.
     *
     * @param bannerDTO the entity to save
     * @return the persisted entity
     */
    BannerDTO save(BannerDTO bannerDTO);

    /**
     * Get all the banners.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<BannerDTO> findAll(Pageable pageable);


    /**
     * Get the "id" banner.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<BannerDTO> findOne(Long id);

    /**
     * Delete the "id" banner.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the banner corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<BannerDTO> search(String query, Pageable pageable);
}
