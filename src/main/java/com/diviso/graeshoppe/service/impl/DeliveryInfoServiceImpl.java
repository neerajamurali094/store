package com.diviso.graeshoppe.service.impl;

import com.diviso.graeshoppe.service.DeliveryInfoService;
import com.diviso.graeshoppe.domain.DeliveryInfo;
import com.diviso.graeshoppe.repository.DeliveryInfoRepository;
import com.diviso.graeshoppe.repository.search.DeliveryInfoSearchRepository;
import com.diviso.graeshoppe.service.dto.DeliveryInfoDTO;
import com.diviso.graeshoppe.service.mapper.DeliveryInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing DeliveryInfo.
 */
@Service
@Transactional
public class DeliveryInfoServiceImpl implements DeliveryInfoService {

    private final Logger log = LoggerFactory.getLogger(DeliveryInfoServiceImpl.class);

    private final DeliveryInfoRepository deliveryInfoRepository;

    private final DeliveryInfoMapper deliveryInfoMapper;

    private final DeliveryInfoSearchRepository deliveryInfoSearchRepository;

    public DeliveryInfoServiceImpl(DeliveryInfoRepository deliveryInfoRepository, DeliveryInfoMapper deliveryInfoMapper, DeliveryInfoSearchRepository deliveryInfoSearchRepository) {
        this.deliveryInfoRepository = deliveryInfoRepository;
        this.deliveryInfoMapper = deliveryInfoMapper;
        this.deliveryInfoSearchRepository = deliveryInfoSearchRepository;
    }

    /**
     * Save a deliveryInfo.
     *
     * @param deliveryInfoDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DeliveryInfoDTO save(DeliveryInfoDTO deliveryInfoDTO) {
        log.debug("Request to save DeliveryInfo : {}", deliveryInfoDTO);
        DeliveryInfo deliveryInfo = deliveryInfoMapper.toEntity(deliveryInfoDTO);
        deliveryInfo = deliveryInfoRepository.save(deliveryInfo);
        DeliveryInfoDTO result = deliveryInfoMapper.toDto(deliveryInfo);
        deliveryInfoSearchRepository.save(deliveryInfo);
        return result;
    }

    /**
     * Get all the deliveryInfos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DeliveryInfoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DeliveryInfos");
        return deliveryInfoRepository.findAll(pageable)
            .map(deliveryInfoMapper::toDto);
    }


    /**
     * Get one deliveryInfo by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DeliveryInfoDTO> findOne(Long id) {
        log.debug("Request to get DeliveryInfo : {}", id);
        return deliveryInfoRepository.findById(id)
            .map(deliveryInfoMapper::toDto);
    }

    /**
     * Delete the deliveryInfo by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DeliveryInfo : {}", id);        deliveryInfoRepository.deleteById(id);
        deliveryInfoSearchRepository.deleteById(id);
    }

    /**
     * Search for the deliveryInfo corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DeliveryInfoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DeliveryInfos for query {}", query);
        return deliveryInfoSearchRepository.search(queryStringQuery(query), pageable)
            .map(deliveryInfoMapper::toDto);
    }
}
