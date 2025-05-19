package dgtic.core.repository;

import dgtic.core.model.Estudiante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {

    // Consulta que retorna los estudiantes de los grupos cuyo maestro tiene el id indicado
    @Query("SELECT DISTINCT e FROM Estudiante e JOIN e.grupos g WHERE g.maestro.idMaestro = :idMaestro")
    List<Estudiante> findByMaestroId(@Param("idMaestro") Integer idMaestro);

    // Consulta que retorna los estudiantes de cierto grupo, pertenecientes a cierto maestro.
    @Query("SELECT DISTINCT e FROM Estudiante e JOIN e.grupos g " +
            "WHERE g.idGrupo = :idGrupo AND g.maestro.idMaestro = :idMaestro")
    List<Estudiante> findByGrupoAndMaestro(@Param("idGrupo") Integer idGrupo,
                                           @Param("idMaestro") Integer idMaestro);

    @Query("SELECT DISTINCT e FROM Estudiante e JOIN e.grupos g " +
            "WHERE g.idGrupo = :idGrupo AND g.maestro.idMaestro = :idMaestro")
    Page<Estudiante> findByGrupoAndMaestro(@Param("idGrupo") Integer idGrupo,
                                           @Param("idMaestro") Integer idMaestro,
                                           Pageable pageable);
}
