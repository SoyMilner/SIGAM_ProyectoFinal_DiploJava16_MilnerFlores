package dgtic.core.controller;

import dgtic.core.model.Grupo;
import dgtic.core.model.Maestro;
import dgtic.core.security.CookieUtil;
import dgtic.core.security.jwt.JwtUtil;
import dgtic.core.service.GrupoService;
import dgtic.core.service.MaestroService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalAttributesController {

    @Autowired
    private MaestroService maestroService;

    @Autowired
    private GrupoService grupoService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CookieUtil cookieUtil;



    @ModelAttribute
    public void mostrarGruposGlobalesEnNavBar(HttpServletRequest request, Model model) {
        // Obtener correo del maestro autenticado desde Spring Security
        // Esto se hace con el SecurityContextHolder porque es una "petición" constante que podría entrar en conflicto con la pantalla de logout
        String correoMaestro = SecurityContextHolder.getContext().getAuthentication().getName();

        // Buscar al maestro por su correo
        Maestro maestro = maestroService.buscarPorCorreo(correoMaestro);
        Integer idMaestro = (maestro != null) ? maestro.getIdMaestro() : null;

        //Se obtienen todos los grupos para mostrarlos en la barra de navegación
//        List<Grupo> selectGrupos = grupoService.obtenerTodosLosGrupos();
//        model.addAttribute("selectGrupo", (selectGrupos != null) ? selectGrupos : List.of());
        List<Grupo> selectGrupos = maestroService.obtenerGruposDeMaestro(idMaestro);
        model.addAttribute("selectGrupo", (selectGrupos != null) ? selectGrupos : List.of());
    }
}
