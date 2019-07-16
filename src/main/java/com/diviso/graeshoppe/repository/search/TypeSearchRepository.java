package com.diviso.graeshoppe.repository.search;

import com.diviso.graeshoppe.domain.Type;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Type entity.
 */
public interface TypeSearchRepository extends ElasticsearchRepository<Type, Long> {
}
