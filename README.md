# Employee Management — Backend

API REST de gestión de empleados con Spring Boot 3, Spring Data JPA (Hibernate) y arquitectura en capas.

## Requisitos

| Requisito | Notas |
|-----------|--------|
| Java 21+ | Obligatorio |
| Maven Wrapper | Incluido (`mvnw` / `mvnw.cmd`) — no hace falta instalar Maven |
| Docker Desktop | Solo si vas a usar el perfil **Oracle** |
| Node.js 18+ | Solo para el frontend (repo aparte) |

Perfiles:

- **H2** (por defecto): base en memoria, ideal para demo rápida sin Docker
- **Oracle**: base real vía Docker Compose (`docker-compose.yml`)

## Puertos

| Servicio | Puerto |
|----------|--------|
| Backend API | `8080` |
| Consola H2 | `http://localhost:8080/h2-console` |
| Oracle (Docker) | `1521` |
| Frontend Angular | `4200` (repo frontend) |

---

## Opción A — Levantar con H2 (rápido)

Desde la carpeta del backend:

**Windows (PowerShell):**
```powershell
cd "ruta\al\employee-management-backend"
.\mvnw.cmd spring-boot:run
```

**Linux / macOS:**
```bash
cd ruta/al/employee-management-backend
./mvnw spring-boot:run
```

- API: `http://localhost:8080`
- Al iniciar, `DataLoader` carga 3 departamentos y 5 empleados de prueba

### Consola H2

- JDBC URL: `jdbc:h2:mem:empleadosdb`
- User: `sa`
- Password: *(vacío)*

---

## Opción B — Levantar con Oracle (Docker)

Orden recomendado: **1) Oracle → 2) Backend → 3) Frontend**.

### 1. Arrancar Oracle

Necesitas **Docker Desktop** abierto.

Desde la carpeta del backend:

```powershell
cd "ruta\al\employee-management-backend"
docker compose up -d
```

La primera vez puede tardar **1–3 minutos** (descarga de imagen + arranque de la BD).

Comprobar estado:
```powershell
docker compose ps
```

Cuando veas el contenedor `employee-oracle` en estado **Up** / **healthy**, o en los logs el mensaje `DATABASE IS READY TO USE!`, sigue al paso 2.

El `docker-compose.yml` crea el usuario de aplicación:

| Dato | Valor |
|------|--------|
| Contenedor | `employee-oracle` |
| Puerto | `1521` |
| Service name | `XEPDB1` |
| Usuario app | `empleados` |
| Password app | `empleados` |
| Password sistema (`ORACLE_PASSWORD`) | `Oracle123` |

### 2. Arrancar el backend con perfil Oracle

**Importante:** no tengas otro proceso usando el puerto `8080` (por ejemplo un backend con H2).

**Windows (PowerShell):**
```powershell
cd "ruta\al\employee-management-backend"
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=oracle"
```

**Linux / macOS:**
```bash
cd ruta/al/employee-management-backend
./mvnw spring-boot:run -Dspring-boot.run.profiles=oracle
```

Configuración (`application-oracle.properties`):

- URL: `jdbc:oracle:thin:@localhost:1521/XEPDB1`
- Usuario / password: `empleados` / `empleados`
- `ddl-auto=update` (Hibernate crea/actualiza tablas)
- Al arrancar se siembran los datos de prueba

Señal de éxito en logs:

- `The following 1 profile is active: "oracle"`
- `HikariPool-1 - Start completed`
- `Started EmployeeManagementApplication`

### 3. Frontend

En otra terminal, en el repo **employee-management-frontend**:

```powershell
cd "ruta\al\employee-management-frontend"
npm install
npm start
```

Abrir: `http://localhost:4200`

### Detener Oracle

```powershell
cd "ruta\al\employee-management-backend"
docker compose down
```

---

## Logs de depuración

En H2 y Oracle están activos:

- Parámetros SQL reales (valores de los `?`, no solo placeholders)
- Detalle de requests HTTP (`POST /api/empleados`, etc.)

Al crear un empleado verás en la consola del backend el método/URL y binds como el nombre o el email.

> Los logs del contenedor Docker de Oracle **no** muestran cada `INSERT` de negocio; eso se ve en la consola de Spring Boot.

---

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
exception → @RestControllerAdvice (400 / 404 / 409 / 500)
```

CORS habilitado para `http://localhost:4200`.
