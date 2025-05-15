package dgtic.core.service;

import dgtic.core.model.Grupo;

import java.util.List;

public interface GrupoService {
    List<Grupo> obtenerTodosLosGrupos();
    Grupo guardarGrupo(Grupo grupo);
    Grupo buscarPorId(Integer id);
    void eliminarGrupo(Integer id);
}
