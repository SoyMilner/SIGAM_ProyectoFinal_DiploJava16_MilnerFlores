package dgtic.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CalificacionTrabajoDTO {
    private Integer idEstudiante;
    private String nombreCompleto;
    private Double calificacion;
    private LocalDate fechaEntrega;
    private String comentarios;

}
