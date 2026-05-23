package com.J.M_CODERS.KinalCodeQuest.controller;

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
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        Jugador jugador = jugadorService.autenticar(username, password);
        if (jugador != null) {
            session.setAttribute("usuarioLogueado", jugador);
            return "redirect:/game/dashboard";
        }
        model.addAttribute("error", "Credenciales incorrectas en el núcleo del sistema.");
        return "login";
    }

    @GetMapping("/registro")
    public String registroPage(Model model) {
        model.addAttribute("jugador", new Jugador());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(@ModelAttribute Jugador jugador, HttpSession session) {
        Jugador nuevo = jugadorService.registrarJugador(jugador);
        session.setAttribute("usuarioLogueado", nuevo);
        return "redirect:/game/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}