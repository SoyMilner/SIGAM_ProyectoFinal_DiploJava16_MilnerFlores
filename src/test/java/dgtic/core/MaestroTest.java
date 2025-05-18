package dgtic.core;
import dgtic.core.model.Grupo;
import dgtic.core.repository.AsignaturaRepository;
import dgtic.core.repository.GrupoRepository;
import dgtic.core.service.MaestroService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MaestroTest {

    @Autowired
    private MaestroService maestroService;


    @Test
    void testEncontrarGruposMaestro() {

        // Recuperamos los grupos desde la base de datos
        List<Grupo> grupos = maestroService.obtenerGruposDeMaestro(2);

        for(Grupo gp: grupos){
            System.out.println(gp.getNombreGrupo()+ " "  + " Maestro:" +gp.getMaestro().getNombre());
        }

        // Validamos los resultados
        assertThat(grupos).isNotEmpty();


    }

    @Test
    void testCrearGrupoMaestro(){
        Grupo grupo = new Grupo();
        grupo.setNombreGrupo("Grupo Test 2");
        maestroService.crearGrupo(2,grupo);
    }
}
