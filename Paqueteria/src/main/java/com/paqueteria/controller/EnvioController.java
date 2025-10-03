package com.paqueteria.controller;

import com.paqueteria.model.Envio;
import com.paqueteria.model.Paquete;
import com.paqueteria.model.Cliente;
import com.paqueteria.repository.EnvioRepository;
import com.paqueteria.repository.PaqueteRepository;
import com.paqueteria.repository.ClienteRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/envios")
public class EnvioController {
    private final EnvioRepository repo;
    private final ClienteRepository clienteRepo;
    private final PaqueteRepository paqueteRepo;

    public EnvioController(EnvioRepository repo, ClienteRepository clienteRepo, PaqueteRepository paqueteRepo) {
        this.repo = repo; this.clienteRepo = clienteRepo; this.paqueteRepo = paqueteRepo;
    }

    @GetMapping
    public List<Envio> list() { return repo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Envio> get(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Envio envio) {
        // Ensure cliente and paquete exist (client should send objects with id only or full object)
        if (envio.getCliente() == null || envio.getCliente().getId() == null) {
            return ResponseEntity.badRequest().body("cliente.id es requerido");
        }
        if (envio.getPaquete() == null || envio.getPaquete().getId() == null) {
            return ResponseEntity.badRequest().body("paquete.id es requerido");
        }
        Cliente c = clienteRepo.findById(envio.getCliente().getId()).orElse(null);
        Paquete p = paqueteRepo.findById(envio.getPaquete().getId()).orElse(null);
        if (c == null || p == null) return ResponseEntity.badRequest().body("cliente o paquete no encontrado");
        envio.setCliente(c);
        envio.setPaquete(p);
        if (envio.getFechaRegistro() == null) envio.setFechaRegistro(LocalDateTime.now());
        Envio saved = repo.save(envio);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Envio envio) {
        return repo.findById(id).map(existing -> {
            if (envio.getCliente() != null && envio.getCliente().getId() != null) {
                Cliente c = clienteRepo.findById(envio.getCliente().getId()).orElse(null);
                if (c == null) return ResponseEntity.badRequest().body("cliente no encontrado");
                existing.setCliente(c);
            }
            if (envio.getPaquete() != null && envio.getPaquete().getId() != null) {
                Paquete p = paqueteRepo.findById(envio.getPaquete().getId()).orElse(null);
                if (p == null) return ResponseEntity.badRequest().body("paquete no encontrado");
                existing.setPaquete(p);
            }
            existing.setFechaRegistro(envio.getFechaRegistro() != null ? envio.getFechaRegistro() : existing.getFechaRegistro());
            existing.setCosto(envio.getCosto());
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
