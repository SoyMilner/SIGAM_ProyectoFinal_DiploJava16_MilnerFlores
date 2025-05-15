package dgtic.core.service.impl;

import dgtic.core.model.Asignatura;
import dgtic.core.repository.AsignaturaRepository;
import dgtic.core.service.AsignaturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsignaturaServiceImpl implements AsignaturaService {

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    @Override
    public List<Asignatura> obtenerTodasLasAsignaturas() {
        return asignaturaRepository.findAll();
    }

    @Override
    public Asignatura buscarPorNombre(String nombre) {
        return asignaturaRepository.findByNombreAsignatura(nombre);
    }

    @Override
    public Asignatura guardarAsignatura(Asignatura asignatura) {
        return asignaturaRepository.save(asignatura);
    }

}
