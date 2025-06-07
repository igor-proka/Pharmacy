package ru.nsu.prokofiev.pharmacy.controller;

import ru.nsu.prokofiev.pharmacy.model.MedicamentTypes;
import ru.nsu.prokofiev.pharmacy.service.MedicamentTypesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicament-types")
public class MedicamentTypesController {

    private final MedicamentTypesService service;

    public MedicamentTypesController(MedicamentTypesService service) {
        this.service = service;
    }

    @GetMapping
    public List<MedicamentTypes> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicamentTypes> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public MedicamentTypes create(@RequestBody MedicamentTypes medicamentType) {
        return service.save(medicamentType);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicamentTypes> update(@PathVariable Long id, @RequestBody MedicamentTypes updated) {
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

