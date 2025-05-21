package dgtic.core.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import dgtic.core.model.PeriodoAcademico;
import dgtic.core.model.dto.ReporteFormDTO;
import dgtic.core.model.dto.TipoTrabajoPonderacionDTO;
import dgtic.core.service.ReporteGrupalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;

@SpringBootTest
public class GenerarReporteGrupoTest {

    @Autowired
    private ReporteGrupalService reporteGrupalService;

    @Test
    public void testGenerarReporteGrupo() {
        // Dado: Un grupo existente
        Integer idGrupo = 1;

        // Y un formulario con datos de prueba
        ReporteFormDTO reporteForm = new ReporteFormDTO();
        reporteForm.setNombrePeriodo("Periodo de Prueba");
        reporteForm.setFechaInicio(LocalDate.of(2025, 1, 1));
        reporteForm.setFechaFin(LocalDate.of(2025, 6, 30));
        reporteForm.setDescripcion("Descripción de periodo de prueba.");

        // Configuramos las ponderaciones esperadas (en este ejemplo se usan dos tipos de trabajo)
        TipoTrabajoPonderacionDTO ponderacion1 = new TipoTrabajoPonderacionDTO();
        ponderacion1.setIdTipoTrabajo(1);
        ponderacion1.setNombreTipoTrabajo("Examen");
        ponderacion1.setPonderacion(50.0);

        TipoTrabajoPonderacionDTO ponderacion2 = new TipoTrabajoPonderacionDTO();
        ponderacion2.setIdTipoTrabajo(2);
        ponderacion2.setNombreTipoTrabajo("Taller");
        ponderacion2.setPonderacion(50.0);

        reporteForm.setPonderaciones(Arrays.asList(ponderacion1, ponderacion2));

        // Cuando: Se invoca el metodo para generar el reporte
        PeriodoAcademico periodo = reporteGrupalService.generarReporteGrupo(idGrupo, reporteForm);

        // Entonces: Se debe retornar un objeto PeriodoAcademico no nulo con los datos esperados
        assertNotNull(periodo, "El período académico no debe ser nulo");
        assertEquals("Periodo de Prueba", periodo.getNombrePeriodo(), "El nombre del período es incorrecto");
        assertEquals(LocalDate.of(2025, 1, 1), periodo.getFechaInicio(), "La fecha de inicio es incorrecta");
        assertEquals(LocalDate.of(2025, 6, 30), periodo.getFechaFin(), "La fecha de fin es incorrecta");
        assertEquals("Descripción de periodo de prueba.", periodo.getDescripcion(), "La descripción del período es incorrecta");

    }
}
