package dgtic.core.service.impl;

import dgtic.core.model.Estudiante;
import dgtic.core.model.Grupo;
import dgtic.core.model.Trabajo;
import dgtic.core.repository.EstudianteRepository;
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

    @Autowired
    private EstudianteRepository estudianteRepository;

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
    public void eliminarGrupo(Integer idGrupo) {
        // Primero, obtenemos el grupo a eliminar
        Grupo grupo = grupoRepository.findById(idGrupo)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));

        // Eliminar el grupo. La BD tiene ON DELETE CASCADE, con esto
        // se eliminarán las filas en estudiante_grupo asociadas a este grupo.
        grupoRepository.delete(grupo);

        // Luego, buscar y eliminar los estudiantes sin asociaciones.
        // Esto asume que un estudiante sin ningún grupo es "huérfano" y se debe eliminar,
        List<Estudiante> todosLosEstudiantes = estudianteRepository.findAll();
        for (Estudiante estudiante : todosLosEstudiantes) {
            // Se verifica si el estudiante no tiene ninguna asociación en la tabla intermedia.
            if (estudiante.getGrupos() == null || estudiante.getGrupos().isEmpty()) {
                estudianteRepository.delete(estudiante);
            }
        }
    }


}
