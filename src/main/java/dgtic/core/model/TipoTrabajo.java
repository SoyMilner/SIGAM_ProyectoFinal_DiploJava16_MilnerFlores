package dgtic.core.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tipo_trabajo")
public class TipoTrabajo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_trabajo")
    private Integer idTipoTrabajo;

    @NotBlank(message = "{tipoTrabajo.nombreTipoTrabajo.notBlank}")
    @Size(max = 50, message = "{tipoTrabajo.nombreTipoTrabajo.size}")
    @Column(name = "nombre_tipo_trabajo", nullable = false)
    private String nombreTipoTrabajo;

    @OneToMany(mappedBy = "tipoTrabajo", fetch = FetchType.EAGER)
    @ToString.Exclude()
    private List<Trabajo> trabajos = new ArrayList<>();
}
