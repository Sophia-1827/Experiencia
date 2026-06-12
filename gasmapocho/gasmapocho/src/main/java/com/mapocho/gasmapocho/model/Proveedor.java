package com.mapocho.gasmapocho.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "proveedores")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProveedor;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 20)
    private String ruc;

    @Column(length = 20)
    private String telefono;

    @Column(nullable = false)
    private boolean activo = true;
}