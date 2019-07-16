package com.diviso.graeshoppe.repository.search;

import com.diviso.graeshoppe.domain.Propreitor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Propreitor entity.
 */
public interface PropreitorSearchRepository extends ElasticsearchRepository<Propreitor, Long> {
}
