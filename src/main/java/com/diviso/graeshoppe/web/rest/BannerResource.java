package com.diviso.graeshoppe.web.rest;
import com.diviso.graeshoppe.domain.Banner;
import com.diviso.graeshoppe.domain.StoreType;
import com.diviso.graeshoppe.service.BannerService;
import com.diviso.graeshoppe.web.rest.errors.BadRequestAlertException;
import com.diviso.graeshoppe.web.rest.util.HeaderUtil;
import com.diviso.graeshoppe.web.rest.util.PaginationUtil;
import com.diviso.graeshoppe.service.dto.BannerDTO;
import com.diviso.graeshoppe.service.dto.DeliveryInfoDTO;
import com.diviso.graeshoppe.service.dto.StoreTypeDTO;
import com.diviso.graeshoppe.service.mapper.BannerMapper;

import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Banner.
 */
@RestController
@RequestMapping("/api")
public class BannerResource {

    private final Logger log = LoggerFactory.getLogger(BannerResource.class);

    private static final String ENTITY_NAME = "storeBanner";

    private final BannerService bannerService;
    
    @Autowired
    private BannerMapper bannerMapper;

    public BannerResource(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    /**
     * POST  /banners : Create a new banner.
     *
     * @param bannerDTO the bannerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bannerDTO, or with status 400 (Bad Request) if the banner has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/banners")
    public ResponseEntity<BannerDTO> createBanner(@RequestBody BannerDTO bannerDTO) throws URISyntaxException {
        log.debug("REST request to save Banner : {}", bannerDTO);
        if (bannerDTO.getId() != null) {
            throw new BadRequestAlertException("A new banner cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BannerDTO result1 = bannerService.save(bannerDTO);
		if (result1.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		BannerDTO result = bannerService.save(result1);
        return ResponseEntity.created(new URI("/api/banners/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /banners : Updates an existing banner.
     *
     * @param bannerDTO the bannerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bannerDTO,
     * or with status 400 (Bad Request) if the bannerDTO is not valid,
     * or with status 500 (Internal Server Error) if the bannerDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/banners")
    public ResponseEntity<BannerDTO> updateBanner(@RequestBody BannerDTO bannerDTO) throws URISyntaxException {
        log.debug("REST request to update Banner : {}", bannerDTO);
        if (bannerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BannerDTO result = bannerService.save(bannerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bannerDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /banners : get all the banners.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of banners in body
     */
    @GetMapping("/banners")
    public ResponseEntity<List<BannerDTO>> getAllBanners(Pageable pageable) {
        log.debug("REST request to get a page of Banners");
        Page<BannerDTO> page = bannerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/banners");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /banners/:id : get the "id" banner.
     *
     * @param id the id of the bannerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bannerDTO, or with status 404 (Not Found)
     */
    @GetMapping("/banners/{id}")
    public ResponseEntity<BannerDTO> getBanner(@PathVariable Long id) {
        log.debug("REST request to get Banner : {}", id);
        Optional<BannerDTO> bannerDTO = bannerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bannerDTO);
    }

    /**
     * DELETE  /banners/:id : delete the "id" banner.
     *
     * @param id the id of the bannerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/banners/{id}")
    public ResponseEntity<Void> deleteBanner(@PathVariable Long id) {
        log.debug("REST request to delete Banner : {}", id);
        bannerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/banners?query=:query : search for the banner corresponding
     * to the query.
     *
     * @param query the query of the banner search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/banners")
    public ResponseEntity<List<BannerDTO>> searchBanners(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Banners for query {}", query);
        Page<BannerDTO> page = bannerService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/banners");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    
    @PostMapping("/banners/toDto")
	public ResponseEntity<List<BannerDTO>> listToDto(@RequestBody List<Banner> banners) {
		log.debug("REST request to convert to DTO");
		List<BannerDTO> dtos = new ArrayList<>();
		banners.forEach(a -> {
			dtos.add(bannerMapper.toDto(a));
		});
		return ResponseEntity.ok().body(dtos);
	}
}
