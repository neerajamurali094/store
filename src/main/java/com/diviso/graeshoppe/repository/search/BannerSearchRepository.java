package com.diviso.graeshoppe.repository.search;

import com.diviso.graeshoppe.domain.Banner;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Banner entity.
 */
public interface BannerSearchRepository extends ElasticsearchRepository<Banner, Long> {
}
