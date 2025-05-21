package dgtic.core.repository;

import dgtic.core.model.HistorialCalificaciones;
import dgtic.core.model.Estudiante;
import dgtic.core.model.PeriodoAcademico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistorialCalificacionesRepository extends JpaRepository<HistorialCalificaciones, Integer> {
    Optional<HistorialCalificaciones> findByEstudianteAndPeriodoAcademico(Estudiante estudiante, PeriodoAcademico periodo);
    List<HistorialCalificaciones> findByPeriodoAcademico(PeriodoAcademico periodoAcademico);
    List<HistorialCalificaciones> findByEstudiante(Estudiante estudiante);

}
