package ru.nsu.prokofiev.pharmacy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.prokofiev.pharmacy.model.TechnologyComponents;
import ru.nsu.prokofiev.pharmacy.model.TechnologyComponentsId;
import ru.nsu.prokofiev.pharmacy.service.TechnologyComponentsService;

import java.util.List;

@RestController
@RequestMapping("/api/technology-components")
public class TechnologyComponentsController {

    private final TechnologyComponentsService service;

    public TechnologyComponentsController(TechnologyComponentsService service) {
        this.service = service;
    }

    @GetMapping
    public List<TechnologyComponents> getAll() {
        return service.findAll();
    }

    @GetMapping("/{technologyId}/{componentId}")
    public ResponseEntity<TechnologyComponents> getById(@PathVariable Long technologyId, @PathVariable Long componentId) {
        TechnologyComponentsId id = new TechnologyComponentsId(technologyId, componentId);
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TechnologyComponents> create(@RequestBody TechnologyComponents component) {
        if (component.getTechnology() == null || component.getComponent() == null) {
            return ResponseEntity.badRequest().build();
        }

        // Устанавливаем EmbeddedId, если не установлен
        if (component.getId() == null) {
            component.setId(new TechnologyComponentsId(
                    component.getTechnology().getId(),
                    component.getComponent().getId()
            ));
        }

        try {
            return ResponseEntity.ok(service.save(component));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{technologyId}/{componentId}")
    public ResponseEntity<TechnologyComponents> update(
            @PathVariable Long technologyId,
            @PathVariable Long componentId,
            @RequestBody TechnologyComponents updated
    ) {
        TechnologyComponentsId id = new TechnologyComponentsId(technologyId, componentId);
        return service.findById(id)
                .map(existing -> {
                    updated.setId(id);
                    return ResponseEntity.ok(service.save(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{technologyId}/{componentId}")
    public ResponseEntity<Void> delete(@PathVariable Long technologyId, @PathVariable Long componentId) {
        TechnologyComponentsId id = new TechnologyComponentsId(technologyId, componentId);
        if (service.findById(id).isPresent()) {
            service.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
