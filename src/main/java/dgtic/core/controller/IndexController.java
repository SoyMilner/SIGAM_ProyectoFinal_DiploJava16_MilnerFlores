package dgtic.core.controller;

import dgtic.core.service.AsignaturaService;
import dgtic.core.service.GrupoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;


@Controller
public class IndexController {

    @Autowired
    private GrupoService grupoService;

    @Autowired
    private AsignaturaService asignaturaService;


    @GetMapping("/")
    public String redirectToLogin() {
        // Redirige automáticamente a la página de login
        return "redirect:/login";
    }

    // Página principal de login
    @GetMapping("/login")
    public String login() {
        return "principal/login";
    }


    // Perfil del usuario
    @GetMapping("/profile")
    public String profile() {
        return "principal/profile";
    }
}
