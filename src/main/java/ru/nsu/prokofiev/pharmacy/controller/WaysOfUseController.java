package ru.nsu.prokofiev.pharmacy.controller;

import ru.nsu.prokofiev.pharmacy.model.WaysOfUse;
import ru.nsu.prokofiev.pharmacy.service.WaysOfUseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ways-of-use")
public class WaysOfUseController {

    private final WaysOfUseService service;

    public WaysOfUseController(WaysOfUseService service) {
        this.service = service;
    }

    @GetMapping
    public List<WaysOfUse> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WaysOfUse> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public WaysOfUse create(@RequestBody WaysOfUse item) {
        return service.save(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WaysOfUse> update(@PathVariable Long id, @RequestBody WaysOfUse updated) {
        return service.findById(id)
                .map(existing -> {
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
