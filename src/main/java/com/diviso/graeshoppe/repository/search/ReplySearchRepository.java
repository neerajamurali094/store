package com.diviso.graeshoppe.repository.search;

import com.diviso.graeshoppe.domain.Reply;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Reply entity.
 */
public interface ReplySearchRepository extends ElasticsearchRepository<Reply, Long> {
}
