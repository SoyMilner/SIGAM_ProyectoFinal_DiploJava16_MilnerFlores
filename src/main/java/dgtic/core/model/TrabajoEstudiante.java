package dgtic.core.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "trabajo_estudiante")
public class TrabajoEstudiante {

    @EmbeddedId
    private TrabajoEstudianteId id; // Clave compuesta

    @ManyToOne
    @MapsId("idTrabajo")
    @JoinColumn(name = "id_trabajo")
    private Trabajo trabajo;

    @ManyToOne
    @MapsId("idEstudiante")
    @JoinColumn(name = "id_estudiante")
    private Estudiante estudiante;

    @DecimalMin(value = "0.0", message = "La calificación no puede ser menor a 0.")
    @Max(value = 10, message = "La calificación no puede ser mayor a 10.")
    @Column(name = "calificacion")
    private Double calificacion;

    @Column(name = "fecha_entrega")
    private LocalDate fechaEntrega;

    @Size(max = 500, message = "Los comentarios no pueden exceder los 500 caracteres.")
    @Column(name = "comentarios")
    private String comentarios;
}

//Clase que representa la relación Trabajo-Estudiante. Es necesaria porque se tienen atributos propios de esta relación,
//a diferencia de la relación Grupo-Estudiante.