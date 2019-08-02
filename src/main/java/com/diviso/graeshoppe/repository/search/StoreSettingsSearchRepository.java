package com.diviso.graeshoppe.repository.search;

import com.diviso.graeshoppe.domain.StoreSettings;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the StoreSettings entity.
 */
public interface StoreSettingsSearchRepository extends ElasticsearchRepository<StoreSettings, Long> {
}
