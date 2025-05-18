package dgtic.core.controller;

import dgtic.core.model.Grupo;
import dgtic.core.model.Maestro;
import dgtic.core.model.TipoTrabajo;
import dgtic.core.model.Trabajo;
import dgtic.core.security.CookieUtil;
import dgtic.core.security.jwt.JwtUtil;
import dgtic.core.service.MaestroService;
import dgtic.core.service.TrabajoService;
import dgtic.core.service.TipoTrabajoService;
import dgtic.core.service.GrupoService;
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
@RequestMapping("/trabajos")
public class TrabajoController {

    @Autowired
    private MaestroService maestroService;

    @Autowired
    private TrabajoService trabajoService;

    @Autowired
    private GrupoService grupoService;

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

    @GetMapping
    public String renderTrabajos(HttpServletRequest request, Model model, @ModelAttribute("successMessage") String successMessage) {

        Integer idMaestro = obtenerIdMaestro(request);
        if (idMaestro == null) {
            return "redirect:/login?sessionExpired=true";
        }

        model.addAttribute("trabajo", new Trabajo()); // Objeto para el formulario
        model.addAttribute("listaTrabajos", trabajoService.obtenerTrabajosDeMaestro(idMaestro));
        model.addAttribute("listaGrupos", maestroService.obtenerGruposDeMaestro(idMaestro));
        model.addAttribute("listaTiposTrabajo", tipoTrabajoService.obtenerTodosLosTiposTrabajo());

        if (!successMessage.isEmpty()) { // Si hay un mensaje de éxito, pásalo a la vista
            model.addAttribute("successMessage", successMessage);
        }

        return "trabajos/trabajosAsignados"; // Vista con la lista de trabajos
    }


    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoTrabajo(HttpServletRequest request, Model model) {
        Integer idMaestro = obtenerIdMaestro(request);
        if (idMaestro == null) {
            return "redirect:/login?sessionExpired=true";
        }

        model.addAttribute("trabajo", new Trabajo()); // Inicializa un trabajo vacío
        model.addAttribute("listaGrupos", maestroService.obtenerGruposDeMaestro(idMaestro));
        model.addAttribute("listaTiposTrabajo", tipoTrabajoService.obtenerTodosLosTiposTrabajo());
        return "trabajos/formulario-trabajo"; // Usa la misma página para agregar
    }

    @PostMapping("/guardar")
    public String guardarTrabajo(HttpServletRequest request,
                                 @Valid @ModelAttribute("trabajo") Trabajo trabajo,
                                 BindingResult result,
                                 @RequestParam(value = "newTipoTrabajo", required = false) String newTipoTrabajo,
                                 RedirectAttributes redirectAttributes, Model model) {
        Integer idMaestro = obtenerIdMaestro(request);
        if (idMaestro == null) {
            return "redirect:/login?sessionExpired=true";
        }

        if (result.hasErrors()) {
            model.addAttribute("listaGrupos", maestroService.obtenerGruposDeMaestro(idMaestro));
            model.addAttribute("listaTiposTrabajo", tipoTrabajoService.obtenerTodosLosTiposTrabajo());
            model.addAttribute("contenido", "Nuevo Trabajo");
            model.addAttribute("errorMessage", "El trabajo no ha sido asignado debido a errores en el formulario.");
            return "trabajos/formulario-trabajo";
        }

        // Verificar si el trabajo es nuevo o está siendo modificado
        boolean esNuevoTrabajo = (trabajo.getIdTrabajo() == null);

        // Manejo del nuevo tipo de trabajo
        if (newTipoTrabajo != null && !newTipoTrabajo.trim().isEmpty()) {
            newTipoTrabajo = newTipoTrabajo.trim().replaceAll("\\s+", " ");
            TipoTrabajo tipoExistente = tipoTrabajoService.buscarPorNombre(newTipoTrabajo);
            if (tipoExistente == null) {
                TipoTrabajo nuevoTipo = new TipoTrabajo();
                nuevoTipo.setNombreTipoTrabajo(newTipoTrabajo);
                tipoTrabajoService.guardarTipoTrabajo(nuevoTipo);
                trabajo.setTipoTrabajo(nuevoTipo);
            } else {
                trabajo.setTipoTrabajo(tipoExistente);
            }
        }

        // Guardar trabajo en la base de datos
        trabajoService.guardarTrabajo(trabajo);

        // Enviar el mensaje solo si se ha creado un nuevo trabajo
        if (esNuevoTrabajo) {
            redirectAttributes.addFlashAttribute("successMessage", "El trabajo fue asignado correctamente.");
        }

        return "redirect:/trabajos";
    }



