package dgtic.core.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "historial_calificaciones")
public class HistorialCalificaciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial_calificaciones")
    private Integer idHistorialCalificaciones;

    @ManyToOne
    @JoinColumn(name = "id_estudiante", referencedColumnName = "id_estudiante", nullable = false)
    @ToString.Exclude()
    private Estudiante estudiante;

    @ManyToOne
    @JoinColumn(name = "id_periodo_academico", referencedColumnName = "id_periodo_academico", nullable = false)
    @ToString.Exclude()
    private PeriodoAcademico periodoAcademico;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    @Column(name = "promedio", nullable = false)
    private Double promedio;

    @Column(name = "comentarios")
    private String comentarios;

}
