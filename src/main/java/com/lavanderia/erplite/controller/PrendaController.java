package com.lavanderia.erplite.controller;

import com.lavanderia.erplite.model.Prenda;
import com.lavanderia.erplite.repository.PrendaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prendas")
public class PrendaController {

    private final PrendaRepository prendaRepository;

    public PrendaController(PrendaRepository prendaRepository) {
        this.prendaRepository = prendaRepository;
    }

    @GetMapping
    public List<Prenda> listarPrendas() {
        return prendaRepository.findAll();
    }

    @PostMapping
    public Prenda crearPrenda(@RequestBody Prenda prenda) {
        return prendaRepository.save(prenda);
    }
}