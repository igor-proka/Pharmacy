package ru.nsu.prokofiev.pharmacy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.prokofiev.pharmacy.model.ReservedMedicaments;
import ru.nsu.prokofiev.pharmacy.model.ReservedMedicamentsId;
import ru.nsu.prokofiev.pharmacy.service.ReservedMedicamentsService;

import java.util.List;

@RestController
@RequestMapping("/api/reserved-medicaments")
public class ReservedMedicamentsController {

    private final ReservedMedicamentsService service;

    public ReservedMedicamentsController(ReservedMedicamentsService service) {
        this.service = service;
    }

    @GetMapping
    public List<ReservedMedicaments> getAll() {
        return service.findAll();
    }

    @GetMapping("/{orderId}/{storageId}")
    public ResponseEntity<ReservedMedicaments> getById(@PathVariable Long orderId,
                                                       @PathVariable Long storageId) {
        ReservedMedicamentsId id = new ReservedMedicamentsId(orderId, storageId);
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ReservedMedicaments create(@RequestBody ReservedMedicaments rm) {
        return service.save(rm);
    }

    @PutMapping("/{orderId}/{storageId}")
    public ResponseEntity<ReservedMedicaments> update(@PathVariable Long orderId,
                                                      @PathVariable Long storageId,
                                                      @RequestBody ReservedMedicaments updated) {
        ReservedMedicamentsId id = new ReservedMedicamentsId(orderId, storageId);
        return service.findById(id)
                .map(existing -> {
                    updated.setId(id);
                    return ResponseEntity.ok(service.save(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{orderId}/{storageId}")
    public ResponseEntity<Void> delete(@PathVariable Long orderId,
                                       @PathVariable Long storageId) {
        ReservedMedicamentsId id = new ReservedMedicamentsId(orderId, storageId);
        if (service.findById(id).isPresent()) {
            service.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
