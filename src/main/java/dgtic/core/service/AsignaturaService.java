package dgtic.core.service;

import dgtic.core.model.Asignatura;

import java.util.List;

public interface AsignaturaService {
    List<Asignatura> obtenerTodasLasAsignaturas();
    Asignatura buscarPorNombre(String nombre);
    Asignatura guardarAsignatura(Asignatura asignatura);

}
