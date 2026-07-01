CREATE TABLE reportes_generados (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_reporte VARCHAR(100) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    total_recaudado DOUBLE NOT NULL,
    total_reservas INT NOT NULL,
    cancha_estrella VARCHAR(255) NOT NULL,
    fecha_generacion DATETIME NOT NULL,
    generado_por VARCHAR(100) NOT NULL
);