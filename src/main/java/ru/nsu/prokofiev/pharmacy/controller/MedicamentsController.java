package ru.nsu.prokofiev.pharmacy.controller;

import ru.nsu.prokofiev.pharmacy.model.Medicaments;
import ru.nsu.prokofiev.pharmacy.service.MedicamentsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicaments")
public class MedicamentsController {

    private final MedicamentsService service;

    public MedicamentsController(MedicamentsService service) {
        this.service = service;
    }

    @GetMapping
    public List<Medicaments> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medicaments> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Medicaments create(@RequestBody Medicaments medicament) {
        return service.save(medicament);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medicaments> update(@PathVariable Long id, @RequestBody Medicaments updated) {
        return service.findById(id)
                .map(existing -> {
                    existing.setName(updated.getName());
                    existing.setType(updated.getType());
                    existing.setUnit(updated.getUnit());
                    existing.setCookable(updated.getCookable());
                    existing.setPrescriptionRequired(updated.getPrescriptionRequired());
                    existing.setDescription(updated.getDescription());
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
