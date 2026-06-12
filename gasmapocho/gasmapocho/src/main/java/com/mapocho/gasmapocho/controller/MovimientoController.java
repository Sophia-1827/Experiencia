package com.mapocho.gasmapocho.controller;

import com.mapocho.gasmapocho.model.Cilindro;
import com.mapocho.gasmapocho.model.Movimiento;
import com.mapocho.gasmapocho.model.Usuario;
import com.mapocho.gasmapocho.repository.CilindroRepository;
import com.mapocho.gasmapocho.repository.MovimientoRepository;
import com.mapocho.gasmapocho.repository.ProveedorRepository;
import com.mapocho.gasmapocho.repository.UsuarioRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/movimientos")
public class MovimientoController {

    private final MovimientoRepository movimientoRepository;
    private final CilindroRepository cilindroRepository;
    private final ProveedorRepository proveedorRepository;
    private final UsuarioRepository usuarioRepository;

    public MovimientoController(MovimientoRepository movimientoRepository,
                                CilindroRepository cilindroRepository,
                                ProveedorRepository proveedorRepository,
                                UsuarioRepository usuarioRepository) {
        this.movimientoRepository = movimientoRepository;
        this.cilindroRepository = cilindroRepository;
        this.proveedorRepository = proveedorRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // ── VENTAS / SALIDAS ──────────────────────────────────────
    @GetMapping("/ventas")
    public String ventas(Model model) {
        model.addAttribute("cilindros", cilindroRepository.findAll());
        model.addAttribute("movimientos", movimientoRepository
                .findTop20ByOrderByFechaHoraDesc());
        return "movimientos/ventas";
    }

    @PostMapping("/ventas/registrar")
    public String registrarVenta(
            @RequestParam Long idCilindro,
            @RequestParam Integer cantidad,
            @RequestParam Movimiento.TipoMovimiento tipo,
            @RequestParam(required = false) String observacion,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes flash) {

        Cilindro cilindro = cilindroRepository.findById(idCilindro)
                .orElseThrow(() -> new RuntimeException("Cilindro no encontrado"));

        Usuario usuario = usuarioRepository
                .findByEmailAndActivoTrue(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Movimiento mov = new Movimiento();
        mov.setCilindro(cilindro);
        mov.setCantidad(cantidad);
        mov.setTipo(tipo);
        mov.setUsuario(usuario);
        mov.setObservacion(observacion);
        movimientoRepository.save(mov);

        // Actualizar stock
        if (tipo == Movimiento.TipoMovimiento.SALIDA) {
            cilindro.setCantidad(Math.max(0, cilindro.getCantidad() - cantidad));
            cilindro.setEstado(Cilindro.EstadoCilindro.VENDIDO);
        } else if (tipo == Movimiento.TipoMovimiento.DEVOLUCION) {
            cilindro.setCantidad(cilindro.getCantidad() + cantidad);
            cilindro.setEstado(Cilindro.EstadoCilindro.VACIO);
        }
        cilindroRepository.save(cilindro);

        flash.addFlashAttribute("exito", "Movimiento registrado correctamente.");
        return "redirect:/movimientos/ventas";
    }

    // ── PROVEEDORES ───────────────────────────────────────────
    @GetMapping("/proveedores")
    public String proveedores(Model model) {
        model.addAttribute("cilindros", cilindroRepository.findAll());
        model.addAttribute("proveedores", proveedorRepository.findByActivoTrue());
        model.addAttribute("movimientos", movimientoRepository
                .findTop20ByOrderByFechaHoraDesc());
        return "movimientos/proveedores";
    }

    @PostMapping("/proveedores/registrar")
    public String registrarEntrada(
            @RequestParam Long idCilindro,
            @RequestParam Long idProveedor,
            @RequestParam Integer cantidad,
            @RequestParam Double kgPorUnidad,
            @RequestParam(required = false) String observacion,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes flash) {

        Cilindro cilindro = cilindroRepository.findById(idCilindro)
                .orElseThrow(() -> new RuntimeException("Cilindro no encontrado"));

        Usuario usuario = usuarioRepository
                .findByEmailAndActivoTrue(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        var proveedor = proveedorRepository.findById(idProveedor)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        Movimiento mov = new Movimiento();
        mov.setCilindro(cilindro);
        mov.setCantidad(cantidad);
        mov.setKgPorUnidad(kgPorUnidad);
        mov.setTipo(Movimiento.TipoMovimiento.ENTRADA);
        mov.setProveedor(proveedor);
        mov.setUsuario(usuario);
        mov.setObservacion(observacion);
        movimientoRepository.save(mov);

        // Actualizar stock
        cilindro.setCantidad(cilindro.getCantidad() + cantidad);
        cilindro.setEstado(Cilindro.EstadoCilindro.LLENO);
        cilindroRepository.save(cilindro);

        flash.addFlashAttribute("exito", "Entrada registrada correctamente.");
        return "redirect:/movimientos/proveedores";
    }
}