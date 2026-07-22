# Employee Management — Backend

API REST de gestión de empleados con Spring Boot 3, Spring Data JPA (Hibernate) y arquitectura en capas.

## Requisitos

- Java 21+
- Maven Wrapper incluido (`mvnw` / `mvnw.cmd`) — no requiere Maven instalado
- Perfil por defecto: **H2 en memoria** (listo para demo)
- Perfil opcional: **Oracle** (`application-oracle.properties`)

## Puertos

| Servicio | Puerto |
|----------|--------|
| Backend API | `8080` |
| Consola H2 | `http://localhost:8080/h2-console` |

## Cómo levantar (perfil H2 — recomendado para la prueba)

```bash
./mvnw spring-boot:run
```

En Windows:

```bash
mvnw.cmd spring-boot:run
```

La API queda en `http://localhost:8080`.

Al iniciar se cargan automáticamente 3 departamentos y 5 empleados de prueba (`DataLoader`).

### Credenciales H2 Console

- JDBC URL: `jdbc:h2:mem:empleadosdb`
- User: `sa`
- Password: *(vacío)*

## Cómo levantar con Oracle

1. Tener Oracle XE/DB accesible (ej. Docker):

```bash
docker run -d --name oracle-xe -p 1521:1521 -e ORACLE_PWD=Oracle123 gvenzl/oracle-xe:21-slim
```

2. Crear usuario/schema `empleados` / `empleados` (o ajustar `application-oracle.properties`).

3. Ejecutar:

```bash
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=oracle
```

Configuración Oracle por defecto:

- URL: `jdbc:oracle:thin:@localhost:1521/XEPDB1`
- Usuario: `empleados`
- Password: `empleados`
- Dialect: `OracleDialect`
- `ddl-auto=update`

## Endpoints principales

### Empleados

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/empleados` | Listar paginado (`page`, `size`, `sort`, `nombre`, `departamento`) |
| GET | `/api/empleados/{id}` | Obtener uno |
| GET | `/api/empleados/buscar?...` | Alias de búsqueda paginada |
| POST | `/api/empleados` | Crear |
| PUT | `/api/empleados/{id}` | Actualizar |
| DELETE | `/api/empleados/{id}` | Eliminar |

### Departamentos

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/departamentos` | Listar (para el combo del formulario) |

## Arquitectura

```
controller → service → repository
     ↓
   DTOs + Bean Validation
exception → @RestControllerAdvice (400 / 404 / 500)
```

CORS habilitado para `http://localhost:4200`.
