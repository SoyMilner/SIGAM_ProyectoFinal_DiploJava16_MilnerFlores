package dgtic.core.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "periodo_academico",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"nombre_periodo"})})
public class PeriodoAcademico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_periodo_academico")
    private Integer idPeriodoAcademico;

    @Column(name = "nombre_periodo", nullable = false, length = 50)
    private String nombrePeriodo;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "descripcion")
    private String descripcion;

    // Relación bidireccional: Un PeriodoAcademico tiene muchos Historiales
    @OneToMany(mappedBy = "periodoAcademico", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<HistorialCalificaciones> historiales = new ArrayList<>();

}
