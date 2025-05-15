package dgtic.core.controller;

import dgtic.core.model.Asignatura;
import dgtic.core.model.Grupo;
import dgtic.core.model.Trabajo;
import dgtic.core.repository.AsignaturaRepository;
import dgtic.core.repository.GrupoRepository;
import dgtic.core.service.AsignaturaService;
import dgtic.core.service.GrupoService;
import dgtic.core.service.TrabajoService;
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
    private GrupoService grupoService;

    @Autowired
    private TrabajoService trabajoService;

    @Autowired
    private AsignaturaService asignaturaService;

    @GetMapping("/grupos/info/{id}")
    public String mostrarInfoGrupo(@PathVariable Integer id, Model model) {
        Grupo grupo = grupoService.buscarPorId(id);

        if (grupo == null) {
            return "redirect:/index"; // Redirigir si el grupo no existe
        }

        List<Trabajo> trabajosGrupo = trabajoService.obtenerTrabajosPorGrupo(id);

        model.addAttribute("grupo", grupo);
        model.addAttribute("trabajosGrupo", trabajosGrupo);

        return "grupos/infoGrupo"; // Vista con la información del grupo y sus trabajos
    }


    @GetMapping("/index")
    public String mostrarGrupos(Model model) {
        List<Grupo> selectGrupos = grupoService.obtenerTodosLosGrupos();
        if (selectGrupos == null) selectGrupos = List.of(); // Lista vacía si no hay grupos

        model.addAttribute("selectGrupo", selectGrupos);
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
    public String guardarGrupo(@Valid @ModelAttribute("grupo") Grupo grupo,
                               BindingResult result,
                               @RequestParam(value = "newAsignatura", required = false) String newAsignatura,
                               RedirectAttributes redirectAttributes, Model model) {

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

        grupoService.guardarGrupo(grupo);

        if (esNuevoGrupo) {
            redirectAttributes.addFlashAttribute("successMessage", "El grupo fue creado correctamente.");
        }

        return "redirect:/index";
    }


    @GetMapping("/grupos/modificar/{id}")
    public String modificarGrupo(@PathVariable Integer id, Model model) {
        Grupo grupo = grupoService.buscarPorId(id);

        if (grupo == null) return "redirect:/index";

        model.addAttribute("grupo", grupo);
        model.addAttribute("selectAsignatura", asignaturaService.obtenerTodasLasAsignaturas());
        return "grupos/modificar-grupo"; // Página de modificación
    }

    @GetMapping("/grupos/eliminar/{id}")
    public String eliminarGrupo(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        grupoService.eliminarGrupo(id);
        redirectAttributes.addFlashAttribute("successMessage", "Grupo eliminado correctamente.");
        return "redirect:/index";
    }



}
