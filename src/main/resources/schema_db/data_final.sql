USE sigam_database;

-- 1. Maestros (3) Contraseña: password
INSERT INTO Maestro (Nombre, Apellido_Paterno, Apellido_Materno, Correo, Contrasena) VALUES
('Irma',    'Pérez',    'González',      'irma.perez@email.com',    '$2a$11$bgqBLk77U1VlGdEknAPWE.LOCDccrXloRGftIN4woXfguesXpen2q'),
('Milner',  'Flores',   'Ushuaía',       'milner.flores@email.com', '$2a$11$bgqBLk77U1VlGdEknAPWE.LOCDccrXloRGftIN4woXfguesXpen2q'),
('Dalina',  'Ramírez',  'Vega',          'dalina.ramirez@email.com','$2a$11$bgqBLk77U1VlGdEknAPWE.LOCDccrXloRGftIN4woXfguesXpen2q');

-- 2. Asignaturas (6)
INSERT INTO Asignatura (Nombre_Asignatura, Descripcion) VALUES
('Español',                  'Estudio de la lengua española'),
('Redacción',                'Técnicas de redacción académica'),
('Literatura',               'Análisis de obras literarias'),
('Lingüística',              'Estructura y función del lenguaje'),
('Ecuaciones Diferenciales', 'Métodos de resolución de EDOs'),
('Circuitos Eléctricos',     'Fundamentos de circuitos eléctricos');

-- 3. Grupos (3 por maestro)
-- Irma: Español y Literatura
INSERT INTO Grupo (Nombre_Grupo, ID_Maestro, ID_Asignatura) VALUES
('301', 1, 1),  -- Irma → Español
('302', 1, 3),  -- Irma → Literatura
('303', 1, 1);  -- Irma → Español (segundo grupo)

-- Milner: Ecuaciones Diferenciales y Circuitos Eléctricos
INSERT INTO Grupo (Nombre_Grupo, ID_Maestro, ID_Asignatura) VALUES
('Tercero A', 2, 5),  -- Milner → Ecuaciones Diferenciales
('Tercero B', 2, 6),  -- Milner → Circuitos Eléctricos
('Tercero C', 2, 5);  -- Milner → Ecuaciones Diferenciales (otro grupo)

-- Dalina: Redacción y Lingüística
INSERT INTO Grupo (Nombre_Grupo, ID_Maestro, ID_Asignatura) VALUES
('Grupo 1', 3, 2),    -- Dalina → Redacción
('Grupo 2', 3, 4),    -- Dalina → Lingüística
('Grupo 3', 3, 2);    -- Dalina → Redacción (otro grupo)

-- 4. Estudiantes y sus comentarios (5 por grupo)

-- Grupo 1 (ID_Grupo = 1)
INSERT INTO Estudiante (Nombre, Apellido_Paterno, Apellido_Materno, Matricula, Correo_Electronico, ID_Maestro) VALUES
('Ana',   'López',   'Soto',   'E01', 'ana.l soto@email.com',    1),
('Luis',  'García',  'Torres', 'E02', 'luis.torres@email.com',   1),
('María', 'Jiménez', 'Ríos',   'E03', 'maria.rios@email.com',    1),
('Juan',  'Pacheco', 'Silva',  'E04', 'juan.silva@email.com',    1),
('Carmen','Reyes',   'Díaz',   'E05', 'carmen.diaz@email.com',   1);

-- Grupo 1: Estudiantes ID 1–5
INSERT INTO Estudiante_Grupo (ID_Estudiante, ID_Grupo) VALUES
(1, 1), (2, 1), (3, 1), (4, 1), (5, 1);

-- Comentarios para estos 5
INSERT INTO Comentarios (Comentario, ID_Estudiante, Fecha_Comentario) VALUES
('Le cuesta trabajo la sintaxis',     1, '2025-02-10'),
('Participa poco en clase oral',       2, '2025-03-05'),
('Debe mejorar la ortografía',        3, '2025-01-20'),
('Muy buen progreso en lectura',      4, '2025-04-15'),
('Necesita más práctica de redacción',5, '2025-03-30');

-- Esto se repite para cada grupo (IDs 2 a 9). Grupo 2:
INSERT INTO Estudiante (Nombre, Apellido_Paterno, Apellido_Materno, Matricula, Correo_Electronico, ID_Maestro) VALUES
('Pedro', 'Martín', 'Alonso', 'L01', 'pedro.alonso@email.com', 1),
('Laura', 'Flores', 'Núñez',  'L02', 'laura.nunez@email.com', 1),
('Diego', 'Méndez', 'Castro', 'L03', 'diego.castro@email.com',1),
('Elena', 'Navarro','Pérez',  'L04', 'elena.perez@email.com', 1),
('Sofía', 'Ramos',  'Vega',   'L05', 'sofia.vega@email.com',   1);

-- Grupo 2: Estudiantes ID 6–10
INSERT INTO Estudiante_Grupo (ID_Estudiante, ID_Grupo) VALUES
(6, 2), (7, 2), (8, 2), (9, 2), (10, 2);

