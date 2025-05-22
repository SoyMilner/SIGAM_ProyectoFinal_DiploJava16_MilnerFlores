package dgtic.core.controller;

import dgtic.core.model.*;
import dgtic.core.repository.AsignaturaRepository;
import dgtic.core.repository.GrupoRepository;
import dgtic.core.security.CookieUtil;
import dgtic.core.security.jwt.JwtUtil;
import dgtic.core.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
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
    private EstudianteService estudianteService;

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
            // Si no hay token, devuelve null para que
            // luego el controlador redirija a login.
            return null;
        }
        String correoMaestro = jwtUtil.extractUsername(token);
        Maestro maestro = maestroService.buscarPorCorreo(correoMaestro);
        return (maestro != null) ? maestro.getIdMaestro() : null;
    }

    @GetMapping("/grupos/info/{id}")
    public String mostrarInfoGrupo(@PathVariable Integer id, HttpServletRequest request, Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "size", defaultValue = "10") int size) {

        log.info("Solicitud para mostrar información del grupo ID {}", id);
        // Extraer el id del maestro autenticado a partir del token en la cookie
        Integer idMaestroAutenticado = obtenerIdMaestro(request);
        if (idMaestroAutenticado == null) {
            log.warn("Sesión expirada. Redirigiendo al login.");
            return "redirect:/login?sessionExpired=true";
        }


        // Buscar el grupo por su id
        Grupo grupo = grupoService.buscarPorId(id);
        if (grupo == null) {
            log.error("Grupo con ID {} no encontrado. Redirigiendo al índice.", id);
            return "redirect:/index"; // Redirigir si el grupo no existe
        }

        // Comprobar si el grupo pertenece al maestro autenticado
        if (!grupo.getMaestro().getIdMaestro().equals(idMaestroAutenticado)) {
            log.warn("Acceso denegado: El maestro ID {} no tiene acceso al grupo ID {}. Redirigiendo.", idMaestroAutenticado, id);
            return "redirect:/index?accessDenied=true";
        }

        // Extraer el parámetro "vista" (si viene en la URL) para determinar qué se va a mostrar (Trabajos | Alumnos).
        String vista = request.getParameter("vista");
        if (vista == null || vista.trim().isEmpty()) {
            vista = "estudiantes";   // valor por defecto, por ejemplo, mostrar estudiantes
        }

        log.info("Vista seleccionada: '{}'. Cargando datos correspondientes.", vista);

        // Obtener la lista de estudiantes inscritos, a través de la relación en la entidad.
        // Suponiendo que si se está mostrando estudiantes, se utiliza la paginación de estudiantes. Si está mostrando trabajos, se muestra paginación de trabajos.
        if ("estudiantes".equals(vista)) {
            Pageable pageable = PageRequest.of(page, size);
            // Usar el metodo paginado
            Page<Estudiante> estudiantesPage = estudianteService.obtenerEstudiantesPorGrupoYMaestroPag(id, grupo.getMaestro().getIdMaestro(), pageable);
            log.info("Se encontraron {} estudiantes en el grupo ID {}", estudiantesPage.getTotalElements(), id);
            model.addAttribute("estudiantesPage", estudiantesPage);
        }

        //Obtener la lista de trabajos que corresponden al grupo
        if ("trabajos".equals(vista)) {
            // Para trabajos, usar también paginación:
            Pageable pageable = PageRequest.of(page, size);
            Page<Trabajo> trabajosPage = trabajoService.obtenerTrabajosPorGrupoPag(id, pageable);
            log.info("Se encontraron {} trabajos en el grupo ID {}", trabajosPage.getTotalElements(), id);
            model.addAttribute("trabajosPage", trabajosPage);
        }

        model.addAttribute("grupo", grupo);
        model.addAttribute("vista", vista);  // Indicamos qué sección se debe mostrar

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
