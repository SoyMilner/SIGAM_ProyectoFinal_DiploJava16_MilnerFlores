package dgtic.core;

import dgtic.core.model.Asignatura;
import dgtic.core.repository.AsignaturaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AsignaturaRepositoryTest {

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    @Test
    void testFindAll() {

        // Recuperamos las asignaturas desde la base de datos
        List<Asignatura> asignaturas = asignaturaRepository.findAll();

        // Validamos los resultados
        assertThat(asignaturas).isNotEmpty();
        //assertThat(asignaturas.size()).isEqualTo(2);
        assertThat(asignaturas.get(0).getNombreAsignatura()).isEqualTo("Matemáticas");
        assertThat(asignaturas.get(1).getNombreAsignatura()).isEqualTo("Español");

        for(Asignatura asg: asignaturas){
            System.out.println(asg);
        }

    }
}
