package com.diviso.graeshoppe.service.impl;

import com.diviso.graeshoppe.service.TypeService;
import com.diviso.graeshoppe.domain.Type;
import com.diviso.graeshoppe.repository.TypeRepository;
import com.diviso.graeshoppe.repository.search.TypeSearchRepository;
import com.diviso.graeshoppe.service.dto.TypeDTO;
import com.diviso.graeshoppe.service.mapper.TypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Type.
 */
@Service
@Transactional
public class TypeServiceImpl implements TypeService {

    private final Logger log = LoggerFactory.getLogger(TypeServiceImpl.class);

    private final TypeRepository typeRepository;

    private final TypeMapper typeMapper;

    private final TypeSearchRepository typeSearchRepository;

    public TypeServiceImpl(TypeRepository typeRepository, TypeMapper typeMapper, TypeSearchRepository typeSearchRepository) {
        this.typeRepository = typeRepository;
        this.typeMapper = typeMapper;
        this.typeSearchRepository = typeSearchRepository;
    }

    /**
     * Save a type.
     *
     * @param typeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TypeDTO save(TypeDTO typeDTO) {
        log.debug("Request to save Type : {}", typeDTO);
        Type type = typeMapper.toEntity(typeDTO);
        type = typeRepository.save(type);
        TypeDTO result = typeMapper.toDto(type);
        typeSearchRepository.save(type);
        return updateToEs(result);
    }
    
    private TypeDTO updateToEs(TypeDTO typeDTO) {
        log.debug("Request to save Type : {}", typeDTO);
        Type type = typeMapper.toEntity(typeDTO);
        type = typeRepository.save(type);
        TypeDTO result = typeMapper.toDto(type);
        typeSearchRepository.save(type);
        return updateToEs(result);
    }
    

    /**
     * Get all the types.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Types");
        return typeRepository.findAll(pageable)
            .map(typeMapper::toDto);
    }


    /**
     * Get one type by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TypeDTO> findOne(Long id) {
        log.debug("Request to get Type : {}", id);
        return typeRepository.findById(id)
            .map(typeMapper::toDto);
    }

    /**
     * Delete the type by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Type : {}", id);
        typeRepository.deleteById(id);
        typeSearchRepository.deleteById(id);
    }

    /**
     * Search for the type corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Types for query {}", query);
        return typeSearchRepository.search(queryStringQuery(query), pageable)
            .map(typeMapper::toDto);
    }
}
