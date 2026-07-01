package cl.municipalidad.reportes.repository;

import cl.municipalidad.reportes.model.ReporteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<ReporteModel, Long> {
    // Permite al administrador buscar reportes históricos de un tipo específico (ej: Financieros)
    List<ReporteModel> findByTipoReporte(String tipoReporte);
}
