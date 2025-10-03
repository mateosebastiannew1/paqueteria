package com.paqueteria.controller;

import com.paqueteria.model.Paquete;
import com.paqueteria.repository.PaqueteRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paquetes")
public class PaqueteController {
    private final PaqueteRepository repo;

    public PaqueteController(PaqueteRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Paquete> list() { return repo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Paquete> get(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Paquete> create(@Valid @RequestBody Paquete paquete) {
        Paquete saved = repo.save(paquete);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paquete> update(@PathVariable Long id, @Valid @RequestBody Paquete paquete) {
        return repo.findById(id).map(existing -> {
            existing.setDescripcion(paquete.getDescripcion());
            existing.setPeso(paquete.getPeso());
            existing.setDestino(paquete.getDestino());
            existing.setEstado(paquete.getEstado());
            existing.setFechaEnvio(paquete.getFechaEnvio());
            existing.setFechaEntrega(paquete.getFechaEntrega());
            repo.save(existing);
            return ResponseEntity.ok(existing);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return repo.findById(id).map(existing -> {
            repo.delete(existing);
            return ResponseEntity.<Void>ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
