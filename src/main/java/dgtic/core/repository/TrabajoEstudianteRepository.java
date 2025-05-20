package dgtic.core.repository;

import dgtic.core.model.Estudiante;
import dgtic.core.model.Trabajo;
import dgtic.core.model.TrabajoEstudiante;
import dgtic.core.model.TrabajoEstudianteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrabajoEstudianteRepository extends JpaRepository<TrabajoEstudiante, TrabajoEstudianteId> {

    // Query para obtener los trabajos asignados a un estudiante que ya tienen calificación (calificados)
    @Query("SELECT te FROM TrabajoEstudiante te WHERE te.estudiante.idEstudiante = :idEstudiante AND te.calificacion IS NOT NULL")
    List<TrabajoEstudiante> findCalificadosByEstudianteId(@Param("idEstudiante") Integer idEstudiante);

    // Query para obtener los trabajos asignados a un estudiante que NO tienen calificación (no calificados)
    @Query("SELECT te FROM TrabajoEstudiante te WHERE te.estudiante.idEstudiante = :idEstudiante AND te.calificacion IS NULL")
    List<TrabajoEstudiante> findNoCalificadosByEstudianteId(@Param("idEstudiante") Integer idEstudiante);

    Optional<TrabajoEstudiante> findByTrabajoAndEstudiante(Trabajo trabajo, Estudiante estudiante);
}
