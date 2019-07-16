package com.diviso.graeshoppe.repository.search;

import com.diviso.graeshoppe.domain.Review;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Review entity.
 */
public interface ReviewSearchRepository extends ElasticsearchRepository<Review, Long> {
}
