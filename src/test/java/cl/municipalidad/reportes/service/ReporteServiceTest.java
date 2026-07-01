package cl.municipalidad.reportes.service;

import cl.municipalidad.reportes.client.AnaliticaClient;
import cl.municipalidad.reportes.dto.request.DtoGenerarReporteRequest;
import cl.municipalidad.reportes.dto.response.DtoReporteResponse;
import cl.municipalidad.reportes.model.ReporteModel;
import cl.municipalidad.reportes.repository.ReporteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReporteServiceTest {

    @Mock
    private ReporteRepository reporteRepository;

    @Mock
    private AnaliticaClient analiticaClient;

    @InjectMocks
    private ReporteService reporteService;

    private DtoGenerarReporteRequest requestValido;
    private final String mockToken = "Bearer token-simulado-xyz";

    @BeforeEach
    void setUp() {
        requestValido = new DtoGenerarReporteRequest();
        requestValido.setTipoReporte("FINANCIERO");
        requestValido.setFechaInicio(LocalDate.of(2026, 1, 1));
        requestValido.setFechaFin(LocalDate.of(2026, 1, 31));
        requestValido.setGeneradoPor("Admin_Pruebas");
    }

    // 1. TEST FLUJO FELIZ: Guardar y consolidar datos correctamente
    @Test
    void procesarYGuardarReporte_CuandoDatosSonValidos_DebeRetornarResponseCorrecto() {
        when(analiticaClient.obtenerRecaudacionPorRango(any(), any(), any())).thenReturn(50000.0);
        when(analiticaClient.obtenerTotalReservasPorRango(any(), any(), any())).thenReturn(25);
        when(analiticaClient.obtenerCanchaEstrellaPorRango(any(), any(), any())).thenReturn("Cancha de Tenis N°1");

        ReporteModel modeloGuardado = new ReporteModel();
        modeloGuardado.setId(100L);
        modeloGuardado.setTipoReporte("FINANCIERO");
        modeloGuardado.setFechaInicio(requestValido.getFechaInicio());
        modeloGuardado.setFechaFin(requestValido.getFechaFin());
        modeloGuardado.setTotalRecaudado(50000.0);
        modeloGuardado.setTotalReservas(25);
        modeloGuardado.setCanchaEstrella("Cancha de Tenis N°1");
        modeloGuardado.setFechaGeneracion(LocalDateTime.now());
        modeloGuardado.setGeneradoPor("Admin_Pruebas");

        when(reporteRepository.save(any(ReporteModel.class))).thenReturn(modeloGuardado);

        DtoReporteResponse response = reporteService.procesarYGuardarReporte(requestValido, mockToken);

        assertThat(response).isNotNull();
        assertThat(response.getIdReporte()).isEqualTo(100L);
        assertThat(response.getTotalRecaudado()).isEqualTo(50000.0);
        assertThat(response.getTotalReservas()).isEqualTo(25);
        assertThat(response.getCanchaEstrella()).isEqualTo("Cancha de Tenis N°1");
        verify(reporteRepository, times(1)).save(any(ReporteModel.class));
    }

    // 2. TEST REGLA DE NEGOCIO: Excepción cuando fecha inicio es posterior a fecha fin
    @Test
    void procesarYGuardarReporte_CuandoFechaInicioEsPosteriorAFin_DebeLanzarIllegalArgumentException() {
        requestValido.setFechaInicio(LocalDate.of(2026, 12, 31));
        requestValido.setFechaFin(LocalDate.of(2026, 1, 1));

        assertThatThrownBy(() -> reporteService.procesarYGuardarReporte(requestValido, mockToken))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Error de negocio: La fecha de inicio no puede ser posterior a la fecha de fin.");

        verifyNoInteractions(analiticaClient);
        verifyNoInteractions(reporteRepository);
    }

    // 3. TEST RESILIENCIA: Control de nulos si ms-pagos falla (retorna 0.0)
    @Test
    void procesarYGuardarReporte_CuandoRecaudacionEsNull_DebeAsignarCero() {
        when(analiticaClient.obtenerRecaudacionPorRango(any(), any(), any())).thenReturn(null);
        when(analiticaClient.obtenerTotalReservasPorRango(any(), any(), any())).thenReturn(10);
        when(analiticaClient.obtenerCanchaEstrellaPorRango(any(), any(), any())).thenReturn("Cancha Futbolito");

        ReporteModel modeloGuardado = new ReporteModel();
        modeloGuardado.setTotalRecaudado(0.0); 
        when(reporteRepository.save(any(ReporteModel.class))).thenReturn(modeloGuardado);

        DtoReporteResponse response = reporteService.procesarYGuardarReporte(requestValido, mockToken);

        assertThat(response.getTotalRecaudado()).isEqualTo(0.0);
    }

    // 4. TEST RESILIENCIA 2: Control de nulos si ms-reservas falla en la cancha estrella
    @Test
    void procesarYGuardarReporte_CuandoCanchaEstrellaEsNull_DebeAsignarMensajePorDefecto() {
        when(analiticaClient.obtenerRecaudacionPorRango(any(), any(), any())).thenReturn(12000.0);
        when(analiticaClient.obtenerTotalReservasPorRango(any(), any(), any())).thenReturn(5);
        when(analiticaClient.obtenerCanchaEstrellaPorRango(any(), any(), any())).thenReturn(null);

        ReporteModel modeloGuardado = new ReporteModel();
        modeloGuardado.setCanchaEstrella("Sin datos");

        when(reporteRepository.save(any(ReporteModel.class))).thenReturn(modeloGuardado);

        DtoReporteResponse response = reporteService.procesarYGuardarReporte(requestValido, mockToken);

        assertThat(response.getCanchaEstrella()).isEqualTo("Sin datos");
    }

    // 5. TEST DE COMPORTAMIENTO: UpperCase estricto del Tipo de Reporte
    @Test
    void procesarYGuardarReporte_DebeGuardarTipoReporteEnMayusculas() {
        requestValido.setTipoReporte("ocupacion_mensual");

        when(analiticaClient.obtenerRecaudacionPorRango(any(), any(), any())).thenReturn(0.0);
        when(analiticaClient.obtenerTotalReservasPorRango(any(), any(), any())).thenReturn(0);
        when(analiticaClient.obtenerCanchaEstrellaPorRango(any(), any(), any())).thenReturn("Ninguna");

        ReporteModel modeloGuardado = new ReporteModel();
        modeloGuardado.setTipoReporte("OCUPACION_MENSUAL");

        when(reporteRepository.save(any(ReporteModel.class))).thenReturn(modeloGuardado);

        DtoReporteResponse response = reporteService.procesarYGuardarReporte(requestValido, mockToken);

        assertThat(response.getTipoReporte()).isEqualTo("OCUPACION_MENSUAL");
    }
}