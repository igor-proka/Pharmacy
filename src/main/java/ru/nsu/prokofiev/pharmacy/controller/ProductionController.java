package ru.nsu.prokofiev.pharmacy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.prokofiev.pharmacy.model.Production;
import ru.nsu.prokofiev.pharmacy.service.ProductionService;

import java.util.List;

@RestController
@RequestMapping("/api/production")
public class ProductionController {

    private final ProductionService service;

    public ProductionController(ProductionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Production> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Production> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Production create(@RequestBody Production production) {
        return service.save(production);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Production> update(@PathVariable Long id, @RequestBody Production updated) {
        return service.findById(id)
                .map(existing -> {
                    updated.setId(id);
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
