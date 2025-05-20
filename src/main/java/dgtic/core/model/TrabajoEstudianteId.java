package dgtic.core.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class TrabajoEstudianteId implements Serializable {

    @Column(name = "id_trabajo")
    private Integer idTrabajo;

    @Column(name = "id_estudiante")
    private Integer idEstudiante;
}

//Llave compuesta que se utiliza en la relación entre trabajos y estudiantes