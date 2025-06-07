// src/main/java/ru/nsu/prokofiev/pharmacy/controller/PowdersController.java
package ru.nsu.prokofiev.pharmacy.controller;

import ru.nsu.prokofiev.pharmacy.model.Powders;
import ru.nsu.prokofiev.pharmacy.model.MedicamentPackages;
import ru.nsu.prokofiev.pharmacy.service.PowdersService;
import ru.nsu.prokofiev.pharmacy.service.MedicamentPackagesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/powders")
public class PowdersController {

    private final PowdersService powdersService;
    private final MedicamentPackagesService packagesService;

    public PowdersController(PowdersService powdersService,
                             MedicamentPackagesService packagesService) {
        this.powdersService = powdersService;
        this.packagesService = packagesService;
    }

    @GetMapping
    public List<Powders> getAll() {
        return powdersService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Powders> getById(@PathVariable Long id) {
        return powdersService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Powders> create(@RequestBody Powders incoming) {
        // Извлекаем ID пачки из вложенного объекта
        MedicamentPackages incomingPkg = incoming.getMedicamentPackage();
        if (incomingPkg == null || incomingPkg.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Long pkgId = incomingPkg.getId();

        // Получаем «managed» MedicamentPackages
        MedicamentPackages managedPkg = packagesService.findById(pkgId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Пачка порошка с ID=" + pkgId + " не найдена"));

        // Создаём новую сущность Powders, устанавливаем управляемую пачку, остальные поля
        Powders toSave = new Powders();
        toSave.setMedicamentPackage(managedPkg);
        toSave.setIsCompound(incoming.getIsCompound());
        toSave.setPackageWeight(incoming.getPackageWeight());
        // @MapsId автоматически присвоит medicamentId = managedPkg.getId()

        Powders saved = powdersService.save(toSave);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Powders> update(@PathVariable Long id, @RequestBody Powders updated) {
        return powdersService.findById(id)
                .map(existing -> {
                    // Обновляем булево поле и вес упаковки
                    existing.setIsCompound(updated.getIsCompound());
                    existing.setPackageWeight(updated.getPackageWeight());

                    // Если поменялась пачка — загружаем «managed» MedicamentPackages
                    MedicamentPackages updPkg = updated.getMedicamentPackage();
                    if (updPkg != null && updPkg.getId() != null
                            && !updPkg.getId().equals(existing.getMedicamentPackage().getId())) {
                        MedicamentPackages managedPkg = packagesService.findById(updPkg.getId())
                                .orElseThrow(() ->
                                        new IllegalArgumentException("Пачка порошка с ID=" + updPkg.getId() + " не найдена"));
                        existing.setMedicamentPackage(managedPkg);
                        // @MapsId присвоит medicamentId = managedPkg.getId()
                    }

                    Powders saved = powdersService.save(existing);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (powdersService.findById(id).isPresent()) {
            powdersService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
