package com.lavanderia.erplite.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ordenes_servicio")
public class OrdenServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "peso_kilos", nullable = false)
    private Double pesoKilos;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado;

    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDateTime fechaIngreso;

    @PrePersist
    public void prePersist() {
        if (this.estado == null || this.estado.trim().isEmpty()) {
            this.estado = "PENDIENTE";
        }
        if (this.fechaIngreso == null) {
            this.fechaIngreso = LocalDateTime.now();
        }
    }

    public OrdenServicio() {}

    public OrdenServicio(Integer id, Cliente cliente, Double pesoKilos, String estado, LocalDateTime fechaIngreso) {
        this.id = id;
        this.cliente = cliente;
        this.pesoKilos = pesoKilos;
        this.estado = estado;
        this.fechaIngreso = fechaIngreso;
    }
}