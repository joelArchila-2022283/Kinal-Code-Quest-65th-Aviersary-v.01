package com.J.M_CODERS.KinalCodeQuest.controller;

import com.J.M_CODERS.KinalCodeQuest.model.dto.LoginDTO;
import com.J.M_CODERS.KinalCodeQuest.model.dto.RegistroDTO;
import com.J.M_CODERS.KinalCodeQuest.model.entity.Jugador;
import com.J.M_CODERS.KinalCodeQuest.service.evaluator.JugadorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JugadorService jugadorService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "player/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("loginDTO") LoginDTO loginDTO, HttpSession session, Model model) {
        Jugador jugador = jugadorService.autenticar(loginDTO.getUsername(), loginDTO.getPassword());
        if (jugador != null) {
            session.setAttribute("usuarioLogueado", jugador);
            return "redirect:/game/dashboard";
        }
        model.addAttribute("error", "Credenciales incorrectas en el núcleo del sistema.");
        return "player/login";
    }

    @GetMapping("/registro")
    public String registroPage(Model model) {
        model.addAttribute("registroDTO", new RegistroDTO());
        return "player/registro";
    }

    @PostMapping("/registro")
    public String registrar(@ModelAttribute("registroDTO") RegistroDTO registroDTO, HttpSession session, Model model) {
        if (!registroDTO.getPassword().equals(registroDTO.getConfirmarPassword())) {
            model.addAttribute("error", "Las contraseñas no coinciden en la matriz de seguridad.");
            return "player/registro";
        }

        // Mapea el DTO a la Entidad real para guardarla en la DB
        Jugador jugador = new Jugador();
        jugador.setUsername(registroDTO.getUsername());
        jugador.setPassword(registroDTO.getPassword());
        jugador.setNombreAvatar(registroDTO.getNombreAvatar());

        try {
            // Intenta registrar al jugador aplicando la validación de duplicados
            Jugador nuevo = jugadorService.registrarJugador(jugador);
            session.setAttribute("usuarioLogueado", nuevo);
            return "redirect:/game/dashboard";

        } catch (IllegalArgumentException e) {
            // Captura el duplicado controlado en el servicio y envía el mensaje a la vista
            if ("DUPLICATE_USERNAME".equals(e.getMessage())) {
                model.addAttribute("error", "El identificador '" + registroDTO.getUsername() + "' ya está asignado a otro operador en el Mainframe.");
                return "player/registro";
            }

            model.addAttribute("error", "Error inesperado en el núcleo de autenticación.");
            return "player/registro";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}