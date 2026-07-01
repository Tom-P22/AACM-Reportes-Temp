package cl.municipalidad.reportes.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.municipalidad.reportes.dto.request.DtoGenerarReporteRequest;
import cl.municipalidad.reportes.dto.response.DtoReporteResponse;
import cl.municipalidad.reportes.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reportes")
@RequiredArgsConstructor
@Tag(name = "Motor de Reportes y Auditoría", description = "Endpoints analíticos para la recopilación síncrona inter-servicios de recaudación e indicadores")
public class ReporteController {

    private final ReporteService reporteService;

    @Operation(summary = "Generar balance analítico consolidado", description = "Consulta síncronamente los montos de ms-pagos e indicadores de ms-reservas para guardar y devolver una auditoría completa.")
    @ApiResponse(responseCode = "201", description = "Historial analítico guardado y compilado con éxito")
    @ApiResponse(responseCode = "400", description = "Inconsistencia en los rangos de fechas u obligatorios")
    @ApiResponse(responseCode = "401", description = "Token JWT inválido o ausente")
    @PostMapping("/generar")
    public ResponseEntity<DtoReporteResponse> crearBalanceAnalitico(
            @Valid @RequestBody DtoGenerarReporteRequest request,
            @RequestHeader("Authorization") String token) {
        
        DtoReporteResponse response = reporteService.procesarYGuardarReporte(request, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}