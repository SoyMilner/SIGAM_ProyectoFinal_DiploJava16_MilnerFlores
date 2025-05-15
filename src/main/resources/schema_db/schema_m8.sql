DROP DATABASE IF EXISTS forms_m8;
CREATE DATABASE IF NOT EXISTS forms_m8;
USE forms_m8;

-- Tabla Asignatura (Cat)

CREATE TABLE asignatura (
    id_asignatura INT AUTO_INCREMENT PRIMARY KEY,
    nombre_asignatura VARCHAR(255) NOT NULL UNIQUE,
    descripcion TEXT
);


-- Tabla Grupo

CREATE TABLE grupo (
    id_grupo INT AUTO_INCREMENT PRIMARY KEY,
    nombre_grupo VARCHAR(50) NOT NULL,
    id_asignatura INT NULL,
    FOREIGN KEY (id_asignatura) REFERENCES asignatura(id_asignatura)
);


-- Tabla Trabajo

CREATE TABLE trabajo (
    id_trabajo INT AUTO_INCREMENT PRIMARY KEY,
    nombre_trabajo VARCHAR(255) NOT NULL,
    id_tipo_trabajo INT NOT NULL,
    fecha_asignacion DATE NOT NULL,
    fecha_limite DATE NOT NULL CHECK (fecha_limite >= fecha_asignacion),
    descripcion TEXT,
    id_grupo INT NOT NULL,
    ponderacion_final DECIMAL(5,2),
    FOREIGN KEY (id_tipo_trabajo) REFERENCES tipo_trabajo(id_tipo_trabajo),
    FOREIGN KEY (id_grupo) REFERENCES grupo(id_grupo)
);


-- Tabla Tipo de trabajo (Cat)

CREATE TABLE tipo_trabajo (
    id_tipo_trabajo INT AUTO_INCREMENT PRIMARY KEY,
    nombre_tipo_trabajo VARCHAR(50) NOT NULL
);