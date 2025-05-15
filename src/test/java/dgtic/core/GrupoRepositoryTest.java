package dgtic.core;

import dgtic.core.model.Asignatura;
import dgtic.core.model.Grupo;
import dgtic.core.repository.AsignaturaRepository;
import dgtic.core.repository.GrupoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GrupoRepositoryTest {

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    @Test
    void testGrupoFindAll() {

        // Recuperamos los grupos desde la base de datos
        List<Grupo> grupos = grupoRepository.findAll();

        for(Grupo gp: grupos){
            System.out.println(gp.getNombreGrupo()+ " " + gp.getAsignatura().getNombreAsignatura());
        }

        // Validamos los resultados
        assertThat(grupos).isNotEmpty();


    }
}
