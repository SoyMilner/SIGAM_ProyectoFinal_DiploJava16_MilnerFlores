package dgtic.core.controller;

import dgtic.core.model.dto.ReporteFormDTO;
import dgtic.core.model.dto.TipoTrabajoPonderacionDTO;
import dgtic.core.model.*;
import dgtic.core.service.*;
import dgtic.core.security.CookieUtil;
import dgtic.core.security.jwt.JwtUtil;
import dgtic.core.util.ReporteGrupalPdfView;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.DataIntegrityViolationException;


import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/grupos/{idGrupo}/reportes")
public class ReporteController {

    @Autowired
    private ReporteGrupalPdfView reporteGrupalPdfView;

    @Autowired
    private ReporteGrupalService reporteGrupalService;

    @Autowired
    private GrupoService grupoService;
    @Autowired
    private TrabajoService trabajoService;
    @Autowired
    private TrabajoEstudianteService trabajoEstudianteService;
    @Autowired
    private HistorialCalificacionesService historialCalificacionesService;
    @Autowired
    private PeriodoAcademicoService periodoAcademicoService;
    @Autowired
    private EstudianteService estudianteService;
    @Autowired
    private MaestroService maestroService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CookieUtil cookieUtil;

    private Integer obtenerIdMaestro(HttpServletRequest request) {
        String token = cookieUtil.extractTokenFromCookie(request);
        if(token == null || token.trim().isEmpty())
            return null;
        String correo = jwtUtil.extractUsername(token);
        Maestro maestro = maestroService.buscarPorCorreo(correo);
        return (maestro != null) ? maestro.getIdMaestro() : null;
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioReporte(@PathVariable("idGrupo") Integer idGrupo,
                                           HttpServletRequest request,
                                           Model model, RedirectAttributes redirectAttributes) {
        Integer idMaestro = obtenerIdMaestro(request);
        if(idMaestro == null)
            return "redirect:/login?sessionExpired=true";
        Grupo grupo = grupoService.buscarPorId(idGrupo);
        if(grupo == null || !grupo.getMaestro().getIdMaestro().equals(idMaestro))
            return "redirect:/index?accessDenied=true";

        // Extraer los tipos de trabajo que se han usado en los trabajos del grupo
        List<Trabajo> trabajos = grupo.getTrabajos();
        Set<TipoTrabajo> tipos = trabajos.stream()
                .map(Trabajo::getTipoTrabajo)
                .collect(Collectors.toSet());

        List<TipoTrabajoPonderacionDTO> listaPonderaciones = new ArrayList<>();
        for(TipoTrabajo tipo : tipos) {
            TipoTrabajoPonderacionDTO dto = new TipoTrabajoPonderacionDTO();
            dto.setIdTipoTrabajo(tipo.getIdTipoTrabajo());
            dto.setNombreTipoTrabajo(tipo.getNombreTipoTrabajo());
            dto.setPonderacion(0.0);
            listaPonderaciones.add(dto);
        }

        ReporteFormDTO reporteForm = new ReporteFormDTO();
        reporteForm.setPonderaciones(listaPonderaciones);

        model.addAttribute("reporteForm", reporteForm);
        model.addAttribute("grupo", grupo);
        return "reportes/formulario-reporte";
    }

    @PostMapping("/generar")
    public String generarReporte(@PathVariable("idGrupo") Integer idGrupo,
                                 @ModelAttribute("reporteForm") ReporteFormDTO reporteForm,
                                 HttpServletRequest request,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        Integer idMaestro = obtenerIdMaestro(request);
        if (idMaestro == null)
            return "redirect:/login?sessionExpired=true";

        Grupo grupo = grupoService.buscarPorId(idGrupo);
        if (grupo == null || !grupo.getMaestro().getIdMaestro().equals(idMaestro)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Acceso denegado");
            return "redirect:/index";
        }

        // Validar que la suma de ponderaciones sea 100%
        double suma = reporteForm.getPonderaciones().stream()
                .mapToDouble(TipoTrabajoPonderacionDTO::getPonderacion).sum();
        if (Math.abs(suma - 100.0) > 0.01) {
            redirectAttributes.addFlashAttribute("errorMessage", "La suma de ponderaciones debe ser 100%");
            return "redirect:/grupos/" + idGrupo + "/reportes/nuevo";
        }

        try {
            // Intenta generar y guardar el período (la lógica de negocio)
            PeriodoAcademico periodo = reporteGrupalService.generarReporteGrupo(idGrupo, reporteForm);
            // Guarda el formulario en sesión (o flash, según lo que uses) si es necesario
            request.getSession().setAttribute("reporteForm", reporteForm);

            // Una vez creados los historiales, redirigir a la generación del PDF
            return "redirect:/grupos/" + idGrupo + "/reportes/pdf?periodo=" + periodo.getIdPeriodoAcademico();
        } catch (DataIntegrityViolationException ex) {
            // Se atrapa la excepción (nombre duplicado) y se retroalimenta al usuario sin mostrar una página de error
            redirectAttributes.addFlashAttribute("errorMessage", "El nombre del período ya existe. Por favor, ingrese otro.");
            return "redirect:/grupos/" + idGrupo + "/reportes/nuevo";
        }
    }



    @GetMapping("/pdf")
    public ModelAndView verReportePdf(@PathVariable("idGrupo") Integer idGrupo,
                                      @RequestParam("periodo") Integer idPeriodo,
                                      HttpServletRequest request) {
        Integer idMaestro = obtenerIdMaestro(request);
        if (idMaestro == null) {
            return new ModelAndView("redirect:/login?sessionExpired=true");
        }
        Grupo grupo = grupoService.buscarPorId(idGrupo);
        if (grupo == null || !grupo.getMaestro().getIdMaestro().equals(idMaestro)) {
            return new ModelAndView("redirect:/index?accessDenied=true");
        }

        PeriodoAcademico periodo = periodoAcademicoService.obtenerPorId(idPeriodo);
        if (periodo == null) {
            return new ModelAndView("redirect:/grupos/" + idGrupo + "/reportes/nuevo");
        }

       // Se recupera el historial de calificaciones a partir del periodo
        List<HistorialCalificaciones> historiales = historialCalificacionesService.findByPeriodo(periodo);


        // Recuperar las ponderaciones desde la sesión
        ReporteFormDTO reporteForm = (ReporteFormDTO) request.getSession().getAttribute("reporteForm");
        List<TipoTrabajoPonderacionDTO> ponderaciones = (reporteForm != null)
                ? reporteForm.getPonderaciones() : new ArrayList<>();

        // Prepara el modelo para el PDF
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("grupo", grupo);
        modelMap.put("periodo", periodo);
        modelMap.put("historiales", historiales);
        modelMap.put("ponderaciones", ponderaciones);

        // Devuelve directamente el ModelAndView con la instancia del PDF view
        return new ModelAndView(reporteGrupalPdfView, modelMap);
    }

}
