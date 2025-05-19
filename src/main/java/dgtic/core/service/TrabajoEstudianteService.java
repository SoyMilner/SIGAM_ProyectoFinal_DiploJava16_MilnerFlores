package dgtic.core.service;

import dgtic.core.model.TrabajoEstudiante;
import java.util.List;

public interface TrabajoEstudianteService {

    List<TrabajoEstudiante> obtenerTrabajosCalificadosPorEstudiante(Integer idEstudiante);

    List<TrabajoEstudiante> obtenerTrabajosNoCalificadosPorEstudiante(Integer idEstudiante);

    // Método para asignar o actualizar la calificación y otros datos de un TrabajoEstudiante.
    TrabajoEstudiante asignarCalificacion(TrabajoEstudiante trabajoEstudiante);
}
