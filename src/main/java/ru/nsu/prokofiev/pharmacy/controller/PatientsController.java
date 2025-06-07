package ru.nsu.prokofiev.pharmacy.controller;

import ru.nsu.prokofiev.pharmacy.model.Patients;
import ru.nsu.prokofiev.pharmacy.service.PatientsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientsController {

    private final PatientsService service;

    public PatientsController(PatientsService service) {
        this.service = service;
    }

    @GetMapping
    public List<Patients> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patients> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Patients create(@RequestBody Patients patient) {
        return service.save(patient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patients> update(@PathVariable Long id, @RequestBody Patients updated) {
        return service.findById(id)
                .map(existing -> {
                    existing.setFullName(updated.getFullName());
                    existing.setBirthday(updated.getBirthday());
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
