# Sistema de Gestión de Turnos - OO2 Grupo 14

## Descripción del Proyecto

Este sistema permite la gestión completa de turnos para diferentes servicios, incluyendo la administración de clientes, empleados, prestadores, servicios, disponibilidades y turnos.

## Funcionalidades Implementadas

### Gestión de Entidades
- **Clientes**: Crear, modificar, eliminar y listar clientes.
- **Empleados**: Crear, modificar, eliminar y listar empleados.
- **Prestadores**: Crear, modificar, eliminar y listar prestadores de servicios.
- **Servicios**: Crear, modificar, eliminar y listar servicios disponibles.
- **Especificaciones**: Crear, modificar, eliminar y listar especificaciones de servicios.
- **Disponibilidades**: Crear, modificar, eliminar y listar disponibilidades de prestadores.
- **Turnos**: Crear, modificar, eliminar y listar turnos. Cambiar el estado de los turnos.
- **Direcciones**: Crear, modificar, eliminar y listar direcciones.
- **Usuarios**: Crear, modificar, eliminar y listar usuarios del sistema.

### Funcionalidades Especiales
- **Calendario de Disponibilidades**: Visualización de disponibilidades en formato calendario.
- **Reserva de Turnos**: Proceso guiado para la reserva de turnos desde el calendario.
- **Sistema de Login**: Autenticación de usuarios para acceder al sistema.

## Tecnologías Utilizadas

- **Backend**: Spring Boot, JPA/Hibernate
- **Frontend**: Thymeleaf, HTML, CSS, JavaScript
- **Base de Datos**: H2 (desarrollo), MySQL (producción)

## Requisitos

- Java 17 o superior
- Maven 3.6 o superior
- MySQL (opcional para producción)

## Instrucciones de Ejecución

### Opción 1: Ejecutar con Maven

1. Clonar el repositorio:
   ```
   git clone https://github.com/tu-usuario/OO2-Grupo-14.git
   cd OO2-Grupo-14
   ```

2. Compilar el proyecto:
   ```
   mvn spring-boot:run
   ```

3. Ejecutar la aplicación:
   ```
   TurnosApplication.java
   ```

4. Acceder a la aplicación:
   Abrir un navegador web y visitar `http://localhost:8080`

### Opción 2: Importar en IDE

1. Importar como proyecto Maven en Eclipse, IntelliJ IDEA o Spring Tool Suite.
2. Ejecutar la clase `TurnosApplication.java` como aplicación Java.

## Credenciales de Acceso (Desarrollo)

- **Usuario**: admin
- **Contraseña**: admin123

## Estructura del Proyecto

```
src/main/java/com/grupo14/turnos/
├── controller/       # Controladores REST y MVC
├── dto/              # Objetos de transferencia de datos
├── exception/        # Excepciones personalizadas
├── modelo/           # Entidades JPA
├── repository/       # Repositorios JPA
└── service/          # Servicios de negocio

src/main/resources/
├── static/           # Recursos estáticos (CSS, JS)
├── templates/        # Plantillas Thymeleaf
└── application.properties  # Configuración de la aplicación
```

## Diagrama de Clases

El sistema sigue el siguiente diagrama de clases:

- **Usuario**: Clase base para usuarios del sistema.
  - **Persona**: Extiende Usuario con datos personales.
    - **Cliente**: Extiende Persona para clientes que solicitan turnos.
    - **Empleado**: Extiende Persona para empleados del sistema.
      - **Prestador**: Extiende Empleado para prestadores de servicios.

- **Servicio**: Representa un servicio ofrecido.
  - **Especificacion**: Detalles específicos de un servicio.

- **Disponibilidad**: Horarios disponibles para un servicio.
- **Turno**: Reserva de un servicio en una fecha y hora específica.
- **Direccion**: Ubicación física.
- **Contacto**: Información de contacto.
- **Fecha**: Representación de fechas en el sistema.

## Contribuidores

- Grupo 14 - OO2 - 2025

## Licencia

Este proyecto es de uso educativo para la materia OO2.

