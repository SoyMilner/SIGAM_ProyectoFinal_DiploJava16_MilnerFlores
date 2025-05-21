package dgtic.core.repository;

import dgtic.core.model.PeriodoAcademico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeriodoAcademicoRepository extends JpaRepository<PeriodoAcademico, Integer> {
    PeriodoAcademico findByNombrePeriodo(String nombrePeriodo);
}
