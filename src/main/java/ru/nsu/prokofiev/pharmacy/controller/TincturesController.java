// src/main/java/ru/nsu/prokofiev/pharmacy/controller/TincturesController.java
package ru.nsu.prokofiev.pharmacy.controller;

import ru.nsu.prokofiev.pharmacy.model.Tinctures;
import ru.nsu.prokofiev.pharmacy.model.MedicamentPackages;
import ru.nsu.prokofiev.pharmacy.service.TincturesService;
import ru.nsu.prokofiev.pharmacy.service.MedicamentPackagesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/tinctures")
public class TincturesController {

    private final TincturesService tincturesService;
    private final MedicamentPackagesService packagesService;

    public TincturesController(TincturesService tincturesService,
                               MedicamentPackagesService packagesService) {
        this.tincturesService = tincturesService;
        this.packagesService = packagesService;
    }

    @GetMapping
    public List<Tinctures> getAll() {
        return tincturesService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tinctures> getById(@PathVariable Long id) {
        return tincturesService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Tinctures> create(@RequestBody Tinctures incoming) {
        // Проверяем: передан ли вложенный пакет с ID
        if (incoming.getMedicamentPackage() == null
                || incoming.getMedicamentPackage().getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Long pkgId = incoming.getMedicamentPackage().getId();

        // Извлекаем управляемую сущность medicamentPackages
        MedicamentPackages managedPkg = packagesService.findById(pkgId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Пачка настойки с ID=" + pkgId + " не найдена"));

        // Создаём новую Tinctures, заполняем поля
        Tinctures toSave = new Tinctures();
        toSave.setMedicamentPackage(managedPkg);
        toSave.setAlcoholPercentage(incoming.getAlcoholPercentage());
        toSave.setMaterial(incoming.getMaterial());
        toSave.setVolumeMl(incoming.getVolumeMl());
        // @MapsId сам назначит medicamentId = managedPkg.getId()

        Tinctures saved = tincturesService.save(toSave);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tinctures> update(@PathVariable Long id, @RequestBody Tinctures updated) {
        return tincturesService.findById(id)
                .map(existing -> {
                    // Обновляем концентрацию, материал, объем
                    existing.setAlcoholPercentage(updated.getAlcoholPercentage());
                    existing.setMaterial(updated.getMaterial());
                    existing.setVolumeMl(updated.getVolumeMl());

                    // Если поменялась пачка — вытягиваем managed-entity
                    if (updated.getMedicamentPackage() != null
                            && updated.getMedicamentPackage().getId() != null
                            && !updated.getMedicamentPackage().getId()
                            .equals(existing.getMedicamentPackage().getId())) {
                        Long newPkgId = updated.getMedicamentPackage().getId();
                        MedicamentPackages managedPkg = packagesService.findById(newPkgId)
                                .orElseThrow(() ->
                                        new IllegalArgumentException("Пачка настойки с ID=" + newPkgId + " не найдена"));
                        existing.setMedicamentPackage(managedPkg);
                        // @MapsId назначит medicamentId = managedPkg.getId()
                    }

                    Tinctures saved = tincturesService.save(existing);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (tincturesService.findById(id).isPresent()) {
            tincturesService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
