package com.diviso.graeshoppe.service.impl;

import com.diviso.graeshoppe.service.PropreitorService;
import com.diviso.graeshoppe.domain.Propreitor;
import com.diviso.graeshoppe.repository.PropreitorRepository;
import com.diviso.graeshoppe.repository.search.PropreitorSearchRepository;
import com.diviso.graeshoppe.service.dto.PropreitorDTO;
import com.diviso.graeshoppe.service.mapper.PropreitorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Propreitor.
 */
@Service
@Transactional
public class PropreitorServiceImpl implements PropreitorService {

    private final Logger log = LoggerFactory.getLogger(PropreitorServiceImpl.class);

    private final PropreitorRepository propreitorRepository;

    private final PropreitorMapper propreitorMapper;

    private final PropreitorSearchRepository propreitorSearchRepository;

    public PropreitorServiceImpl(PropreitorRepository propreitorRepository, PropreitorMapper propreitorMapper, PropreitorSearchRepository propreitorSearchRepository) {
        this.propreitorRepository = propreitorRepository;
        this.propreitorMapper = propreitorMapper;
        this.propreitorSearchRepository = propreitorSearchRepository;
    }

    /**
     * Save a propreitor.
     *
     * @param propreitorDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PropreitorDTO save(PropreitorDTO propreitorDTO) {
        log.debug("Request to save Propreitor : {}", propreitorDTO);
        Propreitor propreitor = propreitorMapper.toEntity(propreitorDTO);
        propreitor = propreitorRepository.save(propreitor);
        PropreitorDTO result = propreitorMapper.toDto(propreitor);
        propreitorSearchRepository.save(propreitor);
        return updateToEs(result);
    }
    
    private PropreitorDTO updateToEs(PropreitorDTO propreitorDTO) {
        log.debug("Request to save Propreitor : {}", propreitorDTO);
        Propreitor propreitor = propreitorMapper.toEntity(propreitorDTO);
        propreitor = propreitorRepository.save(propreitor);
        PropreitorDTO result= propreitorMapper.toDto(propreitor);
        propreitorSearchRepository.save(propreitor);
        return updateToEs(result);
    }

    /**
     * Get all the propreitors.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PropreitorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Propreitors");
        return propreitorRepository.findAll(pageable)
            .map(propreitorMapper::toDto);
    }


    /**
     * Get one propreitor by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PropreitorDTO> findOne(Long id) {
        log.debug("Request to get Propreitor : {}", id);
        return propreitorRepository.findById(id)
            .map(propreitorMapper::toDto);
    }

    /**
     * Delete the propreitor by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Propreitor : {}", id);        propreitorRepository.deleteById(id);
        propreitorSearchRepository.deleteById(id);
    }

    /**
     * Search for the propreitor corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PropreitorDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Propreitors for query {}", query);
        return propreitorSearchRepository.search(queryStringQuery(query), pageable)
            .map(propreitorMapper::toDto);
    }
}
