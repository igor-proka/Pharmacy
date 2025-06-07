package ru.nsu.prokofiev.pharmacy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.prokofiev.pharmacy.model.WaitingMedicaments;
import ru.nsu.prokofiev.pharmacy.model.WaitingMedicamentsId;
import ru.nsu.prokofiev.pharmacy.service.WaitingMedicamentsService;

import java.util.List;

@RestController
@RequestMapping("/api/waiting-medicaments")
public class WaitingMedicamentsController {

    private final WaitingMedicamentsService service;

    public WaitingMedicamentsController(WaitingMedicamentsService service) {
        this.service = service;
    }

    @GetMapping
    public List<WaitingMedicaments> getAll() {
        return service.findAll();
    }

    @GetMapping("/{orderId}/{medicamentId}")
    public ResponseEntity<WaitingMedicaments> getById(@PathVariable Long orderId,
                                                      @PathVariable Long medicamentId) {
        WaitingMedicamentsId id = new WaitingMedicamentsId(orderId, medicamentId);
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public WaitingMedicaments create(@RequestBody WaitingMedicaments wm) {
        return service.save(wm);
    }

    @PutMapping("/{orderId}/{medicamentId}")
    public ResponseEntity<WaitingMedicaments> update(@PathVariable Long orderId,
                                                     @PathVariable Long medicamentId,
                                                     @RequestBody WaitingMedicaments updated) {
        WaitingMedicamentsId id = new WaitingMedicamentsId(orderId, medicamentId);
        return service.findById(id)
                .map(existing -> {
                    updated.setId(id);
                    return ResponseEntity.ok(service.save(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{orderId}/{medicamentId}")
    public ResponseEntity<Void> delete(@PathVariable Long orderId,
                                       @PathVariable Long medicamentId) {
        WaitingMedicamentsId id = new WaitingMedicamentsId(orderId, medicamentId);
        if (service.findById(id).isPresent()) {
            service.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
