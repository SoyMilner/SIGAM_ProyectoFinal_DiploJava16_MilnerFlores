package dgtic.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoTrabajoPonderacionDTO {
    private Integer idTipoTrabajo;
    private String nombreTipoTrabajo;
    private Double ponderacion; // porcentaje asignado (la suma debe ser 100)

}
