package ru.nsu.prokofiev.pharmacy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.prokofiev.pharmacy.model.PreparationMethods;
import ru.nsu.prokofiev.pharmacy.service.PreparationMethodsService;

import java.util.List;

@RestController
@RequestMapping("/api/preparation-methods")
public class PreparationMethodsController {

    private final PreparationMethodsService service;

    public PreparationMethodsController(PreparationMethodsService service) {
        this.service = service;
    }

    @GetMapping
    public List<PreparationMethods> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PreparationMethods> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public PreparationMethods create(@RequestBody PreparationMethods method) {
        return service.save(method);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PreparationMethods> update(@PathVariable Long id, @RequestBody PreparationMethods updated) {
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
