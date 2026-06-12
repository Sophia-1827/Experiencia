package com.mapocho.gasmapocho.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cilindros")
public class Cilindro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCilindro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCilindro tipo; // KG_5, KG_11, KG_15

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCilindro estado; // LLENO, VACIO, VENDIDO, VIEJO

    @Column(nullable = false)
    private Integer cantidad = 0;

    public enum TipoCilindro {
        KG_5, KG_11, KG_15
    }

    public enum EstadoCilindro {
        LLENO, VACIO, VENDIDO, VIEJO
    }
}