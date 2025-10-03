# Paquetería - API REST con Spring Boot

Proyecto de ejemplo para gestionar clientes, paquetes y envíos usando Spring Boot, JPA/Hibernate y MySQL.

Requisitos
- Java 17+
- Maven
- (Opcional) MySQL si deseas usar la base de datos externa

Nota: Si `mvn` no está instalado en tu máquina, instala Maven o usa tu IDE (IntelliJ/Eclipse) para importar el proyecto como Maven.

Configuración de la base de datos

1. Crear la base de datos MySQL:

```sql
CREATE DATABASE paqueteria CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- Opcional: crear usuario y otorgar permisos (ejemplo)
CREATE USER 'default'@'localhost' IDENTIFIED BY 'm@teo123';
GRANT ALL PRIVILEGES ON paqueteria.* TO 'default'@'localhost';
FLUSH PRIVILEGES;
```

2. Por defecto el proyecto usa una base de datos en memoria H2 para facilitar la ejecución sin dependencias externas.

Si prefieres conectar un MySQL local, crea la base de datos y ejecuta con el perfil `mysql`:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

El archivo `src/main/resources/application-mysql.properties` contiene la configuración por defecto (host `localhost`, base `paqueteria`, usuario `default`, contraseña `m@teo123`). Ajusta esos valores si necesitas otras credenciales.

Construir y ejecutar

En Windows PowerShell:

```powershell
mvn clean package
mvn spring-boot:run
```

La API quedará escuchando en http://localhost:8080.

Endpoints principales

- Clientes: /api/clientes
- Paquetes: /api/paquetes
- Envíos: /api/envios

Operaciones soportadas (JSON)

- GET /api/clientes
- GET /api/clientes/{id}
- POST /api/clientes  {"nombre":"...","direccion":"...","telefono":"...","email":"..."}
- PUT /api/clientes/{id}
- DELETE /api/clientes/{id}

- GET /api/paquetes
- POST /api/paquetes {"descripcion":"...","peso":1.2,"destino":"...","estado":"...","fechaEnvio":"2025-10-03T12:00:00"}

- GET /api/envios
- POST /api/envios {"cliente":{"id":1},"paquete":{"id":1},"costo":20.0}

Notas

- Se usan validaciones básicas (email, peso positivo, campos required). Si el usuario/contraseña MySQL es distinto, modifica `application.properties`.
- JPA está configurado con `spring.jpa.hibernate.ddl-auto=update` para crear/actualizar tablas automáticamente en desarrollo. En producción, usar migraciones (Flyway/Liquibase).

Pruebas con Postman

En el directorio del proyecto encontrarás `paqueteria-postman-collection.json` con una colección básica para probar los endpoints.
