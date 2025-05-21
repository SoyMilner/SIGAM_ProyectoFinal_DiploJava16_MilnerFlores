package dgtic.core.service.impl;

import dgtic.core.model.HistorialCalificaciones;
import dgtic.core.model.Estudiante;
import dgtic.core.model.PeriodoAcademico;
import dgtic.core.repository.HistorialCalificacionesRepository;
import dgtic.core.service.HistorialCalificacionesService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HistorialCalificacionesServiceImpl implements HistorialCalificacionesService {

    @Autowired
    private HistorialCalificacionesRepository historialCalificacionesRepository;

    @Override
    public HistorialCalificaciones guardarHistorial(HistorialCalificaciones historial) {
        return historialCalificacionesRepository.save(historial);
    }

    @Override
    @Transactional
    public HistorialCalificaciones actualizar(HistorialCalificaciones historialDto) {
        // Recupéralo de la base de datos usando el ID
        Optional<HistorialCalificaciones> originalOpt = historialCalificacionesRepository.findById(historialDto.getIdHistorialCalificaciones());
        if (!originalOpt.isPresent()) {
            throw new EntityNotFoundException("Historial con ID " + historialDto.getIdHistorialCalificaciones() + " no encontrado.");
        }

        HistorialCalificaciones original = originalOpt.get();

        // Actualiza sólo el campo que deseas modificar (comentarios)
        original.setComentarios(historialDto.getComentarios());

        // Puedes actualizar otros campos si lo necesitas, pero evita sobrescribir los que no vienen en la vista
        // por ejemplo, no setees el estudiante o el período ya que esos deben mantenerse.

        return historialCalificacionesRepository.save(original);
    }


    @Override
    public Optional<HistorialCalificaciones> buscarPorEstudianteYPeriodo(Estudiante estudiante, PeriodoAcademico periodo) {
        return historialCalificacionesRepository.findByEstudianteAndPeriodoAcademico(estudiante, periodo);
    }

    @Override
    public List<HistorialCalificaciones> findByPeriodo(PeriodoAcademico periodo) {
        // Con este método se asegura traer todos los registros asociados a ese período
        return historialCalificacionesRepository.findByPeriodoAcademico(periodo);
    }

    @Override
    public List<HistorialCalificaciones> buscarPorEstudiante(Estudiante estudiante) {
        return historialCalificacionesRepository.findByEstudiante(estudiante);
    }
}
