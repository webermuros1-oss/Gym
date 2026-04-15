# Gym API

API REST desarrollada con Spring Boot para la gestión de un gimnasio.

---

## Tecnologías

- Java 21
- Spring Boot
- Spring Data JPA
- MySQL 8+
- Cloudinary (imágenes)
- Maven

---

## Configuración del proyecto

### Requisitos previos

- Java 21
- MySQL 8+
- Maven

### Variables de entorno (`application.properties`)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gym
spring.datasource.username=root
spring.datasource.password=root
server.port=8080
```

---

## Base de datos — Setup desde cero

Ejecuta los siguientes scripts en orden en tu cliente MySQL (MySQL Workbench, DBeaver, etc.).

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

-- Tabla: activity_user (relación ManyToMany Activities <-> Users)
CREATE TABLE IF NOT EXISTS activity_user (
    activity_id  BIGINT NOT NULL,
    user_id      BIGINT NOT NULL,
    PRIMARY KEY (activity_id, user_id),
    CONSTRAINT fk_au_activity
        FOREIGN KEY (activity_id) REFERENCES activities (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_au_user
        FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### 3. Insertar datos de prueba

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

-- Inscripciones activity_user
-- Ana   -> Yoga, Pilates, Meditación
-- Juan  -> Spinning, Crossfit
-- Sofía -> Yoga, Zumba, Meditación
-- Elena -> Pilates, Zumba
INSERT INTO activity_user (activity_id, user_id) VALUES
(1, 1),
(3, 1),
(6, 1),
(2, 2),
(5, 2),
(1, 3),
(4, 3),
(6, 3),
(3, 5),
(4, 5);
```

### 4. Verificar que todo está correcto

```sql
SELECT * FROM teachers;
SELECT * FROM users;
SELECT * FROM activities;
SELECT * FROM activity_user;
```

---

## Estructura de la base de datos

```
teachers          users
────────          ─────
id (PK)           id (PK)
firstName         first_name
lastName          last_name
dni (UNIQUE)      dni (UNIQUE)
contractYear      registration_year
active            active
imageUrl          image_url
                      │
activities            │
──────────            │
id (PK)               │
title                 │
description           │
price             activity_user
date              ─────────────
imageUrl          activity_id (FK → activities.id)
teacher_id (FK)   user_id     (FK → users.id)
```

---

## Endpoints disponibles

### Users `/api/users`

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/users` | Obtener todos los usuarios |
| GET | `/api/users/{id}` | Obtener usuario por ID |
| POST | `/api/users` | Crear usuario |
| PUT | `/api/users/{id}` | Actualizar usuario |
| DELETE | `/api/users/{id}` | Eliminar usuario |

### Teachers `/api/teachers`

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/teachers` | Obtener todos los profesores |
| GET | `/api/teachers/{id}` | Obtener profesor por ID |
| POST | `/api/teachers` | Crear profesor |
| PUT | `/api/teachers/{id}` | Actualizar profesor |
| DELETE | `/api/teachers/{id}` | Eliminar profesor |

### Activities `/api/activities`

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/activities` | Obtener todas las actividades |
| GET | `/api/activities/{id}` | Obtener actividad por ID |
| GET | `/api/activities/teacher/{teacherId}` | Actividades por profesor |
| POST | `/api/activities` | Crear actividad |
| PUT | `/api/activities/{id}` | Actualizar actividad |
| DELETE | `/api/activities/{id}` | Eliminar actividad |

### Inscripciones `/api/user-activities`

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/user-activities/{activityId}/users/{userId}` | Inscribir usuario en actividad |
| DELETE | `/api/user-activities/{activityId}/users/{userId}` | Desinscribir usuario de actividad |