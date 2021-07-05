package com.vet24.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.test.web.client.MockMvcClientHttpRequestFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@Profile("TestProfile")
@Configuration
public class TestConfig {

    @Bean
    @Primary
    @Scope("prototype")
    public TestRestTemplate anotherRestTemplate(@Autowired MockMvc mockMvc, @Autowired RestTemplateBuilder restTemplateBuilder) {
        MockMvcClientHttpRequestFactory requestFactory = new MockMvcClientHttpRequestFactory(mockMvc);
        return new TestRestTemplate(restTemplateBuilder.requestFactory(() -> requestFactory));
    }

    @Bean
    @Primary
    public MockMvc sharedSessionMockMvc(@Autowired WebApplicationContext wac) {
        return MockMvcBuilders.webAppContextSetup(wac).apply(sharedHttpSession()).build();
    }
}
