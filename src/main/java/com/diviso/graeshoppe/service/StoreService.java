package com.diviso.graeshoppe.service;

import com.diviso.graeshoppe.domain.Store;
import com.diviso.graeshoppe.domain.StoreSettings;
import com.diviso.graeshoppe.service.dto.StoreDTO;
import com.diviso.graeshoppe.service.dto.StoreSettingsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Store.
 */
public interface StoreService {

    /**
     * Save a store.
     *
     * @param storeDTO the entity to save
     * @return the persisted entity
     */
    StoreDTO save(StoreDTO storeDTO);
    
    Store saveStore(Store store);

    /**
     * Get all the stores.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<StoreDTO> findAll(Pageable pageable);


    /**
     * Get the "id" store.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<StoreDTO> findOne(Long id);

    /**
     * Delete the "id" store.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the store corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<StoreDTO> search(String query, Pageable pageable);
    
    StoreSettingsDTO findStoreSettingsByStoreId(String storeId);

	Store findByRegNo(String regNo);
}
