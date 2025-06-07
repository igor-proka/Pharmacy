// src/main/java/ru/nsu/prokofiev/pharmacy/controller/OintmentsController.java
package ru.nsu.prokofiev.pharmacy.controller;

import ru.nsu.prokofiev.pharmacy.model.Ointments;
import ru.nsu.prokofiev.pharmacy.model.MedicamentPackages;
import ru.nsu.prokofiev.pharmacy.service.OintmentsService;
import ru.nsu.prokofiev.pharmacy.service.MedicamentPackagesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ointments")
public class OintmentsController {

    private final OintmentsService ointmentsService;
    private final MedicamentPackagesService packagesService;

    public OintmentsController(OintmentsService ointmentsService,
                               MedicamentPackagesService packagesService) {
        this.ointmentsService = ointmentsService;
        this.packagesService = packagesService;
    }

    @GetMapping
    public List<Ointments> getAll() {
        return ointmentsService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ointments> getById(@PathVariable Long id) {
        return ointmentsService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Ointments> create(@RequestBody Ointments incoming) {
        // Проверяем наличие вложенного medicamentPackage.id
        if (incoming.getMedicamentPackage() == null
                || incoming.getMedicamentPackage().getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Long pkgId = incoming.getMedicamentPackage().getId();

        // Загружаем «managed» MedicamentPackages
        MedicamentPackages managedPkg = packagesService.findById(pkgId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Пачка мази с ID=" + pkgId + " не найдена"));

        // Создаем новую сущность Ointments
        Ointments toSave = new Ointments();
        toSave.setMedicamentPackage(managedPkg);
        toSave.setActiveIngredient(incoming.getActiveIngredient());
        toSave.setVolumeMl(incoming.getVolumeMl());
        // @MapsId автоматически присвоит medicamentId = managedPkg.getId()

        Ointments saved = ointmentsService.save(toSave);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ointments> update(@PathVariable Long id, @RequestBody Ointments updated) {
        return ointmentsService.findById(id)
                .map(existing -> {
                    // Обновляем простые поля
                    existing.setActiveIngredient(updated.getActiveIngredient());
                    existing.setVolumeMl(updated.getVolumeMl());

                    // Если поменялась пачка — загружаем «managed» MedicamentPackages
                    if (updated.getMedicamentPackage() != null
                            && updated.getMedicamentPackage().getId() != null
                            && !updated.getMedicamentPackage().getId()
                            .equals(existing.getMedicamentPackage().getId())) {
                        Long newPkgId = updated.getMedicamentPackage().getId();
                        MedicamentPackages managedPkg = packagesService.findById(newPkgId)
                                .orElseThrow(() ->
                                        new IllegalArgumentException("Пачка мази с ID=" + newPkgId + " не найдена"));
                        existing.setMedicamentPackage(managedPkg);
                        // @MapsId присвоит medicamentId = managedPkg.getId()
                    }

                    Ointments saved = ointmentsService.save(existing);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (ointmentsService.findById(id).isPresent()) {
            ointmentsService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
