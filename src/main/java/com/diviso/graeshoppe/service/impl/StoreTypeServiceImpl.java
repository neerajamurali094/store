package com.diviso.graeshoppe.service.impl;

import com.diviso.graeshoppe.service.StoreTypeService;
import com.diviso.graeshoppe.domain.StoreType;
import com.diviso.graeshoppe.repository.StoreTypeRepository;
import com.diviso.graeshoppe.repository.search.StoreTypeSearchRepository;
import com.diviso.graeshoppe.service.dto.StoreTypeDTO;
import com.diviso.graeshoppe.service.mapper.StoreTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing StoreType.
 */
@Service
@Transactional
public class StoreTypeServiceImpl implements StoreTypeService {

    private final Logger log = LoggerFactory.getLogger(StoreTypeServiceImpl.class);

    private final StoreTypeRepository storeTypeRepository;

    private final StoreTypeMapper storeTypeMapper;

    private final StoreTypeSearchRepository storeTypeSearchRepository;

    public StoreTypeServiceImpl(StoreTypeRepository storeTypeRepository, StoreTypeMapper storeTypeMapper, StoreTypeSearchRepository storeTypeSearchRepository) {
        this.storeTypeRepository = storeTypeRepository;
        this.storeTypeMapper = storeTypeMapper;
        this.storeTypeSearchRepository = storeTypeSearchRepository;
    }

    /**
     * Save a storeType.
     *
     * @param storeTypeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StoreTypeDTO save(StoreTypeDTO storeTypeDTO) {
        log.debug("Request to save StoreType : {}", storeTypeDTO);
        StoreType storeType = storeTypeMapper.toEntity(storeTypeDTO);
        storeType = storeTypeRepository.save(storeType);
        StoreTypeDTO result1 = storeTypeMapper.toDto(storeType);
        storeTypeSearchRepository.save(storeType);
        

        storeType = storeTypeMapper.toEntity(result1);
        storeType = storeTypeRepository.save(storeType);
        StoreTypeDTO result = storeTypeMapper.toDto(storeType);
        storeTypeSearchRepository.save(storeType);
        return result;
    }

    /**
     * Get all the storeTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StoreTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StoreTypes");
        return storeTypeRepository.findAll(pageable)
            .map(storeTypeMapper::toDto);
    }


    /**
     * Get one storeType by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<StoreTypeDTO> findOne(Long id) {
        log.debug("Request to get StoreType : {}", id);
        return storeTypeRepository.findById(id)
            .map(storeTypeMapper::toDto);
    }

    /**
     * Delete the storeType by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StoreType : {}", id);        storeTypeRepository.deleteById(id);
        storeTypeSearchRepository.deleteById(id);
    }

    /**
     * Search for the storeType corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StoreTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of StoreTypes for query {}", query);
        return storeTypeSearchRepository.search(queryStringQuery(query), pageable)
            .map(storeTypeMapper::toDto);
    }
}
