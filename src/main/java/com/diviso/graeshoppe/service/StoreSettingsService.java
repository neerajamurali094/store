package com.diviso.graeshoppe.service;

import com.diviso.graeshoppe.domain.StoreSettings;
import com.diviso.graeshoppe.service.dto.StoreSettingsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Service Interface for managing StoreSettings.
 */
public interface StoreSettingsService {

    /**
     * Save a storeSettings.
     *
     * @param storeSettingsDTO the entity to save
     * @return the persisted entity
     */
    StoreSettingsDTO save(StoreSettingsDTO storeSettingsDTO);

    /**
     * Get all the storeSettings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<StoreSettingsDTO> findAll(Pageable pageable);


    /**
     * Get the "id" storeSettings.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<StoreSettingsDTO> findOne(Long id);

    /**
     * Delete the "id" storeSettings.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the storeSettings corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<StoreSettingsDTO> search(String query, Pageable pageable);
    
   
}
