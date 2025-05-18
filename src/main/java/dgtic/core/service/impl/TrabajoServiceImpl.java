package dgtic.core.service.impl;

import dgtic.core.model.Maestro;
import dgtic.core.model.Trabajo;
import dgtic.core.repository.TrabajoRepository;
import dgtic.core.service.TrabajoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrabajoServiceImpl implements TrabajoService {

    @Autowired
    private TrabajoRepository trabajoRepository;

    @Override
    public List<Trabajo> obtenerTodosLosTrabajos() {
        return trabajoRepository.findAll();
    }

    @Override
    public Trabajo buscarPorId(Integer id) {
        Optional<Trabajo> op=trabajoRepository.findById(id);
        return op.orElse(null);
    }


    @Override
    public Trabajo guardarTrabajo(Trabajo trabajo) {
        return trabajoRepository.save(trabajo);
    }

    @Override
    public void eliminarTrabajo(Integer id) {
        trabajoRepository.deleteById(id);
    }

    @Override
    public List<Trabajo> obtenerTrabajosPorGrupo(Integer idGrupo) {
        return trabajoRepository.findByGrupoIdGrupo(idGrupo);
    }

    @Override
    public List<Trabajo> obtenerTrabajosDeMaestro(Integer idMaestro) {
        return trabajoRepository.findByMaestroIdMaestro(idMaestro);
    }

    @Override
    public Maestro obtenerOwnerTrabajo(Integer idTrabajo) {
        return trabajoRepository.findOwnerTrabajo(idTrabajo);
    }
}
