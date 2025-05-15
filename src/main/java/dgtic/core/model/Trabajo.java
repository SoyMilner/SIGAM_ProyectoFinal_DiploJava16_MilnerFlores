package dgtic.core.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "trabajo")
public class Trabajo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_trabajo")
    private Integer idTrabajo;

    @NotBlank(message = "{trabajo.nombreTrabajo.notBlank}")
    @Size(max = 100, message = "{trabajo.nombreTrabajo.size}")
    @Column(name = "nombre_trabajo", nullable = false)
    private String nombreTrabajo;

    @NotNull(message = "{trabajo.fechaAsignacion.notNull}")
    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDate fechaAsignacion;

    @NotNull(message = "{trabajo.fechaLimite.notNull}")
    @Future(message = "{trabajo.fechaLimite.future}")
    @Column(name = "fecha_limite", nullable = false)
    private LocalDate fechaLimite;

    @Size(max = 500, message = "{trabajo.descripcion.size}")
    @Column(name = "descripcion")
    private String descripcion;

    @NotNull(message = "{trabajo.grupo.notNull}")
    @ManyToOne
    @JoinColumn(name = "id_grupo", referencedColumnName = "id_grupo", nullable = false)
    private Grupo grupo;

    @Min(value = 0, message = "{trabajo.ponderacionFinal.min}")
    @Max(value = 100, message = "{trabajo.ponderacionFinal.max}")
    @Column(name = "ponderacion_final")
    private Double ponderacionFinal;

    @NotNull(message = "{trabajo.tipoTrabajo.notNull}")
    @ManyToOne
    @JoinColumn(name = "id_tipo_trabajo", referencedColumnName = "id_tipo_trabajo", nullable = false)
    @ToString.Exclude()
    private TipoTrabajo tipoTrabajo;
}
