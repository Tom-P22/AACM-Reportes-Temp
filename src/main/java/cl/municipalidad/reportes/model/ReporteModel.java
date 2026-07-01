package cl.municipalidad.reportes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reportes_generados")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReporteModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipoReporte;

    @Column(nullable = false)
    private LocalDate fechaInicio; // Estandarizado

    @Column(nullable = false)
    private LocalDate fechaFin; // Estandarizado

    @Column(nullable = false)
    private Double totalRecaudado;

    @Column(nullable = false)
    private Integer totalReservas;

    @Column(nullable = false)
    private String canchaEstrella; // Estandarizado con el Response

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaGeneracion;

    @Column(nullable = false)
    private String generadoPor; // Estandarizado

    @PrePersist
    protected void onCreate() {
        this.fechaGeneracion = LocalDateTime.now(); // Se ejecuta automáticamente antes de insertar
    }
}