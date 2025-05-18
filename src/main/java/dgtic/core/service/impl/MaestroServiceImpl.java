package dgtic.core.service.impl;

import java.util.List;

import dgtic.core.service.MaestroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dgtic.core.model.Maestro;
import dgtic.core.model.Grupo;
import dgtic.core.repository.MaestroRepository;
import dgtic.core.repository.GrupoRepository;

@Service
public class MaestroServiceImpl implements MaestroService {

    @Autowired
    private MaestroRepository maestroRepository;

    @Autowired
    private GrupoRepository grupoRepository;

    @Override
    public Maestro buscarPorId(Integer idMaestro) {
        return maestroRepository.findById(idMaestro).orElse(null);
    }

    @Override
    public List<Grupo> obtenerGruposDeMaestro(Integer idMaestro) {
        return grupoRepository.findByMaestroIdMaestro(idMaestro);
    }

    @Override
    public void crearGrupo(Integer idMaestro, Grupo grupo) {
        Maestro maestro = buscarPorId(idMaestro);

        if (maestro == null) {
            throw new IllegalArgumentException("El maestro no existe.");
        }

        grupo.setMaestro(maestro);
        grupoRepository.save(grupo);
    }

    @Override
    public Maestro buscarPorCorreo(String correo) {
        return maestroRepository.findByCorreo(correo).orElse(null);
    }


}
