package com.diviso.graeshoppe.service.impl;

import com.diviso.graeshoppe.service.StoreService;
import com.diviso.graeshoppe.service.UserRatingService;
import com.diviso.graeshoppe.domain.UserRating;
import com.diviso.graeshoppe.repository.UserRatingRepository;
import com.diviso.graeshoppe.repository.search.UserRatingSearchRepository;
import com.diviso.graeshoppe.security.SecurityUtils;
import com.diviso.graeshoppe.service.dto.StoreDTO;
import com.diviso.graeshoppe.service.dto.UserRatingDTO;
import com.diviso.graeshoppe.service.mapper.UserRatingMapper;
import com.diviso.graeshoppe.web.rest.errors.BadRequestAlertException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing UserRating.
 */
@Service
@Transactional
@Deprecated
public class UserRatingServiceImpl implements UserRatingService {

    private final Logger log = LoggerFactory.getLogger(UserRatingServiceImpl.class);

    private final UserRatingRepository userRatingRepository;

    private final UserRatingMapper userRatingMapper;

    private final UserRatingSearchRepository userRatingSearchRepository;

    @Autowired
	StoreService storeService;
    
    public UserRatingServiceImpl(UserRatingRepository userRatingRepository, UserRatingMapper userRatingMapper, UserRatingSearchRepository userRatingSearchRepository) {
        this.userRatingRepository = userRatingRepository;
        this.userRatingMapper = userRatingMapper;
        this.userRatingSearchRepository = userRatingSearchRepository;
    }

    /**
     * Save a userRating.
     *
     * @param userRatingDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public UserRatingDTO save(UserRatingDTO userRatingDTO) {

		Integer fiveCount = 0, fourCount = 0, threeCount = 0, twoCount = 0, oneCount = 0;

        log.debug("Request to save UserRating : {}", userRatingDTO);
        UserRating userRating = userRatingMapper.toEntity(userRatingDTO);
        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        userRating.setUserName(currentUserLogin.get());
        userRating.setRatedOn(ZonedDateTime.now());
        log.info("........................Rating...............IMPL.............");
        
        userRating = userRatingRepository.save(userRating);
        
        UserRatingDTO result = userRatingMapper.toDto(userRating);
        
        userRatingSearchRepository.save(userRating);
        
        StoreDTO storeDTO = storeService.findOne(result.getStoreId()).get();
        
        log.info("..............  "+userRatingDTO.getRating()+"   ..................");
        
		/*if (userRatingDTO.getRating() == 5.0) {*/

			fiveCount = userRatingRepository.getCount(5.0);
			log.debug(">>>>>>>>>>>>>>>>>5>>>>>>>>>>>>>>>>>>" + fiveCount);
	//	}
		/*if (userRatingDTO.getRating() == 4.0) {*/
			fourCount = userRatingRepository.getCount(4.0);
			log.debug(">>>>>>>>>>>>>>>>>4>>>>>>>>>>>>>>>>>>" + fourCount);
		//}
		//if (userRatingDTO.getRating() == 3.0) {
			threeCount = userRatingRepository.getCount(3.0);
			log.debug(">>>>>>>>>>>>>>>>3>>>>>>>>>>>>>" + threeCount);
		//}
		//if (userRatingDTO.getRating() == 2.0) {
			twoCount = userRatingRepository.getCount(2.0);
			log.debug(">>>>>>>>>>>>>>2>>>>>>>>>>>>>>>>>" + twoCount);
		//}
		//if (userRatingDTO.getRating() == 1.0) {
			oneCount = userRatingRepository.getCount(1.0);
			log.debug(">>>>>>>>>>>>>>1>>>>>>>>>>>>>>>>>" + oneCount);
		//}

		double rating = (5.0 * fiveCount + 4.0 * fourCount + 3.0 * threeCount + 2.0 * twoCount + 1.0 * oneCount)
				/ (fiveCount + fourCount + threeCount + twoCount + oneCount);
		BigDecimal ratingDB = new BigDecimal(rating).setScale(3, RoundingMode.HALF_UP);

		log.debug("                  >>>>>>>>" + ratingDB.doubleValue());

		if (storeDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", "store", "idnull");
		}

		storeDTO.setTotalRating(ratingDB.doubleValue());

	    StoreDTO dto = storeService.save(storeDTO);

		log.debug("**************store: ********************" + dto);
        
        return result;
    }

    /**
     * Get all the userRatings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserRatingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserRatings");
        return userRatingRepository.findAll(pageable)
            .map(userRatingMapper::toDto);
    }


    /**
     * Get one userRating by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UserRatingDTO> findOne(Long id) {
        log.debug("Request to get UserRating : {}", id);
        
        return userRatingRepository.findById(id)
            .map(userRatingMapper::toDto);
    }

    /**
     * Delete the userRating by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserRating : {}", id);
        userRatingRepository.deleteById(id);
        userRatingSearchRepository.deleteById(id);
    }

    /**
     * Search for the userRating corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserRatingDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UserRatings for query {}", query);
        return userRatingSearchRepository.search(queryStringQuery(query), pageable)
            .map(userRatingMapper::toDto);
    }

	/* (non-Javadoc)
	 * @see com.diviso.graeshoppe.service.UserRatingService#getCount(java.lang.Integer)
	 */
	@Override
	public int getCount(Double rating) {
		return userRatingRepository.getCount(rating);
	}
}
