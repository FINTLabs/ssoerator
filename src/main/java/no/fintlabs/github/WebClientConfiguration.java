package no.fintlabs.github;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    @Value("${flais.operators.ssoerator.github.username}")
    private String username;
    @Value("${flais.operators.ssoerator.github.token}")
    private String token;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .filter(ExchangeFilterFunctions
                        .basicAuthentication(username, token))
                .baseUrl("https://api.github.com/orgs/fintlabs/packages/container/flais-auth-forward-service/versions")
                .build();
    }
}
