package com.diviso.graeshoppe.repository.search;

import com.diviso.graeshoppe.domain.UniqueStoreID;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the UniqueStoreID entity.
 */
public interface UniqueStoreIDSearchRepository extends ElasticsearchRepository<UniqueStoreID, Long> {
}
