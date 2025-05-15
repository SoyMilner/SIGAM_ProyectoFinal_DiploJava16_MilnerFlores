package dgtic.core.repository;

import dgtic.core.model.Trabajo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrabajoRepository extends JpaRepository<Trabajo, Integer> {
    List<Trabajo> findByGrupoIdGrupo(Integer idGrupo);
}