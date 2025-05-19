package dgtic.core.service.impl;

import dgtic.core.model.Estudiante;
import dgtic.core.repository.EstudianteRepository;
import dgtic.core.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EstudianteServiceImpl implements EstudianteService {

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Override
    public List<Estudiante> obtenerTodosLosEstudiantes() {
        return estudianteRepository.findAll();
    }

    @Override
    public Estudiante buscarPorId(Integer id) {
        Optional<Estudiante> op=estudianteRepository.findById(id);
        return op.orElse(null);
    }

    @Override
    public Estudiante guardarEstudiante(Estudiante estudiante) {
        return estudianteRepository.save(estudiante);
    }

    @Override
    public void eliminarEstudiante(Integer id) {
        estudianteRepository.deleteById(id);
    }

    @Override
    public List<Estudiante> obtenerEstudiantesDeMaestro(Integer idMaestro) {
        return estudianteRepository.findByMaestroId(idMaestro);
    }

    @Override
    public List<Estudiante> obtenerEstudiantesPorGrupoYMaestro(Integer idGrupo, Integer idMaestro) {
        return estudianteRepository.findByGrupoAndMaestro(idGrupo, idMaestro);
    }

    @Override
    public Page<Estudiante> obtenerEstudiantesPorGrupoYMaestroPag(Integer idGrupo, Integer idMaestro, Pageable pageable) {
        return estudianteRepository.findByGrupoAndMaestro(idGrupo, idMaestro, pageable);
    }
}
