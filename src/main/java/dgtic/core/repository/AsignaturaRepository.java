package dgtic.core.repository;

import dgtic.core.model.Asignatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsignaturaRepository extends JpaRepository<Asignatura, Integer> {
    Asignatura findByNombreAsignatura(String nombreAsignatura);
}
