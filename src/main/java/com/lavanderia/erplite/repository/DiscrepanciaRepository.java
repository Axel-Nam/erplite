package com.lavanderia.erplite.repository;

import com.lavanderia.erplite.model.Discrepancia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DiscrepanciaRepository extends JpaRepository<Discrepancia, Integer> {
    // ordenar por cliente, prenda, peso, etc. con un solo mecanismo.
    List<Discrepancia> findByTipoResolucion(String tipoResolucion);
}
