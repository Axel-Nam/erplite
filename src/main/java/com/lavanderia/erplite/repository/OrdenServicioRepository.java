package com.lavanderia.erplite.repository;

import com.lavanderia.erplite.model.OrdenServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrdenServicioRepository extends JpaRepository<OrdenServicio, Integer> {
    
    // filtrado por estado de la orden
    List<OrdenServicio> findByEstado(String estado);

    // orden por cliente
    List<OrdenServicio> findByClienteId(Long clienteId);
}