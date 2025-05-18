package dgtic.core.controller;

import dgtic.core.model.Asignatura;
import dgtic.core.model.Grupo;
import dgtic.core.model.Maestro;
import dgtic.core.model.Trabajo;
import dgtic.core.repository.AsignaturaRepository;
import dgtic.core.repository.GrupoRepository;
import dgtic.core.security.CookieUtil;
import dgtic.core.security.jwt.JwtUtil;
import dgtic.core.service.AsignaturaService;
import dgtic.core.service.GrupoService;
import dgtic.core.service.MaestroService;
import dgtic.core.service.TrabajoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class GrupoController {

    @Autowired
    private MaestroService maestroService;

    @Autowired
    private GrupoService grupoService;

    @Autowired
    private TrabajoService trabajoService;

    @Autowired
    private AsignaturaService asignaturaService;

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

    @GetMapping("/grupos/info/{id}")
    public String mostrarInfoGrupo(@PathVariable Integer id, HttpServletRequest request, Model model) {
        // Extraer el id del maestro autenticado a partir del token en la cookie
        Integer idMaestroAutenticado = obtenerIdMaestro(request);
        if (idMaestroAutenticado == null) {
            return "redirect:/login?sessionExpired=true";
        }

        // Buscar el grupo por su id
        Grupo grupo = grupoService.buscarPorId(id);
        if (grupo == null) {
            return "redirect:/index"; // Redirigir si el grupo no existe
        }

        // Comprobar si el grupo pertenece al maestro autenticado
        if (!grupo.getMaestro().getIdMaestro().equals(idMaestroAutenticado)) {
            // Se podría redirigir a una página de error o simplemente a la lista de grupos
            return "redirect:/index?accessDenied=true";
        }

        // Si es correcto, continuar y mostrar la información del grupo
        List<Trabajo> trabajosGrupo = trabajoService.obtenerTrabajosPorGrupo(id);
        model.addAttribute("grupo", grupo);
        model.addAttribute("trabajosGrupo", trabajosGrupo);

        return "grupos/infoGrupo"; // Vista con la información del grupo y sus trabajos
    }


    @GetMapping("/index")
    public String mostrarGrupos(Model model) {
        //Consulta de grupos para barra de navegación
        //Se utiliza GlobalAttributesController para mostrar los grupos
        return "principal/index"; // Página con la lista de grupos
    }

    @GetMapping("/grupos/nuevo")
    public String mostrarFormularioNuevoGrupo(Model model) {
        Grupo grupo = new Grupo();
        grupo.setAsignatura(new Asignatura());

        model.addAttribute("grupo", grupo);
        model.addAttribute("selectAsignatura", asignaturaService.obtenerTodasLasAsignaturas());
        return "grupos/nuevo-grupo"; // Vista de creación
    }

    @PostMapping("/grupos/guardar")
    public String guardarGrupo(HttpServletRequest request, @Valid @ModelAttribute("grupo") Grupo grupo,
                               BindingResult result,
                               @RequestParam(value = "newAsignatura", required = false) String newAsignatura,
                               RedirectAttributes redirectAttributes, Model model) {
        Integer idMaestro = obtenerIdMaestro(request);
        if (idMaestro == null) {
            return "redirect:/login?sessionExpired=true";
        }

        if (result.hasErrors()) {
            model.addAttribute("selectAsignatura", asignaturaService.obtenerTodasLasAsignaturas());
            model.addAttribute("errorMessage", "El grupo no ha sido creado debido a errores en el formulario.");
            return "grupos/nuevoGrupo"; // Mantiene la página si hay errores
        }

        boolean esNuevoGrupo = (grupo.getIdGrupo() == null);

        if (newAsignatura != null && !newAsignatura.trim().isEmpty()) {
            newAsignatura = newAsignatura.trim();
            Asignatura asignaturaExistente = asignaturaService.buscarPorNombre(newAsignatura);

            if (asignaturaExistente == null) {
                Asignatura nuevaAsignatura = new Asignatura();
                nuevaAsignatura.setNombreAsignatura(newAsignatura);
                asignaturaService.guardarAsignatura(nuevaAsignatura);
                grupo.setAsignatura(nuevaAsignatura);
            } else {
                grupo.setAsignatura(asignaturaExistente);
            }
        }
        maestroService.crearGrupo(idMaestro, grupo);


        if (esNuevoGrupo) {
            redirectAttributes.addFlashAttribute("successMessage", "El grupo fue creado correctamente.");
        }

        return "redirect:/index";
    }


    @GetMapping("/grupos/modificar/{id}")
    public String modificarGrupo(@PathVariable Integer id, HttpServletRequest request, Model model) {

        // Extraer el id del maestro autenticado a partir del token en la cookie
        Integer idMaestroAutenticado = obtenerIdMaestro(request);
        if (idMaestroAutenticado == null) {
            return "redirect:/login?sessionExpired=true";
        }

        Grupo grupo = grupoService.buscarPorId(id);
        if (grupo == null) return "redirect:/index";

        // Comprobar si el grupo pertenece al maestro autenticado
        if (!grupo.getMaestro().getIdMaestro().equals(idMaestroAutenticado)) {
            // Se podría redirigir a una página de error o simplemente a la lista de grupos
            return "redirect:/index?accessDenied=true";
        }

        model.addAttribute("grupo", grupo);
        model.addAttribute("selectAsignatura", asignaturaService.obtenerTodasLasAsignaturas());
        return "grupos/modificar-grupo"; // Página de modificación
    }

    @GetMapping("/grupos/eliminar/{id}")
    public String eliminarGrupo(@PathVariable Integer id, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // Extraer el id del maestro autenticado a partir del token en la cookie
        Integer idMaestroAutenticado = obtenerIdMaestro(request);
        if (idMaestroAutenticado == null) {
            return "redirect:/login?sessionExpired=true";
        }

        Grupo grupo = grupoService.buscarPorId(id);
        if (grupo == null) return "redirect:/index";

        // Comprobar si el grupo pertenece al maestro autenticado
        if (!grupo.getMaestro().getIdMaestro().equals(idMaestroAutenticado)) {
            // Se podría redirigir a una página de error o simplemente a la lista de grupos
            return "redirect:/index?accessDenied=true";
        }

        grupoService.eliminarGrupo(id);
        redirectAttributes.addFlashAttribute("successMessage", "Grupo eliminado correctamente.");
        return "redirect:/index";
    }



}
