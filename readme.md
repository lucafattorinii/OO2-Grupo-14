# Sistema de Gestión de Turnos – Grupo 14

Este proyecto fue desarrollado por el Grupo 14 como trabajo integrador para la materia Orientación a Objetos II de la Licenciatura en Sistemas. El sistema permite gestionar turnos entre clientes, empleados y prestadores, incluyendo funcionalidades como autenticación, visualización web con Thymeleaf, servicios REST con Swagger, y persistencia con base de datos MySQL.

---

## Estructura del proyecto

- `modelo/`: Entidades JPA mapeadas segun el diagrama.
- `repository/`: Interfaces que extienden `JpaRepository` para acceso a datos.
- `service/`: Lógica de negocio y conversión de DTOs.
- `controller/`: Endpoints REST para gestión de recursos.
- `dto/`: Clases `record` usadas para entrada/salida en la API.
- `config/`: Configuraciones de seguridad, Swagger, email y manejo de excepciones.
- `templates/` y `static/`: Vistas Thymeleaf y recursos CSS/JS.
- `resources/application.properties`: Configuracion de conexión y propiedades del proyecto.

---

## Requisitos previos

- **Java 17**
- **Spring Tool Suite (STS)** o cualquier IDE compatible con Spring Boot
- **MySQL**
- **Postman** o navegador con Swagger para testear la API

---

## Configuración inicial

1. Crear la base de datos:

   Antes de iniciar la app, abrir el archivo SQL llamado:

sistema_turnos.sql

Ejecutarlo en MySQL Workbench o el gestor que utilicen.

> Esto creará toda la estructura de tablas respetando el diagrama.

2. Configurar `application.properties`:

Asegurarse de que las credenciales y URL estén bien configuradas.


Abrir el proyecto en STS o IDE.

Ejecutar la clase:

com.grupo14.turnos.TurnosApplication
como aplicación Spring Boot App.

Esperar que el log diga: Tomcat started on port 8080.

🔑 Acceso a la app
Swagger UI: http://localhost:8080/swagger-ui/index.html

Vista HTML (Thymeleaf): Ingresar directamente a:

http://localhost:8080/clientes/view

http://localhost:8080/empleados/view

http://localhost:8080/servicios/view

http://localhost:8080/turnos/view

http://localhost:8080/disponibilidades/view

http://localhost:8080/especificaciones/view

http://localhost:8080/direcciones/view

Cómo testear el sistema
1. Probar con Swagger
Ir a http://localhost:8080/swagger-ui/index.html

Buscar el controlador que deseen (cliente-controller, turno-controller, etc).

Hacer clic en Try it out, completar los campos con datos válidos y hacer clic en Execute.

2. Verificar en vistas
Después de crear datos, acceder a las vistas /view para ver si se muestran correctamente.

Seguridad
Se utiliza Spring Security con autenticación en memoria:

Usuario: admin

Contraseña: grupo14

Esto es necesario para acceder a los endpoints protegidos. Swagger también solicitará estas credenciales.

Envío de correos
Está configurado el MailSender, aunque en este proyecto no está integrado el envío real. Puede ser ampliado fácilmente desde MailConfig.

Notas finales
El proyecto está alineado 100% con el diagrama y el modelo relacional creado en conjunto.

Se respetan las convenciones de la materia: DTOs, validaciones, herencia con @PrimaryKeyJoinColumn, relaciones bidireccionales, etc.

Todos los controladores tienen vistas Thymeleaf y endpoints REST funcionando.