    @GetMapping("/modificar/{id}")
    public String mostrarFormularioModificacion(@PathVariable Integer id, HttpServletRequest request,Model model) {

        // Extraer el id del maestro autenticado a partir del token en la cookie
        Integer idMaestroAutenticado = obtenerIdMaestro(request);
        if (idMaestroAutenticado == null) {
            return "redirect:/login?sessionExpired=true";
        }

        Trabajo trabajo = trabajoService.buscarPorId(id);
        if (trabajo == null) {
            return "redirect:/trabajos"; // Redirigir si el trabajo no existe
        }

        // Comprobar si el trabajo pertenece al maestro autenticado
        if (!trabajoService.obtenerOwnerTrabajo(trabajo.getIdTrabajo()).getIdMaestro().equals(idMaestroAutenticado)) {
            // Se podría redirigir a una página de error o simplemente a la lista de grupos
            return "redirect:/index?accessDenied=true";
        }

        if (trabajo.getGrupo() == null) {
            trabajo.setGrupo(new Grupo());
        }
        if (trabajo.getTipoTrabajo() == null) {
            trabajo.setTipoTrabajo(new TipoTrabajo());
        }

        Integer idMaestro = obtenerIdMaestro(request);
        if (idMaestro == null) {
            return "redirect:/login?sessionExpired=true";
        }

        model.addAttribute("trabajo", trabajo);
        model.addAttribute("listaGrupos", maestroService.obtenerGruposDeMaestro(idMaestro));
        model.addAttribute("listaTiposTrabajo", tipoTrabajoService.obtenerTodosLosTiposTrabajo());

        return "trabajos/modificar-trabajo";
    }



    @GetMapping("/eliminar/{id}")
    public String eliminarTrabajo(@PathVariable Integer id, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        // Extraer el id del maestro autenticado a partir del token en la cookie
        Integer idMaestroAutenticado = obtenerIdMaestro(request);
        if (idMaestroAutenticado == null) {
            return "redirect:/login?sessionExpired=true";
        }

        Trabajo trabajo = trabajoService.buscarPorId(id);
        if (trabajo == null) {
            return "redirect:/trabajos"; // Redirigir si el trabajo no existe
        }

        // Comprobar si el trabajo pertenece al maestro autenticado
        if (!trabajoService.obtenerOwnerTrabajo(trabajo.getIdTrabajo()).getIdMaestro().equals(idMaestroAutenticado)) {
            // Se podría redirigir a una página de error o simplemente a la lista de grupos
            return "redirect:/index?accessDenied=true";
        }

        trabajoService.eliminarTrabajo(id);
        redirectAttributes.addFlashAttribute("successMessage", "Trabajo eliminado correctamente.");
        return "redirect:/trabajos";
    }

    @GetMapping("/filtrar")
    public String filtrarTrabajosPorGrupo(@RequestParam(required = false) Integer idGrupo, HttpServletRequest request, Model model) {
        Integer idMaestro = obtenerIdMaestro(request);
        if (idMaestro == null) {
            return "redirect:/login?sessionExpired=true";
        }

        List<Trabajo> trabajosFiltrados;
        if (idGrupo == null || idGrupo == -1) { // Detectar opción "Todos los grupos"
            trabajosFiltrados = trabajoService.obtenerTrabajosDeMaestro(idMaestro);
        } else {
            trabajosFiltrados = trabajoService.obtenerTrabajosPorGrupo(idGrupo);
            Grupo grupo = grupoService.buscarPorId(idGrupo);
            // Comprobar si el grupo pertenece al maestro autenticado
            if (!grupo.getMaestro().getIdMaestro().equals(idMaestro)) {
                // Se podría redirigir a una página de error o simplemente a la lista de grupos
                return "redirect:/index?accessDenied=true";
            }
        }



        model.addAttribute("listaTrabajos", trabajosFiltrados);
        model.addAttribute("listaGrupos", maestroService.obtenerGruposDeMaestro(idMaestro));
        return "trabajos/trabajosAsignados";
    }








}




