package ru.nsu.prokofiev.pharmacy.controller;

import ru.nsu.prokofiev.pharmacy.model.MedicamentPackages;
import ru.nsu.prokofiev.pharmacy.service.MedicamentPackagesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicament-packages")
public class MedicamentPackagesController {

    private final MedicamentPackagesService service;

    public MedicamentPackagesController(MedicamentPackagesService service) {
        this.service = service;
    }

    @GetMapping
    public List<MedicamentPackages> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicamentPackages> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public MedicamentPackages create(@RequestBody MedicamentPackages entity) {
        return service.save(entity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicamentPackages> update(@PathVariable Long id, @RequestBody MedicamentPackages updated) {
        return service.findById(id)
                .map(existing -> {
                    existing.setMedicament(updated.getMedicament());
                    existing.setPrice(updated.getPrice());
                    existing.setCriticalAmount(updated.getCriticalAmount());
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
