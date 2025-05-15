package dgtic.core.service.impl;

import dgtic.core.model.TipoTrabajo;
import dgtic.core.repository.TipoTrabajoRepository;
import dgtic.core.service.TipoTrabajoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoTrabajoServiceImpl implements TipoTrabajoService {

    @Autowired
    private TipoTrabajoRepository tipoTrabajoRepository;

    @Override
    public List<TipoTrabajo> obtenerTodosLosTiposTrabajo() {
        return tipoTrabajoRepository.findAll();
    }

    @Override
    public TipoTrabajo buscarPorNombre(String nombre) {
        return tipoTrabajoRepository.findByNombreTipoTrabajo(nombre);
    }

    @Override
    public TipoTrabajo guardarTipoTrabajo(TipoTrabajo tipoTrabajo) {
        return tipoTrabajoRepository.save(tipoTrabajo);
    }

}
