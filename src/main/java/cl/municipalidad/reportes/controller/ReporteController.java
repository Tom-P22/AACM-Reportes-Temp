package cl.municipalidad.reportes.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.municipalidad.reportes.dto.request.DtoGenerarReporteRequest;
import cl.municipalidad.reportes.dto.response.DtoReporteResponse;
import cl.municipalidad.reportes.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reportes")
@RequiredArgsConstructor
@Tag(name = "Motor de Reportes y Auditoría", description = "Endpoints analíticos para la recopilación síncrona inter-servicios de recaudación e indicadores")
public class ReporteController {

    private final ReporteService reporteService;

    @PostMapping("/generar")
    @Operation(
        summary = "Generar balance analítico consolidado", 
        description = "Consulta síncronamente los montos de ms-pagos e indicadores de ms-reservas para guardar y devolver una auditoría completa.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Historial analítico guardado y compilado con éxito",
            content = @Content(
                mediaType = "application/json", 
                schema = @Schema(implementation = DtoReporteResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Inconsistencia en los rangos de fechas u obligatorios"
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Token JWT inválido o ausente"
        )
    })
    public ResponseEntity<DtoReporteResponse> crearBalanceAnalitico(
            @Valid @RequestBody DtoGenerarReporteRequest request,
            @Parameter(hidden = true) @RequestHeader("Authorization") String token) {
        
        DtoReporteResponse response = reporteService.procesarYGuardarReporte(request, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}