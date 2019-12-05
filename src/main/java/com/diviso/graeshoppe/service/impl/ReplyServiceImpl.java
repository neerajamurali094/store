package com.diviso.graeshoppe.service.impl;

import com.diviso.graeshoppe.service.ReplyService;
import com.diviso.graeshoppe.domain.Reply;
import com.diviso.graeshoppe.repository.ReplyRepository;
import com.diviso.graeshoppe.repository.search.ReplySearchRepository;
import com.diviso.graeshoppe.service.dto.ReplyDTO;
import com.diviso.graeshoppe.service.mapper.ReplyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Reply.
 */
@Service
@Transactional
public class ReplyServiceImpl implements ReplyService {

    private final Logger log = LoggerFactory.getLogger(ReplyServiceImpl.class);

    private final ReplyRepository replyRepository;

    private final ReplyMapper replyMapper;

    private final ReplySearchRepository replySearchRepository;

    public ReplyServiceImpl(ReplyRepository replyRepository, ReplyMapper replyMapper, ReplySearchRepository replySearchRepository) {
        this.replyRepository = replyRepository;
        this.replyMapper = replyMapper;
        this.replySearchRepository = replySearchRepository;
    }

    /**
     * Save a reply.
     *
     * @param replyDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ReplyDTO save(ReplyDTO replyDTO) {
        log.debug("Request to save Reply : {}", replyDTO);
        Reply reply = replyMapper.toEntity(replyDTO);
        reply = replyRepository.save(reply);
        ReplyDTO result = replyMapper.toDto(reply);
        replySearchRepository.save(reply);
        return updateToEs(result);
    }
    
    
    private ReplyDTO updateToEs(ReplyDTO replyDTO) {
        log.debug("Request to save Reply : {}", replyDTO);
        Reply reply = replyMapper.toEntity(replyDTO);
        reply = replyRepository.save(reply);
        ReplyDTO result = replyMapper.toDto(reply);
        replySearchRepository.save(reply);
        return result;
    }

    /**
     * Get all the replies.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReplyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Replies");
        return replyRepository.findAll(pageable)
            .map(replyMapper::toDto);
    }


    /**
     * Get one reply by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ReplyDTO> findOne(Long id) {
        log.debug("Request to get Reply : {}", id);
        return replyRepository.findById(id)
            .map(replyMapper::toDto);
    }

    /**
     * Delete the reply by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Reply : {}", id);        replyRepository.deleteById(id);
        replySearchRepository.deleteById(id);
    }

    /**
     * Search for the reply corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReplyDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Replies for query {}", query);
        return replySearchRepository.search(queryStringQuery(query), pageable)
            .map(replyMapper::toDto);
    }
}
