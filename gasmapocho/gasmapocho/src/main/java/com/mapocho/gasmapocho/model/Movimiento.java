package com.mapocho.gasmapocho.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "movimientos")
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMovimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimiento tipo; // ENTRADA, SALIDA, DEVOLUCION

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cilindro", nullable = false)
    private Cilindro cilindro;

    @Column(nullable = false)
    private Integer cantidad;

    // Solo aplica cuando tipo = ENTRADA (desde proveedor)
    private Double kgPorUnidad;

    // Solo aplica cuando tipo = ENTRADA
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;

    // Usuario que registró el movimiento (auditoría)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    private String observacion;

    @PrePersist
    public void prePersist() {
        this.fechaHora = LocalDateTime.now();
    }

    public enum TipoMovimiento {
        ENTRADA, SALIDA, DEVOLUCION
    }
}