package dgtic.core.service.impl;

import dgtic.core.model.TrabajoEstudiante;
import dgtic.core.repository.TrabajoEstudianteRepository;
import dgtic.core.service.TrabajoEstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TrabajoEstudianteServiceImpl implements TrabajoEstudianteService {

    @Autowired
    private TrabajoEstudianteRepository trabajoEstudianteRepository;

    @Override
    public List<TrabajoEstudiante> obtenerTrabajosCalificadosPorEstudiante(Integer idEstudiante) {
        return trabajoEstudianteRepository.findCalificadosByEstudianteId(idEstudiante);
    }

    @Override
    public List<TrabajoEstudiante> obtenerTrabajosNoCalificadosPorEstudiante(Integer idEstudiante) {
        return trabajoEstudianteRepository.findNoCalificadosByEstudianteId(idEstudiante);
    }

    @Override
    public TrabajoEstudiante asignarCalificacion(TrabajoEstudiante trabajoEstudiante) {
        // Aquí se podría incluir lógica para validar el rango de calificación, etc.
        return trabajoEstudianteRepository.save(trabajoEstudiante);
    }
}
