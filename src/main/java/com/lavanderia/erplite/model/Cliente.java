package com.lavanderia.erplite.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "razon_social", nullable = false)
    private String razonSocial;

    @Column(nullable = false, unique = true, length = 11)
    private String ruc;

    @Column(name = "tipo_cliente", nullable = false)
    private String tipoCliente;

    public Cliente() {}

    public Cliente(Long id, String razonSocial, String ruc, String tipoCliente) {
        this.id = id;
        this.razonSocial = razonSocial;
        this.ruc = ruc;
        this.tipoCliente = tipoCliente;
    }
}