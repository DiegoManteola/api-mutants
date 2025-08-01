# Ejercicio - Deteccion de conjuntos de letras en línea en matriz
Implementación en Java 21 + Spring Boot 3 con PostgreSQL, Flyway, Redis Cache y Docker Compose.

# Descripción:
_Servicio REST que detecta si un ADN pertenece a un mutante.
Algoritmo O(N²) con secuencias parametrizables, persistencia única por hash SHA-256, caché Redis para evitar recalcular y estadísticas globales._

# Requisitos
| Herramienta    | Versión mínima              |
| -------------- | --------------------------- |
| Java JDK       | **21**                      |
| Maven Wrapper  | integrado (`./mvnw`)        |
| Docker Desktop | 4.x (con Docker Compose v2) |

# Clonado y Arranque rápido:

- git clone https://github.com/<TU_USUARIO>/mutants.git
- cd mutants

_Levantar infraestructura (PostgreSQL 15 + Redis 7):_
- docker compose up -d

_Compilar, correr tests y levantar la API:_
- ./mvnw clean verify
- ./mvnw spring-boot:run


# Variables de entorno:
Se cargan desde el archivo .env en la raíz.
Ejemplo:
- PGHOST=
- PGPORT=
- PGDATABASE=
- PGUSER=
- PGPASSWORD=

_*Nota: Incluye un .env.example con los nombres de las variables y valores ejemplo.*_

# Migraciones Flyway:
Al levantar la app, Flyway aplica src/main/resources/db/migration/V1__init.sql
y crea la tabla dna con índice parcial para el conteo de mutantes.

# Ejecutar pruebas y cobertura:
_tests + informe JaCoCo:_
- ./mvnw verify
- open target/site/jacoco/index.html

# Endpoints:

| Método   | Ruta      | Request                    | Respuesta OK                                                | Respuesta NOTOK            |
| -------- | --------- | -------------------------- | ----------------------------------------------------------- | -------------------------- |
| **POST** |  /mutant  |  { "dna": ["ATGCGA", …] }  | 200 OK (mutante)                                            | 403 Forbidden (humano)     |
| **GET**  |  /stats   | —                          |  {"count_mutant_dna":40,"count_human_dna":100,"ratio":0.4}  | —                          |

_Nota: Los errores de validación devuelven 400 Bad Request con detalle._


# Ejemplos curl
## Mutante:
- curl -X POST http://localhost:8080/mutant \
     -H "Content-Type: application/json" \
     -d '{"dna":["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]}'
- ⇒ 200 OK

## Humano:
- curl -X POST http://localhost:8080/mutant \
     -H "Content-Type: application/json" \
     -d '{"dna":["ATGCGA","CAGTGC","TTATGT","AGATGG","GCGTCA","TCACTG"]}'
- ⇒ 403 Forbidden

Estadísticas
curl http://localhost:8080/stats


Despliegue en producción:
- 1 - Crear base PostgreSQL y Redis (Railway, AWS RDS + Elasticache, Render, etc.).
- 2 - Definir variables de entorno equivalentes a las del .env.
- 3 - Build de imagen nativa (opcional):
- - ./mvnw -Pnative -DskipTests spring-boot:build-image
- - docker run -p 8080:8080 --env-file .env mutants:latest
- 4 - Exponer /mutant y /stats detrás de un balanceador. Esto permitirá escalado horizontal porque la app es stateless.


*Autor Diego Manteola - 2025*
