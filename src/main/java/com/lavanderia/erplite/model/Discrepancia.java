package com.lavanderia.erplite.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "discrepancias")
public class Discrepancia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "orden_id", nullable = false)
    private OrdenServicio ordenServicio;

    @ManyToOne
    @JoinColumn(name = "prenda_id", nullable = false)
    private Prenda prenda;

    @Column(name = "cantidad_despachada", nullable = false)
    private Integer cantidadDespachada;

    @Column(name = "cantidad_recibida_cliente", nullable = false)
    private Integer cantidadRecibidaCliente;

    @Column(name = "diferencia", nullable = false)
    private Integer diferencia; // (Despachada - Recibida)

    @Column(name = "tipo_resolucion", length = 50)
    private String tipoResolucion; // PENDIENTE, REPOSICION_ENCONTRADA, NOTA_CREDITO_COBRO

    @Column(name = "observacion", length = 255)
    private String observacion;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @PrePersist
    public void prePersist() {
        if (this.fechaRegistro == null) {
            this.fechaRegistro = LocalDateTime.now();
        }
        if (this.tipoResolucion == null) {
            this.tipoResolucion = "PENDIENTE";
        }
        this.diferencia = this.cantidadDespachada - this.cantidadRecibidaCliente;
    }
}