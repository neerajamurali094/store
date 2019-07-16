package com.diviso.graeshoppe.service;

import com.diviso.graeshoppe.service.dto.DeliveryInfoDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing DeliveryInfo.
 */
public interface DeliveryInfoService {

    /**
     * Save a deliveryInfo.
     *
     * @param deliveryInfoDTO the entity to save
     * @return the persisted entity
     */
    DeliveryInfoDTO save(DeliveryInfoDTO deliveryInfoDTO);

    /**
     * Get all the deliveryInfos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DeliveryInfoDTO> findAll(Pageable pageable);


    /**
     * Get the "id" deliveryInfo.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<DeliveryInfoDTO> findOne(Long id);

    /**
     * Delete the "id" deliveryInfo.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the deliveryInfo corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DeliveryInfoDTO> search(String query, Pageable pageable);
}
