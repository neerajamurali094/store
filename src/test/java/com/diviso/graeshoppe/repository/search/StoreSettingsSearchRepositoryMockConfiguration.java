package com.diviso.graeshoppe.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of StoreSettingsSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class StoreSettingsSearchRepositoryMockConfiguration {

    @MockBean
    private StoreSettingsSearchRepository mockStoreSettingsSearchRepository;

}
