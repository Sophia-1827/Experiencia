package com.mapocho.gasmapocho.repository;

import com.mapocho.gasmapocho.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findTop20ByOrderByFechaHoraDesc();
}