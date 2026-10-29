# Portal Transaccional API

API REST desarrollada bajo principios de **Arquitectura Hexagonal (Clean Architecture)** y **Seguridad Zero Trust**, diseñada para integrar canales digitales con un sistema core legacy basado en SOAP, mitigando vulnerabilidades críticas como IDOR.

---

## Guía de Ejecución Rápida

### Requisitos Previos
* Java 17 o superior.
* Gradle (o usar el wrapper `./gradlew` incluido en el proyecto).

### Configuración de Propiedades y Secretos
Las configuraciones de seguridad (como la clave secreta del JWT) se encuentran externalizadas en el archivo `src/main/resources/application.properties`:
```properties
jwt.secret=SecurePortalEnterpriseSecretKeyValidation2026SecurityStandardWithEnoughLengthForHS256
server.port=8080
```
## Estructrura de Carpetas
```text 
┌────────────────────────────────────────────────────────┐
│                        DOMINIO                         │
│  • domain/model/     -> Entidades y objetos de negocio │
│  • domain/port/      -> Interfaces (puertos)           │
└───────────────────────────┬────────────────────────────┘
                            │
                            ▼
┌────────────────────────────────────────────────────────┐
│                      APLICACIÓN                        │
│  • application/      -> Casos de uso y orquestación    │
└───────────────────────────┬────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────┐
│                   INFRAESTRUCTURA                       │
│  • infrastructure/in/controller/-> Controladores REST   │
│  • infrastructure/in/dto/-> objetos del sistema         │
│  • infrastructure/in/exeption/->Manejo Global de Errores│
│  • infrastructure/security/  -> Filtros JWT / Zero Trust│
│  • infrastructure/adapter/   -> Adaptador SOAP / Logger │
│  • infrastructure/config/    -> Configuraciones         │
│  • infrastructure/out/soap  ->   Consumos externos      │
│  • infrastructure/out/logging->  Consumos de logs       │
└─────────────────────────────────────────────────────────┘
```
## Manejo Global de Excepciones
El sistema cuenta con un interceptor global (`@ControllerAdvice`) ubicado en `infrastructure/exception/`. Esto permite:
* Capturar excepciones personalizadas del negocio y errores no controlados.
* Retornar contratos de error uniformes y limpios (con códigos de estado HTTP correctos, mensaje descriptivo y marca de tiempo).
* Evitar la exposición de trazas de error (*stack traces*) hacia el cliente externo.
## Compilar y Ejecutar la Aplicación

Abre una terminal en la raíz del proyecto y ejecuta:

- Linux / macOS:

```bash
    ./gradlew bootRun    
```
- Windows:

```bash
    gradlew.bat bootRun
```
La aplicación se ejecutará por defecto en http://localhost:8080.

## Ejecución de Pruebas Automatizadas
Para correr la suite de pruebas unitarias y verificar el cumplimiento de las reglas de negocio:

```bash
./gradlew test
```
## Decisiones de Arquitectura y Limitaciones
### Decisiones

- **Arquitectura Hexagonal:** Separación estricta entre domain (negocio puro), application (casos de uso) e infrastructure (adaptadores HTTP/SOAP).

- **Seguridad Zero Trust :** Validación cruzada en la capa de entrada entre los claims del JWT firmado (sub y tipoDocumento) y los parámetros del request, respondiendo 403 Forbidden ante manipulaciones.

- **Resiliencia:** Encapsulamiento del conector SOAP en una Anti-Corruption Layer (ACL) con soporte para patrones de resiliencia ante alta latencia.

### Limitaciones Conocidas

- Simulación Core SOAP: Para efectos del alcance de la prueba, la llamada al SOAP legacy está integrada mediante un conector/stub simulado que mapea las respuestas XML al modelo de dominio.
## Ejemplo del consumo 

GET: /api/v1/ordenes?tipoDocumento=CC&numeroDocumento=123456

Header: Authorization: Bearer <JWT_VALIDO>

Respuesta Exitosa (200 OK):

```json
{
  "ordenes": [
    {
      "id": "ORD-10001",
      "estado": "PROGRAMADA",
      "fecha": "2026-10-26"
    }
  ]
}
```

## Diagramas

```mermaid
┌─────────────────────────┐
│      Cliente HTTP       │
└────────────┬────────────┘
             │ GET /api/v1/ordenes
             ▼
┌─────────────────────────┐
│    OrdenesController    │
│  (Validación JWT / IDOR)│
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│ ConsultarOrdenesUseCase │
│     (Puerto CorePort)   │
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│     SoapXmlAdapter      │
└────────────┬────────────┘
             │ XML / SOAP
             ▼
┌─────────────────────────┐
│   Legacy SOAP System    │
└─────────────────────────┘
```

