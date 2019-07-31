package com.diviso.graeshoppe.repository.search;

import com.diviso.graeshoppe.domain.StoreType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the StoreType entity.
 */
public interface StoreTypeSearchRepository extends ElasticsearchRepository<StoreType, Long> {
}
