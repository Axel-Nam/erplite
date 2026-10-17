package com.lavanderia.erplite.controller;

import com.lavanderia.erplite.model.OrdenServicio;
import com.lavanderia.erplite.service.OrdenServicioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenServicioController {

    private final OrdenServicioService ordenServicioService;

    public OrdenServicioController(OrdenServicioService ordenServicioService) {
        this.ordenServicioService = ordenServicioService;
    }

    // filtros opcionales: estado y clienteId
    @GetMapping
    public List<OrdenServicio> listar(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Long clienteId) {
        
        if (estado != null && !estado.isBlank()) {
            return ordenServicioService.listarPorEstado(estado);
        }
        if (clienteId != null) {
            return ordenServicioService.listarPorCliente(clienteId);
        }
        
        return ordenServicioService.listarTodas();
    }

    @PostMapping
    public OrdenServicio crear(@RequestBody OrdenServicio orden) {
        return ordenServicioService.guardar(orden);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<OrdenServicio> cambiarEstado(
            @PathVariable Integer id, 
            @RequestBody Map<String, String> body) {
        
        String nuevoEstado = body.get("estado");
        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        OrdenServicio actualizada = ordenServicioService.actualizarEstado(id, nuevoEstado);
        return ResponseEntity.ok(actualizada);
    }
}