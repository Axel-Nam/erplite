package com.lavanderia.erplite.service;

import com.lavanderia.erplite.model.OrdenServicio;
import com.lavanderia.erplite.repository.OrdenServicioRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdenServicioService {

    private final OrdenServicioRepository ordenServicioRepository;

    public OrdenServicioService(OrdenServicioRepository ordenServicioRepository) {
        this.ordenServicioRepository = ordenServicioRepository;
    }

    public List<OrdenServicio> listarTodas() {
        return ordenServicioRepository.findAll();
    }

    public List<OrdenServicio> listarPorEstado(String estado) {
        return ordenServicioRepository.findByEstado(estado.toUpperCase());
    }

    public List<OrdenServicio> listarPorCliente(Long clienteId) {
        return ordenServicioRepository.findByClienteId(clienteId);
    }

    public OrdenServicio guardar(OrdenServicio orden) {
        if (orden.getEstado() == null || orden.getEstado().trim().isEmpty()) {
            orden.setEstado("PENDIENTE");
        }
        if (orden.getFechaIngreso() == null) {
            orden.setFechaIngreso(LocalDateTime.now());
        }
        return ordenServicioRepository.save(orden);
    }

    public OrdenServicio actualizarEstado(Integer id, String nuevoEstado) {
        OrdenServicio orden = ordenServicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + id));
        
        orden.setEstado(nuevoEstado.toUpperCase());
        return ordenServicioRepository.save(orden);
    }
}