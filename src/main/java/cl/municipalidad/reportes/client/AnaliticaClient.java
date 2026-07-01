package cl.municipalidad.reportes.client;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AnaliticaClient {

    private final RestClient restClientPagos;
    private final RestClient restClientReservas;

    public AnaliticaClient(
            @Qualifier("restClientPagos") RestClient restClientPagos, 
            @Qualifier("restClientReservas") RestClient restClientReservas) {
        this.restClientPagos = restClientPagos;
        this.restClientReservas = restClientReservas;
    }

    public Double obtenerRecaudacionPorRango(LocalDate inicio, LocalDate fin, String token) {
        String tokenLimpio = token.startsWith("Bearer ") ? token : "Bearer " + token;

        try {
            return restClientPagos.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/v1/pagos/pago/analitica/recaudacion")
                            .queryParam("fechaInicio", inicio)
                            .queryParam("fechaFin", fin)
                            .build())
                    .header("Authorization", tokenLimpio)
                    .retrieve()
                    .body(Double.class);
        } catch (Exception e) {
            System.err.println("[Error] MS-PAGOS: " + e.getMessage());
            return 0.0;
        }
    }

    public Integer obtenerTotalReservasPorRango(LocalDate inicio, LocalDate fin, String token) {
        String tokenLimpio = token.startsWith("Bearer ") ? token : "Bearer " + token;

        try {
            return restClientReservas.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/v1/reservas/analitica/conteo")
                            .queryParam("fechaInicio", inicio)
                            .queryParam("fechaFin", fin)
                            .build())
                    .header("Authorization", tokenLimpio)
                    .retrieve()
                    .body(Integer.class);
        } catch (Exception e) {
            System.err.println("[ERROR] MS-RESERVAS (Conteo): " + e.getMessage());
            return 0;
        }
    }

    public String obtenerCanchaEstrellaPorRango(LocalDate inicio, LocalDate fin, String token) {
        String tokenLimpio = token.startsWith("Bearer ") ? token : "Bearer " + token;

        try {
            return restClientReservas.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/v1/reservas/analitica/cancha-estrella")
                            .queryParam("fechaInicio", inicio)
                            .queryParam("fechaFin", fin)
                            .build())
                    .header("Authorization", tokenLimpio)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            System.err.println("[ERROR] MS-RESERVAS (Estrella): " + e.getMessage());
            return "Sin datos";
        }
    }
}