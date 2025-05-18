package dgtic.core.service;

import dgtic.core.model.Grupo;

import java.util.List;

public interface GrupoService {
    List<Grupo> obtenerTodosLosGrupos();
//    List<Grupo> obtenerGruposDeMaestro(Integer idMaestro);
    Grupo guardarGrupo(Grupo grupo);
    Grupo buscarPorId(Integer id);
    void eliminarGrupo(Integer id);
}
