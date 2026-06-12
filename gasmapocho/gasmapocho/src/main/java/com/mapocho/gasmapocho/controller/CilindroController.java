package com.mapocho.gasmapocho.controller;

import com.mapocho.gasmapocho.model.Cilindro;
import com.mapocho.gasmapocho.repository.CilindroRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cilindros")
public class CilindroController {

    private final CilindroRepository cilindroRepository;

    public CilindroController(CilindroRepository cilindroRepository) {
        this.cilindroRepository = cilindroRepository;
    }

    @GetMapping
    public String inventario(Model model) {
        model.addAttribute("cilindros", cilindroRepository.findAll());
        model.addAttribute("totalLlenos",   orCero(cilindroRepository.totalLlenos()));
        model.addAttribute("totalVacios",   orCero(cilindroRepository.totalVacios()));
        model.addAttribute("totalVendidos", orCero(cilindroRepository.totalVendidos()));
        model.addAttribute("totalViejos",   orCero(cilindroRepository.totalViejos()));
        return "cilindros/inventario";
    }

    @PostMapping("/marcarViejo/{id}")
    public String marcarViejo(@PathVariable Long id, RedirectAttributes flash) {
        Cilindro c = cilindroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cilindro no encontrado"));
        c.setEstado(Cilindro.EstadoCilindro.VIEJO);
        cilindroRepository.save(c);
        flash.addFlashAttribute("exito", "Cilindro marcado como viejo.");
        return "redirect:/cilindros";
    }

    private int orCero(Integer valor) {
        return valor == null ? 0 : valor;
    }
}