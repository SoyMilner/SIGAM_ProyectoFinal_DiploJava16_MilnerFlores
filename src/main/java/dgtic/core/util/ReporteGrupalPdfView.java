package dgtic.core.util;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import dgtic.core.model.*;
import dgtic.core.model.dto.TipoTrabajoPonderacionDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("pdfReporteGrupal")
public class ReporteGrupalPdfView extends AbstractPdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model,
                                    Document document, PdfWriter writer,
                                    HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Configurar el documento (quitando landscape para este ejemplo)
        // document.setPageSize(PageSize.A4.rotate());
        document.newPage();

        // Recuperación de datos
        // Se asume que en el modelo se han colocado estos objetos
        Grupo grupo = (Grupo) model.get("grupo");
        PeriodoAcademico periodo = (PeriodoAcademico) model.get("periodo");
        @SuppressWarnings("unchecked")
        List<HistorialCalificaciones> historiales =
                (List<HistorialCalificaciones>) model.get("historiales");
        @SuppressWarnings("unchecked")
        List<TipoTrabajoPonderacionDTO> ponderaciones =
                (List<TipoTrabajoPonderacionDTO>) model.get("ponderaciones");

        // 1. Mostrar el nombre del grupo encima del período
        // Se usa Color.decode para obtener el color hexadecimal #7B5F9A
        Paragraph groupName = new Paragraph(
                "Grupo: " + grupo.getNombreGrupo(),
                new Font(Font.HELVETICA, 20, Font.BOLD, Color.decode("#7B5F9A"))
        );
        groupName.setAlignment(Element.ALIGN_CENTER);
        document.add(groupName);

        // 2. Mostrar el título del período
        Paragraph title = new Paragraph(
                periodo.getNombrePeriodo(),
                new Font(Font.HELVETICA, 18, Font.BOLD, Color.BLACK)
        );
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // 3. Mostrar las fechas del período
        Paragraph dateRange = new Paragraph(
                "Desde " + periodo.getFechaInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        + " hasta " + periodo.getFechaFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK)
        );
        dateRange.setAlignment(Element.ALIGN_CENTER);
        document.add(dateRange);

        if (periodo.getDescripcion() != null && !periodo.getDescripcion().isEmpty()) {
            Paragraph descripcion = new Paragraph(
                    periodo.getDescripcion(),
                    new Font(Font.HELVETICA, 12, Font.ITALIC, Color.BLACK)
            );
            descripcion.setAlignment(Element.ALIGN_CENTER);
            document.add(descripcion);
        }
        document.add(new Paragraph(" ")); // Espacio en blanco

        // 4. Tabla de ponderaciones de cada tipo de trabajo
        PdfPTable tablePonderaciones = new PdfPTable(ponderaciones.size());
        tablePonderaciones.setWidthPercentage(100);
        for (TipoTrabajoPonderacionDTO dto : ponderaciones) {
            String celda = dto.getNombreTipoTrabajo() + " (" + dto.getPonderacion() + "%)";
            PdfPCell cell = new PdfPCell(new Phrase(celda, new Font(Font.HELVETICA, 10, Font.BOLD)));
            cell.setBackgroundColor(Color.decode("#E5E5E5"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tablePonderaciones.addCell(cell);
        }
        document.add(tablePonderaciones);
        document.add(new Paragraph(" ")); // Espacio

        // 5. Tabla para los registros de estudiantes
        // La tabla tendrá 3 columnas: Nombre Completo, Promedio Final y Calificaciones
        PdfPTable tableAlumnos = new PdfPTable(3);
        tableAlumnos.setWidthPercentage(100);
        tableAlumnos.setSpacingBefore(10f);

        // Encabezados: se personaliza el relleno (background) con Color.decode("#94AF92")
        Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);

        PdfPCell headerNombre = new PdfPCell(new Phrase("Nombre Completo", headerFont));
        headerNombre.setBackgroundColor(Color.decode("#94AF92"));
        headerNombre.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableAlumnos.addCell(headerNombre);

        PdfPCell headerProm = new PdfPCell(new Phrase("Promedio Final", headerFont));
        headerProm.setBackgroundColor(Color.decode("#94AF92"));
        headerProm.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableAlumnos.addCell(headerProm);

        PdfPCell headerCal = new PdfPCell(new Phrase("Calificaciones", headerFont));
        headerCal.setBackgroundColor(Color.decode("#94AF92"));
        headerCal.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableAlumnos.addCell(headerCal);

        // Filas con datos de cada alumno
        Font cellFont = new Font(Font.HELVETICA, 10);
        for (Estudiante estudiante : grupo.getEstudiantes()) {
            // Celda con el nombre completo
            String nombreCompleto = estudiante.getNombre() + " " +
                    estudiante.getApellidoPaterno() + " " +
                    estudiante.getApellidoMaterno();
            PdfPCell cellNombre = new PdfPCell(new Phrase(nombreCompleto, cellFont));
            cellNombre.setBackgroundColor(Color.decode("#E5E5E5"));
            tableAlumnos.addCell(cellNombre);

            // Celda con el promedio final obtenido del historial (buscando el registro para el alumno)
            double promedioFinal = 0.0;
            for (HistorialCalificaciones h : historiales) {
                if (h.getEstudiante().getIdEstudiante().equals(estudiante.getIdEstudiante())) {
                    promedioFinal = h.getPromedio();
                    break;
                }
            }
            PdfPCell cellProm = new PdfPCell(new Phrase(String.format("%.2f", promedioFinal), cellFont));
            cellProm.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellProm.setBackgroundColor(Color.decode("#E5E5E5"));
            tableAlumnos.addCell(cellProm);

            // Celda con la lista de calificaciones agrupadas por tipo de trabajo
            String calificaciones;
            if (estudiante.getTrabajosEstudiantes() != null && !estudiante.getTrabajosEstudiantes().isEmpty()) {
                Map<String, List<Double>> calificacionesPorTipo = estudiante.getTrabajosEstudiantes().stream()
                        .filter(te -> te.getTrabajo() != null && te.getTrabajo().getTipoTrabajo() != null)
                        .collect(Collectors.groupingBy(
                                te -> te.getTrabajo().getTipoTrabajo().getNombreTipoTrabajo(),
                                Collectors.mapping(TrabajoEstudiante::getCalificacion, Collectors.toList())
                        ));
                calificaciones = calificacionesPorTipo.entrySet().stream()
                        .map(entry -> entry.getKey() + ": " +
                                entry.getValue().stream()
                                        .map(nota -> String.format("%.2f", nota))
                                        .collect(Collectors.joining(", ")))
                        .collect(Collectors.joining("\n"));
            } else {
                calificaciones = "-";
            }
            PdfPCell cellCal = new PdfPCell(new Phrase(calificaciones, cellFont));
            cellCal.setBackgroundColor(Color.decode("#E5E5E5"));
            cellCal.setPadding(5f);
            tableAlumnos.addCell(cellCal);
        }
        document.add(tableAlumnos);
    }

}
