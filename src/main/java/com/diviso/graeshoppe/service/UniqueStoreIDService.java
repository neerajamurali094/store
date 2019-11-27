package com.diviso.graeshoppe.service;

import com.diviso.graeshoppe.service.dto.UniqueStoreIDDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing UniqueStoreID.
 */
public interface UniqueStoreIDService {

    /**
     * Save a uniqueStoreID.
     *
     * @param uniqueStoreIDDTO the entity to save
     * @return the persisted entity
     */
    UniqueStoreIDDTO save(UniqueStoreIDDTO uniqueStoreIDDTO);

    /**
     * Get all the uniqueStoreIDS.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<UniqueStoreIDDTO> findAll(Pageable pageable);


    /**
     * Get the "id" uniqueStoreID.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<UniqueStoreIDDTO> findOne(Long id);

    /**
     * Delete the "id" uniqueStoreID.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the uniqueStoreID corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<UniqueStoreIDDTO> search(String query, Pageable pageable);
}
