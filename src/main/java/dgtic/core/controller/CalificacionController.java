package dgtic.core.controller;

import dgtic.core.model.*;
import dgtic.core.model.dto.CalificacionTrabajoDTO;
import dgtic.core.model.dto.wrapper.CalificacionesWrapper;
import dgtic.core.security.CookieUtil;
import dgtic.core.security.jwt.JwtUtil;
import dgtic.core.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class CalificacionController {

    @Autowired
    private MaestroService maestroService;

    @Autowired
    private GrupoService grupoService;

    @Autowired
    private TrabajoService trabajoService;

    @Autowired
    private AsignaturaService asignaturaService;

    @Autowired
    private EstudianteService estudianteService;

    @Autowired
    private TrabajoEstudianteService trabajoEstudianteService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CookieUtil cookieUtil;

    /**
     * Metodo auxiliar para obtener el idMaestro a partir del token en la cookie.
     * Si no se encuentra token, redirige al login.
     */
    private Integer obtenerIdMaestro(HttpServletRequest request) {
        String token = cookieUtil.extractTokenFromCookie(request);
        if (token == null || token.trim().isEmpty()) {
            // Si no hay token, se puede lanzar una excepción o devolver null para que
            // luego el controlador redirija a login. Aquí decidimos devolver null.
            return null;
        }
        String correoMaestro = jwtUtil.extractUsername(token);
        Maestro maestro = maestroService.buscarPorCorreo(correoMaestro);
        return (maestro != null) ? maestro.getIdMaestro() : null;
    }

    @GetMapping("/grupos/{idGrupo}/trabajos/{idTrabajo}/calificar")
    public String mostrarCalificacionesTrabajo(@PathVariable("idGrupo") Integer idGrupo,
                                               @PathVariable("idTrabajo") Integer idTrabajo,
                                               HttpServletRequest request,
                                               Model model) {
        // Validar sesión y permisos: obtener id del maestro autenticado
        Integer idMaestro = obtenerIdMaestro(request);
        if (idMaestro == null) {
            return "redirect:/login?sessionExpired=true";
        }

        // Buscar el grupo y validar que pertenece al maestro
        Grupo grupo = grupoService.buscarPorId(idGrupo);
        if (grupo == null || !grupo.getMaestro().getIdMaestro().equals(idMaestro)) {
            return "redirect:/index?accessDenied=true";
        }

        // Buscar el trabajo y validar que pertenece al grupo
        Trabajo trabajo = trabajoService.buscarPorId(idTrabajo);
        if (trabajo == null || !trabajo.getGrupo().getIdGrupo().equals(idGrupo)) {
            return "redirect:/grupos/info/" + idGrupo + "?vista=trabajos";
        }

        // Obtener todos los estudiantes del grupo
        List<Estudiante> estudiantes = grupo.getEstudiantes();
        List<CalificacionTrabajoDTO> dtos = new ArrayList<>();
        for (Estudiante estudiante : estudiantes) {
            // Consultar la relación trabajo_estudiante para este trabajo y estudiante
            Optional<TrabajoEstudiante> teOpt = trabajoEstudianteService.findByTrabajoAndEstudiante(trabajo, estudiante);
            Double calificacion = teOpt.map(TrabajoEstudiante::getCalificacion).orElse(0.0);
            LocalDate fechaEntrega = teOpt.map(TrabajoEstudiante::getFechaEntrega).orElse(null);
            String comentarios = teOpt.map(TrabajoEstudiante::getComentarios).orElse("");
            String nombreCompleto = estudiante.getNombre() + " " +
                    estudiante.getApellidoPaterno() + " " +
                    estudiante.getApellidoMaterno();
            dtos.add(new CalificacionTrabajoDTO(
                    estudiante.getIdEstudiante(),
                    nombreCompleto,
                    calificacion,
                    fechaEntrega,
                    comentarios
            ));
        }

        // Envolver la lista en un wrapper
        CalificacionesWrapper wrapper = new CalificacionesWrapper(dtos);

        model.addAttribute("trabajo", trabajo);
        model.addAttribute("grupo", grupo);
        model.addAttribute("wrapper", wrapper);

        return "calificaciones/calificar-trabajo";
    }


    @PostMapping("/grupos/{idGrupo}/trabajos/{idTrabajo}/calificar")
    public String actualizarCalificacionesTrabajo(@PathVariable("idGrupo") Integer idGrupo,
                                                  @PathVariable("idTrabajo") Integer idTrabajo,
                                                  @ModelAttribute("wrapper") CalificacionesWrapper wrapper,
                                                  HttpServletRequest request,
                                                  RedirectAttributes redirectAttributes) {
        // Validar sesión y permisos
        Integer idMaestro = obtenerIdMaestro(request);
        if(idMaestro == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Sesión expirada");
            return "redirect:/login?sessionExpired=true";
        }

        // Validar que el grupo, trabajo existan y pertenezcan al maestro
        Grupo grupo = grupoService.buscarPorId(idGrupo);
        if (grupo == null || !grupo.getMaestro().getIdMaestro().equals(idMaestro)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Acceso denegado");
            return "redirect:/index?accessDenied=true";
        }

        Trabajo trabajo = trabajoService.buscarPorId(idTrabajo);
        if (trabajo == null || !trabajo.getGrupo().getIdGrupo().equals(idGrupo)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Trabajo incorrecto");
            return "redirect:/grupos/info/" + idGrupo + "?vista=trabajos";
        }

        // Iterar sobre la lista de calificaciones del wrapper y actualizar o crear cada relación
        for (CalificacionTrabajoDTO dto : wrapper.getCalificaciones()) {
            // Buscar el estudiante
            Estudiante estudiante = estudianteService.buscarPorId(dto.getIdEstudiante());
            if (estudiante == null || !estudiante.getGrupos().contains(grupo)) {
                continue; // O manejar error según convenga
            }

            Optional<TrabajoEstudiante> teOpt = trabajoEstudianteService.findByTrabajoAndEstudiante(trabajo, estudiante);
            TrabajoEstudiante te;
            if (teOpt.isPresent()) {
                te = teOpt.get();
            } else {
                te = new TrabajoEstudiante();
                TrabajoEstudianteId teId = new TrabajoEstudianteId();
                teId.setIdTrabajo(trabajo.getIdTrabajo());
                teId.setIdEstudiante(estudiante.getIdEstudiante());
                te.setId(teId);  // Asigna la clave compuesta
                te.setTrabajo(trabajo);
                te.setEstudiante(estudiante);
            }
            te.setCalificacion(dto.getCalificacion());
            te.setFechaEntrega(dto.getFechaEntrega());
            te.setComentarios(dto.getComentarios());

            trabajoEstudianteService.guardar(te);
        }

        redirectAttributes.addFlashAttribute("successMessage", "Calificaciones actualizadas correctamente.");
        return "redirect:/grupos/" + idGrupo + "/trabajos/" + idTrabajo + "/calificar";
    }




}
