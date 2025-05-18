package dgtic.core.service;

import dgtic.core.model.Grupo;
import dgtic.core.model.Maestro;

import java.util.List;

public interface MaestroService {
    Maestro buscarPorId(Integer idMaestro);
    List<Grupo> obtenerGruposDeMaestro(Integer idMaestro);
    void crearGrupo(Integer idMaestro, Grupo grupo);
    Maestro buscarPorCorreo(String correo);
}
