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
@Table(name = "maestro")
public class Maestro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_maestro")
    private Integer idMaestro;

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 50, message = "El nombre no debe exceder los 50 caracteres.")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotBlank(message = "El apellido paterno es obligatorio.")
    @Size(max = 50, message = "No debe exceder los 50 caracteres.")
    @Column(name = "apellido_paterno", nullable = false)
    private String apellidoPaterno;

    @NotBlank(message = "El apellido paterno es obligatorio.")
    @Size(max = 50, message = "No debe exceder los 50 caracteres.")
    @Column(name = "apellido_materno", nullable = false)
    private String apellidoMaterno;

    @NotBlank(message = "El correo electrónico es obligatorio.")
    @Size(max = 50, message = "No debe exceder los 50 caracteres.")
    @Email
    @Column(name = "correo", nullable = false)
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(max = 80, message = "No debe exceder los 50 caracteres.")
    @Column(name = "contrasena", nullable = false)
    private String contrasena;

    @OneToMany(mappedBy = "maestro",fetch = FetchType.EAGER)
    private List<Grupo> grupos = new ArrayList<>();

}
