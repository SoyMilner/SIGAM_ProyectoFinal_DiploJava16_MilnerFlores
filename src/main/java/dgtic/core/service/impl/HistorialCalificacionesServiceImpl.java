package dgtic.core.service.impl;

import dgtic.core.model.HistorialCalificaciones;
import dgtic.core.model.Estudiante;
import dgtic.core.model.PeriodoAcademico;
import dgtic.core.repository.HistorialCalificacionesRepository;
import dgtic.core.service.HistorialCalificacionesService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
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
        log.info("Intentando actualizar historial con ID {}", historialDto.getIdHistorialCalificaciones());
        Optional<HistorialCalificaciones> originalOpt = historialCalificacionesRepository.findById(historialDto.getIdHistorialCalificaciones());
        if (!originalOpt.isPresent()) {
            log.warn("No se encontró el historial con ID {} para actualizar.", historialDto.getIdHistorialCalificaciones());
            throw new EntityNotFoundException("Historial con ID " + historialDto.getIdHistorialCalificaciones() + " no encontrado.");
        }

        HistorialCalificaciones original = originalOpt.get();

        log.debug("Datos antes de actualización: {}", original);

        // Actualiza sólo el campo que deseas modificar (comentarios)
        original.setComentarios(historialDto.getComentarios());

        HistorialCalificaciones actualizado = historialCalificacionesRepository.save(original);

        log.info("Historial actualizado exitosamente: {}", actualizado);
        return actualizado;
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
