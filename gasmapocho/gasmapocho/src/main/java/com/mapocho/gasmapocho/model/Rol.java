package com.mapocho.gasmapocho.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRol;

    @Column(nullable = false, unique = true, length = 30)
    private String nombreRol; // ADMINISTRADOR, JEFE_ALMACEN, ASISTENTE, AUDITOR
}