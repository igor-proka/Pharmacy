package ru.nsu.prokofiev.pharmacy.controller;

import ru.nsu.prokofiev.pharmacy.model.Suppliers;
import ru.nsu.prokofiev.pharmacy.service.SuppliersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SuppliersController {

    private final SuppliersService service;

    public SuppliersController(SuppliersService service) {
        this.service = service;
    }

    @GetMapping
    public List<Suppliers> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Suppliers> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Suppliers create(@RequestBody Suppliers supplier) {
        return service.save(supplier);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Suppliers> update(@PathVariable Long id, @RequestBody Suppliers updated) {
        return service.findById(id)
                .map(existing -> {
                    existing.setName(updated.getName());
                    existing.setPhoneNumber(updated.getPhoneNumber());
                    existing.setAddress(updated.getAddress());
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
