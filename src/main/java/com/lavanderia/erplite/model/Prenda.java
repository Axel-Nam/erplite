package com.lavanderia.erplite.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "prendas")
public class Prenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre; // Ej. "Sábana Hotelera King Size", "Toalla de Cuerpo", "Mameluco Industrial"

    @Column(name = "categoria", nullable = false, length = 50)
    private String categoria; // Ej. "LENCERIA", "UNIFORME", "MANTELERIA"

    @Column(name = "descripcion", length = 255)
    private String descripcion;
}