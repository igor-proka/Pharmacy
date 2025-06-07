package ru.nsu.prokofiev.pharmacy.controller;

import ru.nsu.prokofiev.pharmacy.model.MedicamentUse;
import ru.nsu.prokofiev.pharmacy.service.MedicamentUseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicament-use")
public class MedicamentUseController {

    private final MedicamentUseService service;

    public MedicamentUseController(MedicamentUseService service) {
        this.service = service;
    }

    @GetMapping
    public List<MedicamentUse> getAll() {
        return service.findAll();
    }

    @PostMapping
    public MedicamentUse create(@RequestBody MedicamentUse medicamentUse) {
        return service.save(medicamentUse);
    }

    @PutMapping("/{oldTypeId}/{oldUseId}")
    public MedicamentUse update(
            @PathVariable Long oldTypeId,
            @PathVariable Long oldUseId,
            @RequestBody MedicamentUse updated
    ) {
        service.delete(oldTypeId, oldUseId);
        return service.save(updated);
    }

    @DeleteMapping("/{typeId}/{useId}")
    public ResponseEntity<Void> delete(@PathVariable Long typeId, @PathVariable Long useId) {
        service.delete(typeId, useId);
        return ResponseEntity.noContent().build();
    }
}