INSERT INTO Comentarios (Comentario, ID_Estudiante, Fecha_Comentario) VALUES
('Excelente análisis literario',      6, '2025-02-25'),
('Participación destacada en debates',7, '2025-03-12'),
('Debe citar mejor sus fuentes',      8, '2025-04-01'),
('Buena interpretación de textos',    9, '2025-01-30'),
('Profundizar en lecturas críticas', 10,'2025-04-20');


-- Grupo 3: Irma, ID_Grupo = 3, Estudiantes 11–15
INSERT INTO Estudiante (Nombre, Apellido_Paterno, Apellido_Materno, Matricula, Correo_Electronico, ID_Maestro) VALUES
('Raúl',  'Gómez',    'Ibarra',  'E06', 'raul.ibarra@email.com',    1),
('Lucía', 'Hernández','Peña',    'E07', 'lucia.pena@email.com',     1),
('Mario', 'Castro',   'Morales', 'E08', 'mario.morales@email.com', 1),
('Eva',   'Salas',    'Ríos',    'E09', 'eva.rios@email.com',       1),
('Esteban','Mora',    'Cruz',    'E10', 'esteban.cruz@email.com',   1);
INSERT INTO Estudiante_Grupo (ID_Estudiante, ID_Grupo) VALUES
(11, 3), (12, 3), (13, 3), (14, 3), (15, 3);

-- Grupo 4: Milner, ID_Grupo = 4, Estudiantes 16–20
INSERT INTO Estudiante (Nombre, Apellido_Paterno, Apellido_Materno, Matricula, Correo_Electronico, ID_Maestro) VALUES
('Ernesto','Silva',   'Zúñiga',  'M01', 'ernesto.zuniga@email.com', 2),
('Daniela','Guerra',  'Campos',  'M02', 'daniela.campos@email.com', 2),
('Oscar',  'Valdez',  'Jiménez', 'M03', 'oscar.jimenez@email.com', 2),
('Renata','Muñoz',    'Herrera', 'M04', 'renata.herrera@email.com', 2),
('Joel',   'Ríos',    'García',  'M05', 'joel.garcia@email.com',    2);
INSERT INTO Estudiante_Grupo (ID_Estudiante, ID_Grupo) VALUES
(16, 4), (17, 4), (18, 4), (19, 4), (20, 4);

-- Grupo 5: Milner, ID_Grupo = 5, Estudiantes 21–25
INSERT INTO Estudiante (Nombre, Apellido_Paterno, Apellido_Materno, Matricula, Correo_Electronico, ID_Maestro) VALUES
('Nadia',  'León',     'Fernández', 'M06', 'nadia.fernandez@email.com', 2),
('Isaac',  'Orozco',   'Mendoza',   'M07', 'isaac.mendoza@email.com',   2),
('Sara',   'Delgado',  'Nieves',    'M08', 'sara.nieves@email.com',     2),
('Ulises', 'Bravo',    'Castañeda', 'M09', 'ulises.casta@email.com',    2),
('Verónica','Rivas',   'Meza',      'M10', 'veronica.meza@email.com',   2);
INSERT INTO Estudiante_Grupo (ID_Estudiante, ID_Grupo) VALUES
(21, 5), (22, 5), (23, 5), (24, 5), (25, 5);

-- Grupo 6: Milner, ID_Grupo = 6, Estudiantes 26–30
INSERT INTO Estudiante (Nombre, Apellido_Paterno, Apellido_Materno, Matricula, Correo_Electronico, ID_Maestro) VALUES
('Alan',   'Cortés',   'Vera',     'M11', 'alan.vera@email.com',     2),
('Karina', 'Estrada',  'Pineda',   'M12', 'karina.pineda@email.com', 2),
('Julio',  'Mejía',    'Romero',   'M13', 'julio.romero@email.com',  2),
('Elisa',  'Ramos',    'Sánchez',  'M14', 'elisa.sanchez@email.com', 2),
('Luis',   'Trejo',    'Bautista', 'M15', 'luis.bautista@email.com', 2);
INSERT INTO Estudiante_Grupo (ID_Estudiante, ID_Grupo) VALUES
(26, 6), (27, 6), (28, 6), (29, 6), (30, 6);

-- Grupo 7: Dalina, ID_Grupo = 7, Estudiantes 31–35
INSERT INTO Estudiante (Nombre, Apellido_Paterno, Apellido_Materno, Matricula, Correo_Electronico, ID_Maestro) VALUES
('Beatriz', 'Reynoso', 'Delgado', 'D01', 'beatriz.delgado@email.com', 3),
('Tomás',   'Campos',  'Vargas',  'D02', 'tomas.vargas@email.com',    3),
('Diana',   'Acosta',  'González','D03', 'diana.gonzalez@email.com',  3),
('Gerardo', 'Zamora',  'Molina',  'D04', 'gerardo.molina@email.com',  3),
('Carla',   'Moreno',  'Rosales', 'D05', 'carla.rosales@email.com',   3);
INSERT INTO Estudiante_Grupo (ID_Estudiante, ID_Grupo) VALUES
(31, 7), (32, 7), (33, 7), (34, 7), (35, 7);

