package cl.municipalidad.reportes.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Petición administrativa para compilar reportes consolidados y estadísticas")
public class DtoGenerarReporteRequest {

    @NotBlank(message = "El tipo de reporte (FINANCIERO, OCUPACION, etc.) es obligatorio.")
    @Schema(
        description = "Categoría o módulo del reporte del sistema", 
        example = "FINANCIERO", 
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String tipoReporte;

    @NotNull(message = "La fecha de inicio para el rango de analítica es obligatoria.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(
        description = "Fecha inicial del rango de búsqueda", 
        example = "2026-01-01", 
        type = "string", 
        format = "date", 
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin para el rango de analítica es obligatoria.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(
        description = "Fecha límite del rango de búsqueda", 
        example = "2026-01-31", 
        type = "string", 
        format = "date", 
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDate fechaFin;

    @NotBlank(message = "Debe especificar el nombre o ID del administrador que genera el reporte.")
    @Schema(
        description = "Identificador o nombre del funcionario municipal que solicita la operación", 
        example = "Admin_Polideportivo", 
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String generadoPor;
}