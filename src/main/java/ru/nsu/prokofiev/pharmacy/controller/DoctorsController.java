package ru.nsu.prokofiev.pharmacy.controller;

import ru.nsu.prokofiev.pharmacy.model.Doctors;
import ru.nsu.prokofiev.pharmacy.service.DoctorsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorsController {

    private final DoctorsService service;

    public DoctorsController(DoctorsService service) {
        this.service = service;
    }

    @GetMapping
    public List<Doctors> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctors> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Doctors create(@RequestBody Doctors doctor) {
        return service.save(doctor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctors> update(@PathVariable Long id, @RequestBody Doctors updated) {
        return service.findById(id)
                .map(existing -> {
                    existing.setFullName(updated.getFullName());
                    existing.setPosition(updated.getPosition());
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
