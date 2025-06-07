package ru.nsu.prokofiev.pharmacy.controller;

import ru.nsu.prokofiev.pharmacy.model.Prescriptions;
import ru.nsu.prokofiev.pharmacy.service.PrescriptionsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionsController {

    private final PrescriptionsService service;

    public PrescriptionsController(PrescriptionsService service) {
        this.service = service;
    }

    @GetMapping
    public List<Prescriptions> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prescriptions> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Prescriptions create(@RequestBody Prescriptions entity) {
        return service.save(entity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Prescriptions> update(@PathVariable Long id, @RequestBody Prescriptions updated) {
        return service.findById(id)
                .map(existing -> {
                    existing.setDisease(updated.getDisease());
                    existing.setPatient(updated.getPatient());
                    existing.setDoctor(updated.getDoctor());
                    existing.setPrescriptionDate(updated.getPrescriptionDate());
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
