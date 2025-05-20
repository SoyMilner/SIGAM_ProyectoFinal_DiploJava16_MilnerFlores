package dgtic.core.controller;

import dgtic.core.model.*;
import dgtic.core.model.dto.TrabajoAlumnoDTO;
import dgtic.core.security.CookieUtil;
import dgtic.core.security.jwt.JwtUtil;
import dgtic.core.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/grupos/{idGrupo}/estudiantes")
public class EstudianteController {

    @Autowired
    private MaestroService maestroService;

    @Autowired
    private TrabajoService trabajoService;

    @Autowired
    private GrupoService grupoService;

    @Autowired
    private EstudianteService estudianteService;

    @Autowired
    private TipoTrabajoService tipoTrabajoService;

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


    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoEstudiante(@PathVariable("idGrupo") Integer idGrupo,
                                                   HttpServletRequest request,
                                                   Model model) {

        // Extraer el id del maestro autenticado a partir del token en la cookie
        Integer idMaestroAutenticado = obtenerIdMaestro(request);
        if (idMaestroAutenticado == null) {
            return "redirect:/login?sessionExpired=true";
        }

        Grupo grupo = grupoService.buscarPorId(idGrupo);
        if (grupo == null) {
            return "redirect:/index"; // O algún error
        }

        // Comprobar si el grupo pertenece al maestro autenticado
        if (!grupo.getMaestro().getIdMaestro().equals(idMaestroAutenticado)) {
            // Se podría redirigir a una página de error o simplemente a la lista de grupos
            return "redirect:/index?accessDenied=true";
        }

        // Se crea un nuevo estudiante y se le asocia el grupo
        Estudiante estudiante = new Estudiante();

        model.addAttribute("estudiante", estudiante);
        model.addAttribute("grupo", grupo);
        return "estudiantes/formulario-estudiante"; // La vista con el formulario para agregar estudiante
    }

    @PostMapping("/guardar")
    public String guardarEstudiante(@PathVariable("idGrupo") Integer idGrupo,
                                    HttpServletRequest request,
                                    @Valid @ModelAttribute("estudiante") Estudiante estudiante,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes,
                                    Model model) {
        Grupo grupo = grupoService.buscarPorId(idGrupo);
        if (grupo == null) {
            return "redirect:/index";
        }

        if (result.hasErrors()) {
            model.addAttribute("grupo", grupo);
            return "estudiantes/formulario-estudiante";
        }

        // Obtener el id del maestro autenticado y asignarlo al estudiante
        Integer idMaestro = obtenerIdMaestro(request);
        if(idMaestro == null) {
            return "redirect:/login?sessionExpired=true";
        }
        estudiante.setMaestro(maestroService.buscarPorId(idMaestro));

        // Agregar el grupo al estudiante si aún no está definido
        if (!estudiante.getGrupos().contains(grupo)) {
            estudiante.getGrupos().add(grupo);
        }
        estudianteService.guardarEstudiante(estudiante);

        redirectAttributes.addFlashAttribute("successMessage", "Estudiante agregado correctamente.");
        return "redirect:/grupos/info/" + idGrupo + "?vista=estudiantes";
    }

    @GetMapping("/modificar/{idEstudiante}")
    public String mostrarFormularioModificacion(@PathVariable("idGrupo") Integer idGrupo,
                                                @PathVariable("idEstudiante") Integer idEstudiante,
                                                HttpServletRequest request,
                                                Model model) {

        // Validar sesión del maestro autenticado
        Integer idMaestroAutenticado = obtenerIdMaestro(request);
        if (idMaestroAutenticado == null) {
            return "redirect:/login?sessionExpired=true";
        }

        // Buscar el grupo
        Grupo grupo = grupoService.buscarPorId(idGrupo);
        if (grupo == null) {
            return "redirect:/index"; // Si el grupo no existe, redirigir
        }

        // Comprobar si el grupo pertenece al maestro autenticado
        if (!grupo.getMaestro().getIdMaestro().equals(idMaestroAutenticado)) {
            return "redirect:/index?accessDenied=true";
        }

        // Buscar el estudiante
        Estudiante estudiante = estudianteService.buscarPorId(idEstudiante);
        if (estudiante == null) {
            return "redirect:/grupos/info/" + idGrupo + "?vista=estudiantes";
        }

        model.addAttribute("estudiante", estudiante);
        model.addAttribute("grupo", grupo);
        return "estudiantes/formulario-estudiante"; // Se utiliza el mismo formulario pero con datos pre-cargados
    }

