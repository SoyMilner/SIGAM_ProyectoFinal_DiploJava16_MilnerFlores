package dgtic.core.model.dto;

import dgtic.core.model.Trabajo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrabajoAlumnoDTO {
    private Trabajo trabajo;
    private Double calificacion;
    private LocalDate fechaEntrega;
    private String comentarios;

}
