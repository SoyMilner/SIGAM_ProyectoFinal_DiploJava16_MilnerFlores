package dgtic.core.service;

import dgtic.core.model.Estudiante;
import dgtic.core.model.Trabajo;
import dgtic.core.model.TrabajoEstudiante;
import java.util.List;
import java.util.Optional;

public interface TrabajoEstudianteService {

    List<TrabajoEstudiante> obtenerTrabajosCalificadosPorEstudiante(Integer idEstudiante);

    List<TrabajoEstudiante> obtenerTrabajosNoCalificadosPorEstudiante(Integer idEstudiante);

    // Método para asignar o actualizar la calificación y otros datos de un TrabajoEstudiante.
    TrabajoEstudiante asignarCalificacion(TrabajoEstudiante trabajoEstudiante);

    Optional<TrabajoEstudiante> findByTrabajoAndEstudiante(Trabajo trabajo, Estudiante estudiante);
}
