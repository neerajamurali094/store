package com.diviso.graeshoppe.repository.search;

import com.diviso.graeshoppe.domain.StoreAddress;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the StoreAddress entity.
 */
public interface StoreAddressSearchRepository extends ElasticsearchRepository<StoreAddress, Long> {
}
