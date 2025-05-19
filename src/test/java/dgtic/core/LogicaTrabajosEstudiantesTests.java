package dgtic.core;

import dgtic.core.model.Estudiante;
import dgtic.core.model.Trabajo;
import dgtic.core.model.TrabajoEstudiante;
import dgtic.core.service.EstudianteService;
import dgtic.core.service.TrabajoEstudianteService;
import dgtic.core.service.TrabajoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class LogicaTrabajosEstudiantesTests {

    @Autowired
    private EstudianteService estudianteService;

    @Autowired
    private TrabajoService trabajoService;

    @Autowired
    private TrabajoEstudianteService trabajoEstudianteService;

    // 1. Prueba para EstudianteService
    @Test
    void testObtenerEstudiantesDeMaestro_yPorGrupo() {
        // Asumimos que el maestro "Irma" tiene id 1
        Integer maestroId = 1;
        List<Estudiante> estudiantes = estudianteService.obtenerEstudiantesDeMaestro(maestroId);
        // Según los datos de prueba, Irma tiene 3 grupos con 5 estudiantes c/u, total 15.
        assertThat(estudiantes)
                .isNotNull()
                .hasSize(15);

        // Ahora, obtener solo los estudiantes del grupo con id 1 (por ejemplo, grupo "301" de Irma)
        List<Estudiante> estudiantesGrupo = estudianteService.obtenerEstudiantesPorGrupoYMaestro(1, maestroId);
        // En la muestra se asignaron 5 estudiantes a grupo 1.
        assertThat(estudiantesGrupo)
                .isNotNull()
                .hasSize(5);
    }


    // 2. Prueba para TrabajoService
    @Test
    void testTrabajosDeMaestro_yTrabajosPorGrupo_yBusquedaPorId() {
        // Para maestro id 1 (Irma), de acuerdo a la muestra de datos, hay trabajos en grupo 1 y grupo 2.
        // En los inserts se asignaron dos trabajos en grupo 1 y dos trabajos en grupo 2, total 4.
        List<Trabajo> trabajosMaestro = trabajoService.obtenerTrabajosDeMaestro(1);
        assertThat(trabajosMaestro)
                .isNotNull()
                .hasSize(4);

        // Probar obtener trabajos por grupo: Para grupo 1 se esperan 2 trabajos.
        List<Trabajo> trabajosGrupo1 = trabajoService.obtenerTrabajosPorGrupo(1);
        assertThat(trabajosGrupo1)
                .isNotNull()
                .hasSize(2);

        // Verificar la búsqueda del trabajo por ID.
        Trabajo trabajo = trabajoService.buscarPorId(1);
        assertThat(trabajo)
                .isNotNull()
                .extracting("nombreTrabajo")
                .isEqualTo("Examen Recuperación");  // Verifica el nombre
    }

    // 3. Prueba para TrabajoEstudianteService //Posible obsoleto
    @Test
    void testTrabajosCalificados_yNoCalificadosParaEstudiante() {
        // Para el estudiante del grupo 1, asumiendo que el estudiante con id 1 tiene dos registros en trabajo_estudiante,
        // todos con calificación asignada.
        Integer idEstudiante = 1;
        List<TrabajoEstudiante> calificados = trabajoEstudianteService.obtenerTrabajosCalificadosPorEstudiante(idEstudiante);
        List<TrabajoEstudiante> noCalificados = trabajoEstudianteService.obtenerTrabajosNoCalificadosPorEstudiante(idEstudiante); //Posible obsoleto

        // En la muestra, se asignaron 2 trabajos con calificación al estudiante con id 1.
        assertThat(calificados)
                .isNotNull()
                .hasSize(2);
        // Asumimos que no existen registros sin calificación para ese estudiante.
        assertThat(noCalificados)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void testObtenerTrabajosAsignadosPorEstudiante() {
        // Suponemos que el estudiante con id 1 pertenece al grupo 1, el cual tiene dos trabajos asignados.
        Integer idEstudiante = 1;

        List<Trabajo> trabajos = trabajoService.obtenerTrabajosAsignadosPorEstudiante(idEstudiante);

        // Verificar que no es nulo y contiene efectivamente dos trabajos.
        assertThat(trabajos)
                .isNotNull()
                .hasSize(2);

        // Opcional: Verificar que los nombres de los trabajos sean los esperados.
        assertThat(trabajos)
                .extracting("nombreTrabajo")
                .containsExactlyInAnyOrder("Examen Recuperación", "Proyecto Ensayo");
    }

    @Test
    void testObtenerTrabajosNoCalificadosDirecto() {
        // Suponiendo que el estudiante con id 1 tiene todos los trabajos ya calificados,
        // la consulta debe retornar una lista vacía.
        Integer idEstudiante = 1;

        // Llamamos al metodo que utiliza la query que busca los trabajos asignados pero no calificados.
        List<Trabajo> trabajosNoCalificados = trabajoService.obtenerTrabajosNoCalificadosPorEstudianteDirecto(idEstudiante);

        // Validamos que la lista retornada no sea nula y esté vacía.
        assertThat(trabajosNoCalificados)
                .isNotNull()
                .isEmpty();
    }

}
