// src/main/java/ru/nsu/prokofiev/pharmacy/controller/SolutionsController.java
package ru.nsu.prokofiev.pharmacy.controller;

import ru.nsu.prokofiev.pharmacy.model.Solutions;
import ru.nsu.prokofiev.pharmacy.model.MedicamentPackages;
import ru.nsu.prokofiev.pharmacy.service.SolutionsService;
import ru.nsu.prokofiev.pharmacy.service.MedicamentPackagesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/solutions")
public class SolutionsController {

    private final SolutionsService solutionsService;
    private final MedicamentPackagesService packagesService;

    public SolutionsController(SolutionsService solutionsService,
                               MedicamentPackagesService packagesService) {
        this.solutionsService = solutionsService;
        this.packagesService = packagesService;
    }

    @GetMapping
    public List<Solutions> getAll() {
        return solutionsService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Solutions> getById(@PathVariable Long id) {
        return solutionsService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Solutions> create(@RequestBody Solutions incoming) {
        // Проверяем наличие вложенного medicamentPackage.id
        if (incoming.getMedicamentPackage() == null
                || incoming.getMedicamentPackage().getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Long pkgId = incoming.getMedicamentPackage().getId();

        // Загружаем управляемую сущность MedicamentPackages
        MedicamentPackages managedPkg = packagesService.findById(pkgId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Пачка раствора с ID=" + pkgId + " не найдена"));

        // Собираем новую сущность Solutions
        Solutions toSave = new Solutions();
        toSave.setMedicamentPackage(managedPkg);
        toSave.setConcentration(incoming.getConcentration());
        toSave.setPhLevel(incoming.getPhLevel());
        toSave.setVolumeMl(incoming.getVolumeMl());
        // @MapsId установит medicamentId = managedPkg.getId()

        Solutions saved = solutionsService.save(toSave);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Solutions> update(@PathVariable Long id, @RequestBody Solutions updated) {
        return solutionsService.findById(id)
                .map(existing -> {
                    // Обновляем простые поля
                    existing.setConcentration(updated.getConcentration());
                    existing.setPhLevel(updated.getPhLevel());
                    existing.setVolumeMl(updated.getVolumeMl());

                    // Если поменялась пачка — загружаем управляемую связку
                    if (updated.getMedicamentPackage() != null
                            && updated.getMedicamentPackage().getId() != null
                            && !updated.getMedicamentPackage().getId()
                            .equals(existing.getMedicamentPackage().getId())) {
                        Long newPkgId = updated.getMedicamentPackage().getId();
                        MedicamentPackages managedPkg = packagesService.findById(newPkgId)
                                .orElseThrow(() ->
                                        new IllegalArgumentException("Пачка раствора с ID=" + newPkgId + " не найдена"));
                        existing.setMedicamentPackage(managedPkg);
                        // @MapsId снова задаст medicamentId = managedPkg.getId()
                    }

                    Solutions saved = solutionsService.save(existing);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (solutionsService.findById(id).isPresent()) {
            solutionsService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
