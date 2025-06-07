package ru.nsu.prokofiev.pharmacy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.prokofiev.pharmacy.model.Technologies;
import ru.nsu.prokofiev.pharmacy.service.TechnologiesService;

import java.util.List;

@RestController
@RequestMapping("/api/technologies")
public class TechnologiesController {

    private final TechnologiesService service;

    public TechnologiesController(TechnologiesService service) {
        this.service = service;
    }

    @GetMapping
    public List<Technologies> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Technologies> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Technologies create(@RequestBody Technologies technology) {
        return service.save(technology);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Technologies> update(@PathVariable Long id, @RequestBody Technologies updated) {
        return service.findById(id)
                .map(existing -> {
                    updated.setId(existing.getId());
                    return ResponseEntity.ok(service.save(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.findById(id).isPresent()) {
            service.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
