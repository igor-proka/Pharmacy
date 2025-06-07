// src/main/java/ru/nsu/prokofiev/pharmacy/controller/PrescriptionsContentController.java
package ru.nsu.prokofiev.pharmacy.controller;

import ru.nsu.prokofiev.pharmacy.model.PrescriptionsContent;
import ru.nsu.prokofiev.pharmacy.model.PrescriptionsContentId;
import ru.nsu.prokofiev.pharmacy.model.Prescriptions;
import ru.nsu.prokofiev.pharmacy.model.Medicaments;
import ru.nsu.prokofiev.pharmacy.model.WaysOfUse;
import ru.nsu.prokofiev.pharmacy.service.PrescriptionsContentService;
import ru.nsu.prokofiev.pharmacy.service.PrescriptionsService;
import ru.nsu.prokofiev.pharmacy.service.MedicamentsService;
import ru.nsu.prokofiev.pharmacy.service.WaysOfUseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions-content")
public class PrescriptionsContentController {

    private final PrescriptionsContentService contentService;
    private final PrescriptionsService prescriptionsService;
    private final MedicamentsService medicamentsService;
    private final WaysOfUseService waysService;

    public PrescriptionsContentController(PrescriptionsContentService contentService,
                                          PrescriptionsService prescriptionsService,
                                          MedicamentsService medicamentsService,
                                          WaysOfUseService waysService) {
        this.contentService = contentService;
        this.prescriptionsService = prescriptionsService;
        this.medicamentsService = medicamentsService;
        this.waysService = waysService;
    }

    @GetMapping
    public List<PrescriptionsContent> getAll() {
        return contentService.findAll();
    }

    @GetMapping("/id")
    public ResponseEntity<PrescriptionsContent> getById(
            @RequestParam Long prescriptionId,
            @RequestParam Long medicamentId,
            @RequestParam Long amount,
            @RequestParam Long wayToUse
    ) {
        PrescriptionsContentId id = new PrescriptionsContentId(prescriptionId, medicamentId, amount, wayToUse);
        return contentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PrescriptionsContent> create(@RequestBody PrescriptionsContent incoming) {
        // Берём IDs из вложенных объектов
        Long presId = incoming.getPrescription().getId();
        Long medId = incoming.getMedicament().getId();
        Long wayId = incoming.getWayOfUse().getId();
        Long amount = incoming.getAmount().longValue();

        // Загружаем управляемые сущности
        Prescriptions pres = prescriptionsService.findById(presId)
                .orElseThrow(() -> new IllegalArgumentException("Рецепт не найден: " + presId));
        Medicaments med = medicamentsService.findById(medId)
                .orElseThrow(() -> new IllegalArgumentException("Медикамент не найден: " + medId));
        WaysOfUse way = waysService.findById(wayId)
                .orElseThrow(() -> new IllegalArgumentException("Способ применения не найден: " + wayId));

        // Собираем составной PK
        PrescriptionsContentId newId = new PrescriptionsContentId(presId, medId, amount, wayId);

        PrescriptionsContent toSave = new PrescriptionsContent();
        toSave.setId(newId);
        toSave.setPrescription(pres);
        toSave.setMedicament(med);
        toSave.setWayOfUse(way);

        PrescriptionsContent saved = contentService.save(toSave);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/id")
    public ResponseEntity<PrescriptionsContent> update(
            @RequestParam Long prescriptionId,
            @RequestParam Long medicamentId,
            @RequestParam Long amount,
            @RequestParam Long wayToUse,
            @RequestBody PrescriptionsContent updated
    ) {
        PrescriptionsContentId oldId = new PrescriptionsContentId(prescriptionId, medicamentId, amount, wayToUse);

        return contentService.findById(oldId)
                .map(existing -> {
                    // Из пришедшего JSON достаём новые ID (может потребоваться Long -> toLong())
                    Long newPresId = updated.getPrescription().getId();
                    Long newMedId = updated.getMedicament().getId();
                    Long newWayId = updated.getWayOfUse().getId();
                    Long newAmount = updated.getAmount().longValue();

                    boolean pkChanged =
                            !prescriptionId.equals(newPresId) ||
                                    !medicamentId.equals(newMedId) ||
                                    !amount.equals(newAmount) ||
                                    !wayToUse.equals(newWayId);

                    if (pkChanged) {
                        // 1) удаляем старую запись
                        contentService.delete(oldId);

                        // 2) создаём новую с новыми PK
                        Prescriptions pres = prescriptionsService.findById(newPresId)
                                .orElseThrow(() ->
                                        new IllegalArgumentException("Рецепт не найден: " + newPresId));
                        Medicaments med = medicamentsService.findById(newMedId)
                                .orElseThrow(() ->
                                        new IllegalArgumentException("Медикамент не найден: " + newMedId));
                        WaysOfUse way = waysService.findById(newWayId)
                                .orElseThrow(() ->
                                        new IllegalArgumentException("Способ применения не найден: " + newWayId));

                        PrescriptionsContentId newId = new PrescriptionsContentId(newPresId, newMedId, newAmount, newWayId);
                        PrescriptionsContent fresh = new PrescriptionsContent();
                        fresh.setId(newId);
                        fresh.setPrescription(pres);
                        fresh.setMedicament(med);
                        fresh.setWayOfUse(way);
                        PrescriptionsContent savedNew = contentService.save(fresh);
                        return ResponseEntity.ok(savedNew);

                    } else {
                        // PK не изменился, просто меняем ассоциации
                        Prescriptions pres = prescriptionsService.findById(newPresId)
                                .orElseThrow(() ->
                                        new IllegalArgumentException("Рецепт не найден: " + newPresId));
                        Medicaments med = medicamentsService.findById(newMedId)
                                .orElseThrow(() ->
                                        new IllegalArgumentException("Медикамент не найден: " + newMedId));
                        WaysOfUse way = waysService.findById(newWayId)
                                .orElseThrow(() ->
                                        new IllegalArgumentException("Способ применения не найден: " + newWayId));

                        existing.setPrescription(pres);
                        existing.setMedicament(med);
                        existing.setWayOfUse(way);
                        // Важно: amount и wayToUse как PK не меняются здесь

                        PrescriptionsContent saved = contentService.save(existing);
                        return ResponseEntity.ok(saved);
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/id")
    public ResponseEntity<Void> delete(
            @RequestParam Long prescriptionId,
            @RequestParam Long medicamentId,
            @RequestParam Long amount,
            @RequestParam Long wayToUse
    ) {
        PrescriptionsContentId id = new PrescriptionsContentId(prescriptionId, medicamentId, amount, wayToUse);
        if (contentService.findById(id).isPresent()) {
            contentService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
