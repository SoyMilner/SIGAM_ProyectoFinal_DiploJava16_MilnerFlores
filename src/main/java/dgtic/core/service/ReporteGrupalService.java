package dgtic.core.service;

import dgtic.core.model.PeriodoAcademico;
import dgtic.core.model.dto.ReporteFormDTO;

public interface ReporteGrupalService {
    PeriodoAcademico generarReporteGrupo(Integer idGrupo, ReporteFormDTO reporteForm);
}
