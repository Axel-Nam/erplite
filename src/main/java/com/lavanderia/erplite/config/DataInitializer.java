package com.lavanderia.erplite.config;

import com.lavanderia.erplite.model.*;
import com.lavanderia.erplite.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ClienteRepository clienteRepository;
    private final PrendaRepository prendaRepository;
    private final OrdenServicioRepository ordenServicioRepository;
    private final DiscrepanciaRepository discrepanciaRepository;

    public DataInitializer(ClienteRepository clienteRepository,
                           PrendaRepository prendaRepository,
                           OrdenServicioRepository ordenServicioRepository,
                           DiscrepanciaRepository discrepanciaRepository) {
        this.clienteRepository = clienteRepository;
        this.prendaRepository = prendaRepository;
        this.ordenServicioRepository = ordenServicioRepository;
        this.discrepanciaRepository = discrepanciaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (discrepanciaRepository.count() == 0) {
            
            //clientes
            Cliente hotel = clienteRepository.findAll().stream()
                    .filter(c -> "20123456789".equals(c.getRuc()))
                    .findFirst()
                    .orElseGet(() -> {
                        Cliente c = new Cliente();
                        c.setRazonSocial("Hotel Miramar S.A.C.");
                        c.setRuc("20123456789");
                        c.setTipoCliente("HOTEL");
                        return clienteRepository.save(c);
                    });

            Cliente clinica = clienteRepository.findAll().stream()
                    .filter(c -> "20555444333".equals(c.getRuc()))
                    .findFirst()
                    .orElseGet(() -> {
                        Cliente c = new Cliente();
                        c.setRazonSocial("Clinica San Pablo S.A.");
                        c.setRuc("20555444333");
                        c.setTipoCliente("CLINICA");
                        return clienteRepository.save(c);
                    });

            //prendas
            Prenda sabana = prendaRepository.findAll().stream()
                    .filter(p -> "Sábana King Size".equals(p.getNombre()))
                    .findFirst()
                    .orElseGet(() -> {
                        Prenda p = new Prenda();
                        p.setNombre("Sábana King Size");
                        p.setCategoria("LENCERIA");
                        p.setDescripcion("Algodón 300 hilos");
                        return prendaRepository.save(p);
                    });

            Prenda toalla = prendaRepository.findAll().stream()
                    .filter(p -> "Toalla de Baño".equals(p.getNombre()))
                    .findFirst()
                    .orElseGet(() -> {
                        Prenda p = new Prenda();
                        p.setNombre("Toalla de Baño");
                        p.setCategoria("LENCERIA");
                        p.setDescripcion("Toalla felpa blanca");
                        return prendaRepository.save(p);
                    });

            //ordenes
            OrdenServicio orden1 = new OrdenServicio();
            orden1.setCliente(hotel);
            orden1.setPesoKilos(85.0);
            orden1.setEstado("DESPACHADO");
            orden1 = ordenServicioRepository.save(orden1);

            //discrepancias iniciales
            Discrepancia d1 = new Discrepancia();
            d1.setOrdenServicio(orden1);
            d1.setPrenda(sabana);
            d1.setCantidadDespachada(100);
            d1.setCantidadRecibidaCliente(95);
            d1.setObservacion("Faltan 5 sábanas en el bulto #2");
            discrepanciaRepository.save(d1);

            Discrepancia d2 = new Discrepancia();
            d2.setOrdenServicio(orden1);
            d2.setPrenda(toalla);
            d2.setCantidadDespachada(50);
            d2.setCantidadRecibidaCliente(48);
            d2.setObservacion("Toallas no encontradas al recibir");
            discrepanciaRepository.save(d2);
        }
    }
}