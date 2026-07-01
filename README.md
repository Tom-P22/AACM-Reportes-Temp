# 📊 ms-reportes | Módulo de Inteligencia de Negocios y Analítica Municipal

Este microservicio actúa como el **componente centralizador de analítica y reportabilidad** del ecosistema distribuido. Su propósito estratégico es recolectar, consolidar y persistir métricas clave provenientes de múltiples contextos delimitados para la toma de decisiones del personal administrativo.

---

## 🛠️ Stack Tecnológico Core
* **Lenguaje:** Java 21 LTS
* **Framework:** Spring Boot 4.0.6 (o 3.x estable según entorno local)
* **Gestor de Dependencias:** Maven
* **Persistencia:** Spring Data JPA / Hibernate 7.x
* **Base de Datos:** MySQL (Puerto de red 3307 gestionado vía Docker)
* **Librerías Adicionales:** Spring Boot Starter Validation, Spring Reactive Web (WebClient), Lombok

---

## 📂 Árbol de Directorios (Simetría ms-pagos)
```text
ms-reportes/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── cl/
│   │   │       └── municipalidad/
│   │   │           └── reportes/
│   │   │               ├── client/
│   │   │               │   └── AnaliticaClient.java
│   │   │               ├── config/
│   │   │               │   └── WebClientConfig.java
│   │   │               ├── controller/
│   │   │               │   └── ReporteController.java
│   │   │               ├── dto/
│   │   │               │   ├── request/
│   │   │               │   │   └── DtoGenerarReporteRequest.java
│   │   │               │   └── response/
│   │   │               │       └── DtoReporteResponse.java
│   │   │               ├── exception/
│   │   │               │   └── GlobalExceptionHandler.java
│   │   │               ├── model/
│   │   │               │   └── ReporteModel.java
│   │   │               ├── repository/
│   │   │               │   └── ReporteRepository.java
│   │   │               ├── service/
│   │   │               │   └── ReporteService.java
│   │   │               └── ReportesApplication.java
│   │   └── resources/
│   │       └── application.properties
└── pom.xml
🚀 Orquestación y Consumo Multicliente por Red
Este módulo corre de forma exclusiva en el puerto 8087 y se comporta como un servicio orquestador síncrono no bloqueante que consume las APIs de analítica distribuidas en el ecosistema.

Generar Balance Consolidado
URL: POST http://localhost:8087/api/v1/reportes/generar

Headers: Content-Type: application/json

Cuerpo de la Petición (JSON Request):

JSON
{
    "tipoReporte": "Financiero",
    "fechaInicio": "2026-05-01",
    "fechaFin": "2026-05-31",
    "generadoPor": "Admin_Chelo"
}
Cuerpo de la Respuesta (200 OK):

JSON
{
    "idReporte": 1,
    "tipoReporte": "FINANCIERO",
    "desde": "2026-05-01",
    "hasta": "2026-05-31",
    "totalRecaudado": 0.0,
    "totalReservas": 0,
    "canchaEstrella": "Complejo Principal - Cancha de Fútbol 11",
    "fechaGeneracion": "2026-05-18T17:07:08.3473253",
    "operadorResponsable": "Admin_Chelo"
}