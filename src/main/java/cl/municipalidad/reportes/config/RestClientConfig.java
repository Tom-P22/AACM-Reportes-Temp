package cl.municipalidad.reportes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    private static final String GATEWAY_SECRET = "ClaveUltraSecretaEInviolableParaLaMunicipalidad2026!";

    @Bean(name = "restClientPagos")
    public RestClient restClientPagos() {
        return RestClient.builder()
                .baseUrl("http://localhost:8088")
                .requestInterceptor(new JwtInterceptor())
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("X-Gateway-Secret", GATEWAY_SECRET)
                .build();
    }

    @Bean(name = "restClientReservas")
    public RestClient restClientReservas() {
        return RestClient.builder()
                .baseUrl("http://localhost:8084")
                .requestInterceptor(new JwtInterceptor())
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("X-Gateway-Secret", GATEWAY_SECRET)
                .build();
    }
}