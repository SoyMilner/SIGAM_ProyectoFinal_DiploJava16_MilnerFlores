USE forms_m8;

-- Llenado de la Tabla Asignatura
INSERT INTO asignatura (nombre_asignatura, descripcion)
VALUES
('Matemáticas', 'Estudio de los números y sus operaciones'),
('Español', 'Estudio de la lengua y literatura española'),
('Historia del Arte', 'Estudio de eventos artísticos históricos'),
('Biología', 'Estudio de la vida');

-- Llenado de la Tabla Grupo
-- Las asignaturas están asociadas según: 1 = Matemáticas, 2 = Español, 3 = Historia del Arte, 4 = Biología
INSERT INTO grupo (nombre_grupo, id_asignatura)
VALUES
('101A', 1), -- Grupo asociado a Matemáticas
('201A', 2), -- Grupo asociado a Español
('301A', 3), -- Grupo asociado a Historia del Arte
('401A', 4); -- Grupo asociado a Biología

-- Llenado de la Tabla Tipo de Trabajo
INSERT INTO tipo_trabajo (nombre_tipo_trabajo)
VALUES
('Examen'),
('Tarea'),
('Proyecto'),
('Laboratorio');

-- Llenado de la Tabla Trabajo
-- Los trabajos están vinculados a los grupos según: 1 = 101A, 2 = 201A, etc.
INSERT INTO trabajo (nombre_trabajo, id_tipo_trabajo, fecha_asignacion, fecha_limite, descripcion, id_grupo, ponderacion_final)
VALUES
('Examen Matemáticas', 1, '2025-04-01', '2025-04-05', 'Primer examen del curso.', 1, 30.00),
('Ensayo Literario', 2, '2025-04-06', '2025-04-15', 'Ensayo sobre un texto de literatura española.', 2, 20.00),
('Proyecto de Historia', 3, '2025-04-10', '2025-04-20', 'Proyecto sobre eventos históricos importantes.', 3, 40.00),
('Laboratorio Biología', 4, '2025-04-12', '2025-04-25', 'Experimento sobre biología celular.', 4, 25.00);




-- SELECT g.nombre_grupo AS NombreGrupo, a.nombre_asignatura AS Nombre_Asignatura
-- FROM grupo AS g JOIN asignatura AS a 
-- ON g.id_asignatura = a.id_asignatura;
