package dgtic.core.service.impl;

import dgtic.core.model.Grupo;
import dgtic.core.model.Maestro;
import dgtic.core.model.Trabajo;
import dgtic.core.repository.GrupoRepository;
import dgtic.core.repository.TrabajoRepository;
import dgtic.core.service.TrabajoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrabajoServiceImpl implements TrabajoService {

    @Autowired
    private TrabajoRepository trabajoRepository;

    @Autowired
    private GrupoRepository grupoRepository;

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

    //Se modificó eliminar porque se está utilizando OrphanRemoval para cuando se elimina un grupo
    @Override
    public void eliminarTrabajo(Integer id) {
        // Buscar el trabajo
        Trabajo trabajo = trabajoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trabajo no encontrado"));

        // Obtener el grupo al que pertenece el trabajo
        Grupo grupo = trabajo.getGrupo();
        if (grupo != null) {
            // Remover el trabajo de la colección en el grupo.
            // Esto activará el orphanRemoval.
            grupo.getTrabajos().remove(trabajo);
            // Se guarda para propagar el cambio
            grupoRepository.save(grupo);
        } else {
            // Alternativamente, si no tienes el grupo, eliminar directamente:
            trabajoRepository.delete(trabajo);
        }
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

    @Override
    public List<Trabajo> obtenerTrabajosAsignadosPorEstudiante(Integer idEstudiante) {
        return trabajoRepository.findTrabajosAsignadosPorEstudiante(idEstudiante);
    }

    @Override
    public List<Trabajo> obtenerTrabajosNoCalificadosPorEstudianteDirecto(Integer idEstudiante) {
        return trabajoRepository.findTrabajosNoCalificadosDirecto(idEstudiante);
    }

    @Override
    public Page<Trabajo> obtenerTrabajosAsignadosPorEstudiante(Integer idEstudiante, Pageable pageable) {
        return trabajoRepository.findTrabajosAsignadosPorEstudiante(idEstudiante, pageable);
    }

    @Override
    public Page<Trabajo> obtenerTrabajosPorGrupoPag(Integer idGrupo, Pageable pageable) {
        return trabajoRepository.findTrabajosPorGrupoPag(idGrupo, pageable);
    }

    @Override
    public Page<Trabajo> obtenerTrabajosDeMaestroPag(Integer idMaestro, Pageable pageable) {
        return trabajoRepository.findTrabajosDeMaestroPag(idMaestro, pageable);
    }
}
