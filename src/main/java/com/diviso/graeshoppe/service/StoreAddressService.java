package com.diviso.graeshoppe.service;

import com.diviso.graeshoppe.service.dto.StoreAddressDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing StoreAddress.
 */
public interface StoreAddressService {

    /**
     * Save a storeAddress.
     *
     * @param storeAddressDTO the entity to save
     * @return the persisted entity
     */
    StoreAddressDTO save(StoreAddressDTO storeAddressDTO);

    /**
     * Get all the storeAddresses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<StoreAddressDTO> findAll(Pageable pageable);


    /**
     * Get the "id" storeAddress.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<StoreAddressDTO> findOne(Long id);

    /**
     * Delete the "id" storeAddress.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the storeAddress corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<StoreAddressDTO> search(String query, Pageable pageable);
}
