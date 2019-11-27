package com.diviso.graeshoppe.repository.search;

import com.diviso.graeshoppe.domain.UserRatingReview;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the UserRatingReview entity.
 */
public interface UserRatingReviewSearchRepository extends ElasticsearchRepository<UserRatingReview, Long> {
}