    @PostMapping("/modificar/{idEstudiante}")
    public String actualizarEstudiante(@PathVariable("idGrupo") Integer idGrupo,
                                       @PathVariable("idEstudiante") Integer idEstudiante,
                                       HttpServletRequest request,
                                       @Valid @ModelAttribute("estudiante") Estudiante estudiante,
                                       BindingResult result,
                                       RedirectAttributes redirectAttributes,
                                       Model model) {

        Grupo grupo = grupoService.buscarPorId(idGrupo);
        if (grupo == null) {
            return "redirect:/index";
        }

        if (result.hasErrors()) {
            model.addAttribute("grupo", grupo);
            return "estudiantes/formulario-estudiante";
        }

        // Validar maestro autenticado
        Integer idMaestro = obtenerIdMaestro(request);
        if(idMaestro == null) {
            return "redirect:/login?sessionExpired=true";
        }
        estudiante.setMaestro(maestroService.buscarPorId(idMaestro));

        // Asegurar que el estudiante sigue perteneciendo al grupo
        if (!estudiante.getGrupos().contains(grupo)) {
            estudiante.getGrupos().add(grupo);
        }

        estudianteService.guardarEstudiante(estudiante);

        redirectAttributes.addFlashAttribute("successMessage", "Estudiante actualizado correctamente.");
        return "redirect:/grupos/info/" + idGrupo + "?vista=estudiantes";
    }



    @GetMapping("/eliminar/{idEstudiante}")
    public String eliminarEstudiante(@PathVariable("idGrupo") Integer idGrupo,
                                     @PathVariable("idEstudiante") Integer idEstudiante,
                                     HttpServletRequest request,
                                     RedirectAttributes redirectAttributes) {

        // Validar sesión del maestro autenticado
        Integer idMaestroAutenticado = obtenerIdMaestro(request);
        if (idMaestroAutenticado == null) {
            return "redirect:/login?sessionExpired=true";
        }

        // Buscar el grupo
        Grupo grupo = grupoService.buscarPorId(idGrupo);
        if (grupo == null) {
            return "redirect:/index"; // Si el grupo no existe, redirigir
        }

        // Comprobar si el grupo pertenece al maestro autenticado
        if (!grupo.getMaestro().getIdMaestro().equals(idMaestroAutenticado)) {
            return "redirect:/index?accessDenied=true";
        }

        // Buscar al estudiante
        Estudiante estudiante = estudianteService.buscarPorId(idEstudiante);
        if (estudiante == null) {
            return "redirect:/grupos/info/" + idGrupo + "?vista=estudiantes";
        }

        // Remover el estudiante del grupo antes de eliminarlo
        estudiante.getGrupos().remove(grupo);
        estudianteService.guardarEstudiante(estudiante);

        // Validar si el estudiante no pertenece a otros grupos y eliminarlo si es huérfano
        if (estudiante.getGrupos().isEmpty()) {
            estudianteService.eliminarEstudiante(idEstudiante);
        }

        redirectAttributes.addFlashAttribute("successMessage", "Estudiante eliminado correctamente.");
        return "redirect:/grupos/info/" + idGrupo + "?vista=estudiantes";
    }

    @GetMapping("/{idEstudiante}/trabajos")
    public String mostrarTrabajosAsignadosEstudiante(@PathVariable("idGrupo") Integer idGrupo,
                                                 @PathVariable("idEstudiante") Integer idEstudiante,
                                                 HttpServletRequest request,
                                                 Model model) {
        // Validar sesión del maestro
        Integer idMaestro = obtenerIdMaestro(request);
        if (idMaestro == null) {
            return "redirect:/login?sessionExpired=true";
        }

        // Buscar el grupo
        Grupo grupo = grupoService.buscarPorId(idGrupo);
        if (grupo == null) {
            return "redirect:/index";
        }
        // Asegurarse de que el grupo pertenezca al maestro autenticado
        if (!grupo.getMaestro().getIdMaestro().equals(idMaestro)) {
            return "redirect:/index?accessDenied=true";
        }

        // Buscar el estudiante y verificar que pertenece al grupo
        Estudiante estudiante = estudianteService.buscarPorId(idEstudiante);
        if (estudiante == null || !estudiante.getGrupos().contains(grupo)) {
            return "redirect:/grupos/info/" + idGrupo + "?vista=estudiantes";
        }

        // Obtener todos los trabajos asignados a ese grupo
        List<Trabajo> trabajos = grupo.getTrabajos();
        List<TrabajoAlumnoDTO> trabajosEstudiante = new ArrayList<>();

        for (Trabajo trabajo : trabajos) {
            // Consultar si existe una relación en la tabla trabajo_estudiante para este estudiante y trabajo.
            Optional<TrabajoEstudiante> teOpt = trabajoEstudianteService.findByTrabajoAndEstudiante(trabajo, estudiante);
            Double calificacion;
            LocalDate fechaEntrega;
            String comentarios;
            if (teOpt.isPresent()) {
                calificacion = teOpt.get().getCalificacion();
                fechaEntrega = teOpt.get().getFechaEntrega();
                comentarios = teOpt.get().getComentarios();
            } else {
                // Si no existe, la lógica es asignar 0 como calificación, y valores nulos o vacíos para los demás campos.
                calificacion = 0.0;
                fechaEntrega = null;
                comentarios = "";
            }
            trabajosEstudiante.add(new TrabajoAlumnoDTO(trabajo, calificacion, fechaEntrega, comentarios));
        }

        model.addAttribute("trabajosEstudiante", trabajosEstudiante);
        model.addAttribute("estudiante", estudiante);
        model.addAttribute("grupo", grupo);

        return "estudiantes/trabajos-estudiante";
    }



}
