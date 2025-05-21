package dgtic.core.service;

import dgtic.core.model.PeriodoAcademico;

public interface PeriodoAcademicoService {
    PeriodoAcademico guardarPeriodo(PeriodoAcademico periodo);
    PeriodoAcademico obtenerPorId(Integer id);
}
