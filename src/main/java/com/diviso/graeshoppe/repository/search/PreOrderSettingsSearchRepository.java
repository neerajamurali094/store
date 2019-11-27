package com.diviso.graeshoppe.repository.search;

import com.diviso.graeshoppe.domain.PreOrderSettings;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PreOrderSettings entity.
 */
public interface PreOrderSettingsSearchRepository extends ElasticsearchRepository<PreOrderSettings, Long> {
}
