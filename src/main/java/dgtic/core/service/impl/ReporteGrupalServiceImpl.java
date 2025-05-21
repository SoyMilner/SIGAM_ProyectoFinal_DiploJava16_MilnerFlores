package dgtic.core.service.impl;

import dgtic.core.model.*;
import dgtic.core.model.dto.ReporteFormDTO;
import dgtic.core.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReporteGrupalServiceImpl implements ReporteGrupalService{

    @Autowired
    private GrupoService grupoService;

    @Autowired
    private PeriodoAcademicoService periodoAcademicoService;

    @Autowired
    private TrabajoEstudianteService trabajoEstudianteService;

    @Autowired
    private HistorialCalificacionesService historialCalificacionesService;


//     * Genera el reporte para el grupo indicado a partir de los datos del formulario.
//     * Se crea un nuevo período académico, se calculan los promedios ponderados para cada estudiante
//     * y se generan los registros en el historial.
//     *
//     * @param idGrupo El id del grupo.
//     * @param reporteForm Los datos del formulario que contiene los porcentajes y datos periodísticos.
//     * @return El objeto PeriodoAcademico creado.

    public PeriodoAcademico generarReporteGrupo(Integer idGrupo, ReporteFormDTO reporteForm) {
        // Recupera el grupo
        Grupo grupo = grupoService.buscarPorId(idGrupo);

        // Crear y guardar el período académico
        PeriodoAcademico periodo = new PeriodoAcademico();
        periodo.setNombrePeriodo(reporteForm.getNombrePeriodo());
        periodo.setFechaInicio(reporteForm.getFechaInicio());
        periodo.setFechaFin(reporteForm.getFechaFin());
        periodo.setDescripcion(reporteForm.getDescripcion());
        periodo = periodoAcademicoService.guardarPeriodo(periodo);

        // Mapear ponderaciones por idTipoTrabajo, que serán extraídas desde el formulario
        Map<Integer, Double> mapaPonderaciones = reporteForm.getPonderaciones().stream()
                .collect(Collectors.toMap(
                        dto -> dto.getIdTipoTrabajo(),
                        dto -> dto.getPonderacion()
                ));

        // Para cada alumno del grupo, calcular el promedio final ponderado
        for (Estudiante estudiante : grupo.getEstudiantes()) {
            double promedioFinal = 0.0;
            // Recorrer cada tipo de trabajo que se usó
            for (Map.Entry<Integer, Double> entry : mapaPonderaciones.entrySet()) {
                Integer idTipo = entry.getKey();
                Double ponderacion = entry.getValue();
                // Filtrar los trabajos del grupo que tengan ese tipo de trabajo
                List<Trabajo> trabajosTipo = grupo.getTrabajos().stream()
                        .filter(t -> t.getTipoTrabajo().getIdTipoTrabajo().equals(idTipo))
                        .collect(Collectors.toList());
                if (!trabajosTipo.isEmpty()) {
                    double sumaNotas = 0.0;
                    int contador = 0;
                    for (Trabajo trabajo : trabajosTipo) {
                        Optional<TrabajoEstudiante> teOpt =
                                trabajoEstudianteService.findByTrabajoAndEstudiante(trabajo, estudiante);
                        double nota = teOpt.map(TrabajoEstudiante::getCalificacion).orElse(0.0);
                        sumaNotas += nota;
                        contador++;
                    }
                    double promedioTipo = (contador > 0) ? sumaNotas / contador : 0.0;
                    // Se aplica la ponderación (la fórmula: calificación * (porcentaje/100))
                    promedioFinal += (promedioTipo * (ponderacion / 100.0));
                }
            }
            // Crear y guardar el registro en historial_calificaciones para el estudiante
            HistorialCalificaciones historial = new HistorialCalificaciones();
            historial.setEstudiante(estudiante);
            historial.setPeriodoAcademico(periodo);
            historial.setFechaRegistro(LocalDate.now());
            historial.setPromedio(Math.min(Math.max(promedioFinal, 0.0), 10.0));
            historial.setComentarios(""); // Inicialmente vacío
            historialCalificacionesService.guardarHistorial(historial);
        }

        // Se devuelve el período académico recién creado
        return periodo;
    }
}
