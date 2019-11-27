package com.diviso.graeshoppe.service;

import com.diviso.graeshoppe.service.dto.PreOrderSettingsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing PreOrderSettings.
 */
public interface PreOrderSettingsService {

    /**
     * Save a preOrderSettings.
     *
     * @param preOrderSettingsDTO the entity to save
     * @return the persisted entity
     */
    PreOrderSettingsDTO save(PreOrderSettingsDTO preOrderSettingsDTO);

    /**
     * Get all the preOrderSettings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PreOrderSettingsDTO> findAll(Pageable pageable);


    /**
     * Get the "id" preOrderSettings.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PreOrderSettingsDTO> findOne(Long id);

    /**
     * Delete the "id" preOrderSettings.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the preOrderSettings corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PreOrderSettingsDTO> search(String query, Pageable pageable);
}
