package dgtic.core.service;

import dgtic.core.model.HistorialCalificaciones;
import dgtic.core.model.Estudiante;
import dgtic.core.model.PeriodoAcademico;

import java.util.List;
import java.util.Optional;

public interface HistorialCalificacionesService {
    HistorialCalificaciones guardarHistorial(HistorialCalificaciones historial);
    HistorialCalificaciones actualizar(HistorialCalificaciones historial);
    Optional<HistorialCalificaciones> buscarPorEstudianteYPeriodo(Estudiante estudiante, PeriodoAcademico periodo);
    List<HistorialCalificaciones> findByPeriodo(PeriodoAcademico periodo);
    List<HistorialCalificaciones> buscarPorEstudiante(Estudiante estudiante);

}
