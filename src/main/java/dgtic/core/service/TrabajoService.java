package dgtic.core.service;

import dgtic.core.model.Maestro;
import dgtic.core.model.Trabajo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TrabajoService {
    List<Trabajo> obtenerTodosLosTrabajos();
    Trabajo buscarPorId(Integer id);
    Trabajo guardarTrabajo(Trabajo trabajo);
    void eliminarTrabajo(Integer id);
    List<Trabajo> obtenerTrabajosPorGrupo(Integer idGrupo);
    List<Trabajo> obtenerTrabajosDeMaestro(Integer idMaestro);
    Maestro obtenerOwnerTrabajo(Integer idTrabajo);
    List<Trabajo> obtenerTrabajosAsignadosPorEstudiante(Integer idEstudiante);
    //  Metodo para obtener los trabajos asignados pero no calificados directamente
    List<Trabajo> obtenerTrabajosNoCalificadosPorEstudianteDirecto(Integer idEstudiante);

    // Metodo paginado para trabajos asignados al estudiante
    Page<Trabajo> obtenerTrabajosAsignadosPorEstudiante(Integer idEstudiante, Pageable pageable);

    // Metodo paginado para trabajos asignados a un grupo
    Page<Trabajo> obtenerTrabajosPorGrupoPag(Integer idGrupo, Pageable pageable);

    // Nuevo metodo para paginación de trabajos asignados al maestro
    Page<Trabajo> obtenerTrabajosDeMaestroPag(Integer idMaestro, Pageable pageable);

}
