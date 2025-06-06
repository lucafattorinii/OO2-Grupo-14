DROP DATABASE IF EXISTS sistema_turnos;
CREATE DATABASE sistema_turnos;
USE sistema_turnos;

-- ------------------------------------------------
-- 1) Tabla DIRECCION
-- ------------------------------------------------
CREATE TABLE direccion (
    id_direccion INT AUTO_INCREMENT PRIMARY KEY,
    pais VARCHAR(50),
    provincia VARCHAR(50),
    ciudad VARCHAR(50),
    calle VARCHAR(100),
    numero_calle VARCHAR(10),
    codigo_postal VARCHAR(10)
);

-- ------------------------------------------------
-- 2) Tabla USUARIO
-- ------------------------------------------------
CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL
);

-- ------------------------------------------------
-- 3) Tabla CONTACTO con FK a USUARIO y a DIRECCION
-- ------------------------------------------------
CREATE TABLE contacto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    telefono VARCHAR(20),
    direccion_id INT,
    usuario_id INT UNIQUE,
    FOREIGN KEY (direccion_id) REFERENCES direccion(id_direccion),
    FOREIGN KEY (usuario_id)  REFERENCES usuario(id)
);

-- ------------------------------------------------
-- 4) Tabla PERSONA (hereda a USUARIO)
-- ------------------------------------------------
CREATE TABLE persona (
    id INT PRIMARY KEY,
    nombre VARCHAR(100),
    apellido VARCHAR(100),
    dni BIGINT,
    CONSTRAINT fk_persona_usuario FOREIGN KEY (id) REFERENCES usuario(id)
);

-- ------------------------------------------------
-- 5) Tabla CLIENTE (hereda a PERSONA)
-- ------------------------------------------------
CREATE TABLE cliente (
    id INT PRIMARY KEY,
    numero_cliente BIGINT,
    CONSTRAINT fk_cliente_persona FOREIGN KEY (id) REFERENCES persona(id)
);

-- ------------------------------------------------
-- 6) Tabla EMPLEADO (hereda a PERSONA)
-- ------------------------------------------------
CREATE TABLE empleado (
    id INT PRIMARY KEY,
    cuit BIGINT,
    legajo INT,
    puesto_cargo VARCHAR(100),
    CONSTRAINT fk_empleado_persona FOREIGN KEY (id) REFERENCES persona(id)
);

-- ------------------------------------------------
-- 7) Tabla PRESTADOR (hereda a USUARIO)
-- ------------------------------------------------
CREATE TABLE prestador (
    id INT PRIMARY KEY,
    razon_social VARCHAR(100),
    habilitado BOOLEAN,
    CONSTRAINT fk_prestador_usuario FOREIGN KEY (id) REFERENCES usuario(id)
);

-- ------------------------------------------------
-- 8) Tabla SERVICIO (con FK a PRESTADOR)
-- ------------------------------------------------
CREATE TABLE servicio (
    id_servicio BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    duracion_min INT,
    precio FLOAT,
    prestador_id INT,
    FOREIGN KEY (prestador_id) REFERENCES prestador(id)
);

-- ------------------------------------------------
-- 9) Tabla ESPECIFICACION (1:1 con SERVICIO + FK a DIRECCION)
-- ------------------------------------------------
CREATE TABLE especificacion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    direccion_id INT,
    rubro ENUM('MEDICO','MECANICA','GASTRONOMIA','SERVICIOTECNICO','LABORATORIO','BELLEZA'),
    detalles TEXT,
    servicio_id BIGINT,
    FOREIGN KEY (direccion_id) REFERENCES direccion(id_direccion),
    FOREIGN KEY (servicio_id)   REFERENCES servicio(id_servicio),
    UNIQUE (servicio_id)
);

-- ------------------------------------------------
-- 10) Tabla DISPONIBILIDAD (con FK a SERVICIO y a EMPLEADO)
-- ------------------------------------------------
CREATE TABLE disponibilidad (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dia_semana ENUM('LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO','DOMINGO'),
    hora_inicio TIME,
    hora_fin TIME,
    servicio_id BIGINT,
    empleado_id INT,
    FOREIGN KEY (servicio_id)  REFERENCES servicio(id_servicio),
    FOREIGN KEY (empleado_id)  REFERENCES empleado(id)
);

-- ------------------------------------------------
-- 11) Tabla SERVICIO_EMPLEADO (m:n entre SERVICIO y EMPLEADO)
-- ------------------------------------------------
CREATE TABLE servicio_empleado (
    servicio_id BIGINT,
    empleado_id INT,
    PRIMARY KEY (servicio_id, empleado_id),
    FOREIGN KEY (servicio_id) REFERENCES servicio(id_servicio),
    FOREIGN KEY (empleado_id) REFERENCES empleado(id)
);

-- ------------------------------------------------
-- 12) Tabla FECHA (opcional)
-- ------------------------------------------------
CREATE TABLE fecha (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE,
    dia_semana ENUM('LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO','DOMINGO'),
    direccion_id INT,
    FOREIGN KEY (direccion_id) REFERENCES direccion(id_direccion)
);

-- ------------------------------------------------
-- 13) Tabla TURNO
-- ------------------------------------------------
CREATE TABLE turno (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE,
    hora TIME,
    estado ENUM('PENDIENTE','CONFIRMADO','CANCELADO'),
    cliente_id INT,
    disponibilidad_id INT,
    servicio_id BIGINT,
    FOREIGN KEY (cliente_id)       REFERENCES cliente(id),
    FOREIGN KEY (disponibilidad_id) REFERENCES disponibilidad(id),
    FOREIGN KEY (servicio_id)       REFERENCES servicio(id_servicio)
);
