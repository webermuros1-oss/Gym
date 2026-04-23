# Gym API

API REST desarrollada con Spring Boot para la gestión integral de un gimnasio: usuarios, profesores, actividades e inscripciones (`enrollments`). Incluye subida de imágenes a Cloudinary, control de asistencia/pago por inscripción y CORS configurado para frontends locales.

---

## Índice

- [Tecnologías](#tecnologías)
- [Arquitectura del proyecto](#arquitectura-del-proyecto)
- [Configuración](#configuración)
- [Base de datos](#base-de-datos)
- [Imágenes con Cloudinary](#imágenes-con-cloudinary)
- [Endpoints](#endpoints)
- [Reglas de negocio](#reglas-de-negocio)
- [DTOs y validaciones](#dtos-y-validaciones)
- [Manejo de errores](#manejo-de-errores)
- [Licencia](#licencia)

---

## Tecnologías

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 21 | Lenguaje principal |
| Spring Boot | 3.x | Framework web y configuración |
| Spring Data JPA | 3.x | Acceso a base de datos con Hibernate |
| Spring Validation | 3.x | Validación de DTOs |
| MySQL | 8+ | Base de datos relacional |
| Cloudinary | SDK Java | Almacenamiento de imágenes en la nube |
| Lombok | latest | Reducción de boilerplate |
| Maven | 3.x | Gestión de dependencias y build |

---

## Arquitectura del proyecto

```
src/main/java/inditex/P1/Gym/
│
├── config/
│   └── CorsConfig.java              # CORS para http://localhost:5173 y :3000
│
├── controller/
│   ├── UserController.java          # CRUD usuarios + subida imagen
│   ├── TeacherController.java       # CRUD profesores + subida imagen
│   ├── ActivityController.java      # CRUD actividades + imagen + alta/baja inscripción
│   ├── UserActivityController.java  # Actividades por usuario
│   └── EnrollmentController.java    # Gestión completa de inscripciones
│
├── service/
│   ├── UserService.java
│   ├── TeacherService.java
│   ├── ActivityService.java
│   ├── EnrollmentService.java       # Consultar / actualizar / cancelar inscripciones
│   └── CloudinaryService.java       # Subida de imágenes a Cloudinary
│
├── repository/
│   ├── UserRepository.java
│   ├── TeacherRepository.java
│   ├── ActivityRepository.java
│   └── EnrollmentRepository.java    # Repositorio de inscripciones (clave compuesta)
│
├── model/
│   ├── User.java
│   ├── Teacher.java
│   ├── Activity.java
│   ├── Enrollment.java              # Entidad de inscripción (datos extra)
│   ├── EnrollmentId.java            # Clave compuesta (activityId, userId)
│   └── AttendanceStatus.java        # PENDING / ATTENDED / ABSENT / CANCELLED
│
├── DTO/
│   ├── Activity/
│   │   ├── ActivityRequestDTO.java
│   │   ├── ActivityResponseDTO.java
│   │   ├── ActivityDetailResponseDTO.java
│   │   └── ActivityMapper.java
│   ├── Enrollment/
│   │   ├── EnrollmentRequestDTO.java
│   │   ├── EnrollmentResponseDTO.java
│   │   └── EnrollmentMapper.java
│   ├── Teacher/
│   │   ├── TeacherRequestDTO.java
│   │   ├── TeacherResponseDTO.java
│   │   └── TeacherMapper.java
│   └── user/
│       ├── UserRequestDTO.java
│       ├── UserResponseDTO.java
│       └── UserMapper.java
│
└── exception/
    ├── GlobalExceptionHandler.java
    ├── ObjectNotFoundException.java
    └── ErrorResponse.java
```

### Relaciones entre entidades

```
Teacher  1 ──── N  Activity  1 ──── N  Enrollment  N ──── 1  User
```

- Un **Teacher** puede impartir muchas **Activity**.
- Cada **Activity** tiene N **Enrollment** (uno por usuario inscrito).
- Cada **User** tiene N **Enrollment** (uno por actividad en la que se inscribe).
- **Enrollment** es una entidad propia con datos adicionales (pago, asistencia, descuento, notas, fechas), no un simple join table.

---

## Configuración

### Requisitos previos

- Java 21
- MySQL 8+
- Maven 3+
- Cuenta en [Cloudinary](https://cloudinary.com) (gratuita)

### Variables en `application.properties`

```properties
# Base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/gym
spring.datasource.username=root
spring.datasource.password=root

# Puerto
server.port=8080

# Cloudinary
cloudinary.cloud-name=TU_CLOUD_NAME
cloudinary.api-key=TU_API_KEY
cloudinary.api-secret=TU_API_SECRET
```

> Las credenciales de Cloudinary se obtienen desde el **Dashboard** de tu cuenta en cloudinary.com.

### CORS

`CorsConfig` permite peticiones desde:

- `http://localhost:5173` (Vite)
- `http://localhost:3000` (Create React App / Next.js)

Métodos permitidos: `GET, POST, PUT, PATCH, DELETE, OPTIONS`.

### Arrancar el proyecto

```bash
mvn spring-boot:run
```

---

## Base de datos

### 1. Crear la base de datos

```sql
CREATE DATABASE IF NOT EXISTS gym
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE gym;
```

### 2. Crear las tablas

```sql
-- Tabla: teachers
CREATE TABLE IF NOT EXISTS teachers (
    id            BIGINT        NOT NULL AUTO_INCREMENT,
    firstName     VARCHAR(100)  NOT NULL,
    lastName      VARCHAR(100)  NOT NULL,
    dni           VARCHAR(20)   NOT NULL UNIQUE,
    contractYear  INT           NOT NULL,
    active        TINYINT(1)    NOT NULL DEFAULT 1,
    imageUrl      VARCHAR(255),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla: users
CREATE TABLE IF NOT EXISTS users (
    id                BIGINT        NOT NULL AUTO_INCREMENT,
    first_name        VARCHAR(100)  NOT NULL,
    last_name         VARCHAR(100)  NOT NULL,
    dni               VARCHAR(20)   NOT NULL UNIQUE,
    registration_year INT           NOT NULL,
    active            TINYINT(1)    NOT NULL DEFAULT 1,
    image_url         VARCHAR(255),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla: activities
CREATE TABLE IF NOT EXISTS activities (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    title       VARCHAR(255),
    description VARCHAR(255),
    price       DECIMAL(10, 2),
    date        DATETIME,
    imageUrl    VARCHAR(255),
    teacher_id  BIGINT,
    PRIMARY KEY (id),
    CONSTRAINT fk_activity_teacher
        FOREIGN KEY (teacher_id) REFERENCES teachers (id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla: enrollments (sustituye a la antigua activity_user)
CREATE TABLE IF NOT EXISTS enrollments (
    activity_id        BIGINT        NOT NULL,
    user_id            BIGINT        NOT NULL,
    registered_at      DATETIME,
    paid               TINYINT(1)    NOT NULL DEFAULT 0,
    price_paid         DECIMAL(10,2),
    attendance_status  VARCHAR(20)   NOT NULL DEFAULT 'PENDING',
    cancelled_at       DATETIME,
    notes              VARCHAR(500),
    discount_applied   DECIMAL(5,2),
    PRIMARY KEY (activity_id, user_id),
    CONSTRAINT fk_enrollment_activity
        FOREIGN KEY (activity_id) REFERENCES activities (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_enrollment_user
        FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

#### Columnas de `enrollments`

| Columna | Tipo | Descripción |
|---|---|---|
| `activity_id` | BIGINT (PK, FK) | Actividad inscrita |
| `user_id` | BIGINT (PK, FK) | Usuario inscrito |
| `registered_at` | DATETIME | Fecha/hora de la inscripción (se asigna automáticamente al registrar) |
| `paid` | TINYINT(1) | Si la inscripción está pagada |
| `price_paid` | DECIMAL(10,2) | Importe efectivamente abonado |
| `attendance_status` | VARCHAR(20) | `PENDING` / `ATTENDED` / `ABSENT` / `CANCELLED` |
| `cancelled_at` | DATETIME | Fecha/hora de cancelación (la fija el endpoint `cancel`) |
| `notes` | VARCHAR(500) | Comentarios libres sobre la inscripción |
| `discount_applied` | DECIMAL(5,2) | Porcentaje de descuento aplicado (0 - 100) |

### 3. Datos de prueba

```sql
-- Teachers
INSERT INTO teachers (firstName, lastName, dni, contractYear, active, imageUrl) VALUES
('Carlos',   'López Martínez',  '11111111A', 2020, 1, NULL),
('María',    'García Sánchez',  '22222222B', 2018, 1, NULL),
('Pedro',    'Fernández Ruiz',  '33333333C', 2021, 1, NULL),
('Laura',    'Martínez Torres', '44444444D', 2019, 0, NULL);

-- Users
INSERT INTO users (first_name, last_name, dni, registration_year, active, image_url) VALUES
('Ana',    'Pérez González',  '55555555E', 2022, 1, NULL),
('Juan',   'Rodríguez Díaz',  '66666666F', 2021, 1, NULL),
('Sofía',  'Hernández Vega',  '77777777G', 2023, 1, NULL),
('Miguel', 'Gómez Castro',    '88888888H', 2020, 0, NULL),
('Elena',  'Jiménez Moreno',  '99999999I', 2022, 1, NULL);

-- Activities (teacher_id: 1=Carlos, 2=María, 3=Pedro)
INSERT INTO activities (title, description, price, date, imageUrl, teacher_id) VALUES
('Yoga Matutino',    'Sesión de yoga para empezar el día con energía',    15.00, '2026-05-10 09:00:00', NULL, 1),
('Spinning Intenso', 'Clase de ciclismo indoor de alta intensidad',        20.00, '2026-05-11 11:00:00', NULL, 2),
('Pilates Básico',   'Introducción al pilates para principiantes',          12.50, '2026-05-12 18:00:00', NULL, 1),
('Zumba Fitness',    'Baile fitness con música latina',                     18.00, '2026-05-13 19:00:00', NULL, 3),
('Crossfit',         'Entrenamiento funcional de alta intensidad',          25.00, '2026-05-14 07:30:00', NULL, 2),
('Meditación',       'Técnicas de relajación y mindfulness',                10.00, '2026-05-15 20:00:00', NULL, 1);

-- Inscripciones
INSERT INTO enrollments (activity_id, user_id, registered_at, paid, price_paid, attendance_status) VALUES
(1, 1, NOW(), 1, 15.00, 'PENDING'),
(3, 1, NOW(), 0, NULL,  'PENDING'),
(6, 1, NOW(), 1, 10.00, 'PENDING'),
(2, 2, NOW(), 1, 20.00, 'PENDING'),
(5, 2, NOW(), 0, NULL,  'PENDING'),
(1, 3, NOW(), 1, 15.00, 'PENDING'),
(4, 3, NOW(), 0, NULL,  'PENDING'),
(6, 3, NOW(), 1, 10.00, 'PENDING'),
(3, 5, NOW(), 1, 12.50, 'PENDING'),
(4, 5, NOW(), 0, NULL,  'PENDING');
```

### 4. Verificar

```sql
SELECT * FROM teachers;
SELECT * FROM users;
SELECT * FROM activities;
SELECT * FROM enrollments;
```

---

## Imágenes con Cloudinary

Los endpoints de creación y actualización de **usuarios**, **profesores** y **actividades** aceptan una imagen opcional. La imagen se sube a Cloudinary y se guarda la URL segura (`https://...`) en la base de datos.

### Cómo funciona

1. El cliente envía la petición como `multipart/form-data` (no JSON).
2. Los campos del formulario son los mismos que antes (texto), más un campo `image` opcional con el archivo.
3. El backend sube el archivo a Cloudinary mediante `CloudinaryService` y obtiene la `secure_url`.
4. Esa URL se asigna al campo `imageUrl` del DTO antes de persistir.

### Ejemplo con fetch (JavaScript)

```javascript
const formData = new FormData();
formData.append('firstName', 'Carlos');
formData.append('lastName', 'López');
formData.append('dni', '11111111A');
formData.append('contractYear', '2020');
formData.append('active', 'true');
formData.append('image', archivoSeleccionado); // File del <input type="file">

fetch('/api/teachers', {
  method: 'POST',
  body: formData
  // NO establecer Content-Type, el navegador lo pone automáticamente con el boundary
});
```

> El campo `image` es **opcional**. Si no se envía, `imageUrl` queda como `null` o mantiene el valor anterior.

### Campo `date` en actividades

Al enviar actividades como `multipart/form-data`, el campo `date` debe tener formato ISO 8601:

```
2026-05-10T09:00:00
```

---

## Endpoints

### Users — `/api/users`

| Método | Ruta | Content-Type | Descripción |
|--------|------|---|-------------|
| GET | `/api/users` | — | Obtener todos los usuarios |
| GET | `/api/users/{id}` | — | Obtener usuario por ID |
| GET | `/api/users/active` | — | Obtener usuarios activos |
| GET | `/api/users/{userId}/activities` | — | Actividades en las que está inscrito el usuario |
| POST | `/api/users` | `multipart/form-data` | Crear usuario (imagen opcional) |
| PUT | `/api/users/{id}` | `multipart/form-data` | Actualizar usuario (imagen opcional) |
| DELETE | `/api/users/{id}` | — | Eliminar usuario |

#### Campos del formulario (POST/PUT)

| Campo | Tipo | Requerido | Validación |
|-------|------|-----------|-----------|
| `firstName` | String | Sí | No puede estar vacío |
| `lastName` | String | Sí | No puede estar vacío |
| `dni` | String | Sí | No puede estar vacío, único |
| `registrationYear` | Integer | Sí | >= 2000 |
| `active` | Boolean | No | — |
| `image` | File | No | Imagen subida a Cloudinary |

---

### Teachers — `/api/teachers`

| Método | Ruta | Content-Type | Descripción |
|--------|------|---|-------------|
| GET | `/api/teachers` | — | Obtener todos los profesores |
| GET | `/api/teachers/{id}` | — | Obtener profesor por ID |
| GET | `/api/teachers/active` | — | Obtener profesores activos |
| POST | `/api/teachers` | `multipart/form-data` | Crear profesor (imagen opcional) |
| PUT | `/api/teachers/{id}` | `multipart/form-data` | Actualizar profesor (imagen opcional) |
| DELETE | `/api/teachers/{id}` | — | Eliminar profesor |

#### Campos del formulario (POST/PUT)

| Campo | Tipo | Requerido | Validación |
|-------|------|-----------|-----------|
| `firstName` | String | Sí | No puede estar vacío |
| `lastName` | String | Sí | No puede estar vacío |
| `dni` | String | Sí | No puede estar vacío, único |
| `contractYear` | Integer | Sí | >= 2000 |
| `active` | Boolean | No | — |
| `image` | File | No | Imagen subida a Cloudinary |

---

### Activities — `/api/activities`

| Método | Ruta | Content-Type | Descripción |
|--------|------|---|-------------|
| GET | `/api/activities/future` | — | Actividades futuras |
| GET | `/api/activities/{id}` | — | Detalle de actividad (incluye profesor, usuarios inscritos y `enrolledCount`) |
| GET | `/api/activities/teacher/{teacherId}` | — | Actividades de un profesor |
| POST | `/api/activities` | `multipart/form-data` | Crear actividad (imagen opcional) |
| PUT | `/api/activities/{id}` | `multipart/form-data` | Actualizar actividad (imagen opcional) |
| DELETE | `/api/activities/{id}` | — | Eliminar actividad |
| POST | `/api/activities/{activityId}/users/{userId}` | — | Inscribir usuario en actividad (crea `Enrollment`) |
| DELETE | `/api/activities/{activityId}/users/{userId}` | — | Desinscribir usuario (elimina `Enrollment`) |

#### Campos del formulario (POST/PUT)

| Campo | Tipo | Requerido | Validación |
|-------|------|-----------|-----------|
| `title` | String | Sí | No puede estar vacío |
| `description` | String | Sí | No puede estar vacío |
| `price` | BigDecimal | Sí | > 0 |
| `date` | LocalDateTime | Sí | Formato `2026-05-10T09:00:00` |
| `teacherId` | Long | Sí | >= 1, profesor activo |
| `image` | File | No | Imagen subida a Cloudinary |

---

### Enrollments — `/api/enrollments`

Gestión detallada de inscripciones (pago, asistencia, notas, descuento). El alta y la baja de la inscripción siguen haciéndose desde `ActivityController` (`POST/DELETE /api/activities/{activityId}/users/{userId}`); este controlador se ocupa del ciclo de vida posterior.

| Método | Ruta | Content-Type | Descripción |
|--------|------|---|-------------|
| GET | `/api/enrollments` | — | Listar todas las inscripciones |
| GET | `/api/enrollments/activity/{activityId}` | — | Inscripciones de una actividad |
| GET | `/api/enrollments/user/{userId}` | — | Inscripciones de un usuario |
| GET | `/api/enrollments/{activityId}/{userId}` | — | Detalle de una inscripción concreta |
| PUT | `/api/enrollments/{activityId}/{userId}` | `application/json` | Actualizar pago / estado / notas / descuento |
| PATCH | `/api/enrollments/{activityId}/{userId}/cancel` | — | Marcar como `CANCELLED` y fijar `cancelledAt` |

#### Body del PUT (`EnrollmentRequestDTO`)

Todos los campos son opcionales: solo se actualizan los que vengan informados.

| Campo | Tipo | Validación |
|-------|------|-----------|
| `paid` | Boolean | — |
| `pricePaid` | BigDecimal | >= 0 |
| `attendanceStatus` | Enum | `PENDING` / `ATTENDED` / `ABSENT` / `CANCELLED` |
| `notes` | String | Máx. 500 caracteres |
| `discountApplied` | BigDecimal | Entre 0 y 100 |

#### Ejemplo de respuesta (`EnrollmentResponseDTO`)

```json
{
  "activityId": 1,
  "activityTitle": "Yoga Matutino",
  "userId": 3,
  "userFullName": "Sofía Hernández Vega",
  "registeredAt": "2026-04-20T10:15:00",
  "paid": true,
  "pricePaid": 13.50,
  "attendanceStatus": "PENDING",
  "cancelledAt": null,
  "notes": "Cliente con descuento de fidelidad",
  "discountApplied": 10.00
}
```

---

## Reglas de negocio

Validaciones aplicadas en los servicios además de las restricciones de los DTOs:

- **Crear/actualizar actividad**: el `teacherId` debe existir y el profesor debe estar **activo**.
- **Inscribir usuario en actividad**:
  - El usuario debe estar **activo**.
  - No puede haber otra inscripción previa para el mismo `(activityId, userId)`.
  - El usuario no puede tener **más de 3 inscripciones en actividades futuras** (basado en `activity.date > NOW()`).
  - Al crear la inscripción se fija `registeredAt = NOW()`, `paid = false` y `attendanceStatus = PENDING`.
- **Desinscribir usuario**: la inscripción debe existir; se elimina la fila completa de `enrollments`.
- **Cancelar inscripción** (`PATCH .../cancel`): no la borra, marca `attendanceStatus = CANCELLED` y rellena `cancelledAt`.
- **Detalle de actividad** (`GET /api/activities/{id}`): incluye el profesor, la lista de usuarios inscritos derivada de `enrollments` y `enrolledCount`.

---

## DTOs y validaciones

Todos los endpoints de escritura usan DTOs con validaciones de Jakarta Validation. Cada agregado tiene su propio paquete con `RequestDTO`, `ResponseDTO` y `Mapper` (`entity2DTO`, `dto2Entity`, `updateEntityFromDto`), de forma que las entidades JPA nunca se exponen directamente en la API.

Si algún campo no cumple la restricción, se devuelve un `400 Bad Request` con el detalle del error:

```json
{
  "status": 400,
  "message": "El nombre no puede estar vacío"
}
```

---

## Manejo de errores

El `GlobalExceptionHandler` centraliza el manejo de excepciones:

| Excepción | HTTP | Cuándo ocurre |
|-----------|------|---------------|
| `ObjectNotFoundException` | 404 | Entidad no encontrada por ID, o entidad inactiva (profesor/usuario) cuando se requiere activa |
| `MethodArgumentNotValidException` | 400 | Fallo de validación en DTO |
| `IllegalArgumentException` | 400 | Reglas de negocio: ya inscrito, no inscrito al desinscribir, límite de 3 actividades futuras, error al subir imagen a Cloudinary |
| `Exception` | 500 | Error inesperado del servidor |

---

## Licencia

MIT License — ver [LICENSE](LICENSE)

---
