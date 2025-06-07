// src/main/java/ru/nsu/prokofiev/pharmacy/controller/PillsController.java
package ru.nsu.prokofiev.pharmacy.controller;

import ru.nsu.prokofiev.pharmacy.model.Pills;
import ru.nsu.prokofiev.pharmacy.model.MedicamentPackages;
import ru.nsu.prokofiev.pharmacy.service.PillsService;
import ru.nsu.prokofiev.pharmacy.service.MedicamentPackagesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pills")
public class PillsController {

    private final PillsService pillsService;
    private final MedicamentPackagesService packagesService;

    public PillsController(PillsService pillsService,
                           MedicamentPackagesService packagesService) {
        this.pillsService = pillsService;
        this.packagesService = packagesService;
    }

    @GetMapping
    public List<Pills> getAll() {
        return pillsService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pills> getById(@PathVariable Long id) {
        return pillsService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pills> create(@RequestBody Pills incoming) {
        // Извлекаем ID пачки из вложенного объекта
        MedicamentPackages incomingPkg = incoming.getMedicamentPackage();
        if (incomingPkg == null || incomingPkg.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Long pkgId = incomingPkg.getId();

        // Получаем «managed» MedicamentPackages
        MedicamentPackages managedPkg = packagesService.findById(pkgId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Пачка таблетки с ID=" + pkgId + " не найдена"));

        // Создаём новую сущность Pills, устанавливаем управляемую пачку, остальные поля
        Pills toSave = new Pills();
        toSave.setMedicamentPackage(managedPkg);
        toSave.setMassPerPill(incoming.getMassPerPill());
        toSave.setPillsPerPack(incoming.getPillsPerPack());
        // @MapsId автоматически присвоит medicamentId = managedPkg.getId()

        Pills saved = pillsService.save(toSave);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pills> update(@PathVariable Long id, @RequestBody Pills updated) {
        return pillsService.findById(id)
                .map(existing -> {
                    // Обновляем числовые поля
                    existing.setMassPerPill(updated.getMassPerPill());
                    existing.setPillsPerPack(updated.getPillsPerPack());

                    // Если поменялась пачка — загружаем «managed» MedicamentPackages
                    MedicamentPackages updPkg = updated.getMedicamentPackage();
                    if (updPkg != null && updPkg.getId() != null
                            && !updPkg.getId().equals(existing.getMedicamentPackage().getId())) {
                        MedicamentPackages managedPkg = packagesService.findById(updPkg.getId())
                                .orElseThrow(() ->
                                        new IllegalArgumentException("Пачка таблетки с ID=" + updPkg.getId() + " не найдена"));
                        existing.setMedicamentPackage(managedPkg);
                        // @MapsId присвоит medicamentId = managedPkg.getId()
                    }

                    Pills saved = pillsService.save(existing);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (pillsService.findById(id).isPresent()) {
            pillsService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
