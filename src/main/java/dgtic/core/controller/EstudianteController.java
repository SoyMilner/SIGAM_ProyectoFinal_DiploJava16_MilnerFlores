package dgtic.core.controller;

import dgtic.core.model.*;
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


}
