package com.diviso.graeshoppe.service;

import com.diviso.graeshoppe.service.dto.StoreTypeDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing StoreType.
 */
public interface StoreTypeService {

    /**
     * Save a storeType.
     *
     * @param storeTypeDTO the entity to save
     * @return the persisted entity
     */
    StoreTypeDTO save(StoreTypeDTO storeTypeDTO);

    /**
     * Get all the storeTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<StoreTypeDTO> findAll(Pageable pageable);


    /**
     * Get the "id" storeType.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<StoreTypeDTO> findOne(Long id);

    /**
     * Delete the "id" storeType.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the storeType corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<StoreTypeDTO> search(String query, Pageable pageable);
}
