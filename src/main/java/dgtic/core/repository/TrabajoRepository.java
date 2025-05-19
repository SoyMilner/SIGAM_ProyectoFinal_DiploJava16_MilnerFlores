package dgtic.core.repository;

import dgtic.core.model.Maestro;
import dgtic.core.model.Trabajo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrabajoRepository extends JpaRepository<Trabajo, Integer> {
    List<Trabajo> findByGrupoIdGrupo(Integer idGrupo);

    @Query("SELECT t FROM Trabajo t JOIN t.grupo g WHERE g.maestro.idMaestro = :idMaestro")
    List<Trabajo> findByMaestroIdMaestro(@Param("idMaestro") Integer idMaestro);

    @Query("SELECT t.grupo.maestro FROM Trabajo t WHERE t.idTrabajo = :idTrabajo")
    Maestro findOwnerTrabajo(@Param("idTrabajo") Integer idTrabajo);

    //Muestra los trabajos que existen en el grupo de cierto estudiante
    @Query("SELECT t FROM Trabajo t JOIN t.grupo g JOIN g.estudiantes e " +
            "WHERE e.idEstudiante = :idEstudiante")
    List<Trabajo> findTrabajosAsignadosPorEstudiante(@Param("idEstudiante") Integer idEstudiante);

    // Nueva consulta para obtener directamente los trabajos no calificados
    @Query("SELECT t FROM Trabajo t " +
            "JOIN t.grupo g " +
            "JOIN g.estudiantes e " +
            "WHERE e.idEstudiante = :idEstudiante " +
            "  AND t.idTrabajo NOT IN (" +
            "       SELECT te.trabajo.idTrabajo FROM TrabajoEstudiante te " +
            "       WHERE te.estudiante.idEstudiante = :idEstudiante AND te.calificacion IS NOT NULL" +
            "  )")
    List<Trabajo> findTrabajosNoCalificadosDirecto(@Param("idEstudiante") Integer idEstudiante);


    // Metodo para obtener los trabajos de un maestro (a través del grupo)
    @Query("SELECT t FROM Trabajo t JOIN t.grupo g WHERE g.maestro.idMaestro = :idMaestro")
    Page<Trabajo> findTrabajosDeMaestroPag(@Param("idMaestro") Integer idMaestro, Pageable pageable);

    //Encontrar los trabajos por el id de estudiante Pageable
    @Query("SELECT t FROM Trabajo t JOIN t.grupo g JOIN g.estudiantes e " +
            "WHERE e.idEstudiante = :idEstudiante")
    Page<Trabajo> findTrabajosAsignadosPorEstudiante(@Param("idEstudiante") Integer idEstudiante,
                                                     Pageable pageable);

    //Encontrar los trabajos por id de grupo Pageable
    @Query("SELECT t FROM Trabajo t JOIN t.grupo g WHERE g.idGrupo = :idGrupo")
    Page<Trabajo> findTrabajosPorGrupoPag(@Param("idGrupo") Integer idGrupo, Pageable pageable);

}