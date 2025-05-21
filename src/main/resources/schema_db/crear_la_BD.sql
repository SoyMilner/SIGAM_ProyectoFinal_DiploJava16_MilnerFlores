-- crear la base de datos y seleccionarla
drop database if exists sigam_database;
create database if not exists sigam_database;
use sigam_database;

-- tabla maestro

create table maestro (
    id_maestro int auto_increment primary key,
    nombre varchar(255) not null,
    apellido_paterno varchar(255) not null,
    apellido_materno varchar(255) not null,
    correo varchar(255) not null unique,
    contrasena VARCHAR(255) not null,
    check (length(contrasena) >= 6)
);

-- tabla asignatura (catálogo)

create table asignatura (
    id_asignatura int auto_increment primary KEY,
    nombre_asignatura varchar(255) not null unique,
    descripcion text
);

-- tabla grupo

create table grupo (
    id_grupo int auto_increment primary key,
    nombre_grupo varchar(50) not null,
    id_maestro int not null,
    id_asignatura int null,
    foreign key (id_maestro) references maestro(id_maestro) ON DELETE RESTRICT,
    foreign key (id_asignatura) references asignatura(id_asignatura) ON DELETE SET NULL
);

-- tabla estudiante

create table estudiante (
    id_estudiante int auto_increment primary key,
    nombre varchar(255) not null,
    apellido_paterno varchar(255) not null,
    apellido_materno varchar(255) not null,
    matricula varchar(50) not null,
    correo_electronico varchar(255) not null,
    id_maestro int not null,
    foreign key (id_maestro) references maestro(id_maestro) ON DELETE RESTRICT,
    unique (correo_electronico, matricula, id_maestro)
);

-- tabla intermedia estudiante_grupo (m:n)

create table estudiante_grupo (
    id_estudiante int not null,
    id_grupo int not null,
    primary key (id_estudiante, id_grupo),
    foreign key (id_estudiante) references estudiante(id_estudiante) ON DELETE CASCADE,
    foreign key (id_grupo) references grupo(id_grupo) ON DELETE CASCADE
);

-- tabla tipo_trabajo 

create table tipo_trabajo (
    id_tipo_trabajo int auto_increment primary key,
    nombre_tipo_trabajo varchar(50) not null
);

-- tabla trabajo

create table trabajo (
    id_trabajo int auto_increment primary key,
    nombre_trabajo varchar(255) not null,
    id_tipo_trabajo int not null,
    fecha_asignacion date not null,
    fecha_limite date not null check (fecha_limite >= fecha_asignacion),
    descripcion text,
    id_grupo int not null,
    ponderacion_historica decimal(5,2),
    foreign key (id_tipo_trabajo) references tipo_trabajo(id_tipo_trabajo) ON DELETE RESTRICT,
    foreign key (id_grupo) references grupo(id_grupo) ON DELETE CASCADE
);

-- tabla etapa_trabajo

create table etapa_trabajo (
    id_etapa_trabajo int auto_increment primary key,
    id_trabajo int not null,
    numero_etapa int not null,
    calificacion_obtenida decimal(5,2) check (calificacion_obtenida >= 0 and calificacion_obtenida <= 10),
    fecha_entrega date not null,
    comentarios text,
    foreign key (id_trabajo) references trabajo(id_trabajo),
    unique (id_trabajo, numero_etapa)
);

-- tabla intermedia trabajo_estudiante (m:n)

create table trabajo_estudiante (
    id_trabajo int not null,
    id_estudiante int not null,
    calificacion decimal(5,2),
    fecha_entrega date,
    comentarios text,
    primary key (id_estudiante, id_trabajo),
    foreign key (id_trabajo) references trabajo(id_trabajo) ON DELETE CASCADE,
    foreign key (id_estudiante) references estudiante(id_estudiante) ON DELETE CASCADE
);

-- tabla periodo_academico (catálogo)

create table periodo_academico (
    id_periodo_academico int auto_increment primary key,
    nombre_periodo varchar(50) not null unique,
    fecha_inicio date not null,
    fecha_fin date not null,
    descripcion text
);

-- tabla historial_calificaciones

create table historial_calificaciones (
    id_historial_calificaciones int auto_increment primary key,
    id_estudiante int not null,
    id_periodo_academico int not null,
    fecha_registro date not null,
    promedio decimal(5,2) not null check (promedio >= 0 and promedio <= 10),
    comentarios text,
    foreign key (id_estudiante) references estudiante(id_estudiante),
    foreign key (id_periodo_academico) references periodo_academico(id_periodo_academico)
);

-- tabla comentarios

-- create table comentarios (
--    id_comentario int auto_increment primary key,
--    comentario text not null,
--   fecha_comentario datetime not null default current_timestamp,
--    id_estudiante int not null,
--    foreign key (id_estudiante) references estudiante(id_estudiante)
-- );
