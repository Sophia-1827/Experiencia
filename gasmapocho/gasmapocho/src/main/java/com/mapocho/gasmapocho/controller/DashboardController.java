package com.mapocho.gasmapocho.controller;

import com.mapocho.gasmapocho.model.Cilindro;
import com.mapocho.gasmapocho.repository.CilindroRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final CilindroRepository cilindroRepository;

    public DashboardController(CilindroRepository cilindroRepository) {
        this.cilindroRepository = cilindroRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Totales generales
        model.addAttribute("totalLlenos",   orCero(cilindroRepository.totalLlenos()));
        model.addAttribute("totalVacios",   orCero(cilindroRepository.totalVacios()));
        model.addAttribute("totalVendidos", orCero(cilindroRepository.totalVendidos()));
        model.addAttribute("totalViejos",   orCero(cilindroRepository.totalViejos()));

        // Por tipo
        for (Cilindro.TipoCilindro tipo : Cilindro.TipoCilindro.values()) {
            String key = tipo.name().toLowerCase(); // kg_5, kg_11, kg_15
            for (Cilindro.EstadoCilindro estado : Cilindro.EstadoCilindro.values()) {
                String attr = key + "_" + estado.name().toLowerCase();
                model.addAttribute(attr,
                        orCero(cilindroRepository.totalPorTipoYEstado(tipo, estado)));
            }
        }
        return "dashboard";
    }

    private int orCero(Integer valor) {
        return valor == null ? 0 : valor;
    }
}