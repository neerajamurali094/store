package com.diviso.graeshoppe.repository.search;

import com.diviso.graeshoppe.domain.Store;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Store entity.
 */
public interface StoreSearchRepository extends ElasticsearchRepository<Store, Long> {
}
