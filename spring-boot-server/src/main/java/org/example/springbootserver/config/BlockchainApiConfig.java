package org.example.springbootserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BlockchainApiConfig {

    @Value("${spring.baseUrl.BC_SERVER_URL}")
    private String BC_SERVER_URL;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.rootUri(BC_SERVER_URL)
                .build();
    }

}
