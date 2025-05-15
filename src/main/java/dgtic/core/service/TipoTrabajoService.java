package dgtic.core.service;

import dgtic.core.model.TipoTrabajo;

import java.util.List;

public interface TipoTrabajoService {
    List<TipoTrabajo> obtenerTodosLosTiposTrabajo();
    TipoTrabajo buscarPorNombre(String nombre);
    TipoTrabajo guardarTipoTrabajo(TipoTrabajo tipoTrabajo);
}
