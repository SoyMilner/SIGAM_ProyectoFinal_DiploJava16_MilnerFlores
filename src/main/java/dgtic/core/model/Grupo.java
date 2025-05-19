package dgtic.core.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "grupo")
public class Grupo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_grupo")
    private Integer idGrupo;

    @NotBlank(message = "El nombre del grupo es obligatorio.")
    @Size(max = 50, message = "El nombre no debe exceder los 50 caracteres.")
    @Column(name = "nombre_grupo", nullable = false)
    private String nombreGrupo;

    @ManyToOne
    @ToString.Exclude()
    @JoinColumn(name = "id_maestro", referencedColumnName = "id_maestro", nullable = false)
    private Maestro maestro = new Maestro();

    @ManyToOne
    @ToString.Exclude()
    @JoinColumn(name = "id_asignatura", referencedColumnName = "id_asignatura", nullable = true)
    private Asignatura asignatura = new Asignatura();

    //Al eliminar un grupo se borrarán los trabajos en cascada
    @OneToMany(mappedBy = "grupo",fetch =FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude()
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Trabajo> trabajos = new ArrayList<>();

    @ManyToMany(mappedBy = "grupos", fetch = FetchType.EAGER)
    @ToString.Exclude()
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Estudiante> estudiantes = new ArrayList<>();

}
