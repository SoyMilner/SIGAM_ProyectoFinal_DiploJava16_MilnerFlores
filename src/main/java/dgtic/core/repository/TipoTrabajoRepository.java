package dgtic.core.repository;

import dgtic.core.model.TipoTrabajo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoTrabajoRepository extends JpaRepository<TipoTrabajo, Integer> {
    TipoTrabajo findByNombreTipoTrabajo(String nombreTipoTrabajo);
}
