package dgtic.core.model;

import jakarta.persistence.*;
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
@Table(name = "asignatura")
public class Asignatura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignatura")
    private Integer id;

    @NotBlank(message = "El nombre de la asignación es obligatoria.")
    @Size(max = 50, message = "El nombre no debe exceder los 50 caracteres.")
    @Column(name="nombre_asignatura", nullable = false)
    private String nombreAsignatura;

    @Column(name = "descripcion")
    private String descripcion;


    @OneToMany(mappedBy = "asignatura",fetch = FetchType.EAGER)
    private List<Grupo> grupos = new ArrayList<>();

}
