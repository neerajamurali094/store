package com.diviso.graeshoppe.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of PreOrderSettingsSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PreOrderSettingsSearchRepositoryMockConfiguration {

    @MockBean
    private PreOrderSettingsSearchRepository mockPreOrderSettingsSearchRepository;

}
