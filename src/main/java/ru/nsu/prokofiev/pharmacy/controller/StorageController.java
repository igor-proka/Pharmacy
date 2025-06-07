package ru.nsu.prokofiev.pharmacy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.prokofiev.pharmacy.model.Storage;
import ru.nsu.prokofiev.pharmacy.service.StorageService;

import java.util.List;

@RestController
@RequestMapping("/api/storage")
public class StorageController {

    private final StorageService service;

    public StorageController(StorageService service) {
        this.service = service;
    }

    @GetMapping
    public List<Storage> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Storage> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Storage create(@RequestBody Storage storage) {
        return service.save(storage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Storage> update(@PathVariable Long id, @RequestBody Storage updated) {
        return service.findById(id)
                .map(existing -> {
                    existing.setMedicamentPackage(updated.getMedicamentPackage());
                    existing.setAvailableAmount(updated.getAvailableAmount());
                    existing.setOriginalAmount(updated.getOriginalAmount());
                    existing.setReceiptDatetime(updated.getReceiptDatetime());
                    existing.setManufactureDate(updated.getManufactureDate());
                    existing.setShelfLife(updated.getShelfLife());
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
