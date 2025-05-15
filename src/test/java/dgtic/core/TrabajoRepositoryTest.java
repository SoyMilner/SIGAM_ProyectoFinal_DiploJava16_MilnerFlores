package dgtic.core;

import dgtic.core.model.Asignatura;
import dgtic.core.model.Trabajo;
import dgtic.core.model.TipoTrabajo;
import dgtic.core.model.Grupo;
import dgtic.core.repository.AsignaturaRepository;
import dgtic.core.repository.GrupoRepository;
import dgtic.core.repository.TipoTrabajoRepository;
import dgtic.core.repository.TrabajoRepository;
import dgtic.core.service.GrupoService;
import dgtic.core.service.TrabajoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TrabajoRepositoryTest {

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    @Autowired
    private TrabajoRepository trabajoRepository;

    @Autowired
    private TipoTrabajoRepository tipoTrabajoRepository;


    @Autowired
    private TrabajoService trabajoService;

    @Autowired
    private GrupoService grupoService;

    @Test
    void testTrabajoFindAll() {

        // Recuperamos los trabajos desde la base de datos
        List<Trabajo> trabajos = trabajoRepository.findAll();

        for(Trabajo tr: trabajos){
            System.out.println(tr.getNombreTrabajo()+ " " + tr.getTipoTrabajo().getNombreTipoTrabajo());
        }

        // Validamos los resultados
        assertThat(trabajos).isNotEmpty();


    }


    @Test
    void testTrabajoPorGrupo() {
        // ID del grupo a probar (ajusta según tu base de datos)
        Integer idGrupoPrueba = 1;

        // Verificamos que el grupo existe
        Grupo grupo = grupoService.buscarPorId(idGrupoPrueba);
        assertThat(grupo).isNotNull();

        // Recuperamos los trabajos asociados al grupo
        List<Trabajo> trabajos = trabajoService.obtenerTrabajosPorGrupo(idGrupoPrueba);

        // Mostramos los resultados en consola
        System.out.println("Trabajos del Grupo " + grupo.getNombreGrupo() + ":");
        trabajos.forEach(tr -> System.out.println(tr.getNombreTrabajo()));

        // Validamos que no esté vacío
        assertThat(trabajos).isNotEmpty();
    }

}
