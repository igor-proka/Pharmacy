package ru.nsu.prokofiev.pharmacy.controller;

import ru.nsu.prokofiev.pharmacy.model.UnitsOfMeasurement;
import ru.nsu.prokofiev.pharmacy.service.UnitsOfMeasurementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/units-of-measurement")
public class UnitsOfMeasurementController {

    private final UnitsOfMeasurementService service;

    public UnitsOfMeasurementController(UnitsOfMeasurementService service) {
        this.service = service;
    }

    @GetMapping
    public List<UnitsOfMeasurement> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnitsOfMeasurement> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public UnitsOfMeasurement create(@RequestBody UnitsOfMeasurement unit) {
        return service.save(unit);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UnitsOfMeasurement> update(@PathVariable Long id, @RequestBody UnitsOfMeasurement updated) {
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
