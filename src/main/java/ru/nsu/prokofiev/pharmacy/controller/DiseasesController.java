package ru.nsu.prokofiev.pharmacy.controller;

import ru.nsu.prokofiev.pharmacy.model.Diseases;
import ru.nsu.prokofiev.pharmacy.service.DiseasesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diseases")
public class DiseasesController {

    private final DiseasesService service;

    public DiseasesController(DiseasesService service) {
        this.service = service;
    }

    @GetMapping
    public List<Diseases> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Diseases> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Diseases create(@RequestBody Diseases entity) {
        return service.save(entity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Diseases> update(@PathVariable Long id, @RequestBody Diseases updated) {
        return service.findById(id)
                .map(existing -> {
                    existing.setName(updated.getName());
                    return ResponseEntity.ok(service.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.findById(id).isPresent()) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
