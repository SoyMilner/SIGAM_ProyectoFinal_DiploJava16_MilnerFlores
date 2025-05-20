package dgtic.core.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "trabajo")
public class Trabajo {

    public Trabajo(Integer idTrabajo, String nombreTrabajo, LocalDate fechaAsignacion, LocalDate fechaLimite,
                   String descripcion, Grupo grupo, Double ponderacionHistorica, TipoTrabajo tipoTrabajo) {
        this.idTrabajo = idTrabajo;
        this.nombreTrabajo = nombreTrabajo;
        this.fechaAsignacion = fechaAsignacion;
        this.fechaLimite = fechaLimite;
        this.descripcion = descripcion;
        this.grupo = grupo;
        this.ponderacionHistorica = 0.0; // Inicialmente será 0, se podrá guardar en el sistema para mejorar experiencia de usuario pero no lo modificará el usuario.
        this.tipoTrabajo = tipoTrabajo;
    }

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
    @ToString.Exclude()
    private Grupo grupo;

    @Column(name = "ponderacion_historica", columnDefinition = "DECIMAL(5,2) DEFAULT 0.0")
    private Double ponderacionHistorica=0.0;

    @NotNull(message = "{trabajo.tipoTrabajo.notNull}")
    @ManyToOne
    @JoinColumn(name = "id_tipo_trabajo", referencedColumnName = "id_tipo_trabajo", nullable = false)
    @ToString.Exclude()
    private TipoTrabajo tipoTrabajo;

    @OneToMany(mappedBy = "trabajo")
    @ToString.Exclude()
    private List<TrabajoEstudiante> estudiantesTrabajos = new ArrayList<>();


}
