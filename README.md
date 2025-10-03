#  Bank Manager

Bank Manager es una API REST desarrollada en **Java + Spring Boot** que permite la gestión de usuarios, cuentas bancarias y transacciones.  
El proyecto implementa una arquitectura en capas (Controller, Service, Repository, DTOs y Entities) y sigue buenas prácticas con **MapStruct**, **validaciones con Jakarta Validation** y **tests unitarios con JUnit + Mockito**.

---

## Tecnologías usadas
- Java 17+
- Spring Boot 3.x
- Spring Data JPA
- Hibernate
- H2 / PostgreSQL (según configuración)
- MapStruct
- Gradle
- JUnit 5 & Mockito

---

## Estructura del proyecto

bank-manager
┣ src
┃ ┣  main
┃ ┃ ┣  java/com/system/bank_manager
┃ ┃ ┃ ┣  controller # Controladores REST
┃ ┃ ┃ ┣  dto # DTOs (Request / Response)
┃ ┃ ┃ ┣  entity # Entidades JPA
┃ ┃ ┃ ┣  exception # Excepciones personalizadas
┃ ┃ ┃ ┣  mapper # MapStruct mappers
┃ ┃ ┃ ┣  repository # Repositorios JPA
┃ ┃ ┃ ┣  service # Lógica de negocio
┃ ┃ ┃ ┗  BankManagerApplication.java
┃ ┣  resources
┃ ┃ ┣  application.properties
┃ ┃ ┗  data.sql / schema.sql
┃
┣  test/java/com/system/bank_manager
┃ ┣  controller
┃ ┣  service
┃ ┗  mapper
┃
┣  build.gradle
┗  settings.gradle


---

##  Configuración

### 1. Clonar el repositorio
```bash
git clone https://github.com/usuario/bank-manager.git
cd bank-manager

2. Construir el proyecto
./gradlew build

3. Ejecutar la aplicación
./gradlew bootRun


Por defecto se levanta en: http://localhost:8080

Endpoints principales
Cuentas

GET /accounts → Listar cuentas

GET /accounts/{id} → Obtener cuenta por ID

POST /accounts → Crear nueva cuenta

Transacciones

POST /transactions/deposit → Depositar dinero

POST /transactions/withdraw → Retirar dinero

POST /transactions/transfer → Transferir entre cuentas

GET /transactions → Listar todas las transacciones

Tests

Para ejecutar los tests:

./gradlew test


Incluyen:

Unit tests con JUnit 5 + Mockito

Validaciones de DTOs

Tests de servicios y controladores con Spring Boot Test

 Contribución

Haz un fork del repositorio

Crea una nueva rama (git checkout -b feature/nueva-funcionalidad)

Realiza tus cambios y haz commit (git commit -m 'Añadir nueva funcionalidad')

Haz push a tu rama (git push origin feature/nueva-funcionalidad)

Crea un Pull Request
