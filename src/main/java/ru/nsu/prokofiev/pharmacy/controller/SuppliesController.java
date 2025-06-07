package ru.nsu.prokofiev.pharmacy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.prokofiev.pharmacy.model.Supplies;
import ru.nsu.prokofiev.pharmacy.service.SuppliesService;

import java.util.List;

@RestController
@RequestMapping("/api/supplies")
public class SuppliesController {

    private final SuppliesService suppliesService;

    public SuppliesController(SuppliesService suppliesService) {
        this.suppliesService = suppliesService;
    }

    @GetMapping
    public List<Supplies> getAllSupplies() {
        return suppliesService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Supplies> getSupplyById(@PathVariable Long id) {
        return suppliesService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Supplies createSupply(@RequestBody Supplies supply) {
        return suppliesService.save(supply);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Supplies> updateSupply(@PathVariable Long id, @RequestBody Supplies updatedSupply) {
        return suppliesService.findById(id)
                .map(supply -> {
                    updatedSupply.setId(supply.getId());
                    return ResponseEntity.ok(suppliesService.save(updatedSupply));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupply(@PathVariable Long id) {
        if (suppliesService.findById(id).isPresent()) {
            suppliesService.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
