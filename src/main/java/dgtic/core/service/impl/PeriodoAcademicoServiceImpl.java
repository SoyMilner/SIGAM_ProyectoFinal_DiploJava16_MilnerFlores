package dgtic.core.service.impl;

import dgtic.core.model.PeriodoAcademico;
import dgtic.core.repository.PeriodoAcademicoRepository;
import dgtic.core.service.PeriodoAcademicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PeriodoAcademicoServiceImpl implements PeriodoAcademicoService {

    @Autowired
    private PeriodoAcademicoRepository periodoAcademicoRepository;

    @Override
    public PeriodoAcademico guardarPeriodo(PeriodoAcademico periodo) {
        return periodoAcademicoRepository.save(periodo);
    }

    @Override
    public PeriodoAcademico obtenerPorId(Integer id) {
        return periodoAcademicoRepository.findById(id).orElse(null);
    }
}
