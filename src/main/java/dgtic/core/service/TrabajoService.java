package dgtic.core.service;

import dgtic.core.model.Trabajo;

import java.util.List;

public interface TrabajoService {
    List<Trabajo> obtenerTodosLosTrabajos();
    Trabajo buscarPorId(Integer id);
    Trabajo guardarTrabajo(Trabajo trabajo);
    void eliminarTrabajo(Integer id);
    List<Trabajo> obtenerTrabajosPorGrupo(Integer idGrupo);
}
