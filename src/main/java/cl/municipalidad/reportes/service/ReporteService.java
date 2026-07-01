package cl.municipalidad.reportes.service;

import cl.municipalidad.reportes.client.AnaliticaClient;
import cl.municipalidad.reportes.dto.request.DtoGenerarReporteRequest;
import cl.municipalidad.reportes.dto.response.DtoReporteResponse;
import cl.municipalidad.reportes.model.ReporteModel;
import cl.municipalidad.reportes.repository.ReporteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final ReporteRepository reporteRepository;
    private final AnaliticaClient analiticaClient;

    /**
     * Procesa la analítica consultando a los otros servicios de forma síncrona,
     * guarda el registro histórico y retorna el DTO estandarizado.
     */
    @Transactional // 🗄️ Asegura la integridad transaccional de la persistencia
    public DtoReporteResponse procesarYGuardarReporte(DtoGenerarReporteRequest request, String token) {
        
   
        if (request.getFechaInicio().isAfter(request.getFechaFin())) {
            throw new IllegalArgumentException("Error de negocio: La fecha de inicio no puede ser posterior a la fecha de fin.");
        }

        Double recaudacionTotal = analiticaClient.obtenerRecaudacionPorRango(request.getFechaInicio(), request.getFechaFin(), token);
        Integer cantidadReservas = analiticaClient.obtenerTotalReservasPorRango(request.getFechaInicio(), request.getFechaFin(), token);
        String canchaEstrella = analiticaClient.obtenerCanchaEstrellaPorRango(request.getFechaInicio(), request.getFechaFin(), token);

        ReporteModel modelo = new ReporteModel();
        modelo.setTipoReporte(request.getTipoReporte().toUpperCase());
        modelo.setFechaInicio(request.getFechaInicio());
        modelo.setFechaFin(request.getFechaFin());
        // Manejo de nulos por si los otros MS fallan
        modelo.setTotalRecaudado(recaudacionTotal != null ? recaudacionTotal : 0.0);
        modelo.setTotalReservas(cantidadReservas != null ? cantidadReservas : 0);
        modelo.setCanchaEstrella(canchaEstrella != null ? canchaEstrella : "Sin datos");
        modelo.setGeneradoPor(request.getGeneradoPor()); 

        ReporteModel registroGuardado = reporteRepository.save(modelo);

        DtoReporteResponse response = new DtoReporteResponse();
        response.setIdReporte(registroGuardado.getId());
        response.setTipoReporte(registroGuardado.getTipoReporte());
        response.setFechaInicio(registroGuardado.getFechaInicio());
        response.setFechaFin(registroGuardado.getFechaFin());
        response.setTotalRecaudado(registroGuardado.getTotalRecaudado());
        response.setTotalReservas(registroGuardado.getTotalReservas());
        response.setCanchaEstrella(registroGuardado.getCanchaEstrella());
        response.setFechaGeneracion(registroGuardado.getFechaGeneracion()); 
        response.setGeneradoPor(registroGuardado.getGeneradoPor());

        return response;
    }
}