package com.diviso.graeshoppe.repository.search;

import com.diviso.graeshoppe.domain.UserRating;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the UserRating entity.
 */
public interface UserRatingSearchRepository extends ElasticsearchRepository<UserRating, Long> {
}