-- Grupo 8: Dalina, ID_Grupo = 8, Estudiantes 36–40
INSERT INTO Estudiante (Nombre, Apellido_Paterno, Apellido_Materno, Matricula, Correo_Electronico, ID_Maestro) VALUES
('Iván',    'Sosa',    'Almanza', 'D06', 'ivan.almanza@email.com',   3),
('Paola',   'Guzmán',  'Chávez',  'D07', 'paola.chavez@email.com',   3),
('Camilo',  'Zúñiga',  'Reyes',   'D08', 'camilo.reyes@email.com',   3),
('Natalia', 'López',   'Carrillo','D09', 'natalia.carrillo@email.com',3),
('Ángel',   'Ríos',    'Zarate',  'D10', 'angel.zarate@email.com',   3);
INSERT INTO Estudiante_Grupo (ID_Estudiante, ID_Grupo) VALUES
(36, 8), (37, 8), (38, 8), (39, 8), (40, 8);

-- Grupo 9: Dalina, ID_Grupo = 9, Estudiantes 41–45
INSERT INTO Estudiante (Nombre, Apellido_Paterno, Apellido_Materno, Matricula, Correo_Electronico, ID_Maestro) VALUES
('Sebastián','Nieto',  'Galván',  'D11', 'sebastian.galvan@email.com', 3),
('Mónica',   'Vargas', 'Paz',     'D12', 'monica.paz@email.com',       3),
('David',    'Fierro', 'Santos',  'D13', 'david.santos@email.com',     3),
('Liliana',  'Aguilar','Gómez',   'D14', 'liliana.gomez@email.com',    3),
('Rodrigo',  'Ortega', 'López',   'D15', 'rodrigo.ortega@email.com',   3);
INSERT INTO Estudiante_Grupo (ID_Estudiante, ID_Grupo) VALUES
(41, 9), (42, 9), (43, 9), (44, 9), (45, 9);


-- 5. Catálogo de Tipo_trabajo
INSERT INTO Tipo_trabajo (Nombre_Tipo_Trabajo) VALUES
('Examen'),
('Tarea'),
('Proyecto');

-- 6. Trabajos (2 por cada grupo; aquí ejemplifico para los IDs 1 y 2)
INSERT INTO Trabajo (Nombre_Trabajo, ID_Tipo_Trabajo, Fecha_Asignacion, Fecha_Limite, Descripcion, ID_Grupo, Ponderacion_historica) VALUES
('Examen Recuperación', 1, '2025-02-01', '2025-02-05', 'Examen semestral', 1, 0.00),
('Proyecto Ensayo',     3, '2025-02-10', '2025-03-10', 'Ensayo literario', 1, 0.00),

('Tarea Gramática',     2, '2025-03-01', '2025-03-07', 'Ejercicios de gramática', 2, 0.00),
('Examen Parcial Lit.', 1, '2025-03-10', '2025-03-10', 'Examen mitad de semestre', 2, 0.00);

-- 7. Trabajo_Estudiante (calificaciones)
-- Por cada estudiante de Grupo 1 (IDs 1–5) se asignan sus dos trabajos:
INSERT INTO Trabajo_Estudiante (ID_Trabajo, ID_Estudiante, Calificacion, Fecha_Entrega, Comentarios) VALUES
(1, 1,  8.5, '2025-02-05', 'Buen desempeño'),
(2, 1,  9.0, '2025-03-09', 'Excelente contenido'),

(1, 2,  7.0, '2025-02-05', 'Revisar errores'),
(2, 2,  8.0, '2025-03-09', 'Buena redacción'),

(1, 3,  9.5, '2025-02-05', 'Muy bien'),
(2, 3,  9.0, '2025-03-09', 'Excelente'),

(1, 4,  6.5, '2025-02-05', 'Debe esforzarse más'),
(2, 4,  7.5, '2025-03-09', 'Buen avance'),

(1, 5,  8.0, '2025-02-05', 'Correcto'),
(2, 5,  8.5, '2025-03-09', 'Muy bien');

-- 8. Periodo_Academico (2 registros)
INSERT INTO Periodo_Academico (Nombre_Periodo, Fecha_Inicio, Fecha_Fin, Descripcion) VALUES
('2025-1', '2025-01-01', '2025-06-30', 'Primer semestre 2025'),
('2025-2', '2025-07-01', '2025-12-31', 'Segundo semestre 2025');

-- 9. Historial_Calificaciones (2 registros de ejemplo)
-- Asume que el alumno 1 y 2 ya tienen promedio
INSERT INTO Historial_Calificaciones (ID_Estudiante, ID_Periodo_Academico, Fecha_Registro, Promedio, Comentarios) VALUES
(1, 1, '2025-06-30', 8.75, 'Buen semestre'),
(2, 1, '2025-06-30', 7.50, 'Puede mejorar');

