package ru.nsu.prokofiev.pharmacy.controller;

import ru.nsu.prokofiev.pharmacy.model.Mixtures;
import ru.nsu.prokofiev.pharmacy.model.MedicamentPackages;
import ru.nsu.prokofiev.pharmacy.service.MixturesService;
import ru.nsu.prokofiev.pharmacy.service.MedicamentPackagesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mixtures")
public class MixturesController {

    private final MixturesService mixturesService;
    private final MedicamentPackagesService packagesService;

    public MixturesController(MixturesService mixturesService,
                              MedicamentPackagesService packagesService) {
        this.mixturesService = mixturesService;
        this.packagesService = packagesService;
    }

    @GetMapping
    public List<Mixtures> getAll() {
        return mixturesService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mixtures> getById(@PathVariable Long id) {
        return mixturesService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Mixtures> create(@RequestBody Mixtures incoming) {
        // Проверяем, что в JSON пришёл только ID пачки (medicamentId) или вложенный объект с полем id
        Long pkgId;
        MedicamentPackages incomingPkg = incoming.getMedicamentPackage();
        if (incomingPkg != null) {
            pkgId = incomingPkg.getId();
        } else {
            pkgId = null;
        }
        // Если ID не передали, возвращаем 400
        if (pkgId == null) {
            return ResponseEntity.badRequest().build();
        }

        // Извлекаем пачку из базы (managed-entity)
        MedicamentPackages managedPkg = packagesService.findById(pkgId)
                .orElseThrow(() -> new IllegalArgumentException("Пачка с ID=" + pkgId + " не найдена"));

        // Создаём новую Mixtures, заполняем поля вручную
        Mixtures toSave = new Mixtures();
        toSave.setMedicamentPackage(managedPkg);
        toSave.setSolvent(incoming.getSolvent());
        toSave.setSugarFree(incoming.getSugarFree());
        toSave.setVolumeMl(incoming.getVolumeMl());
        // Здесь не вызываем setMedicamentId — @MapsId сам подхватит ID из managedPkg

        Mixtures saved = mixturesService.save(toSave);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mixtures> update(@PathVariable Long id, @RequestBody Mixtures updated) {
        return mixturesService.findById(id)
                .map(existing -> {
                    // Обновляем простые поля
                    existing.setSolvent(updated.getSolvent());
                    existing.setSugarFree(updated.getSugarFree());
                    existing.setVolumeMl(updated.getVolumeMl());

                    // Если нужно поменять пачку — вытягиваем managed-entity пакета
                    Long newPkgId;
                    MedicamentPackages updatedPkg = updated.getMedicamentPackage();
                    if (updatedPkg != null) {
                        newPkgId = updatedPkg.getId();
                    } else {
                        newPkgId = null;
                    }
                    if (newPkgId != null && !newPkgId.equals(existing.getMedicamentPackage().getId())) {
                        MedicamentPackages managedPkg = packagesService.findById(newPkgId)
                                .orElseThrow(() -> new IllegalArgumentException("Пачка с ID=" + newPkgId + " не найдена"));
                        existing.setMedicamentPackage(managedPkg);
                        // @MapsId подхватит medicamentId = managedPkg.getId()
                    }

                    Mixtures saved = mixturesService.save(existing);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (mixturesService.findById(id).isPresent()) {
            mixturesService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
