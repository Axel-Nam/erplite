package com.lavanderia.erplite.controller;

import com.lavanderia.erplite.model.Discrepancia;
import com.lavanderia.erplite.model.OrdenServicio;
import com.lavanderia.erplite.repository.ClienteRepository;
import com.lavanderia.erplite.repository.DiscrepanciaRepository;
import com.lavanderia.erplite.repository.PrendaRepository;
import com.lavanderia.erplite.service.OrdenServicioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
@RequestMapping("/ordenes")
public class OrdenViewController {

    private final OrdenServicioService ordenServicioService;
    private final ClienteRepository clienteRepository;
    private final DiscrepanciaRepository discrepanciaRepository;
    private final PrendaRepository prendaRepository;

    public OrdenViewController(OrdenServicioService ordenServicioService, 
                               ClienteRepository clienteRepository, 
                               DiscrepanciaRepository discrepanciaRepository,
                               PrendaRepository prendaRepository) {
        this.ordenServicioService = ordenServicioService;
        this.clienteRepository = clienteRepository;
        this.discrepanciaRepository = discrepanciaRepository;
        this.prendaRepository = prendaRepository;
    }

    // Criterios de ordenamiento disponibles: clave -> etiqueta y tipo (para los textos A-Z / menor-mayor / fecha)
    private static final Map<String, String[]> CRITERIOS = new LinkedHashMap<>();
    static {
        CRITERIOS.put("faltante",    new String[]{"Faltante (cantidad)",   "numero"});
        CRITERIOS.put("despachado",  new String[]{"Cant. despachada",      "numero"});
        CRITERIOS.put("recibido",    new String[]{"Cant. recibida",        "numero"});
        CRITERIOS.put("peso",        new String[]{"Peso de la orden (kg)", "numero"});
        CRITERIOS.put("hotel",       new String[]{"Nombre del hotel",      "texto"});
        CRITERIOS.put("prenda",      new String[]{"Nombre de la prenda",   "texto"});
        CRITERIOS.put("fecha",       new String[]{"Fecha de registro",     "fecha"});
        CRITERIOS.put("id",          new String[]{"N.º de registro",       "numero"});
    }

    private static final Collator COLLATOR = Collator.getInstance(Locale.forLanguageTag("es"));
    static {
        COLLATOR.setStrength(Collator.PRIMARY); // ignora mayúsculas y tildes al ordenar
    }
    private static final Comparator<String> TEXTO = Comparator.nullsLast(COLLATOR::compare);

    private Comparator<Discrepancia> comparadorPara(String criterio) {
        switch (criterio) {
            case "despachado": return Comparator.comparing(Discrepancia::getCantidadDespachada, Comparator.nullsLast(Comparator.naturalOrder()));
            case "recibido":   return Comparator.comparing(Discrepancia::getCantidadRecibidaCliente, Comparator.nullsLast(Comparator.naturalOrder()));
            case "peso":       return Comparator.comparing((Discrepancia d) -> d.getOrdenServicio().getPesoKilos(), Comparator.nullsLast(Comparator.<Double>naturalOrder()));
            case "hotel":      return Comparator.comparing((Discrepancia d) -> d.getOrdenServicio().getCliente().getRazonSocial(), TEXTO);
            case "prenda":     return Comparator.comparing((Discrepancia d) -> d.getPrenda().getNombre(), TEXTO);
            case "fecha":      return Comparator.comparing(Discrepancia::getFechaRegistro, Comparator.nullsLast(Comparator.naturalOrder()));
            case "id":         return Comparator.comparing(Discrepancia::getId, Comparator.nullsLast(Comparator.naturalOrder()));
            case "faltante":
            default:           return Comparator.comparing(Discrepancia::getDiferencia, Comparator.nullsLast(Comparator.naturalOrder()));
        }
    }

    @GetMapping
    public String verVistaOrdenes(
            @RequestParam(required = false) String estado, 
            @RequestParam(required = false) String discFiltro,
            @RequestParam(required = false, defaultValue = "faltante") String orden,
            @RequestParam(required = false, defaultValue = "desc") String dir,
            Model model) {

        if (!CRITERIOS.containsKey(orden)) {
            orden = "faltante";
        }
        if (!"asc".equals(dir)) {
            dir = "desc";
        }
        
        List<OrdenServicio> ordenes;
        if (estado != null && !estado.isBlank()) {
            ordenes = ordenServicioService.listarPorEstado(estado);
        } else {
            ordenes = ordenServicioService.listarTodas();
        }

        List<Discrepancia> discrepancias;
        if (discFiltro != null && !discFiltro.isBlank()) {
            discrepancias = new ArrayList<>(discrepanciaRepository.findByTipoResolucion(discFiltro));
        } else {
            discrepancias = new ArrayList<>(discrepanciaRepository.findAll());
        }

        Comparator<Discrepancia> comparador = comparadorPara(orden);
        if ("desc".equals(dir)) {
            comparador = comparador.reversed();
        }
        // desempate estable: si hay empate, el registro más reciente va primero
        discrepancias.sort(comparador.thenComparing(Discrepancia::getId, Comparator.reverseOrder()));

        model.addAttribute("ordenes", ordenes);
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("prendas", prendaRepository.findAll());
        model.addAttribute("estadoActual", estado);
        model.addAttribute("nuevaOrden", new OrdenServicio());
        
        model.addAttribute("discrepancias", discrepancias);
        model.addAttribute("discFiltroActual", discFiltro);
        model.addAttribute("criterios", CRITERIOS);
        model.addAttribute("ordenActual", orden);
        model.addAttribute("dirActual", dir);
        model.addAttribute("tipoOrdenActual", CRITERIOS.get(orden)[1]);
        model.addAttribute("etiquetaOrdenActual", CRITERIOS.get(orden)[0]);
        model.addAttribute("nuevaDiscrepancia", new Discrepancia());
        
        return "ordenes-listado";
    }

    @PostMapping("/guardar")
    public String guardarOrdenDesdeWeb(@ModelAttribute OrdenServicio orden) {
        ordenServicioService.guardar(orden);
        return "redirect:/ordenes";
    }

    @PostMapping("/{id}/cambiar-estado")
    public String cambiarEstadoWeb(@PathVariable Integer id, @RequestParam String estado) {
        ordenServicioService.actualizarEstado(id, estado);
        return "redirect:/ordenes";
    }

    @PostMapping("/discrepancias/guardar")
    public String guardarDiscrepancia(@ModelAttribute Discrepancia discrepancia) {
        discrepanciaRepository.save(discrepancia);
        return "redirect:/ordenes";
    }

    @PostMapping("/discrepancias/{id}/resolver")
    public String resolverDiscrepancia(@PathVariable Integer id, @RequestParam String tipoResolucion) {
        Discrepancia disc = discrepanciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discrepancia no encontrada"));
        disc.setTipoResolucion(tipoResolucion);
        discrepanciaRepository.save(disc);
        return "redirect:/ordenes";
    }
}