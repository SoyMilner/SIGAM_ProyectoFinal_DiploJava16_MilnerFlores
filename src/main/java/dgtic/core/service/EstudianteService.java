package dgtic.core.service;

import dgtic.core.model.Estudiante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EstudianteService {

    List<Estudiante> obtenerTodosLosEstudiantes();
    Estudiante buscarPorId(Integer id);
    Estudiante guardarEstudiante(Estudiante estudiante);
    void eliminarEstudiante(Integer id);

    List<Estudiante> obtenerEstudiantesDeMaestro(Integer idMaestro);

    List<Estudiante> obtenerEstudiantesPorGrupoYMaestro(Integer idGrupo, Integer idMaestro);

    // Nuevo metodo para paginación (usaré 10 elementos por página)
    Page<Estudiante> obtenerEstudiantesPorGrupoYMaestroPag(Integer idGrupo, Integer idMaestro, Pageable pageable);
}
