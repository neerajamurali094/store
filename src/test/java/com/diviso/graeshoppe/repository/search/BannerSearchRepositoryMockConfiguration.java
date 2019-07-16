package com.diviso.graeshoppe.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of BannerSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class BannerSearchRepositoryMockConfiguration {

    @MockBean
    private BannerSearchRepository mockBannerSearchRepository;

}
