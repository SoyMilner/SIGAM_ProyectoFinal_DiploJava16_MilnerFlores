package dgtic.core.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "estudiante")
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estudiante")
    private Integer idEstudiante;

    @NotBlank(message = "El nombre del estudiante es obligatorio.")
    @Size(max = 50, message = "El nombre no debe exceder los 50 caracteres.")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotBlank(message = "El apellido paterno del estudiante es obligatorio.")
    @Size(max = 50, message = "El apellido paterno no debe exceder los 50 caracteres.")
    @Column(name = "apellido_paterno", nullable = false)
    private String apellidoPaterno;

    @NotBlank(message = "El apellido materno del estudiante es obligatorio.")
    @Size(max = 50, message = "El apellido materno no debe exceder los 50 caracteres.")
    @Column(name = "apellido_materno", nullable = false)
    private String apellidoMaterno;

    @NotBlank(message = "La matrícula del estudiante es obligatoria.")
    @Size(max = 50, message = "La matrícula no debe exceder los 50 caracteres.")
    @Column(name = "matricula", nullable = false)
    private String matricula;

    @NotBlank(message = "El correo del estudiante es obligatorio.")
    @Size(max = 60, message = "El correo no debe exceder los 60 caracteres.")
    @Email
    @Column(name = "correo_electronico", nullable = false)
    private String correoElectronico;

    @ManyToMany
    @JoinTable(
            name = "estudiante_grupo",
            joinColumns = @JoinColumn(name = "id_estudiante"),
            inverseJoinColumns = @JoinColumn(name = "id_grupo")
    )
    @ToString.Exclude()
    private List<Grupo> grupos = new ArrayList<>();

    @OneToMany(mappedBy = "estudiante")
    @ToString.Exclude()
    private List<TrabajoEstudiante> trabajosEstudiantes = new ArrayList<>();

    @ManyToOne
    @ToString.Exclude()
    @JoinColumn(name = "id_maestro", referencedColumnName = "id_maestro", nullable = false)
    private Maestro maestro;

    // Relación bidireccional con HistorialCalificaciones
    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude()
    private List<HistorialCalificaciones> historiales = new ArrayList<>();


}

