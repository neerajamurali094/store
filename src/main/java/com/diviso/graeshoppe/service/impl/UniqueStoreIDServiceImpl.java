package com.diviso.graeshoppe.service.impl;

import com.diviso.graeshoppe.service.UniqueStoreIDService;
import com.diviso.graeshoppe.domain.UniqueStoreID;
import com.diviso.graeshoppe.repository.UniqueStoreIDRepository;
import com.diviso.graeshoppe.repository.search.UniqueStoreIDSearchRepository;
import com.diviso.graeshoppe.service.dto.UniqueStoreIDDTO;
import com.diviso.graeshoppe.service.mapper.UniqueStoreIDMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing UniqueStoreID.
 */
@Service
@Transactional
public class UniqueStoreIDServiceImpl implements UniqueStoreIDService {

    private final Logger log = LoggerFactory.getLogger(UniqueStoreIDServiceImpl.class);

    private final UniqueStoreIDRepository uniqueStoreIDRepository;

    private final UniqueStoreIDMapper uniqueStoreIDMapper;

    private final UniqueStoreIDSearchRepository uniqueStoreIDSearchRepository;

    public UniqueStoreIDServiceImpl(UniqueStoreIDRepository uniqueStoreIDRepository, UniqueStoreIDMapper uniqueStoreIDMapper, UniqueStoreIDSearchRepository uniqueStoreIDSearchRepository) {
        this.uniqueStoreIDRepository = uniqueStoreIDRepository;
        this.uniqueStoreIDMapper = uniqueStoreIDMapper;
        this.uniqueStoreIDSearchRepository = uniqueStoreIDSearchRepository;
    }

    /**
     * Save a uniqueStoreID.
     *
     * @param uniqueStoreIDDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public UniqueStoreIDDTO save(UniqueStoreIDDTO uniqueStoreIDDTO) {
        log.debug("Request to save UniqueStoreID : {}", uniqueStoreIDDTO);
        UniqueStoreID uniqueStoreID = uniqueStoreIDMapper.toEntity(uniqueStoreIDDTO);
        uniqueStoreID = uniqueStoreIDRepository.save(uniqueStoreID);
        UniqueStoreIDDTO result = uniqueStoreIDMapper.toDto(uniqueStoreID);
        uniqueStoreIDSearchRepository.save(uniqueStoreID);
        return result;
    }

    /**
     * Get all the uniqueStoreIDS.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UniqueStoreIDDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UniqueStoreIDS");
        return uniqueStoreIDRepository.findAll(pageable)
            .map(uniqueStoreIDMapper::toDto);
    }


    /**
     * Get one uniqueStoreID by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UniqueStoreIDDTO> findOne(Long id) {
        log.debug("Request to get UniqueStoreID : {}", id);
        return uniqueStoreIDRepository.findById(id)
            .map(uniqueStoreIDMapper::toDto);
    }

    /**
     * Delete the uniqueStoreID by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UniqueStoreID : {}", id);        uniqueStoreIDRepository.deleteById(id);
        uniqueStoreIDSearchRepository.deleteById(id);
    }

    /**
     * Search for the uniqueStoreID corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UniqueStoreIDDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UniqueStoreIDS for query {}", query);
        return uniqueStoreIDSearchRepository.search(queryStringQuery(query), pageable)
            .map(uniqueStoreIDMapper::toDto);
    }
}
