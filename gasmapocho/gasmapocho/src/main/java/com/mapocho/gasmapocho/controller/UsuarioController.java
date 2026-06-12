package com.mapocho.gasmapocho.controller;

import com.mapocho.gasmapocho.model.Usuario;
import com.mapocho.gasmapocho.repository.RolRepository;
import com.mapocho.gasmapocho.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioRepository usuarioRepository,
                             RolRepository rolRepository,
                             PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "usuarios/lista";
    }

    @GetMapping("/nuevo")
    public String formNuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", rolRepository.findAll());
        return "usuarios/form";
    }

    @PostMapping("/nuevo")
    public String guardarNuevo(@Valid @ModelAttribute Usuario usuario,
                               BindingResult result,
                               @RequestParam String passwordRaw,
                               Model model,
                               RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("roles", rolRepository.findAll());
            return "usuarios/form";
        }
        usuario.setPassword(passwordEncoder.encode(passwordRaw));
        usuarioRepository.save(usuario);
        flash.addFlashAttribute("exito", "Usuario creado correctamente.");
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String formEditar(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", rolRepository.findAll());
        return "usuarios/form";
    }

    @PostMapping("/editar/{id}")
    public String guardarEdicion(@PathVariable Long id,
                                 @Valid @ModelAttribute Usuario usuario,
                                 BindingResult result,
                                 @RequestParam String passwordRaw,
                                 Model model,
                                 RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("roles", rolRepository.findAll());
            return "usuarios/form";
        }
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        existente.setNombre(usuario.getNombre());
        existente.setEmail(usuario.getEmail());
        existente.setRol(usuario.getRol());
        existente.setActivo(usuario.isActivo());
        if (passwordRaw != null && !passwordRaw.isBlank()) {
            existente.setPassword(passwordEncoder.encode(passwordRaw));
        }
        usuarioRepository.save(existente);
        flash.addFlashAttribute("exito", "Usuario actualizado correctamente.");
        return "redirect:/usuarios";
    }

    @PostMapping("/desactivar/{id}")
    public String desactivar(@PathVariable Long id, RedirectAttributes flash) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
        flash.addFlashAttribute("exito", "Usuario desactivado.");
        return "redirect:/usuarios";
    }
}