package dgtic.core.repository;

import dgtic.core.model.Grupo;
import dgtic.core.model.Maestro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MaestroRepository extends JpaRepository<Maestro, Integer> {
    List<Grupo> findGruposByIdMaestro(Integer idMaestro);
    Optional<Maestro> findByCorreo(String correo);
}
