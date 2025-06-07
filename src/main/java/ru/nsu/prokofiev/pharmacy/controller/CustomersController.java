package ru.nsu.prokofiev.pharmacy.controller;

import ru.nsu.prokofiev.pharmacy.model.Customers;
import ru.nsu.prokofiev.pharmacy.service.CustomersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomersController {

    private final CustomersService service;

    public CustomersController(CustomersService service) {
        this.service = service;
    }

    @GetMapping
    public List<Customers> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customers> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Customers create(@RequestBody Customers customer) {
        return service.save(customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customers> update(@PathVariable Long id, @RequestBody Customers updated) {
        return service.findById(id)
                .map(existing -> {
                    existing.setFullName(updated.getFullName());
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
