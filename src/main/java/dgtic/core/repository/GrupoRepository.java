package dgtic.core.repository;

import dgtic.core.model.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Integer> {
    List<Grupo> findByMaestroIdMaestro(Integer idMaestro);
}
