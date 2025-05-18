package dgtic.core.repository;

import dgtic.core.model.Maestro;
import dgtic.core.model.Trabajo;
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

}