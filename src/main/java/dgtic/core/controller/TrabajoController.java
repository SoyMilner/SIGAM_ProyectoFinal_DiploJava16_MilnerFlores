package dgtic.core.controller;

import dgtic.core.model.Grupo;
import dgtic.core.model.TipoTrabajo;
import dgtic.core.model.Trabajo;
import dgtic.core.service.TrabajoService;
import dgtic.core.service.TipoTrabajoService;
import dgtic.core.service.GrupoService;
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
    private TrabajoService trabajoService;

    @Autowired
    private GrupoService grupoService;

    @Autowired
    private TipoTrabajoService tipoTrabajoService;

    @GetMapping
    public String renderTrabajos(Model model, @ModelAttribute("successMessage") String successMessage) {
        model.addAttribute("trabajo", new Trabajo()); // Objeto para el formulario
        model.addAttribute("listaTrabajos", trabajoService.obtenerTodosLosTrabajos());
        model.addAttribute("listaGrupos", grupoService.obtenerTodosLosGrupos());
        model.addAttribute("listaTiposTrabajo", tipoTrabajoService.obtenerTodosLosTiposTrabajo());

        if (!successMessage.isEmpty()) { // Si hay un mensaje de éxito, pásalo a la vista
            model.addAttribute("successMessage", successMessage);
        }

        return "trabajos/trabajosAsignados"; // Vista con la lista de trabajos
    }


    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoTrabajo(Model model) {
        model.addAttribute("trabajo", new Trabajo()); // Inicializa un trabajo vacío
        model.addAttribute("listaGrupos", grupoService.obtenerTodosLosGrupos());
        model.addAttribute("listaTiposTrabajo", tipoTrabajoService.obtenerTodosLosTiposTrabajo());
        return "trabajos/formulario-trabajo"; // Usa la misma página para agregar
    }

    @PostMapping("/guardar")
    public String guardarTrabajo(@Valid @ModelAttribute("trabajo") Trabajo trabajo,
                                 BindingResult result,
                                 @RequestParam(value = "newTipoTrabajo", required = false) String newTipoTrabajo,
                                 RedirectAttributes redirectAttributes, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("listaGrupos", grupoService.obtenerTodosLosGrupos());
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
    public String mostrarFormularioModificacion(@PathVariable Integer id, Model model) {
        Trabajo trabajo = trabajoService.buscarPorId(id);

        if (trabajo == null) {
            return "redirect:/trabajos"; // Redirigir si el trabajo no existe
        }

        if (trabajo.getGrupo() == null) {
            trabajo.setGrupo(new Grupo());
        }
        if (trabajo.getTipoTrabajo() == null) {
            trabajo.setTipoTrabajo(new TipoTrabajo());
        }

        model.addAttribute("trabajo", trabajo);
        model.addAttribute("listaGrupos", grupoService.obtenerTodosLosGrupos());
        model.addAttribute("listaTiposTrabajo", tipoTrabajoService.obtenerTodosLosTiposTrabajo());

        return "trabajos/modificar-trabajo";
    }



    @GetMapping("/eliminar/{id}")
    public String eliminarTrabajo(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        trabajoService.eliminarTrabajo(id);
        redirectAttributes.addFlashAttribute("successMessage", "Trabajo eliminado correctamente.");
        return "redirect:/trabajos";
    }

    @GetMapping("/filtrar")
    public String filtrarTrabajosPorGrupo(@RequestParam(required = false) Integer idGrupo, Model model) {
        List<Trabajo> trabajosFiltrados;

        if (idGrupo == null || idGrupo == -1) { // Detectar opción "Todos los grupos"
            trabajosFiltrados = trabajoService.obtenerTodosLosTrabajos();
        } else {
            trabajosFiltrados = trabajoService.obtenerTrabajosPorGrupo(idGrupo);
        }

        model.addAttribute("listaTrabajos", trabajosFiltrados);
        model.addAttribute("listaGrupos", grupoService.obtenerTodosLosGrupos());
        return "trabajos/trabajosAsignados";
    }








}




