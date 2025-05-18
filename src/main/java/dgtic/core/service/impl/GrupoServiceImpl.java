package dgtic.core.service.impl;

import dgtic.core.model.Grupo;
import dgtic.core.model.Trabajo;
import dgtic.core.repository.GrupoRepository;
import dgtic.core.service.GrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GrupoServiceImpl implements GrupoService {

    @Autowired
    private GrupoRepository grupoRepository;

    @Override
    public List<Grupo> obtenerTodosLosGrupos() {
        return grupoRepository.findAll();
    }

//    @Override
//    public List<Grupo> obtenerGruposDeMaestro(Integer idMaestro) {
//        return grupoRepository.findByMaestroIdMaestro(idMaestro);
//    }

    @Override
    public Grupo guardarGrupo(Grupo grupo) {
        return grupoRepository.save(grupo);
    }

    @Override
    public Grupo buscarPorId(Integer id) {
        Optional<Grupo> op=grupoRepository.findById(id);
        return op.orElse(null);
    }

    @Override
    public void eliminarGrupo(Integer id) {
        grupoRepository.deleteById(id);
    }


}
