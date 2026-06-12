package com.mapocho.gasmapocho.repository;

import com.mapocho.gasmapocho.model.Cilindro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface CilindroRepository extends JpaRepository<Cilindro, Long> {

    @Query("SELECT SUM(c.cantidad) FROM Cilindro c WHERE c.estado = 'LLENO'")
    Integer totalLlenos();

    @Query("SELECT SUM(c.cantidad) FROM Cilindro c WHERE c.estado = 'VACIO'")
    Integer totalVacios();

    @Query("SELECT SUM(c.cantidad) FROM Cilindro c WHERE c.estado = 'VENDIDO'")
    Integer totalVendidos();

    @Query("SELECT SUM(c.cantidad) FROM Cilindro c WHERE c.estado = 'VIEJO'")
    Integer totalViejos();

    @Query("SELECT SUM(c.cantidad) FROM Cilindro c WHERE c.tipo = ?1 AND c.estado = ?2")
    Integer totalPorTipoYEstado(Cilindro.TipoCilindro tipo, Cilindro.EstadoCilindro estado);
}